package com.uteq.app_smart_pills_dispenser.ui.setings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentHomeBinding;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentSetingsBinding;
import com.uteq.app_smart_pills_dispenser.ui.home.HomeViewModel;

public class SetingsFragment extends Fragment {

    private SetingsViewModel mViewModel;
    private FragmentSetingsBinding binding;



    //txtStings

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SetingsViewModel setingsViewModel =
                new ViewModelProvider(this).get(SetingsViewModel.class);

        binding = FragmentSetingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSetings;
        setingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}