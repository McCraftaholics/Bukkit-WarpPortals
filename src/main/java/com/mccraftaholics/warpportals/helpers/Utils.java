package com.mccraftaholics.warpportals.helpers;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import org.bukkit.Location;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Utils {

    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static String formatISO(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601);
        return sdf.format(date);
    }

    public static Date parseIsoTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String join(String[] array, String sep, int first, int end) {
        if (!(first < end && array != null && array.length > 0 && sep != null)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = first; i < end; i++) {
            sb.append(array[i]).append(sep);
        }
        return sb.substring(0, sb.length() - sep.length());
    }

    public static boolean arrayContains(Object[] array, Object key) {
        for (Object object : array) {
            if (object.equals(key))
                return true;
        }
        return false;
    }

    public static String readStream(InputStream in, String encoding) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line).append("\n");
        }
        reader.close();
        return out.toString();
    }

    public static String readFile(String path, String encoding) throws IOException {
        return readStream(new FileInputStream(path), encoding);
    }

    public static boolean writeToFile(String data, File dataFile) {
        if (dataFile.canWrite()) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(dataFile.getAbsoluteFile());
                bw = new BufferedWriter(fw);
                bw.write(data);
                return true;
            } catch (IOException e) {
            } finally {
                if (bw != null)
                    try {
                        bw.close();
                    } catch (IOException e) {
                    }
            }
        }
        return false;
    }

    public static InputStream urlGet(String baseUrl, Map<String, String> params) throws IOException, URISyntaxException {
        StringBuilder queryParams = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            queryParams.append(param.getKey()).append("=").append(param.getValue());
        }
        URL url = new URL(baseUrl);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), queryParams.toString(), url.getRef());
        URLConnection connection = new URL(uri.toASCIIString()).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        return connection.getInputStream();
    }

    public static InputStream urlPost(String url, String contentType, byte[] data) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", contentType + ";charset=UTF-8");
        OutputStream output = connection.getOutputStream();
        try {
            output.write(data);
            return connection.getInputStream();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                output.close();
            } catch (IOException logOrIgnore) {
            }
        }
    }

    public static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }

    public static String[] ymlLevelCleanup(String[] orig, String startComb) {
        String[] comb = new String[orig.length];
        int i = 0;
        boolean fRun = true;
        for (String line : orig) {
            if (fRun) {
                comb[i] = line;
                fRun = false;
            } else {
                if (!line.startsWith(startComb))
                    i++;
                comb[i] = (comb[i] != null ? comb[i] + "\n" : "") + line;
            }
        }
        return comb;
    }

    public static void coordsToLoc(Coords coords, Location loc) {
        loc.setWorld(coords.world);
        loc.setX(coords.x);
        loc.setY(coords.y);
        loc.setZ(coords.z);
    }

    public static void coordsToLoc(CoordsPY coords, Location loc) {
        loc.setWorld(coords.world);
        loc.setX(coords.x);
        loc.setY(coords.y);
        loc.setZ(coords.z);
        loc.setPitch(coords.pitch);
        loc.setYaw(coords.yaw);
    }

}
