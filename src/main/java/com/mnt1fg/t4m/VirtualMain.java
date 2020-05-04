package com.mnt1fg.t4m;

import java.io.IOException;

public class VirtualMain {
    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}