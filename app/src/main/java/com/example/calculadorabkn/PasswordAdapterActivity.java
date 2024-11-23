package com.example.calculadorabkn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapterActivity extends RecyclerView.Adapter<PasswordAdapterActivity.PasswordViewHolder> {

    private final List<PasswordActivity> passwordActivityList;

    public PasswordAdapterActivity(List<PasswordActivity> passwordActivityList) {
        this.passwordActivityList = passwordActivityList;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        PasswordActivity passwordActivity = passwordActivityList.get(position);
        holder.txtSiteName.setText(passwordActivity.getSiteName());
        holder.txtUsername.setText(passwordActivity.getUsername());
    }

    @Override
    public int getItemCount() {
        return passwordActivityList.size();
    }

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView txtSiteName, txtUsername;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSiteName = itemView.findViewById(R.id.txtSiteName);
            txtUsername = itemView.findViewById(R.id.txtUsername);
        }
    }
}
