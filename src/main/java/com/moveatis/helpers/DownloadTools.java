package com.moveatis.helpers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

public class DownloadTools {
	public static File getImageFromByteArr(String filename, byte img_bytes[]) {
		ByteArrayInputStream bis = new ByteArrayInputStream(img_bytes);
		BufferedImage image;
		File outputfile = null;
		try {
			image = ImageIO.read(bis);
			bis.close();
			outputfile = File.createTempFile(filename, ".png");
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputfile;
	}

	
	public static void downloadFile(File file, String responseType) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		ec.responseReset();

		ec.setResponseContentType(responseType);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		try {
			OutputStream outputStream = ec.getResponseOutputStream();
			FileInputStream input = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			while ((input.read(buffer)) != -1) {
				outputStream.write(buffer);
			}
			outputStream.flush();
			input.close();
			fc.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadCSV(String data,String fileName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		ec.responseReset();

		ec.setResponseContentType("text/csv");
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName+".csv" + "\"");
		try {
			OutputStream outputStream = ec.getResponseOutputStream();
			for (char c: data.toCharArray()) {
				outputStream.write(c);
			}
			outputStream.flush();
			fc.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
