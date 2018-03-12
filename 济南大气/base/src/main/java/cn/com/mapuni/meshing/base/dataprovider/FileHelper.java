package cn.com.mapuni.meshing.base.dataprovider;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.http.client.ClientProtocolException;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.DataSyncActivity;
import cn.com.mapuni.meshing.base.Global;
import cn.com.mapuni.meshing.base.attachment.T_Attachment;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.netprovider.Net;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;


/**
 * FileName: FileHelper.java Description:文件操作帮助类
 * 
 * @author 王振洋
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 下午3:44:13
 */
public class FileHelper {

	private final String TAG = "FileHelper";
	// public static final String BASE_PATH = "/sdcard/mapuni/";
	// public static final String SDCARD_RASK_DATA_PATH = BASE_PATH
	// +"MobileEnforcement/";
	// public static final String SDCARD_date_LOCAL_PATH = SDCARD_RASK_DATA_PATH
	// +"data";//手机端data文件目录
	private final String SDCardPath = Environment.getExternalStorageDirectory()
			+ "/";

	/**
	 * 将加密的64位字符串转化为本地文件
	 * 
	 * @param base64String
	 *            base64加密字符串
	 * @param savePath
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 */
	synchronized public void convertBase64StringToLocalFile(
			String base64String, String dir, // 64字符转化为本地压缩文件
			String fileName) {

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
	 * @param base64String
	 *            base64加密字符串
	 * @param savePath
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 */
	synchronized public void convertBase64ListToLocalFile(
			List<String> base64List, String dir, // 64字符转化为本地压缩文件
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
	 * 将加密的64位Array转化为本地文件
	 * 
	 * @param base64String
	 *            base64加密字符串
	 * @param savePath
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 */
	synchronized public void convertBase64ArrayToLocalFile(
			String[] base64Array, String dir, // 64字符转化为本地压缩文件
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

			byte[] filebytes = Base64.decode(base64Array[i].toString(),
					Base64.DEFAULT);
			convertByteArrayToLocalFile(filebytes, dir, fileName);
		}
	}

	/**
	 * 将字节转化为本地文件
	 * 
	 * @param content
	 *            读取的内容
	 * @param savePath
	 *            保存地址
	 * @param fileName
	 *            文件名称
	 */
	public void convertByteArrayToLocalFile(byte[] content, String dir,
			String fileName) {

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
			FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
			fos.write(content);
			fos.flush();
			fos.close();
			LogUtil.v(TAG, dir + fileName + " 成功转化为本地文件");
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
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
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
	}

	/**
	 * 读取文件并转换为byte字节数组
	 * 
	 * @param filepath
	 *            文件的绝对路径
	 * @return
	 */
	public boolean readFileToBase64String(String absFileName, String namespace,
			String url, String methodName, String RWBH) {

		try {
			absFileName = new String(absFileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;
		boolean resultResponse = false;
		try {
			File absFile = new File(absFileName);
			fis = new FileInputStream(absFileName);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[(int) absFile.length() + 1];
			int count = 0;

			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);

			}
			// String rwname=absFileName.substring(absFileName.lastIndexOf("/")
			// + 1);
			// count+=1000*1024;
			// strbuf.append(str);
			String filename = absFileName.substring(absFileName.indexOf(RWBH));
			String attachmentData = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT);
			// this.convertStringToLocalFile1("pash", attachmentData);
			/* 调用webserice的参数设置 */
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fileNameWithPath", filename);
			params.add(param);

			param = new HashMap<String, Object>();
			param.put("strValue", attachmentData);
			params.add(param);
			resultResponse = (Boolean) WebServiceProvider.callWebService(
					namespace, methodName, params, url,
					WebServiceProvider.RETURN_BOOLEAN, true);

			LogUtil.v(TAG, absFileName + "\t成功转化为Base64 Bytes");
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} finally {
			try {
				baos.flush();
				baos.close();
				fis.close();
			} catch (IOException e) {
				LogUtil.e(TAG,"//"+ e.getMessage());
				return false;
			}
		}
		return resultResponse;
	}

	/**
	 * 读取文件并转换为byte字节数组进行上传附件
	 * 
	 * @param filepath
	 *            文件的绝对路径
	 * @return
	 */
	public boolean uploadFileToBase64String(String absFileName,
			String namespace, String url, String methodName, String bmidTime) {

		try {
			absFileName = new String(absFileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;
		boolean resultResponse = false;
		try {

			fis = new FileInputStream(absFileName);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;

			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);

			}
			// String rwname=absFileName.substring(absFileName.lastIndexOf("/")
			// + 1);
			// count+=1000*1024;
			// strbuf.append(str);

			String attachmentData = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT);
			// this.convertStringToLocalFile1("pash", attachmentData);
			/* 调用webserice的参数设置 */
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("fileNameWithPath", bmidTime);
			params.add(param);

			param = new HashMap<String, Object>();
			param.put("strValue", attachmentData);
			params.add(param);
			resultResponse = (Boolean) WebServiceProvider.callWebService(
					namespace, methodName, params, url,
					WebServiceProvider.RETURN_BOOLEAN, true);

			LogUtil.v(TAG, absFileName + "\t成功转化为Base64 Bytes");
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} catch (NullPointerException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} finally {
			try {
				baos.flush();
				baos.close();
				fis.close();
			} catch (IOException e) {
				LogUtil.e(TAG,"//"+ e.getMessage());
				return false;
			}
		}
		return resultResponse;
	}

	/**
	 * 读取文件并分段转换为byte字节数组
	 * 
	 * @param filepath
	 *            文件的绝对路径
	 * @return
	 */
	public boolean sctionreadFileToBase64String(String absFileName,
			String namespace, String url, String methodName, String RWBH) {

		try {
			absFileName = new String(absFileName.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;
		boolean result = false;

		try {
			// StringBuffer strbuf=new StringBuffer();
			File absFile = new File(absFileName);
			fis = new FileInputStream(absFileName);
			baos = new ByteArrayOutputStream();

			int count = 0;
			// int i=0;
			boolean end = false;
			for (int i = 0; i < (int) absFile.length() / (1024 * 500) + 1; i++) {
				String attachmentData = "";
				if (i == (int) absFile.length() / (1024 * 500)) {
					end = true;
					byte[] buffers = new byte[(int) absFile.length()
							% (1024 * 500)];

					count = fis.read(buffers);

					// baos.write(buffers, 0, count);
					attachmentData = Base64.encodeToString(buffers,
							Base64.DEFAULT);
					// baos.flush();
					// baos.close();
					// fis.close();
					// if(!attachmentData.endsWith("==")){
					// attachmentData=attachmentData.substring(0,attachmentData.length()-3);
					// attachmentData=attachmentData.concat("==");
					// }
				} else {
					byte[] buffer = new byte[1024 * 500];

					count = fis.read(buffer);

					// baos.write(buffer,0,count);
					attachmentData = Base64.encodeToString(buffer,
							Base64.DEFAULT);

				}
				// String
				// rwname=absFileName.substring(absFileName.lastIndexOf("/") +
				// 1)+"."+i;
				// count+=1000*1024;
				// strbuf.append(str);
				String filename = absFileName.substring(absFileName
						.indexOf(RWBH));
				// this.convertStringToLocalFile1("pash"+i,attachmentData);
				/* 调用webserice的参数设置 */
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("fileNameWithPath", filename + "." + i);
				params.add(param);

				param = new HashMap<String, Object>();
				param.put("strValue", attachmentData);
				params.add(param);

				param = new HashMap<String, Object>();
				param.put("isEnd", end);
				params.add(param);
				// LogUtil.i("s", ""+WSUtils.callWebService(namespace,
				// "UploadPartFile", params, url, WSUtils.RETURN_BOOLEAN,
				// true));
				Object resultResponseObj = WebServiceProvider.callWebService(
						namespace, "UploadPartFile", params, url,
						WebServiceProvider.RETURN_BOOLEAN, true);
				boolean resultResponse = false;
				if (null != resultResponseObj) {
					resultResponse = (Boolean) resultResponseObj;
				}

				if (resultResponse) {
					result = resultResponse;
				} else {
					result = false;
					break;
				}
			}
			// this.convertBase64StringToLocalFile(strbuf.t,
			// "/sdcard/mapuni/MobileEnforcement/rw/", "rwmc");
			// while ((count = fis.read(buffer,1,1000)) >= 0) {

			// }
			LogUtil.v(TAG, absFileName + "\t成功转化为Base64 Bytes");
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
			return false;
		} finally {
			try {
				baos.flush();
				baos.close();
				fis.close();

			} catch (IOException e) {
				LogUtil.e(TAG,"//"+ e.getMessage());
				return false;
			}
		}
		// return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		return result;
	}

	/**
	 * 将字符串写入任务附件文件
	 * 
	 * @param path
	 * @param content
	 */
	public void convertStringToLocalFile1(String path, String content) {
		try {
			File f = new File("/sdcard/mapuni/MobileEnforcement/rwfj");// 如果没有目录先建立目录
			if (!f.exists())
				f.mkdirs();
			File fil = new File("/sdcard/mapuni/MobileEnforcement/rwfj/" + path);// 有目录之后建文件
			FileWriter fw = new FileWriter(fil);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
			LogUtil.e(TAG, "写入文件" + path + "成功!!!");
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
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
	public void zipedFiles(HashMap<String, Object> zipedAbsFileNames,
			String zipOutAbsFileName) {

		try {
			zipOutAbsFileName = new String(zipOutAbsFileName.getBytes(),
					"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LogUtil.e(TAG, e1.getMessage());
		}

		byte[] buf = new byte[1024];
		try {
			// 创建压缩后文件的文件夹
			zipOutAbsFileName = zipOutAbsFileName.replace("\\", "/");
			File zipOutAbsFileNameFolder = new File(
					zipOutAbsFileName.substring(0,
							zipOutAbsFileName.lastIndexOf("/")));
			if (!zipOutAbsFileNameFolder.exists()) {
				zipOutAbsFileNameFolder.mkdirs();
			}
			// 进行压缩操作
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipOutAbsFileName));
			Iterator<Map.Entry<String, Object>> iterator = zipedAbsFileNames
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();

				String inputStr = new String(entry.getKey().getBytes(), "UTF-8");
				String outputStr = new String(entry.getValue().toString()
						.getBytes(), "UTF-8");

				File inputFile = new File(inputStr);
				ArrayList<File> files = new ArrayList<File>();
				getAbsFiles(inputFile, files);

				if (files.size() > 1) {
					// 是目录
					for (File file : files) {
						inputStr = file.getAbsolutePath();
						if (inputStr.indexOf(outputStr) != -1) {

							outputStr = inputStr.substring(inputStr
									.indexOf(outputStr) + 1);

							FileInputStream in = new FileInputStream(inputStr);
							out.putNextEntry(new ZipEntry(outputStr));
							int len;
							while ((len = in.read(buf)) > 0) {
								out.write(buf, 0, len);
							}
							out.closeEntry();
							in.close();

							outputStr = new String(entry.getValue().toString()
									.getBytes(), "UTF-8");
						}
					}
				} else {
					// 是一个文件的绝对路径
					outputStr = inputStr
							.substring(inputStr.indexOf(outputStr) + 1);

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
			LogUtil.e(TAG,"//"+ e.getMessage());
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
	synchronized public ArrayList<File> deZipFiles(String zipedAbsFileName,
			String destDir) {// 解压后并删除原压缩文件

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
		ZipFile zipFile;
		try {
			File file = new File(zipedAbsFileName);
			zipFile = new ZipFile(file);
			Enumeration<?> enumeration = zipFile.entries();
			ZipEntry zipEntry = null;

			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				File loadFile = new File(destDir + zipEntry.getName());
				File load = new File(destDir + "service.txt");
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
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
		return new ArrayList<File>();
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
	synchronized public ArrayList<File> deZipFiles(String zipedAbsFileName,
			String destDir, int i) {// 解压后并删除原压缩文件

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
		ZipFile zipFile;
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
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
		return new ArrayList<File>();
	}

	/**
	 * 递归遍历文件夹
	 * 
	 * @param filepath
	 * @return
	 */
	public void getAbsFiles(File filepath, ArrayList<File> fileslist) {

		File[] files = filepath.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				getAbsFiles(file, fileslist);
			} else {
				fileslist.add(file);
			}
		}
	}

	private static ArrayList<File> allfiles;

	/**
	 * 返回目录下的文件夹集合
	 * 
	 * @param filePath
	 * @return
	 */
	public ArrayList<File> getFileDir(String filePath) {// 返回目录下的文件夹
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
		String behand = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		if (behand.equals("jpg") || behand.equals("mp4")
				|| behand.equals("amr") || "png".equals(behand))
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
	public static ArrayList<ArrayList<File>> getAllMedias(
			ArrayList<File> filelist) {
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
	 * 获取文件集合中文件数量
	 * 
	 * @param list
	 * @return
	 */
	public static int getMediaFileNum(ArrayList<ArrayList<File>> list) {
		int count = 0;
		for (ArrayList<File> flist : list) {
			count += flist.size();
		}
		return count;
	}

	/**
	 * Description:获得所有的多媒体文件，存入allfiles
	 * 
	 * @param path
	 * @return
	 * @author Administrator Create at: 2012-12-3 下午5:56:48
	 */
	public static ArrayList<File> getAllMediaFiles(String path) {
		allfiles = new ArrayList<File>();
		File file = new File(path);
		RecursionDir(file);
		return allfiles;
	}

	/**
	 * Description:获取当前文件夹下所有的html文件
	 * 
	 * @param path
	 *            目标路径
	 * @return html文件集合
	 * @author wanglg
	 * @Create at: 2013-7-11 上午11:35:45
	 */
	public static ArrayList<File> getAllHtmlFiles(String path) {
		ArrayList<File> allFiles = new ArrayList<File>();
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {

				if (files[i].isFile()) {
					String fileName = files[i].getName();
					String behand = fileName.substring(
							fileName.lastIndexOf(".") + 1, fileName.length());
					if (behand.equals("html")) {
						allFiles.add(files[i]);
					}
				}

			}

		}

		return allFiles;
	}

	/**
	 * Description:递归遍历返回多媒体文件集合
	 * 
	 * @param file
	 * @author Administrator
	 * @Create at: 2013-4-24 下午7:17:50
	 */
	public static void RecursionDir(File file) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && isMedia(files[i].getName())) {
				allfiles.add(files[i]);
			} else if (files[i].isDirectory()) {
				RecursionDir(files[i]);
			}
		}
	}

	/**
	 * Description:获得参数list中的所有文件，存入ArrayList
	 * 
	 * @param list
	 * @return ArrayList
	 * @author 王振洋 Create at: 2012-12-3 下午6:01:18
	 */
	public static ArrayList<File> getAllMediaFilesList(
			ArrayList<ArrayList<File>> list) {
		ArrayList<File> filesList = new ArrayList<File>();
		for (ArrayList<File> f : list) {
			for (File file : f) {
				filesList.add(file);
			}
		}
		return filesList;
	}

	// /**
	// * 打开视频文件
	// * @param context
	// * @param file
	// */
	// public static void openVideo(Context context,File file){
	// Intent it=new Intent("android.intent.action.VIEW");
	// it.addCategory("android.intent.category.DEFAULT");
	// it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// it.putExtra("oneshot", 0);
	// it.putExtra("configchange", 0);
	// Uri uri = Uri.fromFile(file);
	// it.setDataAndType(uri, "video/*");
	// context.startActivity(it);
	// }
	//
	// /**
	// * 打开音频文件
	// * @param context
	// * @param file
	// */
	// public static void openAudio(Context context, File file){
	// Intent intent = new Intent("android.intent.action.VIEW");
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// intent.putExtra("oneshot", 0);
	// intent.putExtra("configchange", 0);
	// Uri uri = Uri.fromFile(file);
	// intent.setDataAndType(uri, "audio/*");
	// context.startActivity(intent);
	// }
	//
	// /**
	// * 打开图片文件
	// * @param context
	// * @param file
	// */
	// public static void openImage(Context context, File file){
	// Intent intent = new Intent("android.intent.action.VIEW");
	//
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// intent.putExtra("oneshot", 0);
	// intent.putExtra("configchange", 0);
	// Uri uri = Uri.fromFile(file);
	// intent.setDataAndType(uri, "image/*");
	// context.startActivity(intent);
	// }

	/**
	 * 获取文件类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileType(File file) {
		String type = file.getName().substring(
				file.getName().lastIndexOf(".") + 1, file.getName().length());
		return type;
	}

	/**
	 * 格式化字符串 太长的字符串只取前length位，使用...补充
	 * 
	 * @author Liusy
	 * @param textResult
	 * @return
	 */
	public static String formatTextLength(String s, int length) {
		if (s != null && s.length() > length) {
			return s.substring(0, length) + "...";
		}
		return s;
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
	 * 下载文件方法
	 * 
	 * @param urlpath
	 *            网络地址
	 * @param filepath
	 *            文件存放地址
	 */

	public static void downLoadFile(String urlpath, String filepath) {
		FileOutputStream fos = null;
		InputStream in = null;
		try {
			URL url = new URL(urlpath);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// long length = con.getContentLength();
			in = con.getInputStream();

			File f = new File(PathManager.SDCARD_DATA_LOCAL_PATH);// 如果没有目录先建立目录
			if (!f.exists())
				f.mkdirs();
			File fil = new File(filepath);
			fos = new FileOutputStream(fil);
			byte[] bytes = new byte[1024];
			int flag = -1;
			int count = 0;// 文件总字节长度
			while ((flag = in.read(bytes)) != -1) {// 若未读到文件末尾则一直读取
				fos.write(bytes, 0, flag);
				count += flag;
			}
			fos.flush();

		} catch (ClientProtocolException e) {
			RecordLog.WriteCaughtEXP(e, "DownFile");
			e.printStackTrace();
		} catch (IOException e) {
			RecordLog.WriteCaughtEXP(e, "DownFile");
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过传递一个url下载对应的文件。
	 * 
	 * @param strUrl
	 *            下载文件的url地址
	 * @param path
	 *            保存文件的路径
	 * @param fileName
	 *            保存文件的文件名
	 * @return 0:文件下载成功 1:文件下载失败 EXIST:同名文件已存在
	 * @author wanglg 2013-05-13 创建
	 */
	public int fileDownload(String strUrl, String path, String fileName) {

		InputStream inputStream = null;
		File file = null;
		/*
		 * File file2=new File(SDCardPath+path+fileName); if(file2.exists()){
		 * file2.delete(); }
		 */
		int result = 0;

		try {
			inputStream = getInputStream(strUrl);
			file = writeToSDCard(path, fileName, inputStream);
			if (file == null || !file.exists()) {
				result = 1;
			}
		} catch (IOException e) { // 网络IO出错
			e.printStackTrace();
			result = 2;
		} catch (Exception e) {// filenotfound错误或其他错误
			e.printStackTrace();
			result = 1;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 将一个输入流中的内容写入到SD卡上生成文件 （如果本地已经存在该文件则删掉重新创建）
	 * 
	 * @param path
	 *            文件目录
	 * @param fileName
	 *            文件名
	 * @param inputStream
	 *            字节输入流
	 * @return 得到的文件
	 */
	public File writeToSDCard(String path, String fileName,
			InputStream inputStream) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, len);
				Log.v("lfwang", "===============" + len);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(file != null && file.exists()) {
				file.delete();
			}
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
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
			dir.mkdirs();
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
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		return file;
	}

	/**
	 * 通过一个url获取http连接的输入流
	 * 
	 * @param strUrl
	 *            目标url
	 * @return 到该url的http连接的输入流
	 * @throws IOException
	 */
	private InputStream getInputStream(String strUrl) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		
		urlConn.setRequestProperty("User-Agent", "PacificHttpClient");   
		urlConn.setConnectTimeout(10000); //连接超时时间
		urlConn.setReadTimeout(20000);    //读取数据超时时间
        if (urlConn.getResponseCode() == 404) {   
            throw new IOException("fail!");   
        }   
         
		InputStream is = urlConn.getInputStream();
		return is;
	}

	/**
	 * 执行文件复制操作
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
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
	 * Description:递归扫描文件
	 * 
	 * @param filePath
	 *            要扫描的目录路径
	 * @param exten
	 *            文件后缀名，大小写区分
	 * @param result
	 * @author 王振洋 Create at: 2012-12-4 上午10:41:58
	 */
	private static void scanFile(String filePath, String exten,
			ArrayList<File> result) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (File f : files) {
						scanFile(f.getAbsolutePath(), exten, result);
					}
				}
			} else {
				if (file.getName().endsWith(exten)) {
					result.add(file);
				}
			}
		}

	}

	/**
	 * Description:扫描并返回特殊后缀名的文件集合
	 * 
	 * @param filePath
	 *            要扫描的目录路径
	 * @param exten
	 *            需要的文件后缀名，区分大小写
	 * @return 搜索结果集合
	 * @author 王振洋 Create at: 2012-12-4 上午10:44:41
	 */
	public static ArrayList<File> searchFiles(String filePath, String exten) {
		ArrayList<File> resultFile = new ArrayList<File>();
		scanFile(filePath, exten, resultFile);
		return resultFile;
	}

	/**
	 * Description: 调用系统共享功能
	 * 
	 * @param uri
	 *            发送文件路径
	 * @param context
	 * @author Administrator
	 * @Create at: 2013-5-9 上午9:17:04
	 */
	public static void sendToBluetooth(String uri, Context context) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "body");
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
		// shareIntent.setType("text/plain");
		shareIntent.setType("image/*");
		context.startActivity(shareIntent);
	}

	/**
	 * Description: 打开不同格式的附件
	 * 
	 * @param type
	 *            文件格式
	 * @param param
	 *            文件路径
	 * @param paramBoolean
	 * @param context
	 *            上下文
	 * @param file
	 *            要打开的文件 void
	 * @author 王红娟 Create at: 2012-12-7 下午01:16:05
	 */
	public static void OpenFile(String type, String param,
			boolean paramBoolean, Context context, File file) {
		FileType filetype = getFileType(type);
		Intent intent = new Intent("android.intent.action.VIEW");
		Uri uri = null;
		switch (filetype) {
		case HTML:
			uri = Uri.parse(param).buildUpon()
					.encodedAuthority("com.android.htmlfileprovider")
					.scheme("content").encodedPath(param).build();
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
			Toast.makeText(context, "附件无法打开，请下载相关软件", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * FileName: FileHelper.java Description: 文件类型的枚举类
	 * 
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-7 下午01:18:21
	 */
	public static enum FileType {

		HTML("text/html"), IMAGE("image/*"), PDF("application/pdf"), TXT(
				"text/plain"), AUDIO("audio/*"), VIDEO("video/*"), CHM(
				"application/x-chm"), WORD("application/msword"), EXCEL(
				"application/vnd.ms-excel"), PPT(
				"application/vnd.ms-powerpoint");

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

	// 判断媒体是否存在在内存卡，若存在直接打开，若不存在则下载。
	/**
	 * @param filePath
	 *            :文件名称
	 * @return 0:文件下载成功 1:文件下载失败 EXIST:同名文件已存在
	 * 
	 */
	public void showFile(String filePath , Context context) {
		String sql = "select * from T_Attachment where filepath = '" + filePath
				+ "'";
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		if (list != null && list.size() > 0) {
			int fk_unit = Integer.parseInt(list.get(0).get("fk_unit")
					.toString());
			T_Attachment t_attach = new T_Attachment();
			String strUrl = Global.getGlobalInstance().getSystemurl()
					+ "/Attach/" + t_attach.transitToChinese(fk_unit) + "/"
					+ filePath;
			String path = "mapuni/MobileEnforcement/" + "Attach/"
					+ t_attach.transitToChinese(fk_unit) + "/";
			String fileName = filePath;
			String p = Global.SDCARD_RASK_DATA_PATH + "Attach/"
					+ t_attach.transitToChinese(fk_unit) + "/" + fileName;
			
			// 判断本地是否有文件，如果没有则下载。
			if (!FileHelper.isFileExist(p)) {
				new CustomAsyncTask(context, strUrl, path, fileName, p).execute();
			} else {
				File file = new File(p);
				openFile(file, context);
			}
		}
	}

	/**
	 * @param guid
	 *            :文件guid
	 * @return 0:文件下载成功 1:文件下载失败 EXIST:同名文件已存在
	 * 
	 */
	public void showFileByGuid(String guid, Context context) {
		String sql = "select * from T_Attachment where guid = '" + guid + "'";
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		if (list != null && list.size() > 0) {

			int fk_unit = Integer.parseInt(list.get(0).get("fk_unit")
					.toString());
			String extension = list.get(0).get("extension").toString();
			T_Attachment t_attach = new T_Attachment();
			String strUrl = Global.getGlobalInstance().getSystemurl()
					+ "/Attach/" + t_attach.transitToChinese(fk_unit) + "/"
					+ guid + extension;
			String path = "mapuni/MobileEnforcement/" + "Attach/"
					+ t_attach.transitToChinese(fk_unit) + "/";
			String fileName = guid + extension;
			String p = Global.SDCARD_RASK_DATA_PATH + "Attach/"
					+ t_attach.transitToChinese(fk_unit) + "/" + fileName;
			// 判断本地是否有文件，如果没有则下载。
			if (!FileHelper.isFileExist(p)) {
				new CustomAsyncTask(context, strUrl, path, fileName, p)
						.execute();
			} else {
				File file = new File(p);
				openFile(file, context);
			}
		}

	}

	private class CustomAsyncTask extends AsyncTask<Void, Void, Integer> {
		private YutuLoading yutuLoading;
		private Context context;
		private String strUrl;
		private String path;
		private String fileName;
		private String p;

		public CustomAsyncTask(Context context, String strUrl, String path,
				String fileName, String p) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.strUrl = strUrl;
			this.path = path;
			this.fileName = fileName;
			this.p = p;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			yutuLoading = new YutuLoading(context);
			yutuLoading.setCancelable(false);
			yutuLoading.showDialog();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			int flag = -1;

			if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
				if(Net.checkURL(strUrl)) {
					int result = 1;
					result = fileDownload(strUrl, path, fileName);
					if (result == 2) {
						flag = 1;
					} else if (result != 0) {
						flag = 2;
					} else {
						flag = 3;
					}
				} else {
					flag = 4;
				}
			} else {
				flag = 0;
			}

			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}

			switch (result) {
			case 0:
				Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(context, "网络不给力啊，文件下载失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(context, "文件下载失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				File file = new File(p);
				openFile(file, context);
				break;
			case 4:
				Toast.makeText(context, "文件不存在！", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

			super.onPostExecute(result);
		}

	}

	/**
	 * wsc
	 * 
	 * @param guid
	 *            :文件guid
	 * @return 0:文件下载成功 1:文件下载失败 EXIST:同名文件已存在
	 * 
	 */
	public int showFileByQyid(String guid, String fkunit, Context context) {

		T_Attachment t_attach = new T_Attachment();
		String sql = "select filepath,extension from T_Attachment where  fk_id ='"
				+ guid + "' and fk_unit=" + t_attach.getCode(fkunit);
		// String sql =
		// "select filepath,extension from T_Attachment where  Guid ='" + guid +
		// "' and fk_unit=" + t_attach.getCode(fkunit) ;
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

		try {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					String extension = list.get(i).get("extension").toString();
					String filepath = list.get(i).get("filepath").toString();
					String strUrl = Global.getGlobalInstance().getSystemurl()
							+ "/Attach/" + fkunit + "/" + filepath;
					String path = "mapuni/MobileEnforcement/" + "Attach/"
							+ fkunit + "/";

					String p = Global.SDCARD_RASK_DATA_PATH + "Attach/"
							+ fkunit + "/" + filepath;
					
					// 判断本地是否有文件，如果没有则下载。
					if (!FileHelper.isFileExist(p)) {
						new CustomAsyncTask(context, strUrl, path, filepath, p)
								.execute();
					} else {
						File file = new File(p);
						openFile(file, context);
					}
				}
			} else {
				Toast.makeText(context, "附件不存在", Toast.LENGTH_SHORT).show();
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;

		}
		return 1;
	}

	/**
	 * 
	 * @param guid
	 *            :文件guid
	 * @return 0:文件下载成功 1:文件下载失败 EXIST:同名文件已存在
	 * 
	 */
	public ArrayList<String> downFileByQyid(String guid, String fkunit,
			Context context) {

		T_Attachment t_attach = new T_Attachment();
		String sql = "select filepath,extension from T_Attachment where  fk_id ='"
				+ guid + "' and fk_unit=" + t_attach.getCode(fkunit);
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		ArrayList<String> array = new ArrayList<String>();

		try {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					String extension = list.get(i).get("extension").toString();
					String filepath = list.get(i).get("filepath").toString();
					String strUrl = Global.getGlobalInstance().getSystemurl()
							+ "/Attach/" + fkunit + "/" + filepath;
					String path = "mapuni/MobileEnforcement/" + "Attach/"
							+ fkunit + "/";

					String p = Global.SDCARD_RASK_DATA_PATH + "Attach/"
							+ fkunit + "/" + filepath;
					int result = 1;
					// 判断本地是否有文件，如果没有则下载。
					if (!FileHelper.isFileExist(p)) {
						if (!Net.checkURL(Global.getGlobalInstance()
								.getSystemurl())) {

						} else {
							result = fileDownload(strUrl, path, filepath);

						}
					} else {
						result = 0;
					}

					if (result == 0) {
						File file;
						if (extension.equalsIgnoreCase(".jpg")
								|| extension.equalsIgnoreCase(".png")
								|| extension.equalsIgnoreCase(".bmp")) {
							array.add(filepath);
						} else if (extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".mts")) {
							file = new File(p);
							FileHelper.OpenFile(
									FileHelper.FileType.VIDEO.getType(), "",
									false, context, file);
						} else if (extension.equalsIgnoreCase(".amr")) {
							file = new File(p);
							FileHelper.OpenFile(
									FileHelper.FileType.AUDIO.getType(), "",
									false, context, file);
						} else if (extension.equalsIgnoreCase(".doc")
								|| extension.equalsIgnoreCase(".docx")) {
							file = new File(p);
							FileHelper.OpenFile(
									FileHelper.FileType.WORD.getType(), "",
									false, context, file);
						} else if (extension.equalsIgnoreCase(".xlsx")) {
							file = new File(p);
							FileHelper.OpenFile(
									FileHelper.FileType.EXCEL.getType(), "",
									false, context, file);
						} else if (extension.equalsIgnoreCase(".pdf")) {
							file = new File(p);
							FileHelper.OpenFile(
									FileHelper.FileType.PDF.getType(), "",
									false, context, file);
						} else if (extension.equalsIgnoreCase(".ppt")
								|| extension.equalsIgnoreCase(".dps")) {
							file = new File(p);
							FileHelper.OpenFile(
									FileHelper.FileType.PPT.getType(), "",
									false, context, file);
						}
					}

				}
				return array;
			} else {
				return new ArrayList<String>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	/**
	 * @author wanglg
	 * @param file
	 *            文件对象
	 * @param context
	 *            上下文对象
	 */
	public static void openFile(File file, Context context) {
		Intent intent = getopenFileIntent(file.getAbsolutePath());
		if (intent != null) {
			try {
				context.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(context, "附件无法打开，请下载相关软件", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, "文件不存在！", Toast.LENGTH_SHORT).show();

		}

	}

	/**
	 * 获取附件打开的意图
	 * 
	 * @param filePath
	 * @return
	 */
	public static Intent getopenFileIntent(String filePath) {

		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		/* 取得扩展名 */
		String end = file
				.getName()
				.substring(file.getName().lastIndexOf(".") + 1,
						file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")
				|| end.equals("amr")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("3gp") || end.equals("mp4") || end.equals("mts")) {
			return getVideoFileIntent(filePath);
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			return getImageFileIntent(filePath);
		} else if (end.equals("apk")) {
			return getApkFileIntent(filePath);
		} else if (end.equals("ppt")) {
			return getPptFileIntent(filePath);
		} else if (end.equals("xls")) {
			return getExcelFileIntent(filePath);
		} else if (end.equals("doc")) {
			return getWordFileIntent(filePath);
		} else if (end.equals("pdf")) {
			return getPdfFileIntent(filePath);
		} else if (end.equals("chm")) {
			return getChmFileIntent(filePath);
		} else if (end.equals("txt")) {
			return getTextFileIntent(filePath, false);
		} else {
			return getAllIntent(filePath);
		}
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android获取一个用于打开Html文件的intent
	public static Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// Android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// Android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// Android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// Android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(String param, boolean paramBoolean) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// Android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
}
