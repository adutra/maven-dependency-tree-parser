====
    Copyright 2011 Alexandre Dutra

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
====

This library contains:

- A set of parsers that parse the output of the Maven command "mvn dependency:tree"; 
  different parsers can be used to parse the following output formats: text, dot, graphml and tgf. 
  See http://maven.apache.org/plugins/maven-dependency-plugin/tree-mojo.html for further details.
  The following idiom is the recommended way to parse any input:
  
  InputType type = ...;
  Reader r = ...;
  Parser parser = type.newParser();
  Node tree = parser.parse(r);
  
  Note that this library is NOT a drop-in replacement for the Maven Mercury 
  DependencyTreeBuilder API (see http://maven.apache.org/mercury/). 
  In other words, it does NOT make any dependency resolution whatsoever per itself;
  it simply takes an already-built dependency tree and parses it to an object representation
  that is lightweight and easily reusable.

- A set of visitors that can be used to scan the parsed tree. In particular, visitors can be 
  used to create human-readable representations of the parsed tree. E.g. the following code
  transforms the parsed tree back again to the Maven standard text format:

  Node tree = ...; 
  StandardTextVisitor visitor = new StandardTextVisitor();
  visitor.visit(tree);
  System.out.println(visitor);
        
- A set of Velocity renderers that create HTML representations of the parsed tree. These renderers rely upon JQuery (see http://jquery.com) and some JQuery plugins: 
  - JSTree: see http://www.jstree.com/
  - TreeTable: see http://plugins.jquery.com/project/treeTable
  These utilities are inteded as examples of HTML code; feel free to adapt them to your needs.
  To generate the HTML code and all the secondary resources (images, CSS files, etc.), run the class
  fr.dutra.tools.maven.deptree.extras.VelocityRendererMain with the following arguments:
  - Full path of dependency tree file to parse;
  - Dependency tree file format; one of: TEXT, DOT, GRAPHML or TGF;
  - Full path to the output directory where HTML files and static resources will be generated (this directory will be erased prior to file generation);
  - Renderer format; one of: 
    - JQUERY_JSTREE (uses JQuery plugin JSTree)
    - JQUERY_TREE_TABLE (uses JQuery plugin TreeTable)
