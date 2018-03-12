package com.jy.environment.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.util.Log;



/**
 * 保存的是传递给子线程的分析数据
 *
 */
public class InterPolation {
		//颜色带	RGBA
		private final int[] _colorRamp={0,228,0,0,0,228,0,50,0,228,0,50,255,255,0,150,255,165,0,150,255,0,0,180,128,0,128,180,126,0,35,180,126,0,35,180};
		
		//环境指数权重值
//		private final int[] _IAQIWeight={0,5,50,100,150,200,300,400,500};
//		private final int[] _SO2Weight={0,5,150,500,650,800,1600,2100,2620};
//		private final int[] _NO2Weight={0,5,100,200,700,1200,2340,3090,3840};
//		private final int[] _COWeight={0,2,5,10,35,60,90,120,150};
//		private final int[] _O3Weight={0,5,160,200,300,400,800,1000,1200};
//		private final int[] _PM10Weight={0,5,50,150,250,350,420,500,600};
//		private final int[] _PM25Weight={0,5,35,75,115,150,250,350,500};
		
		private final int[] _IAQIWeight={0,5,25,75,125,175,250,350,450};
		private final int[] _SO2Weight={0,5,75,325,575,725,1200,1850,2620};
		private final int[] _NO2Weight={0,5,50,150,450,950,1770,2735,3840};
		private final int[] _COWeight={0,2,4,8,22,47,75,105,150};
		private final int[] _O3Weight={0,5,80,180,250,350,600,900,1200};
		private final int[] _PM10Weight={0,5,25,100,200,300,385,460,600};
		private final int[] _PM25Weight={0,5,17,55,95,132,200,300,500};
		
		//定义边界距离
		private final double _maxDIS=50000000.0;
		//距离扩散权值
		private final double _disWeight=500000;
		private final double _disWeight1=3000000;
		//角点坐标 左上和右下
		private List<Point> _points=null;
		
		
		//内插像素宽度和高度，保证长宽比和
		private int _width=0;
		private int _height=0;
		
		//返回的内插图片
		private Bitmap _interpolationimg=null;
		
		//权值数组 
		private double[][] _weight;
		
		//内插类型    AQI pm2.5等
		private PollutantTypeEnum _type;
		
		//基站点数组
		private List<PollutantStation> _lstPollutantStation;
		//重分类后的权重值集合
		private double[] _reclassWeight;
		
		
		
		public InterPolation(int width,int height,List<Point> points,PollutantTypeEnum type,List<PollutantStation> lstPollutantstation){
			_width=width;
			_height=height;
			_points=points;
			_type=type;
			_lstPollutantStation=lstPollutantstation;
			_weight=new double[width][height];
			_reclassWeight=new double[lstPollutantstation.size()];
			_interpolationimg=Bitmap.createBitmap(_width, _height, Config.ARGB_8888);
		}
		
		//根据像素点获取经纬度坐标
		private Point GetPointByPixel(int x,int y){
			if(_points.size()!=2){
				return null;
			}
			double x1=_points.get(0).x+ (_points.get(1).x-_points.get(0).x)*x/this._width;
			double y1=_points.get(0).y+(_points.get(1).y-_points.get(0).y)*y/this._height;
			return new Point(x1,y1);
		}
		
		
		//过滤点的最近距离的四个点，并且按照Y值进行排序  占时无用
		private List<PolationPoint> GetPointsByDis(Point p){
			List<InterPoint> lst=new ArrayList<InterPoint>();
			for(int i=0;i<_lstPollutantStation.size();i++){
				InterPoint pp=new InterPoint(_lstPollutantStation.get(i).get_point(), p, _reclassWeight[i]);
				lst.add(pp);
			}
			ComparatorInterPoint comparator=new ComparatorInterPoint();
			Collections.sort(lst,comparator);
			if(lst.size()<4 ){
				return null;
			}
			if(lst.get(3).distance()>_maxDIS){
				return null;
			}
			List<PolationPoint> lstpoint=new ArrayList<PolationPoint>();
			for(int i=0;i<4;i++){
				PolationPoint p1=new PolationPoint(lst.get(i).get_point(),lst.get(i).get_weight());
				lstpoint.add(p1);
			}
//			SortPoint(lstpoint);
			return lstpoint;
		}
		
		
		//将污染物浓度转换为对应的污染权重值 权重值得重分类
 		private void PollutantToWeight(){
			switch(_type){
				case AQI:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("AQI");
						if(w<=_IAQIWeight[_IAQIWeight.length-1]){
							_reclassWeight[i]=w;
							
						}
						
						else{
							_reclassWeight[i]=_IAQIWeight[_IAQIWeight.length-1];
						}
					}
					break;
				case CO:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("CO");
						//寻找权重值所处区间
						int d=_COWeight.length;
						for(int j=0;j<_COWeight.length;j++){
							if(w>=_COWeight[j] && w<=_COWeight[j+1]){
								d=j;
								break;
							}
						}
						//权重值的重分类  超出范围为最大值
						if(d!=_COWeight.length){
							_reclassWeight[i]=_IAQIWeight[d]+(w-_COWeight[d])/(_COWeight[d+1]-_COWeight[d])*(_IAQIWeight[d+1]-_IAQIWeight[d]);
						}
						else{
							_reclassWeight[i]=_IAQIWeight[d-1];
						}
					}
					break;
				case NO2:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("NO2");
						//寻找权重值所处区间
						int d=_NO2Weight.length;
						for(int j=0;j<_NO2Weight.length;j++){
							if(w>=_NO2Weight[j] && w<=_NO2Weight[j+1]){
								d=j;
								break;
							}
						}
						//权重值的重分类  超出范围为最大值
						if(d!=_NO2Weight.length){
							_reclassWeight[i]=_IAQIWeight[d]+(w-_NO2Weight[d])/(_NO2Weight[d+1]-_NO2Weight[d])*(_IAQIWeight[d+1]-_IAQIWeight[d]);
						}
						else{
							_reclassWeight[i]=_IAQIWeight[d-1];
						}
					}
					break;
				case O3:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("O3");
						//寻找权重值所处区间
						int d=_O3Weight.length;
						for(int j=0;j<_O3Weight.length;j++){
							if(w>=_O3Weight[j] && w<=_O3Weight[j+1]){
								d=j;
								break;
							}
						}
						//权重值的重分类  超出范围为最大值
						if(d!=_O3Weight.length){
							_reclassWeight[i]=_IAQIWeight[d]+(w-_O3Weight[d])/(_O3Weight[d+1]-_O3Weight[d])*(_IAQIWeight[d+1]-_IAQIWeight[d]);
						}
						else{
							_reclassWeight[i]=_IAQIWeight[d-1];
						}
					}
					break;
				case PM10:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("PM10");
						//寻找权重值所处区间
						int d=_PM10Weight.length;
						for(int j=0;j<_PM10Weight.length;j++){
							if(w>=_PM10Weight[j] && w<=_PM10Weight[j+1]){
								d=j;
								break;
							}
						}
						//权重值的重分类  超出范围为最大值
						if(d!=_PM10Weight.length){
							_reclassWeight[i]=_IAQIWeight[d]+(w-_PM10Weight[d])/(_PM10Weight[d+1]-_PM10Weight[d])*(_IAQIWeight[d+1]-_IAQIWeight[d]);
						}
						else{
							_reclassWeight[i]=_IAQIWeight[d-1];
						}
					}
					break;
				case pm25:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("pm25");
						//寻找权重值所处区间
						int d=_PM25Weight.length;
						for(int j=0;j<_PM25Weight.length;j++){
							if(w>=_PM25Weight[j] && w<=_PM25Weight[j+1]){
								d=j;
								break;
							}
						}
						//权重值的重分类  超出范围为最大值
						if(d!=_PM25Weight.length){
							_reclassWeight[i]=_IAQIWeight[d]+(w-_PM25Weight[d])/(_PM25Weight[d+1]-_PM25Weight[d])*(_IAQIWeight[d+1]-_IAQIWeight[d]);
						}
						else{
							_reclassWeight[i]=_IAQIWeight[d-1];
						}
					}
					break;
				case SO2:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("SO2");
						//寻找权重值所处区间
						int d=_SO2Weight.length;
						for(int j=0;j<_SO2Weight.length;j++){
							if(w>=_SO2Weight[j] && w<=_SO2Weight[j+1]){
								d=j;
								break;
							}
						}
						//权重值的重分类  超出范围为最大值
						if(d!=_SO2Weight.length){
							_reclassWeight[i]=_IAQIWeight[d]+(w-_SO2Weight[d])/(_SO2Weight[d+1]-_SO2Weight[d])*(_IAQIWeight[d+1]-_IAQIWeight[d]);
						}
						else{
							_reclassWeight[i]=_IAQIWeight[d-1];
						}
					}
					break;
				default:
					for(int i=0;i<_lstPollutantStation.size();i++){
						double w=_lstPollutantStation.get(i).get_pollutantValue().get("AQI");
						if(w<=_IAQIWeight[_IAQIWeight.length-1]){
							_reclassWeight[i]=w;
						}
						else{
							_reclassWeight[i]=_IAQIWeight[_IAQIWeight.length-1];
						}
					}
					break;
			}
		}
		
		//计算权值
		public void CumputerWeight(){
			for(int i=0;i<this._height;i++){
				for(int j=0;j<this._width;j++){
					Point p=GetPointByPixel(j,i);
					//获得这点的已知点
					List<PolationPoint> lst=GetPointsByDis(p);
					
					//插值已知点数目不足权重值为0
					if(lst==null || lst.size()<4){
						_weight[j][i]=0;
						continue;
					}
					//根据已知点计算当前点的权重值
					double w1=lst.get(0).get_weight();
					double w2=lst.get(1).get_weight();
					double w3=lst.get(2).get_weight();
					double w4=lst.get(3).get_weight();
					Point p1=lst.get(0).get_point();
					Point p2=lst.get(1).get_point();
					Point p3=lst.get(2).get_point();
					Point p4=lst.get(3).get_point();
					double xr1=Math.abs(p2.x-p.x)/Math.abs((p2.x-p1.x))*w1*Math.abs((p1.x-p2.x))/(Math.abs(p.x-p2.x)+Math.abs(p.x-p1.x));
					double xr2=Math.abs(p1.x-p.x)/Math.abs((p2.x-p1.x))*w2*Math.abs((p1.x-p2.x))/(Math.abs(p.x-p2.x)+Math.abs(p.x-p1.x));
			
					double xr3=Math.abs(p4.x-p.x)/Math.abs((p4.x-p3.x))*w3*Math.abs((p3.x-p4.x))/(Math.abs(p.x-p4.x)+Math.abs(p.x-p3.x));
					double xr4=Math.abs(p3.x-p.x)/Math.abs((p4.x-p3.x))*w4*Math.abs((p3.x-p4.x))/(Math.abs(p.x-p4.x)+Math.abs(p.x-p3.x));
					double r1=xr1+xr2;
					double r2=xr3+xr4;
					
					double y1=(p1.y+p2.y)/2;
					double y2=(p3.y+p4.y)/2;
					
					double yr1=Math.abs(y2-p.y)/Math.abs((y2-y1))*r1*Math.abs((y1-y2))/(Math.abs(p.y-y2)+Math.abs(p.y-y1));
					double yr2=Math.abs(y2-p.y)/Math.abs((y2-y1))*r2*Math.abs((y1-y2))/(Math.abs(p.y-y2)+Math.abs(p.y-y1));
					_weight[j][i]= yr1+yr2;
					
					
				}
			}
		}
		
		//根据权值获取颜色值
		private int Get_PixcelColor(double weight){
			if(weight<=0 || weight==Double.NaN){
				return Color.argb(10,0, 228, 0);
			}
			int a=10;
			int r=0;
			int g=0;
			int b=0;
			//表示权重值得颜色范围
			int f=0;
			double scale=0;
			
			for(int i=0;i<_IAQIWeight.length;i++){
				if(weight>=_IAQIWeight[i] && weight<=_IAQIWeight[i+1]){
					f=i;
					scale=(double)(weight-_IAQIWeight[i])/(_IAQIWeight[i+1]-_IAQIWeight[i]);
					break;
				}
				if(weight>=_IAQIWeight[_IAQIWeight.length-1]){
					f=_colorRamp.length/4;
					
					return Color.argb(_colorRamp[f*4-1], _colorRamp[f*4-4], _colorRamp[f*4-3], _colorRamp[f*4-2]);
				}
			}
			r=(int)(Math.abs(_colorRamp[f*4]+(_colorRamp[(f+1)*4]-_colorRamp[f*4])*scale));
			g=(int)(Math.abs(_colorRamp[f*4+1]+(_colorRamp[(f+1)*4+1]-_colorRamp[f*4+1])*scale));
			b=(int)(Math.abs(_colorRamp[f*4+2]+(_colorRamp[(f+1)*4+2]-_colorRamp[f*4+2])*scale));
			a=(int)(Math.abs(_colorRamp[f*4+3]+(_colorRamp[(f+1)*4+3]-_colorRamp[f*4+3])*scale));
			return Color.argb(a,r,g,b);
		}
		
		//根据权值获取颜色值
				private int Get_PixcelColor(double weight,int al){
					if(weight<=0 || weight==Double.NaN){
						return Color.argb(10,0, 228, 0);
					}
					int a=10;
					int r=0;
					int g=0;
					int b=0;
					//表示权重值得颜色范围
					int f=0;
					double scale=0;
					
					for(int i=0;i<_IAQIWeight.length;i++){
						if(weight>=_IAQIWeight[i] && weight<=_IAQIWeight[i+1]){
							f=i;
							scale=(double)(weight-_IAQIWeight[i])/(_IAQIWeight[i+1]-_IAQIWeight[i]);
							break;
						}
						if(weight>=_IAQIWeight[_IAQIWeight.length-1]){
							f=_colorRamp.length/4;
							
							return Color.argb(_colorRamp[f*4-1], _colorRamp[f*4-4], _colorRamp[f*4-3], _colorRamp[f*4-2]);
						}
					}
					r=(int)(Math.abs(_colorRamp[f*4]+(_colorRamp[(f+1)*4]-_colorRamp[f*4])*scale));
					g=(int)(Math.abs(_colorRamp[f*4+1]+(_colorRamp[(f+1)*4+1]-_colorRamp[f*4+1])*scale));
					b=(int)(Math.abs(_colorRamp[f*4+2]+(_colorRamp[(f+1)*4+2]-_colorRamp[f*4+2])*scale));
					a=al;
					return Color.argb(a,r,g,b);
				}
		
		
		//根据权重值和颜色带内插图片颜色
		private void GetColors(){
			for(int i=0;i<_height;i++){
				for(int j=0;j<_width;j++){
					if(_weight[i][j]<0.1 || _weight[i][j]==Double.NaN){
						_interpolationimg.setPixel(j, i, Color.argb(10, 0, 228, 0));
					}
					else{
//						if(i==0 || i==_height-1 || j==0 || j==_width-1){
//							_interpolationimg.setPixel(j, i, Get_PixcelColor(_weight[i][j],0));
//						}else{
//							_interpolationimg.setPixel(j, i, Get_PixcelColor(_weight[i][j]));
//						}
						_interpolationimg.setPixel(j, i, Get_PixcelColor(_weight[i][j]));
					}
				}
			}
		}
		
		//两点之间的距离公式
		public double distance(Point _point,Point _point1){
			return Math.sqrt((_point.x-_point1.x)*1000000*1000000*(_point.x-_point1.x)+(_point.y-_point1.y)*1000000*1000000*(_point.y-_point1.y));
		}
		
		//计算反距离权重内插
		private void CumputerDisWeight(){
			for(int i=0;i<this._height;i++){
				for(int j=0;j<this._width;j++){
					Point p=GetPointByPixel(j,i);
					//Log.v("interp", p.x+","+p.y);
					double  r1=0.0;
					double r2=0.0;
					for(int z=0;z<_lstPollutantStation.size();z++){
						Point p1=_lstPollutantStation.get(z).get_point();
						double dis=distance(p,p1);
						if(dis>_maxDIS || _reclassWeight[z]==0){
							continue;
						}
						double w1=0.0;
						w1=_reclassWeight[z]-_reclassWeight[z]*((dis/_disWeight)*(dis/_disWeight)*(dis/_disWeight)*0.5);

						
						r1=r1+w1/dis/dis/dis;
						r2=r2+1/dis/dis/dis;
						//Log.v("interp:w1,dis,r1,r2",w1+","+dis+","+r1+","+r2 );
					}
					if(r2!=0){
						_weight[i][j]=r1/r2;
					}
					else{
						_weight[i][j]=0;
					}
				}
			}
		}
		
		private void doublelinedisWeight(){
			for(int i=0;i<this._height;i++){
				for(int j=0;j<this._width;j++){
					Point p=GetPointByPixel(j,i);
					double  r1=0.0;
					double r2=0.0;
					double r3=0.0;
					double r4=0.0;
					double r5=0.0;
					double r6=0.0;
					for(int z=0;z<_lstPollutantStation.size();z++){
						Point p1=_lstPollutantStation.get(z).get_point();
						double disx=Math.abs(p1.x-p.x);
						double disy=Math.abs(p1.y-p.y);
						double rx=_reclassWeight[z]-_reclassWeight[z]*disx/2;
						double ry=_reclassWeight[z]-_reclassWeight[z]*disy/2;
						double dis=distance(p,p1);
						double w1=_reclassWeight[z]-_reclassWeight[z]*dis/2000000;
						r5=r5+w1/dis/dis/dis;
						r6=r6+1/dis/dis/dis;
						r1=r1+rx/disx/disx/disx;
						r2=r2+1/disx/disx/disx;
						r3=r3+ry/disy/disy/disy;
						r4=r4+1/disy/disy/disy;
					}

					_weight[i][j]=(r1/r2+r3/r4+r5/r6+r5/r6+r5/r6)/5;
//					_weight[i][j]=(r1/r2+r3/r4)/2;

				}
			}
		}
		
		//返回插值后图片
		public Bitmap Get_Bitmap(){
			PollutantToWeight();
			CumputerDisWeight();
			GetColors();
			return _interpolationimg;
		}
		
		
}
