package com.whebtos.e_chiro.ui.clienthome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.whebtos.e_chiro.R;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private List<ListItem> listItemList;
    private Context context;
    private ItemClickListener mItemListener;

    public ListItemAdapter(List<ListItem> listItems, Context context, ItemClickListener itemClickListener) {
        this.listItemList = listItems;
        this.context = context;
        this.mItemListener = itemClickListener;
    }

    public ListItemAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItemList = listItems;
    }

//    public ServicesRecyclerViewAdapter(ClientHomeFragment clientHomeFragment, ArrayList<Service> serviceList, Context context) {
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItemList.get(position);

        holder.textViewServiceName.setText(listItem.getServiceName());
        holder.textViewServiceDescription.setText(listItem.getServiceDescription());

        Picasso.get().load(listItem.getServiceImage()).into(holder.serviceImage);

        holder.itemView.setOnClickListener(view ->
        {
            mItemListener.onItemClick(listItemList.get(position));
        });

    }


    @Override
    public int getItemCount() {
        return listItemList.size();
    }

    public interface ItemClickListener
    {
        void onItemClick(ListItem listItemClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewServiceName;
        public TextView textViewServiceDescription;
        public ImageView serviceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewServiceName = itemView.findViewById(R.id.nearest_service_name);
            textViewServiceDescription = itemView.findViewById(R.id.nearest_service_description);
            serviceImage = itemView.findViewById(R.id.nearest_service_image);

        }
    }

}