package com.example.proyectodam1.providers;

import com.example.proyectodam1.models.Chat;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ChatsProvider {
    CollectionReference mCollection;

    public ChatsProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("chats");

    }
    public Task<Void>create(Chat chat){
        return mCollection.document().set(chat);
    }
    public Query getUserChats(String idUser){
        return mCollection.whereArrayContains("ids", idUser);
    }


    public Query getChatByUser1andUser2(String idUser1, String idUser2){
        ArrayList<String> ids = new ArrayList<>();
        ids.add(idUser1 + idUser2);
        ids.add(idUser2 + idUser1);
        return mCollection.whereIn("id", ids);
    }
}
