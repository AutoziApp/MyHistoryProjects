package com.mapuni.administrator.itemFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.dbAc.ImagePlayerActivity;
import com.mapuni.administrator.activity.dbAc.PlayerActivity;
import com.mapuni.administrator.bean.AttachmentBean;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.manager.PathManager;
import com.mapuni.administrator.utils.FileDownUtil;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TypeUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import okhttp3.Call;

/**
 * Created by 15225 on 2017/8/17.
 */

public class AttachmentRecyclerItemFactory extends AssemblyRecyclerItemFactory<AttachmentRecyclerItemFactory.AttachmentRecyclerItem> {
    private EventListener mEventListener;

    private Context baseContext;

    public AttachmentRecyclerItemFactory(Context context) {
        mEventListener=new EventProcess(context);
        baseContext=context;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof AttachmentBean.RowsBean;
    }

    @Override
    public AttachmentRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new AttachmentRecyclerItem(R.layout.list_item_no_sign_task,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, AttachmentBean.RowsBean rows);
    }


    private static class EventProcess implements EventListener {
        private Context context;
        public EventProcess(Context context) {
            this.context=context;
        }
        @Override
        public void onClickDetail(int position, AttachmentBean.RowsBean data) {
            getAttachUrl(data);
        }


        public void getAttachUrl(final AttachmentBean.RowsBean bean) {
            String handleAttachmentUuid = bean.getUuid();
            String sessionId = (String) SPUtils.getSp(context, "sessionId", "");
            NetManager.getAttachUrl(sessionId, handleAttachmentUuid, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(context, "服务端暂未找到该附件", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("status").equals("0")) {
                            String url = PathManager.PathUrl +jsonObject.optString("url").replaceAll("\\\\","/");
                            String extension = bean.getExtension();
                            if (TypeUtil.isImageType(extension)) {//如果是图片格式
                                Intent intent = new Intent(context, ImagePlayerActivity.class);
                                intent.putExtra("imageUrl", url);
                                context.startActivity(intent);
                                ((Activity) context).overridePendingTransition(0, 0);
                            } else if (TypeUtil.isWordType(extension)) {//文档格式 下载 调用wps

                                FileDownUtil.getInstance(context).downLoadWord(url, bean.getName());

                            } else if (TypeUtil.isMP3Type(extension)) {//mp3格式 下载 调用wps
                                Intent i=new Intent(Intent.ACTION_VIEW);
                                Uri uri=Uri.parse(url);
                                i.setDataAndType(uri, "audio/*");
                                context.startActivity(i);
                                
                            }else {//视频格式
                                Intent intent = new Intent(context, PlayerActivity.class);
                                intent.putExtra("videoUrl", url);
                                intent.putExtra("videoName", bean.getName());
                                context.startActivity(intent);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    public class AttachmentRecyclerItem extends AssemblyRecyclerItem<AttachmentBean.RowsBean> {


        private TextView tvTaskName;
        private TextView tvEndDate;
        private TextView tvTaskSource;
        private TextView tvTaskDetail;

        public AttachmentRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {

            tvTaskName= (TextView) findViewById(R.id.tvTaskName);
            tvEndDate= (TextView) findViewById(R.id.tvEndDate);
            tvTaskSource= (TextView) findViewById(R.id.tvTaskSource);
            tvTaskDetail= (TextView) findViewById(R.id.tvTaskDetail);
        }

        @Override
        protected void onConfigViews(final Context context) {
            tvTaskDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDetail(getLayoutPosition(),getData());
                }
            });
        }

        @Override
        protected void onSetData(int i, AttachmentBean.RowsBean rows) {
            
            tvTaskName.setText(rows.getName());
            tvEndDate.setText(rows.getCreateTime());
            tvTaskSource.setText(rows.getSize());
            tvTaskDetail.setText("预览");
        }
    }
}
