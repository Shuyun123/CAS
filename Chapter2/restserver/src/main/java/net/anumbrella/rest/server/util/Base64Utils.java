package net.anumbrella.rest.server.util;


import java.util.Base64;

/**
 * @author anumbrella
 */
public class Base64Utils {


    /**
     * 转换为base64
     *
     * @param msg
     * @return
     */
    public static String encoder(String msg) {
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }

    /**
     * 解码base64
     *
     * @param msg
     * @return
     */
    public static String decoder(String msg) {
        return new String(Base64.getDecoder().decode(msg));
    }
}

