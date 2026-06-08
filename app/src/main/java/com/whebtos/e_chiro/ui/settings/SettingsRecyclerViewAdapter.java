package com.whebtos.e_chiro.ui.settings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whebtos.e_chiro.R;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.MyAdapter> {
    Context context;

    public SettingsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_setting,parent,false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        if(position==0)
        {
            holder.image.setImageResource(R.drawable.download);
            holder.image1.setImageResource(R.drawable.download);
            holder.back.setBackgroundColor(Color.parseColor("#E6E53935"));
            holder.text.setText("Donate blood every year");
        }
        if(position==1)
        {
            holder.image.setImageResource(R.drawable.volunteering);
            holder.image1.setImageResource(R.drawable.volunteering);
            holder.back.setBackgroundColor(Color.parseColor("#F236883A"));
            holder.text.setText("Large local volunteers");
        }
        if(position==2){
            holder.image.setImageResource(R.drawable.door);
            holder.image1.setImageResource(R.drawable.door);
            holder.back.setBackgroundColor(Color.parseColor("#F2AF4576"));
            holder.text.setText("Everyday door to door visit");
        }
        if(position==3)
        {
            holder.image.setImageResource(R.drawable.localevent);
            holder.image1.setImageResource(R.drawable.localevent);
            holder.back.setBackgroundColor(Color.parseColor("#F2EEAA45"));
            holder.text.setText("Organize local events");
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
    public class MyAdapter extends RecyclerView.ViewHolder {
        ImageView image,image1;
        TextView text;
        RelativeLayout back;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            image1=itemView.findViewById(R.id.image1);
            text=itemView.findViewById(R.id.text);
            back=itemView.findViewById(R.id.back);
        }
    }
}

