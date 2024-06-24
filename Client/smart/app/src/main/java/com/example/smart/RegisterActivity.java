package com.example.smart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.bean.ResultBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private String ip = "10.132.125.37:8081";
    private EditText edtname, edtpassword, edtnickname, edtphone, edtaddress;
    private Button Btnregister;
    private RadioButton rb_man, rb_woman;
    private RadioGroup rg_sex;
    private Intent intent;
    String sex = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister);

        edtname = findViewById(R.id.et_name);
        edtpassword = findViewById(R.id.et_password);
        edtnickname = findViewById(R.id.et_nickname);
        edtphone = findViewById(R.id.et_phone);
        //edtaddress = findViewById(R.id.et_address);

        //Btnlogin = findViewById(R.id.btn_login2);
        Btnregister = findViewById(R.id.btn_register2);

        rb_man = findViewById(R.id.rb_man);
        rb_woman = findViewById(R.id.rb_woman);
        rg_sex = findViewById(R.id.rg_sex);

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId==rb_man.getId()) {
                    sex = "1";
                }else {
                    sex = "0";
                }
            }
        });

        Btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtname.getText().toString();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nickname = edtnickname.getText().toString();
                if(TextUtils.isEmpty(nickname)){
                    Toast.makeText(getApplicationContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = edtphone.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = edtpassword.getText().toString();
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
//                String address = edtaddress.getText().toString();
//                if(TextUtils.isEmpty(address)){
//                    Toast.makeText(getApplicationContext(), "请输入地址", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                FormBody formBody = new FormBody.Builder()
                        .add("username",username)
                        .add("nickname", nickname)
                        .add("phone", phone)
                        .add("password", password)
                        .add("sex", sex)
                        .build();

                Request request = new Request.Builder()
                        .post(formBody)
                        .url("http://" + ip + "/user/register")
                        .build();

                OkHttpClient client = new OkHttpClient();
                okhttp3.Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("请求情况：", "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()) {
                            String loginBody = response.body().string();
                            Log.e("响应信息", loginBody);

                            Gson gson = new Gson();
                            ResultBean resultBean = gson.fromJson(loginBody, ResultBean.class);
                            if (resultBean.getState().equals("200")) {
                                Looper.prepare();
                                Log.e("登录状态", "注册成功");
                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                                Looper.loop();
                                intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else if(resultBean.getState().equals("301")){
                                Looper.prepare();
                                Log.e("登录状态", "用户名已存在");
                                Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }else {
                                Looper.prepare();
                                Log.e("登录状态", "注册失败");
                                Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        }
                    }
                });


            }
        });


    }

}
