package com.yunfin.Football.data;

public class TeamInfoExtra extends TeamInfo {
	
	private String mLeagueId, mLeagueName;
	
	public TeamInfoExtra(String team_id, String team_name) {
		super(team_id, team_name);
		// TODO Auto-generated constructor stub
	}

	public String getLeagueId() {
		return mLeagueId;
	}

	public void setLeagueId(String league_id) {
		mLeagueId = league_id;
	}

	public String getLeagueName() {
		return mLeagueName;
	}

	public void setLeagueName(String league_name) {
		mLeagueName = league_name;
	}

	@Override
	public String toString() {
		return "TeamInfoExtra [getLeagueId()=" + getLeagueId() + ", getLeagueName()=" + getLeagueName()
				+ ", getTeamId()=" + getTeamId() + ", getTeamName()=" + getTeamName() + "]";
	}
	
	

}
