package com.ccdp.appkebersihan5.api;

/**
 * Created by Susi on 3/14/2017.
 */
import com.ccdp.appkebersihan5.model.Kebersihan;
import com.ccdp.appkebersihan5.model.KebersihanResult;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DatasAPI {

    @GET("datas/all")
    Call<KebersihanResult> all(@Query("token") String token);

    @GET("datas/find")
    Call<KebersihanResult> find(@Query("id") int id,@Query("token") String token);

    @FormUrlEncoded
    @POST("datas/insert")
    Call<Kebersihan> insert(@Field("keterangan") String Keterangan,
                            @Field("userid") String Userid,
                            @Field("tgllapor") String Tgllapor,
                            @Field("lokasi") String Lokasi,
                            @Field("token") String token);

    @FormUrlEncoded
    @POST("datas/update")
    Call<Kebersihan> update(@Field("keterangan") String Keterangan,
                            @Field("userid") String Userid,
                            @Field("tgllapor") String Tgllapor,
                            @Field("lokasi") String Lokasi,
                            @Field("id") int id,
                            @Field("token") String token);

    @FormUrlEncoded
    @POST("datas/delete")
    Call<Kebersihan> delete(@Field("id") int id,@Field("token") String token);

    @Multipart
    @POST("datas/upload")
    Call<ResponseBody> upload(@Part("id") int id,@Part MultipartBody.Part file,@Part("token") String token);



}

