/**
 * 
 */
package fr.dutra.tools.maven.deptree.extras;

public enum VelocityRenderType {

    JQUERY_JSTREE {

        @Override
        public VelocityRenderer newRenderer() {
            return new JQueryJSTreeRenderer();
        }
    },

    JQUERY_TREE_TABLE {

        @Override
        public VelocityRenderer newRenderer() {
            return new JQueryTreeTableRenderer();
        }
    };

    public abstract VelocityRenderer newRenderer();
}