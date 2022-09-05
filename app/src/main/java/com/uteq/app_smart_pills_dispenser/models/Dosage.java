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
    private String registration_date;
    private String endDate;
    private String starDate;
    private int quantity;
    private Boolean state;
    private String hour;
    private MedicalTreatment medicalTreatment;
    private Pill pill;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStarDate() {
        return starDate;
    }

    public void setStarDate(String starDate) {
        this.starDate = starDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public MedicalTreatment getMedicalTreatment() {
        return medicalTreatment;
    }

    public void setMedicalTreatment(MedicalTreatment medicalTreatment) {
        this.medicalTreatment = medicalTreatment;
    }

    public Pill getPill() {
        return pill;
    }

    public void setPill(Pill pill) {
        this.pill = pill;
    }
}
