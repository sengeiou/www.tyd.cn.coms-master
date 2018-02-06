package com.touedian.tyd.com.loadingdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.touedian.tyd.com.R;

/**
 *
 * Author:hwr
 * Date:2018/newhomepageone/22
 * 佛祖保佑       永无BUG     永不修改
 * 自定义Dialog
 */


public class ErrorDialog extends Dialog {
    public ErrorDialog(Context context) {
        super(context);
    }

    public ErrorDialog(Context context, int themeResId) {//themeResId,就是我们可以传入一个主题值，比如R.style.XXX
        super(context, themeResId);
    }

    protected ErrorDialog(Context context, boolean cancelable
        , OnCancelListener cancelListener) {//cancelable  控制点击屏幕外不会消失
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.errdialog_layout,null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = dip2px(getContext(),250);
        lp.width = dip2px(getContext(),200);
        win.setAttributes(lp);

        view.findViewById(R.id.btn_cancel).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
    //Dip转换px
    public static int dip2px(Context pContext, float pFloat) {
        int value = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pFloat, pContext.getResources().getDisplayMetrics()) + 0.5f);
        return value;
    }


}

