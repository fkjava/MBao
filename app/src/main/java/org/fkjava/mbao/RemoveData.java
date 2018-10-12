package org.fkjava.mbao;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.fkjava.mbao.domain.IndexAction;
import org.fkjava.mbao.domain.IndexPage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoveData {

    // OKHttpClient里面，内置了连接池，所以只需要创建一次就可以重复使用
    private static OkHttpClient httpClient = new OkHttpClient();

    public static IndexPage getIndexPage() {

        // 创建请求
        Request request = new Request.Builder()//
                .url("http://192.168.0.99:8080/ec/commerce/indexJson.action")//从服务器获取数据
                .method("GET", null)//以GET方式获取数据，没有请求体
                .build();//构建请求对象
        // 创建Call对象，用于异步执行HTTP请求
        Call call = httpClient.newCall(request);

        // 设置回调，并把Call放入调度队列中，会自动连接网络、发送请求，结果通过回调来获取
        // 不需要调用execute方法
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("网络", "出现错误信息啦:" + e.getMessage());
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                Log.d("网络", "返回的JSON:" + json);
//            }
//        });

        // 同步发送请求，前面不能使用enqueue方法
        try {
            Response response = call.execute();
            String json = response.body().string();
            //Log.d("网络", "返回的JSON:" + json);

            IndexAction action = JSON.parseObject(json, IndexAction.class);

            return action.getPage();
        } catch (IOException e) {
            Log.d("网络", "出现错误信息啦:" + e.getMessage());
        }
        return null;
    }
}
