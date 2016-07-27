package com.zzu.ehome.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

import com.zzu.ehome.R;
import com.zzu.ehome.view.MyDialogView;

/**
 * Created by Administrator on 2016/5/17.
 */
public class DialogUtils {
    /**
     * 自定义弹框
     * @param context
     * @return
     */
    public static Dialog showSelfDialog(Context context, MyDialogView dialogView){
        final Dialog dialog =  new Dialog(context, R.style.loadingDialog);

        dialogView.setExitDialog(new MyDialogView.ExitDialog() {

            @Override
            public void exitDialog() {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();
        return dialog;
    }
}
