package info.gianlucacosta.sagittarius.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Ant;

import java.util.Objects;

public class AntFacade {
    private final Project project;

    public AntFacade(Project project) {
        Objects.requireNonNull(project);

        this.project = project;
    }


    public void redownloadDependencies(String redownloadDependenciesTargetsString) {
        Objects.requireNonNull(redownloadDependenciesTargetsString);

        String[] redownloadDependenciesTargets =
                redownloadDependenciesTargetsString.split("\\s*,\\s*");


        for (String redownloadDependenciesTarget : redownloadDependenciesTargets) {
            executeTarget(redownloadDependenciesTarget);
        }
    }


    private void executeTarget(String target) {
        Ant ant =
                new Ant();

        ant.setProject(project);

        ant.setTarget(target);

        ant.execute();
    }
}
