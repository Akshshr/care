package com.example.care;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonReader {

    public static final String securityKey = "85a14c2fc61f1132f35440200db1ed55";

    public JsonReader() {
    }

    private String[] jsonMd5() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return new String[]{timestamp, md5(timestamp + securityKey)};
    }

    private String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    UrlEncodedFormEntity ent;

    public JSONObject postJSONFromUrl(ArrayList<NameValuePair> list, String url)
            throws JSONException, IOException {
        HttpResponse response = null;
        Log.i("URL", url);

        HttpPost httppost = new HttpPost(url);
        try {
            String[] md5 = jsonMd5();
            // list.add(new BasicNameValuePair("img", img.out.toString()));

//            list.add(new BasicNameValuePair("security_data", md5[0]));
//            list.add(new BasicNameValuePair("security_hash", md5[1]));

            ent = new UrlEncodedFormEntity(list);
            Log.i("list", list.toString());
            httppost.setEntity(ent);

            // Execute HTTP Post Request
            response = new DefaultHttpClient().execute(httppost);

        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream in = response.getEntity().getContent();
        // json is UTF-8 by default
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,
                "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        Log.i("string", sb.toString());

        try {
            in.close();
        } catch (Exception squish) {
            squish.printStackTrace();
        }
        try {
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JSON RESPONSE string", "ERROR");
        }
        return new JSONObject();
    }

    public JSONObject getJSONFromUrl(String url) {

        Log.i("finalUrl", url);
        // Depends on your web service
        InputStream inputStream = null;
        String result = "";
        try {
            inputStream = new DefaultHttpClient(new BasicHttpParams())
                    .execute(new HttpGet(url)).getEntity().getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            Log.i("string", result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception squish) {
                squish.printStackTrace();
            }
        }
        try {
            return new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}