package com.example.proyectodam1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectodam1.R;
import com.example.proyectodam1.adapters.ViewPagerAdapter;
import com.example.proyectodam1.fragments.ChatsFragment;
import com.example.proyectodam1.fragments.ContactsFragment;
import com.example.proyectodam1.fragments.PhotoFragment;
import com.example.proyectodam1.fragments.StatusFragment;
import com.example.proyectodam1.providers.AuthProvider;
import com.google.android.material.tabs.TabLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class HomeActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    AuthProvider mAuthProvider;
    MaterialSearchBar mSearchBar;

    TabLayout mTabLayout;
    ViewPager mViewPager;

    ChatsFragment mChatsFragments;
    ContactsFragment mContactsFragment;
    StatusFragment mStatusFragment;
    PhotoFragment mPhotoFragment;

    int mTabSelected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSearchBar = findViewById(R.id.searchBar);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        mViewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mChatsFragments = new ChatsFragment();
        mContactsFragment = new ContactsFragment();
        mStatusFragment = new StatusFragment();
        mPhotoFragment = new PhotoFragment();

        adapter.addFragment(mPhotoFragment, "");
        adapter.addFragment(mChatsFragments, "CHATS");
        adapter.addFragment(mStatusFragment, "ESTADOS");
        adapter.addFragment(mContactsFragment, "CONTACTOS");

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTabSelected);

        setupTabIcon();

        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemSignOut){
                    signOut();
                }
                else if (item.getItemId() == R.id.itemProfile){
                    goToProfile();
                }

                return true;
            }
        });

        mAuthProvider = new AuthProvider();
    }

    private void goToProfile() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void setupTabIcon() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_white);
        LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.weight = 0.5f;
        linearLayout.setLayoutParams(layoutParams);
    }

    private void signOut() {
        mAuthProvider.signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}
