package com.uteq.app_smart_pills_dispenser.ui.subfragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.itextpdf.text.pdf.PdfContentByte.ALIGN_CENTER;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uteq.app_smart_pills_dispenser.R;
import com.uteq.app_smart_pills_dispenser.models.ComparatorDosage;
import com.uteq.app_smart_pills_dispenser.models.Dosage;
import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.models.Patient;
import com.uteq.app_smart_pills_dispenser.utils.Apis;

import com.itextpdf.text.Document;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportPatientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List <MedicalTreatment>  medicalTreatmentListReport;
    Patient patient;
    Button viewTreatments;
    Button viewDosages;
    List<Dosage> dosageByPatientList = new ArrayList<>();




    public ReportPatientFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patient = (Patient) getArguments().getSerializable("patient");
        }

        if(checkPermission()) {
            Toast.makeText(getContext(), "Permission Accept", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions();
        }




        try {
            getAllDosage();

            getMedicalTreatment();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void crearPDF() {
        try {
           String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
           //String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EjemploITextPDF";
            File dir = new File(path);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, "treatments_patient_"+ patient.getName()+".pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Document documento = new Document() ;
            PdfWriter.getInstance(documento, fileOutputStream);

            documento.open();

            Paragraph titulo = new Paragraph(
                    "Treatments Report of patient: " +patient.getName() + "\n\n\n",
                    FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE)
            );
            titulo.setAlignment(ALIGN_CENTER);
            documento.add(titulo);

            PdfPTable tabla = new PdfPTable(4);
            tabla.addCell("Description");
            tabla.addCell("Registration treatment");
            tabla.addCell("Begin Treatment");
            tabla.addCell("Doctor");


            for (int i = 0 ; i < medicalTreatmentListReport.size() ; i++) {
                tabla.addCell(medicalTreatmentListReport.get(i).getDescription());
                tabla.addCell(medicalTreatmentListReport.get(i).getRegistrationDate());
                tabla.addCell(medicalTreatmentListReport.get(i).getStartDate());
                tabla.addCell(medicalTreatmentListReport.get(i).getDoctor().getName());

            }

            documento.add(tabla);

            documento.close();


        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public  void createPdfDosage() {
        try {
            String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            //String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EjemploITextPDF";
            File dir = new File(path);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, "dosages_patient_"+patient.getName()+".pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Document documento = new Document() ;
            PdfWriter.getInstance(documento, fileOutputStream);

            documento.open();

            Paragraph titulo = new Paragraph(
                    "Dosages Report of patient: " +patient.getName() + "\n\n\n",
                    FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE)
            );
            titulo.setAlignment(ALIGN_CENTER);
            documento.add(titulo);

            PdfPTable tabla = new PdfPTable(7);
            tabla.addCell("Prescription");
            tabla.addCell("Registration Dosage");
            tabla.addCell("Pill");
            tabla.addCell("Date Take pill");
            tabla.addCell("Treatment");
            tabla.addCell("Doctor");
            tabla.addCell("Status");

            Collections.sort(dosageByPatientList, new ComparatorDosage());


            for (int i = 0 ; i < dosageByPatientList.size() ; i++) {
                tabla.addCell(dosageByPatientList.get(i).getPrescription());
                tabla.addCell(dosageByPatientList.get(i).getRegistrationDate());
                tabla.addCell(dosageByPatientList.get(i).getPill().getName());
                tabla.addCell(dosageByPatientList.get(i).getDateTake());
                tabla.addCell(dosageByPatientList.get(i).getMedicalTreatment().getDescription());
                tabla.addCell(dosageByPatientList.get(i).getMedicalTreatment().getDoctor().getName());
                if (dosageByPatientList.get(i).getState()) {
                    tabla.addCell("Not take");
                }else{
                    tabla.addCell("Take");
                }

            }
            tabla.setWidthPercentage(100);




            documento.add(tabla);

            documento.close();


        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_report_patient, container, false);
        viewTreatments = view.findViewById(R.id.btnReportTreatments);
        viewTreatments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPDF();
                Toast.makeText(getContext(), "Verify the Report of treatments in downloads", Toast.LENGTH_LONG).show();
            }


        });

        viewDosages = view.findViewById(R.id.btnReportDosages);
        viewDosages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdfDosage();
                Toast.makeText(getContext(), "Verify the Report of dosages in downloads", Toast.LENGTH_LONG).show();
            }
        });



        return view;
    }

    public void getAllDosage() throws Exception {
        Call<List<Dosage>> dosageList = Apis.getDosageService().getAllDosage();

        dosageList.enqueue(new Callback<List<Dosage>>() {
            @Override
            public void onResponse(Call<List<Dosage>> call, Response<List<Dosage>> response) {
                if(response.isSuccessful()){
                    List <Dosage>  dosages = response.body();

                    for (Dosage dosage: dosages){
                        if (dosage.getMedicalTreatment().getPatient().equals(patient)){
                            dosageByPatientList.add(dosage);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Dosage>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    public void getMedicalTreatment() throws Exception {

        String id = ""+patient.getId();
        Call<List<MedicalTreatment>> medicalTreatmentList = Apis.getMedicalTreatmentService().getMedicalTreatment(id);

        medicalTreatmentList.enqueue(new Callback<List<MedicalTreatment>>() {
            @Override
            public void onResponse(Call<List<MedicalTreatment>> call, Response<List<MedicalTreatment>> response) {
                if(response.isSuccessful()){
                    medicalTreatmentListReport = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<MedicalTreatment>> call, Throwable t) {
                Log.e("faliure", t.getLocalizedMessage());
            }
        });
    }
}