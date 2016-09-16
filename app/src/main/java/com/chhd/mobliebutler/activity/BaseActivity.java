package com.chhd.mobliebutler.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.chhd.mobliebutler.R;

/**
 * Created by CWQ on 2016/8/25.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.set_aty_exit_aty_enter, R.anim.set_aty_exit_aty_exit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.set_aty_exit_aty_enter, R.anim.set_aty_exit_aty_exit);
    }
}
