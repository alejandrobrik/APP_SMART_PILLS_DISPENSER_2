package com.uteq.app_smart_pills_dispenser.ui.doctors;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentHomeBinding;

public class DoctorListFragment extends Fragment {



    private FragmentHomeBinding binding;
    public static DoctorListFragment newInstance() {
        return new DoctorListFragment();
    }

    public DoctorListFragment() {
        super(R.layout.fragment_doctor_list);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_list, container, false);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}