package cn.com.mapuni.meshing.activity;

import cn.com.mapuni.meshing.activity.gis.MapTdtFragment;
import cn.com.mapuni.meshing.activity.wd_activity.WdFragment;
import com.example.meshing.R;
import com.mapuni.android.base.util.DisplayUitl;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NewMainActivity extends Activity implements
		OnCheckedChangeListener {
	private RadioGroup rg_main;// ѡ�ť
	private RadioButton rb_xc;// Ѳ�鰴ťѡ��
	private RadioButton rb_db;// Ѳ�鰴ťѡ��
	/**
	 * ���ڶ�Fragment���й���
	 */
	private FragmentManager fragmentManager;
	/**
	 * ����չʾ�û���Ϣ��Fragment
	 */
	private WdFragment wdFragment;
	/**
	 * ����չʾ��ͼ��Ϣ��Fragment
	 */
	private MapTdtFragment mapTdtFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		fragmentManager = getFragmentManager();
		initView();
		initData();
	}

	/*
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		rg_main = (RadioGroup) findViewById(R.id.rg_main);
		rg_main.setOnCheckedChangeListener(this);
		rb_xc = (RadioButton) findViewById(R.id.rb_xc);
		rb_db = (RadioButton) findViewById(R.id.rb_db);
	}

	/*
	 * ��ʼ������
	 */
	private void initData() {
		// ��һ������ʱѡ�е�0��tab
		//������Ѳ�칦�ܵĽ�ɫ����Ѳ��ҳ��
		String organization_code = DisplayUitl.readPreferences(this, "lastuser", "organization_code");
		String havePatrolRole = DisplayUitl.readPreferences(this, "lastuser", "havePatrolRole");
		String haveAdminRole = DisplayUitl.readPreferences(this, "lastuser", "haveAdminRole");
		String haveLiaisonRole = DisplayUitl.readPreferences(this, "lastuser", "haveLiaisonRole");
		if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) && !"1".equals(haveLiaisonRole))
				|| (organization_code.length() == 10)) {
			rb_xc.setChecked(true);
		} else {
			rb_xc.setVisibility(View.GONE);
			rb_db.setChecked(true);
		}
	}

	int checkedId=-1;
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(-1!=checkedId){
			rg_main.check(checkedId);
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		this.checkedId=checkedId;
		// TODO Auto-generated method stub
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		if (wdFragment != null) {
			transaction.hide(wdFragment);
		}
		if (mapTdtFragment != null) {
			transaction.hide(mapTdtFragment);
		}

		if (checkedId == R.id.rb_xc) {
			if (mapTdtFragment == null) {
				// ���MapTdtFragmentΪ�գ��򴴽�һ������ӵ�������
				mapTdtFragment = new MapTdtFragment(0);
				transaction.add(R.id.content, mapTdtFragment);
			} else {
				// ���MapTdtFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(mapTdtFragment);
				mapTdtFragment.changeView(0);
			}
		} else if (checkedId == R.id.rb_db) {
			if (mapTdtFragment == null) {
				// ���MapTdtFragmentΪ�գ��򴴽�һ������ӵ�������
				mapTdtFragment = new MapTdtFragment(1);
				transaction.add(R.id.content, mapTdtFragment);
			} else {
				// ���MapTdtFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(mapTdtFragment);
				mapTdtFragment.changeView(1);
			}
		} else if (checkedId == R.id.rb_wd) {
			if (wdFragment == null) {
				// ���WdFragmentΪ�գ��򴴽�һ������ӵ�������
				wdFragment = new WdFragment();
				transaction.add(R.id.content, wdFragment);
			} else {
				// ���WdFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(wdFragment);
			}
		}
		
		transaction.commit();
	}
	
	/**
	 * �������������Ƴ��Ի���
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(R.drawable.base_icon_mapuni_white);
			dialog.setTitle("ϵͳ�˳�");
			dialog.setMessage("ȷ��Ҫ�˳�ϵͳ��");
			dialog.setPositiveButton("��", new btnExitListener());
			dialog.setNegativeButton("��", null);
			dialog.show();
		}
		return super.dispatchKeyEvent(event);
	}
	
	/**
	 * FileName: GridActivity.java Description: �˳�ϵͳ
	 * 
	 * @author xuhuaiguang
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-6 ����09:43:32
	 */
	private class btnExitListener implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			finish();
		}
	}
}
