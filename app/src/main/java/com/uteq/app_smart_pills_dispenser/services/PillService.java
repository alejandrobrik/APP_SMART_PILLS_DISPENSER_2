package com.uteq.app_smart_pills_dispenser.services;

import com.uteq.app_smart_pills_dispenser.models.Pill;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PillService {
    @GET("api/pill")
    Call<List<Pill>> getPill();

    @POST("api/pill")
    Call<Pill>addPill(@Body Pill pill);
}
