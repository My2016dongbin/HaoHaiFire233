package com.skyline.terraexplorer.db;

import android.content.Context;


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
                .setDbName("qdgrid.db")
                .setAllowTransaction(true)
                .setDbDir(context.getFilesDir())
                //.setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(20);

        return daoConfig;
    }
    public DbManager getDbManager(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        return db;
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
                    if (user.getIsLogin() == 1){
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public List<Resource> getResourceList(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Resource> resourceList = db.selector(Resource.class)
                    .where("state","=","ACTIVE")
                    .where("isdisplay","=","1")
                    .findAll();
             return resourceList;

        } catch (DbException e) {
        }
        return null;
    }

    public List<CheckRecord> getCheckRecordList(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<CheckRecord> checkRecordList = db.selector(CheckRecord.class)
                    .findAll();
            return checkRecordList;

        } catch (DbException e) {
        }
        return null;
    }

    public List<DangerRecord> getDangerRecordList(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<DangerRecord> dangerRecordList = db.selector(DangerRecord.class)
                    .findAll();
            return dangerRecordList;

        } catch (DbException e) {
        }
        return null;
    }

    public List<ImagesUrl> getImageUrlList(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<ImagesUrl> imagesUrlList = db.selector(ImagesUrl.class)
                    .findAll();
            return imagesUrlList;

        } catch (DbException e) {
        }
        return null;
    }

}
