/**
 * Copyright 2011 Alexandre Dutra
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package fr.dutra.tools.maven.deptree.extras;

import java.io.File;
import java.io.FileInputStream;
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

import fr.dutra.tools.maven.deptree.core.Node;
import fr.dutra.tools.maven.deptree.core.VisitException;
import fr.dutra.tools.maven.deptree.core.Visitor;

public abstract class VelocityRenderer implements Visitor {

    private static final String STATIC_RESOURCES_BASE = "/static";

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
            staticDir = new File(this.getClass().getResource(STATIC_RESOURCES_BASE).toURI());
        } catch (URISyntaxException e) {
        }
        this.staticDir = staticDir;
    }

    protected File outputDir;

    protected String fileName = "index.html";

    protected String encoding = "UTF-8";

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getEncoding() {
        return encoding;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void visit(Node tree) throws VisitException {
        try {
            generateMainFile(tree);
            copyResources();
        } catch (UnsupportedEncodingException e) {
            throw new VisitException();
        } catch (IOException e) {
            throw new VisitException();
        }
    }

    protected void generateMainFile(Node tree) throws IOException, UnsupportedEncodingException {
        VelocityContext vc = createVelocityContext(tree);
        Template t = createVelocityTemplate();
        File mainFile = new File(getOutputDir(), getFileName());
        final FileOutputStream fos = new FileOutputStream(mainFile);
        final PrintWriter p = new PrintWriter(new OutputStreamWriter(fos, getEncoding()));
        try {
            t.merge(vc, p);
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

    protected VelocityContext createVelocityContext(Node tree) {
        VelocityContext vc = new VelocityContext();
        vc.put("tree", tree);
        return vc;
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

    protected void copyResources() throws IOException {
        String staticPath = staticDir.getAbsolutePath();
        final Collection<File> files = getFilesToCopy();
        final byte data[] = new byte[2048];
        for(final File src: files) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = new FileInputStream(src);
                String relativePath = StringUtils.substringAfter(src.getAbsolutePath(), staticPath);
                final File dest = new File(getOutputDir(), relativePath);
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
