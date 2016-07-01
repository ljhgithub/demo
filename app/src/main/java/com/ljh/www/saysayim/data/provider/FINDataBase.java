package com.ljh.www.saysayim.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.provider.BaseColumns;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.data.provider.FINContact.*;

import java.security.acl.LastOwnerException;

/**
 * Created by ljh on 2016/6/30.
 */
public class FINDatabase extends SQLiteOpenHelper {
    private static final String TAG = LogUtils.makeLogTag(FINDatabase.class.getSimpleName());
    private static final String DATABASE_NAME = "finance.db";

    private static final int VER_100_RELEASE_A = 100;
    private static final int VER_200_RELEASE_C = 200;
    private static final int CUR_DATABASE_VERSION = VER_200_RELEASE_C;
    private Context mContext;

    public FINDatabase(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        this.mContext = context;
    }

    interface Tables {
        String FUNDS = "funds";
        String SHARES = "shares";
        String FINANCES = "finances";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.LOGD(TAG, "CREATE TABLES");
        db.execSQL("CREATE TABLE " + Tables.FUNDS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FundColumns.FUND_ID + " TEXT NOT NULL,"
                + FundColumns.FUND_CODE + " INTEGER NOT NULL,"
                + FundColumns.BUY_PRICE + " TEXT,"
                + FundColumns.SELL_PRICE + " TEXT,"
                + FundColumns.PUBLISH_DATETIME + " TEXT,"
                + "UNIQUE (" + FundColumns.FUND_ID + ") ON CONFLICT REPLACE)");
        db.execSQL("CREATE TABLE " + Tables.FINANCES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FinanceColumns.FINANCE_ID + " TEXT NOT NULL,"
                + FinanceColumns.TOTAL_SUM + " INTEGER,"
                + "UNIQUE (" + FinanceColumns.FINANCE_ID + ") ON CONFLICT REPLACE)");

        upgradeAtoC(db);

    }

    private void upgradeAtoC(SQLiteDatabase db) {
        LogUtils.LOGD(TAG, "upgradeAtoC ");
        db.execSQL("CREATE TABLE " + Tables.SHARES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ShareColumns.SHARE_ID + " TEXT NOT NULL,"
                + ShareColumns.SHARE_CODE + " INTEGER NOT NULL,"
                + ShareColumns.PUBLISH_DATETIME + " TEXT,"
                + "UNIQUE (" + ShareColumns.SHARE_ID + ") ON CONFLICT REPLACE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.LOGD(TAG, oldVersion + "onUpgrade " + newVersion);
        int version = oldVersion;
        if (version == VER_100_RELEASE_A) {
            upgradeAtoC(db);
            version = VER_200_RELEASE_C;
        }

        if (version != CUR_DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.FINANCES);
            onCreate(db);
            version = CUR_DATABASE_VERSION;
        }

    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
