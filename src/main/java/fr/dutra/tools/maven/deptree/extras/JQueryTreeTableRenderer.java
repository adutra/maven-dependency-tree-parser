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
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;


public class JQueryTreeTableRenderer extends VelocityRenderer {

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
