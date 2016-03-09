package com.example.newstest.view;

import com.example.newstest.Config;
import com.example.newstest.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyListView extends ListView {

	private static LayoutInflater mInflater;
	// 底部显示正在加载的页面
	private View footerView = null;
	private TextView noData;
	private TextView loadFull;
	private TextView more;
	private ProgressBar loading;
	// 存储上下文
	private Context mContext;
	// 记录第一行Item的数值
	private int mFirstVisibleItem;
	// 记录滑动状态
	private int mScrollState;
	// 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview
	private boolean isRecorded;
	private boolean isLoading;// 判断是否正在加载
	private boolean loadEnable = true;// 开启或者关闭加载更多功能
	private boolean isLoadFull;
	private int pageSize = Config.NEWS_COUNT;
	// 上拉刷新ListView的回调监听
	private MyListViewCallBack mListViewCallBack = null;

	/**
	 * 
	 * @ClassName: MyListViewCallBack
	 * @Description: 上拉刷新的回调接口
	 * @author chrisking 2016-3-6
	 */
	public interface MyListViewCallBack {
		void onLoad();
	}

	public void setMyListViewCallBack(MyListViewCallBack listViewCallBack) {
		this.loadEnable = true;
		this.mListViewCallBack = listViewCallBack;
	}

	/**
	 * 
	 * @Title: isLoadEnable
	 * @Description: 判断是否可以加载更多
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isLoadEnable() {
		return loadEnable;
	}

	/**
	 * 
	 * @Title: setLoadEnable
	 * @Description: 开启或者关闭加载更多，并不支持动态调整
	 * @param loadEnable
	 * @return: void
	 * @throws:
	 */
	public void setLoadEnable(boolean loadEnable) {
		this.loadEnable = loadEnable;
		this.removeFooterView(footerView);
	}

	/**
	 * 
	 * @Title: getPageSize
	 * @Description: 获取加载新闻条数
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 
	 * @Title: setPageSize
	 * @Description: 设置加载新闻条数
	 * @param pageSize
	 * @return: void
	 * @throws:
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public MyListView(Context context) {
		super(context);
		initView(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 
	 * @Title: initListView
	 * @Description: 初始化ListView
	 * @return: void
	 * @throws:
	 */
	private void initView(Context context) {

		if (footerView == null) {
			mInflater = LayoutInflater.from(context);
			// 第二个参数如果设置成this会报错
			footerView = mInflater.inflate(R.layout.layout_list_footerview,
					null);
			loadFull = (TextView) footerView.findViewById(R.id.loadFull);
			noData = (TextView) footerView.findViewById(R.id.noData);
			more = (TextView) footerView.findViewById(R.id.more);
			loading = (ProgressBar) footerView.findViewById(R.id.loading);
		}
		this.addFooterView(footerView);
		// 设置滑动监听
		setOnScrollListener(new listener());
		// 去掉底部分割线
		// setFooterDividersEnabled(false);
	}

	public void onLoad() {
		if (mListViewCallBack != null) {
			mListViewCallBack.onLoad();
		}
	}

	/**
	 * 
	 * @Title: onLoadComplete
	 * @Description: 用于加载更多结束后的回调
	 * @return: void
	 * @throws:
	 */
	public void onLoadComplete() {
		isLoading = false;
	}

	/**
	 * 
	 * @ClassName: listener
	 * @Description: 滑动监听器
	 * @author chrisking 2016-3-6
	 */
	private class listener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mFirstVisibleItem = firstVisibleItem;

			if (footerView != null) {
				// 判断可视Item是否能在当前页面完全显示
				if (visibleItemCount == totalItemCount) {
					// removeFooterView(footerView);
					footerView.setVisibility(View.GONE);// 隐藏底部布局
				} else {
					// addFooterView(footerView);
					footerView.setVisibility(View.VISIBLE);// 显示底部布局
				}
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			mScrollState = scrollState;
			ifNeedLoad(view, scrollState);
		}
	}

	/**
	 * 
	 * @Title: ifNeedLoad
	 * @Description: 根据listview滑动的状态判断是否需要加载更多
	 * @param view
	 * @param scrollState
	 * @return: void
	 * @throws:
	 */
	private void ifNeedLoad(AbsListView view, int scrollState) {
		if (!loadEnable) {
			return;
		}
		try {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& !isLoading
					&& view.getLastVisiblePosition() == view
							.getPositionForView(footerView) && !isLoadFull) {
				onLoad();
				isLoading = true;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @Title: setResultSize
	 * @Description: 这个方法是根据结果的大小来决定footer显示的。
	 *               <p>
	 *               这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，
	 *               这时footer显示已经全部加载
	 *               </p>
	 * @param resultSize
	 * @return: void
	 * @throws:
	 */
	public void setResultSize(int resultSize) {
		if (resultSize == 0) {
			isLoadFull = true;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		} else if (resultSize > 0 && resultSize < pageSize) {
			isLoadFull = true;
			loadFull.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
		} else if (resultSize == pageSize) {
			isLoadFull = false;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			more.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
		}

	}

}
