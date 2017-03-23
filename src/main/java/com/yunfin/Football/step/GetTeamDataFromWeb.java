package com.yunfin.Football.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.yunfin.Football.data.TeamInfo;

public final class GetTeamDataFromWeb {
	private static final String SQL_QUERY_TEAM_INFO = "select * from league_team_map";
	
	public static boolean getAllTeamData(Connection mysql_connection) {
		ArrayList<TeamInfo> all_team_infos = getTeamInfoFromDatabase(mysql_connection);
		if(null == all_team_infos || all_team_infos.size() <= 0) {
			return false;
		}
		
		for(TeamInfo current_team_info : all_team_infos) {
			System.out.println("GetTeamDataFromWeb::team_info= " + current_team_info);
			
		}
		return false;
	}
	
	/**
	 * Get team info from football database
	 * @param mysql_connection
	 * @return
	 */
	private static ArrayList<TeamInfo> getTeamInfoFromDatabase(Connection mysql_connection){
		System.out.println("GetTeamDataFromWeb::getTeamInfoFromDatabase::begin=========");
		try{
			PreparedStatement ps = mysql_connection.prepareStatement(SQL_QUERY_TEAM_INFO);
			ResultSet team_infos_rs = ps.executeQuery();
			if(null == team_infos_rs || !team_infos_rs.first()) {
				System.out.println("GetTeamDataFromWeb::getTeamInfoFromDatabase::error::no record in league_team_map");
				return null;
			}
			
			
			ArrayList<TeamInfo> all_team_infos = new ArrayList<TeamInfo>();
			while(team_infos_rs.next()) {
				String team_id = team_infos_rs.getString("team_id");
				String team_name = team_infos_rs.getString("team_name");
				TeamInfo current_team_info = new TeamInfo(team_id, team_name);
				System.out.println("GetTeamDataFromWeb::getTeamInfoFromDatabase::team_info= " + current_team_info);
				all_team_infos.add(current_team_info);
			}
			
			team_infos_rs.close();
			return all_team_infos;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
