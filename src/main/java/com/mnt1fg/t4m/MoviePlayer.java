package com.mnt1fg.t4m;

import com.mnt1fg.t4m.util.Data;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import com.mnt1fg.t4m.util.Util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class MoviePlayer {

    public static void play(String scheme) {
        Data dataObj = Util.parse(scheme);
        setViewed(dataObj);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(createUrl(dataObj));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setViewed(Data data) {
        SoapObject soapObject = new SoapObject("http://pos.toshin.com/", "registViewedContents");
        soapObject.addProperty("SSO_TOKEN", data.sso_token);
        soapObject.addProperty("vodfilepath", data.vod_file_path);
        soapObject.addProperty("contentsinfo", data.contents_info);
        soapObject.addProperty("validdtm", data.validdtm);
        SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(110);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(
                "https://pos2.toshin.com/DRM2/DRM25/Webservice/DRMWebService.asmx");
        try {
            httpTransportSE.call("http://pos.toshin.com/registViewedContents",
                    (SoapEnvelope) soapSerializationEnvelope);
            String str = soapSerializationEnvelope.getResponse().toString();
            //System.out.println(str);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
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