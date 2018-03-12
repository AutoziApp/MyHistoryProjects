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
 * FileName: CityAdapter.java Description: ������
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����09:03:46
 */
public class CityAdapter<T> extends BaseAdapter implements Filterable {
	/**
	 * ����ArrayAdapter����������б��ʾ������ ����б�����ݳ�Ϊ�����顱���ĵ� Contains the list of objects
	 * that represent the data of this ArrayAdapter. The content of this list is
	 * referred to as "the array" in the documentation.
	 */
	private List<T> mObjects;
	private List<T> mObjects2;

	/**
	 * �������޸����ݵ�{ @link # mObjects }�� ִ���κ�д��������Ӧ��ͬ��������� �����Ҳʹ�ù�����(��{ @link
	 * #getFilter()}���ø�����ԭʼ����ͬ�����ݡ�
	 * 
	 * Lock used to modify the content of{@link #mObjects}. Any write operation
	 * performed on the array should be synchronized on this lock. This lock is
	 * also used by the filter (see {@link #getFilter()} to make a synchronized
	 * copy of the original array of data.
	 */
	private final Object mLock = new Object();

	/**
	 * ����ʾ����������ݵ���������ʽ�Ĳ���id The resource indicating what views to inflate to
	 * display the content of this array adapter.
	 */
	private int mResource;

	/**
	 * ����ʾ��������ĳһ�����ʽ�Ĳ���id The resource indicating what views to inflate to
	 * display the content of this array adapter in a drop down widget.
	 */
	private int mDropDownResource;

	/**
	 * ����һ���ڲ����ļ���TextView��resourceID If the inflated resource is not a TextView,
	 * {@link #mFieldId} is used to find a TextView inside the inflated views
	 * hierarchy. This field must contain the identifier that matches the one
	 * defined in the resource file.
	 */
	private int mFieldId = 0;

	/**
	 * ��ʶ����������Դ�Ƿ���Ҫ���� Indicates whether or not {@link #notifyDataSetChanged()}
	 * must be called whenever {@link #mObjects} is modified.
	 */
	private boolean mNotifyOnChange = true;

	/** ������ */
	private Context mContext;

	/** ����һ��������������Դ���� */
	private ArrayList<T> mOriginalValues;
	/** �ڲ���ArrayFilter */
	private ArrayFilter mFilter;
	/** ���������� */
	private LayoutInflater mInflater;

	/**
	 * Description: ���췽��
	 * 
	 * @param context
	 *            ������ context The current context.
	 * @param textViewResourceId
	 *            �����ļ��е����id textViewResourceId The resource ID for a layout
	 *            file containing a TextView to use when instantiating views.
	 */
	public CityAdapter(Context context, int textViewResourceId) {
		init(context, textViewResourceId, 0, new ArrayList<T>(),
				new ArrayList<T>());
	}

	/**
	 * Description: ���췽��
	 * 
	 * @param context
	 *            ������
	 * @param resource
	 *            ��Ҫʵ���������ļ���ʱ������Դ�ļ��е�idֵ The resource ID for a layout file
	 *            containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            TextView�ڲ�����Դ��id The id of the TextView within the layout
	 *            resource to be populated
	 */
	public CityAdapter(Context context, int resource, int textViewResourceId) {
		init(context, resource, textViewResourceId, new ArrayList<T>(),
				new ArrayList<T>());
	}

	/**
	 * Description: ���췽��
	 * 
	 * @param context
	 *            ������ The current context.
	 * @param textViewResourceId
	 *            ��Ҫʵ���������ļ���ʱ������Դ�ļ��е�idֵ The resource ID for a layout file
	 *            containing a TextView to use when instantiating views.
	 * @param objects
	 *            ����Դ The objects to represent in the ListView.
	 * @param objects2
	 *            ����ƴ������
	 */
	public CityAdapter(Context context, int textViewResourceId, T[] objects,
			T[] objects2) {
		init(context, textViewResourceId, 0, Arrays.asList(objects),
				Arrays.asList(objects2));
	}

	/**
	 * Description: ���췽��
	 * 
	 * @param context
	 *            ������ The current context.
	 * @param resource
	 *            ��Ҫʵ���������ļ���ʱ������Դ�ļ��е�idֵ The resource ID for a layout file
	 *            containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            TextView�ڲ�����Դ��id The id of the TextView within the layout
	 *            resource to be populated
	 * @param objects
	 *            ListView����������Դ The objects to represent in the ListView.
	 * @param objects2
	 *            ����ƴ������
	 */
	public CityAdapter(Context context, int resource, int textViewResourceId,
			T[] objects, T[] objects2) {
		init(context, resource, textViewResourceId, Arrays.asList(objects),
				Arrays.asList(objects2));
	}

	/**
	 * Description: ���췽��
	 * 
	 * @param context
	 *            ������ The current context.
	 * @param textViewResourceId
	 *            ��Ҫʵ���������ļ���ʱ������Դ�ļ��е�idֵ The resource ID for a layout file
	 *            containing a TextView to use when instantiating views.
	 * @param objects
	 *            ListView����������Դ The objects to represent in the ListView.
	 * @param objects2
	 *            ����ƴ������
	 */
	public CityAdapter(Context context, int textViewResourceId,
			List<T> objects, List<T> objects2) {
		init(context, textViewResourceId, 0, objects, objects2);
	}

	/**
	 * Description: ���췽��
	 * 
	 * @param context
	 *            ������ The current context.
	 * @param resource
	 *            ��Ҫʵ���������ļ���ʱ������Դ�ļ��е�idֵ The resource ID for a layout file
	 *            containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            TextView�ڲ�����Դ��id The id of the TextView within the layout
	 *            resource to be populated
	 * @param objects
	 *            ListView����������Դ The objects to represent in the ListView.
	 * @param objects2
	 *            ����ƴ������
	 */
	public CityAdapter(Context context, int resource, int textViewResourceId,
			List<T> objects, List<T> objects2) {
		init(context, resource, textViewResourceId, objects, objects2);
	}

	/**
	 * Description: ��ָ����Object��������Ӷ��� Adds the specified object at the end of
	 * the array.
	 * 
	 * @param object
	 *            Ҫ��ӵĶ��� The object to add at the end of the array.
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
	 * Description: ��ָ����Object�������λ������Ӷ��� Inserts the specified object at the
	 * specified index in the array.
	 * 
	 * @param object
	 *            Ҫ��ӵĶ��� The object to insert into the array.
	 * @param index
	 *            Ҫ��ӵ�λ�� The index at which the object must be inserted.
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
	 * Description: ��ָ����array��һ��һ������ Removes the specified object from the
	 * array.
	 * 
	 * @param object
	 *            Ҫ�Ƴ��Ķ��� The object to remove.
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
	 * Description: �Ƴ��б������е�Ԫ�� Remove all elements from the list.
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
	 * Description: ͨ��ָ���ıȽ����������� Sorts the content of this adapter using the
	 * specified comparator.
	 * 
	 * @param comparator
	 *            �����������������������Ķ���ıȽ��� The comparator used to sort the objects
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
	 * Description: �÷������������б����ݸı�
	 * 
	 * @param notifyOnChange
	 *            Ĭ��ֵ��true Control whether methods that change the list (
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
	 * Description: ��ʼ��������
	 * 
	 * @param context
	 *            ������
	 * @param resource
	 *            ��Ҫʵ���������ļ���ʱ������Դ�ļ��е�idֵ
	 * @param textViewResourceId
	 *            TextView�ڲ�����Դ��id
	 * @param objects
	 *            ListView����������Դ
	 * @param objects2
	 *            ����ƴ������ void Create at: 2012-12-5 ����09:55:43
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
	 * Description: ���������������ص����������� �����������ڴ�����ͼ����Դ���ݵ����캯���� Returns the context
	 * associated with this array adapter. The context is used to create views
	 * from the resource passed to the constructor.
	 * 
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * Description: ��������������Դ������ {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return mObjects.size();
	}

	/**
	 * Description: ���������еõ�ĳ�����ݡ����߶��� {@inheritDoc}
	 */
	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	/**
	 * Description: �������з���ָ�������ݻ����Ƕ��� Returns the position of the specified item
	 * in the array.
	 * 
	 * @param item
	 *            ���������װ��һ������ The item to retrieve the position of. 264. * 265.
	 *            *
	 * @return �ö���������Դ�е�λ�� The position of the specified item. 266.
	 */
	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	/**
	 * Description: �õ�ĳһ���λ�ý�int����ת��Ϊlong
	 * 
	 * @param position
	 *            λ��
	 * @return ����long���͵�λ����ֵ 272. * {@inheritDoc} 273.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Description: ͨ����Դ�ļ�������ͼ
	 * 
	 * @param position
	 *            λ��
	 * @param convertView
	 *            ��ͼ
	 * @param parent
	 *            Ҫ��ӵ�ViewGroup
	 * @param resource
	 *            ��Դ�����ļ���idֵ
	 * @return ����һ����ͼ View 279. * {@inheritDoc} 280.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	/**
	 * Description: ͨ����Դ�ļ�������ͼ
	 * 
	 * @param position
	 *            λ��
	 * @param convertView
	 *            ��ͼ
	 * @param parent
	 *            Ҫ��ӵ�ViewGroup
	 * @param resource
	 *            ��Դ�����ļ���idֵ
	 * @return ����һ����ͼ View Create at: 2012-12-5 ����11:26:01
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
				 * a TextView ����������Դ��һ��TextView,û���Զ���TextView��ָ��
				 */
				text = (TextView) view;
			} else {
				/**
				 * Otherwise, find the TextView field within the layout
				 * ����,ʵ����һ��TextView
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
	 * Description: ���ò�����Դ���������ķ�� 316. *
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
	 * Description: ����һ���µ�ArrayAdapter ���ݵ�������ͨ��{ @link android����res��Դ#
	 * getTextArray(int)}��
	 * 
	 * @param context������
	 * @param textArrayResId
	 *            �����textArrayResId��Դ�ļ���id
	 * @param textViewResId
	 *            textView�ڲ����е�id���ڴ�����ͼ��
	 * @return һ��ArrayAdapter < CharSequence >�� 334. * Creates a new
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
	 * Description: ���ص�ǰArrayFilter 350. * {@inheritDoc} 351.
	 */
	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	/**
	 * Description: һ�����������,������������������.ÿһ���һ��ǰ׺�����û������б���ɾ�� 360. *
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

					/** ����ƴ�� */
					if (valueText2.startsWith(prefixString)) {
						newValues.add(value);
						/** ���Һ��� */
					} else if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {

						/** ��Ӻ��ֹ��� */
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}

						/** ���ƴ���������� */
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
