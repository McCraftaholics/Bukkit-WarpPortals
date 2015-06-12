package com.mccraftaholics.warpportals.remote.tasks;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.common.model.WarpPortalsVersion;
import com.mccraftaholics.warpportals.remote.RemoteConstants;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class VersionTestRunnable extends AbstractRemoteRunnable {

    Gson gson;
    String channel;
    PortalPlugin plugin;

    public VersionTestRunnable(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String getApiEndpoint() {
        return RemoteConstants.BASE_API_URL() + "/version?channel=" + channel;
    }

    @Override
    public long getFrequency() {
        return RemoteConstants.MILLIS_PER_DAY / 24;
    }

    @Override
    public long getLastSuccessTimestamp() {
        return 0;
    }

    @Override
    public void success(InputStream response) {
        try {
            WarpPortalsVersion wpVersion = gson.fromJson(new InputStreamReader(response, "UTF-8"), WarpPortalsVersion.class);
            // TODO what's going on here?
            String hi = wpVersion.version;
        } catch (UnsupportedEncodingException e) {
            // Should never be called
        }
    }

    @Override
    public void error(Exception e) {

    }
}
