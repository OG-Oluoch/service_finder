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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private ArrayList<Notification> notificationsList = new ArrayList<>();
    //private List<String> notificationsList;
    private LayoutInflater inflater;
    private ItemClickListener mItemListener;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationsList, ItemClickListener itemClickListener) {
        this.notificationsList = notificationsList;
        this.inflater = LayoutInflater.from(context);
        this.mItemListener = itemClickListener;
    }

//    public NotificationAdapter(ArrayList<Notification> notifications_list, Context context, ArrayList<Notification> notifications_list1) {
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notifications_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification model = notificationsList.get(position);
        holder.titleTextView.setText(model.getTitle());
        holder.contentTextView.setText(model.getContent());
        holder.idTextView.setText(model.getId());
        holder.dateCreatedTextView.setText(model.getDateCreated());
       // holder.requestIDTextVIew.setText(model.ge);

//        holder.itemView.setOnClickListener(view ->
//        {
//            mItemListener.onItemClick(notificationsList.get(position));
//        });

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public interface ItemClickListener
    {
        void onItemClick(Notification listItemClick);

        void onRejectBidButtonClick(Notification notification);

        void onMakeBidButtonClick(Notification notification);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView idTextView;
        TextView dateCreatedTextView;
        TextView requestIDTextVIew;
        Button makeBidButton;
        Button rejectBidButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notification_title);
            contentTextView = itemView.findViewById(R.id.notification_content);
            idTextView = itemView.findViewById(R.id.notification_ID);
            dateCreatedTextView = itemView.findViewById(R.id.notification_dateCreated);
           // requestIDTextVIew = itemView.findViewById(R.id.requestID);
            makeBidButton = itemView.findViewById(R.id.make_a_bid);
            rejectBidButton = itemView.findViewById(R.id.reject_a_bid);

            // Set button click listeners
            makeBidButton.setOnClickListener(v -> {
                if (mItemListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mItemListener.onMakeBidButtonClick(notificationsList.get(position));
                    }
                }
            });

            rejectBidButton.setOnClickListener(v -> {
                if (mItemListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mItemListener.onRejectBidButtonClick(notificationsList.get(position));
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
    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        TextView contentTextView;
//        TextView idTextView;
//        TextView dateCreatedTextView;
//        TextView requestIDTextVIew;
//        Button makeBidButton;
//        Button rejectBidButton;
//
////        public ViewHolder(@NonNull View itemView) {
////            super(itemView);
////            titleTextView = itemView.findViewById(R.id.notification_title);
////            contentTextView = itemView.findViewById(R.id.notification_content);
////            idTextView = itemView.findViewById(R.id.notification_ID);
////            dateCreatedTextView = itemView.findViewById(R.id.notification_dateCreated);
////            requestIDTextVIew = itemView.findViewById(R.id.requestID);
////            makeBidButton = itemView.findViewById(R.id.make_a_bid);
////            rejectBidButton = itemView.findViewById(R.id.reject_a_bid);
////
////            // Set button click listeners
////            makeBidButton.setOnClickListener(v -> {
////                if (itemClickListener != null) {
////                    int position = getAdapterPosition();
////                    if (position != RecyclerView.NO_POSITION) {
////                        itemClickListener.onMakeBidButtonClick(notificationsList.get(position));
////                    }
////                }
////            });
////
////            rejectBidButton.setOnClickListener(v -> {
////                if (itemClickListener != null) {
////                    int position = getAdapterPosition();
////                    if (position != RecyclerView.NO_POSITION) {
////                        itemClickListener.onRejectBidButtonClick(notificationsList.get(position));
////                    }
////                }
////            });
////
////            itemView.setOnClickListener(v -> {
////                if (itemClickListener != null) {
////                    int position = getAdapterPosition();
////                    if (position != RecyclerView.NO_POSITION) {
////                        itemClickListener.onItemClick(notificationsList.get(position));
////                    }
////                }
////            });
////        }
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.notification_title);
//            contentTextView = itemView.findViewById(R.id.notification_content);
//            idTextView = itemView.findViewById(R.id.notification_ID);
//            dateCreatedTextView = itemView.findViewById(R.id.notification_dateCreated);
//            requestIDTextVIew = itemView.findViewById(R.id.requestID);
//            makeBidButton = itemView.findViewById(R.id.make_a_bid);
//            rejectBidButton = itemView.findViewById(R.id.reject_a_bid);
//        }
//    }
}
