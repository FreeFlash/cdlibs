package com.march.libs.recyclerlibs;

/**
 * CdLibsTest     com.march.libs.recyclerlibs
 * Created by 陈栋 on 16/2/4.
 * 功能:
 */
public  interface OnRecyclerItemClickListener<T extends BaseHolder>  {
    void onItemClick(int pos,T holder);
}
