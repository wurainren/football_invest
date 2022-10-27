package com.wurainren.football_invest.demo;

import com.wurainren.football_invest.utils.OKHttp3Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Test1 {
    public static void main(String[] args) throws Exception {
        //声明需要格式化的格式(日期)
        // 2022/10/25
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String date = dfDate.format(LocalDateTime.now());

        String url = "http://vip.titan007.com/history/loadOverDownXml.aspx";
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1");
        Map<String,String> paramMap = new HashMap<>();
        List<Integer> companyIdList = new ArrayList<>();
        companyIdList.add(1);
        companyIdList.add(3);
        companyIdList.add(14);
        companyIdList.add(24);
        companyIdList.add(31);
        companyIdList.add(4);
        companyIdList.add(8);
        companyIdList.add(12);
        String companyid = companyIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
        paramMap.put("companyid",companyid);
        paramMap.put("matchdate",date);
        String result = OKHttp3Utils.get(url, 0, headerMap, paramMap);
        System.out.println(result);

        // 解析XML

    }


}
