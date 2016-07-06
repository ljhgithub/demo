package com.ljh.www.saysayim.main.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.ljh.www.imkit.util.log.LogUtils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ljh on 2016/7/3.
 */
public class SharePercentModel implements Parcelable {
    private static final String TAG = LogUtils.makeLogTag(SharePercentModel.class.getSimpleName());
    public String code;
    public String name;
    public String fundnum;
    private String total;
    private String change;
    public String totalcap;
    public String accrate;
    public String changesta;
    public String time;

    public void setChange(String change) {
        this.change = change;
    }

    public String getChange() {
        if (!TextUtils.isEmpty(change)) {
            return change.replace(",", "");
        }
        return change;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        if (!TextUtils.isEmpty(total)) {
            return total.replace(",", "");
        }
        return total;
    }

    public SharePercentModel() {
    }


    protected SharePercentModel(Parcel in) {

        code = in.readString();
        name = in.readString();
        fundnum = in.readString();
        total = in.readString();
        change = in.readString();
        totalcap = in.readString();
        accrate = in.readString();
        changesta = in.readString();
        time = in.readString();
    }

    public static final Creator<SharePercentModel> CREATOR = new Creator<SharePercentModel>() {
        @Override
        public SharePercentModel createFromParcel(Parcel in) {
            return new SharePercentModel(in);
        }

        @Override
        public SharePercentModel[] newArray(int size) {
            return new SharePercentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(fundnum);
        dest.writeString(total);
        dest.writeString(change);
        dest.writeString(totalcap);
        dest.writeString(accrate);
        dest.writeString(changesta);
        dest.writeString(time);
    }

    public static SharePercentModel buildFrom(JsonObject jsonObject) {
        return new Gson().fromJson(jsonObject, SharePercentModel.class);
    }

    public static List<SharePercentModel> buildListFrom(JsonArray jsonArray) {
        List<SharePercentModel> lst = Lists.newArrayList();
        JsonObject element = (JsonObject) jsonArray.get(0);
        Iterator<Map.Entry<String, JsonElement>> it = element.entrySet().iterator();
        Gson gson = new GsonBuilder().create();
        while (it.hasNext()) {
            SharePercentModel m = gson.fromJson(it.next().getValue(), SharePercentModel.class);
            lst.add(m);
        }

        return lst;
    }

}
