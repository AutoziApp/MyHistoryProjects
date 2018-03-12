package com.mapuni.android.base.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * FileName: CityAdapter.java Description: 适配器
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-5 上午09:03:46
 */
public class CityAdapter<T> extends BaseAdapter implements Filterable {
	/**
	 * 这种ArrayAdapter包含对象的列表表示的数据 这个列表的内容称为“数组”的文档 Contains the list of objects
	 * that represent the data of this ArrayAdapter. The content of this list is
	 * referred to as "the array" in the documentation.
	 */
	private List<T> mObjects;
	private List<T> mObjects2;

	/**
	 * 锁用于修改内容的{ @link # mObjects }。 执行任何写操作数组应该同步这把锁。 这把锁也使用过滤器(见{ @link
	 * #getFilter()}来让副本的原始数组同步数据。
	 * 
	 * Lock used to modify the content of{@link #mObjects}. Any write operation
	 * performed on the array should be synchronized on this lock. This lock is
	 * also used by the filter (see {@link #getFilter()} to make a synchronized
	 * copy of the original array of data.
	 */
	private final Object mLock = new Object();

	/**
	 * 来显示这个数组内容的适配器样式的布局id The resource indicating what views to inflate to
	 * display the content of this array adapter.
	 */
	private int mResource;

	/**
	 * 来显示这个数组的某一项的样式的布局id The resource indicating what views to inflate to
	 * display the content of this array adapter in a drop down widget.
	 */
	private int mDropDownResource;

	/**
	 * 定义一个在布局文件里TextView的resourceID If the inflated resource is not a TextView,
	 * {@link #mFieldId} is used to find a TextView inside the inflated views
	 * hierarchy. This field must contain the identifier that matches the one
	 * defined in the resource file.
	 */
	private int mFieldId = 0;

	/**
	 * 标识适配器数据源是否需要更改 Indicates whether or not {@link #notifyDataSetChanged()}
	 * must be called whenever {@link #mObjects} is modified.
	 */
	private boolean mNotifyOnChange = true;

	/** 上下文 */
	private Context mContext;

	/** 定义一个适配器的数据源集合 */
	private ArrayList<T> mOriginalValues;
	/** 内部类ArrayFilter */
	private ArrayFilter mFilter;
	/** 布局扩充其 */
	private LayoutInflater mInflater;

	/**
	 * Description: 构造方法
	 * 
	 * @param context
	 *            上下文 context The current context.
	 * @param textViewResourceId
	 *            布局文件中的组件id textViewResourceId The resource ID for a layout
	 *            file containing a TextView to use when instantiating views.
	 */
	public CityAdapter(Context context, int textViewResourceId) {
		init(context, textViewResourceId, 0, new ArrayList<T>(),
				new ArrayList<T>());
	}

	/**
	 * Description: 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            需要实例化布局文件的时候，在资源文件中的id值 The resource ID for a layout file
	 *            containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            TextView在布局资源的id The id of the TextView within the layout
	 *            resource to be populated
	 */
	public CityAdapter(Context context, int resource, int textViewResourceId) {
		init(context, resource, textViewResourceId, new ArrayList<T>(),
				new ArrayList<T>());
	}

	/**
	 * Description: 构造方法
	 * 
	 * @param context
	 *            上下文 The current context.
	 * @param textViewResourceId
	 *            需要实例化布局文件的时候，在资源文件中的id值 The resource ID for a layout file
	 *            containing a TextView to use when instantiating views.
	 * @param objects
	 *            数据源 The objects to represent in the ListView.
	 * @param objects2
	 *            城市拼音数组
	 */
	public CityAdapter(Context context, int textViewResourceId, T[] objects,
			T[] objects2) {
		init(context, textViewResourceId, 0, Arrays.asList(objects),
				Arrays.asList(objects2));
	}

	/**
	 * Description: 构造方法
	 * 
	 * @param context
	 *            上下文 The current context.
	 * @param resource
	 *            需要实例化布局文件的时候，在资源文件中的id值 The resource ID for a layout file
	 *            containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            TextView在布局资源的id The id of the TextView within the layout
	 *            resource to be populated
	 * @param objects
	 *            ListView中填充的数据源 The objects to represent in the ListView.
	 * @param objects2
	 *            城市拼音数组
	 */
	public CityAdapter(Context context, int resource, int textViewResourceId,
			T[] objects, T[] objects2) {
		init(context, resource, textViewResourceId, Arrays.asList(objects),
				Arrays.asList(objects2));
	}

	/**
	 * Description: 构造方法
	 * 
	 * @param context
	 *            上下文 The current context.
	 * @param textViewResourceId
	 *            需要实例化布局文件的时候，在资源文件中的id值 The resource ID for a layout file
	 *            containing a TextView to use when instantiating views.
	 * @param objects
	 *            ListView中填充的数据源 The objects to represent in the ListView.
	 * @param objects2
	 *            城市拼音数组
	 */
	public CityAdapter(Context context, int textViewResourceId,
			List<T> objects, List<T> objects2) {
		init(context, textViewResourceId, 0, objects, objects2);
	}

	/**
	 * Description: 构造方法
	 * 
	 * @param context
	 *            上下文 The current context.
	 * @param resource
	 *            需要实例化布局文件的时候，在资源文件中的id值 The resource ID for a layout file
	 *            containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            TextView在布局资源的id The id of the TextView within the layout
	 *            resource to be populated
	 * @param objects
	 *            ListView中填充的数据源 The objects to represent in the ListView.
	 * @param objects2
	 *            城市拼音数组
	 */
	public CityAdapter(Context context, int resource, int textViewResourceId,
			List<T> objects, List<T> objects2) {
		init(context, resource, textViewResourceId, objects, objects2);
	}

	/**
	 * Description: 在指定的Object对象里添加对象 Adds the specified object at the end of
	 * the array.
	 * 
	 * @param object
	 *            要添加的对象 The object to add at the end of the array.
	 */
	public void add(T object) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.add(object);
				if (mNotifyOnChange)
					notifyDataSetChanged();
			}
		} else {
			mObjects.add(object);
			if (mNotifyOnChange)
				notifyDataSetChanged();
		}
	}

	/**
	 * Description: 在指定的Object对象里的位置里添加对象 Inserts the specified object at the
	 * specified index in the array.
	 * 
	 * @param object
	 *            要添加的对象 The object to insert into the array.
	 * @param index
	 *            要添加的位置 The index at which the object must be inserted.
	 */
	public void insert(T object, int index) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.add(index, object);
				if (mNotifyOnChange)
					notifyDataSetChanged();
			}
		} else {
			mObjects.add(index, object);
			if (mNotifyOnChange)
				notifyDataSetChanged();
		}
	}

	/**
	 * Description: 从指定的array里一处一个对象 Removes the specified object from the
	 * array.
	 * 
	 * @param object
	 *            要移除的对象 The object to remove.
	 */
	public void remove(T object) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.remove(object);
			}
		} else {
			mObjects.remove(object);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Description: 移除列表中所有的元素 Remove all elements from the list.
	 */
	public void clear() {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.clear();
			}
		} else {
			mObjects.clear();
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Description: 通过指定的比较器进行排序 Sorts the content of this adapter using the
	 * specified comparator.
	 * 
	 * @param comparator
	 *            用来排序包含在这个适配器的对象的比较器 The comparator used to sort the objects
	 *            contained in this adapter.
	 */
	public void sort(Comparator<? super T> comparator) {
		Collections.sort(mObjects, comparator);
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mNotifyOnChange = true;
	}

	/**
	 * Description: 该方法用来控制列表内容改变
	 * 
	 * @param notifyOnChange
	 *            默认值是true Control whether methods that change the list (
	 *            {@link #add}, {@link #insert}, {@link #remove}, {@link #clear}
	 *            ) automatically call {@link #notifyDataSetChanged}. If set to
	 *            false, caller must manually call notifyDataSetChanged() to
	 *            have the changes 214. * reflected in the attached view. 215. *
	 *            216. * The default is true, and calling notifyDataSetChanged()
	 *            217. * resets the flag to true. 218. * 219. * @param
	 *            notifyOnChange if true, modifications to the list will 220. *
	 *            automatically call {@link 221. * #notifyDataSetChanged} 222.
	 */
	public void setNotifyOnChange(boolean notifyOnChange) {
		mNotifyOnChange = notifyOnChange;
	}

	/**
	 * Description: 初始化适配器
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            需要实例化布局文件的时候，在资源文件中的id值
	 * @param textViewResourceId
	 *            TextView在布局资源的id
	 * @param objects
	 *            ListView中填充的数据源
	 * @param objects2
	 *            城市拼音数组 void Create at: 2012-12-5 上午09:55:43
	 */
	private void init(Context context, int resource, int textViewResourceId,
			List<T> objects, List<T> objects2) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResource = mDropDownResource = resource;
		mObjects = objects;
		mObjects2 = objects2;
		mFieldId = textViewResourceId;
	}

	/**
	 * Description: 返回上下文与此相关的数组适配器 上下文是用于创建视图从资源传递到构造函数。 Returns the context
	 * associated with this array adapter. The context is used to create views
	 * from the resource passed to the constructor.
	 * 
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * Description: 返回适配器数据源的条数 {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return mObjects.size();
	}

	/**
	 * Description: 从适配器中得到某条数据。或者对象 {@inheritDoc}
	 */
	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	/**
	 * Description: 从数组中返回指定的数据或者是对象 Returns the position of the specified item
	 * in the array.
	 * 
	 * @param item
	 *            适配器里封装的一个对象 The item to retrieve the position of. 264. * 265.
	 *            *
	 * @return 该对象在数据源中的位置 The position of the specified item. 266.
	 */
	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	/**
	 * Description: 得到某一项的位置将int类型转换为long
	 * 
	 * @param position
	 *            位置
	 * @return 返回long类型的位置数值 272. * {@inheritDoc} 273.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Description: 通过资源文件创建视图
	 * 
	 * @param position
	 *            位置
	 * @param convertView
	 *            视图
	 * @param parent
	 *            要添加的ViewGroup
	 * @param resource
	 *            资源布局文件的id值
	 * @return 返回一个视图 View 279. * {@inheritDoc} 280.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	/**
	 * Description: 通过资源文件创建视图
	 * 
	 * @param position
	 *            位置
	 * @param convertView
	 *            视图
	 * @param parent
	 *            要添加的ViewGroup
	 * @param resource
	 *            资源布局文件的id值
	 * @return 返回一个视图 View Create at: 2012-12-5 上午11:26:01
	 */
	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View view;
		TextView text;

		if (convertView == null) {
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		try {
			if (mFieldId == 0) {
				/**
				 * If no custom field is assigned, assume the whole resource is
				 * a TextView 假设整个资源是一个TextView,没有自定义TextView被指定
				 */
				text = (TextView) view;
			} else {
				/**
				 * Otherwise, find the TextView field within the layout
				 * 否则,实例化一个TextView
				 */
				text = (TextView) view.findViewById(mFieldId);
			}
		} catch (ClassCastException e) {
			Log.e("ArrayAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"ArrayAdapter requires the resource ID to be a TextView", e);
		}

		text.setText(getItem(position).toString());

		return view;
	}

	/**
	 * Description: 设置布局资源创建下拉的风格 316. *
	 * <p>
	 * Sets the layout resource to create the drop down views.
	 * </p>
	 * 317. * 318. * @param resource the layout resource defining the drop down
	 * views 319. * @see #getDropDownView(int, android.view.View,
	 * android.view.ViewGroup) 320.
	 */
	public void setDropDownViewResource(int resource) {
		this.mDropDownResource = resource;
	}

	/**
	 * 326. * {@inheritDoc} 327.
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent,
				mDropDownResource);
	}

	/**
	 * Description: 创建一个新的ArrayAdapter 内容的数组是通过{ @link android内容res资源#
	 * getTextArray(int)}。
	 * 
	 * @param context上下文
	 * @param textArrayResId
	 *            数组的textArrayResId资源文件的id
	 * @param textViewResId
	 *            textView在布局中的id用于创建视图。
	 * @return 一个ArrayAdapter < CharSequence >。 334. * Creates a new
	 *         ArrayAdapter from external resources. The content of the array is
	 *         335. * obtained through
	 *         {@link android.content.res.Resources#getTextArray(int)}. 336. *
	 *         337. * @param context The application's environment. 338. * @param
	 *         textArrayResId The identifier of the array to use as the data
	 *         source. 339. * @param textViewResId The identifier of the layout
	 *         used to create views. 340. * 341. * @return An
	 *         ArrayAdapter<CharSequence>. 342.
	 */
	public static ArrayAdapter<CharSequence> createFromResource(
			Context context, int textArrayResId, int textViewResId) {
		CharSequence[] strings = context.getResources().getTextArray(
				textArrayResId);
		return new ArrayAdapter<CharSequence>(context, textViewResId, strings);
	}

	/**
	 * Description: 返回当前ArrayFilter 350. * {@inheritDoc} 351.
	 */
	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	/**
	 * Description: 一个数组过滤器,限制适配器数组内容.每一项都有一个前缀，如果没有则从列表中删除 360. *
	 * <p>
	 * An array filter constrains the content of the array adapter with 361. * a
	 * prefix. Each item that does not start with the supplied prefix 362. * is
	 * removed from the list.
	 * </p>
	 * 363.
	 */
	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<T>(mObjects);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					ArrayList<T> list = new ArrayList<T>(mOriginalValues);
					results.values = list;
					results.count = list.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();

				final ArrayList<T> values = mOriginalValues;
				final int count = values.size();

				final ArrayList<T> newValues = new ArrayList<T>(count);

				for (int i = 0; i < count; i++) {
					final T value = values.get(i);
					final String valueText = value.toString().toLowerCase();

					final T value2 = mObjects2.get(i);
					final String valueText2 = value2.toString().toLowerCase();

					/** 查找拼音 */
					if (valueText2.startsWith(prefixString)) {
						newValues.add(value);
						/** 查找汉字 */
					} else if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {

						/** 添加汉字关联 */
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}

						/** 添加拼音关联汉字 */
						final String[] words2 = valueText2.split(" ");
						final int wordCount2 = words2.length;

						for (int k = 0; k < wordCount2; k++) {
							if (words2[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}

					}

				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			mObjects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
