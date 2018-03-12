package com.mapuni.core.widget.takephoto.photo.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.core.R;
import com.mapuni.core.utils.DisplayUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.view.Gravity.CENTER;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by lybin on 2017/8/25.
 */

public class PhotoLook {
    private View view;
    private Context context;
    private PopupWindow mPopWindow;
    public static PhotoLook mLookView;
    private ImageView img;
    private TextView delBtn;
    private DelPhotoListener delListener;
    private int position;

    public PhotoLook(Context context){
        this.context = context;
        creatView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showAtView(View parent, Object obj){
//        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int size = context.getResources().getDimensionPixelSize(R.dimen.avatarSize);
//                outline.setOval(0, 1, size, size);
//            }
//        };
        if(obj instanceof String){
            if(((String) obj).contains("http")){
                Picasso.with(context).load((String) obj).error(R.mipmap.img_lose).into(img);
            }else{
                File file = new File((String) obj);
                Picasso.with(context).load(file).error(R.mipmap.img_lose).into(img);
            }
        }else if(obj instanceof Bitmap){
            img.setImageBitmap((Bitmap) obj);
        }
        if(mPopWindow==null){
            mPopWindow = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, false);
            mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopWindow.setOutsideTouchable(false);
//            view.setTranslationZ(60f);
            mPopWindow.setFocusable(false);
        }
     //   mPopWindow.showAsDropDown(parent);
        mPopWindow.showAtLocation(parent, Gravity.CENTER,0,0);
    }

    public PhotoLook setDelListener(DelPhotoListener listener,int position){
        this.position = position;
        delListener = listener;
        return mLookView;
    }

    public boolean dismiss(){
        if(mPopWindow!=null&&mPopWindow.isShowing()){
            mPopWindow.dismiss();
            return  true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void creatView(Context context){
//        view=View.inflate(context,R.layout.item_photo,nu)
       view  = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        ((LinearLayout)view).setOrientation(LinearLayout.VERTICAL);
        view.setBackgroundColor(context.getResources().getColor(R.color.black));

        RelativeLayout topLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(DisplayUtil.getScreenWidth(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        topLayout.setLayoutParams(topParams);
        topLayout.setPadding(0,30,0,0);
        TextView returnBtn = new TextView(context);
        returnBtn.setText("返回");
        returnBtn.setTextSize(18);
        returnBtn.setGravity(CENTER);
        returnBtn.setTextColor(context.getResources().getColor(R.color.white));
        returnBtn.setOnClickListener(l->{dismiss();});
        RelativeLayout.LayoutParams returnParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(context,80),
                DisplayUtil.dip2px(context,60));
        returnParams.addRule(ALIGN_PARENT_LEFT);
        topLayout.addView(returnBtn,returnParams);


        delBtn = new TextView(context);
        delBtn.setText("更换");
        delBtn.setTextSize(18);
        delBtn.setGravity(CENTER);
        delBtn.setTextColor(context.getResources().getColor(R.color.white));
        delBtn.setOnClickListener(l->{
            if(delListener!=null){delListener.onDelete(position);
            dismiss();
            }
        });
        RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(context,80),
                DisplayUtil.dip2px(context,60));
        delParams.addRule(ALIGN_PARENT_RIGHT);
        topLayout.addView(delBtn,delParams);
        img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        ((LinearLayout) view).addView(topLayout,topParams);
        ((LinearLayout) view).addView(img,imgParams);

    }

    public static PhotoLook getInstance(Context context){
        if(mLookView ==null){
            mLookView = new PhotoLook(context);
        }
        return mLookView;
    }

    public interface DelPhotoListener{
        void  onDelete(int position);
    }

}
