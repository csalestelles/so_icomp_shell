package ufam.so.shell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Diretorio {
	
	public static final int ERROR = 1;
	public static final int WORKING = 0;
	
	private static final String dirInitial = System.getProperty("user.home") + "/";
	private static final String user = System.getProperty("user.name");
	private static final String osName = System.getProperty("os.name");
	private static String dynamicDir = dirInitial;
	private String actualDir;

	Date date;
	private DateFormat formater = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm':'ss'h'");
	
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	
	public String getActualDir() {
		String[] splitedDir = dynamicDir.split("/");
		int lastDir = splitedDir.length;
		actualDir = splitedDir[lastDir-1];
		
		return actualDir;
	}
	public void setActualDir(String actualDir) {
		this.actualDir = actualDir;
	}
	
	public static String getDynamicDir() {
		return dynamicDir;
	}
	public static void setDynamicDir(String dynamicDir) {
		Diretorio.dynamicDir = dynamicDir;
	}
	
	public static String getDirInicial() {
		return dirInitial;
	}
	public static String getUser() {
		return user;
	}
	public static String getOsname() {
		String osNameReduced = osName.replaceAll(" ", "");
		return osNameReduced;
	}

	public String setTerminalInfo()
	{
		return getOsname() + ":" + getActualDir() + " " + getUser() + "$";
	}
	
	public void createLog()
	{
		File directory = new File(dirInitial + "Log");
		date = Calendar.getInstance().getTime(); 
    	String today = formater.format(date);
        if (directory.mkdir())
        {
        	File file = new File(directory, "logTerminalSimulator.txt");
        	try 
        	{
				file.createNewFile();
				fileWriter = new FileWriter(file, true);
				
			} 
        	catch (IOException e) {	e.getMessage();}
        	printWriter = new PrintWriter(fileWriter);
        	printWriter.println("Log Criado em " + today + "Por: " + user + "\n\n\n");
        	printWriter.flush();
        	printWriter.close();
        }
	}
	
	public void writeLog(String registry, int error)   //Erro = 1
	{
		File directory = new File(dirInitial + "Log");
		date = Calendar.getInstance().getTime(); 
    	String today = formater.format(date);
        if (directory.exists())
        {
        	File file = new File(directory, "logTerminalSimulator.txt");
        	try 
        	{
				fileWriter = new FileWriter(file, true);
				
			} 
        	catch (IOException e) {	e.getMessage();}
        	printWriter = new PrintWriter(fileWriter);
        	if (error == 1)
        		printWriter.println(registry + " " + today + "Por: " + user + "\nComando acima apresentou erro\n\n");
        	else
        		printWriter.println(registry + " " + today + " Por: " + user + "\n\n");
        	printWriter.flush();
        	printWriter.close();
        }
	}
	
	public String backToFatherDir(String directory)
	{
		String[] splitedDir = directory.split("/");
		int lastDir = splitedDir.length;
		dynamicDir = directory.replaceAll(splitedDir[lastDir-1]+"/", "");
		
		return dynamicDir;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getDirInicial());
	}

}
