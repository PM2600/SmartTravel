package com.example.smart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart.bean.ResultBean;
import com.example.smart.entity.User;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private String ip = "10.132.125.37:8081";
    private Button loginBtn, registerBtn;
    private EditText edtname, edtpsw;
    private TextView privacy;
    private CheckBox cbread;
    private Intent intent = null;
    private boolean flag = false;

    public static void closeStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog().build());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.btn_login);
        registerBtn = findViewById(R.id.btn_register);
        edtname = findViewById(R.id.edt_name);
        edtpsw = findViewById(R.id.edt_psw);
        cbread = findViewById(R.id.cbread);

        privacy = findViewById(R.id.privacy);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LoginActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbread.isChecked()){
                    userLogin();
                }else{
                    Toast.makeText(getApplicationContext(), "请勾选同意协议", Toast.LENGTH_LONG).show();
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });

    }

    private void userLogin(){
        String name = edtname.getText().toString();
        String pwd = edtpsw.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", name);
            jsonObject.put("password", pwd);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //MediaType mMediaType = MediaType.parse("application/json; charset=utf-8");
        //RequestBody requestBody = RequestBody.create(mMediaType, jsonObject.toString());

        FormBody formBody = new FormBody.Builder()
                .add("username",name)
                .add("password", pwd)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("http://" + ip + "/user/login")
                .build();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG：", "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String loginBody  = response.body().string();
                    Log.e("TAG", loginBody);
                    Gson gson = new Gson();
                    ResultBean resultBean = gson.fromJson(loginBody, ResultBean.class);
                    if(resultBean.getState().equals("200")){
                        Log.e("TAG", "登录成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                            }
                        });

                        FormBody formBody = new FormBody.Builder()
                                .add("username", name)
                                .build();

                        Request request = new Request.Builder()
                                .post(formBody)
                                .url("http://" + ip + "/user/getinfo")
                                .build();

                        OkHttpClient client = new OkHttpClient();
                        okhttp3.Call call2 = client.newCall(request);
                        call2.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("TAG：", "请求失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String info  = response.body().string();
                                //Log.e("TAG", info);
                                Gson gson = new Gson();
                                User user = gson.fromJson(info, User.class);

                                SharedPreferences.Editor editor= getSharedPreferences("user_token", MODE_PRIVATE).edit();
                                editor.putString("uid", user.getUid().toString());
                                editor.putString("username", name);
                                editor.putString("password", pwd);
                                editor.putString("phone", user.getPhone());
                                editor.putString("nickname", user.getNickname());
                                editor.putString("address", user.getAddress());
                                editor.putString("sex", user.getSex() == 1 ? "男" : "女");
                                editor.apply();
                            }
                        });


                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Looper.prepare();
                        Log.e("TAG", "用户名或密码错误");
                        Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });

    }

    private void userRegister(){
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

