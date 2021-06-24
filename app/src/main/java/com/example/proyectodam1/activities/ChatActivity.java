package com.example.proyectodam1.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectodam1.R;
import com.example.proyectodam1.models.Chat;
import com.example.proyectodam1.models.User;
import com.example.proyectodam1.providers.AuthProvider;
import com.example.proyectodam1.providers.ChatsProvider;
import com.example.proyectodam1.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String mExtraIdUser;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;

    ImageView mImageViewBack;
    TextView mTextViewUsername;
    CircleImageView mCircleImageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mExtraIdUser = getIntent().getStringExtra("id");
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();

        showChatToolbar(R.layout.chat_toolbar);
        gerUserInfo();
        createChat();

        checkIfExistesChat();

    }

    private void checkIfExistesChat() {
        mChatsProvider.getChatByUser1andUser2(mExtraIdUser, mAuthProvider.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    if(queryDocumentSnapshots.size() == 0){
                        createChat();
                    }
                    else{
                        Toast.makeText(ChatActivity.this, "El chat entre usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setId(mAuthProvider.getId() + mExtraIdUser);
        chat.setTimestamp(new Date().getTime());

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mAuthProvider.getId());
        ids.add(mExtraIdUser);

        chat.setIds(ids);

        mChatsProvider.create(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ChatActivity.this, "El chat se creo correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gerUserInfo() {
        mUsersProvider.getUserInfo(mExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if(documentSnapshot != null){
                        if(documentSnapshot.exists()){
                            User user = documentSnapshot.toObject(User.class);
                            mTextViewUsername.setText(user.getUserName());
                            if(user.getImage() != null){
                                if(!user.getImage().equals("")){
                                    Picasso.with(ChatActivity.this).load(user.getImage()).into(mCircleImageUser);
                                }
                            }
                        }
                    }
            }
        });
    }

    private  void showChatToolbar(int resource ){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);
        actionBar.setCustomView(view);

        mImageViewBack = view.findViewById(R.id.imageViewBack);
        mTextViewUsername = view.findViewById(R.id.textViewUsername);
        mCircleImageUser = view.findViewById(R.id.circleImageUser);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}