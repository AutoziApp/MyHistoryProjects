package com.mapuni.core.rxjava;


import android.text.TextUtils;

import com.mapuni.core.net.exception.ApiException;
import com.mapuni.core.net.response.CarHttpResponse;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by codeest on 2016/8/3.
 */
public class RxUtil {

    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<MyHttpResponse<T>, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<MyHttpResponse<T>,T>() {
            @Override
            public Flowable<T> apply(Flowable<MyHttpResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<MyHttpResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(MyHttpResponse<T> tGankHttpResponse) {
                        if(tGankHttpResponse.getIsExist().equals("1")) {
                            return createData(tGankHttpResponse.getResult());
                        } else {
                            return Flowable.error(new ApiException(tGankHttpResponse.getIsExist()));
                        }
                    }
                });
            }
        };
    }
    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<CarHttpResponse<T>, T> handleCarResult() {   //compose判断结果
        return new FlowableTransformer<CarHttpResponse<T>,T>() {
            @Override
            public Flowable<T> apply(Flowable<CarHttpResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<CarHttpResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(CarHttpResponse<T> tGankHttpResponse) {
                        if(tGankHttpResponse.getIsExist().equals("1")) {
                            return createData(tGankHttpResponse.getResult());
                        } else {
                            return Flowable.error(new ApiException(tGankHttpResponse.getIsExist()));
                        }
                    }
                });
            }
        };
    }
    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<RequestHttpResponse<T>, T> handleRequestResult() {   //compose判断结果
        return new FlowableTransformer<RequestHttpResponse<T>,T>() {
            @Override
            public Flowable<T> apply(Flowable<RequestHttpResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<RequestHttpResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(RequestHttpResponse<T> tGankHttpResponse) {
                        if(tGankHttpResponse.getIsExist().equals("1")) {
                            return createData(tGankHttpResponse.getResult());
                        } else {
                            return Flowable.error(new ApiException(tGankHttpResponse.getErrInfo(),
                                    Integer.parseInt(tGankHttpResponse.getIsExist())));
                        }
                    }
                });
            }
        };
    }


    /**
     * 生成Flowable
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
