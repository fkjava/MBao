package org.fkjava.mbao;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.fkjava.mbao.domain.Article;
import org.fkjava.mbao.domain.IndexPage;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 使用匿名内部类的方式，创建一个Handler实例，并重写handleMessage方法
    // 其他线程通过此Handler对象发送Message过来，就可以实现在UI线程更新视图了
    private Handler promptHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("错误提示")
                    .setMessage("无法获取数据，请检查网络！")
                    .show();
        }
    };

    private IndexPage page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化商品列表的显示
        initArticles();
    }

    public void clickUserButton(View view) {
        // 如果已经登录，则跳转到用户的个人信息页面，显示订单、优惠券、修改用户信息。
        // 没有登录的时候应该跳转到登录页面
        Intent loginIntent = new Intent(MainActivity.this
                , LoginActivity.class);
        super.startActivity(loginIntent);
    }


    public void previousPage(View view) {
        if (page.getNumber() > 0) {
            this.getPage(page.getNumber() - 1);
        }
    }

    public void nextPage(View view) {
        if (page.getNumber() < page.getTotalPages() - 1) {
            this.getPage(page.getNumber() + 1);
        }
    }


    private void getPage(final int number) {

        // 启动一个线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // 远程获取数据
                    page = RemoteData.getIndexPage(number);
                    processData(page);
                } catch (Throwable e) {
                    promptHandler.sendEmptyMessage(500);

                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    // 通过一个方法来初始化所有的商品
    @SuppressLint("ResourceType")
    private void initArticles() {
        // 启动一个线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // 远程获取数据
                    page = RemoteData.getIndexPage(0);
                    processData(page);
                } catch (Throwable e) {

                    // 子线程禁止更新UI，需要把UI更新操作提交给UI线程来执行
                    // 可以通过Handler、view.post、runOnUiThread等方式提交给UI线程
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            new AlertDialog.Builder(MainActivity.this)
//                                    .setTitle("错误提示")
//                                    .setMessage("无法获取数据，请检查网络！")
//                                    .show();
//                        }
//                    });

//                    View view = MainActivity.super.findViewById(R.id.article_item_layout);
//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            new AlertDialog.Builder(MainActivity.this)
//                                    .setTitle("错误提示")
//                                    .setMessage("无法获取数据，请检查网络！")
//                                    .show();
//                        }
//                    });


                    promptHandler.sendEmptyMessage(500);

                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void processData(IndexPage page) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 清除掉滚动视图中，现有的所有组件，用于增加新的组件
                // 由于组件是添加到ConstraintLayout里面的，所以清除ConstraintLayout中的所有视图
                ConstraintLayout layout = MainActivity.super.findViewById(R.id.article_item_layout);
                layout.removeAllViews();

                ScrollView scrollView = MainActivity.super.findViewById(R.id.scrollView);
                // 滚动到最顶部
                scrollView.scrollTo(0, 0);
                // 内存回收
                System.gc();
            }
        });

        final List<Article> articles = page.getArticles();

        // 把数据通过runOnUiThread方法传递给UI线程，更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (final Article article : articles) {

                    processArticle(article);
                }
            }
        });
    }

    private void processArticle(Article article) {
        int viewId = article.getId() - 1;

        /*// 1.找到布局，通过布局的id能够非常容易定位到布局
        ConstraintLayout layout = super.findViewById(R.id.article_item_layout);

        // 2.创建组件对象，并且设置组件对象的内容
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setId(i);//最小id从1开始
        imageView.setImageDrawable(super.getDrawable(R.drawable.fkjava));
        layout.addView(imageView);

        // 3.创建约束集，里面包含一堆的连接
        ConstraintSet constraintSet = new ConstraintSet();
        // 克隆布局，把布局的属性放入约束集里面
        constraintSet.clone(layout);

        // 把imageView和布局连接起来

        // 把imageView的左边和布局的左边连接起来
        // app:layout_constraintStart_toStartOf="@id/article_item_layout"
        constraintSet.connect(imageView.getId(), ConstraintSet.START, viewId, ConstraintSet.START);
        // app:layout_constraintTop_toTopOf="@id/article_item_layout"
        if (viewId == 0) {
            // 第一次运行的时候，参考布局的上边
            constraintSet.connect(imageView.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        } else {
            // 第二次以及之后，要参考前一个组件的下边
            constraintSet.connect(imageView.getId(), ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM);
        }

        // 指定图片的高度和宽度
        //android:layout_width="84dp"
        //android:layout_height="68dp"
        constraintSet.constrainWidth(imageView.getId(), 150);
        constraintSet.constrainHeight(imageView.getId(), 150);

        // 指定图片的外边距
        //android:layout_marginStart="0dp"
        //android:layout_marginTop="0dp"
        constraintSet.setMargin(imageView.getId(), ConstraintSet.START, 0);
        constraintSet.setMargin(imageView.getId(), ConstraintSet.TOP, 30);

        // 4.应用约束集到布局
        constraintSet.applyTo(layout);*/

        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setId(article.getId());//最小id从1开始
        //imageView.setImageDrawable(MainActivity.super.getDrawable(R.drawable.fkjava));
        //Bitmap bitmap = RemoteData.getImage("/article/" + article.getImage());
        //imageView.setImageBitmap(bitmap);
        RemoteData.setImage(imageView, "/article/" + article.getImage());
        MainActivity.this.addView(imageView, 0, 30, 150, 150, viewId, ConstraintSet.START, ConstraintSet.BOTTOM);

        TextView titleView = new TextView(MainActivity.this);
        titleView.setId(1000 + article.getId());//id不能重复
        titleView.setText(article.getTitle());
        MainActivity.this.addView(titleView, 0, 0, 900, 90, imageView.getId(), ConstraintSet.END, ConstraintSet.TOP);

        // Paint表示画笔，负责画图
        TextView priceView = new TextView(MainActivity.this);
        priceView.setId(2000 + article.getId());
        priceView.setText("￥" + String.valueOf(article.getPrice()));
        priceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//增加删除线显示
        // 宽度为0，表示按照内容自动缩放组件的大小
        MainActivity.this.addView(priceView, 0, 0, 0, 50, titleView.getId(), ConstraintSet.START, ConstraintSet.BOTTOM);

        TextView discountPriceView = new TextView(MainActivity.this);
        discountPriceView.setId(3000 + article.getId());
        discountPriceView.setText("￥" + String.valueOf(article.getDiscountPrice()));
        discountPriceView.setTextColor(Color.RED);//把字体改为红色
        discountPriceView.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);//加粗
        MainActivity.this.addView(discountPriceView, 0, 0, 0, 50, priceView.getId(), ConstraintSet.END, ConstraintSet.TOP);

    }

    /**
     * @param view     要添加的视图组件对象
     * @param start    左外边距
     * @param top      上外边距
     * @param width    组件的宽度
     * @param height   组件的高度
     * @param viewId   参考、引用那个视图组件
     * @param startRef 左边参考viewId的START还是END
     * @param topRef   上边参考viewId的TOP或者BOTTOM
     */
    private void addView(View view, int start, int top, int width, int height, int viewId, int startRef, int topRef) {
        // 1.找到布局，通过布局的id能够非常容易定位到布局
        ConstraintLayout layout = super.findViewById(R.id.article_item_layout);


        layout.addView(view);

        // 3.创建约束集，里面包含一堆的连接
        ConstraintSet constraintSet = new ConstraintSet();
        // 克隆布局，把布局的属性放入约束集里面
        constraintSet.clone(layout);

        // 把imageView和布局连接起来

        // 把imageView的左边和布局的左边连接起来
        // app:layout_constraintStart_toStartOf="@id/article_item_layout"
        constraintSet.connect(view.getId(), ConstraintSet.START, viewId, startRef);
        // app:layout_constraintTop_toTopOf="@id/article_item_layout"
        if (viewId == 0) {
            // 第一次运行的时候，参考布局的上边
            constraintSet.connect(view.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        } else {
            // 第二次以及之后，要参考前一个组件的下边
            constraintSet.connect(view.getId(), ConstraintSet.TOP, viewId, topRef);
        }

        // 指定图片的高度和宽度
        //android:layout_width="84dp"
        //android:layout_height="68dp"
        constraintSet.constrainWidth(view.getId(), width);
        constraintSet.constrainHeight(view.getId(), height);

        // 指定图片的外边距
        //android:layout_marginStart="0dp"
        //android:layout_marginTop="0dp"
        constraintSet.setMargin(view.getId(), ConstraintSet.START, start);
        constraintSet.setMargin(view.getId(), ConstraintSet.TOP, top);

        // 4.应用约束集到布局
        constraintSet.applyTo(layout);
    }
}
