
package com.mnt1fg.t4m;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.Group;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.concurrent.Worker;
import com.jfoenix.controls.JFXButton;

import org.kordamp.ikonli.javafx.FontIcon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLIFrameElement;
import org.kordamp.ikonli.fontawesome.FontAwesome;

public class App extends Application {
    boolean flag = false; // 戻るボタンの判別用
    int location = -1; // 現在いるページ

    boolean androidFlag = false;
    WebEngine engine;

    public static final String EVENT_TYPE_CLICK = "click";

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("東進受講 for Mac");
        stage.show();
        WebView view = new WebView();
        view.setLayoutY(30);
        view.setPrefWidth(stage.getWidth()); // 幅を設定
        view.setPrefHeight(stage.getHeight()); // 高さを設定
        root.getChildren().add(view);
        engine = view.getEngine();
        engine.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
        engine.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
            @Override
            public WebEngine call(PopupFeatures p) {
                return null;
            }
        });

        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(stage.getWidth());
        pane.setPrefHeight(30);
        pane.setBorder(new Border(new BorderStroke(null, null, Color.rgb(218, 220, 224), null, null, null,
                BorderStrokeStyle.SOLID, null, null, null, null)));
        root.getChildren().add(pane);
        ArrayList<String> list = new ArrayList<>(); // URLのリスト
        JFXButton backButton = new JFXButton("<");
        backButton.setLayoutX(0);
        pane.getChildren().add(backButton);
        backButton.setOnAction(e -> {
            try {
                if (androidFlag) {
                    switchUserAgent(false);
                }
                String url = list.get(location - 1);
                engine.load(url);
                flag = true; // フラグをオンにする
                location--; // 現在いるページを１つ戻す
            } catch (IndexOutOfBoundsException i) {
            }
        });
        JFXButton nextButton = new JFXButton(">");
        nextButton.setLayoutX(30); // X座標を設定
        pane.getChildren().add(nextButton); // AnchorPaneにButtonを貼り付け
        FontIcon reloadIcon = FontIcon.of(FontAwesome.REPEAT);
        JFXButton reloadButton = new JFXButton("", reloadIcon);
        reloadButton.setLayoutX(60); // X座標を設定
        reloadButton.setOnAction(e -> {
            if(androidFlag) return;
            engine.reload(); // リロードする
        });
        pane.getChildren().add(reloadButton); // AnchoerPaneにButtonを貼り付け

        engine.getLoadWorker().stateProperty().addListener((value, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                if (!flag) { // フラグがオンでなければ
                    list.add(engine.getLocation()); // リストに追加
                    location++; // 現在いるページを進める
                }
                flag = false; // フラグをオフにする

                EventListener floadedEventListener = new EventListener() {

                    @Override
                    public void handleEvent(Event evt) {
                        HTMLIFrameElement iframeElement = (HTMLIFrameElement) evt.getTarget();
                        Document doc = iframeElement.getContentDocument();
                        try {

                            HTMLIFrameElement iframe2 = (HTMLIFrameElement) doc.getElementById("ifViewer");
                            if (iframe2 != null) {
                                ((EventTarget) iframe2).addEventListener("load", new EventListener() {

                                    @Override
                                    public void handleEvent(Event evt) {
                                        HTMLIFrameElement iframe3 = (HTMLIFrameElement) evt.getTarget();
                                        Element tag = iframe3.getContentDocument().getElementById("asd");
                                        if(tag != null) {
                                            MoviePlayer.play(tag.getAttribute("href"));
                                        }
                                    }
                                }, false);
                            }
                             
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                Document doc = engine.getDocument();
                HTMLIFrameElement iframeElement = (HTMLIFrameElement) doc.getElementById("appFrame");
                if(iframeElement != null){
                    switchUserAgent(true);
                    ((EventTarget) iframeElement).addEventListener("load", floadedEventListener, false);
                }
			}
        });
        engine.load("https://pos.toshin.com/SSO1/SSOLogin/StudentLogin.aspx");
    }

    public void switchUserAgent(boolean to) {
        androidFlag = to;
        if(to) {
            engine.setUserAgent("Mozilla/5.0 (Android 4.4; Mobile; rv:70.0) Gecko/70.0 Firefox/70.0");
        } else {
            engine.setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
        }
    }

    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
}
