package info.gianlucacosta.sagittarius.util;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPoller {
    private static final int HTTP_SUCCESS =
            200;

    private final URL url;
    private final String username;
    private final String password;
    private final int maxPolls;
    private final long retryWaitInMillis;


    public HttpPoller(
            String url,
            String username,
            String password,
            int maxPolls,
            long retryWaitInMillis
    ) {
        try {
            this.url =
                    new URL(url);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        this.username = username;
        this.password = password;
        this.maxPolls = maxPolls;
        this.retryWaitInMillis = retryWaitInMillis;
    }


    public boolean poll() {

        for (int pollCount = 1; pollCount <= maxPolls; pollCount++) {
            int httpStatus =
                    checkUrlViaHttp();

            if (httpStatus == HTTP_SUCCESS) {
                return true;
            }

            if (pollCount < maxPolls) {
                try {
                    Thread.sleep(retryWaitInMillis);
                } catch (InterruptedException ex) {
                    //Just do nothing
                }
            }
        }

        return false;
    }


    private int checkUrlViaHttp() {
        try {
            HttpURLConnection httpConnection =
                    (HttpURLConnection) url.openConnection();

            try {
                if (username != null) {
                    setupBasicAuthentication(httpConnection, username, password);
                }


                httpConnection.setRequestMethod("HEAD");

                return httpConnection.getResponseCode();
            } finally {
                httpConnection.disconnect();
            }

        } catch (IOException ex) {
            return -1;
        }
    }


    private void setupBasicAuthentication(
            HttpURLConnection httpConnection,
            String username,
            String password
    ) {
        try {
            String authenticationString =
                    username + ":" + password;

            byte[] authenticationBytes =
                    authenticationString.getBytes("UTF-8");

            String encodedAuthenticationString =
                    DatatypeConverter.printBase64Binary(authenticationBytes);

            httpConnection.setRequestProperty(
                    "Authorization",
                    "Basic " + encodedAuthenticationString
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
