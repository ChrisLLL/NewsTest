package com.example.newstest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.newstest.R;

public class ArcMenu extends ViewGroup {

	// 菜单位置常量
	private static final int POSITION_LEFT_TOP = 0;
	private static final int POSITION_LEFT_BOTTOM = 1;
	private static final int POSITION_RIGHT_TOP = 2;
	private static final int POSITION_RIGHT_BOTTOM = 3;

	// 菜单位置
	private Position mPosition = Position.RIGHT_BOTTOM;
	// 菜单展开Menu Item半径
	private int mRadius;
	// 菜单的初始状态
	private Status mCurrentStatus = Status.CLOSE;
	// 菜单主按钮
	private View mCButton;
	// 回调接口对象
	private OnMenuItemClickListener mMenuItemClickListener;

	// 菜单状态枚举
	public enum Status {
		OPEN, CLOSE
	}

	// 菜单的位置枚举类
	public enum Position {
		LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
	}

	// 点击子菜单项的回调接口
	public interface OnMenuItemClickListener {
		void onClick(View view, int pos);
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener mMenuItemClickListener) {
		this.mMenuItemClickListener = mMenuItemClickListener;
	}

	public ArcMenu(Context context) {
		this(context, null);
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// 展开Menu Item半径动态设置为 100dp
		// applyDimension 讲数值转化为 dp 单位
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());

		// 获取自定义属性的值
		TypedArray _array = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ArcMenu, defStyle, 0);
		// 获取自定义 位置 属性
		int _position = _array.getInt(R.styleable.ArcMenu_position,
				POSITION_RIGHT_BOTTOM);
		// 对自定义 位置 属性进行判断，赋值给成员变量
		switch (_position) {
		case POSITION_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POSITION_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POSITION_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
		case POSITION_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		}
		// 获取自定义 半径 属性,赋值给成员变量
		mRadius = (int) _array.getDimension(R.styleable.ArcMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));

		Log.e("TAG", "position = " + mPosition + " , radius =  " + mRadius);
		// 回收TypedArray局部变量
		_array.recycle();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			// 测量child
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		if (changed) {

			layoutCButton();

			// 获取Menu Item个数
			int _count = getChildCount();

			for (int i = 0; i < _count - 1; i++) {

				// 0 位置上的是主按钮,Menu Item要加1
				View _childView = getChildAt(i + 1);

				// 菜单初始状态为关闭，所以设置Menu Item不可见
				_childView.setVisibility(View.GONE);

				// 计算Menu Item X,Y轴上的位置
				int _childl = (int) (mRadius * Math.sin(Math.PI / 2
						/ (_count - 2) * i));
				int _childt = (int) (mRadius * Math.cos(Math.PI / 2
						/ (_count - 2) * i));

				// 获取Menu Item的宽,高
				int cWidth = _childView.getMeasuredWidth();
				int cHeight = _childView.getMeasuredHeight();

				// 根据菜单位置 改变Menu Item 的坐标位置
				// 如果菜单位置在底部 左下，右下
				if (mPosition == Position.LEFT_BOTTOM
						|| mPosition == Position.RIGHT_BOTTOM) {
					_childt = getMeasuredHeight() - cHeight - _childt;
				}
				// 右上，右下
				if (mPosition == Position.RIGHT_TOP
						|| mPosition == Position.RIGHT_BOTTOM) {
					_childl = getMeasuredWidth() - cWidth - _childl;
				}

				_childView.layout(_childl, _childt, _childl + cWidth, _childt
						+ cHeight);
			}
		}
	}

	/**
	 * 定位主菜单按钮
	 */
	private void layoutCButton() {

		// 获取主菜单
		mCButton = getChildAt(0);
		// 为主菜单设置监听
		mCButton.setOnClickListener(new myOnClickListener());

		// X轴上的位置
		int l = 0;
		// Y轴上的位置
		int t = 0;

		// 菜单主按钮的 宽,高
		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();

		// 对菜单位置进行判断，对 坐标位置 赋值
		switch (mPosition) {
		case LEFT_TOP:
			l = 0;
			t = 0;
			break;
		case LEFT_BOTTOM:
			l = 0;
			t = getMeasuredHeight() - height;
			break;
		case RIGHT_TOP:
			l = getMeasuredWidth() - width;
			t = 0;
			break;
		case RIGHT_BOTTOM:
			l = getMeasuredWidth() - width;
			t = getMeasuredHeight() - height;
			break;
		}
		mCButton.layout(l, t, l + width, t + width);
	}

	private class myOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// mCButton = findViewById(R.id.id_button);
			// if(mCButton == null)
			// {
			// mCButton = getChildAt(0);
			// }

			// 为主按钮添加旋转动画,传入的四个参数分别是 View 对象，旋转起始角度，结束角度，动画持续时间ms
			rotateCButton(v, 0f, 360f, 300);
			// 为Menu Item添加旋转和平移动画
			toggleMenu(300);

		}
	}

	/**
	 * 切换菜单
	 */
	public void toggleMenu(int duration) {

		// 为Menu Item添加平移动画和旋转动画
		int _count = getChildCount();

		for (int i = 0; i < _count - 1; i++) {

			final View _childView = getChildAt(i + 1);
			// 设置可见
			_childView.setVisibility(View.VISIBLE);

			// end 0 , 0
			// start
			int _childl = (int) (mRadius * Math.sin(Math.PI / 2 / (_count - 2)
					* i));
			int _childt = (int) (mRadius * Math.cos(Math.PI / 2 / (_count - 2)
					* i));

			// 平移动画方向标志，当菜单处在不同位置时，Menu Item的平移方向也要相应改变
			// 当菜单 打开，关闭时，Menu Item的平移方向也要改变
			int _xflag = 1;
			int _yflag = 1;

			// 对菜单位置进行判断，并更改Menu Item平移方向
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.LEFT_BOTTOM) {
				_xflag = -1;
			}
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.RIGHT_TOP) {
				_yflag = -1;
			}

			AnimationSet _animset = new AnimationSet(true);
			Animation _tranAnim = null;

			// 平移动画
			// to open
			if (mCurrentStatus == Status.CLOSE) {
				_tranAnim = new TranslateAnimation(_xflag * _childl, 0, _yflag
						* _childt, 0);
				// 菜单打开时，设置Menu Item焦点和点击。
				_childView.setClickable(true);
				_childView.setFocusable(true);

			} else
			// to close
			{
				_tranAnim = new TranslateAnimation(0, _xflag * _childl, 0,
						_yflag * _childt);
				// 菜单关闭时，设置Menu Item无焦点和不可点击。
				_childView.setClickable(false);
				_childView.setFocusable(false);
			}

			_tranAnim.setFillAfter(true);
			_tranAnim.setDuration(duration);
			// 为不同Menu Item设置不同的动画启动时间，产生一个个展开的效果
			_tranAnim.setStartOffset((i * 100) / _count);

			_tranAnim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {

					// 根据菜单当前状态，设置Menu Item是否可见
					if (mCurrentStatus == Status.CLOSE) {
						_childView.setVisibility(View.GONE);
					}
				}
			});
			// 旋转动画
			RotateAnimation rotateAnim = new RotateAnimation(0, 720,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnim.setDuration(duration);
			rotateAnim.setFillAfter(true);

			// 添加平移，旋转动画，启动
			_animset.addAnimation(rotateAnim);
			_animset.addAnimation(_tranAnim);
			_childView.startAnimation(_animset);

			final int _position = i + 1;
			// 为Menu Item设置点击监听
			_childView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mMenuItemClickListener != null)
						mMenuItemClickListener.onClick(_childView, _position);

					// 为Menu Item添加缩放动画，点击的Menu Item变大，其他的变小
					menuItemAnim(_position - 1);
					changeStatus();

				}
			});
		}
		// 切换菜单状态
		// 要在Menu Item的循环遍历之外
		changeStatus();
	}

	/**
	 * 添加menuItem的点击动画
	 * 
	 * @param i
	 */
	private void menuItemAnim(int pos) {
		for (int i = 0; i < getChildCount() - 1; i++) {

			View childView = getChildAt(i + 1);
			if (i == pos) {
				childView.startAnimation(scaleBigAnim(300));
			} else {

				childView.startAnimation(scaleSmallAnim(300));
			}

			childView.setClickable(false);
			childView.setFocusable(false);

		}

	}

	private Animation scaleSmallAnim(int duration) {

		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);
		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);
		return animationSet;

	}

	/**
	 * 为当前点击的Item设置变大和透明度降低的动画
	 * 
	 * @param duration
	 * @return
	 */
	private Animation scaleBigAnim(int duration) {
		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);

		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);
		return animationSet;

	}

	/**
	 * 切换菜单状态
	 */
	private void changeStatus() {
		mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN
				: Status.CLOSE);
	}

	// 对外开放的,菜单状态判断
	public boolean isOpen() {
		return mCurrentStatus == Status.OPEN;
	}

	private void rotateCButton(View v, float start, float end, int duration) {

		RotateAnimation anim = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}

}