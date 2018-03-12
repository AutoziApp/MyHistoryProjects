package com.mapuni.android.fragment;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

public class TimePickerFragment extends DialogFragment {
	private TimePickerDialog.OnTimeSetListener listener;

	public TimePickerFragment() {
		super();
		setArguments(null);
	}

	public TimePickerFragment(OnTimeSetListener listener) {
		super();
		setArguments(null);
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), listener, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	public void setListener(TimePickerDialog.OnTimeSetListener listener) {
		this.listener = listener;
	}

}
