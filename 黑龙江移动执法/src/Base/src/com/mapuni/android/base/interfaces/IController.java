package com.mapuni.android.base.interfaces;


public interface IController {

/*
初始化模块消息
*/
public static final int MODLE_INIT = 100;

/*
退出模块消息，数据保留
*/
public static final int MODLE_CANCEL = 101;

/*
退出模块消息，数据销毁
*/
public static final int MODLE_EIXT = 102;

/*
进入模块主界面消息
*/
public static final int OPEN_MAIN = 103;

/*
显示界面消息
*/
public static final int DIALOG_SHOW = 104;

/*
隐藏界面消息
*/
public static final int DIALOG_HIDE = 105;


/*
初始化模块需要要的所有数据
*/
public boolean ModleInit();

/*
打开模块主界面
*/
public void OpenMainDialog();

/*
退出模块，数据保留
*/
public void ModleCancel();

/*
退出模块，数据销毁
*/
public void ModleEixt();

/*
显示界面
*/
public void ShowDialog();

/*
隐藏界面
*/
public void HideDialog();

}
