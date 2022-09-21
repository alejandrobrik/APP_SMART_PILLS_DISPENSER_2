package com.uteq.app_smart_pills_dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uteq.app_smart_pills_dispenser.Activities.CarerAddActivity;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.services.CarerService;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText txtname;
    EditText txtphoneNumber;
    EditText txtemail;
    EditText txtpassword;
    EditText txtRepeatPassword;

    Button btnlogin;
    Button btnsingup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtemail = findViewById(R.id.txtEmailLogin);
        txtpassword = findViewById(R.id.txtPasswordLogin);

        txtemail.setText("cuidador0512@gmail.com");
        txtpassword.setText("1");

        btnlogin = findViewById(R.id.buttonLogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
         //           btnlogin.setEnabled(false);
         //           btnsingup.setEnabled(false);
                    DoLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //     Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
           //     startActivity(intent);
            }
        });

        btnsingup = findViewById(R.id.buttonSingup);

        btnsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CarerAddActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void DoLogin() throws Exception {
        Call<List<Carer>> call = Apis.getCarerService().getCarer();
        call.enqueue(new Callback<List<Carer>>() {
            @Override
            public void onResponse(Call<List<Carer>> call, Response<List<Carer>> response) {
                if (response.isSuccessful()) {
                    List<Carer> carers = response.body();
                    for (Carer carer : carers) {
                        if(carer.getEmail().equals(txtemail.getText().toString()) &&
                                carer.getPassword().equals(txtpassword.getText().toString()))
                        {
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                            intent.putExtra("c",  carer);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Incorrect user or pass.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Carer>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al realizar la petición. " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}