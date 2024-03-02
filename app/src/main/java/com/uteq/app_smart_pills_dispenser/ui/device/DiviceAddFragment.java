package com.uteq.app_smart_pills_dispenser.ui.device;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.uteq.app_smart_pills_dispenser.MainActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Device;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.services.DosageService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.TimePickerFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;
import com.uteq.app_smart_pills_dispenser.utils.MQTTManager;
import com.uteq.app_smart_pills_dispenser.utils.MyDialogFragment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiviceAddFragment extends Fragment {

    DosageService service;

    String pillGson, patientGson;
    Pill pill;
    MedicalTreatment medicalTreatment;

    Device device;
    Dosage dosageCardview;

    Patient patient;


    Boolean stateDate;
    EditText txtCode;
    EditText txtDescription;
    EditText txtMac;
    EditText txtPrescription;
    TextView tvNameSelectedPill;
    TextView tvDateHourSelectedDosage;
    Button btnSelectDate;
    Button btnLinkDevices;
    Button btnSearchDevice;
    Button btnSelectTreatment;
    Button btnAddDosages;
    String cadenaRespuesta;
    String onlyDate ="";
    String onlyHour = " ";
    String fecha ="";
    String hora ="";


    private MQTTManager mqttClient;

    public DiviceAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {



        }

        txtCode = view.findViewById(R.id.txt_code_device);


        txtDescription = view.findViewById(R.id.txt_description_device);

        txtMac = view.findViewById(R.id.txt_mac_device);


        btnSearchDevice = view.findViewById(R.id.btnDeviceSearch);
        btnSearchDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code;

                code = txtCode.getText().toString();

                if (code!= null) {
                    try {
                        getDevice(code);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(getContext(), "No hay texto",Toast.LENGTH_LONG).show();
                }


               /* Navigation.findNavController(view).navigate(R.id.pillListFragment,);*/
            }
        });




        btnLinkDevices = view.findViewById(R.id.btnLinkDevice);
        btnLinkDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendMessageEnrrol();



/*                if (pill == null || dosage.getPrescription().isEmpty() || dosage.getDateHour().length() != 16){
                    Toast.makeText(getContext(), "Please checke the field ",Toast.LENGTH_LONG).show();;
                }
                else {
                    Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();


                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
*//*                    bundle.putSerializable("dosage", dosage);
                    Navigation.findNavController(view).navigate(R.id.action_dosageAddFragment_to_dosageListFragment, bundle);*//*
                }*/
            }
        });

        //Si la dosis que viajo es diferente de null seteamos los campos
        if (dosageCardview!=null){
            txtPrescription.setText(dosageCardview.getPrescription());
            tvDateHourSelectedDosage.setText(dosageCardview.getDateHour());
            //       txtStartDate.setText(dosageCardview.getStarDate());
            //       txtEndDate.setText(dosageCardview.getEndDate());

        }


            }


            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.fragment_divice_add, container, false);
            }


            public void getDevice(String code) throws Exception {


                Call<Device> deviceCall = Apis.getDeviceService().getDevice(code);

                deviceCall.enqueue(new Callback<Device>() {
                    @Override
                    public void onResponse(Call<Device> call, Response<Device> response) {
                        if (response.isSuccessful()) {
                            Device device1 = response.body();
                            device = device1;

                            if (device1 != null){
                            txtDescription.setText(device.getDescription());
                            txtMac.setText(device.getMac());

                            btnLinkDevices.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(getContext(), "The code not is valid", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "The code not is valid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Device> call, Throwable t) {
                        Log.e("faliure", t.getLocalizedMessage());
                    }
                });
            }


            public void okEnrrol(){

            }

            public void sendMessageEnrrol(){
                mqttClient = new MQTTManager(getContext());
                // Conectar y suscribirse al topic
                mqttClient.connectAndSubscribe();



                // Enviar el mensaje automáticamente al iniciar la aplicación
                mqttClient.publishMessage("enrrolar:");

                MyDialogFragment dialogFragment = new MyDialogFragment();
                dialogFragment.show(getFragmentManager(), "MyDialog");


            }



}