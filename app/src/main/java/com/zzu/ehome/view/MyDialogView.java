package com.zzu.ehome.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/5/17.
 */
public abstract class MyDialogView extends LinearLayout {
    protected Context context;
    protected ExitDialog mExitDialog;

    public interface ExitDialog {
        public void exitDialog();
    }

    public void setExitDialog(ExitDialog exitDialog) {
        mExitDialog = exitDialog;
    }

    public MyDialogView(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public boolean ensureInfo(){ return false; };
    public abstract DialogInterface.OnClickListener getPositiveButtonListener();
    public abstract DialogInterface.OnClickListener getNegativeButtonListener();
}
