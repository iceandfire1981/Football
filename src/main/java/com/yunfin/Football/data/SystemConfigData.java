package com.yunfin.Football.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yunfin.Football.config.FootballSystemConfig;

public final class SystemConfigData {
	
	private final class AddressConfig {
		private String mMainAddress, mScondAddress, mTeamAddress;
		private String mPrefix;
		
		public AddressConfig() {
			this("", "", "", "");
		}
		
		public AddressConfig(String main_address, String scond_address, String team_address, String prefix) {
			super();
			mMainAddress = main_address;
			mScondAddress = scond_address;
			mTeamAddress = team_address;
			mPrefix = prefix;
		}

		public void setMainAddress(String main_address) {
			mMainAddress = main_address;
		}

		public void setScondAddress(String scond_address) {
			mScondAddress = scond_address;
		}

		public void setTeamAddress(String team_address) {
			mTeamAddress = team_address;
		}

		public void setPrefix(String prefix) {
			mPrefix = prefix;
		}

		@Override
		public String toString() {
			return "AddressConfig [mMainAddress=" + mMainAddress + ", mScondAddress=" + mScondAddress
					+ ", mTeamAddress=" + mTeamAddress + ", mPrefix=" + mPrefix + "]";
		}
		
		
	}
	
	public final class League{
		private String mName, mId, mIsMain;

		public League(String name, String team_id, String is_main) {
			super();
			mName = name;
			mId = team_id;
			mIsMain = is_main;
		}

		public String getName() {
			return mName;
		}

		public String getId() {
			return mId;
		}

		public String getIsMain() {
			return mIsMain;
		}

		@Override
		public String toString() {
			return "League [getName()=" + getName() + ", getId()=" + getId() + ", getIsMain()=" + getIsMain() + "]";
		}
		
	}
	
	private final class LeaguesConfig {
		
		private ArrayList<League> mAllLeagues;

		public LeaguesConfig() {
			super();
			mAllLeagues = new ArrayList<League>();
		}
		
		
		public ArrayList<League> getAllLeagues(){
			return mAllLeagues;
		}
		
		public void addLeague(League league) {
			if(null == league) {
				return;
			}
			
			if(null == mAllLeagues) {
				mAllLeagues = new ArrayList<League>();
			}
			
			mAllLeagues.add(league);
		}


		@Override
		public String toString() {
			return "LeaguesConfig [mAllLeagues size =" + ((null == mAllLeagues) ? String.valueOf(-1) : String.valueOf(mAllLeagues.size())) + "]";
		}
		
		
	}
	
	private final class DatabaseConfig{
		private String mDatabaseUrl, mDatabaseUser, mDatabasePsd;

		public DatabaseConfig() {
			super();
			mDatabaseUrl = "";
			mDatabaseUser = "";
			mDatabasePsd = "";
		}
		public void setDatabaseUrl(String db_url) {
			mDatabaseUrl = db_url;
		}

		public void setDatabaseUser(String db_user) {
			mDatabaseUser = db_user;
		}

		public void setDatabasePsd(String db_psd) {
			mDatabasePsd = db_psd;
		}
		
	}
	
	private AddressConfig mAddressConfig;
	private LeaguesConfig mLeaguesConfig;
	private DatabaseConfig mDBConfig;
	
	public SystemConfigData() {
		super();
		mAddressConfig = new AddressConfig();
		mLeaguesConfig = new LeaguesConfig();
		mDBConfig = new DatabaseConfig();
	}
	
	public boolean initialSystem(){
		return parserLeagueConfig();
	}
	
	private boolean parserLeagueConfig(){
		SAXReader xml_reader = new SAXReader();
		File config_file = new File(FootballSystemConfig.CONFIG_FILE_NAME);
		try {
			System.out.println("parserLeagueConfig::info::begin::path= " + config_file.getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Document xml_doc = xml_reader.read(config_file);
			List<Element> children_elements = xml_doc.getRootElement().elements();
			if(null == children_elements || children_elements.size() <= 0) {
				System.out.println("parserLeagueConfig::info::end::can not found any element in " + xml_doc.getRootElement().getName());
				return false;
			}
			
			for(Element current_element : children_elements) {
				System.out.println("parserLeagueConfig::child_info= " + current_element.toString());
				if(FootballSystemConfig.XML_TAG_ROOT_DB.equals(current_element.getName())) {
					if(!parserDBConfig(current_element)) {
						System.out.println("parserLeagueConfig::error_info::Can not found db config");
						return false;
					}
				}
				
				if(FootballSystemConfig.XML_TAG_ROOT_ADDRESS.equals(current_element.getName())) {
					if(!parserAddress(current_element)) {
						System.out.println("parserLeagueConfig::error_info::Can not found address config");
						return false;
					}
				}
				
				if(FootballSystemConfig.XML_TAG_ROOT_LEAGUE.equals(current_element.getName())) {
					if(!parserLeague(current_element)) {
						System.out.println("parserLeagueConfig::error_info::Can not found any league config here");
						return false;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("parserLeagueConfig::info::exception= " + e);
			return false;
		}
		System.out.println("parserLeagueConfig::info::end===================");
		return true;
	}
	
	private boolean parserAddress(Element address_elemnts_root) {
		System.out.println("parserAddress::child_info= " + address_elemnts_root);
		if(null != address_elemnts_root) {
			List<Element> address_configs = address_elemnts_root.elements();
			if(null != address_configs && address_configs.size() > 0) {
				for(Element address_config : address_configs) {
					System.out.println("parserAddress::address= " + address_config);
					String element_name = address_config.getName();
					if(FootballSystemConfig.XML_TAG_MAIN_ADDRESS.equals(element_name)) {
						mAddressConfig.setMainAddress(address_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
					
					if(FootballSystemConfig.XML_TAG_SCON_ADDRESS.equals(element_name)) {
						mAddressConfig.setScondAddress(address_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
					
					if(FootballSystemConfig.XML_TAG_TEAM_ADDRESS.equals(element_name)) {
						mAddressConfig.setTeamAddress(address_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
					
					if(FootballSystemConfig.XML_TAG_PREFIX.equals(element_name)) {
						mAddressConfig.setPrefix(address_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean parserLeague(Element league_elemnts_root){
		System.out.println("parserLeague::child_info= " + league_elemnts_root);
		if(null != league_elemnts_root) {
			List<Element> league_configs = league_elemnts_root.elements();
			if(null != league_configs && league_configs.size() > 0) {
				for(Element league_config : league_configs) {
					System.out.println("parserLeague::match_config= " + league_config);
					League current_league = new League(league_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_NAME), 
							league_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_ID), 
							league_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_MAIN));
					mLeaguesConfig.addLeague(current_league);
					System.out.println("parserLeague::match_config_result= " + current_league);
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean parserDBConfig(Element database_elemnts_root){
		System.out.println("parserDBConfig::child_info= " + database_elemnts_root);
		if(null != database_elemnts_root) {
			List<Element> database_configs = database_elemnts_root.elements();
			if(null != database_configs && database_configs.size() > 0) {
				for(Element database_config : database_configs) {
					if(FootballSystemConfig.XML_TAG_DB_URL.equals(database_config.getName())){
						mDBConfig.setDatabaseUrl(database_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
					
					if(FootballSystemConfig.XML_TAG_DB_USER.equals(database_config.getName())){
						mDBConfig.setDatabaseUser(database_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
					
					if(FootballSystemConfig.XML_TAG_DB_PSD.equals(database_config.getName())){
						mDBConfig.setDatabasePsd(database_config.attributeValue(FootballSystemConfig.XML_TAG_ATTRIBUTE_VALUE));
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public String getMainUrl(){
		if(null != mAddressConfig) {
			return mAddressConfig.mMainAddress;
		} else {
			return null;
		}
	}
	
	public String getSecondUrl(){
		if(null != mAddressConfig) {
			return mAddressConfig.mScondAddress;
		} else {
			return null;
		}
	}
	
	public String getTeamUrl(){
		if(null != mAddressConfig) {
			return mAddressConfig.mTeamAddress;
		} else {
			return null;
		}
	}
	
	public String getPagePrefix(){
		if(null != mAddressConfig) {
			return mAddressConfig.mPrefix;
		} else {
			return null;
		}
	}
	
	public ArrayList<League> getAllLeagues(){
		if(null != mLeaguesConfig) {
			return mLeaguesConfig.getAllLeagues();
		} else {
			return null;
		}
	}
	
	public String getDBUrl(){
		if(null != mDBConfig) {
			return mDBConfig.mDatabaseUrl;
		} else {
			return null;
		}
	}
	
	public String getDBUser(){
		if(null != mDBConfig) {
			return mDBConfig.mDatabaseUser;
		} else {
			return null;
		}
	}
	
	public String getDBPassword(){
		if(null != mDBConfig) {
			return mDBConfig.mDatabasePsd;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "SystemConfigData [mAddressConfig=" + mAddressConfig + ", mLeaguesConfig=" + mLeaguesConfig + "]";
	}
	
	
}
