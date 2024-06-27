package com.example.smart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smart.bean.ResultBean;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReleaseActivity extends AppCompatActivity {

    private String ip = "10.132.125.37:8081";
    private Button addphoto;
    private Intent intent = null;
    private ImageView img;
    private Button btn_release;
    private EditText dy_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        addphoto = (Button)findViewById(R.id.addphoto);
        img = (ImageView) findViewById(R.id.img);
        btn_release = findViewById(R.id.btn_release);
        dy_text = findViewById(R.id.dy_text);

        btn_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","提交按钮");
                SharedPreferences sp = getSharedPreferences("user_token", MODE_PRIVATE);
                String uid = sp.getString("uid", "");
                String dytext = dy_text.getText().toString();
                FormBody formBody = new FormBody.Builder()
                        .add("uid", uid)
                        .add("dytext", dytext)
                        .build();

                Request request = new Request.Builder()
                        .post(formBody)
                        .url("http://" + ip + "/dynamic/release")
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("请求失败",e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String info = response.body().string();
                            ResultBean result = gson.fromJson(info, ResultBean.class);
                            String code = result.getState();
                            if (code.equals("200")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"动态发布成功", Toast.LENGTH_LONG).show();
                                        setResult(200);
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        checkPermission();
        initListener();
    }

    private void initListener() {

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ReleaseActivity.this)
//                        .setIcon(R.mipmap.picture)
                        .setMessage("插入图片")
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                takePhoto();
                            }
                        }).setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseFromAlbum();
                    }
                }).create().show();
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(ReleaseActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 执行到这里表示没有访问权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReleaseActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(ReleaseActivity.this,"禁止访问",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(ReleaseActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
            }
        } else {
            takePhoto();
        }
    }


    public void takePhoto() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 200);   //有数据的返回
    }

    public void chooseFromAlbum() {
        intent = new Intent();
        intent.setType("image/*");   //设定类型为image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);//选中相片后返回本Activity
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();  //获取数据
            ContentResolver contentResolver = getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;
            if (requestCode == 100) {
                try {
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));  //将对象存入Bitmap中
                    saveBitmap(bitmap, "test");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == 200) {
                try {
                    if (uri != null){
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);  // 根据Uri获取Bitmap图片
                    } else{  // 从Bundle里面获取Bitmap图片
                        extras = data.getExtras();
                        bitmap = extras.getParcelable("data");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int imgWidth = bitmap.getWidth();  //获取图片宽度
            int imgHeight = bitmap.getHeight();  // 获取图片高度
            double partion = imgWidth * 1.0 / imgHeight;
            double sqrtLength = Math.sqrt(partion * partion + 1);

            /**
             * 设置图片新的缩略图大小
             */
            double newImgW = 680 * (partion / sqrtLength);
            double newImgH = 680  * (1 / sqrtLength);
            float scaleW = (float) (newImgW / imgWidth);
            float scaleH = (float) (newImgH / imgHeight);
            Matrix mx = new Matrix();

            /**
             * 对原图片进行缩放
             */
            mx.postScale(scaleW, scaleH);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
            bitmap = getBitmapWidth(bitmap);
            img.setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmapWidth(Bitmap bitmap) {
        float frameSize = 0.2f;
        Matrix matrix = new Matrix();
        // 用来做底图
        Bitmap mbitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 设置底图为画布
        Canvas canvas = new Canvas(mbitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f / (bitmap.getWidth());
        float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f / (bitmap.getHeight());
        matrix.reset();
        matrix.postScale(scale_x, scale_y);

        // 减去边框的大小
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        // 绘制底图边框
        canvas.drawRect(new Rect(0, 0, mbitmap.getWidth(), mbitmap.getHeight()),paint);
        // 绘制灰色边框
        paint.setColor(Color.GRAY);
        canvas.drawRect(new Rect((int) (frameSize), (int) (frameSize), mbitmap.getWidth() - (int) (frameSize), mbitmap.getHeight() - (int) (frameSize)), paint);
        canvas.drawBitmap(bitmap, frameSize + 2, frameSize + 2, paint);
        return mbitmap;
    }

    private void saveBitmap(Bitmap bitmap,String bitName) throws IOException
    {
        File file = new File(Environment.getExternalStorageDirectory() + "/sdcard/DCIM/Camera/" + bitName);
        Log.e("TAG", Environment.getExternalStorageDirectory().toString());
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
