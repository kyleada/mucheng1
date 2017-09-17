package me.kkwang.commonlib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import butterknife.ButterKnife;
import me.kkwang.commonlib.R;
import me.kkwang.commonlib.utils.ToastUtils;

/**
 * Created by kw on 2016/3/11.
 */
public abstract class BaseToolBarActivity extends AppCompatActivity {


    protected Activity mActivity;
    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHidden = false;

    protected abstract int getLayoutId();

    public abstract String getToolbarTitle();

    protected boolean isActionBarNeedBackEnable(){
		return true;
	}

    protected  void beforeSetContentView(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    };
    protected abstract void afterCreate(Bundle savedInstanceState);

    protected  int getAppBarLayoutId(){ return -1;};

    protected  int getToolbarLayoutId(){return -1;};



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getLayoutId());
        mActivity = this;
        if(getAppBarLayoutId() != -1){
            mAppBar = ButterKnife.findById(this,getAppBarLayoutId());
        }else{
            mAppBar =  ButterKnife.findById(this,R.id.app_bar_layout);
        }

        if(getToolbarLayoutId() != -1){
            mToolbar = ButterKnife.findById(this,getToolbarLayoutId());
        }else{
            mToolbar =  ButterKnife.findById(this,R.id.toolbar);
        }

        if (mToolbar == null) {
            throw new IllegalStateException("No toolbar");
        }
        mToolbar.setTitle(getToolbarTitle());
        setSupportActionBar(mToolbar);

        if (isActionBarNeedBackEnable()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }
        afterCreate(savedInstanceState);
    }

    @Override public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }


    protected void setAppBarAlpha(float alpha) {
		if(mAppBar != null){
			mAppBar.setAlpha(alpha);
		}

    }


    protected void hideOrShowToolbar() {
		if(mAppBar != null){
			mAppBar.animate()
					.translationY(mIsHidden ? 0 : -mAppBar.getHeight())
					.setInterpolator(new DecelerateInterpolator(2))
					.start();

			mIsHidden = !mIsHidden;
		}
    }

    public void startActivityClass(Class target){
        Intent intent = new Intent(this,target);
        startActivity(intent);
    }

    public void startActivityForResultClass(Class target, int requestCode){
        Intent intent=new Intent(this,target);
        startActivityForResult(intent,requestCode);
    }

    public void startActivityForResultClass(Class target, Bundle bundle, int requestCode){
        Intent intent=new Intent(this,target);
        intent.putExtra("info", bundle);
        startActivityForResult(intent,requestCode);
    }

    public void setToolbarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    protected void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastUtils.show(msg);
			}
		});
    }

	protected void showToast(final int msgId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastUtils.show(msgId);
			}
		});
	}
}
