package com.yunfin.Football.data;

public final class OddDataInfo {
    
    private int mRecordId;
    private String mTeamLeagueId, mTeamId, mTeamName;
    private String mMatchName, mMatchDateTime;
    private String mHomeTeamName, mAwayTeamName;
    private String mFinalScore, mHalfScore;
    private String mPankou, mPanlu;
    public int getRecordId() {
        return mRecordId;
    }
    public void setRecordId(int record_id) {
        mRecordId = record_id;
    }
    public String getTeamLeagueId() {
        return mTeamLeagueId;
    }
    public void setTeamLeagueId(String team_league_id) {
        mTeamLeagueId = team_league_id;
    }
    public String getTeamId() {
        return mTeamId;
    }
    public void setTeamId(String team_id) {
        mTeamId = team_id;
    }
    public String getTeamName() {
        return mTeamName;
    }
    public void setTeamName(String team_name) {
        mTeamName = team_name;
    }
    public String getMatchName() {
        return mMatchName;
    }
    public void setMatchName(String match_name) {
        mMatchName = match_name;
    }
    public String getMatchDateTime() {
        return mMatchDateTime;
    }
    public void setMatchDateTime(String match_datetime) {
        mMatchDateTime = match_datetime;
    }
    public String getHomeTeamName() {
        return mHomeTeamName;
    }
    public void setHomeTeamName(String home_team_name) {
        mHomeTeamName = home_team_name;
    }
    public String getAwayTeamName() {
        return mAwayTeamName;
    }
    public void setAwayTeamName(String away_team_name) {
        mAwayTeamName = away_team_name;
    }
    public String getFinalScore() {
        return mFinalScore;
    }
    public void setFinalScore(String final_score) {
        mFinalScore = final_score;
    }
    public String getHalfScore() {
        return mHalfScore;
    }
    public void setHalfScore(String half_score) {
        mHalfScore = half_score;
    }
    public String getPankou() {
        return mPankou;
    }
    public void setPankou(String pan_kou) {
        mPankou = pan_kou;
    }
    public String getPanlu() {
        return mPanlu;
    }
    public void setPanlu(String pan_lu) {
        mPanlu = pan_lu;
    }
    @Override
    public String toString() {
        return "OddDataInfo [getRecordId()=" + getRecordId() + ", getTeamLeagueId()=" + getTeamLeagueId()
                + ", getTeamId()=" + getTeamId() + ", getTeamName()=" + getTeamName() + ", getMatchName()="
                + getMatchName() + ", getMatchDateTime()=" + getMatchDateTime() + ", getHomeTeamName()="
                + getHomeTeamName() + ", getAwayTeamName()=" + getAwayTeamName() + ", getFinalScore()="
                + getFinalScore() + ", getHalfScore()=" + getHalfScore() + ", getPankou()=" + getPankou()
                + ", getPanlu()=" + getPanlu() + "]";
    }
    

}
