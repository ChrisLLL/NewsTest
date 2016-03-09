package com.example.newstest.util;

import java.util.List;

import com.example.newstest.model.News;

public interface GetDataInterface {

	public List<News> getNews(int NewsID, int Size) throws Exception;

}
