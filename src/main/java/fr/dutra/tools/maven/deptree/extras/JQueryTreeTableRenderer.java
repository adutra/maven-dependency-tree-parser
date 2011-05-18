package fr.dutra.tools.maven.deptree.extras;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;


public class JQueryTreeTableRenderer extends MavenDependencyTreeVelocityRenderer {

    @Override
    protected String getTemplatePath() {
        return "/vm/treeTable/index.vm";
    }

    @Override
    protected String getVelocityPropertiesPath() {
        return "/vm/treeTable/velocity.properties";
    }

    @Override
    protected Collection<File> getFilesToCopy() {
        return FileUtils.listFiles(staticDir, FileFilterUtils.or(new PathContainsFilter("treeTable"), new PathContainsFilter("common")), TrueFileFilter.TRUE);
    }

}
