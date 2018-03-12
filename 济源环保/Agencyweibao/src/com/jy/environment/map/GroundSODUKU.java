package com.jy.environment.map;


import com.baidu.mapapi.map.GroundOverlay;


/**
 * @author SHANG
 * @category 瓦片九宫格排列描述
 */
public class GroundSODUKU {
	//---------------------------------
	//	排列规则：
	//		1	2	3
	//		8	0	4
	//		7	6	5
	//---------------------------------
	private GroundOverlay _g0=null;
	private GroundOverlay _g1=null;
	private GroundOverlay _g2=null;
	private GroundOverlay _g3=null;
	private GroundOverlay _g4=null;
	private GroundOverlay _g5=null;
	private GroundOverlay _g6=null;
	private GroundOverlay _g7=null;
	private GroundOverlay _g8=null;
	public GroundSODUKU(){
		
	}
	
	
	
	public void setGround(RelativeTileEnum re,GroundOverlay ground)
	{
		switch(re)
		{
		case CENTER:
			_g0 = ground;
			break;
		case BOTTOM:
			_g6 = ground;
			break;
		case LEFT:
			_g8 = ground;
			break;
		case LEFT_BOTTOM:
			_g7 = ground;
			break;
		case LEFT_TOP:
			_g1 = ground;
			break;
		case RIGHT:
			_g4 = ground;
			break;
		case RIGHT_BOTTOM:
			_g5 = ground;
			break;
		case RIGHT_TOP:
			_g3 = ground;
			break;
		case TOP:
			_g2 = ground;
			break;
		default:
				break;
		
		}
	
		
	
	}
	
	public void setGroundIsNull(int re)
	{
		switch(re)
		{
		case 0:
			_g0 = null;
			break;
		case 6:
			_g6 = null;
			break;
		case 8:
			_g8 = null;
			break;
		case 7:
			_g7 = null;
			break;
		case 1:
			_g1 = null;
			break;
		case 4:
			_g4 = null;
			break;
		case 5:
			_g5 = null;
			break;
		case 3:
			_g3 = null;
			break;
		case 2:
			_g2 = null;
			break;
		default:
				break;
		
		}
	
		
	
	}
	
	public GroundOverlay getg0() {
		return _g0;
	}
	public void setg0(GroundOverlay _g0) {
		this._g0 = _g0;
	}
	public GroundOverlay getg1() {
		return _g1;
	}
	public void setg1(GroundOverlay _g1) {
		this._g1 = _g1;
	}
	public GroundOverlay getg2() {
		return _g2;
	}
	public void setg2(GroundOverlay _g2) {
		this._g2 = _g2;
	}
	public GroundOverlay getg3() {
		return _g3;
	}
	public void setg3(GroundOverlay _g3) {
		this._g3 = _g3;
	}
	public GroundOverlay getg4() {
		return _g4;
	}
	public void setg4(GroundOverlay _g4) {
		this._g4 = _g4;
	}
	public GroundOverlay getg5() {
		return _g5;
	}
	public void setg5(GroundOverlay _g5) {
		this._g5 = _g5;
	}
	public GroundOverlay getg6() {
		return _g6;
	}
	public void setg6(GroundOverlay _g6) {
		this._g6 = _g6;
	}
	public GroundOverlay getg7() {
		return _g7;
	}
	public void setg7(GroundOverlay _g7) {
		this._g7 = _g7;
	}
	public GroundOverlay getg8() {
		return _g8;
	}
	public void setg8(GroundOverlay _g8) {
		this._g8 = _g8;
	}
}
