mkdir texdocs

javadoc -docletpath ../TeXDoclet.jar -doclet org.stfm.texdoclet.TeXDoclet -noindex -tree -hyperref -output texdocs/docs.tex -title "Moveatis API Documentation" -author "Moveatis" -sourcepath src/main/java -subpackages com

cd texdocs

pdflatex docs.tex
pdflatex docs.tex

