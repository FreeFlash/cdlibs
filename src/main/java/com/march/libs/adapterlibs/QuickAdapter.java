package com.march.libs.adapterlibs;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 抽象适配器，使用了模板方法模式，将设置item显示内容的部分抽象到了类外 这是单类型的抽象适配
 * 
 * @author chendong
 * 
 * @param <T>
 *            泛型
 */
public abstract class QuickAdapter<T> extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private int resId;
	private List<T> datas;
	private int viewCount = 3;
	private Context context;


	/**
	 * @param context
	 *            上下文对象，建议使用getApplicationContext();
	 * @param resId
	 *            item布局id
	 * @param datas
	 *            数据集
	 */
	public QuickAdapter(Context context, int resId, List<T> datas) {
		super();
		this.layoutInflater = LayoutInflater.from(context);
		this.resId = resId;
		this.datas = datas;
		this.context  =context;
	}

	public Context getContext(){
		return  this.context;
	}
	public List<T> getData(){
		return datas;
	}

	public void swapData(List<T> datas){
		this.datas = datas;
		notifyDataSetChanged();
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


	int prePos = -1;
	public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = layoutInflater.inflate(resId, parent, false);
				holder = new ViewHolder(convertView, viewCount);
				convertView.setTag(holder);
				//在这里绑定监听
				bindListener4View(holder,datas.get(position),position);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//if (parent.getChildCount() == position&&prePos!=position)
			bindData4View(holder, datas.get(position), position);
			prePos = position;

		return convertView;
	}

	/**
	 * 绑定数据
	 * @param holder
	 * @param data
	 */
	public abstract void bindData4View(ViewHolder holder, T data,int pos);

	/**
	 * 绑定监听
	 * @param holder
	 * @param pos
	 */
	public abstract void bindListener4View(ViewHolder holder, T data,int pos);
}
