package fr.dutra.tools.maven.deptree.renderer;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.velocity.VelocityContext;

import fr.dutra.tools.maven.deptree.model.MavenDependencyNode;


public class JQueryJSTreeRenderer extends AbstractRenderer {

    private Map<String, String> imagesMap = new HashMap<String, String>();

    {
        imagesMap.put("jar", "img/common/jar.gif");
        imagesMap.put("war", "img/common/war.gif");
        imagesMap.put("ear", "img/common/ear.gif");
        imagesMap.put("pom", "img/common/pom.gif");
        imagesMap.put("plugin", "img/common/plugin.gif");
        imagesMap.put("bundle", "img/common/bundle.gif");
        imagesMap.put("default", "img/common/other.gif");
    }

    @Override
    protected String getTemplatePath() {
        return "/vm/jstree/index.vm";
    }

    @Override
    protected String getVelocityPropertiesPath() {
        return "/vm/jstree/velocity.properties";
    }

    @Override
    protected VelocityContext createVelocityContext(MavenDependencyNode tree, File outputDir) {
        VelocityContext context = super.createVelocityContext(tree, outputDir);
        context.put("imagesMap", imagesMap);
        return context;
    }

    @Override
    protected Collection<File> getFilesToCopy() {
        return FileUtils.listFiles(staticDir, FileFilterUtils.or(new PathContainsFilter("jstree"), new PathContainsFilter("common")), TrueFileFilter.TRUE);
    }

}
