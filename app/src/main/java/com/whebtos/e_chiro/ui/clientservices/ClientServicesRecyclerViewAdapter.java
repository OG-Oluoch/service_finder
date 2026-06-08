package com.whebtos.e_chiro.ui.clientservices;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.ui.clienthome.ServicesRecyclerViewAdapter;

import java.util.List;

public class ClientServicesRecyclerViewAdapter extends RecyclerView.Adapter<ClientServicesRecyclerViewAdapter.ViewHolder> {

    private List<ClientService> listItemList;
    private Context context;
    private ClientServicesRecyclerViewAdapter.ItemClickListener mItemListener;

    public ClientServicesRecyclerViewAdapter(List<ClientService> list, Context context, ItemClickListener itemClickListener) {
        this.listItemList = list;
        this.context = context;
        this.mItemListener = itemClickListener;
    }

    public ClientServicesRecyclerViewAdapter(Context context, List<ClientService> list) {
        this.context = context;
        this.listItemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientService list = listItemList.get(position);

        holder.textViewServiceName.setText(list.getServiceName());
        holder.textViewServiceDescription.setText(list.getServiceDescription());

        // Safe image loading with Picasso
        String imageUrl = list.getServiceImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.logo1) // Add placeholder drawable
                    .error(R.drawable.logo1) // Add error drawable
                    .into(holder.serviceImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded successfully: " + imageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Error loading image: " + imageUrl, e);
                            holder.serviceImage.setImageResource(R.drawable.logo1);
                        }
                    });
        } else {
            holder.serviceImage.setImageResource(R.drawable.briefcase);
            Log.w("Picasso", "Image URL is null or empty for position: " + holder.getAdapterPosition());
        }

        holder.itemView.setOnClickListener(view ->
        {
            mItemListener.onItemClick(listItemList.get(holder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        if(listItemList==null) return 0;
        return listItemList.size();
    }

    public interface ItemClickListener
    {
        void onItemClick(ClientService listItemClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewServiceName;
        public TextView textViewServiceDescription;
        public ImageView serviceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewServiceName = itemView.findViewById(R.id.service_name);
            textViewServiceDescription = itemView.findViewById(R.id.service_description);
            serviceImage = itemView.findViewById(R.id.service_image);

        }
    }

}

