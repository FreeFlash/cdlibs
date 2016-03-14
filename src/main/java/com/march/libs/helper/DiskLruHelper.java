package com.march.libs.helper;

/**
 * babyphoto_app     com.babypat.common
 * Created by 陈栋 on 16/2/26.
 * 功能:disklrucache的封装类
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.march.rapiddevelibs.utils.AppUtils;
import com.march.rapiddevelibs.utils.FileUtils;
import com.march.rapiddevelibs.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 * Created by zhy on 15/7/28.
 */
public class DiskLruHelper {
    private static final String DIR_NAME = "diskCache";
    private static final int MAX_COUNT = 10 * 1024 * 1024; //10M
    private static final int DEFAULT_APP_VERSION = 1;

    private static final int DEFAULT_VALUE_COUNT = 1;

    private static final String TAG = "DiskLruCacheHelper";

    private static DiskLruCache mDiskLruCache;

    public static DiskLruHelper helper;

    public static DiskLruHelper getInst() {
        if (helper == null) {
            throw new IllegalStateException("u must invoke the method newInst() at first!");
        }
        return helper;
    }

    public static DiskLruHelper newInst(Context context, String dir, int maxCount) {
        if (helper == null) {
            synchronized (DiskLruHelper.class) {
                if (helper == null) {
                    if (dir == null)
                        dir = FileUtils.getDiskCacheDir(context, DIR_NAME).getAbsolutePath();
                    if (maxCount == 0)
                        maxCount = MAX_COUNT;
                    helper = new DiskLruHelper(context, dir, maxCount);
                }
            }
        }
        return helper;
    }

    public static DiskLruHelper newInst(Context context, String dir) {
        return newInst(context, dir, 0);
    }

    public static DiskLruHelper newInst(Context context) {
        return newInst(context, null, 0);
    }

    //私有化构造方法
    private DiskLruHelper(Context context, String dirName, int maxCount) {
        mDiskLruCache = generateCache(context, dirName, maxCount);
    }


    //构造DiskLruCache对象
    private static DiskLruCache generateCache(Context context, String dir, int maxCount) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException(
                    dir + " is not a directory or does not exists. ");
        }

        int appVersion = context == null ? DEFAULT_APP_VERSION : AppUtils.getAppVersion();

        try {
            return DiskLruCache.open(
                    file,
                    appVersion,
                    DEFAULT_VALUE_COUNT,
                    maxCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入String对象
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(key);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(value);
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //s
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取String对象
     *
     * @param key
     * @return
     */
    public String getAsString(String key) {
        InputStream inputStream = null;
        try {
            //write READ
            inputStream = get(key);
            if (inputStream == null) return null;
            StringBuilder sb = new StringBuilder();
            int len = 0;
            byte[] buf = new byte[128];
            while ((len = inputStream.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
            return sb.toString();


        } catch (IOException e) {
            e.printStackTrace();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 插入jsonobj
     *
     * @param key
     * @param jsonObject
     */
    public void put(String key, JSONObject jsonObject) {
        put(key, jsonObject.toString());
    }

    /**
     * 获取jsonobj
     *
     * @param key
     * @return
     */
    public JSONObject getAsJson(String key) {
        String val = getAsString(key);
        try {
            if (val != null)
                return new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入jsonarray
     *
     * @param key
     * @param jsonArray
     */
    public void put(String key, JSONArray jsonArray) {
        put(key, jsonArray.toString());
    }

    /**
     * 获取jsonarray
     *
     * @param key
     * @return
     */
    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 插入byte数组
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = editor(key);
            if (editor == null) {
                return;
            }
            out = editor.newOutputStream(0);
            out.write(value);
            out.flush();
            editor.commit();//write CLEAN
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (editor != null)
                    editor.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取byte数组
     *
     * @param key
     * @return
     */
    public byte[] getAsBytes(String key) {
        byte[] res = null;
        InputStream is = get(key);
        if (is == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[256];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 插入序列化对象
     *
     * @param key
     * @param value
     */
    public void put(String key, Serializable value) {
        DiskLruCache.Editor editor = editor(key);
        ObjectOutputStream oos = null;
        if (editor == null) return;
        try {
            OutputStream os = editor.newOutputStream(0);
            oos = new ObjectOutputStream(os);
            oos.writeObject(value);
            oos.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取序列化对象,在点之后方法之前声明泛型
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getAsSerializable(String key) {
        T t = null;
        InputStream is = get(key);
        ObjectInputStream ois = null;
        if (is == null) return null;
        try {
            ois = new ObjectInputStream(is);
            t = (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 插入bitmap
     *
     * @param key
     * @param bitmap
     */
    public void put(String key, Bitmap bitmap) {
        put(key, ImageUtils.bitmap2Bytes(bitmap));
    }

    /**
     * 获取bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getAsBitmap(String key) {
        byte[] bytes = getAsBytes(key);
        if (bytes == null) return null;
        return ImageUtils.bytes2Bitmap(bytes);
    }

    /**
     * 插入drawable
     *
     * @param key
     * @param value
     */
    public void put(String key, Drawable value) {
        put(key, ImageUtils.drawable2Bitmap(value));
    }

    /**
     * 获取drawable
     *
     * @param key
     * @return
     */
    public Drawable getAsDrawable(String key) {
        byte[] bytes = getAsBytes(key);
        if (bytes == null) {
            return null;
        }
        return ImageUtils.bitmap2Drawable(ImageUtils.bytes2Bitmap(bytes));
    }

    /**
     * 删除制定key对应的输入
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            key = FileUtils.hashKeyForDisk(key);
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void close() throws IOException {
        mDiskLruCache.close();
    }

    public void delete() throws IOException {
        mDiskLruCache.delete();
    }

    public void flush() throws IOException {
        mDiskLruCache.flush();
    }

    public boolean isClosed() {
        return mDiskLruCache.isClosed();
    }

    public long size() {
        return mDiskLruCache.size();
    }

    public void setMaxSize(long maxSize) {
        mDiskLruCache.setMaxSize(maxSize);
    }

    public File getDirectory() {
        return mDiskLruCache.getDirectory();
    }

    public long getMaxSize() {
        return mDiskLruCache.getMaxSize();
    }


    public DiskLruCache.Editor editor(String key) {
        try {
            key = FileUtils.hashKeyForDisk(key);
            //wirte DIRTY
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            //edit maybe null :the entry is editing
            if (edit == null) {
                Log.w(TAG, "the entry spcified key:" + key + " is editing by other . ");
            }
            return edit;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取输入流
     *
     * @param key
     * @return
     */
    public InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(FileUtils.hashKeyForDisk(key));
            if (snapshot == null) //not find entry , or entry.readable = false
            {
                Log.e(TAG, "not find entry , or entry.readable = false");
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}
