package com.skyline.test.myview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.UI;


/**
 * Created by Yellow on 2017-05-24.
 */

public class MyModalDialog {
    public interface MyModalDialogDelegate
    {
        void mymodalDialogDidDismissWithOk(MyModalDialog dlg);
        void mymodalDialogDidDismissWithCancel(MyModalDialog dlg);
    }

    private AlertDialog.Builder builder;
    private MyModalDialogDelegate delegate;
    private Object tag;
    private AlertDialog dialog;
    private View content;
    private boolean disableRenderOnShow = true;

    private DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(disableRenderOnShow)
                UI.getTEView().enableRender();

            delegate.mymodalDialogDidDismissWithOk(MyModalDialog.this);
            MyModalDialog.this.hide();
        }
    };
    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(disableRenderOnShow)
                UI.getTEView().enableRender();
            delegate.mymodalDialogDidDismissWithCancel(MyModalDialog.this);
            MyModalDialog.this.hide();
        }
    };
    public MyModalDialog(int title, MyModalDialogDelegate delegate)
    {
        this(TEApp.getAppContext().getString(title), delegate);
    }

    public MyModalDialog(String title, MyModalDialogDelegate delegate)
    {
        builder = new AlertDialog.Builder(new ContextThemeWrapper(TEApp.getCurrentActivityContext(), R.style.AlertDialogCustom));
        builder.setTitle(title)
                .setPositiveButton(R.string.mm_plottool_ok, positiveListener)
                .setNegativeButton(R.string.mm_plottool_cancel, negativeListener);
        this.delegate = delegate;
    }
    public void setContent(View view)
    {
        content = view;
        builder.setView(view);
    }

    public void setContentTextField(int inputType,final String value)
    {
        final EditText text = new EditText(TEApp.getAppContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        text.setText(value);
        text.setInputType(inputType);
        text.setLayoutParams(lp);
        if(value != null)
        {
            text.post(new Runnable()
            {
                @Override
                public void run() {
                    text.setSelection(value.length());
                }
            });
        }
        setContent(text);
    }
    public void setContentMessage(String message)
    {
        builder.setMessage(message);
    }
    public void setContentMessage(int message)
    {
        builder.setMessage(message);
    }
    public void setOneButtonMode()
    {
        builder.setNegativeButton(null, null);
    }
    public void setTag(Object tag)
    {
        this.tag = tag;
    }

    public Object getTag()
    {
        return tag;
    }

    public void show()
    {
        if(disableRenderOnShow)
            UI.getTEView().disableRender();

        dialog = builder.create();
        dialog.show();
    }

    public void hide()
    {
        if(dialog != null)
        {
            if(disableRenderOnShow)
                UI.getTEView().enableRender();
            dialog.dismiss();
        }
        dialog = null;
    }
    public TextView getTextField() {
        return (TextView)content;
    }

    public View contentViewWithTag(int tag)
    {
        return content.findViewWithTag(tag);
    }

    public void setOkButtonTitle(int text)
    {
        builder.setPositiveButton(text, positiveListener);
    }
    public void setCancelButtonTitle(int text)
    {
        builder.setNegativeButton(text, negativeListener);
    }

    public void setOpacity(int opacity)
    {

    }

    public void setDisableRenderOnShow(boolean disable)
    {
        disableRenderOnShow = disable;
    }
}
