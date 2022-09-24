package com.uteq.app_smart_pills_dispenser.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalTreatment implements Serializable {
    private String id;
    private String description;
    private String registrationDate;
    private String startDate;
    private Boolean state;
    Doctor doctor;
    Patient patient;

}
