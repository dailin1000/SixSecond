package com.sixmediaplayer.www;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sixmediaplayer.www.fragmnets.LocalMusicFragment;
import com.sixmediaplayer.www.fragmnets.ShowFragment;
import com.sixmediaplayer.www.fragmnets.head.MyFragment;
import com.sixmediaplayer.www.interfaces.OnFragmentInteractionListener;
import com.sixmediaplayer.www.utils.music.MusicLoader;
import com.sixmediaplayer.www.MusicService.NatureBinder;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;


public class UIActivity extends FragmentActivity implements
        View.OnClickListener,OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private long lastTime=0;
    private SlidingMenu slidingMenu;

    private TextView tvCurrentMusic;
    private ArrayList<MusicLoader.MusicInfo> musicList;
    private int currentMusic; // The music that is playing.
    private int currentPosition; //The position of the music is playing.
    private int currentMax;

    private ImageView btnStartStop;
    private ImageView btnNext;

    private ProgressReceiver progressReceiver;
    private NatureBinder natureBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            natureBinder = (NatureBinder) service;
        }
    };

    public UIActivity() {
    }

    private void connectToNatureService(){
        Intent intent = new Intent(UIActivity.this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    public void onMenuRadioButtonClick(){
        RadioGroup menu_radiogroup = (RadioGroup) findViewById(R.id.menu_radiogroup);
        menu_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.menu_button_set_model:
                        PopupWindow popupWindow = new PopupWindow(UIActivity.this);
                        DisplayMetrics metrics=new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        int w = metrics.widthPixels;
                        int h = metrics.heightPixels;
                        int width=w/5*4;
                        int heigth=h/4*3;
                        popupWindow.setWidth(width);
                        popupWindow.setHeight(heigth);
                        View v = getLayoutInflater().inflate(R.layout.activity_menu_set, null);
                        popupWindow.setContentView(v);
                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);

                        break;
                    case R.id.menu_button_night_model:

                        break;
                    case R.id.menu_button_sleep_model:

                        break;
                    case R.id.menu_button_change_backgroup:

                        break;


                    case R.id.menu_button_exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(UIActivity.this);
                        AlertDialog dialog = builder.setTitle("操作")
                                .setIcon(R.drawable.ic_launcher)
                                .setMessage("确认退出应用程序吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        System.exit(0);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();
                        break;

                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ShowFragment f = new ShowFragment();
        ft.replace(R.id.small_container,f).commit();
        onCreateSlidingMenu();
        MusicLoader musicLoader = MusicLoader.instance(getContentResolver());
        musicList = (ArrayList<MusicLoader.MusicInfo>) musicLoader.getMusicList();
        connectToNatureService();
        initComponents();
        //侧滑菜单功能
        onMenuRadioButtonClick();
    }

    @Override
    public void onFragmentInteraction(String TAG, int position) {
        if (TAG.equals("MyFragment")){

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_left_enter,R.anim.activity_trnslate_out);
            LocalMusicFragment f = LocalMusicFragment.newInstance("", musicList);
            ft.replace(R.id.small_container,f);
            ft.addToBackStack("");
            ft.commit();
        }
        if(TAG.equals("LocalMusicFragment")){
            currentMusic = position;
            natureBinder.startPlay(currentMusic,0);
            if(natureBinder.isPlaying()){
                btnStartStop.setBackgroundResource(R.drawable.player_pause);
            }
        }
    }

    //左右侧滑菜单
    private void onCreateSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //侧滑方式(左右)
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setMenu(R.layout.ui_slidingmenu);
        //布局宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        slidingMenu.setBehindWidth(metrics.widthPixels * 3 / 4);
        //侧滑响应方式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //阴影宽度
        slidingMenu.setShadowWidth(50);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindScrollScale(0.35f);
        //设置滑动效果
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

    }

    public void onResume(){
        super.onResume();
        registerReceiver();
        if(natureBinder != null){
            if(natureBinder.isPlaying()){
                btnStartStop.setBackgroundResource(R.drawable.player_pause);
            }else{
                btnStartStop.setBackgroundResource(R.drawable.player_play);
            }
            natureBinder.notifyActivity();
        }
    }

    public void onPause(){
        super.onPause();
        unregisterReceiver(progressReceiver);
    }

    public void onStop(){
        super.onStop();
    }

    public void onDestroy(){
        super.onDestroy();
        if(natureBinder != null){
            unbindService(serviceConnection);
        }
    }

    //设置组件功能,(监听事件)
    private void initComponents(){
        tvCurrentMusic = (TextView) findViewById(R.id.tvCurrentMusic);

        btnStartStop = (ImageView)findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(this);

        btnNext = (ImageView)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        findViewById(R.id.ui_linear).setOnClickListener(this);
    }

    private void registerReceiver(){
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
        intentFilter.addAction(MusicService.ACTION_UPDATE_DURATION);
        intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
        registerReceiver(progressReceiver, intentFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartStop:
                play(currentMusic,R.id.btnStartStop);
                break;
            case R.id.btnNext:
                natureBinder.toNext();
                btnStartStop.setBackgroundResource(R.drawable.player_pause);
                break;
            case R.id.ui_linear:
                Intent intent = new Intent(UIActivity.this,PlayMusicActivity.class);
//                intent.putExtra(PlayMusicActivity.MUSIC_LENGTH, currentMax);
//                intent.putExtra(PlayMusicActivity.CURRENT_MUSIC, currentMusic);
//                intent.putExtra(PlayMusicActivity.CURRENT_POSITION, currentPosition);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_trnslate_in,R.anim.activity_trnslate_out);
                break;
        }
    }

    private void play(int position, int resId){
        if(natureBinder.isPlaying()){
            natureBinder.stopPlay();
            btnStartStop.setBackgroundResource(R.drawable.player_play);
        }else{
            natureBinder.startPlay(position,currentPosition);
            btnStartStop.setBackgroundResource(R.drawable.player_pause);
        }
    }

    class ProgressReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MusicService.ACTION_UPDATE_PROGRESS.equals(action)){
                int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, 0);
                if(progress > 0){
                    currentPosition = progress; // Remember the current position
                }
            }else if(MusicService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)){
                //Retrive the current music and get the title to show on top of the screen.
                currentMusic = intent.getIntExtra(MusicService.ACTION_UPDATE_CURRENT_MUSIC, 0);
                tvCurrentMusic.setText(musicList.get(currentMusic).getTitle());
            }else if(MusicService.ACTION_UPDATE_DURATION.equals(action)){
                //Receive the duration and show under the progress bar
                //Why do this ? because from the ContentResolver, the duration is zero.
                currentMax = intent.getIntExtra(MusicService.ACTION_UPDATE_DURATION, 0);
                int max = currentMax / 1000;
            }
        }
    }
}
