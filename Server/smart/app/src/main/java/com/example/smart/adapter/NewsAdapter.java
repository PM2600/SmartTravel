//package com.example.smart.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.example.smart.R;
//import com.example.smart.entity.News;
//
//import java.util.List;
//
//public class NewsAdapter extends BaseAdapter {
//
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return i;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if(view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.news_item, viewGroup, false);
//        }
//
//        TextView new_Title = view.findViewById(R.id.new_title);
//        TextView new_date = view.findViewById(R.id.new_date);
//
//        TextView new_context = view.findViewById(R.id.new_context);
//        TextView likeNumber = view.findViewById(R.id.likeNumber);
//        TextView viewsNumber = view.findViewById(R.id.viewsNumber);
//        return null;
//    }
//}
