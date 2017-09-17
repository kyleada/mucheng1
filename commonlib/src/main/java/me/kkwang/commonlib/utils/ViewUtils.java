package me.kkwang.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kw on 2016/3/1.
 */
public class ViewUtils {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int msgID) {
        Toast.makeText(context, msgID, Toast.LENGTH_SHORT).show();
    }

    public static void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void goneView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void fadeOut(final View view, int duration) {
        if (view.getVisibility() != View.VISIBLE || view.getAlpha() == 0) {
            return;
        }
//        view.animate()
//                .alpha(0)
//                .setDuration(duration)
//                .setInterpolator(new FastOutLinearInInterpolator())
//                .setListener(new AnimatorListenerAdapter() {
//                    private boolean mCanceled = false;
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        mCanceled = true;
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        if (!mCanceled) {
//                            view.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                })
//                .start();
    }

    public static void fadeOut(final View view) {
        fadeOut(view, getShortAnimTime(view));
    }

    public static void fadeIn(View view, int duration) {
        if (view.getVisibility() == View.VISIBLE && view.getAlpha() == 1) {
            return;
        }
//        view.setAlpha(0);
//        view.setVisibility(View.VISIBLE);
//        view.animate()
//                .alpha(1)
//                .setDuration(duration)
//                .setInterpolator(new FastOutSlowInInterpolator())
//                        // NOTE: We need to remove any previously set listener or Android will reuse it.
//                .setListener(null)
//                .start();
    }

    public static void fadeIn(View view) {
        fadeIn(view, getShortAnimTime(view));
    }

    public static void crossfade(View fromView, View toView, int duration) {
        fadeOut(fromView, duration);
        fadeIn(toView, duration);
    }

    public static void crossfade(View fromView, View toView) {
        crossfade(fromView, toView, getShortAnimTime(fromView));
    }

    public static float dpToPx(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPxInt(float dp, Context context) {
        return Math.round(dpToPx(dp, context));
    }

    public static int getColorFromAttrRes(int attrRes, int defValue, Context context) {
        int[] attrs = new int[]{attrRes};
        TypedArray a = context.obtainStyledAttributes(attrs);
        int color = a.getColor(0, defValue);
        a.recycle();
        return color;
    }

    public static ColorStateList getColorStateListFromAttrRes(int attrRes, Context context) {
        int[] attrs = new int[]{attrRes};
        TypedArray a = context.obtainStyledAttributes(attrs);
        ColorStateList colorStateList = a.getColorStateList(0);
        a.recycle();
        return colorStateList;
    }

    public static Drawable getDrawableFromAttrRes(int attrRes, Context context) {
        int[] attrs = new int[]{attrRes};
        TypedArray a = context.obtainStyledAttributes(attrs);
        Drawable drawable = a.getDrawable(0);
        a.recycle();
        return drawable;
    }

    public static int getShortAnimTime(Resources resources) {
        return resources.getInteger(android.R.integer.config_shortAnimTime);
    }

    public static int getShortAnimTime(View view) {
        return getShortAnimTime(view.getResources());
    }

    public static int getShortAnimTime(Context context) {
        return getShortAnimTime(context.getResources());
    }

    public static int getMediumAnimTime(Resources resources) {
        return resources.getInteger(android.R.integer.config_mediumAnimTime);
    }

    public static int getMediumAnimTime(View view) {
        return getMediumAnimTime(view.getResources());
    }

    public static int getMediumAnimTime(Context context) {
        return getMediumAnimTime(context.getResources());
    }

    public static int getLongAnimTime(Resources resources) {
        return resources.getInteger(android.R.integer.config_longAnimTime);
    }

    public static int getLongAnimTime(View view) {
        return getLongAnimTime(view.getResources());
    }

    public static int getLongAnimTime(Context context) {
        return getLongAnimTime(context.getResources());
    }

    private static float getScreenSmallestWidthDp(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int smallestWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
        return pxToDp(smallestWidth, context);
    }

    public static boolean hasSw600dp(Context context) {
        return getScreenSmallestWidthDp(context) > 600;
    }

    public static View inflate(int resource, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    }

    public static boolean isInLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void postOnPreDraw(final View view, final Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                runnable.run();
                return true;
            }
        });
    }

    public static float pxToDp(float px, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / metrics.density;
    }

    public static int pxToDpInt(float px, Context context) {
        return Math.round(pxToDp(px, context));
    }

    public static void replaceChild(ViewGroup viewGroup, View oldChild, View newChild) {
        int index = viewGroup.indexOfChild(oldChild);
        viewGroup.removeViewAt(index);
        viewGroup.addView(newChild, index);
    }

    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height == height) {
            return;
        }
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setSize(View view, int size) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.width == size && layoutParams.height == size) {
            return;
        }
        layoutParams.width = size;
        layoutParams.height = size;
        view.setLayoutParams(layoutParams);
    }

    public static void setTextViewBold(TextView textView, boolean bold) {

        Typeface typeface = textView.getTypeface();
        if (typeface.isBold() == bold) {
            return;
        }

        int style = textView.getTypeface().getStyle();
        if (bold) {
            style |= Typeface.BOLD;
        } else {
            style &= ~Typeface.BOLD;
        }
        // Workaround insane behavior in TextView#setTypeface(Typeface, int).
        if (style > 0) {
            textView.setTypeface(typeface, style);
        } else {
            textView.setTypeface(Typeface.create(typeface, style), style);
        }
    }

    public static void setTextViewItalic(TextView textView, boolean italic) {

        Typeface typeface = textView.getTypeface();
        if (typeface.isItalic() == italic) {
            return;
        }

        int style = textView.getTypeface().getStyle();
        if (italic) {
            style |= Typeface.ITALIC;
        } else {
            style &= ~Typeface.ITALIC;
        }
        // Workaround insane behavior in TextView#setTypeface(Typeface, int).
        if (style > 0) {
            textView.setTypeface(typeface, style);
        } else {
            textView.setTypeface(Typeface.create(typeface, style), style);
        }
    }

    public static void setTextViewLinkClickable(TextView textView) {
        textView.setMovementMethod(ClickableMovementMethod.getInstance());
        // Reset for TextView.fixFocusableAndClickableSettings(). We don't want View.onTouchEvent()
        // to consume touch events.
        textView.setFocusable(false);
        textView.setClickable(false);
        textView.setLongClickable(false);
    }

    public static void setVisibleOrGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setVisibleOrInvisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public static void setWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.width == width) {
            return;
        }
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    static int displayWidth;
    public static int getDisplayWith(Context context){
		if(context == null){
			return 0;
		}
		if(displayWidth == 0){
			DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
			displayWidth = dm.widthPixels;
		}
		return displayWidth;
	}

    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int length) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int min = Math.min(length, listAdapter.getCount());
        for (int i = 0; i < min; i++) {
            View listItem = listAdapter.getView(i, null, listView);

            listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.UNSPECIFIED));
            listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //用于密码输入框显示和隐藏输入文字
    public static void toggleEditTextShow(EditText editText, boolean show) {
        if (show) {
            //设置EditText文本为可见的
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        editText.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = editText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    public static int getStatusBarHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public int getStatusBarHeight2(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hideTitleBar(Activity activity, int resource, int steepToolBarColor) {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            LinearLayout whole_layout = (LinearLayout) activity.findViewById(resource);
            whole_layout.setBackgroundColor(steepToolBarColor);
            whole_layout.setPadding(0, activity.getResources().getDimensionPixelSize(getStatusBarHeight(activity)), 0, 0);
        }
    }

    /**
     * exist SDCard
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getImageName() {
        String PATTERN = "yyyyMMddHHmmss";
        return new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date()) + ".jpg";
    }

}
