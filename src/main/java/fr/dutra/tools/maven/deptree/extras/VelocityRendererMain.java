package fr.dutra.tools.maven.deptree.extras;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import fr.dutra.tools.maven.deptree.core.Node;
import fr.dutra.tools.maven.deptree.core.InputType;
import fr.dutra.tools.maven.deptree.core.ParseException;
import fr.dutra.tools.maven.deptree.core.Parser;
import fr.dutra.tools.maven.deptree.core.VisitException;


public class VelocityRendererMain {

    /**
     * Arguments list:
     * <ol>
     * <li>Full path of dependency tree file to parse;</li>
     * <li>Dependency tree file format; must be the (case-insensitive) name of a <code>{@link InputType}</code> enum;</li>
     * <li>Full path to the output directory where HTML files and static resources will be generated (<em>this directory will be erased</em>);</li>
     * <li>Renderer format; must be the (case-insensitive) name of a <code>{@link VelocityRenderType}</code> enum;</li>
     * </ol>
     * @param args
     * @throws ParseException
     * @throws IOException
     * @throws VisitException
     */
    public static void main(String[] args) throws IOException, ParseException, VisitException {
        InputType format = InputType.valueOf(args[1].toUpperCase());
        Parser parser = format.newParser();
        Node tree = parser.parse(new FileReader(args[0]));
        File outputDir = new File(args[2]);
        FileUtils.deleteDirectory(outputDir);
        outputDir.mkdirs();
        VelocityRenderer renderer =
            VelocityRenderType.valueOf(args[3].toUpperCase()).newRenderer();
        renderer.setFileName("index.html");
        renderer.setOutputDir(outputDir);
        renderer.visit(tree);
    }

}
