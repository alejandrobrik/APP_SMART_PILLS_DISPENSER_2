package com.uteq.app_smart_pills_dispenser.services;


import com.uteq.app_smart_pills_dispenser.models.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DoctorService {
    @GET("api/doctor")
    Call<List<Doctor>> getDoctors();

    @POST("api/doctor")
    Call<Doctor> addDoctor(@Body Doctor doctor);
}