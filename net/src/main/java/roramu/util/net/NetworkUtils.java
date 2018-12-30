package roramu.util.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

/**
 *
 */
public final class NetworkUtils {
    private static final String[] ipServices = new String[] {
        "http://checkip.amazonaws.com",
        "http://icanhazip.com",
        "http://www.trackip.net/ip",
        "http://myexternalip.com/raw",
        "http://ipecho.net/plain",
        "http://bot.whatismyipaddress.com"
    };

    private NetworkUtils() {}

    /**
     * Tries to get the external IP address of the machine this is running on.
     *
     * @return Either an IPv4 or IPv6 address if successful, otherwise null.
     */
    public static InetAddress tryGetExternalAddress() {
        for (String service : ipServices) {
            try {
                URL url = new URL(service);
                String ipAddressString;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
                    ipAddressString = in.readLine();
                }
                InetAddress ipAddress = InetAddress.getByName(ipAddressString);

                return ipAddress;
            } catch (Exception ex) {
                // Swallow exception so we return null
            }
        }

        return null;
    }

    /**
     * Throws an exception if the provided port number is invalid, i.e. if it is
     * negative or greater than 65535. This method is provided in addition to
     * {@link #validatePort(int, boolean)} to allow a more meaningful exception
     * message to be thrown.
     *
     * @param port The port number.
     */
    public static void validatePort(int port) {
        NetworkUtils.validatePort(port, false);
    }

    /**
     * Throws an exception if the provided port number is invalid, i.e. if it is
     * negative or greater than 65535. This method is provided in addition to
     * {@link #validatePort(int)} to allow a more meaningful exception message
     * to be thrown.
     *
     * @param port The port number.
     * @param rejectWellKnownPorts Whether or not to also reject the port if it
     * is in the range of well-known port numbers (i.e. if {@literal 0 <= port < 1024}).
     * This is defaulted to false.
     */
    public static void validatePort(int port, boolean rejectWellKnownPorts) {
        if (port < 0) {
            throw new IllegalArgumentException("'port' cannot be negative");
        }
        if (port > 65535) {
            throw new IllegalArgumentException("'port' cannot be greater than 65535");
        }
        if (rejectWellKnownPorts && port < 1024) {
            throw new IllegalArgumentException("'port' cannot be less than 1024, as they are reserved");
        }
    }

    /**
     * Checks whether the provided port number is invalid, i.e. if it is
     * negative or greater than 65535.
     *
     * @param port The port number.
     * @return True if the port number is valid, otherwise false.
     */
    public static boolean isValidPort(int port) {
        return NetworkUtils.isValidPort(port, false);
    }

    /**
     * Checks whether the provided port number is invalid, i.e. if it is
     * negative or greater than 65535.
     *
     * @param port The port number.
     * @param rejectWellKnownPorts Whether or not to also reject the port if it
     * is in the range of well-known port numbers (i.e. if {@literal 0 <= port < 1024}).
     * This is defaulted to false.
     * @return True if the port number is valid, otherwise false.
     */
    public static boolean isValidPort(int port, boolean rejectWellKnownPorts) {
        if (port < 0) {
            return false;
        }
        if (port > 65535) {
            return false;
        }
        if (rejectWellKnownPorts && port < 1024) {
            return false;
        }

        return true;
    }
}
