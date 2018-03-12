package com.mapuni.mobileenvironment.utils;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FileUtils {

	private final String TAG = "FileUtils";
	private final String SDCardPath = Environment.getExternalStorageDirectory() + File.separator;

	/**
	 * 将加密的64位字符串转化为本地文件
	 * 
	 * @param base64String
	 *            base64加密字符串
	 * @param dir
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 */
	synchronized public void convertBase64StringToLocalFile(String base64String, String dir, String fileName) {
		try {
			dir = new String(dir.getBytes(), "UTF-8");
			fileName = new String(fileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}
		byte[] filebytes = Base64.decode(base64String, Base64.DEFAULT);
		convertByteArrayToLocalFile(filebytes, dir, fileName);
	}

	/**
	 * 将加密的64位List转化为本地文件
	 * 
	 * @param base64List
	 *            base64加密字符串
	 * @param dir
	 *            保存地址
	 * @param ListfileName
	 *            文件名称
	 */
	synchronized public void convertBase64ListToLocalFile(List<String> base64List, String dir, // 64字符转化为本地压缩文件
			List<String> ListfileName) {
		int i = 0;
		for (String base64String : base64List) {
			String fileName = ListfileName.get(i);
			try {
				dir = new String(dir.getBytes(), "UTF-8");
				fileName = new String(fileName.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LogUtil.e(TAG, e1.getMessage());
			}

			byte[] filebytes = Base64.decode(base64String, Base64.DEFAULT);
			convertByteArrayToLocalFile(filebytes, dir, fileName);
			i++;
		}
	}

	/**
	 * FileName: FileUtils.java Description: 文件类型的枚举类
	 * 
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-7 下午01:18:21
	 */
	public static enum FileType {

		HTML("text/html"), IMAGE("image/*"), PDF("application/pdf"), TXT("text/plain"), AUDIO("audio/*"), VIDEO("video/*"), CHM("application/x-chm"), WORD("application/msword"), EXCEL(
				"application/vnd.ms-excel"), PPT("application/vnd.ms-powerpoint");

		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		private FileType(String type) {
			this.type = type;
		}

	}

	/**
	 * Description: 通过不同的文件类型返回不同的枚举对象
	 * 
	 * @param type
	 *            系统的文件类型
	 * @return 文件类型枚举对象 FileType
	 * @author 王红娟 Create at: 2012-12-7 下午01:18:46
	 */
	public static FileType getFileType(String type) {
		if (type.equals("text/html")) {
			return FileType.HTML;
		}
		if (type.equals("image/*")) {
			return FileType.IMAGE;
		}
		if (type.equals("application/pdf")) {
			return FileType.PDF;
		}
		if (type.equals("text/plain")) {
			return FileType.TXT;
		}
		if (type.equals("audio/*")) {
			return FileType.AUDIO;
		}
		if (type.equals("video/*")) {
			return FileType.VIDEO;
		}
		if (type.equals("application/x-chm")) {
			return FileType.CHM;
		}
		if (type.equals("application/msword")) {
			return FileType.WORD;
		}
		if (type.equals("application/vnd.ms-excel")) {
			return FileType.EXCEL;
		}
		if (type.equals("application/vnd.ms-powerpoint")) {
			return FileType.PPT;
		}
		return null;
	}

	public static void OpenFile(String type, String param, boolean paramBoolean, Context context, File file) {
		FileType filetype = getFileType(type);
		Intent intent = new Intent("android.intent.action.VIEW");
		Uri uri = null;
		switch (filetype) {
		case HTML:
			uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
			break;
		case IMAGE:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			uri = Uri.fromFile(file);
			break;
		case PDF:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			uri = Uri.fromFile(file);
			break;
		case TXT:
			if (paramBoolean) {
				Uri uri1 = Uri.parse(param);
				intent.setDataAndType(uri1, type);
			} else {
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
			}
			break;
		case AUDIO:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("oneshot", 0);
			intent.putExtra("configchange", 0);
			uri = Uri.fromFile(file);
			break;
		case VIDEO:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("oneshot", 0);
			intent.putExtra("configchange", 0);
			uri = Uri.fromFile(file);
			break;
		case CHM:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			uri = Uri.fromFile(file);
			break;
		case WORD:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			uri = Uri.fromFile(file);
			break;
		case EXCEL:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			uri = Uri.fromFile(file);
			break;
		case PPT:
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			uri = Uri.fromFile(file);
			break;
		}
		intent.setDataAndType(uri, type);

		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "附件无法打开，请下载相关软件", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 将加密的64位Array转化为本地文件
	 * 
	 * @param base64Array
	 *            base64加密字符串
	 * @param dir
	 *            保存地址
	 * @param ListfileName
	 *            文件名称
	 */
	synchronized public void convertBase64ArrayToLocalFile(String[] base64Array, String dir, // 64字符转化为本地压缩文件
			List<String> ListfileName) {
		int j = base64Array.length;
		for (int i = 0; i < j; i++) {
			String fileName = ListfileName.get(i);
			try {
				dir = new String(dir.getBytes(), "UTF-8");
				fileName = new String(fileName.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LogUtil.e(TAG, e1.getMessage());
			}

			byte[] filebytes = Base64.decode(base64Array[i].toString(), Base64.DEFAULT);
			convertByteArrayToLocalFile(filebytes, dir, fileName);
		}
	}

	/**
	 * 将字节转化为本地文件
	 * 
	 * @param content
	 *            读取的内容
	 * @param dir
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 */
	public void convertByteArrayToLocalFile(byte[] content, String dir, String fileName) {

		try {
			dir = new String(dir.getBytes(), "UTF-8");
			fileName = new String(fileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		File destDir = new File(dir);
		try {
			if (!destDir.exists())
				destDir.mkdir();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, fileName), false));
			bos.write(content);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (IOException e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	/**
	 * 将字符串写入文件
	 * 
	 * @param path
	 * @param content
	 */
	public void convertStringToLocalFile(String path, String content) {
		try {
			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
			LogUtil.e(TAG, "写入文件" + path + "成功!!!");
		} catch (IOException e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	/**
	 * 将一组文件打包成 zip 压缩包，压缩包中没有文件夹目录
	 * 
	 * @param zipedAbsFileNames
	 *            1，可以按一组文件的存储绝对路径名称和对应压缩文件夹目录层次设置 key: /sdcard/test.txt ;
	 *            value: test.txt 2，可以直接压缩一个文件夹下所有的文件，压缩文件里面的路径为文件夹路径的一部分 key:
	 *            /sdcard/floder/ ; value: floder/
	 * @param zipOutAbsFileName
	 *            打包后的 zip 文件名
	 */
	public void zipedFiles(HashMap<String, Object> zipedAbsFileNames, String zipOutAbsFileName) {

		try {
			zipOutAbsFileName = new String(zipOutAbsFileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		byte[] buf = new byte[1024];
		try {
			// 创建压缩后文件的文件夹
			zipOutAbsFileName = zipOutAbsFileName.replace("\\", "/");
			File zipOutAbsFileNameFolder = new File(zipOutAbsFileName.substring(0, zipOutAbsFileName.lastIndexOf("/")));
			if (!zipOutAbsFileNameFolder.exists()) {
				zipOutAbsFileNameFolder.mkdirs();
			}
			// 进行压缩操作
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipOutAbsFileName));
			Iterator<Map.Entry<String, Object>> iterator = zipedAbsFileNames.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();

				String inputStr = new String(entry.getKey().getBytes(), "UTF-8");
				String outputStr = new String(entry.getValue().toString().getBytes(), "UTF-8");

				File inputFile = new File(inputStr);
				ArrayList<File> files = new ArrayList<File>();
				getAbsFiles(inputFile, files);

				if (files.size() > 1) {
					// 是目录
					for (File file : files) {
						inputStr = file.getAbsolutePath();
						if (inputStr.indexOf(outputStr) != -1) {

							outputStr = inputStr.substring(inputStr.indexOf(outputStr) + 1);

							FileInputStream in = new FileInputStream(inputStr);
							out.putNextEntry(new ZipEntry(outputStr));
							int len;
							while ((len = in.read(buf)) > 0) {
								out.write(buf, 0, len);
							}
							out.closeEntry();
							in.close();

							outputStr = new String(entry.getValue().toString().getBytes(), "UTF-8");
						}
					}
				} else {
					// 是一个文件的绝对路径
					outputStr = inputStr.substring(inputStr.indexOf(outputStr) + 1);

					FileInputStream in = new FileInputStream(inputFile);
					out.putNextEntry(new ZipEntry(outputStr));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
				}
			}
			out.close();
			LogUtil.v(TAG, zipOutAbsFileName + " 压缩成功");
		} catch (IOException e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	/**
	 * 解压 zip 文件，并返回解压文件的绝对路径
	 * 
	 * @param zipedAbsFileName
	 *            压缩文件的绝对路径
	 * @param destDir
	 *            解压缩的文件夹
	 * @return
	 */
	public synchronized ArrayList<File> deZipFiles(String zipedAbsFileName, String destDir) {// 解压后并删除原压缩文件
		try {
			zipedAbsFileName = new String(zipedAbsFileName.getBytes(), "UTF-8");
			destDir = new String(destDir.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			return new ArrayList<File>();
		}
		destDir = destDir.endsWith("//") ? destDir : destDir + "//";
		ArrayList<File> zipFiles = new ArrayList<File>();
		byte b[] = new byte[1024];
		int length;
		ZipFile zipFile = null;
		File file = new File(zipedAbsFileName);
		try {
			zipFile = new ZipFile(file);
		} catch (ZipException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (zipFile != null) {

			Enumeration<?> enumeration = zipFile.entries();
			ZipEntry zipEntry = null;

			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				File loadFile = new File(destDir + zipEntry.getName());
				File load = new File(destDir + UUID.randomUUID() + ".txt");
				loadFile.renameTo(load);
				loadFile.delete();
				zipFiles.add(load);

				if (zipEntry.isDirectory()) {
					load.mkdirs();
				} else {
					if (!load.getParentFile().exists())
						load.getParentFile().mkdirs();

					OutputStream outputStream = null;
					InputStream inputStream = null;
					try {
						outputStream = new FileOutputStream(load);
						inputStream = zipFile.getInputStream(zipEntry);

						while ((length = inputStream.read(b)) != -1) {
							outputStream.write(b, 0, length);
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (file.delete()) {
			}
		}
		return zipFiles;
	}

	/**
	 * 解压 zip 文件，并返回解压文件的绝对路径
	 * 
	 * @param zipedAbsFileName
	 *            压缩文件的绝对路径
	 * @param destDir
	 *            解压缩的文件夹
	 * @return
	 * @throws IOException
	 */
	synchronized public ArrayList<File> deZipFiles(String zipedAbsFileName, String destDir, int i) {// 解压后并删除原压缩文件

		try {
			zipedAbsFileName = new String(zipedAbsFileName.getBytes(), "UTF-8");
			destDir = new String(destDir.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		destDir = destDir.endsWith("//") ? destDir : destDir + "//";
		ArrayList<File> deZipFiles = new ArrayList<File>();
		byte b[] = new byte[1024];
		int length;
		ZipFile zipFile = null;
		try {
			File file = new File(zipedAbsFileName);
			zipFile = new ZipFile(file);
			Enumeration<?> enumeration = zipFile.entries();
			ZipEntry zipEntry = null;

			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				File loadFile = new File(destDir + zipEntry.getName());
				File load = new File(destDir + "service" + i + ".txt");
				loadFile.renameTo(load);
				loadFile.delete();
				deZipFiles.add(load);

				if (zipEntry.isDirectory()) {
					load.mkdirs();
				} else {
					if (!load.getParentFile().exists())
						load.getParentFile().mkdirs();

					OutputStream outputStream = new FileOutputStream(load);
					InputStream inputStream = zipFile.getInputStream(zipEntry);

					while ((length = inputStream.read(b)) > 0)
						outputStream.write(b, 0, length);

					inputStream.close();
					outputStream.close();
				}
			}
			file.delete();
			LogUtil.v(TAG, zipedAbsFileName + " : 文件解压成功\n" + "解压目录 ：" + destDir);
			return deZipFiles;
		} catch (IOException e) {
			LogUtil.e(TAG, e.getMessage());
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<File>();
	}

	/**
	 * 递归遍历文件夹
	 * 
	 * @param filepath
	 * @return
	 */
	public void getAbsFiles(File filepath, List<File> fileslist) {

		File[] files = filepath.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				getAbsFiles(file, fileslist);
			} else {
				fileslist.add(file);
			}
		}
	}

	/**
	 * 返回目录下的文件夹集合
	 * 
	 * @param filePath
	 * @return
	 */
	public List<File> getFileDir(String filePath) {// 返回目录下的文件夹
		ArrayList<File> fileList = new ArrayList<File>();
		File[] files = null;
		File file = new File(filePath);
		if (file.exists())
			files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory())// 是任务文件夹则添加
				fileList.add(f);
		}
		return fileList;
	}

	/**
	 * 判断文件名后缀，是否是多媒体文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isMedia(String fileName) {// 判断文件名后缀是否为多媒体文件(mp3,amr,mp4)
		String behand = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (behand.equals("jpg") || behand.equals("mp4") || behand.equals("amr") || "png".equals(behand))
			return true;
		else
			return false;
	}

	/**
	 * 获取所有的多媒体文件
	 * 
	 * @param filelist
	 * @return
	 */
	public static ArrayList<ArrayList<File>> getAllMedias(ArrayList<File> filelist) {
		ArrayList<ArrayList<File>> list = new ArrayList<ArrayList<File>>();
		for (File f : filelist) {// 遍历每个任务文件夹
			ArrayList<File> file = new ArrayList<File>();
			File[] filearray = f.listFiles();
			for (File fi : filearray) {// 遍历每个任务文件夹中的多媒体文件
				if (isMedia(fi.getName())) {
					file.add(fi);
				}
			}
			list.add(file);
		}
		return list;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileType(File file) {
		String type = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
		return type;
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static boolean isFileExist(String path) {
		return new File(path).exists();
	}

	/**
	 * 在SD卡上创建目录 （如果不存在则创建一个）
	 * 
	 * @param dirName
	 *            要创建的目录名
	 * @return 创建得到的目录
	 */
	public File createSDDir(String dirName) {
		File dir = new File(SDCardPath + dirName);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	/**
	 * 在SD卡上创建文件 （已经存在则删掉）
	 * 
	 * @param fileName
	 *            要创建的文件名
	 * @return 创建得到的文件
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDCardPath + fileName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();

		} else {
			file.delete();
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 执行文件复制操作
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @param context
	 * @param fileName
	 * @param content
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";
		try {
			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String readInStream(FileInputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			LogUtil.i("FileTest", e.getMessage());
		}
		return null;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 * 向手机写图片
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder, String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory() + File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size
	 *            字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 获取目录文件个数
	 * 
	 * @param dir
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}

	/**
	 * 将输入流转换为字节数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;

	}

	/**
	 * 计算SD卡的剩余空间
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 新建目录
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装SD卡
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/" + listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					LogUtil.e("DirectoryManager deleteDirectory", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					LogUtil.e("DirectoryManager deleteFile", fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	@SuppressLint("DefaultLocale")
	public static void openFile(Context context, String path) {
		File file = null;
		if (path.toLowerCase().endsWith("jpg") || path.toLowerCase().endsWith("png")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.IMAGE.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith("mp3") || path.toLowerCase().endsWith("amr")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.AUDIO.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith("mp4")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.VIDEO.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith(".doc") || path.toLowerCase().endsWith(".docx")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.WORD.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith(".xlsx") || path.toLowerCase().endsWith(".xls")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.EXCEL.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith(".pdf")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.PDF.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith(".ppt") || path.toLowerCase().endsWith(".dps")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.PPT.getType(), "", false, context, file);
		} else if (path.toLowerCase().endsWith(".txt")) {
			file = new File(path);
			FileUtils.OpenFile(FileType.TXT.getType(), "", false, context, file);
		} else {
			Toast.makeText(context, "无法支持此类型！", Toast.LENGTH_LONG).show();
		}
	}
}