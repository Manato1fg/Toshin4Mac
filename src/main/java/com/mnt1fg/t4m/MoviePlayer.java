package com.mnt1fg.t4m;

import com.mnt1fg.t4m.util.Data;
import com.mnt1fg.t4m.util.JavaBridge;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import com.mnt1fg.t4m.util.Util;

public class MoviePlayer {
    private static final JavaBridge bridge = new JavaBridge();

    public static void play(String scheme) {
        Data dataObj = Util.parse(scheme);
        System.out.println(dataObj.toString());
        Stage stage = new Stage();
        stage.setTitle("受講画面");
        stage.show();
        Group group = new Group();
        Scene scene = new Scene(group, 1000, 600);
        stage.setScene(scene);
        WebView view = new WebView();
        view.setPrefWidth(stage.getWidth()); // 幅を設定
        view.setPrefHeight(stage.getHeight()); // 高さを設定
        group.getChildren().add(view);
        view.getEngine().setUserAgent(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
        view.getEngine().load("https://toshin4mac.netlify.app/player/index.html");
        view.getEngine().getLoadWorker().stateProperty().addListener((value, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) view.getEngine().executeScript("window");
                window.setMember("java", bridge);
                view.getEngine()
                        .executeScript("console.log = function(message)\n" + "{\n" + "    java.log(message);\n" + "};");
                view.getEngine().executeScript("initData(\'" + dataObj.url + "\',\'" + dataObj.ticket + "\',\'"
                        + dataObj.title + "\',\'" + dataObj.user_id + "\')");
            }
        });
    }
}