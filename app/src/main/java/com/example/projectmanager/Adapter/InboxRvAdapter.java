package com.example.projectmanager.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmanager.Model.Projects;
import com.example.projectmanager.R;

import java.util.ArrayList;

public class InboxRvAdapter extends RecyclerView.Adapter<InboxRvAdapter.HomeRvViewHolder> {

    private ArrayList<Projects> mProjects;

    class HomeRvViewHolder extends RecyclerView.ViewHolder {
        TextView pName;
        TextView pProducer;
        TextView pDate;
        CardView cardView;
        int w, h;
        Context context;

        public HomeRvViewHolder(@NonNull View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.tv_project_name);
            pProducer = itemView.findViewById(R.id.tv_project_producer_name);
            pDate = itemView.findViewById(R.id.tv_project_date);
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
            pDate.setLayoutParams(Rparams);
        }
    }

    public InboxRvAdapter(ArrayList<Projects> projects) {
        mProjects = projects;
    }

    @NonNull
    @Override
    public HomeRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv_layout, parent, false);
        HomeRvViewHolder hrv = new HomeRvViewHolder(v);
        return hrv;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRvViewHolder holder, int position) {
        Projects projects = mProjects.get(position);

        holder.pName.setText(projects.getProjectName());
        holder.pProducer.setText(" " + projects.getProducerId() + " ");
        holder.pDate.setText(projects.getDate());

    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

}
