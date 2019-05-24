# Shell script that generates class documentations.
# This is not very clean, but works for now.
# Generated documentations:
#   ./docs/java/    					- Java HTML documentation
#   ./docs/js/    					- JavaScript HTML documentation
#   ./docs/moveatis_java_class_documentation.pdf	- Java PDF documentation
#   ./docs/moveatis_js_class_documentation.html		- JavaScript one page HTML documentation

mkdir docs
mkdir docs/js
mkdir docs/js-onepage
mkdir docs/java-tex

jsdoc_path="../node_modules/jsdoc"
js_onepage_path="../node_modules/jsdoc-one-page"
js_out_path="docs/js"
js_onepage_out_path="docs/js-onepage"
js_src_path="src/main/webapp/META-INF/resources/js"
js_class_doc_file="docs/moveatis_js_class_documentation.html"
js_front_page="docs/js-front-page.md"

texdoclet_path="../TeXDoclet.jar"
tex_init_file="docs/tex_init.tex"
tex_out_path="docs/java-tex"
tex_out_file="moveatis_java_class_documentation.tex"
java_out_path="docs/java"
java_class_doc_file="moveatis_java_class_documentation.pdf"
java_src_path="src/main/java"

# JavaScript documentation front page

echo "
# Moveatis JavaScript Class Documentation
## Software version 2.0.0

## Documentation version 1.0.0

Moveatis is a web application designed to help the analysis of teaching situations by means of systematic observation. It was developed for the Department of Sport Pedagogy at University of Jyväskylä. Moveatis was written in Java and JavaScript programming languages. The JavaScript classes are documented here and the Java classes are described in a separate class documentation.

(c) Copyright 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio and Ilari Paananen.
(c) Copyright 2019, Visa Nykänen, Tuomas Moisio, Petra Puumala and Karoliina Lappalainen.
" > $js_front_page

# JavaScript documentation

$jsdoc_path -d $js_out_path $(find_cyg $js_src_path -name *.js) $js_front_page
$jsdoc_path -t $js_onepage_path -d $js_onepage_out_path $(find_cyg $js_src_path -name *.js) $js_front_page

echo "<!DOCTYPE html>
<html><head>
	<meta charset='utf-8'/>
	<title>Moveatis JavaScript Class Documentation</title> 
	<style>
		.back-to-top { display: none; }
		td { padding: 0 0.5em; }
		footer { padding-top: 2em; }
	</style>
</head><body>" > $js_class_doc_file

cat $js_onepage_out_path/api_content_only.html >> $js_class_doc_file

grep -A 2 "<footer>" $js_onepage_out_path/index.html >> $js_class_doc_file

echo "<script>
function appendToc(toc, heading, items) {
	var h3 = document.createElement('h3');
	h3.appendChild(document.createTextNode(heading));
	toc.appendChild(h3);
	var ul = document.createElement('ul');	
	for (var i = 0; i < items.length; i++) {
		var li = document.createElement('li');
		li.appendChild(document.createTextNode(items[i]));
		ul.appendChild(li);
	}
	toc.appendChild(ul);
}
(function() {
	var sections = document.getElementsByTagName('section');
	var classes = [];
	var modules = [];
	for (var i = 0; i < sections.length; i++) {
		var s = sections[i];
		var id = s.getAttribute('id');
		if (id === null) continue;
		var name;
		if (id.startsWith('class')) {
			name = id.slice(id.indexOf('~') + 1);
			classes.push(name);
			name = 'Class: ' + name;
		} else if (id.startsWith('module')) {
			name = id.slice(id.indexOf(':') + 1);
			modules.push(name);
			name = 'Module: ' + name;
		}
		var h1 = document.createElement('h1');
		h1.appendChild(document.createTextNode(name));
		s.parentElement.insertBefore(h1, s);
	}
	var toc = document.createElement('section');
	var h2 = document.createElement('h2');
	h2.appendChild(document.createTextNode('Table of Contents'));
	toc.appendChild(h2);
	appendToc(toc, 'Classes', classes);
	appendToc(toc, 'Modules', modules);
	sections[0].parentElement.insertBefore(toc, sections[0].nextSibling);
	
	var headers = document.getElementsByTagName('header');
	while (headers.length) {
		headers[0].remove();
		headers = document.getElementsByTagName('header');
	}
})();
</script></body></html>" >> $js_class_doc_file

# Java documentation

javadoc -d $java_out_path -sourcepath $java_src_path -subpackages com
mvn generate-sources javadoc:javadoc
cp -r target/site/apidocs/ docs/java/

echo "\\usepackage[utf8]{inputenc}" > $tex_init_file

javadoc -docletpath $texdoclet_path -doclet org.stfm.texdoclet.TeXDoclet \
-texinit $tex_init_file \
-tree -output $tex_out_path/$tex_out_file \
-title "Moveatis Java Class Documentation\\\\Software version 2.0.0" \
-subtitle "Version 1.0.0" \
-author "Jarmo Juuj\\""\"arvi\\\\Sami Kallio\\\\Kai Korhonen\\\\Juha Moisio\\\\Ilari Paananen\\\\Visa Nyk\\""\"anen\\\\Petra Puumala\\\\ Karoliina Lappalainen\\\\Tuomas Moisio" \
-nosummaries \
-sourcepath $java_src_path -subpackages com

cd $tex_out_path

pdflatex $tex_out_file
pdflatex $tex_out_file

cp $java_class_doc_file ../

