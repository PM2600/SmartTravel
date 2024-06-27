package com.example.smart;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.bean.ResultDynamic;
import com.example.smart.bean.ResultViews;
import com.example.smart.entity.Dynamic;
import com.example.smart.entity.Views;
import com.example.smart.fragment.DynamicFragment;
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

public class ViewActivity extends AppCompatActivity {

    private String ip = "10.132.125.37:8081";
    private ListView lv;
    private List<Views> ls = new ArrayList<>();
    private ViewAdapter viewAdapter;
    Map<String, Integer> viewmap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        viewmap.clear();
        ls.clear();
        viewAdapter = new ViewAdapter();
        lv = (ListView) findViewById(R.id.view_listView);
        viewmap.put("呼伦贝尔大草原", R.drawable.hlber);
        viewmap.put("大召寺", R.drawable.dzs);
        viewmap.put("内蒙古博物院", R.drawable.bwg);
        viewmap.put("锡林郭勒大草原", R.drawable.xlgl);
        viewmap.put("成吉思汗陵旅游区", R.drawable.cjsh);
        viewmap.put("阿尔山国家森林公园", R.drawable.aers);

        Request request = new Request.Builder()
                .url("http://" + ip + "/view/getviews")
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
                    ResultViews vis = gson.fromJson(Body, ResultViews.class);

                    if (vis.getState() == 200) {
                        Log.e("TAG", "请求景点成功");
                        ls = vis.getRows();

                        ViewActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                lv.setAdapter(viewAdapter);
                            }
                        });


                    } else {
                        Looper.prepare();
                        Log.e("TAG", "请求景点失败");
                        Toast.makeText(getApplicationContext(), "请求景点失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });
    }

    public class ViewAdapter extends BaseAdapter {

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
            ViewHolder holder;

            if (view == null) {
                //view = m.inflate(R.layout.view_item,null);
                view = View.inflate(getApplicationContext(), R.layout.view_item,null);
                holder = new ViewHolder();

                holder.view_text = view.findViewById(R.id.view_text);
                holder.view_addr = view.findViewById(R.id.view_addr);
                holder.view_like = view.findViewById(R.id.view_like);
                holder.view_img = view.findViewById(R.id.view_img);

                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

//            holder.view_text.setText("a");
//            holder.view_addr.setText("a");
//            holder.view_like.setText("a");

            holder.view_text.setText(ls.get(i).getViewtext());
            holder.view_addr.setText(ls.get(i).getViewaddr());
            holder.view_like.setText(ls.get(i).getViewlike() + "");

            holder.view_img.setImageResource(viewmap.get(ls.get(i).getViewtext()));

            holder.view_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap0 = ((BitmapDrawable)holder.view_img.getDrawable()).getBitmap();
                    bigImageLoader(bitmap0);
                }
            });

            return view;
        }
        class ViewHolder{
            TextView view_text;
            TextView view_addr;
            TextView view_like;
            ImageView view_img;
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
