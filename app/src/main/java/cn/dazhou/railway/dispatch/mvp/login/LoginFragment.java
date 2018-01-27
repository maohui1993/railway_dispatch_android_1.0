package cn.dazhou.railway.dispatch.mvp.login;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.commonlib.util.ActivityUtils;
import cn.dazhou.railway.dispatch.R;

/**
 * @author Hooyee on 2018/1/26.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    private EditText mUsernameEdit;
    private EditText mPasswordEdit;
    private View mContentView;

    @Override
    protected void initAdapter() {

    }

    @Override
    public View initView(final View root) {
        root.findViewById(R.id.bt_login).setOnClickListener(mPresenter);
        mContentView = root.findViewById(R.id.content);
        mUsernameEdit = root.findViewById(R.id.edit_username);
        mPasswordEdit = root.findViewById(R.id.edit_password);

        root.setOnClickListener(mPresenter);

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ActivityUtils.isKeyboardShown(root)) {
//                    onSoftInputShow(Util.dip2px(getContext(), -50));
                } else {
//                    onSoftInputHide();
                }
            }
        });
        return root;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }
}
