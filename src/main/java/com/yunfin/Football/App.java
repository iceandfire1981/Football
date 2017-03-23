package com.yunfin.Football;

import java.sql.Connection;
import java.sql.DriverManager;

import com.yunfin.Football.data.SystemConfigData;
import com.yunfin.Football.step.GetTeamDataFromWeb;
import com.yunfin.Football.step.GetTeamInAllLeagues;

/**
 * Hello world!
 *
 */
public class App 
{
	private static SystemConfigData mConfigData;
	private static Connection mMySqlConnection;
	
	private static final String MYSQL_URL = "jdbc:mysql://47.90.55.236:3306/football";
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        mConfigData = new SystemConfigData();
        if(!mConfigData.initialSystem())
        	return;
        
        boolean is_success = false;
        try{
        	//Load MYSQL driver
        	Class.forName("com.mysql.jdbc.Driver");
        	//initial MYSQL connection
        	mMySqlConnection = DriverManager.getConnection(mConfigData.getDBUrl(), mConfigData.getDBUser(), mConfigData.getDBPassword());
        	is_success = true;
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        if(!is_success) {
        	System.out.println("main::info::initial connection to database false");
        	return;
        }
        
        is_success = GetTeamInAllLeagues.getAllTeamInLeagues(mMySqlConnection, mConfigData);
        if(!is_success) {
        	System.out.println("main::info::Get teams in league false");
        	return;
        }
        
        is_success = GetTeamDataFromWeb.getAllTeamData(mMySqlConnection);
        if(!is_success) {
        	System.out.println("main::info::Get teams information false");
        	return;
        }
        
    }
}
