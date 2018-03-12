package com.jy.environment.model;

public class PicPath {

			String path = "";
			boolean isNeedNet = true;
			
			public PicPath(String path, boolean isNeedNet) {
				super();
				this.path = path;
				this.isNeedNet = isNeedNet;
			}
			public String getPath() {
				return path;
			}
			public void setPath(String path) {
				this.path = path;
			}
			public boolean isNeedNet() {
				return isNeedNet;
			}
			public void setNeedNet(boolean isNeedNet) {
				this.isNeedNet = isNeedNet;
			}
			
}
