package com.example.newstest.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.newstest.Config;
import com.example.newstest.R;
import com.example.newstest.adapter.NewsAdapter;
import com.example.newstest.model.News;
import com.example.newstest.util.GetDataInterface;
import com.example.newstest.util.GetNews;
import com.example.newstest.util.ServiceRulesException;
import com.example.newstest.view.ArcMenu;
import com.example.newstest.view.ArcMenu.OnMenuItemClickListener;
import com.example.newstest.view.MyListView;
import com.example.newstest.view.MyListView.MyListViewCallBack;

public class MainActivity extends Activity {

	private ArcMenu myArcMenu;
	private MyListView myListView;
	private List<News> news;
	private NewsAdapter newsAdapter;
	// 新闻条目数
	private int mNewsID = 0;
	// 新闻总数
	private int mSize = Config.NEWS_COUNT;
	private GetDataInterface httpUtil = new GetNews();

	private static ProgressDialog dialog;
	private static final int INIT_NEWS = 1;
	private static final int LOAD_MORE_NEWS = 2;
	private static final String THREAD_ERROR = "多半是程序有问题";
	private static final String PROGRAM_ERROR = "多半是没网";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 寻找控件ID
		init();
		// 数据加载提示框
		dialogShow();
		// 开启子线程初次加载新闻
		loadNews(INIT_NEWS);
	}

	/**
	 * 
	 * @Title: loadNews
	 * @Description: 加载新闻
	 * @return: void
	 * @throws:
	 */
	private void loadNews(final int flag) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					news = httpUtil.getNews(mNewsID, mSize);
					handle.sendEmptyMessage(flag);
				} catch (ServiceRulesException e) {
					e.printStackTrace();
					sendMsg("ErrorMsg", e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					sendMsg("ErrorMsg", THREAD_ERROR);
				}
			}
		}).start();
	}

	/**
	 * 
	 * @Title: sendMsg
	 * @Description: 新闻加载后，传递Message给Handle。
	 * @param tag
	 * @param info
	 * @return: void
	 * @throws:
	 */
	private void sendMsg(String tag, String info) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putSerializable(tag, info);
		msg.setData(data);
		handle.sendMessage(msg);
	}

	/**
	 * 
	 * @Title: init
	 * @Description: 匹配ListView控件ID，实例化新闻List对象。
	 * @return: void
	 * @throws:
	 */
	private void init() {
		this.myListView = (MyListView) this.findViewById(R.id.myListView);
		this.myArcMenu = (ArcMenu) this.findViewById(R.id.myArcMenu);
		// 构建本地数据源
		this.news = new ArrayList<News>();
		// this.news.add(new News("今日一条", "凤凰网", 2014));
		// this.news.add(new News("今日二条", "腾讯网", 2015));
		// this.news.add(new News("今日三条", "网易网", 2016));
	}

	/**
	 * 
	 * @Title: dialogShow
	 * @Description: 弹出等待提示框
	 * @return: void
	 * @throws:
	 */
	private void dialogShow() {
		dialog = new ProgressDialog(MainActivity.this);
		dialog.setTitle("请稍候");
		dialog.setMessage("正在获取网络数据");
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 
	 * @Title: showTip
	 * @Description: 弹出Toast消息
	 * @param str
	 * @return: void
	 * @throws:
	 */
	public void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @Title: loadDataListView
	 * @Description: 为ListView绑定适配器，并设置点击事件
	 * @return: void
	 * @throws:
	 */
	public void loadListView() {
		this.newsAdapter = new NewsAdapter(this, R.layout.list_item_news,
				this.news);
		this.myListView.setAdapter(newsAdapter);
		initEvent();
	}

	/**
	 * 
	 * @Title: initEvent
	 * @Description: 初始化监听事件
	 * @return: void
	 * @throws:
	 */
	private void initEvent() {
		this.myListView.setMyListViewCallBack(new IListViewCallBack());
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// showTip(news.get(position).getNewsTitle());
				Intent _intent = new Intent(MainActivity.this,
						BrowserNewsActivity.class);
				_intent.putExtra("newsContentUrl", news.get(position)
						.getNewsContentUrl());
				startActivity(_intent);
			}

		});
		this.myListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (myArcMenu.isOpen()) {
					myArcMenu.toggleMenu(600);
				}

			}
		});

	}

	/**
	 * 
	 * @ClassName: MyHandle
	 * @Description: 自定义Handle，处理新闻加载后传递的Message。
	 * @author chrisking 2016-2-11
	 */
	private static class MyHandle extends Handler {

		// 弱引用常量
		private final WeakReference<Activity> mActivity;

		// 构造方法中传入Activity对象
		public MyHandle(MainActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		// 根据传递的信息执行对应操作
		@Override
		public void handleMessage(Message msg) {

			if (dialog != null) {
				dialog.dismiss();
			}
			int flag = msg.what;
			switch (flag) {
			case 0:
				((MainActivity) mActivity.get()).showTip(PROGRAM_ERROR);
				break;
			case INIT_NEWS:
				((MainActivity) mActivity.get()).loadListView();
				break;
			case LOAD_MORE_NEWS:
				((MainActivity) mActivity.get()).myListView.onLoadComplete();
				((MainActivity) mActivity.get()).myListView
						.setResultSize(((MainActivity) mActivity.get()).news
								.size());
				((MainActivity) mActivity.get()).newsAdapter
						.notifyDataSetChanged();
				break;
			}
		}
	}

	// 实例化一个自定义Handle对象
	private MyHandle handle = new MyHandle(this);

	/**
	 * 
	 * @ClassName: IListViewCallBack
	 * @Description: 实现ListView的加载更多的回调接口
	 * @author chrisking 2016-3-6
	 */
	private class IListViewCallBack implements MyListViewCallBack {

		@Override
		public void onLoad() {
			// 弹出等待提示框
			dialog.show();
			// 改变获取新闻ID
			mNewsID = mNewsID + Config.NEWS_COUNT;
			// 改变获取新闻总数
			mSize = mSize + Config.NEWS_COUNT;
			// 加载更多新闻
			loadNews(LOAD_MORE_NEWS);
		}

	}
}