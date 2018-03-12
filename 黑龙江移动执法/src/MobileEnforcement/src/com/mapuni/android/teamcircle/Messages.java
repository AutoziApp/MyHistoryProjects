package com.mapuni.android.teamcircle;


import java.util.ArrayList;
import java.util.List;

public class Messages {
  
	public String name;
	public String body;
	public long time;
	public int type;
	
	public List<String> zList=new ArrayList<String>();
	public List<String> people=new ArrayList<String>();
	public List<String> content=new ArrayList<String>();
	public List<String> urls=new ArrayList<String>();
	@Override
	public String toString() {
		return "Messages [name=" + name + ", body=" + body + ", time=" + time
				+ ", zList=" + zList + ", people=" + people + ", content="
				+ content + "]";
	}

}
