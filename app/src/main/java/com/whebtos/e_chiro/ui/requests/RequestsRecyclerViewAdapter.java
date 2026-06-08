package com.whebtos.e_chiro.ui.requests;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whebtos.e_chiro.R;

import java.util.List;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder> {

private List<Request> listItemList;
private Context context;
private RequestsRecyclerViewAdapter.ItemClickListener mItemListener;

public RequestsRecyclerViewAdapter(List<Request> list, Context context, ItemClickListener itemClickListener) {
        this.listItemList = list;
        this.context = context;
        this.mItemListener = itemClickListener;
        }

public RequestsRecyclerViewAdapter(Context context, List<Request> list) {
        this.context = context;
        this.listItemList = list;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_list,parent,false);
        return new ViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request list = listItemList.get(position);


        holder.textViewServiceDescription.setText(list.getServiceDescription());

        holder.textViewServiceLatitude.setText(list.getServiceLatitude());

        holder.textViewServiceLongitude.setText(list.getServiceLongitude());

        //Picasso.with(context).load(list.getServiceImage()).into(holder.serviceImage);

        holder.itemView.setOnClickListener(view ->
        {
        mItemListener.onItemClick(listItemList.get(position));
        });

        }

@Override
public int getItemCount() {
        if(listItemList==null) return 0;
        return listItemList.size();
        }

public interface ItemClickListener
{
    void onItemClick(Request listItemClick);
}

public class ViewHolder extends RecyclerView.ViewHolder
{
    public TextView textViewServiceDescription;
    public TextView textViewServiceLatitude;
    public TextView textViewServiceLongitude;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewServiceDescription = itemView.findViewById(R.id.request_service_description);
        textViewServiceLatitude = itemView.findViewById(R.id.request_latitude);
        textViewServiceLongitude = itemView.findViewById(R.id.request_longitude);

    }
}

}
