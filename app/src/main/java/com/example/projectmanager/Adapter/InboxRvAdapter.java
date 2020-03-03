package com.example.projectmanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmanager.Activity.ViewMessageActivity;
import com.example.projectmanager.Model.Messages;
import com.example.projectmanager.R;

import java.util.ArrayList;

public class InboxRvAdapter extends RecyclerView.Adapter<InboxRvAdapter.HomeRvViewHolder> {

    private ArrayList<Messages> mProjects;

    class HomeRvViewHolder extends RecyclerView.ViewHolder {
        private TextView mIntro;
        private TextView mProducer;
        private ImageView mAttach;
        private CardView cardView;
        int w, h;
        Context context;

        public HomeRvViewHolder(@NonNull View itemView) {
            super(itemView);
            mIntro = itemView.findViewById(R.id.tv_message_intro);
            mProducer = itemView.findViewById(R.id.tv_message_producer_name);
            mAttach = itemView.findViewById(R.id.img_message_attach);
            cardView = itemView.findViewById(R.id.cardview_home);
            DisplayMetrics displayMetrics = itemView.getContext().getResources().getDisplayMetrics();

            w = displayMetrics.widthPixels;
            h = displayMetrics.heightPixels;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    (int) (w / 1.1),
                    h / 6);
            cardView.setLayoutParams(params);

            RelativeLayout.LayoutParams Rparams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            Rparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            Rparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            Rparams.leftMargin = w / 30;
            Rparams.bottomMargin = h / 80;
            mAttach.setLayoutParams(Rparams);

            context = itemView.getContext();
        }
    }

    public InboxRvAdapter(ArrayList<Messages> messages) {
        mProjects = messages;
    }

    @NonNull
    @Override
    public HomeRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_rv_layout, parent, false);
        HomeRvViewHolder hrv = new HomeRvViewHolder(v);
        return hrv;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeRvViewHolder holder, final int position) {
        Messages messages = mProjects.get(position);

        holder.mIntro.setText(messages.getMessageIntro());
        holder.mProducer.setText(" " + messages.getProducerId() + " ");
        holder.mAttach.setVisibility(View.VISIBLE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, ViewMessageActivity.class);
                intent.putExtra("messageId", mProjects.get(position).getID());
                intent.putExtra("writerName", mProjects.get(position).getProducerId());
                holder.context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

}
