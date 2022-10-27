package com.wurainren.football_invest.model;

import lombok.Data;

/**
 * 博彩公司信息
 */
@Data
public class LotteryCompany {
    private Long id;
    // 公司id
    private String companyId;
    // 公司名称
    private String companyName;
    // 公司注册所在国家
    private String county;
    // 公司所属洲
    private String continent;
    // 公司注册时间
    private String registerDate;
}
