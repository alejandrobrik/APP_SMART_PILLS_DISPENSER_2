package com.uteq.app_smart_pills_dispenser.services;

import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MedicalTreatmentService {
    @GET("api/patient/medical-treatments/{id}")
    Call<List<MedicalTreatment>> getMedicalTreatment(@Path("id") String id);

    @POST("api/medical-treatment")
    Call<MedicalTreatment>addMedicalTreatment(@Body MedicalTreatment medicalTreatment);

    @GET("api/medical-treatment/max-id")
    Call<String>getMedicalTreatmentLastId();
}
