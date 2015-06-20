package com.stx.fleshfruit.scroll;

import com.stx.fleshfruit.R;
import com.stx.fleshfruit.scroll.ImageScrollView.ScrollToScreenCallback;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class PageControlView extends LinearLayout implements ScrollToScreenCallback{
	/** Context对象 **/
	private Context context;
	/** 圆圈的数量 **/
	private int count;
	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	/**
	 * 回调函数
	 */
	@Override
	public void callback(int currentIndex) {
		generatePageControl(currentIndex);
	}
	/** 设置选中圆圈 **/
	public void generatePageControl(int currentIndex) {
		this.removeAllViews();

		for (int i = 0; i < this.count; i++) {
			ImageView iv = new ImageView(context);
			if (currentIndex == i) {
				iv.setImageResource(R.drawable.shape_point_select);
			} else {
				iv.setImageResource(R.drawable.shape_point_normal);
			}
			iv.setLayoutParams(new LayoutParams(1, 0));
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 8;
			layoutParams.rightMargin = 8;
			iv.setLayoutParams(layoutParams);
			this.addView(iv);
		}
	}

	/** 设置圆圈数量 **/
	public void setCount(int count) {
		this.count = count;
	}

}
