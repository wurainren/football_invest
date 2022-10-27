package com.wurainren.football_invest.model;

import lombok.Data;

/**
 * 欧赔胜平负赔率信息
 */
@Data
public class EuropeanOdds {

   // 博彩公司Id
   private String companyId;
   // 赛事ID
   private String scheid;
   // 初盘主胜
   private Double startHomeOdds;
   // 初盘平局
   private Double startDrawOdds;
   // 初盘客胜
   private Double startAwayOdds;

   // 终盘主胜
   private Double endHomeOdds;
   // 终盘平局
   private Double endDrawOdds;
   // 终盘客胜
   private Double endAwayOdds;

}
