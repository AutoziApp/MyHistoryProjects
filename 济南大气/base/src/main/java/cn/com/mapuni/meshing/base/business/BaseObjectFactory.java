package cn.com.mapuni.meshing.base.business;

/**
 * FileName: ObjectFactory.java Description: 通过类名称创建业务类实例
 * 
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:04:12
 */
public class BaseObjectFactory {

	public final static String BASE_PACKAGENAME = "cn.com.mapuni.meshing.base.business.";
	public final static String CHILD_PACKAGENAME = "cn.com.mapuni.meshingtotal.business.";
	public final static String YQYD_PACKAGENAME = "cn.com.mapuni.meshing.infoQuery.";
	

	/**
	 * Description: 创建业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 张信元 Create at: 2012-12-4 上午10:04:44
	 */
	public static Object createBaseObject(String className)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Object obj = null;
		Class<?> businessClass = Class.forName(BASE_PACKAGENAME + className);
		obj = businessClass.newInstance();
		return obj;
	}

	/**
	 * Description: 创建业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 张信元 Create at: 2012-12-4 上午10:04:44
	 */
	public static Object createObject(String className)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Object obj = null;
		Class<?> businessClass = null;
		businessClass = Class.forName(CHILD_PACKAGENAME + className);
		obj = businessClass.newInstance();
		return obj;
	}
	/**
	 * Description: 创建一企一档业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 张信元 Create at: 2012-12-4 上午10:04:44
	 */
	public static Object createYQYDObject(String className)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Object obj = null;
		Class<?> businessClass = null;
		businessClass = Class.forName(YQYD_PACKAGENAME + className);
		obj = businessClass.newInstance();
		return obj;
	}

	/**
	 * Description: 创建helper帮助业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 赵瑞青 Create at: 2013-8-26 下午14:29:23
	 */
	public static Object createHelperObject(String className)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Object obj = null;
		Class<?> businessClass = null;
		businessClass = Class.forName("com.mapuni.android.helper." + className);
		obj = businessClass.newInstance();
		return obj;
	}

	/**
	 * Description: 创建setting系统设置业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 赵瑞青 Create at: 2013-8-26 下午14:29:23
	 */
	public static Object createSettingObject(String className)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Object obj = null;
		Class<?> businessClass = null;
		businessClass = Class
				.forName("com.mapuni.android.setting." + className);
		obj = businessClass.newInstance();
		return obj;
	}

	/**
	 * Description: 创建信息查询系统设置业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 邵帅 Create at: 2013-8-26 下午14:29:23
	 */
	public static Object createinfoQueryObject(String className)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Object obj = null;
		Class<?> businessClass = null;
		businessClass = Class.forName("com.mapuni.android.infoQuery."
				+ className);
		obj = businessClass.newInstance();
		return obj;
	}

	/**
	 * Description: 创建业务对象
	 * 
	 * @param className
	 *            类名
	 * @return 业务对象
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author 张信元 Create at: 2012-12-4 上午10:04:44
	 */
	public static Class<?> createObject(String className, String packagename)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Class<?> businessClass = null;
		businessClass = Class.forName(packagename + "." + className);
		return businessClass;
	}
}
