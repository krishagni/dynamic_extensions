
package edu.common.dynamicextensions.napi;

import java.util.HashMap;
import java.util.Map;

public class FileControlValue {
	private Long formId;

	private Long recordId;

	private String filename;

	private String contentType;
	
	private String fileId;
	
	private String path;

	private String objectType;

	private Long objectId;

	public FileControlValue() {
		
	}
	
	public FileControlValue(String filename, String contentType, String fileId) {
		this.filename = filename;
		this.contentType = contentType;
		this.fileId = fileId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFileName(String fileName) {
		this.filename = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String toString() {
		return this.filename;
	}
	
	public Map<String, String> toValueMap() {
		if (filename == null || fileId == null || contentType == null) {
			return null;
		}
		
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("filename", getFilename());
		valueMap.put("fileId", getFileId());
		valueMap.put("contentType", getContentType());		
		return valueMap;
	}
	
	public static FileControlValue fromValueMap(Map<String, String> valueMap) {
		String filename = valueMap.get("filename");
		String fileId = valueMap.get("fileId");
		String contentType = valueMap.get("contentType");
		
		if (filename == null || fileId == null || contentType == null) {
			return null;
		}
		
		return new FileControlValue(filename, contentType, fileId);  
	}
}
