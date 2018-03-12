package com.mapuni.android.uitl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;

public class BaseIntentUtil {

	public  int DEFAULT_ENTER_ANIM;
	public  int DEFAULT_EXIT_ANIM;

	private static  Intent intent;

	public  void intentDIY(Activity activity, Class<?> classes) {
		intentDIY(activity, classes, null, DEFAULT_ENTER_ANIM,
				DEFAULT_EXIT_ANIM);
	}

	public  void intentDIY(Activity activity, Class<?> classes,
			Map<String, String> paramMap) {
		intentDIY(activity, classes, paramMap, DEFAULT_ENTER_ANIM,
				DEFAULT_EXIT_ANIM);
	}

	public  void intentDIY(Activity activity, Class<?> classes,
			int enterAnim, int exitAnim) {
		intentDIY(activity, classes, null, enterAnim, exitAnim);
	}

	public static void intentDIY(Activity activity, Class<?> classes,
			Map<String, String> paramMap, int enterAnim, int exitAnim) {
		intent = new Intent(activity, classes);
		organizeAndStart(activity, classes, paramMap);
		if (enterAnim != 0 && exitAnim != 0) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	public static void intentSysDefault(Activity activity, Class<?> classes,
			Map<String, String> paramMap) {
		organizeAndStart(activity, classes, paramMap);
	}

	private static void organizeAndStart(Activity activity, Class<?> classes,
			Map<String, String> paramMap) {
		intent = new Intent(activity, classes);
		if (null != paramMap) {
			Set<String> set = paramMap.keySet();
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				intent.putExtra(key, paramMap.get(key));
			}
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}

}
