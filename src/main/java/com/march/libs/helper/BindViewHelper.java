package com.march.libs.helper;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;

import java.util.HashMap;

/**
 * CdLibsTest     com.march.libs.helper
 * Created by 陈栋 on 16/4/5.
 * 功能:使用ID或者别名获取控件,仍旧使用findViewById()的方法.避免记录过多变量
 */
public class BindViewHelper {

    private View parent;
    private SparseArray<View> views;
    private HashMap<String, Integer> maps;

    /**
     * 添加别名,添加之后可以使用别名获取控件
     *
     * @param alias 别名
     * @param res 资源ID
     */
    public void mapping(String alias, int res) {
        if (maps == null) {
            maps = new HashMap<>();
        }
        maps.put(alias, res);
    }


    public BindViewHelper(Activity activity) {
        this.parent = activity.getWindow().getDecorView();
        this.views = new SparseArray<>();
    }


    public BindViewHelper(View view) {
        this.parent = view;
        this.views = new SparseArray<>();
    }


    /**
     * 根据别名获取控件
     *
     * @param newParent 父控件,如果该控件不能从Activity中获取
     * @param resID     资源id
     * @param <T>       控件类型范型
     * @return 控件
     */
    public <T extends View> T get(View newParent, int resID) {
        T t = (T) views.get(resID);
        if (t == null) {
            t = (T) newParent.findViewById(resID);
            views.put(resID, t);
        }
        return t;
    }


    /**
     * 根据资源id获取控件
     *
     * @param resID 资源id
     * @param <T>   控件类型范型
     * @return 控件
     */
    public <T extends View> T get(int resID) {
        T t = (T) views.get(resID);
        if (t == null) {
            t = (T) parent.findViewById(resID);
            views.put(resID, t);
        }
        return t;
    }


    /**
     * 根据别名获取控件
     *
     * @param alias 别名
     * @param <T>   控件类型范型
     * @return 控件
     */
    public <T extends View> T get(String alias) {
        Integer key = maps.get(alias);
        T t = (T) views.get(key);
        if (t == null) {
            t = (T) parent.findViewById(key);
            views.put(key, t);
        }
        return t;
    }

    /**
     * 根据别名获取控件
     *
     * @param newParent 父控件,如果该控件不能从Activity中获取
     * @param alias     别名
     * @param <T>       控件类型范型
     * @return 控件
     */
    public <T extends View> T get(View newParent, String alias) {
        Integer key = maps.get(alias);
        T t = (T) views.get(key);
        if (t == null) {
            t = (T) newParent.findViewById(key);
            views.put(key, t);
        }
        return t;
    }
}
