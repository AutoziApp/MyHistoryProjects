package cn.com.mapuni.meshing.multimeidia;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import cn.com.mapuni.meshing.base.R;

public class PhotoPopupWindows extends PopupWindow {
	private Context mcontext;
	public PhotoPopupWindows(Context mContext, View parent,final Cameras cameras) {
		this.mcontext = mContext;
		View view = View.inflate(mContext, R.layout.photo_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.push_bottom_in_2));

		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// photo();
				// doTakePhoto();
				cameras.startActionCamera(cameras);
				dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mcontext,
						DiscoverAlbumSelectActivity.class);
				if (BimpHelper.bmp.size() > 0) {
					AlbumHelper.isRefarshAlbum = false;
				} else {
					AlbumHelper.isRefarshAlbum = true;
				}
				mcontext.startActivity(intent);
				dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

	}

 
	
	
	
}
