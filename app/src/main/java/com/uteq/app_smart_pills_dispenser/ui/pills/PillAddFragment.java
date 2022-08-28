package com.uteq.app_smart_pills_dispenser.ui.pills;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uteq.app_smart_pills_dispenser.R;

public class PillAddFragment extends Fragment {

    private PillAddViewModel mViewModel;

    public static PillAddFragment newInstance() {
        return new PillAddFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pill_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PillAddViewModel.class);
        // TODO: Use the ViewModel
    }

}