package com.example.newstest.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.example.newstest.Config;
import com.example.newstest.model.News;

public class GetNews implements GetDataInterface {

	private List<News> news = new ArrayList<News>();

	private static final String NETWORK_REQUEST_ERROR = "网络访问出错";
	private static final String URL = "http://api.1-blog.com/biz/bizserver/news/list.do";

	/**
	 * 通过url获取新闻信息
	 */
	@Override
	public List<News> getNews(int NewsID, int Size) throws Exception {

		// 实例化一个默认的HttpClient对象
		HttpClient client = new DefaultHttpClient();
		// 实例化一个HttpGet对象
		HttpGet get = new HttpGet(URL + "?size=" + Size);
		// 执行Get请求，获取到的内容放在response中。
		HttpResponse response = client.execute(get);
		// 获取状态码
		int statusCode = response.getStatusLine().getStatusCode();
		// 状态码不OK抛出自定义异常
		if (statusCode != HttpStatus.SC_OK) {
			throw new ServiceRulesException(NETWORK_REQUEST_ERROR + statusCode);
		}
		// 获取到的Json数据
		String _results = EntityUtils
				.toString(response.getEntity(), HTTP.UTF_8);

		for (int i = NewsID; i < NewsID + Config.NEWS_COUNT; i++) {

			// 获取到的JSON数据对象
			JSONObject _object = new JSONObject(_results);

			// 此接口接收的JSON数据中有个状态值status，六个0表示获取成功。
			if ((_object.getString("status")).equals("000000")) {
				// 新闻的标题，来源等，封装在键为detail的JSONArray中
				JSONArray _details = _object.getJSONArray("detail");
				// 获取第i条新闻的内容
				JSONObject _newsDetails = _details.getJSONObject(i);
				// 提取JSON数据对象中，新闻内容的标题，来源和发布时间。
				String _newsTitle = _newsDetails.getString("title");
				String _newsSource = _newsDetails.getString("source");
				String _newsContentUrl = _newsDetails.getString("article_url");
				Long _newsTime = _newsDetails.getLong("behot_time");
				// 添加到List中。
				news.add(new News(_newsTitle, _newsSource,
						longToDate(_newsTime), _newsContentUrl));
			} else {
				// 连接失败打印信息。
				Log.d("Info", _object.getString("desc"));
			}
		}
		return news;
	}

	/**
	 * 
	 * @Title: longToDate
	 * @Description: Long类型毫秒数转日期
	 * @param lo
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String longToDate(long lo) {
		Date date = new Date(lo);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(date);
	}
}
