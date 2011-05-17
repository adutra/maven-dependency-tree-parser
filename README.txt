This library contains:

- A set of parsers that parse the output of the maven command "mvn dependency:tree"; 
  different parsers can be used to parse the following output formats: text, dot, graphml and tgf. 
  See http://maven.apache.org/plugins/maven-dependency-plugin/tree-mojo.html for further details.
  The following idiom is the recommended way to parse any input:
  
  MavenDependencyTreeFormat format = ...;
  MavenDependencyTreeParser parser = format.newParser();
  Reader r = ...
  MavenDependencyNode tree = parser.parse(r);
  

- A set of utilities to create HTML representations of the parsed tree. These utilities rely upon JQuery (see http://jquery.com) and some JQuery plugins: 
  - jstree: see http://www.jstree.com/
  - treeTable: see http://plugins.jquery.com/project/treeTable