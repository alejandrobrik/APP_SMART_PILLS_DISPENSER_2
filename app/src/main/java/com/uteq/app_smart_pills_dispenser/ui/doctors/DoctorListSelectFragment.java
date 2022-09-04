package com.uteq.app_smart_pills_dispenser.ui.doctors;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.DoctorAdapter;
import com.uteq.app_smart_pills_dispenser.adapters.PatientAdapter;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentHomeBinding;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorListSelectFragment extends Fragment implements SearchView.OnQueryTextListener {


    FloatingActionButton favNewDoctor;
    FragmentTransaction transaction;


    private int id_carer;
    private Carer carer;

    Carer carerLogin = new Carer();

    private RecyclerView recyclerView;
    private SearchView svSearchDoctor;
    private DoctorAdapter doctorAdapter;
    private FragmentHomeBinding binding;



    public DoctorListSelectFragment() {
        super(R.layout.fragment_doctor_list_select);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            id_carer = getArguments().getInt("id_carer", 0);
            carer = getArguments().getParcelable("c");
        }

        recyclerView = view.findViewById(R.id.reciclerviewDoctor);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        doctorAdapter = new DoctorAdapter();
        recyclerView.setAdapter(doctorAdapter);

        svSearchDoctor = view.findViewById(R.id.svSearchDoctor);

        //Llama a un metodo del activity que toma el carer que inicio sesion
        ((MenuActivity)getActivity()).loadData();

        carerLogin = ((MenuActivity)getActivity()).loadData();


        favNewDoctor = view.findViewById(R.id.favNewDoctor);
        favNewDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("id_login", carerLogin);

                Navigation.findNavController(view).navigate(R.id.patientAddFragment,bundle);

                return;

            }
        });




        try {
            getDoctor();

        } catch (Exception e) {
            e.printStackTrace();
        }

        initListener();


    }

    public void getDoctor() throws Exception {

        Call<List<Doctor>> doctorList = Apis.getDoctorService().getDoctors();

        doctorList.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if(response.isSuccessful()){
                    List <Doctor>  doctors = response.body();
                    doctorAdapter.setData(doctors);
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    private  void initListener(){
        svSearchDoctor.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        doctorAdapter.filter(newText);
        return false;
    }

}