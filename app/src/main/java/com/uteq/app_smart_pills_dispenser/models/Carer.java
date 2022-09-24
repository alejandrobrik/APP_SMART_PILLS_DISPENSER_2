package com.uteq.app_smart_pills_dispenser.models;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Carer implements Serializable{
        private int id;
        private String email;
        private String name;
        private String gender;
        private String birthDate;
        private String password;
        private String phoneNumber;
        private boolean state;
        private String urlImage;

}
