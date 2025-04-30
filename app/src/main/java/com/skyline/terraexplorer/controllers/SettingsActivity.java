package com.skyline.terraexplorer.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.User;
import com.skyline.terraexplorer.models.AppLinks;
import com.skyline.terraexplorer.models.DisplayGroupItem;
import com.skyline.terraexplorer.models.DisplayItem;
import com.skyline.terraexplorer.models.LocalBroadcastManager;
import com.skyline.terraexplorer.models.MainButtonDragGestures;
import com.skyline.terraexplorer.models.TableDataSource;
import com.skyline.terraexplorer.models.TableDataSource.TableDataSourceDelegate;
import com.skyline.terraexplorer.models.TableDataSourceDelegateBase;
import com.skyline.terraexplorer.models.ToolManager;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.AboutTool;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.terraexplorer.tools.TutorialTool;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.ModalDialogDelegateBase;
import com.skyline.test.myview.ServerAddressActivity;
import com.skyline.test.myview.SignUp_Activity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Arrays;

public class SettingsActivity extends MatchParentActivity {

    private static final String ITEM_NAME = "com.skyline.terraexplrorer.ITEM_NAME";
    private static final String ITEM_TAG = "com.skyline.terraexplrorer.ITEM_TAG";
    private static final String ITEM_VALUE = "com.skyline.terraexplrorer.ITEM_VALUE";
    private static final String ITEM_VALUE_SET = "com.skyline.terraexplrorer.ITEM_VALUE_SET";
    public static final String USER_INITIAL_LOCATION = "com.skyline.terraexplorer.initial.Location";
    public static final String USER_INITIAL_IP = "com.skyline.terraexplorer.initial.ip";
    public static final String USER_INITIAL_PORT= "com.skyline.terraexplorer.initial.port";
    private TableDataSourceDelegate delegate = new TableDataSourceDelegateBase() {
        @Override
        public void didSelectRowAtIndexPath(long packedPosition) {
            SettingsActivity.this.didSelectRowAtIndexPath(packedPosition);
        }
    };
    private TableDataSource datasource;

    private DisplayGroupItem project;
    private DisplayGroupItem view;
    private SharedPreferences prefs;
    private DisplayItem websearchUrl;

    private  class ModalDialogDelegate extends ModalDialogDelegateBase
    {
        @Override
        public void modalDialogDidDismissWithOk(ModalDialog dlg) {
            SharedPreferences preferences=getSharedPreferences(SettingsTool.PREFERENCES_NAME,Context.MODE_PRIVATE);
            preferences.edit().putString(USER_INITIAL_LOCATION,dlg.getTextField().getText().toString()).commit();
            showSettings();

        }
    }
    private ModalDialogDelegate modalDialogDelegate = new ModalDialogDelegate();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UI.addHeader(R.string.title_activity_settings, R.drawable.settings, this);
        datasource = new TableDataSource(UI.addFullScreenTable(this), delegate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE);
        Intent startingIntent = getIntent();
        // if we have item name in starting intent, we need to show editor for that item 如果我们在启动意图中有项目名称，我们需要显示该项目的编辑器
        if (startingIntent.getStringExtra(ITEM_NAME) != null) {
            showItemEditor(startingIntent);
        }
        // otherwise show regular settings screen 否则显示常规设置屏幕
        else {
            showSettings();
        }

    }

    private void showItemEditor(Intent startingIntent) {
        // update header 更新标题
        ((TextView) findViewById(R.id.header_title)).setText(startingIntent.getStringExtra(ITEM_NAME));
        // update list data 更新列表数据
        DisplayGroupItem parentItem = new DisplayGroupItem(null);
        for (String string : startingIntent.getStringArrayExtra(ITEM_VALUE_SET)) {
            parentItem.childItems.add(new DisplayItem(string));
        }
        parentItem.childItems.get(startingIntent.getIntExtra(ITEM_VALUE, 0)).accessoryIcon = R.drawable.checkbox_on;
        datasource.setDataItems(new DisplayGroupItem[]{parentItem});
    }

    private void showSettings() {
        project = new DisplayGroupItem(null);

        project.childItems.addAll(Arrays.asList(new DisplayItem[]{
//                        itemWithNameAndTag(R.string.settings_units, R.string.key_units),
//				itemWithNameAndTag(R.string.settings_websearch, R.string.key_websearch),
//				itemWithNameAndTag(R.string.settings_websearch_url, R.string.key_websearch_url),
                        itemWithNameAndTag(R.string.settings_about, R.string.key_about),
                        itemWithNameAndTag(R.string.set_project,R.string.key_project),
                        itemWithNameAndTag(R.string.place_initial_titile,R.string.key_initialposition),
                        itemWithNameAndTag(R.string.settings_serveraddress,R.string.key_serveraddress),
                     //   itemWithNameAndTag(R.string.login_account,R.string.key_userRigister),
                        itemWithNameAndTag(R.string.down_fire,R.string.key_down_fire),
                        itemWithNameAndTag(R.string.login_out,R.string.key_userOutLogin)

//				itemWithNameAndTag(R.string.settings_tutorial, R.string.settings_tutorial)
                }
        ));
//		websearchUrl = project.childItems.get(2);  //IndexOutOfBoundsException所以在这里注释此行代码

        view = new DisplayGroupItem(getString(R.string.settings_section_view));
        view.childItems.addAll(Arrays.asList(new DisplayItem[]{
//                itemWithNameAndTag(R.string.settings_navigation_buttons, R.string.key_navigation_buttons),
//                itemWithNameAndTag(R.string.settings_underground_button, R.string.key_underground_button),
                itemWithNameAndTag(R.string.settings_myposition, R.string.key_my_position),
//                itemWithNameAndTag(R.string.settings_gpstrail, R.string.key_gps_trail),
                //itemWithNameAndTag(R.string.settings_sunlight, R.string.key_sunlight),
                itemWithNameAndTag(R.string.menuButton_slideRight, R.string.key_menubutton_slide_right),
                itemWithNameAndTag(R.string.menuButton_slideUp, R.string.key_menubutton_slide_up)
        }));
        for (int i = 0; i < project.childItems.size(); i++) {
            updateItemDisplay(project.childItems.get(i));
        }

        for (DisplayItem item : view.childItems) {
            updateItemDisplay(item);
        }
        datasource.setDataItems(new DisplayGroupItem[]{project, view});
    }

    private DisplayItem itemWithNameAndTag(int name, int tag) {
        DisplayItem item = new DisplayItem(name);
        item.tag = tag;
        return item;
    }

    private void updateItemDisplay(DisplayItem item) {
        String key = getString(item.tag);
        switch (item.tag) {
            case R.string.key_units:
                item.subTitle = prefs.getInt(key, 0) == 0 ? getString(R.string.settings_units_meters) : getString(R.string.settings_units_feet);
                break;
            case R.string.key_websearch: {
                boolean value = prefs.getBoolean(key, true);
                item.subTitle = value ? getString(R.string.settings_websearch_on) : getString(R.string.settings_websearch_off);
                item.accessoryIcon = value ? R.drawable.checkbox_on : R.drawable.checkbox_off;
                websearchUrl.disabled = value == false;
                break;
            }
            case R.string.key_websearch_url:
                item.subTitle = prefs.getString(key, AppLinks.getDefaultSearchServer());
                break;
            case R.string.key_about:
                try {
                    PackageInfo pInfo = TEApp.getAppContext().getPackageManager().getPackageInfo(TEApp.getAppContext().getPackageName(), 0);
                    String fullVersion = String.format(TEApp.getAppContext().getString(R.string.version_string), pInfo.versionName);
                    item.subTitle = fullVersion;
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.string.key_navigation_buttons: {
                int value = prefs.getInt(key, 0);
                item.subTitle = value == 0 ? getString(R.string.settings_navigation_buttons_on) : getString(R.string.settings_navigation_buttons_off);
                break;
            }
            case R.string.key_underground_button: {
                boolean value = prefs.getBoolean(key, false);
                item.subTitle = value ? getString(R.string.settings_underground_button_on) : getString(R.string.settings_underground_button_off);
                item.accessoryIcon = value ? R.drawable.checkbox_on : R.drawable.checkbox_off;
                break;
            }
            case R.string.key_my_position: {
                boolean value = prefs.getBoolean(key, true);
                item.subTitle = value ? getString(R.string.settings_myposition_on) : getString(R.string.settings_myposition_off);
                item.accessoryIcon = value ? R.drawable.checkbox_on : R.drawable.checkbox_off;
                break;
            }
            case R.string.key_gps_trail: {
                boolean value = prefs.getBoolean(key, true);
                item.subTitle = value ? getString(R.string.settings_gpstrail_on) : getString(R.string.settings_gpstrail_off);
                item.accessoryIcon = value ? R.drawable.checkbox_on : R.drawable.checkbox_off;
                break;
            }
            case R.string.key_sunlight: {
                boolean value = prefs.getBoolean(key, true);
                item.subTitle = value ? getString(R.string.settings_sunlight_on) : getString(R.string.settings_sunlight_off);
                item.accessoryIcon = value ? R.drawable.checkbox_on : R.drawable.checkbox_off;
                break;
            }
            case R.string.key_menubutton_slide_right: {
                int value = prefs.getInt(key, 2);
                item.subTitle = MainButtonDragGestures.instance.getActionNames()[value];
                break;
            }
            case R.string.key_menubutton_slide_up: {
                int value = prefs.getInt(key, 1);
                item.subTitle = MainButtonDragGestures.instance.getActionNames()[value];
                break;
            }
            case R.string.key_project:  //选择加载地图
            {
                item.subTitle = "选择地图";
                break;
            }
            case R.string.key_initialposition: {
                String iposition = getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(USER_INITIAL_LOCATION, "");
                if (!iposition.isEmpty()) {
                    item.subTitle = iposition;
                }
                break;
            }
            //ip地址配置
            case R.string.key_serveraddress:
            {
                item.subTitle="";
                break;
            }
        }
    }

    public static final String DISABLE_BACK_BUTTON = "com.skyline.terraexpolrer.ProjectsTool.DISABLE_BACK_BUTTON";

    /**
     * 设置界面各条内容的跳转
     * @param packedPosition
     */
    private void didSelectRowAtIndexPath(long packedPosition) {
        DisplayItem item = datasource.getItemForPath(packedPosition);
        // we in item edit mode
        if (item.tag == 0) {
            setResult(RESULT_OK, new Intent().putExtra(ITEM_VALUE, ExpandableListView.getPackedPositionChild(packedPosition)));
            finish();
            return;
        }

        String key = getString(item.tag);
        Object newValue = null;

        switch (item.tag) {
            case R.string.key_units: {
                showListEditor(item, new String[]{getString(R.string.settings_units_meters), getString(R.string.settings_units_feet)});
                break;
            }
            case R.string.key_websearch_url: {
                showTextEditor(R.string.settings_websearch_url, item);
                break;
            }
            //设置关于gis的说明
            case R.string.key_about: {
                ToolManager.INSTANCE.openTool(AboutTool.class.getName());
                break;
            }
            case R.string.settings_tutorial: {
                ToolManager.INSTANCE.openTool(TutorialTool.class.getName());
                break;
            }
            case R.string.key_navigation_buttons: {
                showListEditor(item, new String[]{getString(R.string.settings_navigation_buttons_on), getString(R.string.settings_navigation_buttons_off)});
                break;
            }
            case R.string.key_underground_button:
            case R.string.key_websearch:
            case R.string.key_my_position:
            case R.string.key_sunlight:
            case R.string.key_gps_trail: {
                prefs.edit().putBoolean(key, !prefs.getBoolean(key, true)).apply();
                newValue = prefs.getBoolean(key, true);
                break;
            }
            case R.string.key_menubutton_slide_right: {
                showListEditor(item, MainButtonDragGestures.instance.getActionNames(),
                        prefs.getInt(key, 2));
                break;
            }
            case R.string.key_menubutton_slide_up: {
                showListEditor(item, MainButtonDragGestures.instance.getActionNames(),
                        prefs.getInt(key, 1));
                break;
            }
            case R.string.key_project: {
                Intent intent = new Intent(TEApp.getCurrentActivityContext(),ProjectsActivity.class);
//                intent.putExtra(DISABLE_BACK_BUTTON, true);   //解除注释可去除项目界面的返回按钮
                TEApp.getCurrentActivityContext().startActivity(intent);
                break;

            }
            case R.string.key_initialposition: {
                showEditPosition();
                break;

            }
            //服务器地址及其端口配置
            case R.string.key_serveraddress:
            {
                Intent intent = new Intent(TEApp.getCurrentActivityContext(),ServerAddressActivity.class);
                TEApp.getCurrentActivityContext().startActivity(intent);
                break;
            }
            case R.string.key_userRigister:
                Intent intent = new Intent(TEApp.getCurrentActivityContext(),SignUp_Activity.class);
                TEApp.getCurrentActivityContext().startActivity(intent);
                break;
            //退出登录
            case R.string.key_userOutLogin:
                DbConfig config = new DbConfig(getApplicationContext());
                User user = config.getUser();
                user.setIsLogin(0);
                DbManager db = config.getDbManager();
                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Intent intent1 = new Intent(TEApp.getCurrentActivityContext(),LoginActivity.class);
                intent1.putExtra("FROM","main");
                TEApp.getCurrentActivityContext().startActivity(intent1);
                Intent intent2= new Intent();
                intent2.setAction("out_login");
                sendBroadcast(intent2);

                break;
            //地图文件下载
            case R.string.key_down_fire:

                startActivity(new Intent(getApplicationContext(),DownMapActivity.class));
                break;

        }
        if (newValue != null) {
            updateItemDisplay(item);
            datasource.reloadData();
            sendSettingChangedIntent(item.tag, newValue);
        }
    }

    private void showTextEditor(int title, final DisplayItem item) {
        ModalDialog dlg = new ModalDialog(title, new ModalDialogDelegateBase() {
            @Override
            public void modalDialogDidDismissWithOk(ModalDialog dlg) {
                prefs.edit().putString(getString(item.tag), dlg.getTextField().getText().toString()).apply();
                updateItemDisplay(item);
                sendSettingChangedIntent(item.tag, item.subTitle);
            }
        });
        dlg.setContentTextField(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI, item.subTitle);
        dlg.show();
    }

    private void sendSettingChangedIntent(int settingTag, Object newValue) {
        Intent intent = new Intent(SettingsTool.SettingChanged.getAction(0));
        intent.putExtra(SettingsTool.SETTING_NAME, settingTag);
        if (newValue instanceof Boolean)
            intent.putExtra(SettingsTool.SETTING_VALUE, (Boolean) newValue);
        else if (newValue instanceof Integer)
            intent.putExtra(SettingsTool.SETTING_VALUE, (Integer) newValue);
        else if (newValue instanceof String)
            intent.putExtra(SettingsTool.SETTING_VALUE, (String) newValue);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void showListEditor(DisplayItem item, String[] values) {
        showListEditor(item, values, -1);
    }

    private void showListEditor(DisplayItem item, String[] values, int currentValue) {

        if (currentValue == -1)
            currentValue = prefs.getInt(getString(item.tag), 0);

        Intent intent = new Intent(this, this.getClass());
        intent.putExtra(ITEM_NAME, item.name)
                .putExtra(ITEM_TAG, item.tag)
                .putExtra(ITEM_VALUE, currentValue)
                .putExtra(ITEM_VALUE_SET, values);
        startActivityForResult(intent, item.tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            prefs.edit().putInt(getString(requestCode), data.getIntExtra(ITEM_VALUE, 0)).apply();
            sendSettingChangedIntent(requestCode, data.getIntExtra(ITEM_VALUE, 0));
        }
    }
    private void showEditPosition()
    {
        String iposition=getSharedPreferences(SettingsTool.PREFERENCES_NAME,Context.MODE_PRIVATE).getString(USER_INITIAL_LOCATION,"");

        ModalDialog modalDialog=new ModalDialog(R.string.place_initial_titile,modalDialogDelegate);
        modalDialog.setContentTextField(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE, iposition);
        modalDialog.setTag(1);
        modalDialog.show();
    }
    //服务器IP，及其端口配置

}