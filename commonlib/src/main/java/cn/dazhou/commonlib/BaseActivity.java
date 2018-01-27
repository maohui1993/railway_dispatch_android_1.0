package cn.dazhou.commonlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.dazhou.commonlib.util.ActivityUtils;

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    private TextView mTitleTx;

    protected BaseFragment mFragment;
    private static ArrayList<BaseActivity> sActivities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sActivities.add(this);
        setContentView(R.layout.activity_base);

        mTitleTx = findViewById(R.id.tx_title);
        mToolbar = findViewById(R.id.my_toolbar);
        if (hasActionBar()) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        super.onDestroy();
    }

    public void setTitle(String title) {
//        mToolbar.setTitle(title);
        mTitleTx.setText(title);
    }

    protected abstract boolean hasActionBar();

    protected void addLayoutToBase(@LayoutRes int layoutResID){

        FrameLayout llContent = (FrameLayout) findViewById(R.id.contentFrame); //v_content是在基类布局文件中预定义的layout区域
//        通过LayoutInflater填充基类的layout区域
        View v = LayoutInflater.from(this).inflate(layoutResID, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        llContent.addView(v, params);
    }

    /**
     * 初始化fragment
     * @param viewId layout渲染到指定的viewgroup下
     * @param layoutId 用来渲染到activity的layout
     */
    protected void initContent(@IdRes int viewId, @LayoutRes int layoutId, Class clazz) {
        mFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(viewId);
        if (mFragment == null) {
            mFragment = BaseFragment.newInstance(layoutId, clazz);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }

    public static void startActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void exit() {
        for (BaseActivity activity : sActivities) {
            activity.finish();
        }
    }

    public static BaseActivity getTopActivity() {
        return sActivities.get(sActivities.size() - 1);
    }
}
