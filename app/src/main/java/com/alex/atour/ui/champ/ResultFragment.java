package com.alex.atour.ui.champ;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.alex.atour.BuildConfig;
import com.alex.atour.R;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.ConfirmationDialog;

import java.io.File;

public class ResultFragment extends Fragment {

    private final String champID;

    public static ResultFragment newInstance(String champID) {
        return new ResultFragment(champID);
    }

    private ResultFragment(String champID){this.champID = champID;}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        ImageButton btnDownloadProtocol = view.findViewById(R.id.btn_download_protocol);
        btnDownloadProtocol.setOnClickListener(v->{
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                downloadTotalProtocolFile();
            }else {
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
            }
        });

        return view;
    }

    public void downloadTotalProtocolFile(){
        DBManager db = DBManager.getInstance();
        db.downloadTotalProtocol(champID, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog("Протокол сохранен в папке Documents.\nОткрыть?", () -> {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/ATour/"),
                            "protocol_total.xlsx");
                    if (file.exists()) {
                        Intent xlsxOpenintent = new Intent();
                        Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file);
                        xlsxOpenintent.setAction(Intent.ACTION_VIEW);
                        xlsxOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        xlsxOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        xlsxOpenintent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        xlsxOpenintent.setDataAndType(uri, "*/*");
                        xlsxOpenintent.setDataAndType(uri, "application/vnd.ms-excel");
                        try {
                            startActivity(xlsxOpenintent);
                        } catch (ActivityNotFoundException e) {
                            ((ChampActivity)getActivity()).showError("К сожалению, открыть файл не получилось.");
                            e.printStackTrace();
                        }
                    }else{
                        ((ChampActivity)getActivity()).showError( "Файл не найден");
                    }
                });
                confirmationDialog.show(getActivity().getSupportFragmentManager(), "myDialog");
            }

            @Override
            public void onFailed(String msg) {
                ((ChampActivity)getActivity()).showError(msg);
            }
        });
    }
}
