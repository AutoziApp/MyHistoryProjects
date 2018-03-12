package com.mapuni.car.mvp.detailcar.ItemFactory;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.model.PhotoBean;
import com.mapuni.car.mvp.main.activity.MainActivity;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;
import com.mapuni.core.utils.DisplayUtil;
import com.mapuni.core.widget.takephoto.photo.util.PhotoLook;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by yawei on 2017/8/25.
 */

public class PhotoItemFactory extends AssemblyRecyclerItemFactory<PhotoItemFactory.CarItem> {
    Activity act;
    GridView gridView;
    View view;
    PopupWindow pop;
    LinearLayout ll_popup;
    public View selectView;
    SimpleAdapter saImageItems;
    HashMap selectMap;
    Bitmap carIcon;
    ArrayList<HashMap<String, Object>> lstImageItem;
    private File photo_file = new File(ConsTants.PhotoPath);
    private String[] name = {"VIN", "车身外观（前）", "车身外观（后）",
            "环保装置1", "环保装置2", "环保装置3"};
    public static Uri photoUri;
    public static String photoKey;
    public static String[] PhotoKeys = new String[]{"vinImgs", "beforeCarAspectImgs",
            "afterCarAspectImgs", "enprotectImgsOne", "enprotectImgsTwo"
            , "enprotectImgsThree"};
    public static final int TAKE_PICTURE = 0x000001;
    public static final int PHOTO_REQUEST_GALLERY = 0x000002;

    @Override
    public boolean isTarget(Object data) {
        return data instanceof PhotoBean;
    }

    public PhotoItemFactory(Activity act) {
        this.act = act;
    }

    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        view = parent;
        return new CarItem(R.layout.item_photo_layout, parent);
    }

    public class CarItem extends AssemblyRecyclerItem<PhotoBean> {
        Context context;

        public CarItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
            createView();
            initPoup();
        }

        @Override
        protected void onFindViews() {

        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;

        }

        @Override
        protected void onSetData(int position, PhotoBean bean) {
            if (selectMap != null)//避免重置数据源
                return;
            lstImageItem.clear();
            for (int i = 0; i < name.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                String url = (String) getData().getPhotoAdress().get(PhotoKeys[i]);
                if (url != null && !url.equals("")) {
                    map.put("img", url);
                } else {
                    map.put("img", carIcon);
                }
                map.put("txt", name[i]);
                lstImageItem.add(map);
            }
            saImageItems.notifyDataSetChanged();
        }

        private void createView() {
            gridView = (GridView) findViewById(R.id.photoGrid);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            int length = name.length;
            lstImageItem = new ArrayList<HashMap<String, Object>>();
            carIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img);
            for (int i = 0; i < length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("img", carIcon);
                map.put("txt", name[i]);
                lstImageItem.add(map);
            }
            saImageItems = new SimpleAdapter(context,
                    lstImageItem,
                    R.layout.photo_item,
                    new String[]{"img", "txt"},
                    new int[]{R.id.img, R.id.txt});
            saImageItems.setViewBinder(
                    (View view, Object data, String textRepresentation) -> {
                        // TODO Auto-generated method stub
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView i = (ImageView) view;
                            i.setImageBitmap((Bitmap) data);
                            return true;
                        } else if (view instanceof ImageView && data instanceof String) {
                            ImageView i = (ImageView) view;
//                        i.setImageBitmap((Bitmap) data);
                            if (((String) data).contains("http")) {
                                Picasso.with(context).load((String) data).placeholder(R.mipmap.img_lose)
                                        .resize(DisplayUtil.dip2px(context, 100), DisplayUtil.dip2px(context, 100))
                                        .centerCrop().into(i);
                            } else {
                                File file = new File((String) data);
                                Picasso.with(context).load(file).placeholder(R.mipmap.img_lose)
                                        .resize(DisplayUtil.dip2px(context, 100), DisplayUtil.dip2px(context, 100))
                                        .centerCrop().into(i);

                            }
                            return true;
                        }
                        return false;
                    });
            gridView.setAdapter(saImageItems);
            gridView.setOnItemClickListener((AdapterView<?> av, View view, int position, long id) -> {
                Object obj = (Object) lstImageItem.get(position).get("img");
                photoKey = PhotoKeys[position];
                if (obj == null || obj.equals("null") || (obj instanceof Bitmap && obj == carIcon)) {
                    showPoup(position);
                } else {
                    //查看大图  绑定替换监听
                    PhotoLook.getInstance(context).setDelListener((int podition) -> {
//                        HashMap map = lstImageItem.get(podition);
//                        map.put("img",carIcon);
//                        saImageItems.notifyDataSetChanged();
//                        DetailPresenter.delImg(PhotoKeys[position]);


                        showPoup(podition);
                    }, position).showAtView(view, obj);
                }
            });
        }

        //照片补录
        private void showPoup(int position) {
            selectMap = lstImageItem.get(position);
            TextView tv = (TextView) view.findViewById(R.id.txt);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(context, com.mapuni.core.R.anim.activity_translate_in));
            pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }

        public void initPoup() {

            pop = new PopupWindow(context);

            View view = ((Activity) context).getLayoutInflater().inflate(com.mapuni.core.R.layout.item_popupwindows, null);

            ll_popup = (LinearLayout) view.findViewById(com.mapuni.core.R.id.ll_popup);

            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setFocusable(true);
            pop.setOutsideTouchable(true);
            pop.setContentView(view);

            RelativeLayout parent = (RelativeLayout) view.findViewById(com.mapuni.core.R.id.parent);
            Button bt1 = (Button) view
                    .findViewById(com.mapuni.core.R.id.item_popupwindows_camera);
//            Button bt2 = (Button) view
//                    .findViewById(com.mapuni.core.R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(com.mapuni.core.R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(l);
//            bt2.setOnClickListener(l);
            bt3.setOnClickListener(l);
        }

        private View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_popupwindows_camera:
                        camera();
                        pop.dismiss();
                        ll_popup.clearAnimation();
                        break;
//                    case R.id.item_popupwindows_Photo:
//                        gallery();
//                        pop.dismiss();
//                        ll_popup.clearAnimation();
//                        break;
                    case R.id.item_popupwindows_cancel:
                        pop.dismiss();
                        ll_popup.clearAnimation();
                        break;
                }
            }
        };

        public void camera() {
            if (!photo_file.exists()) {
                photo_file.mkdir();
            }
            photo_file = new File(ConsTants.PhotoPath, "/" + System.currentTimeMillis() + ".jpg");
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, photo_file.getAbsolutePath());
            photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            ((Activity) context).startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }

        /*
 36      * 从相册获取
 37      */
        public void gallery() {
            // 激活系统图库，选择一张图片
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
            ((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }

    }

    public void setSelectPhoto(Bitmap bmp) {
        selectMap.put("img", bmp);
        saImageItems.notifyDataSetChanged();
    }

    public void setSelectPhoto(String path) {
        selectMap.put("img", path);
        saImageItems.notifyDataSetChanged();
    }
}
