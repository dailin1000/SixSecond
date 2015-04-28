package com.sixmediaplayer.www.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixmediaplayer.www.R;
import com.sixmediaplayer.www.interfaces.OnFragmentInteractionListener;
import com.sixmediaplayer.www.utils.music.FormatHelper;
import com.sixmediaplayer.www.utils.music.MusicLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaa on 15-4-27.
 */
public class MusicListAdapter extends BaseAdapter {
    private ArrayList<MusicLoader.MusicInfo> musicList;
    private Context context;
    private LayoutInflater inflater;
    private CheckBox checkBox2;
    private static final String THISNAME = "MusicListAdapter";
    private int count=0;

    public MusicListAdapter(Context context,ArrayList<MusicLoader.MusicInfo> musicList) {
        this.musicList = musicList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public MusicLoader.MusicInfo getItem(int position) {
            return musicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder =null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.music_list_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.list_item_portrait);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_title);
                viewHolder.artist = (TextView) convertView.findViewById(R.id.list_item_artist);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MusicLoader.MusicInfo musicInfo = musicList.get(position);
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            viewHolder.title.setText(musicInfo.getTitle());
            String duration = FormatHelper.formatDuration(musicInfo.getDuration());
            viewHolder.artist.setText(musicInfo.getArtist()+"   "+duration);
            count++;
            return convertView;
        }
    class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView artist;
    }

}
