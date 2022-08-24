package com.uteq.app_smart_pills_dispenser.services;

import com.uteq.app_smart_pills_dispenser.models.Carer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CarerService {
    @GET("api/carer")
    Call<List<Carer>>getCarer();

    @POST("api/carer")
    Call<Carer>addCarer(@Body Carer carer);
}
