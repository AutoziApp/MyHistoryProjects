package com.jy.environment.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.os.Environment;

import com.jy.environment.util.MyLog;

public class NewsCounts implements Serializable {
	private static final long serialVersionUID = 2L;
	// private String province;
	private String city;
	private String time;

	public NewsCounts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewsCounts(String city, String time) {
		super();
		this.city = city;
		this.time = time;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "NewsCounts [city=" + city + ", time=" + time + "]";
	}

	public static boolean deleteInfo(Context context, NewsCounts info) {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		List<NewsCounts> infos = null;
		try {
			// 存入数据
			File file = new File(Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "weibao"
					+ File.separator
					+ "counts.date");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			if (file.exists()) {
				infos = getNewsCount(context);
				if (null != infos) {
					deleteInfoAll(context);
				}
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			for (int i = 0; i < infos.size(); i++) {
				if (infos.get(i).getCity().equals(info.getCity()) ) {
					infos.remove(i);
				}
			}
			if (infos.size() > 0) {
				fileOutputStream = new FileOutputStream(file.toString());
				objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(infos);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean saveInfo(Context context, NewsCounts info) {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		List<NewsCounts> infos = null;
		try {
			// 存入数据
			File file = new File(Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "weibao"
					+ File.separator
					+ "counts.date");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			if (file.exists()) {
				infos = getNewsCount(context);
				if (null != infos) {
					deleteInfoAll(context);
				}
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			if (null != infos) {
				infos.add(info);
			} else {
				infos = new ArrayList<NewsCounts>();
				infos.add(info);
			}
			fileOutputStream = new FileOutputStream(file.toString());
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(infos);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean deleteInfoAll(Context context) {
		try {
			// 存入数据
			File file = new File(Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "weibao"
					+ File.separator
					+ "counts.date");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			if (file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public static ArrayList<NewsCounts> getNewsCount(Context context) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		ArrayList<NewsCounts> counts = null;
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "weibao"
					+ File.separator
					+ "counts.date");

			if (!file.exists()) {
				return counts;
			}
			// 取出数据
			fileInputStream = new FileInputStream(file.toString());
			objectInputStream = new ObjectInputStream(fileInputStream);
			counts = (ArrayList<NewsCounts>) objectInputStream.readObject();
			return counts;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return counts;
	}
}
