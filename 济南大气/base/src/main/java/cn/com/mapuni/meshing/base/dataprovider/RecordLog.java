package cn.com.mapuni.meshing.base.dataprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.mapuni.meshing.base.interfaces.PathManager;

/**
 * 将已捕获异常写入Log
 * @author programed by dy
 *
 */
public class RecordLog {
	synchronized public static void WriteCaughtEXP(Throwable e, String ClassName){
		try{
		File file=new File(PathManager.SDCARD_LOG_LOCAL_PATH);
		if(!file.exists()){
			file.mkdirs();
		}
		File f = new File(PathManager.SDCARD_LOG_LOCAL_PATH + "/log.txt");
		FileWriter fw=new FileWriter(f, true);
	
		if(!f.exists())
			f.createNewFile();

		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-hh-mm-ss");
		fw.write(dateFormat.format(now)+"     ClassName:"+ClassName+"\n");
		fw.write(e.getClass().getName()+"\n");

			for(StackTraceElement ste:e.getStackTrace()){
				String line=ste.toString();
				fw.write("at ");
				fw.write(line);
				fw.write("\n");
			}
			fw.write("\n");
			fw.flush();
			fw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e.printStackTrace();
		}
	}

	
	
	
	
}
