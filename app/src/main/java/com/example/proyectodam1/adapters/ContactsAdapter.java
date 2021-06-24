package com.example.proyectodam1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodam1.R;
import com.example.proyectodam1.activities.ChatActivity;
import com.example.proyectodam1.models.User;
import com.example.proyectodam1.providers.AuthProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends FirestoreRecyclerAdapter<User, ContactsAdapter.ViewHolder> {

    Context context;
    AuthProvider authProvider;


    public ContactsAdapter(FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
        authProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final User user) {

       if (user.getId().equals(authProvider.getId())){
           RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
           param.height = 0;
           param.width = LinearLayout.LayoutParams.MATCH_PARENT;
           param.topMargin = 0;
           param.bottomMargin = 0;
           holder.itemView.setVisibility(View.VISIBLE);
       }

        holder.textViewInfo.setText(user.getInfo());
        holder.textViewUserName.setText(user.getUserName());
        if (user.getImage() != null){
            if (!user.getImage().equals("")){
                Picasso.with(context).load(user.getImage()).into(holder.circleImageUser);
            }
            else {
                holder.circleImageUser.setImageResource(R.drawable.ic_person);
            }
        }
        else {
            holder.circleImageUser.setImageResource(R.drawable.ic_person);
        }

        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity(user.getId());
            }
        });

    }

    private void goToChatActivity(String id) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idUser", id);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contacts, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUserName;
        TextView textViewInfo;
        CircleImageView circleImageUser;
        View myView;

        public ViewHolder(View view) {
            super(view);
            myView = view;

            textViewUserName = view.findViewById(R.id.tvUserName);
            textViewInfo = view.findViewById(R.id.tvInfo);
            circleImageUser = view.findViewById(R.id.circleImageUser);

        }

    }

}
