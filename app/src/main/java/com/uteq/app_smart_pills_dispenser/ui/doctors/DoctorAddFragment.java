package com.uteq.app_smart_pills_dispenser.ui.doctors;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.DoctorService;
import com.uteq.app_smart_pills_dispenser.services.PatientService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoctorAddFragment extends Fragment {

    DoctorService service;

    EditText txtname;
    EditText txtSpecialism;
    EditText txtPhoneNumber;
    EditText txtEmail;
    EditText txtDirection;
    String genero;

    MedicalTreatment treatment;


    Button save;

    Carer carerLogin;

    public DoctorAddFragment() {
        super(R.layout.fragment_doctor_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            carerLogin = (Carer) getArguments().get("id_login");
            treatment  = (MedicalTreatment) getArguments().getSerializable("treatment");
        }

        txtname = view.findViewById(R.id.txtDoctorName);
        txtSpecialism = view.findViewById(R.id.txtDoctorSpecialism);
        txtPhoneNumber = view.findViewById(R.id.txtDoctorPhoneNumber);
        txtEmail = view.findViewById(R.id.txtDoctorEmail);
        txtDirection = view.findViewById(R.id.txtDoctoDirection);


        save = view.findViewById(R.id.btnSaveDosage);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor d = new Doctor();
                d.setName(txtname.getText().toString());
                d.setSpecialism(txtSpecialism.getText().toString());
                d.setPhoneNumber(txtPhoneNumber.getText().toString());
                d.setEmail(txtEmail.getText().toString());
                d.setDirection(txtDirection.getText().toString());
                d.setState((true));

                if(d.getName().isEmpty() || d.getSpecialism().isEmpty() || d.getPhoneNumber().isEmpty() || d.getEmail().isEmpty() || d.getDirection().isEmpty())
                {
                    Toast.makeText(getContext(),"Please chek the fields", Toast.LENGTH_LONG).show();
                }
                else {
                    addDoctor(d);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("treatment", treatment);

                    if (treatment != null) {
                        Navigation.findNavController(view).navigate(R.id.doctorListSelectFragment, bundle);
                    } else {
                        Navigation.findNavController(view).navigate(R.id.nav_doctors, bundle);
                    }

                }

            }
        });




    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                month = month + 1;
                String monthParse;
                String dayParse;
                String selectedDate;

                if (month < 10)
                    monthParse = "0" + month;
                else
                    monthParse = "" + month;
                if (day < 10)
                    dayParse = "0" + day;
                else
                    dayParse = "" + day;
                selectedDate = (year + "-" + monthParse + "-" + dayParse);



            }
        });

        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void addDoctor(Doctor d) {
        service = Apis.getDoctorService();
        Call<Doctor> call = service.addDoctor(d);

        call.enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response != null) {
                    Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }

}