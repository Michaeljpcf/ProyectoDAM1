package com.example.proyectodam1.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectodam1.R;
import com.example.proyectodam1.adapters.ChatsAdapter;
import com.example.proyectodam1.adapters.MessagesAdapter;
import com.example.proyectodam1.models.Chat;
import com.example.proyectodam1.models.Message;
import com.example.proyectodam1.models.User;
import com.example.proyectodam1.providers.AuthProvider;
import com.example.proyectodam1.providers.ChatsProvider;
import com.example.proyectodam1.providers.MessagesProvider;
import com.example.proyectodam1.providers.UsersProvider;
import com.example.proyectodam1.utils.AppBackgroundHelper;
import com.example.proyectodam1.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String mExtraIdUser;
    String mExtraIdChat;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    MessagesProvider mMessagesProvider;

    ImageView mImageViewBack;
    TextView mTextViewUsername;
    TextView mTextViewOnline;
    CircleImageView mCircleImageUser;
    EditText mEditTextMessage;
    ImageView mImageViewSend;

    ImageView mImageViewSelectPictures;

    MessagesAdapter mAdapter;
    RecyclerView mRecyclerViewMessage;
    LinearLayoutManager mLinearLayoutManager;

    Timer mTimer;

    ListenerRegistration mListenerChat;

    User mUser;

    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mExtraIdUser = getIntent().getStringExtra("idUser");
        mExtraIdChat = getIntent().getStringExtra("idChat");

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();
        mMessagesProvider = new MessagesProvider();

        mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSend = findViewById(R.id.imageViewSend);
        mImageViewSelectPictures = findViewById(R.id.imageViewSelectPicture);

        mRecyclerViewMessage = findViewById(R.id.rvMessage);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewMessage.setLayoutManager(mLinearLayoutManager);

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(5)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)
                .setExcludeVideos(true)
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");

        showChatToolbar(R.layout.chat_toolbar);
        gerUserInfo();

        checkIfExistsChat();
        setWriting();

        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mImageViewSelectPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPix();
            }
        });

    }

    private void setWriting() {
        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //Esta Escribiendo
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTimer != null) {
                    if (mExtraIdChat != null) {
                        mChatsProvider.updateWriting(mExtraIdChat, mAuthProvider.getId());
                        mTimer.cancel();
                    }
                }
            }

            //Dejó de escribir
            @Override
            public void afterTextChanged(Editable s) {
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mExtraIdChat != null) {
                            mChatsProvider.updateWriting(mExtraIdChat, "");
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppBackgroundHelper.online(ChatActivity.this, true);
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAdapter != null){
            mAdapter.stopListening();
        }
        AppBackgroundHelper.online(ChatActivity.this, false);
        mAdapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListenerChat != null) {
            mListenerChat.remove();
        }
    }

    private void startPix() {
        Pix.start(ChatActivity.this, mOptions);
    }


    private void createMessage() {
        String textMessage = mEditTextMessage.getText().toString();
        if (!textMessage.equals("")) {
            Message message = new Message();
            message.setIdChat(mExtraIdChat);
            message.setIdSender(mAuthProvider.getId());
            message.setIdReceiver(mExtraIdUser);
            message.setMessage(textMessage);
            message.setStatus("Enviado");
            message.setTimestamp(new Date().getTime());

            mMessagesProvider.create(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mEditTextMessage.setText("");
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    mChatsProvider.updateNumberMessage(mExtraIdChat);
                    //Toast.makeText(ChatActivity.this, "El mensaje se creo correctamente", Toast.LENGTH_SHORT).show();
                }
            });
        }

        else {
            Toast.makeText(this, "Ingresa el mensaje", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfExistsChat() {
        mChatsProvider.getChatByUser1andUser2(mExtraIdUser, mAuthProvider.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    if(queryDocumentSnapshots.size() == 0){
                        createChat();
                    }
                    else{
                        mExtraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                        getMessagesByChat();
                        updateStatus();
                        getChatInfo();
                        //Toast.makeText(ChatActivity.this, "El chat entre dos usuarios ya existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getChatInfo() {
        mListenerChat = mChatsProvider.getChatById(mExtraIdChat).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        Chat chat = documentSnapshot.toObject(Chat.class);
                        if (chat.getWriting() != null) {
                            if (!chat.getWriting().equals("")) {
                                if (!chat.getWriting().equals(mAuthProvider.getId())) {
                                    mTextViewOnline.setText("Escribiendo...");
                                }
                                else if (mUser != null){
                                    if (mUser.isOnline()) {
                                        mTextViewOnline.setText("En línea");;
                                    }
                                    else {
                                        String relativeTime = RelativeTime.getTimeAgo(mUser.getLastConnect(), ChatActivity.this);
                                        mTextViewOnline.setText(relativeTime);
                                    }
                                }
                                else {
                                    mTextViewOnline.setText("");
                                }
                            }
                            else if (mUser != null){
                                if (mUser.isOnline()) {
                                    mTextViewOnline.setText("En línea");;
                                }
                                else {
                                    String relativeTime = RelativeTime.getTimeAgo(mUser.getLastConnect(), ChatActivity.this);
                                    mTextViewOnline.setText(relativeTime);
                                }
                            }
                            else {
                                mTextViewOnline.setText("");
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateStatus() {
        mMessagesProvider.getMessageNotRead(mExtraIdChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                    Message message = document.toObject(Message.class);
                    if (!message.getIdSender().equals(mAuthProvider.getId())) {
                        mMessagesProvider.updateStatus(message.getId(), "Visto");
                    }
                }
            }
        });
    }

    private void getMessagesByChat() {
        Query query = mMessagesProvider.getMessageByChat(mExtraIdChat);

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        mAdapter = new MessagesAdapter(options, ChatActivity.this);
        mRecyclerViewMessage.setAdapter(mAdapter);
        mAdapter.startListening();

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateStatus();

                int numberMessage = mAdapter.getItemCount();
                int lastMessagePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastMessagePosition == -1 || (positionStart >= (numberMessage - 1) && lastMessagePosition == (positionStart - 1))) {
                    mRecyclerViewMessage.scrollToPosition(positionStart);
                }
            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setId(mAuthProvider.getId() + mExtraIdUser);
        chat.setTimestamp(new Date().getTime());
        chat.setNumberMessage(0);
        chat.setWriting("");

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mAuthProvider.getId());
        ids.add(mExtraIdUser);

        chat.setIds(ids);
        mExtraIdChat = chat.getId();

        mChatsProvider.create(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                getMessagesByChat();
                //Toast.makeText(ChatActivity.this, "El chat se creo correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gerUserInfo() {
        mUsersProvider.getUserInfo(mExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if(documentSnapshot != null){
                        if(documentSnapshot.exists()){
                            mUser = documentSnapshot.toObject(User.class);
                            mTextViewUsername.setText(mUser.getUserName());
                            if(mUser.getImage() != null){
                                if(!mUser.getImage().equals("")){
                                    Picasso.with(ChatActivity.this).load(mUser.getImage()).into(mCircleImageUser);
                                }
                            }

                            if (mUser.isOnline()) {
                                mTextViewOnline.setText("En línea");;
                            }
                            else {
                                String relativeTime = RelativeTime.getTimeAgo(mUser.getLastConnect(), ChatActivity.this);
                                mTextViewOnline.setText(relativeTime);
                            }
                        }
                    }
            }
        });
    }

    private  void showChatToolbar(int resource ) {
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
        mTextViewOnline = view.findViewById(R.id.tvOnline);

        mImageViewBack.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
                finish(); } });
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                Intent intent = new Intent(ChatActivity.this, ConfirmImageSendActivity.class);
                intent.putExtra("data", mReturnValues);
                startActivity(intent);
            }
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Pix.start(ChatActivity.this, mOptions);
                    } else {
                        Toast.makeText(ChatActivity.this, "Debes conceder los permisos para acceder a la cámara", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
        }
    }