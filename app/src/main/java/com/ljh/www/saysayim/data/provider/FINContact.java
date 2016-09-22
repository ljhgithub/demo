package com.ljh.www.saysayim.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

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

    public interface SharePercentColumns {
        //        "code":"000002",		/*股票代码*/
//                "name":"万科A",			/*股票名称*/
//                "fundnum":"180",		/*持有基金家数*/
//                "total":"247,221",		/*持股总数（万股）*/
//                "change":"-42,289",		/*持股变化（和上季度比）*/
//                "totalcap":"2084074.79",	/*持股总市值（万元）*/
//                "accrate":"22.52",		/*占该股流通市值比例（%）*/
//                "changesta":"-3.85",		/*占流通股比例表变化（和上季度比）（%）*/
//                "time":"20120930"		/*时间*/
        String CODE = "code";
        String NAME = "name";
        String FUND_NUM = "fund_num";
        String TOTAL = "total";
        String CHANGE = "change";
        String TOTAL_CAP = "total_cap";
        String ACC_RATE = "acc_rate";
        String CHANGE_STATUS = "change_status";
        String TIME = "time";
    }

    private static final String PATH_FUNDS = "funds";
    private static final String PATH_SHARE_PERCENT = "share_percent";

    public static class Funds implements BaseColumns, FundColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.saysayim.funds";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.saysayim.funds";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FUNDS).build();

        public static Uri buildFundUri(String fundCode) {
            return CONTENT_URI.buildUpon().appendPath("id").appendPath(fundCode).build();
        }


        public static String getFundId(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

    public static class SharePercent implements BaseColumns, SharePercentColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.saysayim.sharepercent";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.saysayim.sharepercent";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHARE_PERCENT).build();

        public static Uri buildSharePercentLimitUri(int start, int offset) {
            return CONTENT_URI.buildUpon().appendPath("limit").appendPath(start + "").appendPath(offset + "").build();
        }

        public static Uri buildSharePercentUri(String shareCode) {
            return CONTENT_URI.buildUpon().appendPath("id").appendPath(shareCode).build();
        }

        public static String getShareCode(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

    public static class Shares implements BaseColumns, ShareColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.saysayim.shares";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.saysayim.shares";
    }
}
