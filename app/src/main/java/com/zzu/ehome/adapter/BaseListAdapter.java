package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dell on 2016/3/14.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    /**
     * 数据列表
     */
    private List<T> mObjects;
    /**
     * 上下文对象
     */
    protected Context mContext;
    /**
     * 是否更新
     */
    private boolean mNotifyOnChange = true;
    /**
     * 解析布局管理器
     */
    private LayoutInflater mInflater;

    /**
     * 初始化构造函数
     *
     * @param context
     */
    public BaseListAdapter(Context context) {
        init(context, new ArrayList<T>());
    }

    /**
     * 初始化构造函数
     *
     * @param context
     * @param objects
     */
    public BaseListAdapter(Context context, List<T> objects) {
        init(context, objects);
    }
    /**
     * 初始化构造函数
     *
     * @param context
     * @param objects
     */
    public BaseListAdapter(Context context, T[] objects) {
        init(context, Arrays.asList(objects));
    }
    /**
     * 初始化数据
     *
     * @param context
     * @param objects
     */
    private void init(Context context, List<T> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    /**
     * 返回布局解析器
     *
     * @return
     */
    public LayoutInflater getInflater() {
        return mInflater;
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 添加单个数据
     *
     * @param object
     */
    public void add(T object) {
        mObjects.add(object);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }
    /**
     * 添加单个数据位置
     * @param location
     * @param object
     */
    public void add(int location,T object){
        mObjects.add(location, object);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据集合
     *
     * @param objects
     */
    public void addAll(List<T> objects) {
        if(objects == null) {
            System.out.println("不能加空list");
            return;
        }
        mObjects.addAll(objects);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }
    /**
     * 添加数据集合
     * @param objects
     * @param location
     */
    public void addAll(List<T> objects,int location){
        if(objects == null) {
            System.out.println("不能加空list");
            return;
        }
        mObjects.addAll(location, objects);
        if(mNotifyOnChange){
            notifyDataSetChanged();
        }
    }

    /**
     * 取得数据集合
     *
     * @return
     */
    public List<T> getmObjects() {
        return mObjects;
    }

    /**
     * 指定位置添加单个数据
     *
     * @param object
     * @param index
     */
    public void insert(T object, int index) {
        mObjects.add(index, object);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除某个数据
     *
     * @param object
     */
    public void remove(T object) {
        mObjects.remove(object);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }
    /**
     * 删除某个数据重载
     * @param position
     */
    public void remove(int position){
        mObjects.remove(position);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 清空列表
     */
    public void clear() {
        mObjects.clear();
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 覆写刷新函数
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * 设置是否可以刷新
     *
     * @param notifyOnChange
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    /**
     * 返回数据数量
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * 返回某个数据
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * 返回数据所在位置
     *
     * @param item
     * @return
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * 返回数据ID
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * 返回每个item的View
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return getGqView(position, convertView, parent);
    }

    /**
     * 抽象方法返回每个Item的View
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getGqView(int position, View convertView,
                                   ViewGroup parent);

}
