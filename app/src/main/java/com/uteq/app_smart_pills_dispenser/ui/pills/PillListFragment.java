package com.uteq.app_smart_pills_dispenser.ui.pills;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.adapters.PillAdapter;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.models.Pill;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillListFragment extends Fragment {
    Button btnSearch, btnPillSave, btnBackPill;
    FloatingActionButton favAddPill;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    private RecyclerView recyclerView;
    private PillAdapter pillAdapter;

    public PillListFragment() {
        super(R.layout.fragment_pill_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.reciclerViewPill);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        pillAdapter = new PillAdapter();
        recyclerView.setAdapter(pillAdapter);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        favAddPill = view.findViewById(R.id.favAddPill);
        favAddPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
                ((MenuActivity)getActivity()).optionSelect();
                return;
            }
        });
        try {
            getPill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPill() throws Exception {
        Call<List<Pill>> pillList = Apis.getPillService().getPill();
        pillList.enqueue(new Callback<List<Pill>>() {
            @Override
            public void onResponse(Call<List<Pill>> call, Response<List<Pill>> response) {
                if(response.isSuccessful()){
                    List <Pill>  pill = response.body();
                    pillAdapter.setData(pill);
                }
            }
            @Override
            public void onFailure(Call<List<Pill>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }
}