package com.whebtos.e_chiro.ui.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.utils.DefaultSettings;

import java.util.ArrayList;

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Service> arrayList;
    private Context context;
    private ServicesFragment servicesFragment;

    // creating a constructor.
    public ServicesRecyclerViewAdapter(ServicesFragment ServicesFragment, ArrayList<Service> newsModalArrayList, Context context) {
        this.servicesFragment = ServicesFragment;
        this.arrayList = newsModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServicesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ServicesRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_services, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ServicesRecyclerViewAdapter.ViewHolder holder, int position) {
        // on below line we are setting data to our ui components.
        Service model = arrayList.get(position);

        holder.nameTV.setText(model.getName());

        String link = DefaultSettings.URL + DefaultSettings.URL_SERVICE_IMAGE + model.getImage();

        Picasso.with(context).load(link).into(holder.imageIV);

    }

    @Override
    public int getItemCount() {
        // returning the size of array list
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // creating variables for our text view.
        public TextView nameTV;

        public ImageView imageIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our text view
            nameTV = itemView.findViewById(R.id.tv_name);

            imageIV = itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {

                Service service = arrayList.get(position);

                servicesFragment.openProvidersMap();
            }

        }

    }
}
