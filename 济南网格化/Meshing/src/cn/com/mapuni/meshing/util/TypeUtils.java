package cn.com.mapuni.meshing.util;

public class TypeUtils {
	
	
	public static boolean isIMG(String imgPath){
		String[] allowTypes=new String[] {".bmp",".pcx","tiff",".gif",".jpeg",".tga"
										,".exif",".fpx","svg",".psd",".cdr",".pcd"
										,".dxf",".ufo",".eps",".ai",".png",".hdri"
										,".raw",".wmf",".emf",".jpg"};
		for(String type:allowTypes){
			if(imgPath.toLowerCase().indexOf(type)>-1){
				return true;
			}
		}
		return false;
	}

}
