package com.uteq.app_smart_pills_dispenser.services;


import com.uteq.app_smart_pills_dispenser.models.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DoctorService {
    @GET("api/doctor")
    Call<List<Doctor>> getDoctors();
}