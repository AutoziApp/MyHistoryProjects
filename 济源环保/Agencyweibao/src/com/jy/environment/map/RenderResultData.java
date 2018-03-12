package com.jy.environment.map;

import android.graphics.Bitmap;

/**
 * @author shang
 * @category 描述渲染操作后的数据信息，包括渲染区域和渲染的bitmap。
 *
 */
public class RenderResultData {
	
	private RenderBlock _rblock;
	
	private Bitmap _bitmap; 
	
	
	
	public RenderResultData(){
		
	}

	public RenderBlock get_rblock() {
		return _rblock;
	}

	public void set_rblock(RenderBlock _rblock) {
		this._rblock = _rblock;
	}

	public Bitmap get_bitmap() {
		return _bitmap;
	}

	public void set_bitmap(Bitmap _bitmap) {
		this._bitmap = _bitmap;
	}

	
}
