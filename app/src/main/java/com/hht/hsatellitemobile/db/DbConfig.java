package com.hht.hsatellitemobile.db;

import android.content.Context;

import com.hht.hsatellitemobile.db.model.Area;
import com.hht.hsatellitemobile.db.model.Setting;
import com.hht.hsatellitemobile.db.model.User;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by 13589 on 2019/8/6.
 */

public class DbConfig {
    public Context context;

    public DbConfig(Context context) {
        this.context = context;
    }

    public DbManager.DaoConfig getDaoConfig() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("haohai.db")
                .setAllowTransaction(true)
                .setDbDir(context.getFilesDir())
                //.setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(14);

        return daoConfig;
    }
    public DbManager getDbManager(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        return db;
    }
    public List<Area> getAreaList(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Area> areaList = db.selector(Area.class)
                    .findAll();
            return areaList;

        } catch (DbException e) {
        }
        return null;
    }

    public User getUser(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")){
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public Setting getSetting(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Setting> settings = db.selector(Setting.class)
                    .findAll();
            if (settings != null){
                Setting setting = settings.get(0);
                return setting;
            }

        } catch (DbException e) {
        }
        return null;
    }

}
