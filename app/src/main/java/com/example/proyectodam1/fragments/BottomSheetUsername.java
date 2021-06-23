package com.example.proyectodam1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectodam1.R;
import com.example.proyectodam1.activities.ProfileActivity;
import com.example.proyectodam1.providers.AuthProvider;
import com.example.proyectodam1.providers.ImageProvider;
import com.example.proyectodam1.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetUsername extends BottomSheetDialogFragment {

    ImageProvider mImageProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUserProvider;

    Button mButtonSave;
    Button mButtonCancel;
    EditText mEditTextUsername;

    String userName;

    public static BottomSheetUsername newInstance(String userName) {
        BottomSheetUsername bottomSheetSelectImage = new BottomSheetUsername();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        bottomSheetSelectImage.setArguments(args);
        return bottomSheetSelectImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userName = getArguments().getString("userName");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_username, container, false);
        mButtonSave = view.findViewById(R.id.btnSave);
        mButtonCancel = view.findViewById(R.id.btnCancel);
        mEditTextUsername = view.findViewById(R.id.editTextUsername);
        mEditTextUsername.setText(userName);

        mImageProvider = new ImageProvider();
        mUserProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserName();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void updateUserName() {
        String userName = mEditTextUsername.getText().toString();
        if (!userName.equals("")) {
            mUserProvider.updateUserName(mAuthProvider.getId(), userName).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dismiss();
                    Toast.makeText(getContext(), "El nombre de Usuario se actualizó", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
