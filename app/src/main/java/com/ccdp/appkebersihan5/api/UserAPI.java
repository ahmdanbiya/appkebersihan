package com.ccdp.appkebersihan5.api;

/**
 * Created by Susi on 3/14/2017.
 */

import com.ccdp.appkebersihan5.model.LoginResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserAPI {
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResult> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/logout")
    Call<LoginResult> logout(@Field("token") String token);
}
