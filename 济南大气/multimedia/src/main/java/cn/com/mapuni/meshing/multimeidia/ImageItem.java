package cn.com.mapuni.meshing.multimeidia;

import java.io.Serializable;

/**
 * һ��ͼƬ����
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
