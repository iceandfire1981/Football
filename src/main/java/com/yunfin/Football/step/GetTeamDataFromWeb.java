package com.yunfin.Football.step;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yunfin.Football.data.TeamInfo;
import com.yunfin.Football.data.TeamInfoExtra;
import com.yunfin.Football.data.TeamOddData;
import com.yunfin.Football.util.FootballUtils;

public final class GetTeamDataFromWeb {
	private static final String SQL_QUERY_TEAM_INFO = "select * from league_team_map";
	
	public static boolean getAllTeamData(Connection mysql_connection, String team_base_address, String url_post_fix) {
		ArrayList<TeamInfoExtra> all_team_infos = getTeamInfoFromDatabase(mysql_connection);
		if(null == all_team_infos || all_team_infos.size() <= 0) {
			return false;
		}
		
		for(TeamInfoExtra current_team_info : all_team_infos) {
			System.out.println("GetTeamDataFromWeb::team_info= " + current_team_info);
			getOneTeamData(mysql_connection, current_team_info, team_base_address, url_post_fix);
		}
		return false;
	}
	
	/**
	 * Get team info from football database
	 * @param mysql_connection
	 * @return
	 */
	private static ArrayList<TeamInfoExtra> getTeamInfoFromDatabase(Connection mysql_connection){
		System.out.println("GetTeamDataFromWeb::getTeamInfoFromDatabase::begin=========");
		try{
			PreparedStatement ps = mysql_connection.prepareStatement(SQL_QUERY_TEAM_INFO);
			ResultSet team_infos_rs = ps.executeQuery();
			if(null == team_infos_rs || !team_infos_rs.first()) {
				System.out.println("GetTeamDataFromWeb::getTeamInfoFromDatabase::error::no record in league_team_map");
				return null;
			}
			
			
			ArrayList<TeamInfoExtra> all_team_infos = new ArrayList<TeamInfoExtra>();
			while(team_infos_rs.next()) {
				String team_id = team_infos_rs.getString("team_id");
				String team_name = team_infos_rs.getString("team_name");
				String league_id = team_infos_rs.getString("league_id");
				String league_name = team_infos_rs.getString("leagud_name");
				TeamInfoExtra current_team_info = new TeamInfoExtra(team_id, team_name);
				current_team_info.setLeagueId(league_id);
				current_team_info.setLeagueName(league_name);
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
	
	private static boolean getOneTeamData(Connection mysql_connection, TeamInfoExtra current_info, String team_base_url, String post_fix) {
		if(null == current_info || FootballUtils.isEmpty(team_base_url)) {
			return false;
		}
		
		String current_url = getTeamUrl(current_info.getTeamId(), team_base_url, post_fix);
		System.out.println("GetTeamDataFromWeb::getOneTeamData::url= " + current_url);
		getDataFromWeb(mysql_connection, current_info, current_url);
		return true;
	}
	
	private static String getTeamUrl(String team_id, String team_base_url, String post_fix){
		StringBuffer url_buffer = new StringBuffer(team_base_url);
		url_buffer.append(team_id);
		url_buffer.append(post_fix);
		return url_buffer.toString();
	}
	
	private static final boolean getDataFromWeb(Connection mysql_connect, TeamInfoExtra team_info, String current_url) {
		System.out.println("GetTeamDataFromWeb::getDataFromWeb::url= " + current_url);
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
        	HtmlPage current_page = wc.getPage(current_url);
        	boolean is_first = true;
			do {
				if(! is_first) {
					current_page = current_page.getPage();
				}
				
				DomElement match_root_element = current_page.getElementById("div_Table2");
				System.out.println("GetTeamDataFromWeb::getDataFromWeb::match_root_element= " + match_root_element.asText());
				DomNode match_table_node = match_root_element.getFirstChild();
				System.out.println("GetTeamDataFromWeb::getDataFromWeb::match_table_node= " + match_table_node.asText());
				DomNode match_tbody_node = (DomElement) match_table_node.getFirstChild();
				System.out.println("GetTeamDataFromWeb::getDataFromWeb::match_tbody_node= " + match_tbody_node.asText());
				ArrayList<TeamOddData> all_team_datas = getTeamOddDataFromCurrentPage(match_tbody_node);
				System.out.println("GetTeamDataFromWeb::getDataFromWeb::datas= "
						+ (null == all_team_datas ? "NULL" : all_team_datas.size()));
				
				if(is_first)
					is_first = false;
			} while (hasNextPage(current_page));

        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	wc.close();
        }
		return false;
	}
	
	/**
	 * Found "<a>" target tag in current and click it
	 * @param current_page
	 * @return
	 */
	private static boolean hasNextPage(HtmlPage current_page) {
		HtmlAnchor target_anchor = current_page.getAnchorByName("下一页");
		System.out.println("GetTeamDataFromWeb::hasNextPage= " + (null == target_anchor ? "not found!" : target_anchor.asText()));
		if(null == target_anchor){
			return false;
		} else {
			try {
				target_anchor.click();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	
	private static ArrayList<TeamOddData> getTeamOddDataFromCurrentPage(DomNode tbody_node) {
		DomNodeList<DomNode> node_list = tbody_node.getChildNodes();
		if(null == node_list || node_list.size() <= 0){
			System.out.println("GetTeamDataFromWeb::getTeamOddDataFromCurrentPage not found any children");
			return null;
		} else {
			ArrayList<TeamOddData> result_list = new ArrayList<TeamOddData>();
			for(int index=1; index< node_list.size(); index++){
				DomNode current_node = node_list.get(index);// tr tag
				System.out.println("GetTeamDataFromWeb::getTeamOddDataFromCurrentPage::current_node= " + current_node.asText());
				DomNodeList<DomNode> all_children_node = current_node.getChildNodes();//all td tag
				TeamOddData current_odd_data = new TeamOddData();
				for(int children_index = 0; children_index < all_children_node.size(); children_index++){
					DomNode current_children_node = all_children_node.get(children_index);
					System.out.println("GetTeamDataFromWeb::getTeamOddDataFromCurrentPage::current_node= " + current_node.asText());
					if(children_index == 0){
						current_odd_data.setGameName(current_children_node.asText());
					} else if(children_index == 1) {
						current_odd_data.setGameDateTime(current_children_node.asText());
					} else if(children_index == 2) {
						current_odd_data.setHomeTeamName(current_children_node.asText());
					} else if(children_index == 3) {
						current_odd_data.setFinalScore(current_children_node.asText());
					} else if(children_index == 4) {
						current_odd_data.setAwayTeamName(current_children_node.asText());
					} else if(children_index == 5) {
						current_odd_data.setHalfScore(current_children_node.asText());
					} else if(children_index == 6) {
						current_odd_data.setPankou(current_children_node.asText());
					} else if(children_index == 7) {
						current_odd_data.setPanlu(current_children_node.asText());
					}
				}
				System.out.println("GetTeamDataFromWeb::getTeamOddDataFromCurrentPage::odd= " + current_odd_data);
				result_list.add(current_odd_data);
			}
			return result_list;
		}
	}
}
