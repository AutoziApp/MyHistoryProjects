package com.mapuni.mobileenvironment.unzip;

import android.util.Log;

import com.mapuni.mobileenvironment.utils.FileUtils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件解压类
 *
 * @author Sahadev
 *
 */
public class FileUnZip {
	private UnZipProgressUpdateInterface progressUpdate;

	public FileUnZip(UnZipProgressUpdateInterface progressUpdate) {
		super();
		this.progressUpdate = progressUpdate;
	}

	/**
	 * 解压压缩文件
	 *
	 * @param inputStream
	 * @return
	 */
	public boolean unzip(InputStream inputStream) {
		StringBuffer path = new StringBuffer(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());

		if (FileUtils.isSDCardEnable()) {
			ZipArchiveInputStream zin = new ZipArchiveInputStream(inputStream, "GBK", true);
			int rootCount = path.length();

			ArchiveEntry zipEntry = null;
			FileOutputStream outputStream = null;
			byte[] buffer = new byte[8192];

			try {
				while ((zipEntry = zin.getNextEntry()) != null) {
					if (path.length() > rootCount) {
						path.delete(rootCount, path.length());
					}

					if (zipEntry.isDirectory()) {
						String name = zipEntry.getName();
						name = name.substring(0, name.length() - 1);
						path.append(File.separator).append(name);
						File file = new File(path.toString());
						if (!file.exists()) {
							file.mkdirs();
						}
						continue;
					} else {
						path.append(File.separator).append(zipEntry.getName());

						Log.i("Lybin",path.toString());

						File desFile = new File(path.toString());
						if (!desFile.exists()) {
							File fileParentDir = desFile.getParentFile();
							if (!fileParentDir.exists()) {
								fileParentDir.mkdirs();
							}
							desFile.createNewFile();
						} else {
							desFile.delete();
							desFile.createNewFile();
						}
						long fileSize = zipEntry.getSize();
						fileSize = fileSize == 0 ? 1 : fileSize;
						outputStream = new FileOutputStream(desFile);// 普通解压缩文件
						int offset = 0, count = 0;
						while ((offset = zin.read(buffer)) != -1) {
							outputStream.write(buffer, 0, offset);
							count += offset;
							if (progressUpdate != null) {
								progressUpdate.unzipSingleFile(desFile.getName(), (int) (count * 100 / fileSize));
							}
						}
						outputStream.close();
					}
				}
				zin.close();
				return true;
			} catch (Exception ex) {
				return false;
			} finally {
				try {
					zin.close();
					if (outputStream != null) {
						outputStream.flush();
						outputStream.close();
					}
				} catch (IOException e) {
				}
			}
		}

		return false;
	}

}
