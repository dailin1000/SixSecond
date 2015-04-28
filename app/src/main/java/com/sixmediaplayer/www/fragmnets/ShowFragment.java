package com.sixmediaplayer.www.fragmnets;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sixmediaplayer.www.R;
import com.sixmediaplayer.www.adapters.ShowPagerAdapter;
import com.sixmediaplayer.www.fragmnets.head.MyFragment;
import com.sixmediaplayer.www.utils.animation.DepthPageTransformer;
import com.sixmediaplayer.www.utils.animation.ZoomOutPageTransformer;

import java.util.ArrayList;

public class ShowFragment extends Fragment implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ViewPager pager;
    private ArrayList<Fragment> fragments;
    private RadioGroup radioGroup;

    public static ShowFragment newInstance(String param1, String param2) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        pager = (ViewPager) view.findViewById(R.id.showfragment_viewpager);
        fragments = new ArrayList<Fragment>();
        onCreateFragments();
        ShowPagerAdapter adapter =
                new ShowPagerAdapter(getChildFragmentManager(), fragments);
        pager.setPageTransformer(true,new DepthPageTransformer());

        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);

        radioGroup = (RadioGroup) view.findViewById(R.id.show_radiogroup);
        ((RadioButton)(radioGroup.getChildAt(0))).setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    private void onCreateFragments(){
        for(int i=0;i<4;i++){
            MyFragment myFragment = new MyFragment();
            fragments.add(myFragment);
        }
    }

    //positionOffset   滑动距离的百分比
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        RadioButton radio= (RadioButton) radioGroup.getChildAt(position);
        radio.setChecked(true);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.show_radionbutton0:
                pager.setCurrentItem(0);
                break;
            case R.id.show_radionbutton1:
                pager.setCurrentItem(1);
                break;
            case R.id.show_radionbutton2:
                pager.setCurrentItem(2);
                break;
            case R.id.show_radionbutton3:
                pager.setCurrentItem(3);
                break;
        }
    }
}
