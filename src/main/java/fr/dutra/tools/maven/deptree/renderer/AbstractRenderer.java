package fr.dutra.tools.maven.deptree.renderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import fr.dutra.tools.maven.deptree.model.MavenDependencyNode;

public abstract class AbstractRenderer {

    protected final class PathContainsFilter implements IOFileFilter {

        private String search;

        public PathContainsFilter(String search) {
            super();
            this.search = search;
        }

        @Override
        public boolean accept(File file) {
            return file.getAbsolutePath().contains(search);
        }

        @Override
        public boolean accept(File dir, String name) {
            return accept(new File(dir, name));
        }
    }

    protected final File staticDir;

    {
        File staticDir = null;
        try {
            staticDir = new File(this.getClass().getResource("/static").toURI());
        } catch (URISyntaxException e) {
        }
        this.staticDir = staticDir;
    }

    public void render(MavenDependencyNode tree, File outputDir) throws VelocityException, IOException {
        generateMainFile(tree, outputDir);
        copyResources(outputDir);
    }

    protected void generateMainFile(MavenDependencyNode tree, File outputDir) throws IOException, FileNotFoundException, UnsupportedEncodingException {
        VelocityContext context = createVelocityContext(tree, outputDir);
        Template t = createVelocityTemplate();
        final FileOutputStream fos = new FileOutputStream(new File(outputDir, "index.html"));
        final PrintWriter p = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));
        try {
            t.merge(context, p);
        } finally {
            p.close();
            fos.close();
        }
    }

    protected Template createVelocityTemplate() throws IOException, VelocityException {
        VelocityEngine engine = createVelocityEngine();
        Template t;
        try {
            t = engine.getTemplate(getTemplatePath());
        } catch (IOException e) {
            throw e;
        } catch (VelocityException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new VelocityException(e);
        }
        return t;
    }

    protected abstract String getTemplatePath();

    protected VelocityContext createVelocityContext(MavenDependencyNode tree, File outputDir) {
        VelocityContext context = new VelocityContext();
        context.put("tree", tree);
        context.put("outputDir", outputDir);
        return context;
    }

    protected VelocityEngine createVelocityEngine() throws IOException, VelocityException {
        VelocityEngine velocityEngine = new VelocityEngine();
        Properties props = getVelocityProperties();
        try {
            velocityEngine.init(props);
        } catch (IOException e) {
            throw e;
        } catch (VelocityException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new VelocityException(e);
        }

        return velocityEngine;
    }

    protected Properties getVelocityProperties() throws IOException {
        Properties props = new Properties();
        InputStream is = getClass().getResourceAsStream(getVelocityPropertiesPath());
        props.load(is);
        return props;
    }

    protected abstract String getVelocityPropertiesPath();

    protected void copyResources(File outputDir) throws IOException {
        String staticPath = staticDir.getAbsolutePath();
        final Collection<File> files = getFilesToCopy();
        final byte data[] = new byte[2048];
        for(final File src: files) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = new FileInputStream(src);
                String relativePath = StringUtils.substringAfter(src.getAbsolutePath(), staticPath);
                final File dest = new File(outputDir, relativePath);
                final File parentFile = dest.getParentFile();
                parentFile.mkdirs();
                fos = new FileOutputStream(dest);
                int count;
                while ((count = is.read(data, 0, 2048)) != -1) {
                    fos.write(data, 0, count);
                }
                fos.flush();
            } finally {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    protected Collection<File> getFilesToCopy() {
        return FileUtils.listFiles(staticDir, null, true);
    }

}
