package com.example.smart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class TakePhotoActivity extends AppCompatActivity {

    private String imgString = ""; //要上传的图片路径
    private String mFilePath="";  //拍照得到的原图保存的图片路径
    private Button btn_photo, btn_release;
    private TextView tv;
    private ImageView img;
    private Toolbar toolbar;
    Intent intent = null;

    private final int REQUEST_CODE_FROM_PHOTO = 2; //相册选取返回的requestCode
    private final int REQUEST_CODE_FROM_CAMERA = 1;//拍照返回的requestCode


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_photo = findViewById(R.id.send_btn);
        btn_release = findViewById(R.id.btn_release);
        img = findViewById(R.id.img);

        /**
         * 解决android7调用照相机后直接奔溃问题
         */

        btn_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","提交按钮");
                finish();
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            builder.detectFileUriExposure();
//        }
//        checkPermission();
//        initListener();


    }

    /**
     * 检查拍照权限,防止权限拒绝
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 执行到这里表示没有访问权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(TakePhotoActivity.this,"禁止访问",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
            }
        } else {
            takePhoto();
        }
    }

    private void initListener() {


        // 顶部返回
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 按钮点击事件,单击弹出AlertDialog对话框
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TakePhotoActivity.this)
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

    /**
     * 获取图片
     */
    public void takePhoto() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 200);   //有数据的返回
    }

    /**
     * 选择相册
     */
    public void chooseFromAlbum() {
        intent = new Intent();
        intent.setType("image/*");   //设定类型为image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);//选中相片后返回本Activity
    }


    /**
     * 重写onActivityResult方法：将返回的图片数据设置到ImageView上
     *
     * 参数说明：requestCode值：100 为打开系统相册选择相片,requestCode值：200为调用系统相机拍照
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

                    //saveBitmap(bitmap);

                } catch (FileNotFoundException e) {
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

    /**
     * 给图片加边框，并返回边框后的图片
     * @param bitmap
     * @return
     */
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

    public void saveBitmap(Bitmap bitmap){
        String sdCardDir = Environment.getExternalStorageDirectory() + "/fingerprintimages/";
        Log.e("TAG", sdCardDir);
        String tmplName = "dynamicImage";
        try {
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            File file = new File(sdCardDir, tmplName + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 通知图库更新
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                Uri.parse("file://" + "/sdcard/namecard/")));
    }


//    private void openCamera() {
//        // 获取SD卡路径
//        mFilePath = Environment.getExternalStorageDirectory().getPath();
//        // 保存图片的文件名
//        mFilePath = mFilePath + "/" + "IMG"+ Calendar.getInstance().getTime() +".png";
//        //android7.0以上版本
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//            takePhotoBiggerThan7((new File(mFilePath)).getAbsolutePath());
//        }else{
//            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri mUri = Uri.fromFile(new File(mFilePath));
//
//            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,mUri);
//            startActivityForResult(openCameraIntent,REQUEST_CODE_FROM_CAMERA);
//        }
////        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
//    }
//
//    private void takePhotoBiggerThan7(String absolutePath) {
//        Uri mCameraTempUri;
//        try {
//            ContentValues values = new ContentValues(1);
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//            values.put(MediaStore.Images.Media.DATA, absolutePath);
//            mCameraTempUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            if (mCameraTempUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//            }
//            startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
