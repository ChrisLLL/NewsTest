package com.example.newstest.view;

import com.example.newstest.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyTitleBar extends RelativeLayout {

	private MyTitleBarCallBack mTitleBarCallBack = null;
	Button btnPersonal;
	TextView titlebarTitle;
	LayoutInflater mInflater;

	/**
	 * 
	 * @ClassName: MyTitleBarCallBack
	 * @Description: 回调接口
	 * @author chrisking 2016-3-6
	 */
	public interface MyTitleBarCallBack {
		public void click(int id);
	}

	public void setMyTitleBarCallBack(MyTitleBarCallBack titleBarCallBack) {
		this.mTitleBarCallBack = titleBarCallBack;
	}

	public MyTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * 
	 * @Title: initView
	 * @Description: 初始化控件
	 * @return: void
	 * @throws:
	 */
	private void initView() {
		mInflater = LayoutInflater.from(getContext());
		View view = mInflater.inflate(R.layout.layout_titlebar, this);
		findView(view);
		setOnClick();
	}

	/**
	 * 
	 * @Title: setOnClick
	 * @Description: 为TitleBar设置点击事件
	 * @return: void
	 * @throws:
	 */
	private void setOnClick() {
		btnPersonal.setOnClickListener(new listener());
	}

	/**
	 * 
	 * @ClassName: listener
	 * @Description: 点击事件监听器
	 * @author chrisking 2016-3-6
	 */
	private class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_personal:
				initPic();
				Toast.makeText(getContext(), "你点击了按钮", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

	}

	/**
	 * 
	 * @Title: initPic
	 * @Description: 点击按钮后更改背景图片
	 * @return: void
	 * @throws:
	 */
	private void initPic() {
		btnPersonal.findViewById(R.id.btn_personal).setBackgroundResource(
				R.drawable.btn_personal_down);
	}

	/**
	 * 
	 * @Title: findView
	 * @Description: 匹配控件ID
	 * @param view
	 * @return: void
	 * @throws:
	 */
	private void findView(View view) {
		btnPersonal = (Button) view.findViewById(R.id.btn_personal);
		titlebarTitle = (TextView) view.findViewById(R.id.titlebar_title);
	}
}
