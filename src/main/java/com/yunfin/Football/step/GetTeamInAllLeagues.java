package com.yunfin.Football.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yunfin.Football.config.FootballSystemConfig;
import com.yunfin.Football.data.SystemConfigData;
import com.yunfin.Football.data.SystemConfigData.League;
import com.yunfin.Football.data.TeamInfo;
import com.yunfin.Football.util.FootballUtils;

public final class GetTeamInAllLeagues {
	
	public static boolean getAllTeamInLeagues(Connection mysql_connection, SystemConfigData config_data){
		long begin_time = System.currentTimeMillis();
		System.out.println("GetTeamInAllLeagues::getAllTeamInLeagues::begin[" + begin_time + "]");
		
		String main_url = config_data.getMainUrl();
		String seco_url = config_data.getSecondUrl();
		String url_fix = config_data.getPagePrefix();
		ArrayList<League> all_leagues = config_data.getAllLeagues();
		
		System.out.println("GetTeamInAllLeagues::getAllTeamInLeagues::params::main= " + main_url + "second= " + seco_url + " fix= " + url_fix 
				+ " leagues_size= " + ((null == all_leagues) ? "-1" : String.valueOf(all_leagues.size())));
		
		if(!ensureSystemConfig(main_url, seco_url, url_fix, all_leagues)){
			return false;
		}
		
		clearTeaminfoTable(mysql_connection);
		for(League current_league : all_leagues) {
			String target_url = mixLeagueUrl(main_url, seco_url, url_fix, current_league.getId(), current_league.getIsMain());
			System.out.println("GetTeamInAllLeagues::getAllTeamInLeagues::current_league[" + current_league + "] url= " + target_url);
			ArrayList<TeamInfo> team_infos = getTeamInfoFormLeague(target_url);
			//Insert to database
			if(null != team_infos && team_infos.size() > 0) {
				for(TeamInfo current_team_info : team_infos) {
					boolean is_success = insertIntoDatabase(mysql_connection, current_league.getId(), 
							current_league.getName(), current_league.getIsMain(), current_team_info.getTeamId(), current_team_info.getTeamName());
					System.out.println("GetTeamInAllLeagues::getAllTeamInLeagues::current_team [" + current_team_info + "] insert result= " + is_success);
				}
			}
		}
		return true;
	}
	
	private static boolean ensureSystemConfig(String main_url, String second_url, String page_fix, ArrayList<League> all_leagues) {
		if(FootballUtils.isEmpty(main_url) || FootballUtils.isEmpty(second_url) 
				|| FootballUtils.isEmpty(page_fix) || null == all_leagues || all_leagues.size() <= 0) {
			return false;
		}
		return true;
	}
	
	private static String mixLeagueUrl(String main_url, String second_url, String url_fix, String league_id, String is_main) {
		StringBuffer url_buffer = new StringBuffer();
		if(FootballSystemConfig.IS_MAIN_TRUE.equals(is_main)) {
			url_buffer.append(main_url);
		} else {
			url_buffer.append(second_url);
		}
		url_buffer.append(league_id);
		url_buffer.append(url_fix);
		
		return url_buffer.toString();
	}
	
	private static ArrayList<TeamInfo> getTeamInfoFormLeague(String target_url){
		System.out.println("GetTeamInAllLeagues::getTeamInfoFormLeague::url= " + target_url);
		if(FootballUtils.isEmpty(target_url)){
			System.out.println("GetTeamInAllLeagues::getTeamInfoFormLeague::error::url is null");
			return null;
		}
		
		try{
	    	Thread.sleep(5000);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
		 
		WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
        wc.getOptions().setCssEnabled(false); // 禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(50000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.getOptions().setDoNotTrackEnabled(false);
        
        try{
            HtmlPage current_page = wc.getPage(target_url);
            DomElement drop_element = current_page.getElementById("dropScoreTeam");
            if(null != drop_element) {
	            DomNodeList<DomNode> child_list = drop_element.getChildNodes();
	            if(null == child_list || child_list.size() <= 0) {
	            	System.out.println("GetTeamInAllLeagues::getTeamInfoFormLeague::error==can not get child in dropScoreTeam");
	            } else {
	            	ArrayList<TeamInfo> team_infos = new ArrayList<TeamInfo>();
	            	System.out.println("GetTeamInAllLeagues::getTeamInfoFormLeague::child_size= " + child_list.size());
	            	for(int index=2; index < child_list.size(); index++) {
	            		DomNode current_node = child_list.get(index);
	            		String team_name = current_node.asText();
	            		String team_id = current_node.getAttributes().getNamedItem("value").getNodeValue();
	            		System.out.println("GetTeamInAllLeagues::getTeamInfoFormLeague::team_name= " + team_name + " team_id= " + team_id);
	            		team_infos.add(new TeamInfo(team_id, team_name));
	            	}
	            	return team_infos;
	            }
            } else {
            	System.out.println("GetTeamInAllLeagues::getTeamInfoFormLeague::drop_element is null ");
            }
            wc.close();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	wc.close();
        }
		return null;
	}
	
	private static boolean clearTeaminfoTable(Connection mysql_connect) {
		String sql="delete from league_team_map";
		try {
			PreparedStatement ps =mysql_connect.prepareStatement(sql);
			int delete_record_count = ps.executeUpdate();
			if(delete_record_count > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean insertIntoDatabase(Connection mysql_connect, String league_id, String league_name, String is_main, String team_id, String team_name){
		String insert_sql = "insert into league_team_map (league_id, leagud_name, league_is_main, team_id, team_name) values (?,?,?,?,?)";
		try {
			PreparedStatement ps = mysql_connect.prepareStatement(insert_sql);
            ps.setString(1, league_id);
            ps.setString(2, league_name);
            ps.setString(3, is_main);
            ps.setString(4, team_id);
            ps.setString(5, team_name);
            int insert_result = ps.executeUpdate();
            if(insert_result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
