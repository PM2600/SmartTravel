package com.example.smart.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.smart.LoginActivity;
import com.example.smart.MainActivity;
import com.example.smart.NewsWebViewActivity;
import com.example.smart.R;

import com.example.smart.bean.JsonResult;
import com.example.smart.bean.ResultBean;
import com.example.smart.bean.ResultNews;
import com.example.smart.entity.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private String ip = "10.132.125.37:8081";
    private Banner banner;
    private List<Integer> images = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    List<News> ls = new ArrayList<>();
    private TextView tv;
    private ListView lv;
    private NewsAdapter newsAdapter;
    SimpleAdapter simpleAdapter;

    int[] imgs = {R.drawable.ic1, R.drawable.ic2, R.drawable.ic3, R.drawable.ic4, R.drawable.ic5, R.drawable.ic6, R.drawable.ic7, R.drawable.ic8, R.drawable.ic9};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home,container,false);
        banner = view.findViewById(R.id.banner);
        lv = (ListView) view.findViewById(R.id.new_listView);
        images.clear();
        title.clear();
        ls.clear();
//        images.add(R.drawable.image1);
//        images.add(R.drawable.image2);
//        images.add(R.drawable.image3);
//        images.add(R.drawable.image4);
        images.add(R.drawable.one);
        images.add(R.drawable.two);
        images.add(R.drawable.three);
        images.add(R.drawable.four);
        images.add(R.drawable.five);
        newsAdapter = new NewsAdapter();
        title.add("one");
        title.add("two");
        title.add("three");
        title.add("four");
        title.add("five");

        initView();
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    public void initData() {
        Request request = new Request.Builder()
                .url("http://" + ip + "/news/getnews")
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
                    ResultNews news = gson.fromJson(Body, ResultNews.class);
//                    News[] array = new Gson().fromJson(Body, News[].class);
//                    List<News> ls = Arrays.asList(array);
                    //List<News> ls = gson.fromJson(Body, new TypeToken<List<News>>() {}.getType());
                    System.out.println(ls.size());
                    if (news.getState() == 200) {
                        Log.e("TAG", "请求新闻成功");
                        ls = news.getRows();
                        System.out.println(ls.size());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lv.setAdapter(newsAdapter);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                                        intent.putExtra("title", ls.get(i).getTitle());
                                        intent.putExtra("content", ls.get(i).getContent());
                                        intent.putExtra("time", ls.get(i).getCreateTime());
                                        intent.putExtra("author", ls.get(i).getAuthor());
                                        intent.putExtra("like", ls.get(i).getLikeNumber());
                                        intent.putExtra("view", ls.get(i).getViewsNumber());

                                        startActivity(intent);
                                    }
                                });
                            }
                        });

//                        Looper.prepare();
//                        Toast.makeText(getActivity(), "请求新闻成功", Toast.LENGTH_LONG).show();
//                        Looper.loop();

                    } else {
                        Looper.prepare();
                        Log.e("TAG", "请求新闻失败");
                        Toast.makeText(getActivity(), "请求新闻失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });
    }


    public void initView() {
        //initData();
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImageLoader(new MyImageLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.Default);
        banner.isAutoPlay(true);
        banner.setBannerTitles(title);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setDelayTime(3000);
        banner.setOnBannerListener(this::OnBannerClick);
        banner.start();
    }

    public void OnBannerClick(int position) {
        Toast.makeText(getActivity(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }


    private class MyImageLoader extends ImageLoader {
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    public class NewsAdapter extends BaseAdapter {

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
            TextView new_title = null, new_context = null, new_date = null, likeNumber = null, viewsNumber = null;

            if (view == null) {
                view = View.inflate(getContext(), R.layout.news_item,null);
                holder = new ViewHolder();
                holder.new_title = view.findViewById(R.id.new_title);
                holder.new_context = view.findViewById(R.id.new_context);
                holder.new_date = view.findViewById(R.id.new_date);
                holder.new_img = view.findViewById(R.id.new_img);
//                holder.likeNumber = view.findViewById(R.id.likeNumber);
//                holder.viewsNumber = view.findViewById(R.id.viewsNumber);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
//            holder.likeNumber.setText("a");
//            holder.viewsNumber.setText("a");
//            holder.new_date.setText("a");
//            holder.new_context.setText("a");
//            holder.new_title.setText("a");
            holder.new_date.setText(ls.get(i).getCreateTime() + "");
            holder.new_context.setText(ls.get(i).getContent());
            holder.new_title.setText(ls.get(i).getTitle());
            holder.new_img.setImageResource(imgs[i]);

            return view;
        }
        class ViewHolder{
            TextView new_title;
            TextView new_context;
            TextView new_date;
            TextView viewsNumber;
            TextView likeNumber;
            ImageView new_img;
        }

    }

}

