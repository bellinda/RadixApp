package com.angelova.w510.radixapp.services;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by W510 on 28.1.2018 г..
 */

public interface FileUploadService {
    @Multipart
    @POST("/inquiries/mobile")
    Call<ResponseBody> upload(
            @PartMap() Map<String, RequestBody> descriptions,
            @Part List<MultipartBody.Part> files
    );
}