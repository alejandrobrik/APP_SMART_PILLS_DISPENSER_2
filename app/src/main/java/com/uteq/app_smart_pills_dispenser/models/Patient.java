package com.uteq.app_smart_pills_dispenser.models;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Patient implements Serializable {
    private String id;
    private String birthDate;
    private String gender;
    private String name;
    private Boolean state;
    private String urlImage;
    private Carer carer;

}
