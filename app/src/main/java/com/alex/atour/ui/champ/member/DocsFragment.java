package com.alex.atour.ui.champ.member;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alex.atour.BuildConfig;
import com.alex.atour.DTO.TSMReport;
import com.alex.atour.R;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.ExcelModule;
import com.alex.atour.ui.champ.ChampActivity;

import java.io.File;

public class DocsFragment extends Fragment {

    private DocsViewModel viewModel;
    private final String champID;
    private Button btnSendTSM, btnSend;

    public static DocsFragment newInstance(String champID) {
        return new DocsFragment(champID);
    }

    private DocsFragment(String champID){this.champID = champID;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DocsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_docs_sending, container, false);

        EditText etLink = view.findViewById(R.id.et_link);
        EditText etComment = view.findViewById(R.id.et_comment);
        TextView tvError = view.findViewById(R.id.tv_error);

        btnSend = view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> viewModel.sendDocs(
                champID,
                etComment.getText().toString(),
                etLink.getText().toString()
        ));

        btnSendTSM = view.findViewById(R.id.btn_tsm);
        if ( ! DBManager.getInstance().getPrefs().getTSMFilePath().isEmpty()){
            btnSendTSM.setText("ТСМ принята");
        }
        btnSendTSM.setOnClickListener(v->{
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                showTSMPickerDialog();
            }else {
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            ((ChampActivity)getActivity()).showLoadingProcess(isLoading);
            btnSend.setEnabled(!isLoading);
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);

        return view;
    }

    public void showTSMPickerDialog(){
        Intent docsPickerIntent = new Intent();
        docsPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        docsPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        docsPickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        docsPickerIntent.setType("*/xlsx");
        docsPickerIntent.setType("application/vnd.ms-excel");
        docsPickerIntent.setType("application/*");

        startActivityForResult(docsPickerIntent, 55);
    }

    public void tsmSave(Uri tsmPath){

        ExcelModule excel = new ExcelModule(getActivity());
        TSMReport tsm = excel.parseTSM(tsmPath);
        if (tsm == null){
            viewModel.requestError("Не удалось открыть файл");
            return;
        }
        viewModel.saveTSMPathLocally(tsmPath.getPath());
        viewModel.sendTSMToServer(champID, tsm, tsmPath);


        if (btnSend!= null){
            btnSendTSM.setText("ТСМ принята");
            btnSend.setEnabled(true);
            btnSend.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
    }
}
