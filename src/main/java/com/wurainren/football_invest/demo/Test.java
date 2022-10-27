package com.wurainren.football_invest.demo;

import com.wurainren.football_invest.utils.OKHttp3Utils;
import okhttp3.OkHttpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        String url = "http://m.titan007.com/HandicapDataInterface.ashx";
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1");
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("scheid","2222893");
        paramMap.put("type","1");
        paramMap.put("oddskind","1");
        paramMap.put("isHalf","0");
        paramMap.put("flesh",String.valueOf(new Date().getTime()));
        String result = OKHttp3Utils.get(url, 0, headerMap, paramMap);
        System.out.println(result);
    }


}
