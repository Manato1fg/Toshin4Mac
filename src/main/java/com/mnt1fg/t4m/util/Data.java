package com.mnt1fg.t4m.util;

public class Data {
    public String validdtm;
    public String ticket;
    public String title;
    public String url;
    public String contents_info;
    public String kosuno;
    public String user_id;
    public String vod_file_path;
    public String sso_token;
     @Override
     public String toString(){
         StringBuilder builder = new StringBuilder();
         builder.append("validdtm : " + validdtm + "\n");
         builder.append("title : " + title + " 第" + kosuno + "講\n");
         builder.append("ticket : " + ticket + "\n");
         builder.append("url : " + url + "\n");
         builder.append("user_id : " + user_id + "\n");
         builder.append("vod_file_path : " + vod_file_path + "\n");
         builder.append("sso_token : " + sso_token);
         return builder.toString();
     }

}