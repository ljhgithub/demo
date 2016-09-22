package com.ljh.www.saysayim.data.remote;

import com.ljh.www.saysayim.model.JuheRemoteDataSource;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by ljh on 2016/6/30.
 */
public interface RemoteDataService {
    @GET
    public Observable<JuheRemoteDataSource> getFundZCGJJ(
            @Url String url,
            @Query("key") String Appkey);

    @GET
    public Observable<ResponseBody> testSpdy(
            @Url String url
    );

}
