package com.mnt1fg.t4m;

import com.mnt1fg.t4m.util.Data;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import com.mnt1fg.t4m.util.Util;

public class MoviePlayer {

    public static void play(String scheme) {
        Data dataObj = Util.parse(scheme);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(createUrl(dataObj));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static void registViewed() {
        //
    }

    public static URI createUrl(Data dataObj) throws URISyntaxException, UnsupportedEncodingException {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("https://toshin4mac.netlify.app/player/index.html?");
        sBuilder.append("url=" + Util.base64encode(dataObj.url));
        sBuilder.append("&ticket=" + dataObj.ticket);
        sBuilder.append("&title=" + Util.base64encode(dataObj.title));
        sBuilder.append("&userid=" + dataObj.user_id);
        sBuilder.append("&sso_token=" + dataObj.sso_token);
        sBuilder.append("&contentsinfo=" + dataObj.contents_info);
        sBuilder.append("&vod_file_path=" + Util.base64encode(dataObj.vod_file_path));
        sBuilder.append("&validdtm=" + dataObj.validdtm);
        return new URI(sBuilder.toString());
    }
}