package com.chiemy.app.cashier.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.chiemy.app.cashier.CustomApplication;
import com.chiemy.app.cashier.R;
import com.chiemy.app.cashier.bean.MyUser;
import com.chiemy.app.cashier.utils.ChangeAvatarUtil;
import com.chiemy.app.cashier.utils.PixelUtil;
import com.chiemy.app.cashier.utils.RoundDrawableUtil;
import com.chiemy.app.cashier.utils.SelectPicUtil;

import java.io.File;
import java.lang.ref.WeakReference;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private View headerView;
    private ImageView userAvatarIv;
    private int avatarSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, "1a707ef2b3e4bfa3cd35e5b62637f39f");
        avatarSize = PixelUtil.dp2px(80, this);

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

    /**
     * 初始化NavigationView头部View
     */
    private void initHeaderView(){
        final MyUser user = CustomApplication.getUser();
        userAvatarIv = (ImageView) headerView.findViewById(R.id.imageView);
        userAvatarIv.setOnClickListener(this);

        View logoutView = headerView.findViewById(R.id.btn_logout);
        logoutView.setOnClickListener(this);
        TextView userNameTv = (TextView) headerView.findViewById(R.id.tv_username);
        if (user != null){
            logoutView.setVisibility(View.VISIBLE);
            userNameTv.setText(user.getUsername());
            if (CustomApplication.isDefaultUser()){
                userAvatarIv.setImageResource(R.drawable.ic_account_circle_24dp);
                logoutView.setVisibility(View.GONE);
            }else{
                updateAvatar(user.avatar);
            }
        }
    }

    /**
     * 更新头像
     * @param avatarPath
     */
    private void updateAvatar(String avatarPath){
        RoundDrawableUtil.loadRoundImage(this, avatarPath, avatarSize, userAvatarIv);
    }

    /**
     * 获取当前用户
     * @return
     */
    private MyUser getUser(){
        return CustomApplication.getUser();
    }

    /**
     * 获取当前用户id
     * @return
     */
    private String getUserId(){
        return getUser().getObjectId();
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
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
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
                if (CustomApplication.isDefaultUser()){
                    startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
                }else{
                    showChangeAvatarDialog();
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

    /**
     * 修改头像对话框
     */
    private void showChangeAvatarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.change_avatar));
        builder.setItems(getResources().getStringArray(R.array.array_item_change_avatar_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ChangeAvatarUtil.takePhoto(MainActivity.this, CustomApplication.getUser().getObjectId());
                        break;
                    case 1:
                        ChangeAvatarUtil.selectPicture(MainActivity.this);
                        break;
                }
            }
        });
        //builder.setNegativeButton(getString(android.R.string.cancel), null);
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_LOGIN){
                initHeaderView();
            }else if (requestCode == ChangeAvatarUtil.REQUEST_TAKE_PHOTO){
                String avatarPath = ChangeAvatarUtil.getAvatarImagePath(getUserId());
                uploadAvatar(avatarPath);
            }else if (requestCode == ChangeAvatarUtil.REQUEST_SELECT_PIC){
                Uri selectedImage = data.getData();
                String avatarPath = SelectPicUtil.getPath(this, selectedImage);
                uploadAvatar(avatarPath);
            }
        }
    }

    private void uploadAvatar(String path){
        showAlertAvatarProgressDialog();
        ChangeAvatarUtil.compressAvatar(path, getUserId(), avatarSize, 90, new ChangeAvatarUtil.CompressListener() {
            @Override
            public void onCompressFinish(String path) {
                BmobProFile bmobFile = BmobProFile.getInstance(MainActivity.this.getApplicationContext());
                bmobFile.upload(path, new MyUploadFileListener(MainActivity.this));
            }
        });
    }

    private ProgressDialog alertAvatarProgressDialog;
    private void showAlertAvatarProgressDialog(){
        alertAvatarProgressDialog = showProgressDialog(getString(R.string.changing_avatar));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static class MyUploadFileListener implements UploadListener {
        private WeakReference<MainActivity> weakReference;
        private Context context;
        public MyUploadFileListener(MainActivity activity){
            weakReference = new WeakReference<MainActivity>(activity);
            this.context = activity.getApplicationContext();
        }

        @Override
        public void onSuccess(String fileName, String url, final BmobFile bmobFile) {
            final MyUser user = CustomApplication.getUser();
            user.avatar = bmobFile.getFileUrl(context);
            user.update(context, user.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    String filePath = ChangeAvatarUtil.getAvatarImagePath(user.getObjectId());
                    File file = new File(filePath);
                    file.delete();
                    MainActivity activity = weakReference.get();
                    if (activity != null){
                        activity.alertAvatarProgressDialog.dismiss();
                        activity.updateAvatar(bmobFile.getFileUrl(context));
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    MainActivity activity = weakReference.get();
                    if (activity != null){
                        activity.alertAvatarProgressDialog.dismiss();
                        Toast.makeText(activity, "修改失败(" + i + ")", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onProgress(int i) {
        }

        @Override
        public void onError(int i, String s) {
            Log.d("-", i + ", " + s);
            MainActivity activity = weakReference.get();
            if (activity != null){
                activity.alertAvatarProgressDialog.dismiss();
                Toast.makeText(activity, "修改失败(" + i + ")", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
