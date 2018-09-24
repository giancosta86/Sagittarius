package info.gianlucacosta.sagittarius.ivy;

import info.gianlucacosta.sagittarius.util.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

class IvyDescriptor {
    private static final String branchAttribute =
            "branch";

    private static final String statusAttribute =
            "status";

    private static final String infoRevisionAttribute =
            "revision";

    static final String headBranch =
            "HEAD";

    private final Path descriptorPath;

    private final Document descriptorDocument;
    private final Element infoNode;
    private final NodeList dependencyNodes;


    public IvyDescriptor(Path descriptorPath) {
        this.descriptorPath = descriptorPath;

        try {
            DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder =
                    documentBuilderFactory.newDocumentBuilder();

            descriptorDocument =
                    documentBuilder.parse(descriptorPath.toFile());

            infoNode =
                    (Element) descriptorDocument.getElementsByTagName("info").item(0);

            dependencyNodes =
                    descriptorDocument.getElementsByTagName("dependency");

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public Version getRevision() {
        String revisionString =
                infoNode.getAttribute(infoRevisionAttribute);

        return Version.parse(
                revisionString
        );
    }


    public void setRevision(Version version) {
        infoNode.setAttribute(infoRevisionAttribute, version.toString());
    }


    public String getBranch() {
        if (!infoNode.hasAttribute(branchAttribute)) {
            return null;
        }

        return infoNode.getAttribute(branchAttribute);
    }

    public void setBranch(String branch) {
        infoNode.setAttribute(branchAttribute, branch);
    }


    public void setStatus(String status) {
        infoNode.setAttribute(statusAttribute, status);
    }


    public List<IvyDependency> getDependencies() {
        List<IvyDependency> result =
                new ArrayList<>();

        for (int nodeIndex = 0; nodeIndex < dependencyNodes.getLength(); nodeIndex++) {
            Element dependencyNode =
                    (Element) dependencyNodes.item(nodeIndex);

            IvyDependency ivyDependency =
                    new IvyDependency(
                            dependencyNode.getAttribute("org"),
                            dependencyNode.getAttribute("name"),
                            dependencyNode.getAttribute("rev"),

                            dependencyNode.hasAttribute(branchAttribute) ?
                                    dependencyNode.getAttribute(branchAttribute)
                                    :
                                    null
                    );


            result.add(ivyDependency);
        }


        return result;
    }


    public void save() {
        try {
            saveDom();

            fixSavedXml();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private void saveDom() throws TransformerException {
        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        Transformer transformer =
                transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource domSource =
                new DOMSource(descriptorDocument);

        StreamResult result =
                new StreamResult(descriptorPath.toFile());

        transformer.transform(domSource, result);
    }


    private void fixSavedXml() throws IOException {
        String descriptorString =
                new String(
                        Files.readAllBytes(descriptorPath),
                        "utf-8"
                );

        String fixedDescriptorString =
                descriptorString
                        .replaceAll("&gt;", ">");

        Files.write(
                descriptorPath,
                fixedDescriptorString.getBytes(),
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
