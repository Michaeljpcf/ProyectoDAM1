package com.example.proyectodam1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectodam1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetSelectImage extends BottomSheetDialogFragment {

    public static BottomSheetSelectImage newInstance() {
        BottomSheetSelectImage bottomSheetSelectImage = new BottomSheetSelectImage();
        return bottomSheetSelectImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_select_image, container, false);

        return view;
    }
}
