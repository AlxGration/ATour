package com.alex.atour.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.R;

import java.util.ArrayList;

public class ChampsListRecyclerAdapter extends RecyclerView.Adapter<ChampsListRecyclerAdapter.ViewHolder> {

    ArrayList<ChampInfo> data;

    public ChampsListRecyclerAdapter(ArrayList<ChampInfo> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_champ, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your _data_ at this position and replace the
        // contents of the view with that element
        ChampInfo info = data.get(position);

        // покрас списка в виде "зебры"
        if (position % 2 == 0){
            holder.setGreyBG();
        }else{
            holder.setWhiteBG();
        }

        holder.tvTitle.setText(info.getTitle());
        holder.tvCity.setText(info.getCity());
        holder.tvYear.setText(info.getDataTo().split(".")[2]);

        holder.imgWalk.setVisibility(
                info.isTypeWalk()? View.VISIBLE: View.GONE
        );
        holder.imgSki.setVisibility(
                info.isTypeSki()? View.VISIBLE: View.GONE
        );
        holder.imgHike.setVisibility(
                info.isTypeHike()? View.VISIBLE: View.GONE
        );
        holder.imgWater.setVisibility(
                info.isTypeWater()? View.VISIBLE: View.GONE
        );
        holder.imgSpeleo.setVisibility(
                info.isTypeSpeleo()? View.VISIBLE: View.GONE
        );
        holder.imgBike.setVisibility(
                info.isTypeBike()? View.VISIBLE: View.GONE
        );
        holder.imgAuto.setVisibility(
                info.isTypeAuto()? View.VISIBLE: View.GONE
        );
        holder.imgOther.setVisibility(
                info.isTypeOther()? View.VISIBLE: View.GONE
        );
        
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    ChampInfo info = data.get(((int)view.getTag()));
                    listener.startChampActivityWith(info.getChampID());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static IonItemClickListener listener;
    public static void setOnItemClickListener(IonItemClickListener lis){
        listener = lis;
    }
    public interface IonItemClickListener{
        void startChampActivityWith(String champID);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvCity;
        private final TextView tvYear;
        private final ImageView imgWalk, imgSki, imgHike, imgWater, imgSpeleo, imgBike, imgAuto, imgOther;

        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            tvTitle = view.findViewById(R.id.tv_title);
            tvCity = view.findViewById(R.id.tv_city);
            tvYear = view.findViewById(R.id.tv_year);
            imgWalk = view.findViewById(R.id.tb_walk);
            imgSki = view.findViewById(R.id.img_ski);
            imgHike = view.findViewById(R.id.img_hike);
            imgWater = view.findViewById(R.id.img_water);
            imgSpeleo = view.findViewById(R.id.img_speleo);
            imgBike = view.findViewById(R.id.img_bike);
            imgAuto = view.findViewById(R.id.img_auto);
            imgOther = view.findViewById(R.id.img_other);
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
