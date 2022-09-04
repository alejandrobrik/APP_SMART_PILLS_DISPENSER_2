package com.uteq.app_smart_pills_dispenser.services;

import com.uteq.app_smart_pills_dispenser.models.Dosage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DosageService {

    @GET("api/dosage")
    Call<List<Dosage>> getDosage();
}

