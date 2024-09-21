package com.uteq.app_smart_pills_dispenser.ui.subfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.MainActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.DosageAdapter;
import com.uteq.app_smart_pills_dispenser.adapters.ScheduleAdapter;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.ComparatorDosage;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScheduleFragment extends Fragment {


    Button btnAddPatient;


    private int id_carer;
    private Carer carer;


    String medicalTreatmentGson;
    MedicalTreatment treatmentDosageView;
    private Patient patient;

    Carer carerLogin = new Carer();

    private RecyclerView recyclerView;
    private SearchView svSearchDosage;
    private ScheduleAdapter scheduleAdapter;
    private Dosage dosage;

    private ProgressBar progressBar;
    int counter = 0;


    public ScheduleFragment() {
        // Required empty public constructor
    }


    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        if (getArguments() != null) {
            id_carer = getArguments().getInt("id_carer", 0);
            carer = getArguments().getParcelable("c");
            patient = (Patient) getArguments().getSerializable("patient");
            dosage = (Dosage) getArguments().getSerializable("dosage");


        }

        progressBar = view.findViewById(R.id.progressBarSchedule);
        progressBar.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                counter++;

                progressBar.setProgress(counter);

                if (counter == 100){
                    timer.cancel();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        };
        timer.schedule(timerTask, 100, 5);

        recyclerView = view.findViewById(R.id.reciclerviewSchedule);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        scheduleAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(scheduleAdapter);


        //Llama a un metodo del activity que toma el carer que inicio sesion
        // ((MenuActivity)getActivity()).loadData();

        //  carerLogin = ((MenuActivity)getActivity()).loadData();



        try {
            getAllDosage();
        } catch (Exception e) {
            e.printStackTrace();
        }




        return view;
    }
    public void getAllDosage() throws Exception {
        Call<List<Dosage>> dosageList = Apis.getDosageService().getAllDosage();

        dosageList.enqueue(new Callback<List<Dosage>>() {
            @Override
            public void onResponse(Call<List<Dosage>> call, Response<List<Dosage>> response) {
                if(response.isSuccessful()){
                    List <Dosage>  dosages = response.body();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String currentDateandTime = simpleDateFormat.format(new Date());




                    List<Dosage> dosageByPatientList = new ArrayList<>();
                    for (Dosage dosage: dosages){
                        if (dosage.getMedicalTreatment().getPatient().equals(patient)){
                            String[] fechapartida = dosage.getDateTake().split("T");
                            String fechaHora = "";

                            if (fechapartida.length >= 2) {
                                fechaHora = fechapartida[0] + " " + fechapartida[1];
                            } else {
                                // Si no se puede separar la fecha, se mantiene vacía o se asigna un valor por defecto
                                fechaHora = ""; // O cualquier otro valor por defecto que necesites
                            }


                            try {
                                Date dateDatabase = simpleDateFormat.parse(fechaHora);
                                Date date = (Calendar.getInstance().getTime());
                                if (date.before(dateDatabase))
                                    dosageByPatientList.add(dosage);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    Collections.sort(dosageByPatientList,new ComparatorDosage());

                    scheduleAdapter.setData(dosageByPatientList);
                }
            }

            @Override
            public void onFailure(Call<List<Dosage>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }


    public void getDosage() throws Exception {
        String id ="1";
        if (dosage!= null) {
            if (dosage.getMedicalTreatment().getId() != null)
                id = "" + dosage.getMedicalTreatment().getId();
        }
        else {

        }
        Call<List<Dosage>> dosageList = Apis.getDosageService().getDosage(id);

        dosageList.enqueue(new Callback<List<Dosage>>() {
            @Override
            public void onResponse(Call<List<Dosage>> call, Response<List<Dosage>> response) {
                if(response.isSuccessful()){
                    List <Dosage>  dosages = response.body();
                    scheduleAdapter.setData(dosages);
                }
            }

            @Override
            public void onFailure(Call<List<Dosage>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }
}
