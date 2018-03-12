package com.mapuni.mobileenvironment.unzip;

/**
 * 文件解压进度接口
 * 
 * @author Sahadev
 *
 */
public interface UnZipProgressUpdateInterface {
	/**
	 * @param fileName
	 *            被解压的文件名
	 * @param progress
	 *            该文件的进度
	 */
	public void unzipSingleFile(String fileName, int progress);

	/**
	 * @param progress
	 *            解压一堆文件的总进度
	 */
	public void unzipGroupFile(int progress);
}
