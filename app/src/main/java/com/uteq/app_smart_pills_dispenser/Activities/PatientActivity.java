package com.uteq.app_smart_pills_dispenser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Patient;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PatientActivity extends AppCompatActivity {

    SearchView txtSearch;
    RecyclerView listPatients;
    ArrayList<Patient>listArrayPatients;
    FloatingActionButton  fabNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        txtSearch = findViewById(R.id.txtSearch);
        listPatients.setLayoutManager(new LinearLayoutManager(this));
        fabNuevo = findViewById(R.id.favNewPatient);

    }
}