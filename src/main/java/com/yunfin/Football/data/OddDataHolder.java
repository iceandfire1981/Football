package com.yunfin.Football.data;

import java.sql.Connection;
import java.util.HashMap;

public class OddDataHolder {
    private Connection mMySqlConnection;
    private HashMap<Integer, OddDataInfo> mAllOddRecord;
    
    public OddDataHolder(Connection mysql_connect) {
        mMySqlConnection = mysql_connect;
        mAllOddRecord = new HashMap<>();
        initialHolderData();
    }
    
    private void initialHolderData(){
        
    }
}
