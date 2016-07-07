package com.kuoruan.bomberman.util;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Window10 on 2016/3/14.
 */
public class DataUtil {
    public static String streamToString(InputStream is) {
        byte[] bytes = new byte[1024];
        int length = 0;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
            }
            os.flush();
            String result = new String(os.toByteArray(), "UTF-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] TCPJsonToBytes(JSONObject jsonObject) {
        String data = jsonObject.toString() + "\n";
        return data.getBytes();
    }

}
