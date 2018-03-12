package com.mapuni.shangluo.itemFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.dbAc.DownTaskDetailActivity;
import com.mapuni.shangluo.activity.dbAc.ImagePlayerActivity;
import com.mapuni.shangluo.activity.dbAc.PlayerActivity;
import com.mapuni.shangluo.activity.wdAc.DownTaskCompleteDetailActivity;
import com.mapuni.shangluo.bean.AttachmentBean;
import com.mapuni.shangluo.manager.MessageEvent;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.manager.PathManager;
import com.mapuni.shangluo.utils.FileDownUtil;
import com.mapuni.shangluo.utils.SPUtils;
import com.mapuni.shangluo.utils.ToastUtil;
import com.mapuni.shangluo.utils.TypeUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import okhttp3.Call;

/**
 * Created by 15225 on 2017/8/17.
 */

public class PicAttachmentRecyclerItemFactory extends AssemblyRecyclerItemFactory<PicAttachmentRecyclerItemFactory.AttachmentRecyclerItem> {
    private EventListener mEventListener;

    private Context baseContext;
    private String flag;
    public PicAttachmentRecyclerItemFactory(Context context, String flag) {
        mEventListener=new EventProcess(context);
        baseContext=context;
        this.flag=flag;
    }



    @Override
    public boolean isTarget(Object o) {
        return o instanceof AttachmentBean.RowsBean;
    }

    @Override
    public AttachmentRecyclerItem createAssemblyItem(ViewGroup viewGroup) {
        return new AttachmentRecyclerItem(R.layout.list_item_pic_task,viewGroup);
    }

    public interface EventListener{
        void onClickDetail(int position, AttachmentBean.RowsBean rows);

        void onClickDelate(int position, AttachmentBean.RowsBean rows);
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

        @Override
        public void onClickDelate(int position, AttachmentBean.RowsBean rows) {
            showDelateDilog(rows.getUuid());
        }

        private void showDelateDilog(final String uuid) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setIcon(R.drawable.logo);
            normalDialog.setTitle("提示");
            normalDialog.setMessage("确认要删除该附件?");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
                            deleteAccount(uuid);
                        }
                    });
            normalDialog.setNegativeButton("关闭",null);
            // 显示
            normalDialog.show();
        }
        private void deleteAccount(String uuid) {
            String sessionId = (String) SPUtils.getSp(context, "sessionId", "");
            NetManager.deleteAttachment(sessionId,uuid, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
//                    clearLoginMsg();
                    Log.i("aaa",e.getMessage());
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
//                    clearLoginMsg();
                    Log.i("aaa",response);
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MessageEvent("deleteSuccess"));
                }
            });
        }
        public void getAttachUrl(final AttachmentBean.RowsBean bean) {
            String taskAttachmentUuid = bean.getUuid();
            String sessionId = (String) SPUtils.getSp(context, "sessionId", "");
            NetManager.getPicAttachUrl(sessionId, taskAttachmentUuid, new StringCallback() {
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
                            } else if (TypeUtil.isVideoType(extension)) {//常见视频格式  
                                Intent intent = new Intent(context, PlayerActivity.class);
                                intent.putExtra("videoUrl", url);
                                intent.putExtra("videoName", bean.getName());
                                context.startActivity(intent);
                            } else {
                                ToastUtil.showShort(context, "暂不支持该类型文件");
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
        private TextView tvDelete;

        public AttachmentRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            tvTaskName= (TextView) findViewById(R.id.tvTaskName);
            tvEndDate= (TextView) findViewById(R.id.tvEndDate);
            tvTaskSource= (TextView) findViewById(R.id.tvTaskSource);
            tvTaskDetail= (TextView) findViewById(R.id.tvTaskDetail);
            tvDelete= (TextView) findViewById(R.id.tv_delete);
            //控制删除按钮的显示与隐藏
            if (flag!=null){
                if ( DownTaskDetailActivity.class.getSimpleName().equals(flag)
                        ||DownTaskCompleteDetailActivity.class.getSimpleName().equals(flag)){
                    if (tvDelete.isShown()){
                        tvDelete.setVisibility(View.GONE);
                    }
                }else{
                    if (!tvDelete.isShown()){
                        tvDelete.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        protected void onConfigViews(final Context context) {
            tvTaskDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDetail(getLayoutPosition(),getData());
                }
            });
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onClickDelate(getLayoutPosition(),getData());

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
