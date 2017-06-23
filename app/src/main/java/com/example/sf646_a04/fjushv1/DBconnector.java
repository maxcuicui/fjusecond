package com.example.sf646_a04.fjushv1;

import android.util.Log;
import android.widget.Toast;

import com.example.sf646_a04.fjushv1.imgurmodel.UploadtoDB;

import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by SF646_A04 on 2017/5/9.
 */

public class DBconnector {
    public static String executeQuery(String query_string) {
        String result = "";
        HttpURLConnection urlConnection=null;
        InputStream is =null;
        try {
            URL url = null;
            switch (query_string){ //php的位置
                case "SELECT * FROM 3c":
                url=new URL("http://140.136.150.74/gearhostdb.php");break;
                case "SELECT * FROM category":
                    url=new URL("http://140.136.150.74/category.php");break;
            }
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();//接通資料庫
            is=urlConnection.getInputStream();//從database 開啟 stream

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            is.close();
            result = builder.toString();
        } catch(Exception e) {
            Log.e("log_tag", e.toString());
        }

        return result;
    }
    public static void uploadData(UploadtoDB udb)
    {
        java.sql.Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String url = "mysql4.gear.host";
        String user = "secondhands";
        String password = "123456!";

    }
}
