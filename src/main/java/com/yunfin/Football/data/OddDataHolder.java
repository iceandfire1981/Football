package com.yunfin.Football.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yunfin.Football.config.FootballSystemConfig;

public class OddDataHolder {
    
    private static final String QUERY_ODD_RECORD = "select * from odd_data where team_id = ? order by match_datetime ASC";
    private static final String INSERT_POLICY_RECORD = "insert into prifit_policy values (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private Connection mMySqlConnection;
    private String mTeamId;
    private boolean mIsInitial;
    private int mDataCount;
    private ArrayList<OddDataInfo> mAllOddRecords;
    
    public OddDataHolder(Connection mysql_connect, String team_id, int data_count) {
        mMySqlConnection = mysql_connect;
        mTeamId = team_id;
        mIsInitial = false;
        mAllOddRecords = new ArrayList<>();
        mDataCount = data_count;
        initialHolderData();
    }
    
    public void startProcess(){
        if (mIsInitial && mAllOddRecords.size() > mDataCount) {
            processTeamData();
        }
    }
    
    private void initialHolderData(){
        ResultSet query_result_set = null;
        try{
            PreparedStatement query_statement = mMySqlConnection.prepareStatement(QUERY_ODD_RECORD);
            query_statement.setString(1, mTeamId);
            query_result_set = query_statement.executeQuery();
            
            if (null != query_result_set && query_result_set.first()) {
                do{
                    OddDataInfo current_info = new OddDataInfo();
                    current_info.setRecordId(query_result_set.getInt(1));
                    current_info.setTeamLeagueId(query_result_set.getString(2));
                    current_info.setTeamId(query_result_set.getString(3));
                    current_info.setTeamName(query_result_set.getString(4));
                    current_info.setMatchName(query_result_set.getString(5));
                    current_info.setMatchDateTime(query_result_set.getString(6));
                    current_info.setHomeTeamName(query_result_set.getString(7));
                    current_info.setAwayTeamName(query_result_set.getString(8));
                    current_info.setFinalScore(query_result_set.getString(9));
                    current_info.setHalfScore(query_result_set.getString(10));
                    current_info.setPankou(query_result_set.getString(11));
                    current_info.setPanlu(query_result_set.getString(12));
                    mAllOddRecords.add(current_info);
                } while (query_result_set.next());
                
                mIsInitial = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (null != query_result_set) {
                try{
                    query_result_set.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void processTeamData(){
        for(int index = 0; index < mAllOddRecords.size(); ) {
            OddDataInfo current_odd_data = mAllOddRecords.get(index);
            String current_odd_panlu = current_odd_data.getPanlu();
            ArrayList<OddDataInfo> process_odd_list = createProcessDataList(index, mAllOddRecords, current_odd_panlu);
            if (null == process_odd_list || process_odd_list.size() <= 0) {
                System.out.println("OddDataHolder::processTeamData::info::not =================");
                index = index + 1;
                continue;
            }
            int input_money = 100;
            int finally_money = 180;
            if (null != process_odd_list && process_odd_list.size() > 0) {
                for(int process_index = 0; process_index < process_odd_list.size(); process_index ++){
                    OddDataInfo current_odd_panlu_data = process_odd_list.get(process_index);
                    int target_input_monkey = input_money * (process_index + 1);
                    int target_finally_money = finally_money * (process_index + 1);
                    if(FootballSystemConfig.PANLU_WIN.equals(current_odd_panlu)){//当前为赢盘
                        if(FootballSystemConfig.PANLU_LOST.equals(current_odd_panlu_data.getPanlu())){//紧接着也输
                            //写入数据
                            try {
                                PreparedStatement ps = mMySqlConnection.prepareStatement(INSERT_POLICY_RECORD);
                                ps.setInt(1, 0);
                                ps.setString(2, current_odd_panlu_data.getTeamLeagueId());
                                ps.setString(3, current_odd_panlu_data.getTeamName());
                                ps.setString(4, current_odd_panlu_data.getMatchDateTime());
                                ps.setDouble(5, target_input_monkey);
                                ps.setDouble(6, target_finally_money);
                                ps.setInt(7, 3);
                                ps.setInt(8, process_index + 1);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;
                        } else if (FootballSystemConfig.PANLU_WIN.equals(current_odd_panlu_data.getPanlu())){
                            try {
                                PreparedStatement ps = mMySqlConnection.prepareStatement(INSERT_POLICY_RECORD);
                                ps.setInt(1, 0);
                                ps.setString(2, current_odd_panlu_data.getTeamLeagueId());
                                ps.setString(3, current_odd_panlu_data.getTeamName());
                                ps.setString(4, current_odd_panlu_data.getMatchDateTime());
                                ps.setDouble(5, target_input_monkey);
                                ps.setDouble(6, 0);
                                ps.setInt(7, 3);
                                ps.setInt(8, process_index + 1);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                PreparedStatement ps = mMySqlConnection.prepareStatement(INSERT_POLICY_RECORD);
                                ps.setInt(1, 0);
                                ps.setString(2, current_odd_panlu_data.getTeamLeagueId());
                                ps.setString(3, current_odd_panlu_data.getTeamName());
                                ps.setString(4, current_odd_panlu_data.getMatchDateTime());
                                ps.setDouble(5, target_input_monkey);
                                ps.setDouble(6, target_input_monkey);
                                ps.setInt(7, 3);
                                ps.setInt(8, process_index + 1);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else if(FootballSystemConfig.PANLU_LOST.equals(current_odd_panlu)){
                        if(FootballSystemConfig.PANLU_WIN.equals(current_odd_panlu_data.getPanlu())){//紧接着也输
                            //写入数据
                            try {
                                PreparedStatement ps = mMySqlConnection.prepareStatement(INSERT_POLICY_RECORD);
                                ps.setInt(1, 0);
                                ps.setString(2, current_odd_panlu_data.getTeamLeagueId());
                                ps.setString(3, current_odd_panlu_data.getTeamName());
                                ps.setString(4, current_odd_panlu_data.getMatchDateTime());
                                ps.setDouble(5, target_input_monkey);
                                ps.setDouble(6, target_finally_money);
                                ps.setInt(7, 3);
                                ps.setInt(8, process_index + 1);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;
                        } else if (FootballSystemConfig.PANLU_LOST.equals(current_odd_panlu_data.getPanlu())){
                            try {
                                PreparedStatement ps = mMySqlConnection.prepareStatement(INSERT_POLICY_RECORD);
                                ps.setInt(1, 0);
                                ps.setString(2, current_odd_panlu_data.getTeamLeagueId());
                                ps.setString(3, current_odd_panlu_data.getTeamName());
                                ps.setString(4, current_odd_panlu_data.getMatchDateTime());
                                ps.setDouble(5, target_input_monkey);
                                ps.setDouble(6, 0);
                                ps.setInt(7, 3);
                                ps.setInt(8, process_index + 1);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                PreparedStatement ps = mMySqlConnection.prepareStatement(INSERT_POLICY_RECORD);
                                ps.setInt(1, 0);
                                ps.setString(2, current_odd_panlu_data.getTeamLeagueId());
                                ps.setString(3, current_odd_panlu_data.getTeamName());
                                ps.setString(4, current_odd_panlu_data.getMatchDateTime());
                                ps.setDouble(5, target_input_monkey);
                                ps.setDouble(6, target_input_monkey);
                                ps.setInt(7, 3);
                                ps.setInt(8, process_index + 1);
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } 
                }
                
                index = index + mDataCount;
            } else {
                index = index + 1;
            }
        }
    }
    
    private ArrayList<OddDataInfo> createProcessDataList(int current_index, ArrayList<OddDataInfo> source_records, String target_panlu) {
        //索引达到最大限制
        if(current_index >= (source_records.size() - 1)) {
            return null;
        }
        
        if(FootballSystemConfig.PANLU_OTHER.equals(target_panlu)) {
            System.out.println("OddDataHolder::createProcessDataList::ignorx= " + FootballSystemConfig.PANLU_OTHER);
            return null;
        }
        
        //获取字串，从索引指示的位置开始直到结束。
        List<OddDataInfo> sub_source_data_list = source_records.subList(current_index, source_records.size());
        System.out.println("OddDataHolder::createProcessDataList::size1= " + sub_source_data_list.size());
        
        
        //从当前索引开始往后的子串长度小于跨度或者长度足够跨度但后续已经没有数据
        int sub_list_size = sub_source_data_list.size();
        if(sub_list_size < mDataCount || sub_list_size < (mDataCount + 1)) {
            return null;
        }
        
        //获取最终的长度
        int source_total_size = sub_source_data_list.size();
        int target_total_size = mDataCount * 2;
        int finally_total_size = source_total_size >= target_total_size ? target_total_size : source_total_size; 
        List<OddDataInfo> finally_sub_data_list = sub_source_data_list.subList(0, finally_total_size);
        System.out.println("OddDataHolder::createProcessDataList::size2= " + finally_sub_data_list.size());
        
        //在数据块中寻找连续的数据, 不连续则不继续分析
        ArrayList<OddDataInfo> series_odd_datas = new ArrayList<>();
        int index = 0;
        for(; index < mDataCount; index++){
            OddDataInfo source_odd_data = finally_sub_data_list.get(index);
            System.out.println("OddDataHolder::createProcessDataList::source_panlu= " + source_odd_data.getPanlu() + " target_panlu= " + target_panlu);
            if(!target_panlu.equals(source_odd_data.getPanlu())) {
                return null;
            }
        }
        
        //盘路数据
        for(; index < finally_total_size; index ++) {
            OddDataInfo source_odd_data = finally_sub_data_list.get(index);
            series_odd_datas.add(source_odd_data);
        }
        
        if(null == finally_sub_data_list || finally_sub_data_list.size() <= 0){
            return null;
        } else {
            return series_odd_datas;
        }
        
    }
}
