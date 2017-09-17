package me.kkwang.commonlib.guide;

import android.support.v4.view.PagerAdapter;

import java.util.List;


public interface AutoInterface {
    void updateIndicatorView(int size);

    void setAdapter(PagerAdapter adapter);

    void startScorll();

    void endScorll();
}
