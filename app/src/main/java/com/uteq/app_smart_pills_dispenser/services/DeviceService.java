package com.uteq.app_smart_pills_dispenser.services;

import com.uteq.app_smart_pills_dispenser.models.Carer;
import com.uteq.app_smart_pills_dispenser.models.Device;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeviceService {
    @GET("/api/device/code/{code}")
    Call<Device> getDevice(@Path("code") String code);
}
