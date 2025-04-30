package com.skyline.test.myview;

import android.content.Context;
import android.graphics.Color;

import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.terraexplorer.views.ModalDialog;

import yuku.ambilwarna.AmbilWarnaView;

/**
 * Created by Yellow on 2017-05-22.
 */

public class MyWhiteBoardCommon implements ModalDialog.ModalDialogDelegate {
    private Converter convertor;
    @Override
    public void modalDialogDidDismissWithOk(ModalDialog dlg) {

    }
    private  interface  Converter
    {
        String convertValueToString(ModalDialog dlg);
    }
    public  void  editorColor(String objectId)
    {
        final AmbilWarnaView colorPicker=new AmbilWarnaView(TEApp.getAppContext(), Color.argb(256,255,0,0));
        convertor=new Converter() {
            @Override
            public String convertValueToString(ModalDialog dlg) {
                int color = colorPicker.getColor();
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = (color >> 0) & 0xFF;
                int selectedColorValue = blue * (256*256) + green * (256) +  red;
                String selectedColor = String.valueOf(selectedColorValue);
                setLastColor(selectedColor);
                return selectedColor;
            }
        };
    }
    private void showTextEditor(String value, int title, int inputType) {
        convertor = new Converter() {
            @Override
            public String convertValueToString(ModalDialog dlg) {
                return dlg.getTextField().getText().toString();
            }
        };
        ModalDialog dlg = new ModalDialog(title, this);
        dlg.setContentTextField(inputType, value);
        dlg.show();
    }
    private void  setLastColor(String color)
    {
        TEApp.getAppContext().getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("com.skyline.terraexplorer.whiteboard.last_used_color", color)
                .apply();
    }

    @Override
    public void modalDialogDidDismissWithCancel(ModalDialog dlg) {

    }
}
