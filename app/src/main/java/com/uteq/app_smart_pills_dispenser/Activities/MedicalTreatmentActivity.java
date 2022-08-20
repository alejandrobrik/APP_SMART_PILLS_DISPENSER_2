package com.uteq.app_smart_pills_dispenser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;

import java.util.ArrayList;

public class MedicalTreatmentActivity extends AppCompatActivity {

    SearchView txtSearchMedicalTreatment;
    RecyclerView listMedicalTreatment;
    ArrayList<MedicalTreatment> listArrayMedicalTreatment;
    FloatingActionButton favNewMedicalTreatment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_treatment);
    }
}