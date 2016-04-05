package com.moveatis.lotas.devel;

import com.moveatis.lotas.records.RecordEntity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public class ObservationFileOperations {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationFileOperations.class);
    
    private File file;
    
    private final String path;
    
    /*
    * Output streams
    */
    private FileOutputStream fileOutputStream;
    private BufferedOutputStream bufferedOutputStream;
    private ObjectOutputStream objectOutputStream;
    
    /*
    * Input streams
    */
    private FileInputStream fileInputStream;
    private BufferedInputStream bufferedInputStream;
    private ObjectInputStream objectInputStream;
    
    private Random random;

    public ObservationFileOperations() {
        path = Paths.get(".").toAbsolutePath().normalize().toString();
        
        random = new Random();
    }
    
    /* Total hack! */
    public static class Obs implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        public long endTime = 0;
    }
    
    public void writeEndTime(long endTime) {
        try {
            file = new File(path + "/observation.txt");
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            
            Obs observation = new Obs();
            observation.endTime = endTime;
            objectOutputStream.writeObject(observation);
            
            objectOutputStream.flush();
            objectOutputStream.close();
            bufferedOutputStream.close();
            fileOutputStream.close();
        } catch(IOException ioe) {
            LOGGER.debug("Tiedoston kirjoitus meni vikaan -> " + ioe.toString());
        }
        
        LOGGER.debug("Kirjoitettu tiedosto -> " + file.getName());
    }
    
    public void write(RecordEntity record) {
        try {
            file = new File(path + "/" + record.getCategory() + random.nextInt() + ".dat");
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            
            objectOutputStream.writeObject(record);
            
            objectOutputStream.flush();
            objectOutputStream.close();
            bufferedOutputStream.close();
            fileOutputStream.close();
        } catch(IOException ioe) {
            LOGGER.debug("Tiedoston kirjoitus meni vikaan -> " + ioe.toString());
        }
        
        LOGGER.debug("Kirjoitettu tiedosto -> " + file.getName());
    }
    
    public long readEndTime() {
        try {
            file = new File(path + "/observation.txt");
            LOGGER.debug("Luetaan tiedostoa -> " + file.getName());
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(bufferedInputStream);
            
            Obs observation = (Obs) objectInputStream.readObject();

            objectInputStream.close();
            bufferedInputStream.close();
            fileInputStream.close();
            
            if (file.delete()) {
                LOGGER.debug("Tiedosto poistettiin -> " + file.getName());
            } else {
                LOGGER.debug("Tiedoston poisto epäonnistui -> " + file.getName());
            }
            
            return observation.endTime;
        } catch(IOException | ClassNotFoundException ioe) {
            LOGGER.debug("Tiedostosta luku epäonnistui -> " + ioe.toString());
        }
        
        return 0;
    }
    
    public List<RecordEntity> read() {
        ArrayList<RecordEntity> records = new ArrayList<>();
        try {
            file = new File(path);
            FilenameFilter filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String string) {
                    return string.endsWith(".dat");
                }
            };
            
            File[] files = file.listFiles(filenameFilter);
            
            for(File f:files) {
                LOGGER.debug("Luetaan tiedostoa -> " + f.getName());
                fileInputStream = new FileInputStream(f);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                objectInputStream = new ObjectInputStream(bufferedInputStream);

                records.add((RecordEntity) objectInputStream.readObject());

                objectInputStream.close();
                bufferedInputStream.close();
                fileInputStream.close();
            }
            
            for(File f : files) {
                if (f.delete()) {
                    LOGGER.debug("Tiedosto poistettiin -> " + f.getName());
                } else {
                    LOGGER.debug("Tiedoston poisto epäonnistui -> " + f.getName());
                }
            }
            
            return records;
        } catch(IOException | ClassNotFoundException ioe) {
            LOGGER.debug("Tiedostosta luku epäonnistui -> " + ioe.toString());
        }
        
        return null;
    }
 
}
