package com.uteq.app_smart_pills_dispenser.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import com.uteq.app_smart_pills_dispenser.utils.MQTTListener.MQTTListener;

public class MyDialogFragment extends DialogFragment  implements MQTTListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Fingerprint Registration")
                .setMessage("Please place the patient's fingerprint on the dispenser")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes agregar acciones si el usuario hace clic en Aceptar
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onHuellaOkReceived() {
        if (isAdded()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Fingerprint verified");
            builder.setPositiveButton("Accept", null);
            builder.show();
        }

    }
}