package org.fkjava.mbao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.fkjava.mbao.domain.IndexAction;
import org.fkjava.mbao.domain.IndexPage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteData {

    // OKHttpClient里面，内置了连接池，所以只需要创建一次就可以重复使用
    private static OkHttpClient httpClient = new OkHttpClient();
    //private static final String BASE_URL = "http://192.168.0.99:8080/ec";
    private static final String BASE_URL = "http://192.168.10.222:8080/ec";

    public static IndexPage getIndexPage(int number) throws IOException {

        // 创建请求
        Request request = new Request.Builder()//
                .url(BASE_URL + "/commerce/indexJson.action?number=" + number)//从服务器获取数据
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
        Response response = call.execute();
        String json = response.body().string();
        //Log.d("网络", "返回的JSON:" + json);

        IndexAction action = JSON.parseObject(json, IndexAction.class);

        return action.getPage();
    }

    /**
     * 根据图片名称，获取远程服务器的图片。同步获取图片，不能在主线程里面使用。Android禁止在主线程访问网络。
     *
     * @param name
     * @return
     */
    public static Bitmap getImage(String name) {
        Request request = new Request.Builder()
                .method("GET", null)
                .url(BASE_URL + "/static/fkjava/images" + name)
                .build();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            try (InputStream in = response.body().byteStream();) {
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            }
        } catch (IOException e) {
            Log.d("网络", "出现错误信息啦:" + e.getMessage());
        }
        return null;
    }

    /**
     * 异步读取远程图片，适合在主线程里面直接使用
     *
     * @param imageView
     * @param name
     */
    public static void setImage(final ImageView imageView, String name) {
        Request request = new Request.Builder()
                .method("GET", null)
                .url(BASE_URL + "/static/fkjava/images" + name)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("网络", "出现错误信息啦:" + e.getMessage());
                e.printStackTrace();
            }

            // 这是通过OkHttp异步方式获取图片的回调。异步方式本身就是在另外一个线程里面执行的，不在UI里面。
            // 所以这里也属于是在子线程执行的任务
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final Bitmap bitmap;
                // 把获取到的图片，转换为Bitmap对象
                try (InputStream in = response.body().byteStream()) {
                    bitmap = BitmapFactory.decodeStream(in);
                }
                // 把Bitmap设置给对象，意味着就是更新UI，所以要post到视图里面去，让视图去更新
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
