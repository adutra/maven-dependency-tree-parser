/**
 * 
 */
package fr.dutra.tools.maven.deptree.renderer;

public enum RenderType {

    JQUERY_JSTREE {

        @Override
        public AbstractRenderer newRenderer() {
            return new JQueryJSTreeRenderer();
        }
    },

    JQUERY_TREE_TABLE {

        @Override
        public AbstractRenderer newRenderer() {
            return new JQueryTreeTableRenderer();
        }
    };

    public abstract AbstractRenderer newRenderer();
}