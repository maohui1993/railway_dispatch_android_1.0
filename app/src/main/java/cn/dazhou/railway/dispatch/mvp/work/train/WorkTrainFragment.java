package cn.dazhou.railway.dispatch.mvp.work.train;

import android.view.View;
import android.widget.TextView;

import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.railway.dispatch.AppApplication;
import cn.dazhou.railway.dispatch.R;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public class WorkTrainFragment extends BaseFragment implements WorkTrainContract.View {
    private WorkTrainContract.Presenter mPresenter;

    @Override
    protected void prepare() {
        mPresenter = new WorkTrainPresenter(getContext(), this);
    }

    @Override
    public View initView(View root) {
        ((TextView)(root.findViewById(R.id.tx_train))).setText(AppApplication.getInstance().info.getTrainId());
        root.findViewById(R.id.bt_unregister).setOnClickListener(mPresenter);
        root.findViewById(R.id.bt_call).setOnClickListener(mPresenter);
        return root;
    }
}
