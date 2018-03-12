package com.mapuni.car.api;

import com.mapuni.car.mvp.main.model.GankItemBean;
import com.mapuni.car.mvp.main.model.MainModel;
import com.mapuni.core.net.response.AppHttpResponse;


import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ZhihuDailyApi {

    //获取最近的日报
    @GET("news/latest")
    Flowable<MainModel> getLatestZhihuDaily();

    //获取某一时间之前的日报（本例用于加载更多）
    @GET("news/before/{date}")
    Flowable<MainModel> getZhihuDaily(@Path("date") String date);

    @GET("news/{id}")
    Flowable<MainModel> getStoryContent(@Path("id") String id);

    @GET("random/data/福利/{num}")
    Flowable<AppHttpResponse<List<GankItemBean>>> getImageBean(@Path("num") int num);
}
