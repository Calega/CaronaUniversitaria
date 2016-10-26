package com.lucaoliveira.unicaroneiro.webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.lucaoliveira.unicaroneiro.Constants;
import com.lucaoliveira.unicaroneiro.R;

import org.json.JSONObject;

/**
 * Created by lucaoliveira on 6/19/2016.
 */
public abstract class WebServiceTask extends AsyncTask<Void, Void, Boolean> {
    public static final String TAG = WebServiceTask.class.getName();

    public abstract void showProgress();

    public abstract boolean performRequest();

    public abstract void performSuccessfulOperation();

    public abstract void hideProgress();

    private String mMessage;
    private Context mContext;

    public WebServiceTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (!WebServicesUtils.hasInternetConnection(mContext)) {
            mMessage = Constants.CONNECTION_MESSAGE;
            return false;
        }
        return performRequest();
    }

    @Override
    protected void onPreExecute() {
        showProgress();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        hideProgress();
        if (success) {
            performSuccessfulOperation();
        }

        if (mMessage != null && mMessage.isEmpty()) {
            Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        hideProgress();
    }

    public boolean hasError(JSONObject obj) {
        if (obj != null) {
            int status = obj.optInt(Constants.STATUS);
            Log.d(TAG, "Response " + obj.toString());
            mMessage = obj.optString(Constants.MESSAGE);

            if (status == Constants.STATUS_ERROR || status == Constants.STATUS_UNAUTHORIZED) {
                return true;
            } else {
                return false;
            }
        }
        mMessage = mContext.getString(R.string.error_url_not_found);
        return true;
    }
}
