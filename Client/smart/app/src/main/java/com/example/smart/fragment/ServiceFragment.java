package com.example.smart.fragment;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart.FoodActivity;
import com.example.smart.MapActivity;
import com.example.smart.NewsWebViewActivity;
import com.example.smart.R;
import com.example.smart.ViewActivity;
import com.example.smart.bean.ResultNews;
import com.example.smart.bean.ResultWeather;
import com.example.smart.entity.Weather;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceFragment extends BaseFragment {

    private TextView tv;
    private TextView weather_tem, weather_wea, weather_city, weather_wind, weather_windspeed, weather_hum, weather_time;
    int [] rd={R.drawable.map,R.drawable.bus,R.drawable.food,R.drawable.view};
    private ImageView wea_img;
    private Intent intent = null;


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_service,null);
        tv = (TextView) view.findViewById(R.id.tv);
        weather_tem = view.findViewById(R.id.weather_tem);
        weather_wea = view.findViewById(R.id.weather_wea);
        weather_city = view.findViewById(R.id.weather_city);
        weather_wind = view.findViewById(R.id.weather_wind);
        weather_windspeed = view.findViewById(R.id.weather_windspeed);
        weather_hum = view.findViewById(R.id.weather_hum);
        weather_time = view.findViewById(R.id.weather_time);
        wea_img = view.findViewById(R.id.wea_img);

        GridView gridview = (GridView)view.findViewById(R.id.gridview);
        String[] name = {"城市地图", "智慧出行", "特产美食", "景区景点"};

        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i < 4; i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemImage", rd[i]);
            map.put("ItemText", name[i]);// 按序号做ItemText
            lstImageItem.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(), lstImageItem,
                R.layout.gridview_item,
                new String[] { "ItemImage", "ItemText" },
                new int[] { R.id.ItemImage, R.id.ItemText });
        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());

        searchWeather();
        return view;
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
            //处理监听事件：启动Activity，并在Intent中传递参数（显示哪张图片）
            switch(position){
                case 0:
                    Log.e("TAG", "地图模块被点击");
                    intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                    break;
                case 1:

                    break;
                case 2:
                    Log.e("TAG", "特产美食被点击");
                    intent = new Intent(getActivity(), FoodActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    Log.e("TAG", "景区景点被点击");
                    intent = new Intent(getActivity(), ViewActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void searchWeather(){
        Request request = new Request.Builder()
                .url("https://restapi.amap.com/v3/weather/weatherInfo?key=a14f3bb7388f34918b9b79e988370120&city=150100")
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
                    ResultWeather result = gson.fromJson(Body, ResultWeather.class);

                    if (result.getInfo().equals("OK")) {
                        Log.e("TAG", "请求天气成功");
                        List<Weather> weather = result.getLives();
                        weather_tem.setText(weather.get(0).getTemperature() + "°");
                        weather_wea.setText(weather.get(0).getWeather());
                        weather_city.setText(weather.get(0).getProvince() + " " + weather.get(0).getCity());
                        weather_wind.setText(weather.get(0).getWinddirection() + "风");
                        weather_windspeed.setText("风速：" + weather.get(0).getWindpower());
                        weather_hum.setText("湿度：" + weather.get(0).getHumidity());
                        weather_time.setText(weather.get(0).getReporttime().substring(0, 10));

//                        Looper.prepare();
//                        Toast.makeText(getActivity(), "请求天气成功", Toast.LENGTH_LONG).show();
//                        Looper.loop();

                    } else {
                        Looper.prepare();
                        Log.e("TAG", "请求天气失败");
                        Toast.makeText(getActivity(), "请求天气失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });


    }

}

