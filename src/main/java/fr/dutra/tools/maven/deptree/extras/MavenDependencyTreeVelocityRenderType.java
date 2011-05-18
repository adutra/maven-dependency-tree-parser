/**
 * 
 */
package fr.dutra.tools.maven.deptree.extras;

public enum MavenDependencyTreeVelocityRenderType {

    JQUERY_JSTREE {

        @Override
        public MavenDependencyTreeVelocityRenderer newRenderer() {
            return new JQueryJSTreeRenderer();
        }
    },

    JQUERY_TREE_TABLE {

        @Override
        public MavenDependencyTreeVelocityRenderer newRenderer() {
            return new JQueryTreeTableRenderer();
        }
    };

    public abstract MavenDependencyTreeVelocityRenderer newRenderer();
}