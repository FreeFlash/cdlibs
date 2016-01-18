package com.march.libs.adapterlibs;


import android.graphics.Bitmap;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.march.libs.R;

import java.lang.reflect.Field;

/**
 * @author chendong ViewHolder类
 */
public class ViewHolder {

    /**
     * SparseArray稀疏数组
     */
    private SparseArray<View> cacheViews;
    private View itemView;

    public ViewHolder(View itemView, int viewCount) {
        super();
        this.itemView = itemView;
        cacheViews = new SparseArray<View>(viewCount);
    }


    public View getParentView() {
        return itemView;
    }

    /**
     * 使用资源id找到view
     *
     * @param resId
     * @return
     */
    public View getView(int resId) {
        View v = cacheViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            if (v != null) {
                cacheViews.put(resId, v);
            }
        }
        return v;
    }

    /**
     * 使用类反射找到字符串id代表的view
     *
     * @param idName
     * @return
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

    public ViewHolder setIsVisiable(int resId, int visiable) {
        getView(resId).setVisibility(visiable);
        return this;
    }


    /**
     * 为控件设置tag
     *
     * @param resId
     * @param tag
     * @return
     */
    public ViewHolder setTag(int resId, Object tag) {
        getView(resId).setTag(tag);
        return this;
    }

    /**
     * 为checkbox设置是否选中
     *
     * @param resId
     * @param isChecked
     * @return
     */
    public ViewHolder setChecked(int resId, boolean isChecked) {
        ((CheckBox) getView(resId)).setChecked(isChecked);
        return this;
    }


    public ViewHolder setBg(int resId, int bgRes) {
        getView(resId).setBackgroundResource(bgRes);
        return this;
    }

    /**
     * 为textview设置文本
     *
     * @param resId 控件资源id
     * @param txt   设置的文本
     * @return
     */
    public ViewHolder setText(int resId, String txt) {
        ((TextView) getView(resId)).setText(txt);
        return this;
    }
    public ViewHolder setText(int resId, SpannableString txt) {
        ((TextView) getView(resId)).setText(txt);
        return this;
    }

    public ViewHolder setText(String resId, String txt) {
        ((TextView) getView(resId)).setText(txt);
        return this;
    }

    /**
     * 设置点击监听
     *
     * @param resId
     * @param onClickListener
     * @return
     */
    public ViewHolder setOnClickListener(int resId, View.OnClickListener onClickListener, Object tag) {
        getView(resId).setTag(R.string.key, tag);
        getView(resId).setOnClickListener(onClickListener);
        return this;
    }

    public ViewHolder setOnClickListener(String resId, View.OnClickListener onClickListener, Object tag) {
        getView(resId).setTag(R.string.key, tag);
        getView(resId).setOnClickListener(onClickListener);
        return this;
    }
/***********imageview*****************************/
    /**
     * 为ImageView使用图片资源id设置图片
     *
     * @param resId    控件资源id
     * @param imgResId 图片资源id
     * @return
     */
    public ViewHolder setImg(int resId, int imgResId) {
        ((ImageView) getView(resId)).setImageResource(imgResId);
        return this;
    }

    /**
     * 为ImageView使用位图设置图片
     *
     * @param resId 控件资源id
     * @param bit   图片资源位图
     * @return
     */
    public ViewHolder setImg(int resId, Bitmap bit) {
        ((ImageView) getView(resId)).setImageBitmap(bit);
        return this;
    }

//    public ViewHolder setImg(Context context, int resId, String url) {
//        if (!"".equals(url) && url != null)
//            Picasso.with(context).load(url).into((ImageView) getView(resId));
//        return this;
//    }

    //    	public ViewHolder setImg(Context context,String resId,String url){
//		if(!"".equals(url)&&url!=null)
//			Picasso.with(context).load(url).placeholder(context.getResources().getDrawable(R.mipmap.image_default)).into((ImageView) getView(resId));
//		return this;
//	}
//    public ViewHolder setImg(Context context, String resId, String url, int defaultId) {
//        if (!"".equals(url) && url != null)
//            Picasso.with(context).load(url).placeholder(ContextCompat.getDrawable(context, defaultId)).into((ImageView) getView(resId));
//        return this;
//    }
//
//    public ViewHolder setImg(Context context, int resId, String url, int defaultId) {
//        if (!"".equals(url) && url != null)
//            Picasso.with(context).load(url).placeholder(ContextCompat.getDrawable(context, defaultId)).into((ImageView) getView(resId));
//        return this;
//    }


//    public ViewHolder setFrescoImg(int resId, String url) {
//        SimpleDraweeView draweeView = (SimpleDraweeView) getView(resId);
//        Uri uri = Uri.parse(url);
//        if (url.endsWith(".gif")) {
//            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setImageRequest(request)
//                    .setAutoPlayAnimations(true).build();
//            draweeView.setController(controller);
//        } else {
//            draweeView.setImageURI(uri);
//        }
//        return this;
//    }
}
