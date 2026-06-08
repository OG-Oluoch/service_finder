package com.whebtos.e_chiro.ui.clienthome;

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

import java.util.List;


public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ViewHolder> {

    private List<ListItem> listItemList;
    private Context context;
    private ItemClickListener mItemListener;

    public ServicesRecyclerViewAdapter(List<ListItem> listItems, Context context, ItemClickListener itemClickListener) {
        this.listItemList = listItems;
        this.context = context;
        this.mItemListener = itemClickListener;
    }

    public ServicesRecyclerViewAdapter(Context context, List<ListItem> listItems) {
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

            textViewServiceName = itemView.findViewById(R.id.service_name);
            textViewServiceDescription = itemView.findViewById(R.id.service_description);
            serviceImage = itemView.findViewById(R.id.service_image);

        }
    }

}

//public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ViewHolder> {
//
//    private ArrayList<Service> arrayList;
//    private Context context;
//    private ClientHomeFragment clientHomeFragment;
//
//    // creating a constructor.
//    public ServicesRecyclerViewAdapter(ClientHomeFragment ClientHomeFragment, ArrayList<Service> newsModalArrayList, Context context) {
//        this.clientHomeFragment = ClientHomeFragment;
//        this.arrayList = newsModalArrayList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ServicesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // passing our layout file for displaying our card item
//        return new ServicesRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_services, parent, false));
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ServicesRecyclerViewAdapter.ViewHolder holder, int position) {
//        // on below line we are setting data to our ui components.
//        Service model = arrayList.get(position);
//
//        holder.nameTV.setText(model.getName());
//
//        String link = DefaultSettings.URL + DefaultSettings.URL_SERVICE_IMAGE + model.getImage();
//
//        Picasso.with(context).load(link).into(holder.imageIV);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        // returning the size of array list
//        return arrayList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        // creating variables for our text view.
//        public TextView nameTV;
//
//        public ImageView imageIV;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            // initializing our text view
//            nameTV = itemView.findViewById(R.id.service_name);
//
//            imageIV = itemView.findViewById(R.id.iv_image);
//
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//
//            int position = getAdapterPosition();
//
//            if (position != RecyclerView.NO_POSITION) {
//
//                Service service = arrayList.get(position);
//
//            }
//
//        }
//
//    }
//}
