package info.gianlucacosta.sagittarius.ivy;

import java.util.Objects;

class IvyDependency {
    private final String organisation;
    private final String moduleName;
    private final String revision;
    private final String branch;


    public IvyDependency(String organisation, String moduleName, String revision, String branch) {
        Objects.requireNonNull(organisation);
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(revision);

        this.organisation = organisation;
        this.moduleName = moduleName;
        this.revision = revision;
        this.branch = branch;
    }


    public String getOrganisation() {
        return organisation;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getRevision() {
        return revision;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public String toString() {

        return branch != null ?
                String.format(
                        "%s:%s:%s [%s]",
                        organisation,
                        moduleName,
                        revision,
                        branch
                )
                :
                String.format(
                        "%s:%s:%s",
                        organisation,
                        moduleName,
                        revision
                );
    }
}
