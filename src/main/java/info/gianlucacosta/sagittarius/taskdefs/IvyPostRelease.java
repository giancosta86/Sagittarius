package info.gianlucacosta.sagittarius.taskdefs;

import info.gianlucacosta.sagittarius.util.Version;
import org.apache.tools.ant.BuildException;

public class IvyPostRelease extends ReleaseRelatedTask {
    private String artifactUrlTemplate;

    private String artifactUrlUsername;
    private String artifactUrlPassword;

    private int artifactUrlMaxPolls =
            10;

    private long artifactUrlRetryWaitInMillis =
            20000;

    private String tagTemplate =
            "v%REVISION%";


    public void setArtifactUrlTemplate(String artifactUrlTemplate) {
        this.artifactUrlTemplate = artifactUrlTemplate;
    }

    public void setArtifactUrlUsername(String artifactUrlUsername) {
        this.artifactUrlUsername = artifactUrlUsername;
    }

    public void setArtifactUrlPassword(String artifactUrlPassword) {
        this.artifactUrlPassword = artifactUrlPassword;
    }

    public void setArtifactUrlMaxPolls(int artifactUrlMaxPolls) {
        this.artifactUrlMaxPolls = artifactUrlMaxPolls;
    }

    public void setArtifactUrlRetryWaitInMillis(long artifactUrlRetryWaitInMillis) {
        this.artifactUrlRetryWaitInMillis = artifactUrlRetryWaitInMillis;
    }


    public void setTagTemplate(String tagTemplate) {
        this.tagTemplate = tagTemplate;
    }


    @Override
    public void execute() throws BuildException {
        super.execute();

        if (artifactUrlTemplate != null) {
            pollForArtifact();
        }


        String tag =
                tagTemplate.replaceAll(
                        "%REVISION%",
                        ivyRevision
                );

        gitFacade.createAndPushTag(tag);


        ivyFacade.setupNextIntegration();

        Version nextIntegrationVersion =
                ivyFacade.getRevision();

        gitFacade.pushNewIntegration(nextIntegrationVersion);
    }


    private void pollForArtifact() {
        project.log("Now polling for the artifact - it may take some time...");

        String artifactUrl =
                artifactUrlTemplate
                        .replaceAll("%ORGANISATION%", ivyOrganisation)
                        .replaceAll("%MODULE%", ivyModule)
                        .replaceAll("%REVISION%", ivyRevision);


        HttpPolling httpPolling =
                new HttpPolling();

        httpPolling.setProject(project);

        httpPolling.setUrl(artifactUrl);
        httpPolling.setUsername(artifactUrlUsername);
        httpPolling.setPassword(artifactUrlPassword);
        httpPolling.setMaxPolls(artifactUrlMaxPolls);
        httpPolling.setRetryWaitInMillis(artifactUrlRetryWaitInMillis);


        httpPolling.execute();
    }
}
