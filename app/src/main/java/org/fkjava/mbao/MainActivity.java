package org.fkjava.mbao;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 查找布局
        final ConstraintLayout layout = super.findViewById(R.id.layout2);

        // 记录上一个View的id，用于设置每个view的间隔,如果是0则表示连接到布局管理器上面
        int viewId = 0;

        for (int i = 1; i <= 20; i++) {
            // 创建图片
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setId(i);//id不能从0开始
            imageView.setImageDrawable(MainActivity.super.getDrawable(R.drawable.fkjava));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            MainActivity.this.addView(viewId, layout, imageView, 180, 200, 0, 10);

            // 创建标题
            TextView titleView = new TextView(MainActivity.this);
            titleView.setId(1000 + i);// id不能重复
            titleView.setText("《疯狂Java讲义（第4版）》--最畅销的Java入门教材，全面支援函数式编程、模块化开发");
            titleView.setEllipsize(TextUtils.TruncateAt.END);//尾部放一个省略号
            titleView.setMaxLines(2);//最多两行
            titleView.setLines(2);//显示两行
            MainActivity.this.addView(viewId, layout, titleView, 900, 85, 150, 10);


            // 显示画线价格
            TextView price = new TextView(MainActivity.this);
            price.setId(2000 + i);
            price.setText("￥1999222.99");
            price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//增加删除线显示
            // 宽度为0表示根据内容自动设置宽度
            MainActivity.this.addView(layout, price, 0, 50, imageView, 20, 120);

            // 显示折扣加
            TextView discountPrice = new TextView(MainActivity.this);
            discountPrice.setId(3000 + i);
            discountPrice.setText("￥999.99");
            discountPrice.setTextColor(Color.RED);
            discountPrice.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);//加粗
            //MainActivity.this.addView(viewId, layout, discountPrice, 200, 50, 330, 95);
            MainActivity.this.addView(layout, discountPrice, 0, 50, price, 10, 0);

            // 添加购物车按钮
            ImageView btn = new ImageView(this);
            btn.setId(4000 + i);
            btn.setImageDrawable(super.getDrawable(R.drawable.cart));
            MainActivity.this.addView(layout, btn, 60, 60, discountPrice, 10, 0);

            // 修改viewId，记录本地循环的组件的id，用于把下一个对象放到当前组件的后面
            viewId = i;
        }
    }

    // 把新的视图，放到原本视图的右边
    private void addView(ConstraintLayout layout, View view, int width, int height, View referenceView, int left, int top) {
        layout.addView(view);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);
        cs.constrainWidth(view.getId(), width);
        cs.constrainHeight(view.getId(), height);
        cs.connect(view.getId(), ConstraintSet.START, referenceView.getId(), ConstraintSet.END, left);
        cs.connect(view.getId(), ConstraintSet.TOP, referenceView.getId(), ConstraintSet.TOP, top);
        cs.applyTo(layout);
    }

    private void addView(int viewId, ConstraintLayout layout, View view, int width, int height, int leftMargin, int topMargin) {
        //-----------------------------------------------


        // 把图片加入布局中
        layout.addView(view);

        // 创建约束
        ConstraintSet cs = new ConstraintSet();
        // 克隆布局属性
        cs.clone(layout);

        // 设置布局属性中对象的宽度和高度
        cs.constrainHeight(view.getId(), height);
        cs.constrainWidth(view.getId(), width);

        // 连接约束
        cs.connect(view.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START);

        if (viewId == 0) {
            cs.connect(view.getId(), ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP);
        } else {
            cs.connect(view.getId(), ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM);
        }
        // 做外边距
        cs.setMargin(view.getId(), ConstraintSet.START, leftMargin);
        // 上外边距
        cs.setMargin(view.getId(), ConstraintSet.TOP, topMargin);
        // 应用布局
        cs.applyTo(layout);

        //-----------------------------------------------
    }
}
