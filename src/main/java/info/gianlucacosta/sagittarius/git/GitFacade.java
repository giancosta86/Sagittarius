package info.gianlucacosta.sagittarius.git;

import info.gianlucacosta.sagittarius.util.Version;

public class GitFacade {
    private final String remote;
    private final String branch;


    public GitFacade(String remote, String branch) {
        this.remote = remote;
        this.branch = branch;
    }


    public void commitAndPushRelease(Version releaseVersion) {
        Git.addAll();

        String commitMessage =
                String.format(
                        "Release version %s",
                        releaseVersion
                );

        Git.commit(commitMessage);

        Git.push(remote, branch);
    }


    public void createAndPushTag(String tag) {
        Git.createTag(tag);

        Git.push(remote, tag);
    }


    public void pushNewIntegration(Version integrationVersion) {
        Git.addAll();

        String commitMessage =
                String.format(
                        "Introduce version %s",
                        integrationVersion
                );


        Git.commit(commitMessage);

        Git.push(remote, branch);
    }
}
