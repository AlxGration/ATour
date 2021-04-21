package com.alex.atour.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.DTO.Member;
import com.alex.atour.R;

import java.util.ArrayList;

public class EstimsRecyclerAdapter extends RecyclerView.Adapter<EstimsRecyclerAdapter.ViewHolder> {

    private ArrayList<Member> data;

    public EstimsRecyclerAdapter(ArrayList<Member> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_estim, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Member request = data.get(position);

        // покрас списка в виде "зебры"
        if (position % 2 == 0){
            holder.setGreyBG();
        }else{
            holder.setWhiteBG();
        }

        holder.tvFIO.setText(request.getUserFIO());
        
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                Member req = data.get(((int)view.getTag()));
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
        void startProfileActivityWith(int role, Member req);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFIO;
        private final EditText etComplexity, etNovelty, etStrategy, etTactics, etTechnique, etTension, etInformativeness ;


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
