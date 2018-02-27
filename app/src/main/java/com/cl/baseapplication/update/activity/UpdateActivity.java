package com.cl.baseapplication.update.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cl.baseapplication.update.data.UpdateInfo;
import com.cl.baseapplication.update.helper.impl.BaseUpdate2HelperImpl;


/**
 * @author admin
 */
public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = "UpdateActivity.class";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent() != null && getIntent().getExtras() != null) {
            BaseUpdate2HelperImpl update2Helper = new BaseUpdate2HelperImpl(this);

            update2Helper.onCheckUpdateSuccess(
                    (UpdateInfo) getIntent().getExtras().getSerializable("updateInfo"),
                    getIntent().getExtras().getBoolean("isShow"));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
