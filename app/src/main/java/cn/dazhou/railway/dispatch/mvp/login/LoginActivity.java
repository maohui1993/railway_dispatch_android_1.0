package cn.dazhou.railway.dispatch.mvp.login;

import android.os.Bundle;

import cn.dazhou.railway.dispatch.R;
import cn.dazhou.commonlib.BaseActivity;
import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.commonlib.util.ActivityUtils;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            mFragment = LoginFragment.newInstance(R.layout.fragment_login, LoginFragment.class);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }
}
