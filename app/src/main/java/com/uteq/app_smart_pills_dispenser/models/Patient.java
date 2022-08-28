package com.uteq.app_smart_pills_dispenser.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Patient {
    private String id;
    private String birth_date;
    private String gender;
    private String name;
    private Boolean state;
    private Carer idCarer;


}
