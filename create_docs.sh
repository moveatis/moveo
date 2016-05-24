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

