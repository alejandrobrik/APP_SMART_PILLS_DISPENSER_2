package com.uteq.app_smart_pills_dispenser.services;



import com.uteq.app_smart_pills_dispenser.models.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PatientService {
    @GET("api/carer/patients/{id}")
    Call<List<Patient>> getPatient(@Path("id") String id);

    @POST("api/carer/patients/2")
    Call<Patient>addPatient(@Body Patient patient);
}
