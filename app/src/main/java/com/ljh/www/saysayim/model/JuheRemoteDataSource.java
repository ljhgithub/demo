package com.ljh.www.saysayim.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

/**
 * Created by ljh on 2016/7/3.
 */
public class JuheRemoteDataSource {
    public static String JUHE_SUCCESS="200";
    public String resultcode;
    public JsonArray result;
    public String reason;
        @SerializedName("error_code")
    public int errorCode = -1;
}
