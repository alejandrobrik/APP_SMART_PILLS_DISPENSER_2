package com.uteq.app_smart_pills_dispenser.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Body;

@Data
@NoArgsConstructor
public class Dosage implements Serializable {
    private String id;
    private int quantity;
    private  String prescription;
    private String dateHour;
    private  String dateTake;
    private String registrationDate;
    private Boolean state;
    private Pill pill;
    private MedicalTreatment medicalTreatment;

}
