package com.example.smart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smart.R;

import com.example.smart.bean.ResultBean;
import com.google.gson.Gson;
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

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private String ip = "10.132.125.37:8081";
    private ImageButton btn_back;
    private EditText et_nick;
    private EditText et_phone;
    private EditText et_email;
    private EditText et_address;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private RadioGroup rg_sex;
    private Button btn_update;
    private Button btn_clear;
    private String sex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();
    }

    private void initView() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        et_nick = (EditText) findViewById(R.id.et_nick);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_clear = (Button) findViewById(R.id.btn_clear);

        SharedPreferences sp = this.getSharedPreferences("user_token", MODE_PRIVATE);
        et_nick.setText(sp.getString("nickname", ""));
        et_phone.setText(sp.getString("phone", ""));
        et_address.setText(sp.getString("address", ""));
        //rg_sex.check(Integer.parseInt(sp.getString("sex", "")));

        btn_back.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_man.isChecked()) {
                    sex = "1";
                } else {
                    sex = "0";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_update:
                UpdateInfo();
                break;
            case R.id.btn_clear:
                // 清空
                et_nick.setText("");
                et_phone.setText("");
                et_phone.setText("");
                et_address.setText("");
                sex = "";
                break;
        }
    }

    private void UpdateInfo() {
        //输入校验
        String nickname = et_nick.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sex)){
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        }
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_man.isChecked()) {
                    sex = "1";
                } else {
                    sex = "0";
                }
            }
        });

        SharedPreferences sp = this.getSharedPreferences("user_token", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        SharedPreferences.Editor editor= getSharedPreferences("user_token", MODE_PRIVATE).edit();
        editor.clear();

        editor.putString("uid", uid);
        editor.putString("sex", uid.equals("1") ? "男" : "女");
        editor.putString("nickname", nickname);
        editor.putString("phone", phone);
        editor.putString("address", address);
        editor.apply();


        FormBody formBody = new FormBody.Builder()
                .add("uid", uid)
                .add("nickname", nickname)
                .add("phone", phone)
                .add("address", address)
                .add("sex", sex)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("http://" + ip + "/user/updateinfo")
                .build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {  // 异步请求
            // 请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("请求失败",e.getMessage());
            }
            // 响应成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 成功
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String info = response.body().string();
                    ResultBean result = gson.fromJson(info, ResultBean.class);
                    String code = result.getState();
                    if (code.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"用户信息修改成功", Toast.LENGTH_LONG).show();
                                setResult(200);
                                finish();
                            }
                        });
                    }
                }
            }
        });

    }
}
