package com.alex.atour.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alex.atour.DTO.Member;
import com.alex.atour.R;
import java.util.ArrayList;

public class MembersListRecyclerAdapter extends RecyclerView.Adapter<MembersListRecyclerAdapter.ViewHolder> {

    private ArrayList<Member> data;

    public MembersListRecyclerAdapter(ArrayList<Member> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_member, parent, false);

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

        if (request.getRole() == 1){
            holder.imgRole.setImageResource(R.drawable.ic_member);
        }else{
            holder.imgRole.setImageResource(R.drawable.ic_referee);
        }

        holder.imgWalk.setVisibility(
                request.isTypeWalk()? View.VISIBLE: View.GONE
        );
        holder.imgSki.setVisibility(
                request.isTypeSki()? View.VISIBLE: View.GONE
        );
        holder.imgHike.setVisibility(
                request.isTypeHike()? View.VISIBLE: View.GONE
        );
        holder.imgWater.setVisibility(
                request.isTypeWater()? View.VISIBLE: View.GONE
        );
        holder.imgSpeleo.setVisibility(
                request.isTypeSpeleo()? View.VISIBLE: View.GONE
        );
        holder.imgBike.setVisibility(
                request.isTypeBike()? View.VISIBLE: View.GONE
        );
        holder.imgAuto.setVisibility(
                request.isTypeAuto()? View.VISIBLE: View.GONE
        );
        holder.imgOther.setVisibility(
                request.isTypeOther()? View.VISIBLE: View.GONE
        );
        
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
        private final ImageView imgWalk, imgSki, imgHike, imgWater, imgSpeleo, imgBike, imgAuto, imgOther, imgRole;


        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            tvFIO = view.findViewById(R.id.tv_fio);
            imgRole = view.findViewById(R.id.img_role);
            imgWalk = view.findViewById(R.id.img_walk);
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
