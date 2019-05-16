package com.medina.juanantonio.fileshare;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Tools {
    public static String getFileSize(int size) {
        if(size < 1000)
            return size + "B";
        else if(size < 1000000)
            return (size / 1000) + "KB";
        else if(size < 1000000000)
            return (size / 1000000) + "MB";
        else
            return (size / 1000000000) + "GB";
    }

    public static String formatDate(String date) {
        String dateReturn = "";

        try {
            String string = "2013-03-05T18:05:05.000Z";
            Date formatDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                    .parse(string.replaceAll("Z$", "+0000"));

            dateReturn = formatDate.toString();
        } catch (Exception e) {
            Log.d("HTTP", e.getMessage());
        }

        return dateReturn;
    }

    public static Boolean urlCheck(String url) {
        return url.substring(0,7).equals("http://") || url.substring(0,8).equals("https://");
    }

    public static String previousURL(String currentURL) {
        String[] splitURL = currentURL.split("/");
        String prevURL = "";

        for(int a = 0; a < splitURL.length-1; a++) {
            if(prevURL.equals(""))
                prevURL += splitURL[a];
            else
                prevURL += "/" + splitURL[a];
        }

        return prevURL;
    }

    public static String lastFolder(String previousUrl) {
        String[] splitURL = previousUrl.split("/");

        return splitURL[splitURL.length-1];
    }

    public static ArrayList<FileModel> searchResults(ArrayList<FileModel> rawList, String query) {
        ArrayList<FileModel> results = new ArrayList<>();

        for(int a = 0; a < rawList.size(); a++) {
            FileModel file = rawList.get(a);

            if(file.getFileName().toUpperCase().contains(query.toUpperCase()) ||
                    file.getFileType().toUpperCase().contains(query.toUpperCase()) ||
                    file.getFileSize().toUpperCase().contains(query.toUpperCase()))
                results.add(file);
        }

        return results;
    }
}
