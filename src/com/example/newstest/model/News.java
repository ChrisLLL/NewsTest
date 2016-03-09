package com.example.newstest.model;

import java.io.Serializable;

public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1261511530460415439L;

	private String NewsTitle;
	private String NewsSource;
	private String NewsTime;
	private String NewsContentUrl;

	// 无参构造方法
	public News() {
		super();
	}

	// 含参构造方法
	public News(String newsTitle, String newsSource, String newsTime,
			String newsContentUrl) {
		super();
		NewsTitle = newsTitle;
		NewsSource = newsSource;
		NewsTime = newsTime;
		NewsContentUrl = newsContentUrl;
	}

	public String getNewsTitle() {
		return NewsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		NewsTitle = newsTitle;
	}

	public String getNewsSource() {
		return NewsSource;
	}

	public void setNewsSource(String newsSource) {
		NewsSource = newsSource;
	}

	public String getNewsTime() {
		return NewsTime;
	}

	public void setNewsTime(String newsTime) {
		NewsTime = newsTime;
	}

	public String getNewsContentUrl() {
		return NewsContentUrl;
	}

	public void setNewsContentUrl(String newsContentUrl) {
		NewsContentUrl = newsContentUrl;
	}
}
