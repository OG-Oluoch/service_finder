package com.whebtos.e_chiro.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whebtos.e_chiro.R;

import java.util.ArrayList;

public class ClientNotificationAdapter extends RecyclerView.Adapter<ClientNotificationAdapter.ViewHolder> {


    private ArrayList<Notification> notificationsList = new ArrayList<>();
    //private List<String> notificationsList;
    private LayoutInflater inflater;
    private ItemClickListener mItemListener;

    public ClientNotificationAdapter(Context context, ArrayList<Notification> notificationsList, ItemClickListener itemClickListener) {
        this.notificationsList = notificationsList;
        this.inflater = LayoutInflater.from(context);
        this.mItemListener = itemClickListener;
    }

//    public NotificationAdapter(ArrayList<Notification> notifications_list, Context context, ArrayList<Notification> notifications_list1) {
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.client_notifications_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification model = notificationsList.get(position);
        holder.titleTextView.setText(model.getTitle());
        holder.contentTextView.setText(model.getContent());
        holder.idTextView.setText(model.getId());
        holder.dateCreatedTextView.setText(model.getDateCreated());
        //holder.requestIDTextVIew.setText(model.get);

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public interface ItemClickListener
    {
        void onItemClick(Notification listItemClick);

        void onCancelBidButtonClick(Notification notification);

        void onProceedwithBidButtonClick(Notification notification);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView idTextView;
        TextView dateCreatedTextView;
        TextView requestIDTextVIew;
        Button proceed_with_bid;
        Button cancel_bid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notification_title);
            contentTextView = itemView.findViewById(R.id.notification_content);
            idTextView = itemView.findViewById(R.id.notification_ID);
            dateCreatedTextView = itemView.findViewById(R.id.notification_dateCreated);
            requestIDTextVIew = itemView.findViewById(R.id.requestID);
            proceed_with_bid = itemView.findViewById(R.id.proceed_with_bid);
            cancel_bid = itemView.findViewById(R.id.cancel_bid);

            // Set button click listeners
            proceed_with_bid.setOnClickListener(v -> {
                if (mItemListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mItemListener.onProceedwithBidButtonClick(notificationsList.get(position));
                    }
                }
            });

            cancel_bid.setOnClickListener(v -> {
                if (mItemListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mItemListener.onCancelBidButtonClick(notificationsList.get(position));
                    }
                }
            });

            itemView.setOnClickListener(v -> {
                if (mItemListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mItemListener.onItemClick(notificationsList.get(position));
                    }
                }
            });
        }

//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.notification_title);
//            contentTextView = itemView.findViewById(R.id.notification_content);
//            idTextView = itemView.findViewById(R.id.notification_ID);
//            dateCreatedTextView = itemView.findViewById(R.id.notification_dateCreated);
//            requestIDTextVIew = itemView.findViewById(R.id.requestID);
//        }
    }
}
