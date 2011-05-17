package fr.dutra.tools.maven.deptree.renderer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.exception.VelocityException;

import fr.dutra.tools.maven.deptree.model.MavenDependencyNode;
import fr.dutra.tools.maven.deptree.model.MavenDependencyNodeParseException;
import fr.dutra.tools.maven.deptree.model.MavenDependencyTreeFormat;
import fr.dutra.tools.maven.deptree.model.MavenDependencyTreeParser;


public class RendererMain {

    /**
     * Arguments list:
     * <ol>
     * <li>Full path of dependency tree file to parse;</li>
     * <li>Dependency tree file format; must be the (case-insensitive) name of a <code>{@link MavenDependencyTreeFormat}</code> enum;</li>
     * <li>Full path to the output directory where HTML files and static resources will be generated (<em>this directory will be erased</em>);</li>
     * <li>Renderer format; must be the (case-insensitive) name of a <code>{@link RenderType}</code> enum;</li>
     * </ol>
     * @param args
     * @throws VelocityException
     * @throws IOException
     * @throws MavenDependencyNodeParseException
     */
    public static void main(String[] args) throws VelocityException, IOException, MavenDependencyNodeParseException {
        File outputDir = new File(args[2]);
        FileUtils.deleteDirectory(outputDir);
        outputDir.mkdirs();
        MavenDependencyTreeFormat format = MavenDependencyTreeFormat.valueOf(args[1].toUpperCase());
        MavenDependencyTreeParser parser = format.newParser();
        MavenDependencyNode tree = parser.parse(new FileReader(args[0]));
        AbstractRenderer renderer = RenderType.valueOf(args[3].toUpperCase()).newRenderer();
        renderer.render(tree, outputDir);
    }

}
