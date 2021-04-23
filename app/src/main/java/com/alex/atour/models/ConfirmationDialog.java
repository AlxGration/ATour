package com.alex.atour.models;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment {

    private IDialogResult listener;
    private String message;
    public ConfirmationDialog(String msg, IDialogResult listener){
        message = msg;
        this.listener = listener;
    }

    public interface IDialogResult{
        void onSuccess();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Подтверждение";
        String confirmBtn = "Да";
        String cancelBtn = "Отмена";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(confirmBtn, (dialog, id) -> {
            if (listener != null)listener.onSuccess();
            dismiss();
        });
        builder.setNegativeButton(cancelBtn, (dialog, id) -> {
            dismiss();
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
