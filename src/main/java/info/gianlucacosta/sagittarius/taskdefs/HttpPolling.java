package info.gianlucacosta.sagittarius.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPolling extends Task {
    private static final int HTTP_SUCCESS =
            200;

    private String url;
    private String username;
    private String password;
    private int maxPolls;
    private long retryWaitInMillis;

    private Project project;


    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxPolls(int maxPolls) {
        this.maxPolls = maxPolls;
    }

    public void setRetryWaitInMillis(long retryWaitInMillis) {
        this.retryWaitInMillis = retryWaitInMillis;
    }

    @Override
    public void setProject(Project project) {
        this.project = project;
    }


    @Override
    public void execute() throws BuildException {
        URL actualUrl;

        try {
            actualUrl =
                    new URL(url);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


        for (int pollCount = 1; pollCount <= maxPolls; pollCount++) {
            int httpStatus =
                    checkUrlViaHttp(actualUrl);

            if (httpStatus == HTTP_SUCCESS) {
                return;
            }

            if (pollCount < maxPolls) {
                try {
                    Thread.sleep(retryWaitInMillis);
                } catch (InterruptedException ex) {
                    //Just do nothing
                }
            }
        }

        throw new RuntimeException(
                String.format(
                        "Cannot poll URL '%s'",
                        url
                )
        );
    }


    private int checkUrlViaHttp(URL actualUrl) {
        try {
            HttpURLConnection httpConnection =
                    (HttpURLConnection) actualUrl.openConnection();

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