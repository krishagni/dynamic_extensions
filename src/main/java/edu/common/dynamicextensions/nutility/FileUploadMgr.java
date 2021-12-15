package edu.common.dynamicextensions.nutility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.napi.FormException;

public class FileUploadMgr {
	private static FileUploadMgr instance = new FileUploadMgr();
	
	public static FileUploadMgr getInstance() {
		return instance;
	}
	
	public String saveFile(InputStream in) {
		return saveFile(in, null);
	}

	public String saveFile(InputStream in, String extn) {
		if (StringUtils.isNotBlank(extn)) {
			extn = "." + extn;
		} else {
			extn = "";
		}

		OutputStream out = null;
		try {			
			String fileId = UUID.randomUUID().toString() + extn;
			String path = getFilePath(fileId);
			out = new FileOutputStream(path);
			IoUtil.copy(in, out);
			return fileId;
		} catch (Exception e) {
			throw new FormException("Error saving file", e);
		} finally {
			IoUtil.close(out);
		}
	}
	
	public File getFile(String fileId) {
		String path = getFilePath(fileId);
		return new File(path);
	}
	
	public String getFilePath(String fileId) {
		return DeConfiguration.getInstance().fileUploadDir() + File.separator + fileId;
	}
	
	public void deleteFile(String fileId) {
		File file = getFile(fileId);
		IoUtil.delete(file);
	}	
}
