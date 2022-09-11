package com.uteq.app_smart_pills_dispenser.ui.dosages;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.services.DosageService;
import com.uteq.app_smart_pills_dispenser.services.MedicalTreatmentService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.TimePickerFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DosageAddFragment extends Fragment {

    DosageService service;

    String pillGson, patientGson;
    Pill pill;
    MedicalTreatment medicalTreatment;

    Dosage dosage;
    Dosage dosageCardview;

    Patient patient;

    TextView ultimoID;
    Boolean stateDate;
    EditText txtQuantity;
    EditText txtStartDate;
    EditText txtEndDate;
    EditText txtPrescription;
    TextView tvNameSelectedPill;
    TextView tvDateHourSelectedDosage;
    Button btnSelectDate;
    Button btnSelectHour;
    Button btnSelectPill;
    Button btnSelectTreatment;
    Button btnAddDosages;
    String cadenaRespuesta;
    String onlyDate ="";
    String onlyHour = " ";
    String fecha ="";
    String hora ="";
    String fechaHora;

    LocalDateTime fechita;

    public DosageAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            patientGson = getArguments().getString("patient");
           pillGson = getArguments().getString("pill");

            medicalTreatment = (MedicalTreatment) getArguments().getSerializable("treatment");


            dosageCardview = (Dosage) getArguments().getSerializable("dosage");


            pill = new Gson().fromJson(pillGson, Pill.class);

            tvNameSelectedPill = view.findViewById(R.id.tvNameSelectedPill);

            if (pill != null)
                tvNameSelectedPill.setText("The doctor selected is: " + pill.getName());
            if(medicalTreatment == null)
                medicalTreatment = dosageCardview.getMedicalTreatment();
        }

        ultimoID = view.findViewById(R.id.tvTreamentCode);
        getUltimoId();

        btnSelectDate = view.findViewById(R.id.btnSelectDateDosage);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog2();
            }
        });

        btnSelectHour = view.findViewById(R.id.btnSelectHourDosage);
        btnSelectHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        tvDateHourSelectedDosage = view.findViewById(R.id.tvDateHourSelectedDosage);



        txtPrescription = view.findViewById(R.id.txtPrescription);
        txtQuantity = view.findViewById(R.id.txtDosageQuantity);



        btnSelectPill = view.findViewById(R.id.btnSelectPill);
        btnSelectPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("antes de la desgracia");
                int cantidad;
                if (txtQuantity.getText().toString().isEmpty())
                    cantidad = 0;
                else
                    cantidad = Integer.parseInt(txtQuantity.getText().toString());

                dosage = new Dosage();
                dosage.setPrescription(txtPrescription.getText().toString());
               dosage.setDateHour(tvDateHourSelectedDosage.getText().toString());
             //   dosage.setStarDate(txtStartDate.getText().toString());
             //   dosage.setEndDate(txtEndDate.getText().toString());
                dosage.setQuantity(cantidad);
                int palomita = 512;
                System.out.println(palomita);
                dosage.setMedicalTreatment(medicalTreatment);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dosage", dosage);
                Navigation.findNavController(view).navigate(R.id.pillListFragment,bundle);
            }
        });

//        btnSelectTreatment = view.findViewById(R.id.btnSelectTreatment);
//        btnSelectTreatment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("Nuevo boton para seleccionar tratamiento");
//                int cantidad;
//                if (txtQuantity.getText().toString().isEmpty())
//                    cantidad = 0;
//                else
//                    cantidad = Integer.parseInt(txtQuantity.getText().toString());
//
//                dosage = new Dosage();
//                dosage.setHour(txtHorus.getText().toString());
//                dosage.setStarDate(txtStartDate.getText().toString());
//                dosage.setEndDate(txtEndDate.getText().toString());
//                dosage.setQuantity(cantidad);
//                int palomita = 512;
//                System.out.println(palomita);
//                if (medicalTreatment!=null)
//                    dosage.setMedicalTreatment(medicalTreatment);
//                if (pill!= null)
//                    dosage.setPill(pill);
//
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("dosage", dosage);
//
//
//                bundle.putSerializable("treatment", medicalTreatment);
//
//                Navigation.findNavController(view).navigate(R.id.medicalTreatmentListSelectFragment, bundle);
//            }
//        });



        btnAddDosages = view.findViewById(R.id.btnSaveDosage);
        btnAddDosages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicalTreatment tratamiendoSetear= dosageCardview.getMedicalTreatment();
                getUltimoId();
                //cadenaAdapter.getData();

                System.out.println(fechaHora);
                tratamiendoSetear.setId(ultimoID.getText().toString());
                int cantidad;
                if (txtQuantity.getText().toString().isEmpty())
                    cantidad = 0;
                else
                    cantidad = Integer.parseInt(txtQuantity.getText().toString());

                dosage = new Dosage();
                dosage.setPrescription(txtPrescription.getText().toString());
                dosage.setDateHour(tvDateHourSelectedDosage.getText().toString());
                dosage.setDateTake(fechaHora);
         //       dosage.setStarDate(txtStartDate.getText().toString());
          //      dosage.setEndDate(txtEndDate.getText().toString());
                dosage.setQuantity(cantidad);
                dosage.setMedicalTreatment(tratamiendoSetear);
                dosage.setPill(pill);
               // dosage.getMedicalTreatment().setId(getUltimoId());
                addDosage(dosage);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("dosage", dosage);
                Navigation.findNavController(view).navigate(R.id.dosageListFragment, bundle);
            }
        });

        //Si la dosis que viajo es diferente de null seteamos los campos
        if (dosageCardview!=null){
            txtPrescription.setText(dosageCardview.getPrescription());
            tvDateHourSelectedDosage.setText(dosageCardview.getDateHour());
     //       txtStartDate.setText(dosageCardview.getStarDate());
     //       txtEndDate.setText(dosageCardview.getEndDate());
            txtQuantity.setText( String.valueOf(dosageCardview.getQuantity()));
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dosage_add, container, false);
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                // +1 because January is zero
                month = month +1;
                String monthParse;
                String dayParse;
                String selectedDate;

                if (month <10)
                    monthParse = "0"+month;
                else
                    monthParse = ""+month;
                if (day < 10)
                    dayParse = "0"+day;
                else
                    dayParse = ""+day;
                selectedDate = (year + "-" +monthParse +"-" + dayParse);

                if (stateDate)
                    txtStartDate.setText(selectedDate);
                else
                    txtEndDate.setText(selectedDate);

            }
        });

        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    private void showDatePickerDialog2() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                // +1 because January is zero
                month = month +1;
                String monthParse;
                String dayParse;
                String selectedDate;

                if (month <10)
                    monthParse = "0"+month;
                else
                    monthParse = ""+month;
                if (day < 10)
                    dayParse = "0"+day;
                else
                    dayParse = ""+day;
                selectedDate = (year + "-" +monthParse +"-" + dayParse);

                onlyDate =  selectedDate;

                fecha = selectedDate;

                fechaHora = fecha +hora;

                tvDateHourSelectedDosage.setText(onlyDate+onlyHour);

            }
        });

        newFragment.show(getChildFragmentManager(), "datePicker");
    }



    private void showTimePickerDialog() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                String hourParse="";
                String minuteParse="";
                String selectehour="";

                if (hour<10)
                    hourParse = "0"+hour;
                else
                    hourParse = ""+hour;

                if (minute<10)
                    minuteParse = "0"+minute;
                else
                    minuteParse = ""+minute;

                selectehour = "T"+hourParse +":"+minuteParse;

                onlyHour = selectehour;

                hora = "T"+ hourParse +":"+ minuteParse;

                fechaHora = fecha + hora;
                tvDateHourSelectedDosage.setText(onlyDate+onlyHour);
                System.out.println("la hora que eligio es: " + hourParse + " y los minutos son: "+ minuteParse);
            }
        });

        newFragment.show(getChildFragmentManager(), "timePicker");
    }

    public void addDosage(Dosage dosage) {
        service = Apis.getDosageService();
        Call<Dosage> call = service.addDosage(dosage);

        call.enqueue(new Callback<Dosage>() {
            @Override
            public void onResponse(Call<Dosage> call, Response<Dosage> response) {
                if (response != null) {
                    Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Dosage> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }

    public void getUltimoId(){
        Call<String> ultimo = Apis.getMedicalTreatmentService().getMedicalTreatmentLastId();
        ultimo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    String respuesta = response.body();

                    ultimoID.setText(respuesta);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

}