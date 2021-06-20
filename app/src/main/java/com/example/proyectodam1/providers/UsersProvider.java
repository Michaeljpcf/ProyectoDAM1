package com.example.proyectodam1.providers;

import com.example.proyectodam1.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProvider {

    private CollectionReference mCollection;

    public UsersProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public DocumentReference getUserInfo(String id){
        return mCollection.document(id);
    }

    public Task<Void> create(User user) {
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getUserName());
        map.put("image", user.getImage());

        return mCollection.document(user.getId()).update(map);
    }

}