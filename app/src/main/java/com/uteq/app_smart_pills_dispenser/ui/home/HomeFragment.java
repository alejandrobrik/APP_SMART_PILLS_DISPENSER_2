package com.uteq.app_smart_pills_dispenser.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button btnViewPatients;

    Button btnHome;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        btnViewPatients = view.findViewById(R.id.btnViewPatients);
        btnViewPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Navigation.findNavController(view).navigate(R.id.nav_patients);
                NavOptions navOptions = new NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .setPopUpTo(NavGraph.findStartDestination(Navigation.findNavController(view).getGraph()).getId(),
                                false, // inclusive
                                true) // saveState
                        .build();
                Navigation.findNavController(view).navigate(R.id.nav_patients, null, navOptions);



            }
        });



     // Button b = view.findViewById(R.id.btnReturntHomeEver);







    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}