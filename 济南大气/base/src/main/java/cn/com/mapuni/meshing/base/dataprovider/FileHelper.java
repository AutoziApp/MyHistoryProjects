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
 * FileName: FileHelper.java Description:�ļ�����������
 * 
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����3:44:13
 */
public class FileHelper {

	private final String TAG = "FileHelper";
	// public static final String BASE_PATH = "/sdcard/mapuni/";
	// public static final String SDCARD_RASK_DATA_PATH = BASE_PATH
	// +"MobileEnforcement/";
	// public static final String SDCARD_date_LOCAL_PATH = SDCARD_RASK_DATA_PATH
	// +"data";//�ֻ���data�ļ�Ŀ¼
	private final String SDCardPath = Environment.getExternalStorageDirectory()
			+ "/";

	/**
	 * �����ܵ�64λ�ַ���ת��Ϊ�����ļ�
	 * 
	 * @param base64String
	 *            base64�����ַ���
	 * @param savePath
	 *            �����ַ
	 * @param fileName
	 *            �ļ�����
	 */
	synchronized public void convertBase64StringToLocalFile(
			String base64String, String dir, // 64�ַ�ת��Ϊ����ѹ���ļ�
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
	 * �����ܵ�64λListת��Ϊ�����ļ�
	 * 
	 * @param base64String
	 *            base64�����ַ���
	 * @param savePath
	 *            �����ַ
	 * @param fileName
	 *            �ļ�����
	 */
	synchronized public void convertBase64ListToLocalFile(
			List<String> base64List, String dir, // 64�ַ�ת��Ϊ����ѹ���ļ�
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
	 * �����ܵ�64λArrayת��Ϊ�����ļ�
	 * 
	 * @param base64String
	 *            base64�����ַ���
	 * @param savePath
	 *            �����ַ
	 * @param fileName
	 *            �ļ�����
	 */
	synchronized public void convertBase64ArrayToLocalFile(
			String[] base64Array, String dir, // 64�ַ�ת��Ϊ����ѹ���ļ�
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
	 * ���ֽ�ת��Ϊ�����ļ�
	 * 
	 * @param content
	 *            ��ȡ������
	 * @param savePath
	 *            �����ַ
	 * @param fileName
	 *            �ļ�����
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
			LogUtil.v(TAG, dir + fileName + " �ɹ�ת��Ϊ�����ļ�");
		} catch (FileNotFoundException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
	}

	/**
	 * ���ַ���д���ļ�
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
			LogUtil.e(TAG, "д���ļ�" + path + "�ɹ�!!!");
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
	}

	/**
	 * ��ȡ�ļ���ת��Ϊbyte�ֽ�����
	 * 
	 * @param filepath
	 *            �ļ��ľ���·��
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
			/* ����webserice�Ĳ������� */
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

			LogUtil.v(TAG, absFileName + "\t�ɹ�ת��ΪBase64 Bytes");
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
	 * ��ȡ�ļ���ת��Ϊbyte�ֽ���������ϴ�����
	 * 
	 * @param filepath
	 *            �ļ��ľ���·��
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
			/* ����webserice�Ĳ������� */
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

			LogUtil.v(TAG, absFileName + "\t�ɹ�ת��ΪBase64 Bytes");
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
	 * ��ȡ�ļ����ֶ�ת��Ϊbyte�ֽ�����
	 * 
	 * @param filepath
	 *            �ļ��ľ���·��
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
				/* ����webserice�Ĳ������� */
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
			LogUtil.v(TAG, absFileName + "\t�ɹ�ת��ΪBase64 Bytes");
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
	 * ���ַ���д�����񸽼��ļ�
	 * 
	 * @param path
	 * @param content
	 */
	public void convertStringToLocalFile1(String path, String content) {
		try {
			File f = new File("/sdcard/mapuni/MobileEnforcement/rwfj");// ���û��Ŀ¼�Ƚ���Ŀ¼
			if (!f.exists())
				f.mkdirs();
			File fil = new File("/sdcard/mapuni/MobileEnforcement/rwfj/" + path);// ��Ŀ¼֮���ļ�
			FileWriter fw = new FileWriter(fil);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
			LogUtil.e(TAG, "д���ļ�" + path + "�ɹ�!!!");
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
	}

	/**
	 * ��һ���ļ������ zip ѹ������ѹ������û���ļ���Ŀ¼
	 * 
	 * @param zipedAbsFileNames
	 *            1�����԰�һ���ļ��Ĵ洢����·�����ƺͶ�Ӧѹ���ļ���Ŀ¼������� key: /sdcard/test.txt ;
	 *            value: test.txt 2������ֱ��ѹ��һ���ļ��������е��ļ���ѹ���ļ������·��Ϊ�ļ���·����һ���� key:
	 *            /sdcard/floder/ ; value: floder/
	 * @param zipOutAbsFileName
	 *            ������ zip �ļ���
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
			// ����ѹ�����ļ����ļ���
			zipOutAbsFileName = zipOutAbsFileName.replace("\\", "/");
			File zipOutAbsFileNameFolder = new File(
					zipOutAbsFileName.substring(0,
							zipOutAbsFileName.lastIndexOf("/")));
			if (!zipOutAbsFileNameFolder.exists()) {
				zipOutAbsFileNameFolder.mkdirs();
			}
			// ����ѹ������
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
					// ��Ŀ¼
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
					// ��һ���ļ��ľ���·��
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
			LogUtil.v(TAG, zipOutAbsFileName + " ѹ���ɹ�");
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
	}

	/**
	 * ��ѹ zip �ļ��������ؽ�ѹ�ļ��ľ���·��
	 * 
	 * @param zipedAbsFileName
	 *            ѹ���ļ��ľ���·��
	 * @param destDir
	 *            ��ѹ�����ļ���
	 * @return
	 */
	synchronized public ArrayList<File> deZipFiles(String zipedAbsFileName,
			String destDir) {// ��ѹ��ɾ��ԭѹ���ļ�

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
			LogUtil.v(TAG, zipedAbsFileName + " : �ļ���ѹ�ɹ�\n" + "��ѹĿ¼ ��" + destDir);
			return deZipFiles;
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
		return new ArrayList<File>();
	}

	/**
	 * ��ѹ zip �ļ��������ؽ�ѹ�ļ��ľ���·��
	 * 
	 * @param zipedAbsFileName
	 *            ѹ���ļ��ľ���·��
	 * @param destDir
	 *            ��ѹ�����ļ���
	 * @return
	 */
	synchronized public ArrayList<File> deZipFiles(String zipedAbsFileName,
			String destDir, int i) {// ��ѹ��ɾ��ԭѹ���ļ�

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
			LogUtil.v(TAG, zipedAbsFileName + " : �ļ���ѹ�ɹ�\n" + "��ѹĿ¼ ��" + destDir);
			return deZipFiles;
		} catch (IOException e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
		return new ArrayList<File>();
	}

	/**
	 * �ݹ�����ļ���
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
	 * ����Ŀ¼�µ��ļ��м���
	 * 
	 * @param filePath
	 * @return
	 */
	public ArrayList<File> getFileDir(String filePath) {// ����Ŀ¼�µ��ļ���
		ArrayList<File> fileList = new ArrayList<File>();
		File[] files = null;
		File file = new File(filePath);
		if (file.exists())
			files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory())// �������ļ��������
				fileList.add(f);
		}
		return fileList;
	}

	/**
	 * �ж��ļ�����׺���Ƿ��Ƕ�ý���ļ�
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isMedia(String fileName) {// �ж��ļ�����׺�Ƿ�Ϊ��ý���ļ�(mp3,amr,mp4)
		String behand = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		if (behand.equals("jpg") || behand.equals("mp4")
				|| behand.equals("amr") || "png".equals(behand))
			return true;
		else
			return false;
	}

	/**
	 * ��ȡ���еĶ�ý���ļ�
	 * 
	 * @param filelist
	 * @return
	 */
	public static ArrayList<ArrayList<File>> getAllMedias(
			ArrayList<File> filelist) {
		ArrayList<ArrayList<File>> list = new ArrayList<ArrayList<File>>();
		for (File f : filelist) {// ����ÿ�������ļ���
			ArrayList<File> file = new ArrayList<File>();
			File[] filearray = f.listFiles();
			for (File fi : filearray) {// ����ÿ�������ļ����еĶ�ý���ļ�
				if (isMedia(fi.getName())) {
					file.add(fi);
				}
			}
			list.add(file);
		}
		return list;
	}

	/**
	 * ��ȡ�ļ��������ļ�����
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
	 * Description:������еĶ�ý���ļ�������allfiles
	 * 
	 * @param path
	 * @return
	 * @author Administrator Create at: 2012-12-3 ����5:56:48
	 */
	public static ArrayList<File> getAllMediaFiles(String path) {
		allfiles = new ArrayList<File>();
		File file = new File(path);
		RecursionDir(file);
		return allfiles;
	}

	/**
	 * Description:��ȡ��ǰ�ļ��������е�html�ļ�
	 * 
	 * @param path
	 *            Ŀ��·��
	 * @return html�ļ�����
	 * @author wanglg
	 * @Create at: 2013-7-11 ����11:35:45
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
	 * Description:�ݹ�������ض�ý���ļ�����
	 * 
	 * @param file
	 * @author Administrator
	 * @Create at: 2013-4-24 ����7:17:50
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
	 * Description:��ò���list�е������ļ�������ArrayList
	 * 
	 * @param list
	 * @return ArrayList
	 * @author ������ Create at: 2012-12-3 ����6:01:18
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
	// * ����Ƶ�ļ�
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
	// * ����Ƶ�ļ�
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
	// * ��ͼƬ�ļ�
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
	 * ��ȡ�ļ�����
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
	 * ��ʽ���ַ��� ̫�����ַ���ֻȡǰlengthλ��ʹ��...����
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
	 * ����ļ��Ƿ����
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return
	 */
	public static boolean isFileExist(String path) {
		return new File(path).exists();
	}

	/**
	 * �����ļ�����
	 * 
	 * @param urlpath
	 *            �����ַ
	 * @param filepath
	 *            �ļ���ŵ�ַ
	 */

	public static void downLoadFile(String urlpath, String filepath) {
		FileOutputStream fos = null;
		InputStream in = null;
		try {
			URL url = new URL(urlpath);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// long length = con.getContentLength();
			in = con.getInputStream();

			File f = new File(PathManager.SDCARD_DATA_LOCAL_PATH);// ���û��Ŀ¼�Ƚ���Ŀ¼
			if (!f.exists())
				f.mkdirs();
			File fil = new File(filepath);
			fos = new FileOutputStream(fil);
			byte[] bytes = new byte[1024];
			int flag = -1;
			int count = 0;// �ļ����ֽڳ���
			while ((flag = in.read(bytes)) != -1) {// ��δ�����ļ�ĩβ��һֱ��ȡ
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
	 * ͨ������һ��url���ض�Ӧ���ļ���
	 * 
	 * @param strUrl
	 *            �����ļ���url��ַ
	 * @param path
	 *            �����ļ���·��
	 * @param fileName
	 *            �����ļ����ļ���
	 * @return 0:�ļ����سɹ� 1:�ļ�����ʧ�� EXIST:ͬ���ļ��Ѵ���
	 * @author wanglg 2013-05-13 ����
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
		} catch (IOException e) { // ����IO����
			e.printStackTrace();
			result = 2;
		} catch (Exception e) {// filenotfound�������������
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
	 * ��һ���������е�����д�뵽SD���������ļ� ����������Ѿ����ڸ��ļ���ɾ�����´�����
	 * 
	 * @param path
	 *            �ļ�Ŀ¼
	 * @param fileName
	 *            �ļ���
	 * @param inputStream
	 *            �ֽ�������
	 * @return �õ����ļ�
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
	 * ��SD���ϴ���Ŀ¼ ������������򴴽�һ����
	 * 
	 * @param dirName
	 *            Ҫ������Ŀ¼��
	 * @return �����õ���Ŀ¼
	 */
	public File createSDDir(String dirName) {
		File dir = new File(SDCardPath + dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * ��SD���ϴ����ļ� ���Ѿ�������ɾ����
	 * 
	 * @param fileName
	 *            Ҫ�������ļ���
	 * @return �����õ����ļ�
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
	 * ͨ��һ��url��ȡhttp���ӵ�������
	 * 
	 * @param strUrl
	 *            Ŀ��url
	 * @return ����url��http���ӵ�������
	 * @throws IOException
	 */
	private InputStream getInputStream(String strUrl) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		
		urlConn.setRequestProperty("User-Agent", "PacificHttpClient");   
		urlConn.setConnectTimeout(10000); //���ӳ�ʱʱ��
		urlConn.setReadTimeout(20000);    //��ȡ���ݳ�ʱʱ��
        if (urlConn.getResponseCode() == 404) {   
            throw new IOException("fail!");   
        }   
         
		InputStream is = urlConn.getInputStream();
		return is;
	}

	/**
	 * ִ���ļ����Ʋ���
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
			// �½��ļ����������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			// �½��ļ���������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();
		} finally {
			// �ر���
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * Description:�ݹ�ɨ���ļ�
	 * 
	 * @param filePath
	 *            Ҫɨ���Ŀ¼·��
	 * @param exten
	 *            �ļ���׺������Сд����
	 * @param result
	 * @author ������ Create at: 2012-12-4 ����10:41:58
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
	 * Description:ɨ�貢���������׺�����ļ�����
	 * 
	 * @param filePath
	 *            Ҫɨ���Ŀ¼·��
	 * @param exten
	 *            ��Ҫ���ļ���׺�������ִ�Сд
	 * @return �����������
	 * @author ������ Create at: 2012-12-4 ����10:44:41
	 */
	public static ArrayList<File> searchFiles(String filePath, String exten) {
		ArrayList<File> resultFile = new ArrayList<File>();
		scanFile(filePath, exten, resultFile);
		return resultFile;
	}

	/**
	 * Description: ����ϵͳ������
	 * 
	 * @param uri
	 *            �����ļ�·��
	 * @param context
	 * @author Administrator
	 * @Create at: 2013-5-9 ����9:17:04
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
	 * Description: �򿪲�ͬ��ʽ�ĸ���
	 * 
	 * @param type
	 *            �ļ���ʽ
	 * @param param
	 *            �ļ�·��
	 * @param paramBoolean
	 * @param context
	 *            ������
	 * @param file
	 *            Ҫ�򿪵��ļ� void
	 * @author ����� Create at: 2012-12-7 ����01:16:05
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
			Toast.makeText(context, "�����޷��򿪣�������������", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * FileName: FileHelper.java Description: �ļ����͵�ö����
	 * 
	 * @author �����
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-7 ����01:18:21
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
	 * Description: ͨ����ͬ���ļ����ͷ��ز�ͬ��ö�ٶ���
	 * 
	 * @param type
	 *            ϵͳ���ļ�����
	 * @return �ļ�����ö�ٶ��� FileType
	 * @author ����� Create at: 2012-12-7 ����01:18:46
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

	// �ж�ý���Ƿ�������ڴ濨��������ֱ�Ӵ򿪣��������������ء�
	/**
	 * @param filePath
	 *            :�ļ�����
	 * @return 0:�ļ����سɹ� 1:�ļ�����ʧ�� EXIST:ͬ���ļ��Ѵ���
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
			
			// �жϱ����Ƿ����ļ������û�������ء�
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
	 *            :�ļ�guid
	 * @return 0:�ļ����سɹ� 1:�ļ�����ʧ�� EXIST:ͬ���ļ��Ѵ���
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
			// �жϱ����Ƿ����ļ������û�������ء�
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
				Toast.makeText(context, "���ӷ�����ʧ��", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(context, "���粻���������ļ�����ʧ�ܣ�", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(context, "�ļ�����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				File file = new File(p);
				openFile(file, context);
				break;
			case 4:
				Toast.makeText(context, "�ļ������ڣ�", Toast.LENGTH_SHORT).show();
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
	 *            :�ļ�guid
	 * @return 0:�ļ����سɹ� 1:�ļ�����ʧ�� EXIST:ͬ���ļ��Ѵ���
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
					
					// �жϱ����Ƿ����ļ������û�������ء�
					if (!FileHelper.isFileExist(p)) {
						new CustomAsyncTask(context, strUrl, path, filepath, p)
								.execute();
					} else {
						File file = new File(p);
						openFile(file, context);
					}
				}
			} else {
				Toast.makeText(context, "����������", Toast.LENGTH_SHORT).show();
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
	 *            :�ļ�guid
	 * @return 0:�ļ����سɹ� 1:�ļ�����ʧ�� EXIST:ͬ���ļ��Ѵ���
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
					// �жϱ����Ƿ����ļ������û�������ء�
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
	 *            �ļ�����
	 * @param context
	 *            �����Ķ���
	 */
	public static void openFile(File file, Context context) {
		Intent intent = getopenFileIntent(file.getAbsolutePath());
		if (intent != null) {
			try {
				context.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(context, "�����޷��򿪣�������������", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, "�ļ������ڣ�", Toast.LENGTH_SHORT).show();

		}

	}

	/**
	 * ��ȡ�����򿪵���ͼ
	 * 
	 * @param filePath
	 * @return
	 */
	public static Intent getopenFileIntent(String filePath) {

		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		/* ȡ����չ�� */
		String end = file
				.getName()
				.substring(file.getName().lastIndexOf(".") + 1,
						file.getName().length()).toLowerCase();
		/* ����չ�������;���MimeType */
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

	// Android��ȡһ�����ڴ�APK�ļ���intent
	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android��ȡһ�����ڴ�APK�ļ���intent
	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android��ȡһ�����ڴ�VIDEO�ļ���intent
	public static Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android��ȡһ�����ڴ�AUDIO�ļ���intent
	public static Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android��ȡһ�����ڴ�Html�ļ���intent
	public static Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android��ȡһ�����ڴ�ͼƬ�ļ���intent
	public static Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// Android��ȡһ�����ڴ�PPT�ļ���intent
	public static Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// Android��ȡһ�����ڴ�Excel�ļ���intent
	public static Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// Android��ȡһ�����ڴ�Word�ļ���intent
	public static Intent getWordFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// Android��ȡһ�����ڴ�CHM�ļ���intent
	public static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android��ȡһ�����ڴ��ı��ļ���intent
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

	// Android��ȡһ�����ڴ�PDF�ļ���intent
	public static Intent getPdfFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
}
