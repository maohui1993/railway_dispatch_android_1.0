package cn.dazhou.railway.dispatch.mvp;

import android.os.Bundle;

import cn.dazhou.commonlib.BaseActivity;
import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.commonlib.util.ActivityUtils;
import cn.dazhou.railway.dispatch.AppApplication;
import cn.dazhou.railway.dispatch.R;
import cn.dazhou.railway.dispatch.mvp.work.controllercenter.WorkControllerCenterFragment;
import cn.dazhou.railway.dispatch.mvp.work.station.WorkStationFragment;
import cn.dazhou.railway.dispatch.mvp.work.train.WorkTrainFragment;

public class WorkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            switch (AppApplication.getInstance().info.getType()) {
                case 0:
                    mFragment = WorkTrainFragment.newInstance(R.layout.fragment_work_train, WorkTrainFragment.class);
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
                    break;
                case 1:
                    mFragment = WorkStationFragment.newInstance(R.layout.fragment_work_train, WorkStationFragment.class);
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
                    break;
                case 2:
                    mFragment = WorkControllerCenterFragment.newInstance(R.layout.fragment_work_train, WorkControllerCenterFragment.class);
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

}
