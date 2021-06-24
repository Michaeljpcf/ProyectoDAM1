package com.example.proyectodam1.providers;

import com.example.proyectodam1.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
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

    public Query getAllUsersByName() {
        return mCollection.orderBy("userName");
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

    public Task<Void> updateImage(String id, String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("image", url);
        return mCollection.document(id).update(map);
    }

    public Task<Void> updateUserName(String id, String userName) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        return mCollection.document(id).update(map);
    }

    public Task<Void> updateInfo(String id, String info) {
        Map<String, Object> map = new HashMap<>();
        map.put("info", info);
        return mCollection.document(id).update(map);
    }

    public Task<Void> updateOnline(String idUser, boolean status) {
        Map<String, Object> map = new HashMap<>();
        map.put("online", status);
        map.put("lastConnect", new Date().getTime());
        return mCollection.document(idUser).update(map);
    }

}
