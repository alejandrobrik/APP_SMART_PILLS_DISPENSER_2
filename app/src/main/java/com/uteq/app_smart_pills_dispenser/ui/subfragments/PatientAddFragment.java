package com.uteq.app_smart_pills_dispenser.ui.subfragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uteq.app_smart_pills_dispenser.Activities.CarerAddActivity;
import com.uteq.app_smart_pills_dispenser.MainActivity;
import com.uteq.app_smart_pills_dispenser.MenuActivity;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.services.PatientService;
import com.uteq.app_smart_pills_dispenser.ui.dialogs.DatePickerFragment;
import com.uteq.app_smart_pills_dispenser.utils.Apis;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientAddFragment extends Fragment {


    PatientService service;

    EditText txtname;
    Spinner spinerGenderPatient;
    EditText txtbirthdate;
    EditText txtpassword;
    EditText txtRepeatPassword;
    String genero;

    Button save;

    Carer carerLogin;


    //Implementacion firebase
    ShapeableImageView fotoPAtient;


    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;

    StorageReference storageReference;
    String storage_path = "patient/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    private int contador = 0;

    Button btnPhotoSelect;
    private Uri image_url_Global;
    String imagePatientDatabase;
    String photo = "photoPatient";
    String idd;

    public PatientAddFragment() {
        super(R.layout.fragment_patient_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //Siguiendo la implementacion firebase
        mfirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            carerLogin = (Carer) getArguments().get("id_login");

            contador =  getArguments().getInt("cont");
        }

        txtname = view.findViewById(R.id.txtNamePatient);
        txtbirthdate = view.findViewById(R.id.txtBirthDatePatient);

        txtbirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerDialog();
            }
        });


        spinerGenderPatient = (Spinner) view.findViewById(R.id.spinerGenderPatient);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.combo_gender, R.layout.spiner_item_patient);

        spinerGenderPatient.setAdapter(adapter);
        spinerGenderPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                genero = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtpassword = view.findViewById(R.id.txtpassword);
        txtRepeatPassword = view.findViewById(R.id.txtrepeatPassword);


        save = view.findViewById(R.id.btnSavePatient);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient p = new Patient();
                p.setName(txtname.getText().toString());
                p.setBirthDate(txtbirthdate.getText().toString());
                p.setGender(genero);
                p.setState((true));
                p.setCarer((carerLogin));

                if (imagePatientDatabase!=null)
                    p.setUrlImage(imagePatientDatabase);


                if(p.getName().isEmpty() || p.getBirthDate().isEmpty())
                {
                    Toast.makeText(getContext(),"Please chek the fields", Toast.LENGTH_LONG).show();

                }

                else {
                    addPatient(p);


                   // Navigation.findNavController(view).navigate(R.id.action_patientAddFragment_to_nav_patients);
                }

            }
        });

        fotoPAtient = view.findViewById(R.id.imgPatientPhoto);
        btnPhotoSelect = view.findViewById(R.id.btnSelectPatientPhoto);
        btnPhotoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //         getCacheDir().delete();
                deleteCache(getActivity().getApplicationContext());
                uploadPhoto();
            }
        });


    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

       startActivityForResult(i, COD_SEL_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url_Global = data.getData();
                if (image_url_Global!=null){
                    starCrop(image_url_Global);
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
                Glide.with(getActivity())
                        .load(imageUriResultCrop)
                        .error(R.drawable.ic_user)
                        .into(fotoPAtient);

            }

        }
        
        super.onActivityResult(requestCode, resultCode, data);

    }

    private  void starCrop(@NonNull Uri uri) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = simpleDateFormat.format(new Date());

        String destinationFileName = SAMPLE_CROPPED_IMG_NAME + "patient" + currentDateandTime +contador;
        contador++;
        destinationFileName +=".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);
//        uCrop.withAspectRatio(3, 4);
//
//        uCrop.useSourceImageAspectRatio();
//
//        uCrop.withAspectRatio(2, 3;
//        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(450, 450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(getContext(),this, UCrop.REQUEST_CROP);

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

        image_url_Global = image_url;
        getpatient(image_url);
        try {

     //       Toast toast = Toast.makeText(getContext(), "Cargando foto", Toast.LENGTH_SHORT);
     //       toast.setGravity(Gravity.TOP,0,200);
     //       toast.show();
            Glide.with(getContext())
                    .load(image_url)
                    .error(R.drawable.ic_user)
                    .into(fotoPAtient);

        }catch (Exception e){
            Log.v("Error", "e: " + e);
        }

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


    private void showDatePickerDialog() {
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

                txtbirthdate.setText(selectedDate);

            }
        });

        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void getpatient(Uri image_url) throws Exception {

        String id = ""+carerLogin.getId();
        Call<List<Patient>> patientList = Apis.getPatientService().getPatient();

        patientList.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if(response.isSuccessful()){
                    List <Patient>  patients = response.body();
                    int lastIdx = patients.size() -1;
                    if (patients.isEmpty()){
                        lastIdx = 0;
                        idd = "0";
                    } else {
                    Patient lastElment = patients.get(lastIdx);
                    idd = "" +lastElment.getId()+1;
                    }
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
                                        map.put("patient", download_uri);
                                        imagePatientDatabase = download_uri;
                                        mfirestore.collection("patient").document(idd).update(map);
                                        //        Toast.makeText(CarerAddActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                                        //   progressDialog.dismiss();

                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getContext(), "Error al cargar foto", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    public void addPatient(Patient p) {
        service = Apis.getPatientService();
        Call<Patient> call = service.addPatient(p);

        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response != null) {
                    if (image_url_Global!=null) {
//
//                        idd = response.body().getId();
//                        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() + "" + idd;
//                        StorageReference reference = storageReference.child(rute_storage_photo);
//                        reference.putFile(image_url_Global).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                while (!uriTask.isSuccessful()) ;
//                                if (uriTask.isSuccessful()) {
//                                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String download_uri = uri.toString();
//                                            HashMap<String, Object> map = new HashMap<>();
//                                            map.put("patient", download_uri);
//                                            imagePatientDatabase = download_uri;
//                                            mfirestore.collection("patient").document(idd).update(map);
//                                            //        Toast.makeText(CarerAddActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
//                                            //   progressDialog.dismiss();
//                                            Patient patient = response.body();
//                                            patient.setUrlImage(imagePatientDatabase);
//                                            addPatientUrl(patient);
//
//                                        }
//                                    });
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                               // Toast.makeText(getContext(), "Error al cargar foto", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                    }


                        Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_patientAddFragment_to_nav_patients);


                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }

    public void addPatientUrl(Patient p) {
        service = Apis.getPatientService();
        Call<Patient> call = service.addPatient(p);

        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response != null) {
               //     Toast.makeText(getContext(), "Successful registration Photo.", Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Successful registration.", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_patientAddFragment_to_nav_patients);

                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error:", t.getMessage());

            }
        });
    }


}


