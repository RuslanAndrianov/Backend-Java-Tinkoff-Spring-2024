package edu.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class URLValidator {

    private URLValidator() { }

    public static boolean isValidURL(String text) {
        try {
            URL url = new URL(text);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            int responseCode = huc.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException e) {
            log.error(text + " is not valid URL!");
        }
        return false;
    }

    public static boolean isValidGitHubURL(String text) {
        String regex = "^\\s*https?://github\\.com/[\\w.@:/\\-~]+/[\\w.@:/\\-~]+\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean isValidStackOverflowURL(String text) {
        String regex = "^\\s*https?://stackoverflow\\.com/questions/\\d+/[\\w-]+\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
