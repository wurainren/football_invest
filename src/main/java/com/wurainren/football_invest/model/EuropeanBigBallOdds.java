package com.wurainren.football_invest.model;

import lombok.Data;

/**
 * 欧赔大小球赔率信息
 */
@Data
public class EuropeanBigBallOdds {

   // 博彩公司Id
   private String companyId;
   // 赛事ID
   private String scheid;

   // 初盘大小球
   private Double startBigBall;
   // 初盘大小球上盘
   private Double startBigBallUpOdds;
   // 初盘大小球下盘
   private Double startBigBallDownOdds;

   // 终盘大小球
   private Double endBigBall;
   // 终盘大小球上盘
   private Double endBigBallUpOdds;
   // 终盘大小球下盘
   private Double endBigBallDownOdds;

}
