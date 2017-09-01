package com.example.badweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badweather.db.City;
import com.example.badweather.db.County;
import com.example.badweather.db.Province;
import com.example.badweather.util.HttpUtil;
import com.example.badweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by iroy on 2017/9/1.
 */

public class ChooseAreaFragment extends Fragment {
    TextView title;
    Button button;
    ListView listView;
    List<String> dataList=new ArrayList<>();
    ArrayAdapter<String> adapter;
    int currentLevel;
    final static int LEVEL_PROVINCE=0;
    final static int LEVEL_CITY=1;
    final static int LEVEL_COUNTY=2;
    List<Province> provinceList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();
    List<County> countyList=new ArrayList<>();
    Province selectedProvince;
    private County selectedCounty;
    private City selectedCity;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        title = (TextView) view.findViewById(R.id.title_text);
        button=(Button) view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.listview);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(i);
                    queryCity();
                }
                if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(i);
                    queryCounty();
                }

            }
        });
        button.setOnClickListener((v)->{
            if (currentLevel==LEVEL_COUNTY){
                queryCity();
            }
            if (currentLevel==LEVEL_CITY){
                queryProvince();
            }
        });
        queryProvince();
    }

    private void queryCounty() {
        title.setText(selectedCity.getCityName());
        button.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("citycode=?",String.valueOf(selectedCity.getCityCode())).find(County.class);
        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }
        else {
            String address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode();
            queryFromServer(address,"county");
        }
    }

    private void queryCity() {
        title.setText(selectedProvince.getProvinceName());
        button.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provincecode=?",String.valueOf(selectedProvince.getProvinceCode())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }
        else{
            String address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode();
            queryFromServer(address,"city");
        }
    }

    private void queryProvince() {
        title.setText("China");
        button.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }
        else {
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryFromServer(String address, final String type) {
        showProcessDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProcessDialog();
                        Toast.makeText(getActivity(),"fail to load",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if (type.equals("province")) {
                    result = Utility.handleProvinceResponse(responseText);
                }
                if (type.equals("city")){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getProvinceCode());
                }
                if (type.equals("county")){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(()->{
                        closeProcessDialog();
                        if ("province".equals(type)){
                            queryProvince();
                        }
                        if ("city".equals(type)){
                            queryCity();
                        }
                        if ("county".equals(type)){
                            queryCounty();
                        }
                    });
                }
            }
        });
    }

    private void closeProcessDialog() {
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    private void showProcessDialog() {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

    }
}
