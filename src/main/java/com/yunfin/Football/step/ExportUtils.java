package com.yunfin.Football.step;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class ExportUtils {
    
     public static void export2Excel(Connection mysql_connection){
         
        
        ResultSet query_result_set = null;
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("result");
            
            HSSFRow row_title=sheet.createRow(0);
            row_title.createCell(0).setCellValue("league_name");
            row_title.createCell(1).setCellValue("team_name");    
            row_title.createCell(2).setCellValue("match_datetime");
            row_title.createCell(3).setCellValue("cost");
            row_title.createCell(4).setCellValue("profit");
            row_title.createCell(5).setCellValue("skip_distance");
            row_title.createCell(6).setCellValue("action_match_count");
            
            PreparedStatement ps = mysql_connection.prepareStatement("select * from prifit_policy");
            query_result_set = ps.executeQuery();
            if (null != query_result_set && query_result_set.first()) {
                int row_index = 1;
                while(query_result_set.next()){
                    HSSFRow row_content = sheet.createRow(row_index);
                    row_content.createCell(0).setCellValue(query_result_set.getString(2));
                    row_content.createCell(1).setCellValue(query_result_set.getString(3));    
                    row_content.createCell(2).setCellValue(query_result_set.getString(4));
                    row_content.createCell(3).setCellValue(query_result_set.getDouble(5));
                    row_content.createCell(4).setCellValue(query_result_set.getDouble(6));
                    row_content.createCell(5).setCellValue(query_result_set.getInt(7));
                    row_content.createCell(6).setCellValue(query_result_set.getInt(8));
                    row_index = row_index + 1;
                }
            }
            
           File excel_file = new File("D://result_set.xls");
           if (excel_file.exists()) {
              excel_file.delete();
               
           } 
           FileOutputStream fout = new FileOutputStream(excel_file);
           wb.write(fout);
           fout.close();
           wb.close();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            if(null != query_result_set) {
                try {
                    query_result_set.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
     }
}
