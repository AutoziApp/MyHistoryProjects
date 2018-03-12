package com.mapuni.android.base.interfaces;


public interface IController {

/*
��ʼ��ģ����Ϣ
*/
public static final int MODLE_INIT = 100;

/*
�˳�ģ����Ϣ�����ݱ���
*/
public static final int MODLE_CANCEL = 101;

/*
�˳�ģ����Ϣ����������
*/
public static final int MODLE_EIXT = 102;

/*
����ģ����������Ϣ
*/
public static final int OPEN_MAIN = 103;

/*
��ʾ������Ϣ
*/
public static final int DIALOG_SHOW = 104;

/*
���ؽ�����Ϣ
*/
public static final int DIALOG_HIDE = 105;


/*
��ʼ��ģ����ҪҪ����������
*/
public boolean ModleInit();

/*
��ģ��������
*/
public void OpenMainDialog();

/*
�˳�ģ�飬���ݱ���
*/
public void ModleCancel();

/*
�˳�ģ�飬��������
*/
public void ModleEixt();

/*
��ʾ����
*/
public void ShowDialog();

/*
���ؽ���
*/
public void HideDialog();

}
