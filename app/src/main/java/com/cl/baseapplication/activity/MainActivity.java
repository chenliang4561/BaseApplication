package com.cl.baseapplication.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cl.baseapplication.R;
import com.cl.baseapplication.base.BaseFragmentActivity;
import com.cl.baseapplication.fragment.FragmentFind;
import com.cl.baseapplication.fragment.FragmentHome;
import com.cl.baseapplication.fragment.FragmentMine;

/**
 *
 * @author cliang
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private FrameLayout mContent;
    private RadioGroup mNavigationBtn;
    private RadioButton mBtn1, mBtn2, mBtn3;
    private Fragment[] mFragments;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
    }

    private void initView() {
        mContent = findViewById(R.id.fl_content);
        mNavigationBtn = findViewById(R.id.navigation_btn);
        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        setIconSize();
    }

    private void initFragment() {
        //首页
        FragmentHome fragmenthome = new FragmentHome();
        //发现
        FragmentFind fragmentfind = new FragmentFind();
        //我的
        FragmentMine fragmentmine = new FragmentMine();
        //添加到数据
        mFragments = new Fragment[]{fragmenthome, fragmentfind, fragmentmine};
        //开启事物
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.fl_content, fragmenthome).commit();
        //默认设置为第0个
        setIndexSelected(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                setIndexSelected(0);
                break;

            case R.id.btn2:
                setIndexSelected(1);
                break;

            case R.id.btn3:
                setIndexSelected(2);
                break;

            default:
                break;
        }
    }

    /**
     * fragment的索引
     *
     * @param index
     */
    private void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        //隐藏 正在显示的
        ft.hide(mFragments[mIndex]);
        //判断 要显示的 是否添加 过
        if (!mFragments[index].isAdded()) {
            //没添加 就添加 显示
            ft.add(R.id.fl_content, mFragments[index]).show(mFragments[index]);
        } else {
            //添加过 直接显示
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值 替换正在显示的索引
        mIndex = index;
    }

    /**
     * 设置导航图标大小
     */
    private void setIconSize() {
        int[] drawables = {R.drawable.navigation_bar_selector1, R.drawable.navigation_bar_selector2, R.drawable.navigation_bar_selector3};
        RadioButton[] radioButtons = {mBtn1, mBtn2, mBtn3};
        for (int i = 0; i < drawables.length; i++) {
            radioButtons[i].setTextSize(12);
            radioButtons[i].setTextColor(ContextCompat.getColorStateList(
                    this, R.color.text_background));
            String mess = null;
            if (i == 0) {
                mess = "首页";
                radioButtons[i].setChecked(true);
            } else if (i == 1) {
                mess = "发现";
            } else if (i == 2) {
                mess = "我的";
            }
            radioButtons[i].setText(mess);
        }
        Drawable drawable = getResources().getDrawable(R.drawable.navigation_bar_selector1);
        Drawable drawable1 = getResources().getDrawable(R.drawable.navigation_bar_selector2);
        Drawable drawable2 = getResources().getDrawable(R.drawable.navigation_bar_selector3);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mNavigationBtn.measure(w, h);
        int height = mNavigationBtn.getMeasuredHeight();
        drawable.setBounds(0, 0, height / 2, height / 2);
        drawable1.setBounds(0, 0, height / 2, height / 2);
        drawable2.setBounds(0, 0, height / 2, height / 2);
        mBtn1.setCompoundDrawables(null, drawable, null, null);
        mBtn2.setCompoundDrawables(null, drawable1, null, null);
        mBtn3.setCompoundDrawables(null, drawable2, null, null);
    }
}
