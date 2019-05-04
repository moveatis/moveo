package com.moveatis.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZIPFileMaker {
	public static File makeZipFile(List<File> files) {
		FileOutputStream fos;
		String fileName=files.get(0).getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}
		File outputFile=new File(fileName + ".zip");
		try {
			fos = new FileOutputStream(outputFile);

			ZipOutputStream zipOS = new ZipOutputStream(fos);

			for (File file : files)
				writeToZipFile(file, zipOS);

			zipOS.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputFile;
	}

	public static void writeToZipFile(File file, ZipOutputStream zipOS) {

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);

			ZipEntry zipEntry = new ZipEntry(file.getName());
			zipOS.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOS.write(bytes, 0, length);
			}

			zipOS.closeEntry();
			fis.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
