package com.uteq.app_smart_pills_dispenser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.uteq.app_smart_pills_dispenser.databinding.ActivityMenuBinding;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.ui.patients.PatientListFragment;
import com.uteq.app_smart_pills_dispenser.utils.MoreUtils;

import java.io.File;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    private TextView tvNameCarerMenu, tvEmailCarerMenu;
    private ShapeableImageView navHeaderImageView;

    Carer carer;
    Button returnHome;

    private static final int REQUEST_PHONE_CALL = 1;
    int contador = 0;
    int destinoActualId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();

        this.carer = (Carer)  bundle.getSerializable("c");


        String name = carer.getName();
        String email = carer.getEmail();
        String image = carer.getUrlImage();


       // image = "";

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

        returnHome = findViewById(R.id.btnReturntHomeEver);


  //      binding.appBarMenu.btnReturntHomeEver.setVisibility(View.GONE);

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

                boolean installed = isAppInstalled("com.whatsapp");
                if (installed) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+593990407518"+"&text="+ "hola"));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MenuActivity.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_patients, R.id.nav_alerts, R.id.nav_doctors, R.id.nav_setings, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        binding.appBarMenu.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_logout) {
                    navController.navigate(R.id.nav_logout);
                    // do something for item1
                    return true;
                } else if (itemId == R.id.action_about) {
                    // do something for item2
                    return true;
                } else {
                    // if you do nothing, returning false should be appropriate.
                    return false;
                }

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                if (id == R.id.nav_home) {
                    navController.navigate(R.id.action_anypart_to_nav_home);
                }
                else
                {
                    // Make your navController object final above
                    // or call Navigation.findNavController() again here
                    NavigationUI.onNavDestinationSelected(menuItem, navController);
                }
                drawer.closeDrawer(GravityCompat.START);
                if (id == R.id.action_logout){
                    Toast.makeText(getApplicationContext(), "Cerrando la sesion", Toast.LENGTH_SHORT).show();
                    System.out.println("Aver cerrando sesion");
                }
                return true;
            }
        });


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_home || destination.getId() == R.id.nav_logout) {
                    binding.appBarMenu.btnReturntHomeEver.setVisibility(View.GONE);
                    destinoActualId = R.id.nav_home;
                } else {
                    binding.appBarMenu.btnReturntHomeEver.setVisibility(View.VISIBLE);
                    destinoActualId = 0;
                }
            }
        });
/*
        if ( Navigation.findNavController== R.id.nav_home){
            returnHome = findViewById(R.id.btnReturntHomeEver);
            returnHome.setVisibility(View.GONE);
        }
*/



        try {


            returnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      navController.navigate(R.id.action_anypart_to_nav_home);
                }
            });
        }catch (Exception e){
            System.out.println("Pues aqui hubo un problema: " +e);
        }

    }





    public Carer loadData() {
        Carer carer = this.carer;
        return carer;
    }

    public void enviarWhatsapp()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        String uri  ="https://api.whatsapp.com/send/?phone=593961148453&text=Hola+necesito+Bendici%C3%B3n+Lunar+Promo";
        sendIntent.setData(Uri.parse(uri));
        sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);
    }


    @Override
    public void onBackPressed() {
        if (destinoActualId == R.id.nav_home) {
            if (contador == 0) {
                Toast.makeText(getApplicationContext(), R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
                contador++;
            } else {
                super.onBackPressed();
            }

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    contador = 0;
                }
            }.start();
        }else {
            super.onBackPressed();
        }
        deleteCache(getApplicationContext());

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
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