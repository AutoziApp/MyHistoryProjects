package com.jy.environment.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jy.environment.util.ExitAppUtils;

/**
 * Activity的基类，全站公用菜单、统计等代码编写
 */
public abstract class ActivityBase extends FragmentActivity {
    public static final int REQUEST_FIRST_USER = 1;
    private int mShowingDialogID;
    private Toast mToast;
    /************************** 全局控件 ****************************************/
//    private ImageButton yes, no; // �?��弹出框中的确定�?取消
//    private PopupWindow popupwindow; // �?��弹出�?

    /**
     * 用较短时间显示Toast消息
     * 
     * @param pMsg
     *            消息内容
     */
    protected void showToastShort(String pMsg) {
	if (pMsg != null) {
	    if (mToast == null) {
		mToast = Toast.makeText(this, pMsg, Toast.LENGTH_SHORT);
	    } else {
		mToast.setText(pMsg);
		mToast.setDuration(Toast.LENGTH_SHORT);
	    }
	    mToast.show();
	}
    }

    /**
     * 用较短时间显示Toast消息
     * 
     * @param pStringResId
     *            String资源ID
     */
    protected void showToastShort(int pStringResId) {
	String _MsgString = getString(pStringResId);
	showToastShort(_MsgString);
    }

    /**
     * 用较长时间显示Toast消息
     * 
     * @param pMsg
     *            消息内容
     */
    protected void showToastLong(String pMsg) {
	if (pMsg != null) {
	    if (mToast == null) {
		mToast = Toast.makeText(this, pMsg, Toast.LENGTH_LONG);
	    } else {
		mToast.setText(pMsg);
		mToast.setDuration(Toast.LENGTH_LONG);
	    }
	    mToast.show();
	}
    }

    /**
     * 用较长时间显示Toast消息
     * 
     * @param pStringResId
     *            String资源ID
     */
    protected void showToastLong(int pStringResId) {
	String _MsgString = getString(pStringResId);
	showToastLong(_MsgString);
    }

    /**
     * 运行其他Activity
     * 
     * @param pClass
     *            目标Activity的类对象
     */
    protected void openActivity(Class<?> pClass) {
	openActivity(pClass, null);
    }

    /**
     * 运行其他Activity
     * 
     * @param pClass
     *            目标Activity的类对象
     * @param pBundle
     *            额外信息
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
	Intent _Intent = new Intent(this, pClass);
	if (pBundle != null) {
	    _Intent.putExtras(pBundle);
	}

	startActivity(_Intent);
    }

    /**
     * 运行其他Activity，并期待返回结果�?
     * 
     * @param pClass
     *            目标Activity的类对象
     * @param pBundle
     *            存放数据的Bundle对象，若不需要传递数据，传入null�?
     * @param pRequestCode
     *            请求Code
     */
    protected void openActivityForResult(Class<?> pClass, Bundle pBundle,
	    int pRequestCode) {
	Intent _Intent = new Intent(this, pClass);
	if (pBundle != null) {
	    _Intent.putExtras(pBundle);
	}
	startActivityForResult(_Intent, pRequestCode);
    }

    /**
     * 加载Layout文件，生成View组件
     * 
     * @param pLayoutId
     *            Layout文件的资源Id
     * @param pRoot
     *            生成的View�?��的ViewGroup，传入null表示没有父ViewGroup�?
     * @return 若传入了pRoot，则返回pRoot；反之，则返回生成的View
     */
    protected View inflateView(int pLayoutId, ViewGroup pRoot) {
	return getLayoutInflater().inflate(pLayoutId, pRoot);
    }

    /**
     * 加载MenuLayout文件，生成菜单（选项菜单、上下文菜单等）
     * 
     * @param pMenuLayoutId
     *            MenuLayout文件的资源Id，例如�?R.menu.main_activity�?
     * @param pMenu
     *            用来存放菜单项和子菜单的Menu对象
     */
    protected void inflateMenu(int pMenuLayoutId, Menu pMenu) {
	getMenuInflater().inflate(pMenuLayoutId, pMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	return false;
    }

    @Override
    protected void onPrepareDialog(int pId, Dialog pDialog, Bundle pArgs) {
	super.onPrepareDialog(pId, pDialog, pArgs);
	mShowingDialogID = pId;
    }

    protected int getShowingDialogID() {
	return mShowingDialogID;
    }
    @Override
    protected void onCreate(Bundle arg0) {
    	// TODO Auto-generated method stub
    	super.onCreate(arg0);
    	ExitAppUtils.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ExitAppUtils.getInstance().delActivity(this);
    }
//    /**
//     * 初始化成员变量�? 本方法应该在子类的onCreate方法中，绑定Layout文件后被调用�?
//     * 
//     */
//    protected abstract void initFields();
//
//    /**
//     * 初始化成员View�? 本方法应该在子类的onCreate方法中，绑定Layout文件后被调用�?
//     */
//    protected abstract void initViews();
//
//    /**
//     * 初始化监听器�? 本方法应该在子类的onCreate方法中，绑定Layout文件后被调用�?
//     */
//    protected abstract void initListeners();
//
//    /**
//     * 绑定View组件和Adapter�? 本方法应该在子类的onCreate方法中，绑定Layout文件后被调用�?
//     */
//    protected abstract void bindData();
//
//    /**
//     * 注册上下文菜单等�? 本方法应该在子类的onCreate方法中，绑定Layout文件后被调用�?
//     */
//    protected abstract void register();

    public static void disableHardwareAcceleration(View pView) {
	if (pView != null) {
	    try {
		Class<? extends View> _Clazz = pView.getClass();
		Method _SetLayerTypeMethod = _Clazz.getMethod("setLayerType",
			int.class, Paint.class);
		Field _LayerTypeSoftwareField = _Clazz
			.getField("LAYER_TYPE_SOFTWARE");
		_SetLayerTypeMethod.invoke(pView,
			_LayerTypeSoftwareField.get(pView), null);
	    } catch (Exception e) {
		Log.e("ActivityBase", "disableHardwareAcceleration", e);
	    }
	}
    }

    @Override
    protected void onStop() {
	super.onStop();
    }
}
