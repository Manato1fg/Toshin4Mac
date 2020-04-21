package com.mnt1fg.t4m;

import com.mnt1fg.t4m.util.Data;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;

import netscape.javascript.JSObject;

import com.mnt1fg.t4m.util.Util;

public class MoviePlayer {

    //イベント受付クラス（publicでなければならない）
	public class EventHandler{
		public void log(String msg) {
			System.out.println(msg);
        }
        
        public void error(String msg) {
            System.out.println(msg);
        }
	}

    public static void play(String scheme) {
        Data dataObj = Util.parse(scheme);
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
        view.getEngine().load("https://toshin4mac.netlify.app/player/index.html");
        view.getEngine().getLoadWorker().stateProperty().addListener((value, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // コンソールログの実装
                JSObject window = (JSObject) view.getEngine().executeScript("window");
                window.setMember("eventHandler", new EventHandler());
                view.getEngine().executeScript("console.log = function(msg){window.eventHandler.log(msg);};");
                view.getEngine().executeScript("initData(\'" + dataObj.url + "\',\'" + dataObj.ticket + "\',\'"
                        + dataObj.title + "\',\'" + dataObj.user_id + "\')");
            }
        });
    }
}