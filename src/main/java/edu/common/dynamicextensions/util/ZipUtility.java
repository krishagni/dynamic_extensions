
package edu.common.dynamicextensions.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IoUtil;

/**
 * This class contains the utility method to create zip, extract zip etc.
 * @author pavan_kalantri
 *
 */
public final class ZipUtility
{


	private ZipUtility()
	{
	}

	private static final Logger LOGGER = Logger.getLogger(ZipUtility.class);

	/**
	 * This method will extract the given zip file in filename argument to the destination folder.
	 * if destination folder is null or empty string then it will extract the zip to the current base directory
	 * @param filename zip file to be extracted.
	 * @param destination folder where the file should be extracted.
	 * @throws IOException exception.
	 */
	public static void extractZipToDestination(String filename, String destination)
	{
		ZipInputStream zipinputstream = null;
		try
		{
			String destinationPath = "";
			if (destination != null && !"".equals(destination))
			{
				destinationPath = destination + File.separator;
			}
			zipinputstream = new ZipInputStream(new FileInputStream(filename));
			extractZip(zipinputstream, destinationPath);
			zipinputstream.close();
		}
		catch (IOException e)
		{
			throw new FormException(
					"Can not extract the zip, zip may be currupted", e);
		}
		finally
		{
			IoUtil.close(zipinputstream);
		}
	}

	public static void extractZipToDestination(InputStream in, String destination)
	{
		ZipInputStream zipinputstream = null;
		try
		{
			String destinationPath = "";
			if (destination != null && !"".equals(destination))
			{
				destinationPath = destination + File.separator;
			}
			zipinputstream = new ZipInputStream(in);
			extractZip(zipinputstream, destinationPath);
			zipinputstream.close();
		}
		catch (IOException e)
		{
			throw new FormException(
					"Can not extract the zip, zip may be currupted", e);
		}
		finally
		{
			IoUtil.close(zipinputstream);
		}
	}
	
	/**
	 * This method will extract the zip to which zipinputstream is pointing to destinationPath
	 * @param zipinputstream pointing to zip
	 * @param destinationPath destination path.
	 * @throws IOException exception.
	 */
	private static void extractZip(ZipInputStream zipinputstream, String destinationPath)
	throws IOException
	{
		ZipEntry zipentry = zipinputstream.getNextEntry();
		while (zipentry != null)
		{
			if (!zipentry.isDirectory())
			{
				String entryName = zipentry.getName();
				extractZipEntryToFile(destinationPath, zipinputstream, entryName);
				zipinputstream.closeEntry();
			}
			zipentry = zipinputstream.getNextEntry();
		}
	}

	/**
	 * It will only extract the given zipEntry i.e. particular file in the zip file
	 * to the destination path
	 * @param destinationPath directory in which to extract the directory.
	 * @param zipinputstream input stream
	 * @param entryName name of the file which is to be extracted
	 * @throws IOException exception
	 */
	private static void extractZipEntryToFile(String destinationPath,
			ZipInputStream zipinputstream, String entryName)
	throws IOException
	{
		byte[] buf = new byte[1024];
		FileOutputStream fileoutputstream = null;
		try
		{
			File newFile = new File(destinationPath + entryName);
			createParentDirectories(newFile);
			fileoutputstream = new FileOutputStream(newFile);
			int bytesRead = zipinputstream.read(buf, 0, 1024);
			while (bytesRead > -1)
			{
				fileoutputstream.write(buf, 0, bytesRead);
				bytesRead = zipinputstream.read(buf, 0, 1024);
			}
		}
		finally
		{
			if (fileoutputstream != null)
			{
				fileoutputstream.close();
			}
		}
	}

	/**
	 * This method will verify if the parent directories of the newFile exist or not &
	 * if not present will try to create it else will return.
	 * @param newFile file whose parents to create
	 */
	private static void createParentDirectories(File newFile)
	{
		File parentFile = newFile.getParentFile();
		if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs())
		{
			// this is condition when mkdirs is failed to create the directories
			throw new FormException("Can not create directory " + parentFile);
		}
	}

	/**
	 *  It will zip the current folder & create the zip file with
	 *  the given  file name in second parameter.
	 * @param srcFolder folder to be zipped.
	 * @param destZipFile name of the zip file.
	 * @return the file object pointing to the Zip file.
	 * @throws IOException Exception.
	 */
	public static File zipFolder(String srcFolder, String destZipFile)
	{
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		File destZip = null;
		try
		{

			destZip = new File(destZipFile);
			checkIfZipExist(destZipFile, destZip);
			checkIfFolderExist(srcFolder);
			fileWriter = new FileOutputStream(destZip);
			zip = new ZipOutputStream(fileWriter);

			addFolderToZip("", srcFolder, zip);
			zip.flush();
		}
		catch (IOException e)
		{
			throw new FormException("Error occured while reading the folder", e);
		}
		finally
		{
			IoUtil.close(zip);
			IoUtil.close(fileWriter);
		}
		return destZip;
	}

	
	public static File zipFiles(List<String> filesToBeZipped, String destZipFile)
	throws IOException
	{
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		File destZip = null;
		try
		{
		
			destZip = new File(destZipFile);
			checkIfZipExist(destZipFile, destZip);
			fileWriter = new FileOutputStream(destZip);
			zip = new ZipOutputStream(fileWriter);
		
			for(String srcFile : filesToBeZipped)
			{
				byte[] buf = new byte[1024];
	
				FileInputStream inputStream = null;
				try
				{
					inputStream = new FileInputStream(srcFile);
					zip.putNextEntry(new ZipEntry(srcFile));
					int len = inputStream.read(buf);
					while (len > 0)
					{
						zip.write(buf, 0, len);
						len = inputStream.read(buf);
					}
				}
				finally
				{
					if (inputStream != null)
					{
						inputStream.close();
					}
				}
			}
			zip.flush();
		}
		catch (IOException e)
		{
			throw new FormException("Error occured while reading the folder", e);
		}
		finally
		{
			if (zip != null)
			{
				zip.close();
			}
			if (fileWriter != null)
			{
				fileWriter.close();
			}
		}
		return destZip;
	}

	/**
	 * Check whether Zip exist or not and if exist delete it.
	 * @param destZipFile zip to be check.
	 * @param destZip  file under zip.
	 */
	private static void checkIfZipExist(String destZipFile, File destZip)
	{
		if (destZip.exists() && !destZip.delete())
		{
			LOGGER.error("can not delete " + destZipFile);
		}
	}

	/**
	 * This method will check if folder exist or not.
	 * @param srcFolder Path of the folder.
	 * exist.
	 */
	private static void checkIfFolderExist(String srcFolder)
	{
		File folder = new File(srcFolder);
		if (!folder.exists() || !folder.isDirectory())
		{
			throw new FormException(srcFolder
					+ "does not exist. Please specify correct path");
		}
	}

	/**
	 * It will add the current file in the zip at the given path.
	 * @param path path where to add the file in zip.
	 * @param srcFile file to add in zip.
	 * @param zip zip output stream pointing to the zipped file.
	 * @throws IOException exception.
	 */
	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip)
			throws IOException
	{

		File folder = new File(srcFile);
		if (folder.isDirectory())
		{
			addFolderToZip(path, srcFile, zip);
		}
		else
		{
			byte[] buf = new byte[1024];

			FileInputStream inputStream = null;
			try
			{
				inputStream = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				int len = inputStream.read(buf);
				while (len > 0)
				{
					zip.write(buf, 0, len);
					len = inputStream.read(buf);
				}
			}
			finally
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
		}
	}

	/**
	 * This method will add the given srcFolder to the given zip
	 * output stream zip at the given path in the created zip file.
	 * @param path path where the folder should be added in the zip file.
	 * @param srcFolder folder to add in the zip.
	 * @param zip zip out put stream pointing to zip file.
	 * @throws IOException Exception.
	 */
	private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
			throws IOException
	{
		File folder = new File(srcFolder);

		for (String fileName : folder.list())
		{
			if ("".equals(path))
			{
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			}
			else
			{
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}



}
