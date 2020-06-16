package com.mnt1fg.t4m.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Config {

    private static Config _this;
    private File file;

    private HashMap<String, String> data;

    private Config() {
        boolean flag = false;

        file = new File("ToshinConfig.yml");
        if (!file.exists()) {
            flag = true;
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        load();
        if(flag)
            setInteger("version", 1);
    }

    public String getString(String key) {
        return data.get(key);
    }

    public int getInteger(String key) {
        return Integer.valueOf(data.get(key));
    }

    public double getDouble(String key) {
        return Double.valueOf(data.get(key));
    }

    public void setString(String key, String value) {
        data.put(key, value);
        save();
    }

    public void setInteger(String key, int value) {
        setString(key, Integer.toString(value));
    }

    public void setDouble(String key, double value) {
        setString(key, Double.toString(value));
    }

    public void save() {
        FileOutputStream fileOutputStream;
        try {
            file.delete();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            data.forEach((key, value) -> {
                String str = key + ":" + value + "\n";
                try {
                    fileOutputStream.write(str.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void load() {
        data = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] s = line.split(":", 2);
                data.put(s[0], s[1]);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if(_this == null) {
            _this = new Config();
        }
        return _this;
    }
}