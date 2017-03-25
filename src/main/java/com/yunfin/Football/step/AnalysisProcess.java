package com.yunfin.Football.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.yunfin.Football.data.TeamInfo;
import com.yunfin.Football.data.TeamInfoExtra;

public final class AnalysisProcess {
    
    private static String CLEAR_TABLE_SQL = "delete from prifit_policy";
    private static String QUERY_ODD_SQL = "select from odd_data where = ?";
    private static String QUERY_TEAM_INFO = "select team_id, team_name from league_team_map";
    
    public static void processAnalysis(Connection mysql_connecion){
        ArrayList<TeamInfo> all_support_team = getTeamInfo(mysql_connecion);
        if(null == all_support_team || all_support_team.size() <= 0) {
            return ;
        }

        //First clear all process record;
        try{
            PreparedStatement ps = mysql_connecion.prepareStatement(CLEAR_TABLE_SQL);
            ps.executeUpdate();
        } catch(Exception e ){
            e.printStackTrace();
        }
        
        for (TeamInfo current_team : all_support_team) {
            System.out.println("AnalysisProcess::processAnalysis::current_team=" + current_team);
            try{
                PreparedStatement ps = mysql_connecion.prepareStatement(QUERY_ODD_SQL);
                ps.setString(1, current_team.getTeamId());
                ResultSet rs = ps.executeQuery();
                if (null != rs && rs.first()) {
                    
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
    
    private static ArrayList<TeamInfo> getTeamInfo(Connection mysql_connecion) {
        ArrayList<TeamInfo> all_support_teams = new ArrayList<>();
        try{
            PreparedStatement ps = mysql_connecion.prepareStatement(QUERY_TEAM_INFO);
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.first()) {
                while(rs.next()) {
                    TeamInfo current_team = new TeamInfo(rs.getString(1), rs.getString(2));
                    System.out.println("AnalysisProcess::getTeamInfo::current_team=" + current_team);
                    all_support_teams.add(current_team);
                }
                rs.close();
                return all_support_teams;
            }
            rs.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return all_support_teams;
    }
    
}
