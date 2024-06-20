package com.example.smart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smart.bean.ResultBean;
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

    private Button loginBtn, registerBtn;
    private EditText edtname, edtpsw;
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
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);


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
                .url("http://10.132.125.37:8081/user/login")
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
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
//                        Looper.prepare();
//                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
//                        Looper.loop();

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

