package com.uteq.app_smart_pills_dispenser.services;

import com.uteq.app_smart_pills_dispenser.models.Dosage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DosageService {

    @GET("api/dosage")
    Call<List<Dosage>> getDosage();

    @POST("api/dosage")
    Call<Dosage> addDosage(@Body Dosage dosage);
}

