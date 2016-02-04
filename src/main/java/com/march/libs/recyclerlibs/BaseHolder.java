package com.march.libs.recyclerlibs;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * BBPP     com.babypat.adapter
 * Created by 陈栋 on 15/12/28.
 * 功能:
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder {

    protected OnRecyclerItemClickListener<BaseHolder> clickListener;
    protected OnRecyclerItemLongClickListener<BaseHolder> longClickListenter;

    public void setOnItemClickListener(OnRecyclerItemClickListener<BaseHolder> listener) {
        if (listener != null) {
            this.clickListener = listener;
        }
    }

    public void setOnItemLongClickListener(OnRecyclerItemLongClickListener<BaseHolder> longClickListener) {
        if (longClickListener != null) {
            this.longClickListenter = longClickListener;
        }
    }

    public BaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), BaseHolder.this);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListenter != null) {
                    longClickListenter.onItemLongClick(getAdapterPosition(), BaseHolder.this);
                }
                return true;
            }
        });
    }

    protected <V extends View> V  getView(int resId){
        View viewById = itemView.findViewById(resId);
        if(viewById!=null){
            return (V) viewById;
        }else {
            throw new IllegalStateException("can not find this view,check first please");
        }
    }

}
