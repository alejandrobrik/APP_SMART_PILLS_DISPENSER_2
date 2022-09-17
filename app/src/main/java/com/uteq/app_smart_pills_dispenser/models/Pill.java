package com.uteq.app_smart_pills_dispenser.models;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pill {
    private int id;
    private String description;
    private String name;
    private String registrationDate;
    private boolean state;
    private  String urlImage;

}
