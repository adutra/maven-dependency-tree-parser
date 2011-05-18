package fr.dutra.tools.maven.deptree.extras;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import fr.dutra.tools.maven.deptree.core.MavenDependencyTreeNode;
import fr.dutra.tools.maven.deptree.core.MavenDependencyTreeInputType;
import fr.dutra.tools.maven.deptree.core.MavenDependencyTreeParseException;
import fr.dutra.tools.maven.deptree.core.MavenDependencyTreeParser;
import fr.dutra.tools.maven.deptree.core.MavenDependencyTreeVisitException;


public class MavenDependencyTreeVelocityRendererMain {

    /**
     * Arguments list:
     * <ol>
     * <li>Full path of dependency tree file to parse;</li>
     * <li>Dependency tree file format; must be the (case-insensitive) name of a <code>{@link MavenDependencyTreeInputType}</code> enum;</li>
     * <li>Full path to the output directory where HTML files and static resources will be generated (<em>this directory will be erased</em>);</li>
     * <li>Renderer format; must be the (case-insensitive) name of a <code>{@link MavenDependencyTreeVelocityRenderType}</code> enum;</li>
     * </ol>
     * @param args
     * @throws MavenDependencyTreeParseException
     * @throws IOException
     * @throws MavenDependencyTreeVisitException
     */
    public static void main(String[] args) throws IOException, MavenDependencyTreeParseException, MavenDependencyTreeVisitException {
        MavenDependencyTreeInputType format = MavenDependencyTreeInputType.valueOf(args[1].toUpperCase());
        MavenDependencyTreeParser parser = format.newParser();
        MavenDependencyTreeNode tree = parser.parse(new FileReader(args[0]));
        File outputDir = new File(args[2]);
        FileUtils.deleteDirectory(outputDir);
        outputDir.mkdirs();
        MavenDependencyTreeVelocityRenderer renderer =
            MavenDependencyTreeVelocityRenderType.valueOf(args[3].toUpperCase()).newRenderer();
        renderer.setFileName("index.html");
        renderer.setOutputDir(outputDir);
        renderer.visit(tree);
    }

}
