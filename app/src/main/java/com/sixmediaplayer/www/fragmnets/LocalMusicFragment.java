package com.sixmediaplayer.www.fragmnets;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sixmediaplayer.www.R;
import com.sixmediaplayer.www.adapters.MusicListAdapter;
import com.sixmediaplayer.www.interfaces.OnFragmentInteractionListener;
import com.sixmediaplayer.www.utils.music.MusicLoader;

import java.util.ArrayList;

public class LocalMusicFragment extends Fragment {

    private static final String ARG_PARAM1 = "mParam1";
    private static final String ARG_PARAM2 = "musicInfos";
    private static final String THISNAME = "LocalMusicFragment";
    private ListView listView;

    private ArrayList<MusicLoader.MusicInfo> musicInfos;
    private OnFragmentInteractionListener listener;
    private String mParam1;

    public LocalMusicFragment() {
    }
    public static LocalMusicFragment newInstance(String param1, ArrayList<MusicLoader.MusicInfo> musicInfos) {
        LocalMusicFragment fragment = new LocalMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelableArrayList(ARG_PARAM2,musicInfos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            musicInfos = getArguments().getParcelableArrayList(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localmusic, container, false);
        listView = (ListView) view.findViewById(R.id.localmusic_listview);
        if(musicInfos!=null){
            setListViewData();
        }
        return view;
    }
    public void setListViewData(){
        MusicListAdapter adapter = new MusicListAdapter(getActivity(), musicInfos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(listener!=null){
                    listener.onFragmentInteraction(THISNAME,position);
                }
            }
        });
    }

}
