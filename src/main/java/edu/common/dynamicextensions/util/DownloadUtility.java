/**
 *
 */
package edu.common.dynamicextensions.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IoUtil;

/**
 * @author gaurav_mehta
 *
 */
public final class DownloadUtility
{


	private DownloadUtility()
	{

	}

	private static final Logger LOGGER = Logger.getLogger(DownloadUtility.class);


	/**
	 * This method will download the Zip file usin the outputStream in request in the provided tempDirName.
	 * If tempDirName dir does not exists then it will create it first & then download the zip in that folder.
	 * @param request from which to download the Zip file.
	 * @param tempDirName directory name in which to download it.
	 * @throws IOException Exception.
	 */
	public static void downloadZipFile(HttpServletRequest request, String tempDirName,
			String fileName)
	{
		BufferedInputStream reader = null;
		BufferedOutputStream fileWriter = null;
		DirOperationsUtility.getInstance().createNewTempDirectory(tempDirName);
		String completeFileName = tempDirName + File.separator + fileName;
		try
		{
			reader = new BufferedInputStream(request.getInputStream());
			File file = new File(completeFileName);
			if (file.exists() && !file.delete())
			{
				LOGGER.error("Can not delete file : " + file);
			}
			fileWriter = new BufferedOutputStream(new FileOutputStream(file));

			byte[] buffer = new byte[1024];
			int len = reader.read(buffer);
			while (len >= 0)
			{
				fileWriter.write(buffer, 0, len);
				len = reader.read(buffer);
			}
			fileWriter.flush();

		}
		catch (IOException e)
		{
			throw new FormException(
					"Exception occured while downloading the zip on server", e);

		}
		finally
		{
			IoUtil.close(fileWriter);
			IoUtil.close(reader);
		}
		
		ZipUtility.extractZipToDestination(completeFileName, tempDirName);
	}
}
