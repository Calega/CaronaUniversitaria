package com.lucaoliveira.unicaroneiro.webservices;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.lucaoliveira.unicaroneiro.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by lucaoliveira on 6/19/2016.
 */
public class WebServicesUtils {
    public static final String TAG = WebServicesUtils.class.getName();

    public enum METHOD {
        POST, GET, DELETE
    }

    public static JSONObject requestJSONObject(String serviceUrl, METHOD method,
                                               ContentValues headerValues, boolean hasAuthorization) {
        return requestJSONObject(serviceUrl, method, headerValues, null, null, hasAuthorization);
    }

    public static JSONObject requestJSONObject(String serviceUrl, METHOD method,
                                               ContentValues urlValues, ContentValues bodyValues) {
        return requestJSONObject(serviceUrl, method, null, urlValues, bodyValues, false);
    }

    public static JSONObject requestJSONObject(String serviceUrl, METHOD method,
                                               ContentValues headerValues, ContentValues urlValues, ContentValues bodyValues,
                                               boolean hasAuthorization) {

        HttpURLConnection urlConnection = null;
        try {
            if (urlValues != null) {
                serviceUrl = addParameterToUrl(serviceUrl, urlValues);
            }

            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT);
            urlConnection.setRequestMethod(method.toString());
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            if (hasAuthorization) {
                addBasicAuthorization(urlConnection);
            }

            if (headerValues != null) {
                Uri.Builder builder = new Uri.Builder();
                for (String key : headerValues.keySet()) {
                    builder.appendQueryParameter(key, headerValues.getAsString(key));
                }

                String query = builder.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }

            if (bodyValues != null) {
                JSONObject jsonObject = new JSONObject();
                for (String key : bodyValues.keySet()) {
                    jsonObject.put(key, bodyValues.getAsString(key));
                }
                String str = jsonObject.toString().trim();
                urlConnection.setRequestProperty("Content-Type", "application/sjon");
                urlConnection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                Log.d(TAG, "JSON Content Values : >>> " + str);
                osw.write(str);
                osw.flush();
                osw.close();
            }

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.d(TAG, " Unauthorized Access");
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "URL Response Error");
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new JSONObject(convertInputStreamToString(in));

        } catch (MalformedURLException e) {
            Log.d(TAG, e.getMessage());
        } catch (SocketTimeoutException e) {
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static String addParameterToUrl(String url, ContentValues urlValues) {
        StringBuffer stringBuffer = new StringBuffer(url);
        stringBuffer.append("?");
        for (String key : urlValues.keySet()) {
            stringBuffer.append(key);
            stringBuffer.append("=");
            stringBuffer.append(urlValues.getAsString(key));
            stringBuffer.append("&");
        }
        stringBuffer.replace(stringBuffer.length() - 1, stringBuffer.length() - 1, "");
        return stringBuffer.toString();
    }

    private static String convertInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String responseText;

        try {
            while ((responseText = bufferedReader.readLine()) != null) {
                Log.d(TAG, "responseText while convertInputStreamToString >>>> " + responseText);
                stringBuilder.append(responseText);
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException in convertInputStreamToString");
            e.printStackTrace();
        }

        Log.d(TAG, "Response resolved from server : >>> " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private static void addBasicAuthorization(HttpURLConnection urlConnection) {
        final String basicAuth = "Basic " + Base64.encodeToString((Constants.APP_KEY +
                ":" + Constants.APP_SECRET).getBytes(), Base64.NO_WRAP);
        urlConnection.setRequestProperty(Constants.AUTHORIZATION, basicAuth);
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
