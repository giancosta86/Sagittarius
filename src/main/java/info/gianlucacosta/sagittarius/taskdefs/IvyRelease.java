package info.gianlucacosta.sagittarius.taskdefs;

import info.gianlucacosta.sagittarius.ant.AntFacade;
import info.gianlucacosta.sagittarius.util.Version;
import org.apache.tools.ant.BuildException;


public class IvyRelease extends ReleaseRelatedTask {
    private String redownloadDependenciesTargets;


    public void setRedownloadDependenciesTargets(String redownloadDependenciesTargets) {
        this.redownloadDependenciesTargets = redownloadDependenciesTargets;
    }


    @Override
    public void execute() throws BuildException {
        super.execute();

        AntFacade antFacade =
                new AntFacade(project);

        ivyFacade.ensureDependenciesAreNotOnHeadBranch();

        if (redownloadDependenciesTargets != null) {
            antFacade.redownloadDependencies(redownloadDependenciesTargets);
        }

        ivyFacade.setupInfoForRelease();


        Version releaseVersion =
                ivyFacade.getRevision();

        gitFacade.commitAndPushRelease(releaseVersion);
    }
}
