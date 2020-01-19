package com.example.projectmanager.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmanager.R;
import com.example.projectmanager.Utils.Projects;

import java.util.ArrayList;

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.HomeRvViewHolder> {

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
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    h / 6);
            cardView.setLayoutParams(params);

//            RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(w,h);

        }
    }

    public HomeRvAdapter(ArrayList<Projects> projects) {
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
        holder.pProducer.setText(projects.getProducerId());
        holder.pDate.setText(projects.getDate());

    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

}
