package com.jy.environment.invitefriends;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class ContactsUtils {

	public static List<SortModel> searchContact(Context context) {

		List<SortModel> list = new ArrayList<SortModel>();
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		while (cursor.moveToNext()) {
			SortModel c1 = new SortModel();
			int nameFieldColumnIndex = cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			c1.setName(name);
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			c1.setContactId(contactId);
			Cursor phone = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);

			List<String> phoneNumber = new ArrayList<String>();
			while (phone.moveToNext()) {
				String strPhoneNumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phoneNumber.add(strPhoneNumber);
			}
			if (phoneNumber.size() != 0) {
				c1.setStrPhoneNumber(phoneNumber.get(0));
			}

			phone.close();
			list.add(c1);
			// ContentValues values = new ContentValues();
			// values.put("name", name);
			// values.put("isCheacked", 0);
			// values.put("strPhoneNumber",
			// Integer.parseInt(phoneNumber.get(0)));
			// DBManager.getInstances(context).insertSQLite(context,
			// DBInfo.TABLE_NAME_MAINDATA, null, values);
		}
		cursor.close();
		System.out.println(">>>>>>list" + list);
		return list;
	}
}
