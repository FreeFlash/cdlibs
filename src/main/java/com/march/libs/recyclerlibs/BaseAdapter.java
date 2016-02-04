package com.march.libs.recyclerlibs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by 陈栋 on 15/12/28.
 * 功能:
 */
public abstract class BaseAdapter<H extends BaseHolder, D> extends RecyclerView.Adapter<H> {

    protected List<D> datas;
    protected LayoutInflater mLayoutInflater;
    protected Context context;

    public BaseAdapter(List<D> datas, Context context) {
        this.datas = datas;mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    private OnRecyclerItemClickListener<BaseHolder> clickListener;
    private OnRecyclerItemLongClickListener<BaseHolder> longClickListenter;

    public void setClickListener(OnRecyclerItemClickListener<BaseHolder> listener) {
        if (listener != null) {
            this.clickListener = listener;
        }
    }

    public void setLongClickListenter(OnRecyclerItemLongClickListener<BaseHolder> longClickListenter) {
        if (longClickListenter != null) {
            this.longClickListenter = longClickListenter;
        }
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewHype) {
        H t = OnGetViewHolder(parent, viewHype);
        if (clickListener != null)
            t.setOnItemClickListener(clickListener);
        if (longClickListenter != null) {
            t.setOnItemLongClickListener(longClickListenter);
        }
        return t;
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        OnBindData(holder, position, position, datas.get(position));
    }

    public View getInflateView(int resId, ViewGroup parent){
        return mLayoutInflater.inflate(resId,parent,false);
    }

    /**
     * 重写该方法获得对应类型的ViewHolder
     * @param parent
     * @param type
     * @return
     */
    public abstract H OnGetViewHolder(ViewGroup parent, int type);

    /**
     * 重写该方法绑定ViewHodler将要显示的数据
     * @param holder
     * @param pos
     * @param type
     * @param data
     */
    public abstract void OnBindData(H holder, int pos, int type, D data);


    @Override
    public int getItemCount() {
        return datas.size();
    }
}
