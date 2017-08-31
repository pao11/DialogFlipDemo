package com.pao11.xyi.dialogflip;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pao11 on 2017/8/16.
 */


public class ForgetPasswordFragment extends Fragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.et_email)
    EditText etEmil;
    @BindView(R.id.btn_back)
    Button btnBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnBackListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_forget_password, container, false);
        unbinder = ButterKnife.bind(this, mView);
        btnBack.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    OnBackListener mCallback;

    @Override
    public void onClick(View view) {
        mCallback.goBack();
    }

    public interface OnBackListener {
        void goBack();
    }
}
