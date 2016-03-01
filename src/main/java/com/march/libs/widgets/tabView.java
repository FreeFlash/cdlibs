package com.march.libs.widgets;

/**
 * CdLibsTest     com.march.libs.widgets
 * Created by 陈栋 on 16/2/14.
 * 功能:
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.march.libs.R;

public class TabView extends LinearLayout {

    /**
     * 从代码加载
     *
     * @param context
     */
    public TabView(Context context) {
        super(context);
    }


    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray array = getContext().obtainStyledAttributes(attrs,
//                R.styleable.TabView);
//        /**
//         * 初始化tab导航的图标,标题
//         */
//        mNormalDrawable = array.getDrawable(R.styleable.TabView_normalImg);
//        mChooseDrawable = array.getDrawable(R.styleable.TabView_chooseImg);
//        txtStr = array.getString(R.styleable.TabView_text);
//        array.recycle();
//        initView();
    }

    /**
     * 从xml文件加载
     *
     * @param context
     * @param attrs
     */
    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 从代码加载之后使用该方法设置属性
     *
     * @param chooseColor
     * @param normalColor
     * @param normalDrawable
     * @param chooseDrawable
     * @param txt
     */
    public void init(int tabType, int chooseColor,
                     int normalColor,
                     Drawable normalDrawable,
                     Drawable chooseDrawable, String txt) {
        this.type = tabType;
        this.mNormalColor = normalColor;
        this.mChooseColor = chooseColor;
        this.mNormalDrawable = normalDrawable;
        this.mChooseDrawable = chooseDrawable;
        this.txtStr = txt;
        initView();
    }

    private ImageView mIconIv;//图标
    private TextView mTxtTv;//文本内容
    private int mChooseColor;//被选中的颜色
    private int mNormalColor;//没有被选中的颜色
    private Drawable mNormalDrawable;//没有被选中时的图标
    private Drawable mChooseDrawable;//选中时的图标
    private String txtStr;//显示的字符串
    private boolean isChoosed = false;//记录是否被选中


    private int type = 2;


    private void generate4Type() {
        switch (type) {
            case TabHolder.TYPE_HORIZONTAL_IMG_TXT:
                LayoutInflater.from(getContext()).inflate(R.layout.widget_tabview_horizontal, this, true);
                mIconIv = (ImageView) findViewById(R.id.widget_tabview_hori_img);
                mTxtTv = (TextView) findViewById(R.id.widget_tabview_hori_txt);
                break;
            case TabHolder.TYPE_VERTICAL_IMG_TXT:
                LayoutInflater.from(getContext()).inflate(R.layout.widget_tabview_vertical, this, true);
                mIconIv = (ImageView) findViewById(R.id.widget_tabview_img);
                mTxtTv = (TextView) findViewById(R.id.widget_tabview_txt);
                break;
        }
    }

    private void initView() {
        generate4Type();
//        this.mChooseColor = Color.BLUE;//getResources().getColor(R.color.transparent_gray);
//        this.mNormalColor = Color.YELLOW;//getResources().getColor(R.color.transparent_gray);
        //初始化控件状态,默认没有选中
        this.mTxtTv.setText(txtStr);
        this.mTxtTv.setTextColor(mNormalColor);
        this.mIconIv.setImageDrawable(mNormalDrawable);
    }


    /**
     * 切换状态,如果是选中则设置为不选中
     */
    public void toogle() {
        if (isChoosed) {
            //改为不选
            this.mTxtTv.setTextColor(mNormalColor);
            this.mIconIv.setImageDrawable(mNormalDrawable);
        } else {
            //改为选中
            this.mTxtTv.setTextColor(mChooseColor);
            this.mIconIv.setImageDrawable(mChooseDrawable);
        }
        isChoosed = !isChoosed;
    }

    public void select() {
        this.mTxtTv.setTextColor(mChooseColor);
        this.mIconIv.setImageDrawable(mChooseDrawable);
        isChoosed = true;
    }

    public void unselect() {
        this.mTxtTv.setTextColor(mNormalColor);
        this.mIconIv.setImageDrawable(mNormalDrawable);
        isChoosed = false;
    }


    public void setTextColor(int color) {
        mTxtTv.setTextColor(color);
        if (color == -1)
            mTxtTv.setTextColor(mNormalColor);
    }

    private int index;
    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
