package name.teze.layout.lib;

import name.teze.layout.lib.SlidingPaneLayout.PanelSlideListener;
import com.happy.ui.R;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 功能： SwipeBackActivity
 * 
 * @author by Fooyou 2014年5月7日 上午11:19:54
 */
public class SwipeBackActivity extends FragmentActivity implements
		PanelSlideListener {

	private static final String TAG = "SwipeBackActivity";
	private boolean swipeEnable = true;
	private Drawable mShadowDrawable;
	private View localViewTemp;
	private int mShadowResource = R.drawable.translucent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* if (BuildConfig.DEBUG)Log.i(TAG, "onCreate"); */
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		/*
		 * if (BuildConfig.DEBUG)Log.i(TAG,
		 * "onPostCreate >> swipeEnable >> "+swipeEnable);
		 */
		if (swipeEnable) {
			ViewGroup localViewGroup = (ViewGroup) getWindow().getDecorView();
			View localView = localViewGroup.getChildAt(0);
			localViewGroup.removeView(localView);
			SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(this);
			slidingPaneLayout.setPanelSlideListener(this);
			slidingPaneLayout.setSliderFadeColor(getResources().getColor(
					android.R.color.transparent));
			// slidingPaneLayout.setShadowResource(mShadowResource);
			// if (mShadowDrawable != null) {
			// slidingPaneLayout.setShadowDrawable(mShadowDrawable);
			// }
			slidingPaneLayout.setCoveredFadeColor(getResources().getColor(
					android.R.color.transparent));
			/* slidingPaneLayout.setBackgroundColor(Color.alpha(0)); */

			localViewTemp = new TextView(this);
			/*
			 * localViewTemp.setTextColor(getResources().getColor(R.color.beta));
			 * localViewTemp.setBackgroundResource(android.R.color.black);
			 */
			localViewTemp.setBackgroundResource(android.R.color.black);

			ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(
					-1, -1);
			slidingPaneLayout.addView(localViewTemp, localLayoutParams);
			slidingPaneLayout.addView(localView);
			localViewGroup.addView(slidingPaneLayout);

			getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);
		}
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		ViewHelper.setAlpha(localViewTemp, (1 - slideOffset));
	}

	@Override
	public void onPanelOpened(View panel) {
		finish();
	}

	@Override
	public void onPanelClosed(View panel) {
	}

	public boolean isSwipeEnable() {
		return swipeEnable;
	}

	public void setSwipeEnable(boolean swipeEnable) {
		/*
		 * if (BuildConfig.DEBUG)Log.i(TAG,
		 * "setSwipeEnable >> swipeEnable >> "+swipeEnable);
		 */
		this.swipeEnable = swipeEnable;
	}

	public void setShadowDrawable(Drawable d) {
		mShadowDrawable = d;
	}

	public void setShadowResource(int resId) {
		mShadowResource = resId;
	}

	/**
	 * 返回键调成此方法
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.activity_ani_exist);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_ani_exist);
	}
}
