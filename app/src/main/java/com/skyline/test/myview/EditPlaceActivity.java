package com.skyline.test.myview;


import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.EditFavoriteSelectIconActivity;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.terraexplorer.models.UI;


public class EditPlaceActivity extends MatchParentActivity {
    private EditText placeName=null;
    private TextView iconName=null;
    private TextView placePosition=null;
    private EditText content=null;
    private ImageView icon=null;
    private Switch isShow=null;
    private Button save=null;
    private FavoriteItem currentItem;
    private View nameContrainer;
    private View showOn3DContainer;
    private View iconContrainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_favorit1);
        placeName=(EditText) findViewById(R.id.editText_name);
        iconName=(TextView)findViewById(R.id.textView_iconname);
        content=(EditText)findViewById(R.id.editText_content);
        icon=(ImageView)findViewById(R.id.imageView_select);
        isShow=(Switch)findViewById(R.id.switch_show);
        save=(Button)findViewById(R.id.button_saveplace);
        placePosition=(TextView)findViewById(R.id.textView_placeposition);
        iconContrainer = (View) icon.getParent();
        showOn3DContainer = (View) isShow.getParent();
        String favoriteItemId = getIntent().getExtras().getString(FavoriteItem.FAVORITE_ID);
        int header;
        if (favoriteItemId == null) {
            header = R.string.title_activity_favorites;
            currentItem = new FavoriteItem();
            currentItem.name = getString(R.string.favorites_default_name);
        } else {
            header = R.string.title_activity_favorites;
            currentItem = FavoritesStorage.defaultStorage.getItem(favoriteItemId);
            if (currentItem == null) {
                Toast toast = Toast.makeText(this, getString(R.string.favorites_item_not_found), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
                return;
            }
        }
        iconContrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconTaped(view);
            }
        });
        isShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showOn3DValueChanged(compoundButton);
            }
        });
        showOn3DContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShow.setChecked(!isShow.isChecked());
                showOn3DValueChanged(isShow);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存数据。
                if(placeName.getText().toString()==null)
                {
                    ToastUtils.showShort("名称不能为空");
                }
                else
                {
                    currentItem.name=placeName.getText().toString();
                    if (content.getText().toString()!=null) {
                        currentItem.desc =content.getText().toString();
                    }
                    FavoritesStorage.defaultStorage.saveItem(currentItem);
                    finish();
                }

            }
        });
        isShow.setChecked(currentItem.showOn3D);
        placeName.setText(currentItem.name);
        content.setText(currentItem.desc);
        setIconNewValue(currentItem.icon);
        showOn3DValueChanged(isShow);
       UI.runOnRenderThread(new Runnable() {
           @Override
           public void run() {
//               placePosition.setText(String.format(String.valueOf(currentItem.position.getX()), "#.0000")+";"+String.valueOf(currentItem.position.getY()));
               placePosition.setText(String.format("[%.4f;%.4f]",currentItem.position.getX(), currentItem.position.getY()));
           }
       });

        UI.addHeader(header, R.drawable.favorits, this);
    }
    private void iconTaped(View sender) {
        Intent intent = new Intent(this, EditFavoriteSelectIconActivity.class);
        intent.putExtra(FavoriteItem.FAVORITE_ICON, currentItem.icon);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setIconNewValue(data.getExtras().getInt(FavoriteItem.FAVORITE_ICON));
        }
    }

    /**
     * 设置新的图标名称
     * @param iconId
     */
    private void setIconNewValue(int iconId) {
        currentItem.icon = iconId;
        iconName.setText(FavoritesStorage.defaultStorage.descriptionForIcon(currentItem.icon));
        icon.setImageResource(FavoritesStorage.defaultStorage.resourceForIcon(currentItem.icon));
    }
    private void showOn3DValueChanged(View sender) {

        Switch cliker = (Switch) sender;

        if (cliker.isChecked()) {
            iconContrainer.setClickable(true);
            iconContrainer.setAlpha(1);
            icon.setAlpha(255);
        } else {
            iconContrainer.setClickable(false);
            iconContrainer.setAlpha(.3f);
            icon.setAlpha((int) (255 * 0.3));

        }
        currentItem.showOn3D = cliker.isChecked();
    }
}
