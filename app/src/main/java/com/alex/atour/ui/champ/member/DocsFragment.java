package com.alex.atour.ui.champ.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alex.atour.R;
import com.alex.atour.ui.champ.ChampActivity;
import com.alex.atour.ui.champ.admin.MembersFragment;
import com.alex.atour.ui.champ.admin.MembersViewModel;

public class DocsFragment extends Fragment {

    private DocsViewModel viewModel;
    private final String champID;

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

        View view = inflater.inflate(R.layout.fragment_docs_send, container, false);

        EditText etLink = view.findViewById(R.id.et_link);
        EditText etComment = view.findViewById(R.id.et_comment);
        TextView tvError = view.findViewById(R.id.tv_error);

        Button btnSend = view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> viewModel.sendDocs(
                champID,
                etComment.getText().toString(),
                etLink.getText().toString()
        ));

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            ((ChampActivity)getActivity()).showLoadingProcess(isLoading);
            btnSend.setEnabled(!isLoading);
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);

        return view;
    }
}
