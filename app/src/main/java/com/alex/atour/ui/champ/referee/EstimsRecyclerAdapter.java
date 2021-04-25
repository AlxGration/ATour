package com.alex.atour.ui.champ.referee;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.R;
import com.alex.atour.db.DBManager;
import com.alex.atour.db.RealmDB;
import com.alex.atour.models.ValueFormatter;

import java.util.ArrayList;
import java.util.Locale;

public class EstimsRecyclerAdapter extends RecyclerView.Adapter<EstimsRecyclerAdapter.ViewHolder> {

    private ArrayList<MemberEstimation> data;
    private final RealmDB realmDB;

    public EstimsRecyclerAdapter(ArrayList<MemberEstimation> data){
        this.data = data;
        realmDB = DBManager.getInstance().getRealmDB();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_referee_estim, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MemberEstimation estim = data.get(position);

        // покрас списка в виде "зебры"
        if (position % 2 == 0){
            holder.setGreyBG();
        }else{
            holder.setWhiteBG();
        }

        holder.etComplexity.setText(String.format(Locale.ENGLISH, "%.2f", estim.getComplexity()));
        holder.etNovelty.setText(String.format(Locale.ENGLISH, "%.2f", estim.getNovelty()));
        holder.etStrategy.setText(String.format(Locale.ENGLISH, "%.2f", estim.getStrategy()));
        holder.etTactics.setText(String.format(Locale.ENGLISH, "%.2f", estim.getTactics()));
        holder.etTechnique.setText(String.format(Locale.ENGLISH, "%.2f", estim.getTechnique()));
        holder.etTension.setText(String.format(Locale.ENGLISH, "%.2f", estim.getTension()));
        holder.etInformativeness.setText(String.format(Locale.ENGLISH, "%.2f", estim.getInformativeness()));

        holder.tvFIO.setText(estim.getMemberFIO());
        holder.btnSave.setOnClickListener(view->{
            //data validation
            MemberEstimation e = new MemberEstimation(estim);
            String isAnyErr = ValueFormatter.isEstimationOK(e, holder.etComplexity.getText().toString(),
                    holder.etNovelty.getText().toString(),
                    holder.etStrategy.getText().toString(),
                    holder.etTactics.getText().toString(),
                    holder.etTechnique.getText().toString(),
                    holder.etTension.getText().toString(),
                    holder.etInformativeness.getText().toString()
            );
            if (isAnyErr == null){
                realmDB.writeMemberEstimation(e);
                listener.showError("Сохранено");
            }else {
                Log.e("TAG", "can't save "+estim.getMemberFIO());
                listener.showError(isAnyErr);
            }
        });
        
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                MemberEstimation req = data.get(((int)view.getTag()));
                listener.startProfileActivityWith(role, req);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static IonItemClickListener listener;
    private static int role = -1;
    //role - от чьего имени открывается экран
    public static void setOnItemClickListener(int _role, IonItemClickListener lis){
        role = _role;
        listener = lis;
    }
    public interface IonItemClickListener{
        void startProfileActivityWith(int role, MemberEstimation req);
        void showError(String msg);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFIO;
        private final EditText etComplexity, etNovelty, etStrategy, etTactics, etTechnique, etTension, etInformativeness;
        private ImageButton btnSave;


        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            tvFIO = view.findViewById(R.id.tv_fio);
            etComplexity = view.findViewById(R.id.et_complexity);
            etNovelty = view.findViewById(R.id.et_novelty);
            etStrategy = view.findViewById(R.id.et_strategy);
            etTactics = view.findViewById(R.id.et_tactics);
            etTechnique = view.findViewById(R.id.et_technique);
            etTension = view.findViewById(R.id.et_tension);
            etInformativeness = view.findViewById(R.id.et_informativeness);
            btnSave = view.findViewById(R.id.btn_save);
        }
        private void setGreyBG() {
            if (view == null) return;
            view.setBackgroundResource(R.drawable.bg_grey);
        }
        private void setWhiteBG() {
            if (view == null) return;
            view.setBackgroundResource(R.drawable.bg_white);
        }
    }
}
