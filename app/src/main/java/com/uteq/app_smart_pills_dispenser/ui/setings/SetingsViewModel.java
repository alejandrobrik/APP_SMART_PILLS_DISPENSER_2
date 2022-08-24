package com.uteq.app_smart_pills_dispenser.ui.setings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SetingsViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public SetingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is setings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}