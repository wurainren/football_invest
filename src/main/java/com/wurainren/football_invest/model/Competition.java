package com.wurainren.football_invest.model;

/**
 * 赛事信息
 */
public class Competition {
    /**
     * 赛事ID
     */
    private String scheid;

    /**
     * 联赛
     */
    private String league;

    /**
     * 主队
     */
    private String homeTeam;

    /**
     * 客队
     */
    private String awayTeam;

    /**
     * 比赛时间
     */
    private String matchDate;

    /**
     * 上半场 主队比分
     */
    private String halfHomeTeamScore;

    /**
     * 上半场 客队比分
     */
    private String halfAwayTeamScore;

    /**
     * 全场 主队比分
     */
    private String homeTeamScore;

    /**
     * 全场 客队比分
     */
    private String awayTeamScore;

    /**
     * 比赛胜负 3 1 0
     */
    private String matchWins;

}
