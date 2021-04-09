package com.alex.atour.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.R;

import java.util.ArrayList;

public class RequestsListRecyclerAdapter extends RecyclerView.Adapter<RequestsListRecyclerAdapter.ViewHolder> {

    ArrayList<MembershipRequest> data;

    public RequestsListRecyclerAdapter(ArrayList<MembershipRequest> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your _data_ at this position and replace the
        // contents of the view with that element
        MembershipRequest request = data.get(position);

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
                MembershipRequest req = data.get(((int)view.getTag()));
                listener.startProfileActivityWith(req);
            }
        });
        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) {listener.acceptRequest(request.getId());}
        });
        holder.btnDeny.setOnClickListener(v -> {
            if (listener != null) {listener.denyRequest(request.getId());}
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
        void startProfileActivityWith(MembershipRequest req);

        void acceptRequest(String reqID);
        void denyRequest(String reqID);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFIO;
        private final ImageView imgWalk, imgSki, imgHike, imgWater, imgSpeleo, imgBike, imgAuto, imgOther, imgRole;
        private final Button btnAccept, btnDeny;

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
            btnAccept = view.findViewById(R.id.btn_accept);
            btnDeny = view.findViewById(R.id.btn_deny);
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
