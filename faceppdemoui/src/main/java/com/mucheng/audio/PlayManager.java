package com.mucheng.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import me.kkwang.commonlib.base.AppContextUtil;

/**
 * Created by Administrator on 2017/9/15.
 */

public class PlayManager {

	private Context mContext;
	private MediaPlayer mediaPlayer;

	private static PlayManager sManager = null;

	public synchronized static PlayManager getInstance (Context context) {
		if (sManager == null) {
			sManager = new PlayManager(context.getApplicationContext());
		}
		return sManager;
	}
	public synchronized static PlayManager getInstance () {
		if (sManager == null) {
			sManager = new PlayManager(AppContextUtil.getAppContext());
		}
		return sManager;
	}

	private PlayManager (Context context) {
		mContext = context;
		mediaPlayer = new MediaPlayer();
	}

	public void startPlay(String path){
		try{
			AssetFileDescriptor afd = mContext.getAssets().openFd(path);
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mediaPlayer.prepareAsync();
			mediaPlayer.start();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void stopPlay(){
		mediaPlayer.stop();
	}

	public void setListener(MediaPlayer.OnCompletionListener onCompletionListener){
		mediaPlayer.setOnCompletionListener(onCompletionListener);
	}
}
