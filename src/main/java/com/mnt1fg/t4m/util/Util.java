package com.mnt1fg.t4m.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

public class Util {


    public static Data parse(String scheme) {
        Data dataObj = new Data();
        String data1 = scheme.split("A477C046-2D9B-40CF-94C0-427C9C99E580://nagase.com/openwith?")[1];
        String[] data2 = data1.split(",");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String datum : data2) {
            String key = datum.split("=", 2)[0];
            String value = datum.split("=", 2)[1];
            hashMap.put(key, value);
        }
        String validdtm = hashMap.get("validdtm");
        String ticket = hashMap.get("ticket");
        String url_2 = DecryptString(hashMap.get("url_2"), validdtm);
        String contents_info = hashMap.get("contentsinfo");
        String sso_token = hashMap.get("?SSO_TOKEN");

        dataObj.validdtm = validdtm;
        dataObj.ticket = ticket;
        dataObj.url = url_2;
        dataObj.contents_info = contents_info;
        dataObj.sso_token = sso_token;
        parseContentsInfo(dataObj);
        return dataObj;
    }

    public static void parseContentsInfo(Data dataObj) {
        String contents_info = DecryptString(dataObj.contents_info, dataObj.validdtm);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String datum : contents_info.split(",")) {
            String key = datum.split("=", 2)[0];
            String value = datum.split("=", 2)[1];
            hashMap.put(key, value);
        }
        dataObj.title = DecryptString(hashMap.get("title"), dataObj.validdtm);
        dataObj.kosuno = hashMap.get("kosuno");
        dataObj.title += "第" + dataObj.kosuno + "講";
        dataObj.user_id = hashMap.get("userid");
        dataObj.vod_file_path = hashMap.get("vodfilepath");
    }


    public static String base64decode(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            return new String(decoder.decode(str), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String base64encode(String str) {
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            return new String(encoder.encode(str.getBytes()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String DecryptString(String paramString1, String paramString2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NagaseDRMContensCrypt_");
            stringBuilder.append(paramString2);
            paramString2 = stringBuilder.toString();
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] arrayOfByte1 = decoder.decode(paramString1);
            byte[] arrayOfByte3 = paramString2.getBytes("UTF-8");
            byte[] arrayOfByte5 = ResizeBytesArray(arrayOfByte3, 32);
            byte[] arrayOfByte4 = ResizeBytesArray(arrayOfByte3, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(arrayOfByte5, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(arrayOfByte4);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] arrayOfByte2 = cipher.doFinal(arrayOfByte1);
            String str = new String(arrayOfByte2, "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private static byte[] ResizeBytesArray(byte[] paramArrayOfbyte, int paramInt) {
        byte[] arrayOfByte = new byte[paramInt];
        byte b = 0;
        if (paramArrayOfbyte.length <= paramInt) {
            for (paramInt = b; paramInt < paramArrayOfbyte.length; paramInt++)
                arrayOfByte[paramInt] = (byte) paramArrayOfbyte[paramInt];
        } else {
            b = 0;
            paramInt = b;
            while (b < paramArrayOfbyte.length) {
                int i = paramInt + 1;
                arrayOfByte[paramInt] = (byte) (byte) (arrayOfByte[paramInt] ^ paramArrayOfbyte[b]);
                if (i >= arrayOfByte.length) {
                    paramInt = 0;
                } else {
                    paramInt = i;
                }
                b++;
            }
        }
        return arrayOfByte;
    }

    public static int getLatestVersion() {
        return Integer.parseInt(getFromURL("https://toshin4mac.netlify.app/install/version.txt"));
    }

    public static String getFromURL(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String xml = "", line = "";
            while ((line = reader.readLine()) != null)
                xml += line;
            reader.close();
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}