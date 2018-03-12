package com.jy.environment.map;



/**
 * @author SHANG
 *	对所要渲染的瓦片区域进行描述
 *	以当前的瓦片显示区域为中心。
 *	以正北方向为TOP
 */
public enum RelativeTileEnum {
	CENTER,
	TOP,
	BOTTOM,
	RIGHT,
	LEFT,
	LEFT_TOP,
	LEFT_BOTTOM,
	RIGHT_TOP,
	RIGHT_BOTTOM;
}
