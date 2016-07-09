/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

/**
 *
 * @author Internship
 */
public class AboutMe {

    public static void main(String[] args) throws IOException, JSONException {
        parse();
    }

    public String[] aboutme = {"work", "education", "living", "contact-info", "basic-info", "nicknames", "relationship", "bio"};

    public static void parse() throws IOException, JSONException {
        File input = new File("/Users/Internship/Desktop/fb.html");
        Document doc = Jsoup.parse(input, "UTF-8");

        JSONObject json = new JSONObject()
                .put("About Me", new JSONObject()
                        .put("Work", work(doc))
                        .put("Education", education(doc))
                        .put("Contact Info", contactInfo(doc))
                );
        System.out.println(json);
    }

    public static String mobileToDesktopURL(String url) {
        StringBuilder desktopURL = new StringBuilder();
        if (url.contains("m.facebook.com")) {
            desktopURL.append(url.substring(0, url.indexOf("m.facebook.com")));
            desktopURL.append("www.facebook.com");
            desktopURL.append(url.substring(url.indexOf("m.facebook.com") + 14));
        }
        return desktopURL.toString();
    }

    public static JSONArray education(Document doc) throws JSONException {
        // Education
        Element education = doc.getElementById("education");
        JSONObject json = new JSONObject()
                .put(education.select(".__gx").text(), "");
        Object[] experienceElements = education.select(".ib.cc.experience").toArray();
        JSONArray jsonArrayEdu = new JSONArray();
        JSONObject jsonObj = null;
        for (Object elements : experienceElements) {
            Element element = (Element) elements;
            String name = element.select("._52jd._52jb._52jh._3-8_").text();
            String link = element.select("._52jd._52jb._52jh._3-8_").select("a").attr("href");
            String department = element.select(".concs.mfss.fcg").text();
            String date = element.select("._52jc._52j9").text();
            jsonObj = new JSONObject()
                    .put("name", name)
                    .put("department", department)
                    .put("date", date)
                    .put("link", mobileToDesktopURL(link));
            jsonArrayEdu.put(jsonObj);
        }
//        System.out.println(jsonArrayEdu);
        return jsonArrayEdu;
    }

    public static JSONArray work(Document doc) throws JSONException {
        Element work = doc.getElementById("work");
        Object[] experienceElements = work.select(".ib.cc.experience").toArray();
        JSONArray jsonArrayWork = new JSONArray();
        JSONObject jsonObj = null;
        for (Object elements : experienceElements) {
            Element element = (Element) elements;
            String name = element.select("._52jd._52jb._52jh._3-8_").text();
            String link = element.select("._52jd._52jb._52jh._3-8_").select("a").attr("href");
            String position = element.select("._52jc._52j9").text();
            String contact = element.select("._52jc._52jb").text();
            jsonObj = new JSONObject()
                    .put("name", name)
                    .put("position", position)
                    .put("contact", contact)
                    .put("link", mobileToDesktopURL(link));
            jsonArrayWork.put(jsonObj);
        }
//        System.out.println(jsonObj);
        return jsonArrayWork;
    }

    public static JSONObject contactInfo(Document doc) throws JSONException {
        Element contactInfo = doc.getElementById("contact-info");
        Object[] experienceElements = contactInfo.select("._55x2._5ji7").toArray();
        JSONObject jsonObj = new JSONObject();
        for (Object elements : experienceElements) {
            Object[] element = ((Element) elements).children().toArray();
            for (Object ele : element) {
                if (((Element) ele).attr("title") != null && ((Element) ele).attr("title") != "") {
                    String name = ((Element) ele).children().select("span").text();
                    String user = ((Element) ele).select("._5cdv.r").text();
//                    System.out.println(name + " => " + user);
//                    System.out.println("---------");
                    jsonObj.put(name, user);
                }

            }

        }
        return jsonObj;
    }
}
