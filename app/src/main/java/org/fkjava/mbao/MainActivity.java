package org.fkjava.mbao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

    // 通过一个方法来初始化所有的商品
    @SuppressLint("ResourceType")
    private void initArticles() {
        for (int i = 1; i <= 20; i++) {

            int viewId = i - 1;

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
            imageView.setId(i);//最小id从1开始
            imageView.setImageDrawable(super.getDrawable(R.drawable.fkjava));
            this.addView(imageView, 0, 30, 150, 150, viewId, ConstraintSet.START, ConstraintSet.BOTTOM);

            TextView titleView = new TextView(this);
            titleView.setId(1000 + i);//id不能重复
            titleView.setText("《疯狂Java讲义（第4版）》");
            this.addView(titleView, 0, 0, 500, 100, imageView.getId(), ConstraintSet.END, ConstraintSet.TOP);

            // Paint表示画笔，负责画图
            TextView priceView = new TextView(this);
            priceView.setId(2000 + i);
            priceView.setText("￥99999.99");
            priceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//增加删除线显示
            // 宽度为0，表示按照内容自动缩放组件的大小
            this.addView(priceView, 0, 0, 0, 50, titleView.getId(), ConstraintSet.START, ConstraintSet.BOTTOM);

            TextView discountPriceView = new TextView(this);
            discountPriceView.setId(3000 + i);
            discountPriceView.setText("￥99.99");
            discountPriceView.setTextColor(Color.RED);//把字体改为红色
            discountPriceView.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);//加粗
            this.addView(discountPriceView, 0, 0, 0, 50, priceView.getId(), ConstraintSet.END, ConstraintSet.TOP);

        }
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
