package com.uteq.app_smart_pills_dispenser.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Doctor {
    private int id;
    private String name;
    private String specialism;
    private Boolean state;
    private String phoneNumber;
    private String email;
    private String direction;
    private String registration_date;
}
