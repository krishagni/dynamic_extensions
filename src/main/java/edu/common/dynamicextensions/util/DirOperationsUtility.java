/**
 *
 */

package edu.common.dynamicextensions.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.napi.FormException;

/**
 * @author gaurav_mehta
 *
 */
public final class DirOperationsUtility
{
	private static final Logger LOGGER = Logger.getLogger(DirOperationsUtility.class);

	private static DirOperationsUtility dirOperations;

	public static synchronized DirOperationsUtility getInstance()
	{
		if (dirOperations == null)
		{
			dirOperations = new DirOperationsUtility();
		}
		return dirOperations;
	}

	private DirOperationsUtility()
	{

	}

	/**
	 * This method will delete the directory with the name tempDirName if present & then will
	 * create the new one for use.
	 * @param tempDirName name of the directory to be created.
	 */
	public void createNewTempDirectory(String tempDirName)
	{
		if (tempDirName != null && tempDirName.length() != 0)
		{
			File tempDir = new File(tempDirName);
			if (tempDir.exists())
			{
				deleteDirectory(new File(tempDirName));
			}
			if (!tempDir.mkdirs())
			{
				throw new FormException("Unable to create tempDirectory" + tempDirName);
			}
		}
		else
		{
			LOGGER.info("Given temp directory is empty");
		}
	}

	public void createTempDirectory(String tempDirName) {
		if (tempDirName != null && tempDirName.length() != 0) {
			File tempDir = new File(tempDirName);
			if (tempDir.exists()) {
				return;
			}
			if (!tempDir.mkdirs()) {
				throw new FormException("Unable to create tempDirectory" + tempDirName);
			}
		} else {
			LOGGER.info("Given temp directory is empty");
		}
	}
	/**
	 * This method will first of all delete all the files & folders
	 * present in the given file object & then will delete the given Directory.
	 * @param path directory which is to be deleted.
	 * @return true if deletion is succesfull.
	 */
	public boolean deleteDirectory(File path)
	{
		if (path.exists())
		{
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]);
				}
				else if (!files[i].delete())
				{
					LOGGER.error("Can not delete file " + files[i]);
				}
			}
		}
		return path.delete();
	}

	/**
	 * This will validate that the size of the folder specified in first parameter is less than the
	 * given maxSize.If not will throw the exception.
	 * @param srcFolder folder to be validated.
	 * @param maxSize maximum size expected.
	 * @throws DynamicExtensionsSystemException if the size of folder is greater than maxSize.
	 */
	public static void validateFolderSizeForUpload(String srcFolder, long maxSize)
	{
		File folder = new File(srcFolder);
		if (!folder.exists() || !folder.isDirectory())
		{
			throw new FormException(srcFolder
					+ "does not exist. Please specify correct path");
		}
		LOGGER.info("DirOpUtility :: validateFolderSizeForUpload :: Size :: "+getSize(folder));
		if (maxSize < getSize(folder))
		{
			throw new FormException(srcFolder
					+ "Exceeds the maximum file size. The folder size should be less than 500MB");
		}
	}

	
	public static void validateXMISizeForUpload(String srcFile, long maxSize)
	{
		File xmiFile = new File(srcFile);
		if (!xmiFile.exists() || xmiFile.isDirectory())
		{
			throw new FormException(xmiFile
					+ "does not exist. Please specify correct path");
		}
		if (maxSize < getSize(xmiFile))
		{
			throw new FormException(xmiFile
					+ "Exceeds the maximum file size. The folder size should be less than 500MB");
		}
	}
	/**
	 * This will determine the size of the folder in bytes.
	 * @param folder folderPath.
	 * @return size of the folder in bytes.
	 */
	private static long getSize(File folder)
	{
		long folderSize = 0;
		if (folder.isDirectory())
		{
			File[] filelist = folder.listFiles();
			for (int i = 0; i < filelist.length; i++)
			{
				folderSize = folderSize + getSize(filelist[i]);
			}
		}
		else
		{
			folderSize = folder.length();
		}
		return folderSize;
	}

	/**
	 * This method copies the source file to destination directory.
	 * @param sourceFilePath source path where file exists.
	 * @param destDir destination path where files need to copied.
	 * @param prefix prefix to be applied to source file.
	 * @throws IOException throw IO Exception.
	 */
	public void copyFileToDestDir(String sourceFilePath, String destDir, String prefix)
			throws IOException
	{
		File file = new File(sourceFilePath);
		InputStream ins = new FileInputStream(file);
		String destFileName = file.getName();
		if (prefix != null || !"".equals(prefix))
		{
			destFileName = prefix + file.getName();
		}
		OutputStream out = new FileOutputStream(new File(destDir, destFileName));
		// Transfer bytes from ins to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = ins.read(buf)) > 0)
		{
			out.write(buf, 0, len);
		}
		ins.close();
		out.close();
	}

}
