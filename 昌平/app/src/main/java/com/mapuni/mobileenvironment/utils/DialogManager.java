package com.mapuni.mobileenvironment.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;

/**
 * Created by yawei on 2017/2/20.
 */

public class DialogManager {
    private static AlertDialog _Dialog;
    public static void showDialog(int view, String title, int icon, final Context context,
                                  final DialogListaner listener){
        final AlertDialog.Builder buider = new AlertDialog.Builder(context);
        View _View = LayoutInflater.from(context).inflate(view,null);
        buider.setView(_View);
        final EditText et = (EditText) _View.findViewById(R.id.set_ip);
        buider.setTitle(title);
        buider.setIcon(icon);
        buider.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(et.getText().toString());
            }
        });
        buider.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick("-1");
                _Dialog.dismiss();
            }
        });
        _Dialog = buider.create();
        _Dialog.show();
    }
    public interface DialogListaner{
        public void onClick(String s);
    }
}
