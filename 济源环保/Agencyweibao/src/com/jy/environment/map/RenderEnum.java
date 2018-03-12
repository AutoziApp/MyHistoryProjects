package com.jy.environment.map;

/**
 * @author shang
 * 标识在地图上渲染的类型，目前就一类：
 * AQI
 */
public enum RenderEnum  {
 /**
 * 标识地图上什么渲染也没显示
 */
NULL,
/**
 * 标识显示AQI的实时渲染图（包含具体气体的）
 */
AQI;
}
