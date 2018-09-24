package info.gianlucacosta.sagittarius.ivy;

import info.gianlucacosta.sagittarius.util.Version;

import java.nio.file.Path;

public class IvyFacade {

    private final IvyDescriptor ivyDescriptor;

    public IvyFacade(Path ivyDescriptorPath) {
        this.ivyDescriptor =
                new IvyDescriptor(ivyDescriptorPath);
    }


    public Version getRevision() {
        return ivyDescriptor.getRevision();
    }


    public void setupInfoForRelease() {
        Version releaseVersion =
                ivyDescriptor.getRevision();

        if (ivyDescriptor.getBranch() != null) {
            ivyDescriptor.setBranch(releaseVersion.toString());
        }

        ivyDescriptor.setStatus("release");

        ivyDescriptor.save();
    }


    public void ensureDependenciesAreNotOnHeadBranch() {
        for (IvyDependency ivyDependency : ivyDescriptor.getDependencies()) {

            String dependencyBranch =
                    ivyDependency.getBranch();

            if (dependencyBranch != null) {
                String upperCaseBranch =
                        dependencyBranch.toUpperCase();


                if (upperCaseBranch.contains(IvyDescriptor.headBranch.toUpperCase())) {
                    throw new RuntimeException(
                            String.format(
                                    "Dependency %s is invalid, as it's based on branch %s",
                                    ivyDependency,
                                    IvyDescriptor.headBranch
                            )
                    );
                }
            }
        }
    }


    public void setupNextIntegration() {
        Version nextIntegrationVersion =
                ivyDescriptor.getRevision().increase();

        ivyDescriptor.setRevision(nextIntegrationVersion);

        if (ivyDescriptor.getBranch() != null) {
            ivyDescriptor.setBranch(IvyDescriptor.headBranch);
        }

        ivyDescriptor.setStatus("integration");
        ivyDescriptor.save();
    }
}
