package cn.com.mapuni.meshing.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.com.mapuni.meshing.base.Global;
import cn.com.mapuni.meshing.base.R;
/**
 * ��ҳ����listview ����setOnPageCountChangListenerʵ�ַ�ҳ���� ��дonAddPage�������ͳɹ���Ϣ���ؽ�������������ȫ�����ݺ�����isCompletedΪtrue
 * @author wanglg
 *
 */
public class PagingListView extends ListView  {
	

	public int batchCount=Global.getGlobalInstance().getListNumber();
	private PageCountChangListener pageCountChangListener;
	LayoutInflater inflater;
	LinearLayout footerview;
	/**�Ƿ�������*/
	Boolean isCompleted=false;
	
	

	public PagingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater=LayoutInflater.from(context);
		footerview=(LinearLayout) inflater.inflate(R.layout.listview_footview, null);
		addFooterView(footerview);
		setOnScrollListener(new MyScrollListener());
		setFooterDividersEnabled(false);
		
		
	}
	public PagingListView(Context context) {
		super(context);
		inflater=LayoutInflater.from(context);
		footerview=(LinearLayout) inflater.inflate(R.layout.listview_footview, null);
		addFooterView(footerview);
		setOnScrollListener(new MyScrollListener());
		setFooterDividersEnabled(false);
	}
	

	
	private class MyScrollListener implements OnScrollListener{

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&
					(view.getCount()-1)==view.getLastVisiblePosition()
					&&view.getCount()>batchCount&&!isCompleted){
				if(pageCountChangListener!=null){
					footerview.setVisibility(View.VISIBLE);
					pageCountChangListener.onAddPage(view);
				}
			}
			
		}
		
	}
	public void setOnPageCountChangListener(PageCountChangListener  pageCountChangListener){
		this.pageCountChangListener=pageCountChangListener;
	}
	
	public interface PageCountChangListener{
		public void onAddPage(AbsListView view);
		
	}
	
	public Boolean getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	/**
	 * ����FooterView�Ƿ�ɼ�
	 * @param isVisibility
	 */
	public void setFootViewVisibility(Boolean isVisibility){
		if(isVisibility){
			footerview.setVisibility(View.VISIBLE);
		}else{
			footerview.setVisibility(View.GONE);
		}
		
	}

}
