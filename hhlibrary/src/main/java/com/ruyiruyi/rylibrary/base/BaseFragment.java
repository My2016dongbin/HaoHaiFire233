package com.ruyiruyi.rylibrary.base;

import android.app.ProgressDialog;

import androidx.fragment.app.Fragment;

/**
 * Created by geyang on 2019/12/4.
 */

public class BaseFragment extends Fragment {

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }

    public void hideDialogProgress(ProgressDialog dialog) {
        dialog.dismiss();
    }
}