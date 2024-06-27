package com.example.smart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.bean.ResultFood;
import com.example.smart.bean.ResultViews;
import com.example.smart.entity.Food;
import com.example.smart.entity.Views;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoodActivity extends AppCompatActivity {

    private String ip = "10.132.125.37:8081";
    private ListView lv;
    private List<Food> ls = new ArrayList<>();
    private FoodAdapter foodAdapter;
    Map<String, Integer> foodmap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);


        foodmap.clear();
        ls.clear();
        foodAdapter = new FoodAdapter();
        lv = (ListView) findViewById(R.id.food_listView);

        foodmap.put("风干牛肉干", R.drawable.nrg);
        foodmap.put("乌珠穆沁羊肉", R.drawable.yr);
        foodmap.put("敖汉小米", R.drawable.xm);
        foodmap.put("扎兰屯黑木耳", R.drawable.mer);
        foodmap.put("正蓝旗奶豆腐", R.drawable.ndf);
        foodmap.put("马奶酒", R.drawable.mnj);

        Request request = new Request.Builder()
                .url("http://" + ip + "/food/getfoods")
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
                if(response.isSuccessful()) {
                    String Body = response.body().string();
                    Log.e("TAG", Body);
                    Gson gson = new Gson();
                    ResultFood fos = gson.fromJson(Body, ResultFood.class);

                    if (fos.getState() == 200) {
                        Log.e("TAG", "请求特产成功");
                        ls = fos.getRows();


                        FoodActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                lv.setAdapter(foodAdapter);
                            }
                        });



                    } else {
                        Looper.prepare();
                        Log.e("TAG", "请求特产失败");
                        Toast.makeText(getApplicationContext(), "请求特产失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });
    }

    public class FoodAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ls != null ? ls.size() : 0;
            //return 10;
        }

        @Override
        public Object getItem(int i) {
            return ls.get(i);
            //return "a";
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            FoodAdapter.ViewHolder holder;

            if (view == null) {
                //view = m.inflate(R.layout.view_item,null);
                view = View.inflate(getApplicationContext(), R.layout.food_item,null);
                holder = new ViewHolder();

                holder.food_text = view.findViewById(R.id.food_text);
                holder.food_like = view.findViewById(R.id.food_like);
                holder.food_img = view.findViewById(R.id.food_img);

                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

//            holder.view_text.setText("a");
//            holder.view_addr.setText("a");
//            holder.view_like.setText("a");

            holder.food_text.setText(ls.get(i).getFoodtext());
            holder.food_like.setText(ls.get(i).getFoodlike() + "");

            holder.food_img.setImageResource(foodmap.get(ls.get(i).getFoodtext()));
            holder.food_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap0 = ((BitmapDrawable)holder.food_img.getDrawable()).getBitmap();
                    bigImageLoader(bitmap0);
                }
            });

            return view;
        }
        class ViewHolder{
            TextView food_text;
            TextView food_like;
            ImageView food_img;
        }

    }

    private void bigImageLoader(Bitmap bitmap){
        final Dialog dialog = new Dialog(this);
        ImageView image = new ImageView(this);
        image.setImageBitmap(bitmap);
        dialog.setContentView(image);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });
    }


}
