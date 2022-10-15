package com.uteq.app_smart_pills_dispenser.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.collect.BiMap;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uteq.app_smart_pills_dispenser.MainActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Doctor;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.CarerService;
import com.uteq.app_smart_pills_dispenser.services.PatientService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.ui.subfragments.PatientAddFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;
import com.uteq.app_smart_pills_dispenser.utils.EncryptHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarerAddActivity extends AppCompatActivity {

    CarerService service;
    PatientService patientService;

    EditText txtname;
    EditText txtphoneNumber;
    EditText txtemail;
    EditText txtpassword;
    EditText txtRepeatPassword;
    Boolean validaEmail = false;

    Spinner spinerGenderCarer;
    Spinner spinnerRegisterAs;
    String genero;
    String registerAs;
    EditText txtbirthdateCarer;

    EncryptHelper encrypt;
    String encryptValue;

    Button save;
    Button btnClean;

    //Implementacion firebase
    ShapeableImageView fotoCarer;


    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;

    StorageReference storageReference;
    String storage_path = "carer/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    private int contador = 0;

    Button btnPhotoSelect;
    private Uri image_url;
    String imageCarerDatabase;
    String photo = "photoCarer";
    String idd;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_add);

/*        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }*/
        //Siguiendo la implementacion firebase

        mfirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        btnPhotoSelect = findViewById(R.id.btnSelectCarerPhoto);
        fotoCarer = findViewById(R.id.imgCarerPhoto);

        btnPhotoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       //         getCacheDir().delete();
                deleteCache(getApplication());
                uploadPhoto();
            }
        });


        txtname = findViewById(R.id.txtname);

        txtbirthdateCarer = findViewById(R.id.txtBirthDateCarer);
        txtbirthdateCarer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        spinerGenderCarer = findViewById(R.id.spinerGenderCarer);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combo_gender, R.layout.spiner_item_patient);

        spinerGenderCarer.setAdapter(adapter);
        spinerGenderCarer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                genero = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerRegisterAs  =  findViewById(R.id.spinerRegisterAs);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.combo_register_as, R.layout.spiner_item_patient);

        spinnerRegisterAs.setAdapter(adapter2);
        spinnerRegisterAs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                registerAs = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtphoneNumber = findViewById(R.id.txtphonenumberCarer);
        txtemail = findViewById(R.id.txtemail);
        txtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && txtemail.getText().toString()!=null){

                    validaEmail = validaEmail(txtemail);
                    if (validaEmail){


                    }else {
                        txtemail.setError("Email not valid");
                    }
              //      Toast.makeText(getApplicationContext(), txtemail.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtpassword = findViewById(R.id.txtpassword);
        txtRepeatPassword = findViewById(R.id.txtrepeatPassword);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);

            }
        });

        save = findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaEmail = validaEmail(txtemail);
                Carer c = new Carer();
                c.setName(txtname.getText().toString());
                c.setEmail(txtemail.getText().toString());
                c.setPassword(txtpassword.getText().toString());
                c.setPhoneNumber(txtphoneNumber.getText().toString());
                c.setGender(genero);
                c.setBirthDate(txtbirthdateCarer.getText().toString());
                //Inserta imagen en el carer
                if (imageCarerDatabase!=null)
                    c.setUrlImage(imageCarerDatabase);

                c.setState((true));

                String pass=txtpassword.getText().toString();
                String repeatPass = txtRepeatPassword.getText().toString();
                String name =  txtname.getText().toString();
                String phone = txtphoneNumber.getText().toString();
                String email =txtemail.getText().toString();
                String birthDate = txtbirthdateCarer.getText().toString();


                if(pass.equals(repeatPass) && !(pass.isEmpty() || repeatPass.isEmpty() || name.isEmpty() || phone.isEmpty() ||email.isEmpty() || birthDate.isEmpty()) && validaEmail ) {
                    encrypt = new EncryptHelper();
                    try {
                        encryptValue = encrypt.encriptar(c.getPassword(),encrypt.apiKey);
                        c.setPassword(encryptValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addCarer(c);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class );
                    startActivity(intent);
                }
                else if(pass.isEmpty() || repeatPass.isEmpty() || name.isEmpty() || phone.isEmpty() ||email.isEmpty() ){
                    Toast.makeText(CarerAddActivity.this, "Error, check the fields. " , Toast.LENGTH_SHORT).show();
                }
                else if (!validaEmail){
                    Toast.makeText(CarerAddActivity.this, "Error, check the email entered. " , Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(CarerAddActivity.this, "Error, check the password. " , Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnClean = findViewById(R.id.btnClean);

        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCache(getApplication());
                txtname.setText("");
                txtphoneNumber.setText("");
                txtemail.setText("");
                txtpassword.setText("");
                txtRepeatPassword.setText("");

                Glide.with(CarerAddActivity.this)
                        .load(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(fotoCarer);
            }
        });


    }

    public void addCarer(Carer c)
    {
        service = Apis.getCarerService();
        Call<Carer> call = service.addCarer(c);

        call.enqueue(new Callback<Carer>() {
            @Override
            public void onResponse(Call<Carer> call, Response<Carer> response) {
                if(response!=null) {

                    if (!registerAs.equals("Only Carer")) {
                        Patient patient = new Patient();
                        patient.setName(response.body().getName());
                        patient.setBirthDate(response.body().getBirthDate());
                        patient.setGender(response.body().getGender());
                        patient.setCarer(response.body());
                        patient.setUrlImage(response.body().getUrlImage());

                        addPatient(patient);
                    }
                    else {
                        finish();
                        Toast.makeText(CarerAddActivity.this, "Successful registration.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Carer> call, Throwable t) {
                Log.e("Error:",t.getMessage());

            }
        });
    }
    public void addPatient(Patient p) {
        patientService = Apis.getPatientService();
        Call<Patient> call = patientService.addPatient(p);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response != null) {
                    finish();
                     Toast.makeText(getApplicationContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);

    }

    private void showDatePickerDialog() {
        // Metodo para mostrar el dialogo de fecha
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                month = month +1;
                String monthParse;
                String dayParse;
                String selectedDate;

                if (month <10)
                    monthParse = "0"+month;
                else
                    monthParse = ""+month;
                if (day < 10)
                    dayParse = "0"+day;
                else
                    dayParse = ""+day;
                selectedDate = (year + "-" +monthParse +"-" + dayParse);

                txtbirthdateCarer.setText(selectedDate);

            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                if (image_url!=null){
                    starCrop(image_url);
                }
                try {
                  // subirPhoto(image_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){

            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop!= null){
                try {
                    subirPhoto(imageUriResultCrop);
                } catch (Exception e) {
                    e.printStackTrace();
                }

         //        fotoCarer.setImageURI(imageUriResultCrop);
               Glide.with(CarerAddActivity.this)
                        .load(imageUriResultCrop)
                        .error(R.drawable.ic_user)
                        .into(fotoCarer);

            }

       }
        super.onActivityResult(requestCode, resultCode, data);

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

    private  void starCrop(@NonNull Uri uri) {

        Date date = (Calendar.getInstance().getTime());
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME+ date +contador;
        contador++;
        destinationFileName +=".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);
//        uCrop.withAspectRatio(3, 4);
//
//        uCrop.useSourceImageAspectRatio();
//
//        uCrop.withAspectRatio(2, 3;
//        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(450, 450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(CarerAddActivity.this);

    }

    public  UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);

        //CompressType
     //   options.setCompressionFormat(Bitmap.CompressFormat.PNG);
     //   options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //colors
        options.setStatusBarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
        options.setToolbarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

        options.setToolbarTitle("Recortar imagen");
        return  options;

    }


    private void subirPhoto(Uri image_url) throws Exception {
   //     progressDialog.setMessage("Actualizando foto");
   //     progressDialog.show();
        Random numAleatorio1 = new Random();
        int aleatorio = numAleatorio1.nextInt(3000- 25+1) +25;
   //     idd = aleatorio + "";

        getCarer(image_url);
        try {

            Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,200);
            toast.show();
            Glide.with(CarerAddActivity.this)
                    .load(image_url)
                    .error(R.drawable.ic_user)
                    .into(fotoCarer);

        }catch (Exception e){
            Log.v("Error", "e: " + e);
        }
/*        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("carer", download_uri);
                            mfirestore.collection("carer").document(idd).update(map);
                            Toast.makeText(CarerAddActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                         //   progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CarerAddActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private  boolean validaEmail(EditText email){

        String emailInput = email.getText().toString();
        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
           // Toast.makeText(this, "Email valitated succesfully", Toast.LENGTH_SHORT).show();
            return true;
        }else{
           // Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void getCarer(Uri image_url) throws Exception {

        Call<List<Carer>> doctorList = Apis.getCarerService().getCarer();

        doctorList.enqueue(new Callback<List<Carer>>() {
            @Override
            public void onResponse(Call<List<Carer>> call, Response<List<Carer>> response) {
                if(response.isSuccessful()){
                    List <Carer>  carers = response.body();
                        int lastIdx = carers.size() - 1;
                        Carer lastElment = carers.get(lastIdx);
                        idd = "" +lastElment.getId()+1;
                    String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
                    StorageReference reference = storageReference.child(rute_storage_photo);
                    reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            if (uriTask.isSuccessful()){
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String download_uri = uri.toString();
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("carer", download_uri);
                                        imageCarerDatabase = download_uri;
                                        mfirestore.collection("carer").document(idd).update(map);
                               //        Toast.makeText(CarerAddActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                                        //   progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CarerAddActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Carer>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // do your stuff
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
        super.onBackPressed();

    }

}