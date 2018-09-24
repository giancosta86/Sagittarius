package info.gianlucacosta.sagittarius.taskdefs;

import info.gianlucacosta.sagittarius.git.GitFacade;
import info.gianlucacosta.sagittarius.ivy.IvyFacade;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.nio.file.Paths;

public abstract class ReleaseRelatedTask extends Task {
    protected String ivyDescriptorPath =
            "ivy.xml";

    protected String remote =
            "origin";

    protected String branch =
            "master";

    protected Project project;


    protected String ivyOrganisation;

    protected String ivyModule;

    protected String ivyRevision;


    protected GitFacade gitFacade;

    protected IvyFacade ivyFacade;


    public void setIvyDescriptorPath(String ivyDescriptorPath) {
        this.ivyDescriptorPath = ivyDescriptorPath;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public void execute() throws BuildException {
        ivyOrganisation =
                project.getProperty("ivy.organisation");

        if (ivyOrganisation == null) {
            throw new RuntimeException("You need to perform an Ivy resolution task before this one");
        }


        ivyModule =
                project.getProperty("ivy.module");

        ivyRevision =
                project.getProperty("ivy.revision");


        gitFacade =
                new GitFacade(remote, branch);

        ivyFacade =
                new IvyFacade(
                        Paths.get(ivyDescriptorPath)
                );
    }
}
