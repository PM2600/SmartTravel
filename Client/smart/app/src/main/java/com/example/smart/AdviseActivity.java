package com.example.smart;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smart.R;
import com.example.smart.bean.ResultBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdviseActivity extends AppCompatActivity implements View.OnClickListener {

    private String ip = "10.132.125.37:8081";
    private ImageButton btn_back;
    private EditText content;
    private Button btn_send;
    private Intent intent = null;


    // 更新主线程的UI
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        // 接收消息
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                String re = msg.obj.toString();
                Toast.makeText(getApplicationContext(),"反馈成功："+re,Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise);
        initView();
    }

    private void initView() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        content = (EditText) findViewById(R.id.content);
        btn_send = (Button) findViewById(R.id.btn_send);

        btn_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_send:
                SendAdavice();
                break;
        }
    }
    // 发送建议反馈到服务端
    private void SendAdavice() {
        String contentString = content.getText().toString().trim();
        if (TextUtils.isEmpty(contentString)) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取SharedPreferences对象
        SharedPreferences sp = this.getSharedPreferences("user_token", MODE_PRIVATE);
        String uid = sp.getString("uid","");

        FormBody formBody = new FormBody.Builder()
                .add("uid", uid)
                .add("content", contentString)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("http://" + ip + "/user/submit")
                .build();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Call call = client.newCall(request);

        call.enqueue(new Callback() {
            // 响应失败
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure",e.getMessage());
            }
            // 响应成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.e("TAG", result);
                    Gson gson = new Gson();
                    ResultBean resultBean = gson.fromJson(result, ResultBean.class);
                    if(resultBean.getState().equals("200")){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "提交建议成功", Toast.LENGTH_LONG).show();
                        finish();
                        Looper.loop();
                    }
                }
            }
        });
    }
}
