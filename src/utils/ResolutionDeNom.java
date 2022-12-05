package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ResolutionDeNom {

    /**
     * TODO
     *
     * @param host
     * @return
     */
    public static String getIPAddress(String host) {
        String address = null;
        try {
            address = InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }
}
