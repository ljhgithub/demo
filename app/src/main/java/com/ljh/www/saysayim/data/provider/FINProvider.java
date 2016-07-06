package com.ljh.www.saysayim.data.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.common.collect.Tables;
import com.ljh.www.imkit.util.log.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ljh on 2016/6/30.
 */
public class FINProvider extends ContentProvider {
    private static final String TAG = LogUtils.makeLogTag(FINContact.class.getSimpleName());
    private FINDatabase mOpenHelper;
    private static UriMatcher mUriMatcher = buildUriMatcher();

    private static final int FUNDS = 100;
    private static final int FUNDS_ID = 101;

    private static final int SHARES = 200;
    private static final int SHARES_ID = 201;
    private static final int SHARE_PERCENT = 300;
    private static final int SHARE_PERCENT_ID = 301;
    @Override
    public boolean onCreate() {
        mOpenHelper = new FINDatabase(getContext());
        return true;
    }

    private void deleteDatabase() {
        // TODO: wait for content provider operations to finish, then tear down
        mOpenHelper.close();
        Context context = getContext();
        FINDatabase.deleteDatabase(context);
        mOpenHelper = new FINDatabase(getContext());
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = mUriMatcher.match(uri);
        LogUtils.LOGI(TAG, "uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");
        final SelectionBuilder builder = buildExpandedSelection(uri, match);

        boolean distinct = true;
        Cursor cursor = builder
                .where(selection, selectionArgs)
                .query(db, distinct, projection, sortOrder, null);
        Context context = getContext();
        if (null != context) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        LogUtils.LOGD(TAG, "query count " + cursor.getCount());
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case FUNDS:
                return FINContact.Funds.CONTENT_TYPE;
            case FUNDS_ID:
                return FINContact.Funds.CONTENT_ITEM_TYPE;
            case SHARES:
                return FINContact.Funds.CONTENT_TYPE;
            case SHARES_ID:
                return FINContact.Funds.CONTENT_TYPE;
            case SHARE_PERCENT:
                return FINContact.SharePercent.CONTENT_TYPE;
            case SHARE_PERCENT_ID:
                return FINContact.Funds.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LogUtils.LOGI(TAG, "insert(uri=" + uri + ", values=" + values.toString() + " )");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case FUNDS: {
                db.insertOrThrow(FINDatabase.Tables.FUNDS, null, values);
                notifyChange(uri);
                return FINContact.Funds.buildFundUri(values.getAsString(FINContact.Funds.FUND_ID));
            }
            case SHARES: {
                db.insertOrThrow(FINDatabase.Tables.SHARES, null, values);
                return uri;
            }
            case SHARE_PERCENT: {
                db.insertOrThrow(FINDatabase.Tables.SHARE_PERCENT, null, values);
                notifyChange(uri);
                return FINContact.SharePercent.buildSharePercentUri(values.getAsString(FINContact.SharePercent.CODE));
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LogUtils.LOGI(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return retVal;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = mUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case FUNDS: {
                tableName=FINDatabase.Tables.FUNDS;
                break;
            }
            case SHARES: {
                tableName=FINDatabase.Tables.SHARES;
                break;
            }
            case SHARE_PERCENT: {
                tableName=FINDatabase.Tables.SHARE_PERCENT;
             break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = values.length;
            for (int i = 0; i < count; i++) {
                if (db.insert(tableName, null, values[i]) < 0) {
                    return 0;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return values.length;
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FINContact.CONTENT_AUTHORITY;

        matcher.addURI(authority, "funds", FUNDS);
        matcher.addURI(authority, "funds/*", FUNDS_ID);
        matcher.addURI(authority, "shares", SHARES);
        matcher.addURI(authority, "shares/*", SHARES_ID);
        matcher.addURI(authority, "share_percent", SHARE_PERCENT);
        matcher.addURI(authority, "share_percent/*", SHARE_PERCENT_ID);

        return matcher;
    }

    private void notifyChange(Uri uri) {
        //这里可以对指定uri做数据改动通知
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);

    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case FUNDS: {
                return builder.table(FINDatabase.Tables.FUNDS);
            }
            case FUNDS_ID: {
                final String fundId = FINContact.Funds.getFundId(uri);
                return builder.table(FINDatabase.Tables.FUNDS)
                        .where(FINContact.Funds.FUND_ID + "=?", fundId);
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
            }
        }
    }

    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case FUNDS: {
                return builder.table(FINDatabase.Tables.FUNDS);
            }
            case SHARE_PERCENT: {

//                final List<String> segments = uri.getPathSegments();
                return builder.table(FINDatabase.Tables.SHARE_PERCENT)
                        .where(FINContact.SharePercent.FUND_NUM + ">=?", "400");
//                return builder.table(FINDatabase.Tables.SHARE_PERCENT);
            }
//            case BLOCKS_BETWEEN: {
//                final List<String> segments = uri.getPathSegments();
//                final String startTime = segments.get(2);
//                final String endTime = segments.get(3);
//                return builder.table(Tables.BLOCKS)
//                        .where(Blocks.BLOCK_START + ">=?", startTime)
//                        .where(Blocks.BLOCK_START + "<=?", endTime);
//            }
            case FUNDS_ID: {
                final String fundId = FINContact.Funds.getFundId(uri);
                return builder.table(FINDatabase.Tables.FUNDS)
                        .where(FINContact.Funds.FUND_ID + "=?", fundId);
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

}
