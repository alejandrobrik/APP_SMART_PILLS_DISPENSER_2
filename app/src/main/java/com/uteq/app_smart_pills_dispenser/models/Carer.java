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
        private String password;
        private String phoneNumber;
        private boolean state;
        private String urlImage;

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
        }

        public boolean isState() {
                return state;
        }

        public void setState(boolean state) {
                this.state = state;
        }

        public String getUrlImage() {
                return urlImage;
        }

        public void setUrlImage(String urlImage) {
                this.urlImage = urlImage;
        }
}
