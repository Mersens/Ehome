package com.zzu.ehome.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.bean.User;
import com.zzu.ehome.R;

/**
 * Created by Administrator on 2016/4/6.
 */
public class UserAdapter extends BaseListAdapter<User>{
    private Handler handler;
    private  Context context;
    public UserAdapter(Context context) {
        super(context);
    }
    public UserAdapter(Context context,Handler handler) {
        super(context);
        this.handler=handler;
        this.context=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        User mItem = getItem(position);
        if(convertView ==null){
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, null);
            holder.textView = (TextView)convertView.findViewById(R.id.item_text);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(mItem.getMobile()+"");
        final int p=position;
        holder.textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = new Message();
                Bundle data = new Bundle();

                data.putInt("selIndex", p);
                msg.setData(data);
                msg.what = 1;

                handler.sendMessage(msg);
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView textView;

    }
}
