package util;

import java.io.File;
import java.io.PrintStream;
import java.io.*;

/*
 * Created on 2004. 11. 17.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author jaewoo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogFile {
	
	File dir, file;
	PrintWriter out;
	String folder = "";
	
	boolean error=false;
/*
	logFile(){;
		dir = new File("LogFolder");
		if(!dir.exists()){
			dir.mkdir();
			System.out.println("LogFolder Created!");
		}
	}
*/
	public LogFile(String folder, String filename){
		this.folder = folder;
	
		dir = new File(folder);
		if(!dir.exists()){
			dir.mkdir();
			System.out.println(folder + " is created!");
		}
		file = new File(folder+"/"+filename);
		try{
			if(!file.exists())
				file.createNewFile();
		}catch(Exception e){
			error=true;
		}
		try{
			out =new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			
		}catch(Exception e){
			error=true;
		}
	}
	public LogFile(String folder, String filename, boolean createFile){
		this.folder = folder;
		dir = new File(folder);
		if(!dir.exists()){
			dir.mkdir();
			System.out.println(folder + " is created!");
		}
		
		
		file = new File(folder+"/"+filename);
		
		
		
		try{

			if(createFile){
				file.delete();
				file.createNewFile();
			}
			if(!file.exists())
				file.createNewFile();
			
		}catch(Exception e){
			error=true;
		}
		try{
			out =new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			
		}catch(Exception e){
			error=true;
		}
	}
	
	
	public void writeStringLine(String line)throws IOException{
		out.println(line);
		out.flush();
	}
	public void close()throws IOException{
		out.close();
	}
	public static boolean isExist(String s){
		
		File myFile = new File(s);
		if(myFile.exists()) return true;
		else return false;
	}
	public void changefilename(String newfile){
		file = new File(folder+"/"+newfile);
		try{
			if(!file.exists())
				file.createNewFile();
			out =new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		}catch(Exception e){
			error=true;
		}
	}
	
}
