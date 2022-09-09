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
    private String date_hour;
    private  String date_dosage;
    private String registration_date;
    private Boolean state;
    private Pill pill;
    private MedicalTreatment medicalTreatment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getDate_hour() {
        return date_hour;
    }

    public void setDate_hour(String date_hour) {
        this.date_hour = date_hour;
    }

    public String getDate_dosage() {
        return date_dosage;
    }

    public void setDate_dosage(String date_dosage) {
        this.date_dosage = date_dosage;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Pill getPill() {
        return pill;
    }

    public void setPill(Pill pill) {
        this.pill = pill;
    }

    public MedicalTreatment getMedicalTreatment() {
        return medicalTreatment;
    }

    public void setMedicalTreatment(MedicalTreatment medicalTreatment) {
        this.medicalTreatment = medicalTreatment;
    }
}
