/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
