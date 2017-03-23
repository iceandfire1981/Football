package com.yunfin.Football.data;

public final class TeamInfo {
	private String mTeamId, mTeamName;

	public TeamInfo(String team_id, String team_name) {
		super();
		mTeamId = team_id;
		mTeamName = team_name;
	}

	public String getTeamId() {
		return mTeamId;
	}

	public String getTeamName() {
		return mTeamName;
	}

	@Override
	public String toString() {
		return "TeamInfo [getTeamId()=" + getTeamId() + ", getTeamName()=" + getTeamName() + "]";
	}
	
}
