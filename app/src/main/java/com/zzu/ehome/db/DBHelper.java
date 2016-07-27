package com.zzu.ehome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zzu.ehome.activity.LoginActivity;
import com.zzu.ehome.utils.SharePreferenceUtil;

/**
 * Created by zzu on 2016/4/6.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 10;
    private static final String NAME = "EHOME.db";
    private static final String SQL_LOGIN_HISTORY_CREAT = "create table login_historytb(_id integer primary key autoincrement,userid text ,username text,nick text,mobile text,imgHead text,password text,sex text,age text,userno text,patientId text,height text)";
    private static final String SQL_LOGIN_HISTORY_DROP = "drop table if exists login_historytb";
    private static final String SQL_DISEASE_CREAT="create table disease_tb(_id integer primary key autoincrement,name text ,type integer,open integer)";
    private static final String SQL_DISEASE_DROP="drop table if exists disease_tb";
    private static final String SQL_STEP_CREAT="create table step_tb(_id integer primary key autoincrement,num integer ,startTime text,endTime text,userid text,uploadState integer)";
    private static final String SQL_STEP_DROP="drop table if exists step_tb";
    public static DBHelper helper = null;
    public static  Context mContext;

    public static DBHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (DBHelper.class) {
                if (helper == null) {
                    helper = new DBHelper(context.getApplicationContext());
                }
            }
        }
        mContext=context;
        return helper;
    }

    private DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_LOGIN_HISTORY_CREAT);
        db.execSQL(SQL_DISEASE_CREAT);
        db.execSQL(SQL_STEP_CREAT);
    }

    /**当数据库更新时，调用该方法*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_LOGIN_HISTORY_DROP);
        db.execSQL(SQL_DISEASE_DROP);
        db.execSQL(SQL_STEP_DROP);
        db.execSQL(SQL_LOGIN_HISTORY_CREAT);
        db.execSQL(SQL_DISEASE_CREAT);
        db.execSQL(SQL_STEP_CREAT);
        SharePreferenceUtil.getInstance(mContext).setUserId("");
        SharePreferenceUtil.getInstance(mContext).setIsRemeber(false);
    }
}
