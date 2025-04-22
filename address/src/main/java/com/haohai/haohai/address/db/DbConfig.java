package com.haohai.haohai.address.db;

import android.content.Context;


import com.haohai.haohai.address.db.model.Address;
import com.haohai.haohai.address.db.model.User;

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
                .setDbName("haohaiaddress.db")
                .setAllowTransaction(true)
                .setDbDir(context.getFilesDir())
                //.setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(3);

        return daoConfig;
    }
    public DbManager getDbManager(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        return db;
    }
    public List<Address> getAddressList(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Address> areaList = db.selector(Address.class)
                    .orderBy("id",true)
                    .findAll()
                    ;
            return areaList;

        } catch (DbException e) {
        }
        return null;
    }
    public void clearAddress(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            db.delete(Address.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

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



}
