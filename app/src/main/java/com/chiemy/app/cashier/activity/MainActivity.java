package com.chiemy.app.cashier.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chiemy.app.cashier.CustomApplication;
import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.utils.RoundDrawableUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, "1a707ef2b3e4bfa3cd35e5b62637f39f");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PurchaseScanActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        initHeaderView();
    }

    private void initHeaderView(){
        final MyUser user = CustomApplication.getUser();
        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        View logoutView = headerView.findViewById(R.id.btn_logout);
        logoutView.setOnClickListener(this);
        TextView userNameTv = (TextView) headerView.findViewById(R.id.tv_username);
        if (user != null){
            logoutView.setVisibility(View.VISIBLE);
            userNameTv.setText(user.getUsername());
            RoundDrawableUtil.loadRoundImage(this, user.avatar, 500, 500, imageView);
        }else{
            imageView.setImageResource(R.drawable.ic_account_circle_24dp);
            userNameTv.setText(R.string.sign_in);
            logoutView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static final int REQUEST_LOGIN = 1;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                if (CustomApplication.getUser() == null){
                    startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
                }
                break;
            case R.id.btn_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.sure_to_logout));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BmobUser.logOut(getApplicationContext());
                initHeaderView();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_LOGIN){
                initHeaderView();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
