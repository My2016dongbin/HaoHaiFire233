package com.ruyiruyi.rylibrary.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ruyiruyi.rylibrary.R;

public class ErrorDialog extends Dialog {

    private Context context;
    public ErrorDialog(Context context) {
        super(context);
        this.context =context;
    }

    public ErrorDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ErrorDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_error,null);
        setContentView(view);
    }
}