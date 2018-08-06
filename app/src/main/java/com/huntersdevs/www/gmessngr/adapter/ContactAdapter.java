package com.huntersdevs.www.gmessngr.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.pojo.GMessngrContactPOJO;
import com.huntersdevs.www.gmessngr.ui.CircleImageView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<GMessngrContactPOJO> list;
    private Context mContext;

    public ContactAdapter(List<GMessngrContactPOJO> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GMessngrContactPOJO contactPOJO = list.get(position);

        holder.rlItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Hey yo bitch, it's working!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlItemContainer;
        CircleImageView ivProfileImage;
        TextView tvProfileName;

        ViewHolder(View itemView) {
            super(itemView);
            rlItemContainer = itemView.findViewById(R.id.rl_item_contact_holder);
            ivProfileImage = itemView.findViewById(R.id.iv_profile_image);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
        }
    }

}
