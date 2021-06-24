package com.example.proyectodam1.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodam1.R;
import com.example.proyectodam1.activities.ChatActivity;
import com.example.proyectodam1.models.Chat;
import com.example.proyectodam1.models.Message;
import com.example.proyectodam1.models.User;
import com.example.proyectodam1.providers.AuthProvider;
import com.example.proyectodam1.providers.UsersProvider;
import com.example.proyectodam1.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {

    Context context;
    AuthProvider authProvider;
    UsersProvider usersProvider;
    User user;
    ListenerRegistration listenerRegistration;

    public MessagesAdapter(FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
        authProvider = new AuthProvider();
        usersProvider = new UsersProvider();
        user = new User();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Message message) {
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewDate.setText(RelativeTime.timeFormatAMPM(message.getTimestamp(), context));

        if (message.getIdSender().equals(authProvider.getId())) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(100,0,0,0);
            holder.mlinearLayoutMessage.setLayoutParams(params);
            holder.mlinearLayoutMessage.setPadding(30,20,30,20);
            holder.mlinearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.bubble_corner_right));
            holder.textViewMessage.setTextColor(Color.BLACK);
            holder.textViewDate.setTextColor(Color.DKGRAY);
            holder.imageViewCheck.setVisibility(View.VISIBLE);

            if (message.getStatus().equals("Enviado")) {
                holder.imageViewCheck.setImageResource(R.drawable.icon_double_check_gray);
            }
            else if (message.getStatus().equals("Visto")) {
                holder.imageViewCheck.setImageResource(R.drawable.icon_double_check_blue);
            }
        }
        else  {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0,0,100,0);
            holder.mlinearLayoutMessage.setLayoutParams(params);
            holder.mlinearLayoutMessage.setPadding(50,20,30,20);
            holder.mlinearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.bubble_corner_left));
            holder.textViewMessage.setTextColor(Color.BLACK);
            holder.textViewDate.setTextColor(Color.DKGRAY);
            holder.imageViewCheck.setVisibility(View.GONE);
        }
    }

    public ListenerRegistration getListener() {
        return listenerRegistration;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage;
        TextView textViewDate;
        ImageView imageViewCheck;
        LinearLayout mlinearLayoutMessage;

        View myView;

        public ViewHolder(View view) {
            super(view);
            myView = view;

            textViewMessage = view.findViewById(R.id.tvMessage);
            textViewDate = view.findViewById(R.id.tvDate);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
            mlinearLayoutMessage = view.findViewById(R.id.linearLayoutMessage);

        }

    }

}
