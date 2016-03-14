package com.march.libs.recyclerlibs;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.march.libs.R;

import java.lang.reflect.Field;


/**
 * BBPP     com.babypat.adapter
 * Created by 陈栋 on 15/12/28.
 * 功能:
 */
public class RvViewHolder extends RecyclerView.ViewHolder {

    protected OnRecyclerItemClickListener<RvViewHolder> clickListener;
    protected OnRecyclerItemLongClickListener<RvViewHolder> longClickListenter;

    private SparseArray<View> cacheViews;
    private View itemView;


    public RvViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        cacheViews = new SparseArray<>(5);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), RvViewHolder.this);
                }
            }
        });


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListenter != null) {
                    longClickListenter.onItemLongClick(getAdapterPosition(), RvViewHolder.this);
                }
                return true;
            }
        });
    }

    public View getParentView() {
        return itemView;
    }


    /**
     * 使用资源id找到view
     *
     * @param resId 资源id
     * @param <T>   泛型,View的子类
     * @return 返回泛型类
     */
    public <T extends View> T getView(int resId) {
        T v = (T) cacheViews.get(resId);
        if (v == null) {
            v = (T) itemView.findViewById(resId);
            if (v != null) {
                cacheViews.put(resId, v);
            }
        }
        return v;
    }

    /**
     * 使用类反射找到字符串id代表的view
     *
     * @param idName String类型ID
     * @return 返回
     */
    public View getView(String idName) {
        View view = null;
        if (idName != null) {
            Class<R.id> idClass = R.id.class;
            try {
                Field field = idClass.getDeclaredField(idName);
                field.setAccessible(true);
                int id = field.getInt(idClass);
                view = getView(id);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return view;
    }


    public void setOnItemClickListener(OnRecyclerItemClickListener<RvViewHolder> listener) {
        if (listener != null) {
            this.clickListener = listener;
        }
    }

    public void setOnItemLongClickListener(OnRecyclerItemLongClickListener<RvViewHolder> longClickListener) {
        if (longClickListener != null) {
            this.longClickListenter = longClickListener;
        }
    }


    public RvViewHolder setText(int res, String s) {
        TextView tv =  getView(res);
        tv.setText(s);
        return this;
    }
}
