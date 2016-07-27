package com.zzu.ehome.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.View;
import com.zzu.ehome.activity.DataChatActivity;

/**
 * Created by Administrator on 2016/6/16.
 */
public class TempGoView extends RelativeLayout{
    private Context mContext;
    float x1,y1,x2,y2;
    public TempGoView(Context context) {
        super(context);
        this.mContext=context;
    }

    public TempGoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }
    	public boolean onInterceptTouchEvent(MotionEvent motionEvent ) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();
                if (Math.abs(x1 - x2) < 6) {
                    Intent i=new Intent(mContext, DataChatActivity.class);
                    i.putExtra("position",0);
                   mContext.startActivity(i);
                    Toast.makeText(mContext, "滑动了-------------------", Toast.LENGTH_SHORT).show();
                    return false;// 距离较小，当作click事件来处理
                }
                if(Math.abs(x1 - x2) >60){ // 真正的onTouch事件
                    return true;// 返回true，不执行click事件
                }
            }
            return true;// 返回true，不执行click事件


	}


//    public boolean onTouchEvent(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:{
//                //记录按下的点的X坐标
//                startX = event.getRawX();
//                break;
//            }
//            case MotionEvent.ACTION_UP:
//                //记录抬起的点的X坐标， 若两次相等，触发了点击事件，否则是滑动事件
//                float LastX = event.getRawX();
//                if(startX == LastX){
//                    Intent i=new Intent(mContext, DataChatActivity.class);
//                    i.putExtra("position",0);
//                   mContext.startActivity(i);
//                }else{
//                    Toast.makeText(mContext, "滑动了", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//            case MotionEvent.ACTION_MOVE:{
//                break;
//            }
//
//        }
//        return false;
//    }
    //	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev ) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //记录按下的点的X坐标
//                startX = ev.getRawX();
//                break;
//
//            case MotionEvent.ACTION_UP:
//                //记录抬起的点的X坐标， 若两次相等，触发了点击事件，否则是滑动事件
//                float LastX = ev.getRawX();
//                if(startX == LastX){
//                    Intent i=new Intent(mContext, DataChatActivity.class);
//                    i.putExtra("position",0);
//                   mContext.startActivity(i);
//                }
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//
//
//        }
//
//
//		return true;
//	}




//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:{
//                //记录按下的点的X坐标
//                startX = event.getRawX();
//                break;
//            }
//            case MotionEvent.ACTION_UP:
//                //记录抬起的点的X坐标， 若两次相等，触发了点击事件，否则是滑动事件
//                float LastX = event.getRawX();
//                if(startX == LastX){
//                    Intent i=new Intent(mContext, DataChatActivity.class);
//                    i.putExtra("position",0);
//                    mContext.startActivity(i);
//                }
//
//                break;
//            case MotionEvent.ACTION_MOVE:{
//                break;
//            }
//
//        }
//        return false;
//    }

}
