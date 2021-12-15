package edu.common.dynamicextensions.nutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import edu.common.dynamicextensions.napi.FormException;

public class IoUtil {
	private static final Logger logger = Logger.getLogger(IoUtil.class);
	
	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of writer", e);
			}
		}
	}

	public static void close(CSVReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of writer", e);
			}
		}
	}
	
	public static void close(CSVWriter writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of writer", e);
			}
		}
	}

	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of output stream", e);
			}
		}
	}
	
	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of input stream", e);
			}
		}
	}
	
	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of input stream", e);
			}
		}
	}
	
	public static void delete(String file) {
		if (file != null) {
			delete(new File(file));
		}
	}
	
	public static void delete(File file) {
		if (file != null) {
			file.delete();
		}
	}
	
	public static void copy(InputStream in, OutputStream out) 
	throws IOException {
		byte[] bytes = new byte[1024];
		int numRead = 0;
		
		while ((numRead = in.read(bytes)) >= 0) {
			out.write(bytes, 0, numRead);
		}
		
		out.flush();		
	}
	
	public static InputStream getDeleteOnCloseFileInputStream(String file) 
	throws IOException {
		return new DeleteOnCloseFileInputStream(file);
	}

	public static void zipFiles(String inputDir, String outFilePath) {
		zipFiles(inputDir, outFilePath, null);
	}
	
	public static void zipFiles(String inputDir, String outFilePath, List<String> excludeFiles) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(outFilePath);
			zipFiles(inputDir, fout, excludeFiles);
		} catch (Exception e) {
			throw new FormException("Error creating zip file", e);
		} finally {
			IoUtil.close(fout);
		}
	}

	public static void zipFiles(String inputDir, OutputStream out) {
		zipFiles(inputDir, out, null);
	}
	
	public static void zipFiles(String inputDir, OutputStream out, List<String> excludeFiles) {
		ZipOutputStream zout = null;
	
		try {		
			zout = new ZipOutputStream(out);

			File inputDirFile = new File(inputDir);
			URI baseDir = inputDirFile.toURI();
			
			List<File> dirs = new ArrayList<>();
			dirs.add(inputDirFile);
			
			while (!dirs.isEmpty()) {
				File dir = dirs.remove(0);			
			
				for (File file : dir.listFiles()) {
					if (excludeFiles != null && excludeFiles.contains(file.getAbsolutePath())) {
						continue;
					}

					if (file.isDirectory()) {
						dirs.add(file);
						continue;
					}


					zipFile(file, baseDir.relativize(file.toURI()).getPath(), zout);
				}
			}
			
			zout.flush();
		} catch (Exception e) {
			throw new FormException("Error zipping input directory:" + inputDir, e);
		} finally {
			IoUtil.close(zout);
			
		}
	}
	
	private static void zipFile(File file, String entry, ZipOutputStream zout) 
	throws Exception {		
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			
			zout.putNextEntry(new ZipEntry(entry));
			copy(fin, zout);
			zout.closeEntry();
		} finally {
			close(fin);
		}
	}	
}
