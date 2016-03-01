package com.march.libs.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.march.libs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * CdLibsTest     com.march.libs.widgets
 * Created by 陈栋 on 16/2/14.
 * 功能:下方导航栏
 */
public class TabHolder extends LinearLayoutCompat {
    /**
     * 从代码加载
     *
     * @param context
     */
    public TabHolder(Context context) {
        super(context);
    }

    public TabHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 从XML文件加载
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TabHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 从代码加载之后使用该方法设置属性
     *
     * @param type
     * @param chooseColor
     * @param normalColor
     */
    public void init(int type, int chooseColor, int normalColor) {
        this.mChooseColor = chooseColor;
        this.mNormalColor = normalColor;
        this.tabType = type;
        tabs = new ArrayList<>();
        initView();
    }

    private List<TabView> tabs;
    public static final int TYPE_HORIZONTAL_IMG_TXT = 1;
    public static final int TYPE_VERTICAL_IMG_TXT = 2;
    private int tabType = 1;
    private LinearLayoutCompat parent;
    private int mChooseColor = Color.BLACK;//被选中的颜色
    private int mNormalColor = Color.GRAY;//没有被选中的颜色
    private int mPreChoosed = -1;
    private int index = 0;

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_tabholder, this, true);
        parent = (LinearLayoutCompat) findViewById(R.id.widget_tabholder_holder);
    }


    private TabView getTabView(Drawable normal, Drawable choose, String txt) {
        TabView tab = (TabView) LayoutInflater.from(getContext()).inflate(R.layout.widget_item_tabholder, this, false);
        tab.setIndex(index++);
        tab.init(tabType, mChooseColor, mNormalColor, normal, choose, txt);
        initEvent(tab);
        return tab;
    }

    public TabHolder addTab(Drawable normal, Drawable choose, String txt) {
        TabView tabView = getTabView(normal, choose, txt);
        parent.addView(tabView);
        tabs.add(tabView);
        return this;
    }

    public TabHolder addTab(int normal, int choose, String txt) {
        TabView tabView = getTabView(ContextCompat.getDrawable(getContext(), normal),
                ContextCompat.getDrawable(getContext(), choose), txt);
        parent.addView(tabView);
        tabs.add(tabView);
        return this;
    }


    private void initEvent(final TabView view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreChoosed != -1) {
                    tabs.get(mPreChoosed).unselect();
                }
                view.select();
                mPreChoosed = view.getIndex();
            }
        });
    }

}
