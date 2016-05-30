# JavaScript documentation

jsdoc_path="../node_modules/.bin/jsdoc"
onepage_path="../node_modules/jsdoc-one-page"
out_path="jsdocs"
src_path="src/main/webapp/META-INF/resources/js"

$jsdoc_path -d $out_path \
$src_path/control.js $src_path/locales.js $src_path/observer.js $src_path/summary.js

$jsdoc_path -t $onepage_path -d $out_path/onepage \
$src_path/control.js $src_path/locales.js $src_path/observer.js $src_path/summary.js

# Java documentation

mkdir texdocs

javadoc -docletpath ../TeXDoclet.jar -doclet org.stfm.texdoclet.TeXDoclet \
-tree -output texdocs/docs.tex \
-title "Moveatis Class Documentation" \
-subtitle "Version 0.0.1" \
-author "Jarmo Juuj\\""\"arvi\\\\Sami Kallio\\\\Kai Korhonen\\\\Juha Moisio\\\\Ilari Paananen" \
-sourcepath src/main/java -subpackages com

cd texdocs

pdflatex docs.tex
pdflatex docs.tex

