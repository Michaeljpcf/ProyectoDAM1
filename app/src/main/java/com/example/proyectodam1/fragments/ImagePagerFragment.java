package com.example.proyectodam1.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.proyectodam1.R;
import com.example.proyectodam1.adapters.CardAdapter;

import java.io.File;

public class ImagePagerFragment extends Fragment {

    View mView;
    CardView mCardViewOption;
    ImageView mImagenViewPicture;
    ImageView mImagenViewBack;
    LinearLayout mLinearLayoutImgPager;


    public static Fragment newInstance(int position, String imagePath, int size) {
        ImagePagerFragment fragment = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("size", size);
        args.putString("image", imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_image_pager, container, false);
        mCardViewOption = mView.findViewById(R.id.cardViewOptions);
        mCardViewOption.setMaxCardElevation(mCardViewOption.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        mImagenViewPicture = mView.findViewById(R.id.imageViewPicture);
        mImagenViewBack = mView.findViewById(R.id.imageViewBack);
        mLinearLayoutImgPager = mView.findViewById(R.id.linearLayoutViewPager);

        String imagenPath = getArguments().getString("image");
        int size = getArguments().getInt("size");
        if(size == 1){
            mLinearLayoutImgPager.setPadding(0,0,0,0);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mImagenViewBack.getLayoutParams();
            params.leftMargin = 10;
            params.topMargin = 35;
        }

        if(imagenPath != null){
            File file = new File(imagenPath);
            mImagenViewPicture.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        mImagenViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return mView;
    }

    public CardView getCardView(){
        return mCardViewOption;
    }
}