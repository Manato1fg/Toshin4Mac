package com.mnt1fg.t4m;

import com.mnt1fg.t4m.util.Data;
import com.mnt1fg.t4m.util.JavaBridge;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.mnt1fg.t4m.util.Util;

public class MoviePlayer {
    private static final JavaBridge bridge = new JavaBridge();

    public static void play(String scheme) {
        Data dataObj = Util.parse(scheme);
        System.out.println(dataObj.toString());
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(createUrl(dataObj));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static URI createUrl(Data dataObj) throws URISyntaxException {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("https://toshin4mac.netlify.app/player/index.html?");
        sBuilder.append("url=\'" + dataObj.url + "\'");
        sBuilder.append("ticket=\'" + dataObj.ticket + "\'");
        sBuilder.append("title=\'" + dataObj.title + "\'");
        sBuilder.append("userid=\'" + dataObj.user_id + "\'");
        return new URI(sBuilder.toString());
    }
}