package com.jy.environment.base;

import com.jy.environment.controls.AsyncTask;



public abstract class AsyncTaskBase<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
	protected static final int RESULT_OK = 1;
	protected static final int RESULT_FAIL = -1;
	protected static final int RESULT_CUSTOM = 100;
}
