package com.zzu.ehome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.Image;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.IOUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MonthDateView extends View {
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint;
    private Context mContext;
    private int mDayColor = Color.parseColor("#000000");
    private int mSelectDayColor = Color.parseColor("#ffffff");
    private int mSelectBGColor = Color.parseColor("#2ec2cc");
    private int mCurrentColor = Color.parseColor("#ff0000");
    private int mCurrYear;
    private int mCurrMonth;

    public int getmCurrYear() {
        return mCurrYear;
    }

    public void setmCurrYear(int mCurrYear) {
        this.mCurrYear = mCurrYear;
    }

    public int getmCurrMonth() {
        return mCurrMonth;
    }

    public void setmCurrMonth(int mCurrMonth) {
        this.mCurrMonth = mCurrMonth;
    }

    public int getmCurrDay() {
        return mCurrDay;
    }

    public void setmCurrDay(int mCurrDay) {
        this.mCurrDay = mCurrDay;
    }

    private int mCurrDay;
    private int mSelYear,mSelMonth,mSelDay;
    private int mColumnSize,mRowSize;
    private DisplayMetrics mDisplayMetrics;
    private int mDaySize = 18;
    private TextView tv_date,tv_week;
    private int weekRow;
    private int [][] daysString;
    private int mCircleRadius = 8;
    private DateClick dateClick;
    private int mCircleColor = Color.parseColor("#ff0000");
    private List<Integer> daysHasThingList;
    private Long dateCurr;
    private int isShowYear,isShowMonth,isShowDay,isshowx,isshowy;

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    private Boolean isShow=false;
    public MonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        this.mContext=context;
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
        String dayString;
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
        Log.d("DateView", "DateView:" + mSelMonth+"月1号周" + weekNumber);
        for(int day = 0;day < mMonthDays;day++){
            dayString = (day + 1) + "";
            int column = (day+weekNumber - 1) % 7;
            int row = (day+weekNumber - 1) / 7;
            daysString[row][column]=day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
            int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);
            if(dayString.equals(mSelDay+"")){
                //绘制背景色矩形
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                mPaint.setColor(mSelectBGColor);
                int y=(endRecY-startRecY)/2+startRecY;
                int x=(endRecX-startRecX)/2+startRecX;

                    canvas.drawCircle(x, y, (endRecY - startRecY-20) / 2, mPaint);

//                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
                //记录第几行，即第几周
                weekRow = row + 1;
            }
            //绘制事务圆形标志
            drawCircle(row,column,day + 1,canvas);
            if(isShow){
                drawGrayCircle(row,column,day + 1,canvas);
            }
            if(dayString.equals(mSelDay+"")){
                if(!isShow) {

                    mPaint.setColor(mSelectDayColor);
                }else {
                    dateCurr=CommonUtils.stringToDate(mSelYear+"-"+(mSelMonth+1)+"-"+mSelDay,"yyyy-MM-dd").getTime();
                    if (dateCurr<new Date().getTime()){

                            mPaint.setColor(mSelectDayColor);

                    }else{

                            mPaint.setColor(mSelectDayColor);

                        int row1 =isshowy / mRowSize;
                        int column1 = isshowx/ mColumnSize;

                        setSelectYearMonth(isShowYear,isShowMonth,isShowDay);

                        invalidate();
                        //执行activity发送过来的点击处理事件
                        if (dateClick != null) {
                            dateClick.onClickOnDate();
                        }



                    }
                }
            }else if(dayString.equals(mCurrDay+"") && mCurrDay != mSelDay && mCurrMonth == mSelMonth){
                //正常月，选中其他日期，则今日为红色
                mPaint.setColor(mCurrentColor);
            }else if(Integer.valueOf(dayString)>mCurrDay&& mCurrMonth == mSelMonth&& mCurrYear == mSelYear){
                mPaint.setColor(mSelectDayColor);
            }
            else{
                mPaint.setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);
            if(tv_date != null){
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
                tv_date.setTextColor(Color.parseColor("#2ec2cc"));
            }

            if(tv_week != null){
                tv_week.setText("第" + weekRow  +"周");
                tv_week.setTextColor(Color.parseColor("#2ec2cc"));
            }
        }
    }

    private void drawCircle(int row,int column,int day,Canvas canvas){
        if(daysHasThingList != null && daysHasThingList.size() >0){
            if(!daysHasThingList.contains(day))return;
            mPaint.setColor( Color.parseColor("#d0d0d0"));

            int startRecX = mColumnSize * column;
            int startRecY = mRowSize * row;
            int endRecX = startRecX + mColumnSize;
            int endRecY = startRecY + mRowSize;
            int y=(endRecY-startRecY)/2+startRecY;
            int x=(endRecX-startRecX)/2+startRecX;

            float circleX = (float) (mColumnSize * column +	mColumnSize*0.5);
            float circley = (float) (mRowSize * row + mRowSize*0.5);
            canvas.drawCircle(circleX, circley, (endRecY-startRecY-20)/2, mPaint);
        }
    }
    private void drawGrayCircle(int row,int column,int day,Canvas canvas){
        if((mCurrYear==mSelYear)&&(mCurrMonth==mSelMonth)) {


            if(day>mCurrDay) {
                mPaint.setColor(Color.parseColor("#d0d0d0"));
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                int y = (endRecY - startRecY) / 2 + startRecY;
                int x = (endRecX - startRecX) / 2 + startRecX;

                float circleX = (float) (mColumnSize * column + mColumnSize * 0.5);
                float circley = (float) (mRowSize * row + mRowSize * 0.5);
                canvas.drawCircle(circleX, circley, (endRecY - startRecY-20) / 2, mPaint);
            }
        }

    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0,downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize(){
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;


    }

    public void setDate(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 执行点击事件
     * @param x
     * @param y
     */
    private void doClickAction(int x,int y){

            int row = y / mRowSize;
            int column = x / mColumnSize;

        if(isShow){
            isShowYear=mSelYear;
            isShowMonth=mSelMonth;
            isShowDay=mSelDay;
            isshowx=x;
            isshowy=y;
        }

            setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);

            invalidate();
            //执行activity发送过来的点击处理事件
            if (dateClick != null) {
                dateClick.onClickOnDate();
            }


    }


    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }
    /**
     * 左点击，日历向后翻页
     */
    public void onBackLeftClick(ImageView iv){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);

        if(year<mCurrYear) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_r));
        }else if(year==mCurrYear){
            if(month<mCurrMonth) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_r));
            }else{
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
            }
        }
        invalidate();
    }
    public void setBackGray(){

        if(mSelYear==mCurrYear){
            if(mSelMonth==mCurrMonth){
                if(mSelDay>mCurrDay){

                }
            }

        }
    }
    /**
     * 右点击，日历向前翻页,但不会向后翻
     */
    public void onBackRightClick(ImageView iv){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(year<mCurrYear) {

            if (month == 11) {//若果是12月份，则变成1月份
                year = mSelYear + 1;
                month = 0;
            } else if (DateUtils.getMonthDays(year, month) == day) {
                //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
                month = month + 1;
                day = DateUtils.getMonthDays(year, month);
            } else {
                month = month + 1;
            }
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_r));
            setSelectYearMonth(year, month, day);
            invalidate();
        }else if(year==mCurrYear){
            if(month<mCurrMonth) {
                if (month == 11) {//若果是12月份，则变成1月份
                    year = mSelYear + 1;
                    month = 0;
                } else if (DateUtils.getMonthDays(year, month) == day) {
                    //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
                    month = month + 1;
                    day = DateUtils.getMonthDays(year, month);
                    if(month==mCurrMonth){
                        iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
                    }else{
                        iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_r));
                    }
                } else {
                    month = month + 1;
                    if(month==mCurrMonth){

                        iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
                        day=mCurrDay;
                    }else{
                        iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_r));
                    }
                }
                setSelectYearMonth(year, month, day);

                invalidate();
            }
            else{
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
            }

        }else{
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_calender_arr_hui));
        }
    }

    /**
     * 获取选择的年份
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }
    /**
     * 获取选择的月份
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }
    /**
     * 获取选择的日期
     * @param mSelDay
     */
    public int getmSelDay() {
        return this.mSelDay;
    }
    /**
     * 普通日期的字体颜色，默认黑色
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }
    /**
     * 当前日期不是选中的颜色，默认红色
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }
    /**
     * 设置显示当前日期的控件
     * @param tv_date
     * 		显示日期
     * @param tv_week
     * 		显示周
     */
    public void setTextView(TextView tv_date,TextView tv_week){
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * 设置事务天数
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     * @param mCircleColor
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 设置日期的点击回调事件
     * @author shiwei.deng
     *
     */
    public interface DateClick{
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView(){
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
        invalidate();
    }
}


