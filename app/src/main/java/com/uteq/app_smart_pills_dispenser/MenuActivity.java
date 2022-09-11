package com.uteq.app_smart_pills_dispenser;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.uteq.app_smart_pills_dispenser.databinding.ActivityMenuBinding;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.ui.patients.PatientListFragment;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    private TextView tvNameCarerMenu, tvEmailCarerMenu;
    private ShapeableImageView navHeaderImageView;

    Carer carer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();

        this.carer = (Carer)  bundle.getSerializable("c");


        String name = carer.getName();
        String email = carer.getEmail();
        String image = carer.getUrlImage();



        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View vistaHeader = binding.navView.getHeaderView(0);

        tvNameCarerMenu = vistaHeader.findViewById(R.id.tvNameCarerMenu);
        tvEmailCarerMenu = vistaHeader.findViewById(R.id.tvEmailCarerMenu);

        tvNameCarerMenu.setText("NAME: "+ name.toString());

        tvEmailCarerMenu.setText("EMAIL: "+email.toLowerCase(Locale.ROOT));

        navHeaderImageView = vistaHeader.findViewById((R.id.imvCarer));

        Glide.with(this)
                .load(image)
                .error(R.drawable.ic_user)
                .into(navHeaderImageView);






       // Envar datos de un activity a fragment
        PatientListFragment patientListFragment = new PatientListFragment();
        bundle.putInt("id_carer", carer.getId());
        patientListFragment.setArguments(bundle);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.nav_host_fragment_content_menu,galleryFragment);
//        fragmentTransaction.commit();


        setSupportActionBar(binding.appBarMenu.toolbar);
        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_patients, R.id.nav_alerts, R.id.nav_doctors, R.id.nav_setings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    public Carer loadData() {
        Carer carer = this.carer;
        return carer;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}