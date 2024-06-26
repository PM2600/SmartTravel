package com.example.smart.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smart.DragFloatActionButton;
import com.example.smart.NewsWebViewActivity;
import com.example.smart.R;
import com.example.smart.ReleaseActivity;
import com.example.smart.bean.ResultDynamic;
import com.example.smart.bean.ResultNews;
import com.example.smart.entity.Dynamic;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DynamicFragment extends BaseFragment {

    private String ip = "10.132.125.37:8081";
    private TextView tv;
    private DragFloatActionButton mBtn;
    private List<Dynamic> ls = new ArrayList<>();;
    private ListView lv;
    private DynamicAdapter dynamicAdapter;
    private Button send_btn;
    private Intent intent = null;
    private View view;
    int[] images = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five};

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(getActivity(), getActivity().getClass()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public View initView() {
        view = View.inflate(getActivity(), R.layout.fragment_dynamic,null);

        ls.clear();
        dynamicAdapter = new DynamicAdapter();
        lv = (ListView) view.findViewById(R.id.dy_listView);
        tv = (TextView) view.findViewById(R.id.tv);
        send_btn = view.findViewById(R.id.send_btn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), ReleaseActivity.class);
                startActivityForResult(intent, 200);
            }
        });


        Request request = new Request.Builder()
                .url("http://" + ip + "/dynamic/getdynamic")
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
                    ResultDynamic dys = gson.fromJson(Body, ResultDynamic.class);

                    if (dys.getState() == 200) {
                        Log.e("TAG", "请求动态成功");
                        ls = dys.getRows();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lv.setAdapter(dynamicAdapter);
                            }
                        });

                    } else {
                        Looper.prepare();
                        Log.e("TAG", "请求动态失败");
                        Toast.makeText(getActivity(), "请求动态失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }


    public class DynamicAdapter extends BaseAdapter {

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
                view = View.inflate(getContext(), R.layout.dynamic_item,null);
                holder = new DynamicAdapter.ViewHolder();

                holder.dy_name = view.findViewById(R.id.dy_name);
                holder.dy_date = view.findViewById(R.id.dy_date);
                holder.dy_text = view.findViewById(R.id.dy_text);
                holder.dy_addr = view.findViewById(R.id.dy_addr);
                holder.dy_like = view.findViewById(R.id.dy_like);
                holder.dy_img = view.findViewById(R.id.dy_img);

                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            holder.dy_name.setText(ls.get(i).getNickname());
            holder.dy_date.setText(ls.get(i).getDycommit_time());
            holder.dy_text.setText(ls.get(i).getDytext());
            holder.dy_addr.setText(ls.get(i).getAddress());
            holder.dy_like.setText(ls.get(i).getDylike());
            holder.dy_img.setImageResource(images[i]);

            holder.dy_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap0 = ((BitmapDrawable)holder.dy_img.getDrawable()).getBitmap();
                    bigImageLoader(bitmap0);
                }
            });

//            holder.dy_name.setText("a");
//            holder.dy_date.setText("a");
//            holder.dy_text.setText("a");
//            holder.dy_addr.setText("a");
//            holder.dy_like.setText("a");

            return view;
        }
        class ViewHolder{
            TextView dy_name;
            TextView dy_date;
            TextView dy_text;
            TextView dy_addr;
            TextView dy_like;
            ImageView dy_img;
        }

    }

    private void bigImageLoader(Bitmap bitmap){
        final Dialog dialog = new Dialog(getActivity());
        ImageView image = new ImageView(getContext());
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
