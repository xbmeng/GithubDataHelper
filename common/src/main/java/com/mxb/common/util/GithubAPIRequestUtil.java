package com.mxb.common.util;

import com.mxb.common.Interceptor.MyOkHttpRetryInterceptor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class GithubAPIRequestUtil {

//    public static final String ACCESS_TOKEN = "06c78999cb0b07e20afcbc6cd1f1d5fcdb4e05";

    /**
     *
     * @param url
     * @return
     */
    public static String getDataFromGithubAPI(String url) {
        String result = "";
        MyOkHttpRetryInterceptor myOkHttpRetryInterceptor =
                new MyOkHttpRetryInterceptor.Builder()
                        .executionCount(3)
                        .retryInterval(2000)
                        .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(myOkHttpRetryInterceptor)
                .readTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                //.header("Accept","application/vnd.github.v3+json")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("Authorization", " token " + AccessTookenUtil.getAccessTookenUtil())
                .build();
        Call call = okHttpClient.newCall(request);

        Response response = null;
        try {
            response = call.execute();
            result = (response.body().string());
        } catch (Exception e) {
            System.out.println("--------捕获异常-------");
            e.printStackTrace();
            result = getDataFromGithubAPI(url);
        }finally {
            response.body().close();
        }
        System.out.println("请求成功: " + url);

        return result;
    }

    public static String checkChanged(String url,String time)
    {
        return "";
    }


}
