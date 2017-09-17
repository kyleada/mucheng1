package com.mucheng.audio;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2017/9/15.
 */

public class NarratorSpeak {

	public static final String ENG_PATH_1 = "narrator/eng1.mp3";

	public static void startPlay1(MediaPlayer.OnCompletionListener onCompletionListener){
		PlayManager.getInstance().startPlay(ENG_PATH_1);
		PlayManager.getInstance().setListener(onCompletionListener);
	}
}
