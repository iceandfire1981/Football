package com.yunfin.Football.data;

public final class TeamOddData {
	
	private String mGameName, mGameDateTime;
	private String mHomeTeamName, mAwayTeamName;
	private String mFinalScore, mHalfScore;
	private String mPankou, mPanlu;
	
	public String getGameName() {
		return mGameName;
	}
	
	public void setGameName(String game_name) {
		mGameName = game_name;
	}
	
	public String getGameDateTime() {
		return mGameDateTime;
	}
	
	public void setGameDateTime(String game_date_time) {
		mGameDateTime = game_date_time;
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
		this.mPankou = pan_kou;
	}
	
	public String getPanlu() {
		return mPanlu;
	}
	
	public void setPanlu(String pan_lu) {
		mPanlu = pan_lu;
	}

	@Override
	public String toString() {
		return "TeamOddData [getGameName()=" + getGameName() + ", getGameDateTime()=" + getGameDateTime()
				+ ", getHomeTeamName()=" + getHomeTeamName() + ", getAwayTeamName()=" + getAwayTeamName()
				+ ", getFinalScore()=" + getFinalScore() + ", getHalfScore()=" + getHalfScore() + ", getPankou()="
				+ getPankou() + ", getPanlu()=" + getPanlu() + "]";
	}
	
}
