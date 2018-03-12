package cn.com.mapuni.meshing.base.business;

/**
 * FileName: ObjectFactory.java Description: ͨ�������ƴ���ҵ����ʵ��
 * 
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:04:12
 */
public class BaseObjectFactory {

	public final static String BASE_PACKAGENAME = "cn.com.mapuni.meshing.base.business.";
	public final static String CHILD_PACKAGENAME = "cn.com.mapuni.meshingtotal.business.";
	public final static String YQYD_PACKAGENAME = "cn.com.mapuni.meshing.infoQuery.";
	

	/**
	 * Description: ����ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ����Ԫ Create at: 2012-12-4 ����10:04:44
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
	 * Description: ����ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ����Ԫ Create at: 2012-12-4 ����10:04:44
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
	 * Description: ����һ��һ��ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ����Ԫ Create at: 2012-12-4 ����10:04:44
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
	 * Description: ����helper����ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ������ Create at: 2013-8-26 ����14:29:23
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
	 * Description: ����settingϵͳ����ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ������ Create at: 2013-8-26 ����14:29:23
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
	 * Description: ������Ϣ��ѯϵͳ����ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ��˧ Create at: 2013-8-26 ����14:29:23
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
	 * Description: ����ҵ�����
	 * 
	 * @param className
	 *            ����
	 * @return ҵ�����
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *             Object
	 * @author ����Ԫ Create at: 2012-12-4 ����10:04:44
	 */
	public static Class<?> createObject(String className, String packagename)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Class<?> businessClass = null;
		businessClass = Class.forName(packagename + "." + className);
		return businessClass;
	}
}
