package ufam.so.shell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Diretorio {
	
	public static final int ERROR = 1;  //INTEIROS PARA REGISTRO NO LOG
	public static final int WORKING = 0;
	
	private static final String dirInitial = System.getProperty("user.home") + "/";  //ACESSA O DIRETORIO RAIZ
	private static final String user = System.getProperty("user.name");  //ACESSA O USUARIO QUE ESTÁ UTILIZANDO O SISTEMA
	private static final String osName = System.getProperty("os.name");  //ACESSA O NOME DO SO
	private static final File directory =  new File(dirInitial, "Log");  //ACESSA O DIRETORIO ONDE ESTÁ O LOG

	private static String dynamicDir = dirInitial;  //DIRETORIO QUE VARIA DE ACORDO COM A UTILIZACAO DO USUARIO
	private String actualDir;

	Date date;
	private DateFormat formater = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm':'ss'h'");
	
	private FileWriter fileWriter;   //ESCRITOR DO ARQUIVO
	private PrintWriter printWriter;  //LEITOR DO ARQUIVO
	
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
	
	//CRIA OR ARQUIVO DE LOG
	public void createLog()
	{
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
	
	
	//ESCREVE NO ARQUIVO DE LOG
	public void writeLog(String registry, int error)   //Erro = 1
	{
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
	
	//RETORNA PARA O DIRETORIO PAI
	public String backToFatherDir(String directory)
	{
		String[] splitedDir = directory.split("/");
		int lastDir = splitedDir.length;
		dynamicDir = directory.replaceAll(splitedDir[lastDir-1]+"/", "");
		
		return dynamicDir;
	}
	
	
	//MOVE UM ARQUIVO OU DIRETORIO
	public boolean move(String comand) throws IOException
	{
		String[] splitedComand = comand.split(" ");
		
		
		String[] splitedOriginPath = splitedComand[1].split("/");
		String firstDir = null;
		
		if(splitedOriginPath.length > 1)
			firstDir = "/" + splitedOriginPath[1] + "/" + splitedOriginPath[2] + "/";
		else
			firstDir = splitedOriginPath[0] + "/"; //<-
		
		
		String[] splitedDestinyPath = splitedComand[2].split("/");
		String lastDir = null;
		
		if(splitedDestinyPath.length > 1)
			lastDir = "/" + splitedDestinyPath[1] + "/" + splitedDestinyPath[2] + "/";  //<- faltou "/" no inicio
		else
			lastDir = splitedDestinyPath[0] + "/";
			
		File destinyPath = null;
		File file = null;
		
		if (lastDir.equals(dirInitial))
			destinyPath = new File(splitedComand[2]);
		else
			destinyPath = new File(getDynamicDir() + splitedComand[2] + "/");
		
		if (firstDir.equals(dirInitial))
			file = new File(splitedComand[1]);
		else
			file = new File(getDynamicDir() + splitedComand[1]);
		
		if ((file.isFile() || file.isDirectory()) && destinyPath.isDirectory())
		{
			String name = file.getName();
			Files.move(file.toPath(), Paths.get(destinyPath.toString(), name), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			return true;
		}
		else
			return false;
	}
	
	//ABRE UM ARQUIVO (EX.: .txt , .pdf)
	public File openFile(String command)
	{
		int contador = 0;
		String[] splitedCommand = command.split(" ");
		
		String[] pathFile = splitedCommand[1].split("/");
		String dir;
		
		if(pathFile.length > 1)
			dir = "/" + pathFile[1] + "/" + pathFile[2] + "/";
		else
			dir = pathFile[0] + "/";
		
		String destinyPath = "";
		File file = null;
		String fileName = pathFile[pathFile.length-1];
		pathFile[pathFile.length-1] = null;
		String path = "";
		
		while(contador < (pathFile.length-1))
		{
			if(contador == 0)
			{contador++;}
			else
			{
				path += "/" + pathFile[contador];
				contador++;
			}	
		}
		path += "/";
		
		file = new File(path, fileName);
		
		if(dir.equals(dirInitial))
			destinyPath = path;
		else
			destinyPath = getDynamicDir() + path + "/";
		
		file = new File(destinyPath, fileName);
		
		return file;
			
	}

}
