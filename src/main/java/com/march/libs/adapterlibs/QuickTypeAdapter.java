package com.march.libs.adapterlibs;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.march.libs.utils.LUtils;

import java.util.List;

/**
 * version 2<br/>
 * if max(type value) is n<br/>
 * then type value must between 0 - (n-1)<br/>
 * here use templete method,you can implement the method in this subclass<br/>
 * 抽象适配器升级版，可以进行分类适配，使用了模板方法模式，将设置item显示内容的部分抽象到了类外<br/>
 *
 * @param <T> <br/>必须实现MultiEasyAdapterInterface接口{@link QuickInterface}<br/>
 *            <br/>the data must be implement the interface QuickInterface{@link QuickInterface}
 * @author chendong
 */
public abstract class QuickTypeAdapter<T extends QuickInterface>
        extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<T> datas;
    private SparseArray<AdapterConfig> Res4Type;
    private Context context;

    /**
     * @param context 上下文对象
     * @param datas   数据集
     */
    public QuickTypeAdapter(Context context, List<T> datas) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.context = context;
    }

    public QuickTypeAdapter(Context context, List<T> datas,
                            SparseArray<AdapterConfig> Res4Type) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.Res4Type = Res4Type;
        this.context = context;
    }


    protected Context getContext() {
        return context;
    }

    protected List<T> getDatas() {
        return datas;
    }

    /**
     * 切换数据源
     * @param datas
     */
    public void swapData(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return Res4Type.size();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    public int getCount() {
        return datas.size();
    }

    public Object getItem(int position) {
        return datas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (Res4Type == null) {
            throw new IllegalStateException("you must invoke addType first to init type config");
        }
        ViewHolder holder;
        /* get the type*/
        int type = datas.get(position).getType();
        if (convertView == null) {
            LUtils.i("chendong", "type is " + type + "  pos is  " + position);
            int resId = Res4Type.get(type).getResId();
            convertView = layoutInflater.inflate(resId, parent, false);
            holder = new ViewHolder(convertView, Res4Type.get(type)
                    .getViewCount());
            convertView.setTag(holder);
            bindListener4View(holder, datas.get(position), type, position);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindData4View(holder, datas.get(position), type, position);
        return convertView;
    }

    /**
     * 绑定数据
     * bind data
     *
     * @param holder the viewholder
     * @param type   data's type
     * @param data   data
     */
    public abstract void bindData4View(ViewHolder holder, T data, int type, int pos);

    /**
     * 绑定监听
     * bind listener
     *
     * @param holder the viewholder
     * @param type   data's type
     * @param pos    position
     */
    public abstract void bindListener4View(ViewHolder holder, T data, int type, int pos);


    public QuickTypeAdapter addType(int type, int resId) {
        if (this.Res4Type == null)
            this.Res4Type = new SparseArray<>();
        this.Res4Type.put(type, new AdapterConfig(type, resId));
        return this;
    }


}
