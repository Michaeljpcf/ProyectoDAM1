package com.example.proyectodam1.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam1.R;
import com.example.proyectodam1.fragments.BottomSheetInfo;
import com.example.proyectodam1.fragments.BottomSheetSelectImage;
import com.example.proyectodam1.fragments.BottomSheetUsername;
import com.example.proyectodam1.models.User;
import com.example.proyectodam1.providers.AuthProvider;
import com.example.proyectodam1.providers.ImageProvider;
import com.example.proyectodam1.providers.UsersProvider;
import com.example.proyectodam1.utils.AppBackgroundHelper;
import com.example.proyectodam1.utils.MyToolbar;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton mSelectImage;
    BottomSheetSelectImage mBottomSheetSelectImage;
    BottomSheetUsername mBottomSheetUsername;
    BottomSheetInfo mBottomSheetInfo;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ImageProvider mImageProvider;

    TextView mTvUserName;
    TextView mTvPhone;
    TextView mTvInfo;

    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewEditUsername;
    ImageView mImageViewEditInfo;

    User mUser;

    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();
    File mImageFile;

    ListenerRegistration mListenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MyToolbar.show(this, "Perfil", true);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        mTvUserName = findViewById(R.id.tvUserName);
        mTvPhone = findViewById(R.id.tvPhone);
        mTvInfo = findViewById(R.id.tvStatus);

        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mImageViewEditUsername = findViewById(R.id.imageViewEditUsername);
        mImageViewEditInfo = findViewById(R.id.imageViewEditInfo);

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        mSelectImage = findViewById(R.id.fabSelectImage);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetSelectImage();
            }
        });

        mImageViewEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetUsername();
            }
        });

        mImageViewEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetEditInfo();
            }
        });

        getUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppBackgroundHelper.online(ProfileActivity.this, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppBackgroundHelper.online(ProfileActivity.this, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    private void getUserInfo() {
        mListenerRegistration = mUsersProvider.getUserInfo(mAuthProvider.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        mUser = documentSnapshot.toObject(User.class);
                        mTvUserName.setText(mUser.getUserName());
                        mTvPhone.setText(mUser.getCel());
                        mTvInfo.setText(mUser.getInfo());
                        if (mUser.getImage() != null) {
                            if (!mUser.getImage().equals("")) {
                                Picasso.with(ProfileActivity.this).load(mUser.getImage()).into(mCircleImageViewProfile);
                            }
                            else {
                                setImageDefault();
                            }
                        }
                        else {
                            setImageDefault();
                        }
                    }
                }
            }
        });
    }

    private void openBottomSheetSelectImage() {
        if (mUser != null) {
            mBottomSheetSelectImage = BottomSheetSelectImage.newInstance(mUser.getImage());
            mBottomSheetSelectImage.show(getSupportFragmentManager(), mBottomSheetSelectImage.getTag());
        }
        else {
            Toast.makeText(this, "La información no se pudo cargar", Toast.LENGTH_SHORT).show();
        }
    }

    private void openBottomSheetEditInfo() {
        if (mUser != null) {
            mBottomSheetInfo = BottomSheetInfo.newInstance(mUser.getInfo());
            mBottomSheetInfo.show(getSupportFragmentManager(), mBottomSheetInfo.getTag());
        }
        else {
            Toast.makeText(this, "La información no se pudo cargar", Toast.LENGTH_SHORT).show();
        }
    }

    private void openBottomSheetUsername() {
        if (mUser != null) {
            mBottomSheetUsername = mBottomSheetUsername.newInstance(mUser.getUserName());
            mBottomSheetUsername.show(getSupportFragmentManager(), mBottomSheetUsername.getTag());
        }
        else {
            Toast.makeText(this, "La información no se pudo cargar", Toast.LENGTH_SHORT).show();
        }
    }

    public void setImageDefault() {
        mCircleImageViewProfile.setImageResource(R.drawable.ic_person_white);
    }

    public void startPix() {
        Pix.start(ProfileActivity.this, mOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile = new File(mReturnValues.get(0));
            mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ProfileActivity.this, mOptions);
                } else {
                    Toast.makeText(ProfileActivity.this, "Debes conceder los permisos para acceder a la cámara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void saveImage() {
        mImageProvider = new ImageProvider();
        mImageProvider.save(ProfileActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            mUsersProvider.updateImage(mAuthProvider.getId(), url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, "La imagen se actualizó correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(ProfileActivity.this, "No se puedo almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
