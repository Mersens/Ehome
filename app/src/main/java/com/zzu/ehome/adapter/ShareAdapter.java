package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;


/**
 * Created by Administrator on 2016/3/21.
 */
public class ShareAdapter extends BaseAdapter {
    private static String[] shareNames = new String[] { "微信", "微信朋友圈","QQ","QQ空间"};
    private int[] shareIcons = new int[] {R.drawable.sns_weichat_icon,R.drawable.sns_weifrent_icon,R.drawable.sns_qqfriends_icon,R.drawable.sns_qqzone_icon};


    private LayoutInflater inflater;

    public ShareAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return shareNames.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.share_item, null);
        }
        ImageView shareIcon = (ImageView) convertView.findViewById(R.id.share_icon);
        TextView shareTitle = (TextView) convertView.findViewById(R.id.share_title);
        shareIcon.setImageResource(shareIcons[position]);
        shareTitle.setText(shareNames[position]);

        return convertView;
    }

}
