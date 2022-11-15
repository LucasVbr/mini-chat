package reseau;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ResolutionDeNom {

    public static InetAddress getAddress(String host) {
        InetAddress address;
        try {
            address = InetAddress.getByName(host);
            return address;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        String input = args[0];
        String input = "www.google.com";
        InetAddress address = getAddress(input);
        if (address != null) {
            System.out.printf("%s : %s\n", input, address.getHostAddress());
        }
    }
}
