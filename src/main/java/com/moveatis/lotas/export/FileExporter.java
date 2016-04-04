package com.moveatis.lotas.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@ManagedBean(name="fileExporter")
@ViewScoped
public class FileExporter implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileExporter.class);

    private StreamedContent file;
    private final String fileName = "testi.csv";

    /**
     * Creates a new instance of FileExporter
     */
    public FileExporter() {
        this.prepare();
    }
    
    public final void prepare() {
        try {
            LOGGER.debug("Tiedostoa rakennetaan");
            FileInputStream inputStream = new FileInputStream(constructFile(fileName));
            file = new DefaultStreamedContent(inputStream, "text/plain", fileName);  
        } catch (FileNotFoundException ex) {
            LOGGER.debug(ex.toString());
        } catch (IOException ex) {
            LOGGER.debug(ex.toString());
        }
    }
    
    public StreamedContent getFile() {
        LOGGER.debug("Tiedostoa pyydetty");
        LOGGER.debug("File -> " + file.toString());
        return file;
    }
    
    private File constructFile(String fileName) throws IOException { return new FileBuilder().constructFile(fileName); }
    
}
