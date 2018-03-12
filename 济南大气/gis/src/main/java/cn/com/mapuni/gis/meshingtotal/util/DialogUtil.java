package cn.com.mapuni.gis.meshingtotal.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class DialogUtil {

	public Dialog getDialog(Context context, boolean isCancelable) {
		Dialog dialog = new Dialog(context);
		dialog.setCancelable(isCancelable);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		return dialog;
	}

}
