package com.facepp.demo;

import android.app.Application;

import me.kkwang.commonlib.CommonLibUtils;

/**
 * Created by Administrator on 2017/9/15.
 */

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CommonLibUtils.init(this);
	}
}
