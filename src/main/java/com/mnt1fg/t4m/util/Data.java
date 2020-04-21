package com.mnt1fg.t4m.util;

public class Data {
    String validdtm;
    String ticket;
    String title;
    String url;
    String contents_info;
    String kosuno;
	String user_id;

     @Override
     public String toString(){
         StringBuilder builder = new StringBuilder();
         builder.append("validdtm : " + validdtm + "\n");
         builder.append("title : " + title + " 第" + kosuno + "講\n");
         builder.append("ticket : " + ticket + "\n");
         builder.append("url : " + url + "\n");
         builder.append("user_id : " + user_id + "\n");
         return builder.toString();
     }

}