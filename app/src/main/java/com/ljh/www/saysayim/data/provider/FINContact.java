package com.ljh.www.saysayim.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.collect.Lists;
import com.ljh.www.imkit.util.log.LogUtils;

/**
 * Created by ljh on 2016/6/30.
 */
public class FINContact {
    private static final String TAG = LogUtils.makeLogTag(FINContact.class.getSimpleName());
    public static final String CONTENT_AUTHORITY = "com.ljh.www.saysayim";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public interface FundColumns {
        String FUND_ID = "fund_id";
        String FUND_CODE = "fund_code";
        String BUY_PRICE = "buy_price";
        String SELL_PRICE = "sell_price";
        String PUBLISH_DATETIME = "publish_datetime";
    }

    public interface ShareColumns {
        String SHARE_ID = "share_id";
        String SHARE_CODE = "share_code";
        String PUBLISH_DATETIME = "publish_datetime";
    }

    public interface FinanceColumns {
        String FINANCE_ID = "finance_id";
        String TOTAL_SUM = "total_sum";
    }

    private static final String PATH_FUNDS = "funds";

    public static class Funds implements BaseColumns, FundColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.saysayim.funds";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.saysayim.funds";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FUNDS).build();

        public static Uri buildFundUri(String fundId) {
            return CONTENT_URI.buildUpon().appendPath(fundId).build();
        }

        public static String getFundId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    public static class Shares implements BaseColumns, ShareColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.saysayim.shares";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.saysayim.shares";
    }
}
