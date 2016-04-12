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
import java.util.Date;
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
    
    //
    // Helper methods
    //
    
    private <T> void writeObject(T object, String fileName) {
        try {
            file = new File(path + "/" + fileName);
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            
            objectOutputStream.writeObject(object);
            
            objectOutputStream.flush();
            objectOutputStream.close();
            bufferedOutputStream.close();
            fileOutputStream.close();
        } catch(IOException ioe) {
            LOGGER.debug("Tiedoston kirjoitus meni vikaan -> " + ioe.toString());
            return;
        }
        
        LOGGER.debug("Kirjoitettu tiedosto -> " + file.getName());
    }
    
    private <T> T readObject(String fileName) {
        try {
            file = new File(path + "/" + fileName);
            LOGGER.debug("Luetaan tiedostoa -> " + file.getName());
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(bufferedInputStream);
            
            T object = (T) objectInputStream.readObject();

            objectInputStream.close();
            bufferedInputStream.close();
            fileInputStream.close();
            
            return object;
            
        } catch(IOException | ClassNotFoundException ioe) {
            LOGGER.debug("Tiedostosta luku epäonnistui -> " + ioe.toString());
        }
        
        return null;
    }
    
    private File[] getDatFiles() {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                return string.endsWith(".dat");
            }
        };
        file = new File(path);
        return file.listFiles(filenameFilter);
    }
    
    private void deleteFile(String fileName) {
        file = new File(path + "/" + fileName);
        if (file.delete()) LOGGER.debug("Tiedosto poistettiin -> " + file.getName());
        else LOGGER.debug("Tiedoston poisto epäonnistui -> " + file.getName());
    }
    
    private void deleteDatFiles() {
        File[] files = getDatFiles();
        for(File f : files) {
            if (f.delete()) LOGGER.debug("Tiedosto poistettiin -> " + f.getName());
            else LOGGER.debug("Tiedoston poisto epäonnistui -> " + f.getName());
        }
    }
    
    //
    //
    //
    
    public void writeDate(Date date) {
        deleteFile("date.txt");
        deleteFile("duration.txt");
        deleteDatFiles();
        writeObject(date, "date.txt");
    }
    
    public Date readDate() {
        return readObject("date.txt");
    }
    
    public static class Duration implements Serializable {
        private static final long serialVersionUID = 1L;
        public long duration;
        public Duration(long duration) {
            this.duration = duration;
        }
    }
    
    public void writeDuration(long duration) {
        writeObject(new Duration(duration), "duration.txt");
    }
    
    public long readDuration() {
        Duration d = readObject("duration.txt");
        if (d == null) return 0;
        return d.duration;
    }
    
    public void write(RecordEntity record) {
        writeObject(record, record.getCategory().getLabel().getLabel() + random.nextInt() + ".dat");
    }
    
    public List<RecordEntity> read() {
        ArrayList<RecordEntity> records = new ArrayList<>();
        try {
            File[] files = getDatFiles();
            
            for(File f : files) {
                LOGGER.debug("Luetaan tiedostoa -> " + f.getName());
                fileInputStream = new FileInputStream(f);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                objectInputStream = new ObjectInputStream(bufferedInputStream);

                records.add((RecordEntity) objectInputStream.readObject());

                objectInputStream.close();
                bufferedInputStream.close();
                fileInputStream.close();
            }
            
            return records;
        } catch(IOException | ClassNotFoundException ioe) {
            LOGGER.debug("Tiedostosta luku epäonnistui -> " + ioe.toString());
        }
        
        return null;
    }
 
}
