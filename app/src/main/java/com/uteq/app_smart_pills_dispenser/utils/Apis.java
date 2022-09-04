package com.uteq.app_smart_pills_dispenser.utils;

import com.uteq.app_smart_pills_dispenser.models.MedicalTreatment;
import com.uteq.app_smart_pills_dispenser.services.CarerService;
import com.uteq.app_smart_pills_dispenser.services.DoctorService;
import com.uteq.app_smart_pills_dispenser.services.DosageService;
import com.uteq.app_smart_pills_dispenser.services.MedicalTreatmentService;
import com.uteq.app_smart_pills_dispenser.services.PatientService;
import com.uteq.app_smart_pills_dispenser.services.PillService;

public class Apis {
    public static final String URL_001="https://smart-pills-dispenser.herokuapp.com/";

    public static DoctorService getDoctorService()
    {
        return ClientRetrofit.getClient(URL_001).create(DoctorService.class);
    }

    public static CarerService getCarerService()
    {
        return ClientRetrofit.getClient(URL_001).create(CarerService.class);
    }

    public static PillService getPillService()
    {
        return ClientRetrofit.getClient(URL_001).create(PillService.class);
    }
    public static PatientService getPatientService()
    {
        return ClientRetrofit.getClient(URL_001).create(PatientService.class);
    }

    public static MedicalTreatmentService getMedicalTreatmentService()
    {
        return ClientRetrofit.getClient(URL_001).create(MedicalTreatmentService.class);
    }

    public static DosageService getDosageService(){
        return ClientRetrofit.getClient(URL_001).create(DosageService.class);
    }
}
