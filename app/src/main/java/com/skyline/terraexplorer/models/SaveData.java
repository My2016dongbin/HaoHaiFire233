package com.skyline.terraexplorer.models;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public  class SaveData {

    /**
     * 存储缓存数据
     * @param context
     * @param list
     * @param name
     * @param <T>
     */
    public static <T> void setData(Context context, List<T> list,  String name)
    {
        File file = context.getCacheDir();
        File Cache = null;
            Cache = new File(file,name);
        if(Cache.exists()){
            Cache.delete();
        }
        try {
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(new FileOutputStream(Cache));
            outputStream.writeObject(list);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存数据
     * @param context
     * @param name
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> getData(Context context,String name) throws IllegalAccessException, InstantiationException {
        File file = context.getCacheDir();
        File cache;
        List<T> list = null;
            cache = new File(file,name);
            if(!cache.exists()){
                return null;
            }
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(cache));
                list = (List<T>) inputStream.readObject();
                inputStream.close();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }

}
