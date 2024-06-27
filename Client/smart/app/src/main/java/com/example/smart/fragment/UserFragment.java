package com.example.smart.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.smart.AdviseActivity;
import com.example.smart.LoginActivity;
import com.example.smart.MainActivity;
import com.example.smart.ManagerActivity;
import com.example.smart.PrivacyActivity;
import com.example.smart.R;
import com.example.smart.UserInfoActivity;


public class UserFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = UserFragment.class.getSimpleName();

    private TextView tv_nickname, tv_sex, tv_address;
    private ImageButton user_img;
    private Button btn_out;
    private Intent intent = null;
    private LinearLayout ll_info;
    private LinearLayout ll_manager;
    private LinearLayout ll_privacy;
    private LinearLayout ll_advice;
    private Toolbar toolbar;
    private View view;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sp = getActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String nickname = sp.getString("nickname","未设置");
        String sex = sp.getString("sex","未设置");
        String address = sp.getString("address","未设置");
        Log.e("TAG", nickname);
        Log.e("TAG", sex);
        Log.e("TAG", address);

        // 把用户名显示到textView控件上
        tv_nickname.setText(nickname);
        tv_sex.setText(sex);
        tv_address.setText(address);
        startActivity(new Intent(getActivity(), getActivity().getClass()));
    }

    @Override
    public View initView() {
        Log.i(TAG, "用户中心的视图被实例化了");
        view = View.inflate(getContext(), R.layout.fragment_user, null);

        ll_advice = view.findViewById(R.id.ll_advice);
        ll_info = view.findViewById(R.id.ll_info);
        ll_manager = view.findViewById(R.id.ll_manager);
        ll_privacy = view.findViewById(R.id.ll_privacy);
        btn_out = view.findViewById(R.id.btn_out);
        user_img = view.findViewById(R.id.user_img);

        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_sex = view.findViewById(R.id.tv_sex);
        tv_address = view.findViewById(R.id.tv_address);

        ll_manager.setOnClickListener(this);
        ll_info.setOnClickListener(this);
        ll_privacy.setOnClickListener(this);
        ll_advice.setOnClickListener(this);

        btn_out.setOnClickListener(this);
        //user_img.setOnClickListener(this);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        // 读取本地保存用户的登录信息
        SharedPreferences sp = getActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String nickname = sp.getString("nickname","未设置");
        String sex = sp.getString("sex","未设置");
        String address = sp.getString("address","未设置");

        // 把用户名显示到textView控件上
        tv_nickname.setText(nickname);
        tv_sex.setText(sex);
        tv_address.setText(address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_info:
                intent = new Intent(getContext(), UserInfoActivity.class);
                startActivityForResult(intent, 200);
                return;
            case R.id.ll_manager:
                intent = new Intent(getContext(), ManagerActivity.class);
                break;
            case R.id.ll_privacy:
                intent = new Intent(getContext(), PrivacyActivity.class);
                break;
            case R.id.ll_advice:
                intent = new Intent(getContext(), AdviseActivity.class);
                break;
            case R.id.btn_out:
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE).edit();
                editor.clear(); // 清除本地保存的token值
                editor.apply();
                intent = new Intent(getContext(), LoginActivity.class);
                break;
//            case R.id.user_img:
//                break;
        }
        // 页面跳转
        startActivity(intent);
    }
}

