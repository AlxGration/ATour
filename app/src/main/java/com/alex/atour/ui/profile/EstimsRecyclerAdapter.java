package com.alex.atour.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alex.atour.DTO.Estimation;
import com.alex.atour.R;
import java.util.ArrayList;

public class EstimsRecyclerAdapter extends RecyclerView.Adapter<EstimsRecyclerAdapter.ViewHolder> {

    private ArrayList<Estimation> data;

    public EstimsRecyclerAdapter(ArrayList<Estimation> data){
        this.data = data;
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

        Estimation estim = data.get(position);

        // покрас списка в виде "зебры"
        if (position % 2 == 0){
            holder.setGreyBG();
        }else{
            holder.setWhiteBG();
        }

        holder.tvComplexity.setText(String.format("%.2f", estim.getComplexity()));
        holder.tvNovelty.setText(String.format("%.2f", estim.getNovelty()));
        holder.tvStrategy.setText(String.format("%.2f", estim.getStrategy()));
        holder.tvTactics.setText(String.format("%.2f", estim.getTactics()));
        holder.tvTechnique.setText(String.format("%.2f", estim.getTechnique()));
        holder.tvTension.setText(String.format("%.2f", estim.getTension()));
        holder.tvInformativeness.setText(String.format("%.2f", estim.getInformativeness()));

        holder.tvFIO.setText(estim.getMemberFIO());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFIO;
        private final TextView tvComplexity, tvNovelty, tvStrategy, tvTactics, tvTechnique, tvTension, tvInformativeness;

        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            tvFIO = view.findViewById(R.id.tv_fio);
            tvComplexity = view.findViewById(R.id.tv_complexity);
            tvNovelty = view.findViewById(R.id.tv_novelty);
            tvStrategy = view.findViewById(R.id.tv_strategy);
            tvTactics = view.findViewById(R.id.tv_tactics);
            tvTechnique = view.findViewById(R.id.tv_technique);
            tvTension = view.findViewById(R.id.tv_tension);
            tvInformativeness = view.findViewById(R.id.tv_informativeness);
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
