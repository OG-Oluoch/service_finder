package com.whebtos.e_chiro.ui.servicesubcategory;

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
import com.whebtos.e_chiro.ui.servicecategories.ServiceCategory;
import com.whebtos.e_chiro.utils.DefaultSettings;

import java.util.ArrayList;

public class ServiceSubCategoriesRecyclerViewAdapter extends RecyclerView.Adapter<ServiceSubCategoriesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ServiceCategory> arrayList;
    private Context context;
    private ServiceSubCategoriesFragment serviceSubCategoriesFragment;

    // creating a constructor.
    public ServiceSubCategoriesRecyclerViewAdapter(ServiceSubCategoriesFragment ServiceSubCategoriesFragment, ArrayList<ServiceCategory> newsModalArrayList, Context context) {
        this.serviceSubCategoriesFragment = ServiceSubCategoriesFragment;
        this.arrayList = newsModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_service_category, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // on below line we are setting data to our ui components.
        ServiceCategory model = arrayList.get(position);

        holder.nameTV.setText(model.getName());

        String link = DefaultSettings.URL + DefaultSettings.URL_SERVICE_CATEGORY_IMAGE + model.getImage();

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

                ServiceCategory serviceCategory = arrayList.get(position);

                serviceSubCategoriesFragment.openServices(serviceCategory.getId());

            }

        }

    }
}
