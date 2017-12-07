# Maven Dependency Tree Parser

__WARNING: This project is not actively maintained anymore.__

This library contains:

- A set of parsers that parse the output of the Maven command `mvn dependency:tree`; 
  different parsers can be used to parse the following output formats: `text`, `dot`, `graphml` and `tgf`. 
  See the [Maven Dependency Plugin Tree Mojo](http://maven.apache.org/plugins/maven-dependency-plugin/tree-mojo.html) page 
  for further details.
  
To use the parser, one needs to generate a file containing the tree to parse:

```bash
mvn dependency:tree -DoutputFile=/path/to/file -DoutputType=text
```  
  
Then the following idiom is the recommended way to parse the file:
  
```java
InputType type = InputType.TEXT;
Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(...), "UTF-8"));
Parser parser = type.newParser();
Node tree = parser.parse(r);
```
  
Note that this library does NOT make any dependency resolution whatsoever per itself;
it simply takes an already-built dependency tree and parses it to an object representation
that is lightweight and easily reusable.

- A set of visitors that can be used to scan the parsed tree. In particular, visitors can be 
  used to create human-readable representations of the parsed tree. E.g. the following code
  transforms the parsed tree back again to the Maven standard text format:

```java
Node tree = ...; 
StandardTextVisitor visitor = new StandardTextVisitor();
visitor.visit(tree);
System.out.println(visitor);
```
        
- A set of Velocity renderers that create HTML representations of the parsed tree. These renderers rely upon [JQuery](http://jquery.com) and some JQuery plugins: 
  - JSTree: see http://www.jstree.com/
  - TreeTable: see http://plugins.jquery.com/project/treeTable (note: this plugin seems dead as of 2017)

These utilities are intended as examples of HTML code; feel free to adapt them to your needs.
To generate the HTML code and all the secondary resources (images, CSS files, etc.), run the class
`fr.dutra.tools.maven.deptree.extras.VelocityRendererMain` with the following arguments:
  - Full path of dependency tree file to parse;
  - Dependency tree file format; one of: `TEXT`, `DOT`, `GRAPHML` or `TGF`;
  - Full path to the output directory where HTML files and static resources will be generated (this directory will be erased prior to file generation);
  - Renderer format; one of: 
    - `JQUERY_JSTREE` (uses JQuery plugin JSTree)
    - `JQUERY_TREE_TABLE` (uses JQuery plugin TreeTable)
