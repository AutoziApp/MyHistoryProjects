package cn.com.mapuni.meshing.base.controls.surfaceview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * FileName: CameraSurfaceView.java
 * Description:SurfaceViewʵ����Ƶ��
 * @author liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2013-3-25 ����02:52:03
 */
public class CameraSurfaceView extends SurfaceView implements PictureCallback{
	private Context mContext;
	private Camera myCamera;;
	
	boolean isPreview = false;
	
	//���ձ���·��
	private String filePath;
	
	public CameraSurfaceView(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
	 * Description:��ʼ�������Դ
	 * @param caremaFront 0:��������ͷ  1: ǰ������ͷ
	 * @author Administrator<br>
	 * Create at: 2013-3-25 ����02:32:24
	 */
	public void initCamera(int caremaFront, SurfaceHolder mySurfaceHolder) {
		if (!isPreview) {
			if(caremaFront == 0) {
				//Ĭ�Ϻ�������ͷ
				myCamera = android.hardware.Camera.open();
			} else {
				android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
				for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
					Camera.getCameraInfo(i, cameraInfo);
					if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
						myCamera = Camera.open(i);
					}
				}
			}
		}
		if (myCamera != null && !isPreview) {
			try {
				Camera.Parameters parameters = myCamera.getParameters();
				parameters.set("preview-size-values", "480x800");
				parameters.set("jpeg-quality",85);
				
				// parameters.setPreviewFormat(PixelFormat.RGB_565); // ����ͼƬ��ʽ
				// parameters.setPreviewSize(320, 240); // ����preview ��Ļ��С
				// parameters.set("picture-size-values", "320x240");
				// parameters.setPictureSize(320, 240); // ����ͼƬ�ֱ���
				// parameters.setPictureFormat(PixelFormat.JPEG);
				//parameters.setPreviewSize(mySurfaceView.getWidth(), mySurfaceView.getHeight());
				
				
				myCamera.setParameters(parameters);
				myCamera.setDisplayOrientation(90);
				myCamera.setPreviewDisplay(mySurfaceHolder);
				myCamera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isPreview = true;
		}
	}
	
	/**
	 * Description:�ͷ������Դ
	 * 
	 * @author Administrator<br>
	 * Create at: 2013-3-25 ����02:31:02
	 */
	public void stopCamera() {
		if (myCamera != null && isPreview) {
			myCamera.stopPreview();
			myCamera.release();
			myCamera = null;
			isPreview = false;
		}
	}
	
	/**
	 * Description:����
	 * @param photoFilePath
	 * @author Administrator<br>
	 * Create at: 2013-3-25 ����02:31:18
	 */
	public void takePhoto(String photoFilePath) {
		filePath = photoFilePath;
		myCamera.takePicture(null, null, null, this);
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		
		try {
			file = new File(filePath);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(data);
			Toast.makeText(mContext, "���ճɹ���", 1).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
