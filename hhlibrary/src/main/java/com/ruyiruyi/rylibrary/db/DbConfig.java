package com.ruyiruyi.rylibrary.db;

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
                .setDbName("platform.db")
                .setAllowTransaction(true)
                .setDbDir(context.getFilesDir())
                //.setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(2);

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
    public List<Department> getDepartment(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Department> departmentList = db.selector(Department.class)
                    .findAll();
            return departmentList;

        } catch (DbException e) {
        }
        return null;
    }
    public List<UserModel> getUserModel(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<UserModel> userModelList = db.selector(UserModel.class)
                    .findAll();
            return userModelList;

        } catch (DbException e) {
        }
        return null;
    }

    public Attendance getAttendance(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Attendance> attendances = db.selector(Attendance.class)
                    .findAll();
            if (attendances != null){
                return attendances.get(0);
            }

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
                    if (user.getIsLogin() == 1){
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public User getUserOut(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null){
                User user = users.get(0);
                return user;

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
