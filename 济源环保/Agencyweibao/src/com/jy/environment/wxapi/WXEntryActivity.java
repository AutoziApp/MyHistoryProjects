/**
 * Copyright (c) 2013 Baidu.com, Inc. All Rights Reserved
 */
package com.jy.environment.wxapi;

import com.baidu.frontia.activity.share.FrontiaWeixinShareActivity;

public class WXEntryActivity extends FrontiaWeixinShareActivity {

	@Override
	protected boolean handleIntent() {
		if (super.handleIntent()) {
			return true;
		} else {
			return false;
		}
	}
	
}
