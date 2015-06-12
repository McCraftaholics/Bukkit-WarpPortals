package com.mccraftaholics.warpportals.remote.tasks;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.remote.reports.ReportManager;

import java.io.InputStream;
import java.util.HashMap;

public abstract class AbstractRemoteRunnable implements Runnable {

    @Override
    public void run() {
        if (System.currentTimeMillis() - getLastSuccessTimestamp() > getFrequency()) {
            try {
                InputStream inputStream = Utils.urlGet(getApiEndpoint(), new HashMap<String, String>());
                success(inputStream);
            } catch (Exception e) {
                error(e);
            }
        }
    }

    public abstract String getApiEndpoint();

    public abstract long getFrequency();

    public abstract long getLastSuccessTimestamp();

    public abstract void success(InputStream response);

    public abstract void error(Exception e);

}
