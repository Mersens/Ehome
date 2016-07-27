package com.zzu.ehome.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;

import java.util.List;

/**
 * Created by zzu on 2016/4/27.
 */
public class GridViewImgAdapter extends BaseListAdapter<String> {

    private List<String> mList;

    public GridViewImgAdapter(Context context, List<String> mList) {
        super(context, mList);
        this.mList = mList;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_image_1, parent, false);
            holder.img = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String data = mList.get(position);
        Log.i("ssssvvvvvvvv",data);
        ImageLoader.getInstance().displayImage(data, holder.img);
        return convertView;
    }

    public static class ViewHolder {
        private ImageView img;
    }

}
