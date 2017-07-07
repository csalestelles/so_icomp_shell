package ufam.so.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class PastComands 
{
	
	private static final String INITIAL_DIR = System.getProperty("user.home") + "/";
	private static final String USER = System.getProperty("user.name");
	private static String dynamicDir = INITIAL_DIR;
	private String actualDir;
	
	private static final File directory =  new File(INITIAL_DIR, "Log");

	Date date;
	private DateFormat formater = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm':'ss'h'");
	
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	
	public PastComands()
	{
		date = Calendar.getInstance().getTime(); 
    	String today = formater.format(date);
        if (directory.exists())
        {
        	File file = new File(directory, "ComandsRegistry.txt");
        	
        	try 
        	{
        		if (file.exists()){}
        		else
        		{
        			file.createNewFile();
    				fileWriter = new FileWriter(file, true);
    				printWriter = new PrintWriter(fileWriter);
    	        	printWriter.println("Registro Criado em " + today + " Por: " + USER + "\n\n");
    	        	printWriter.flush();
    	        	printWriter.close();
        		}
			} 
        	catch (IOException e) {	e.getMessage();}
        }
	}
	
	public void writeRegistry(String registry)   //Erro = 1
	{
		if (directory.exists())
        {
        	File file = new File(directory, "ComandsRegistry.txt");
        	try 
        	{
				fileWriter = new FileWriter(file, true);
				
			} 
        	catch (IOException e) {	e.getMessage();}
        	printWriter = new PrintWriter(fileWriter);
        	printWriter.println(registry);
        	printWriter.flush();
        	printWriter.close();
        }
	}
	
	public int numberOfRows() 
	{
		int numberOfRows = 0;
		if (directory.exists())
		{
			File file = new File(directory, "ComandsRegistry.txt");
			if (file.exists())
			{
				try
				{
					LineNumberReader linhaLeitura = new LineNumberReader(new FileReader(file));
					linhaLeitura.skip(file.length());
					numberOfRows = linhaLeitura.getLineNumber();
				}
				catch(IOException erro){erro.printStackTrace();}
			}
		}
		return numberOfRows;
	}
	
	public String loadComand(int row)
	{
		int numberOfRows = 0;
		String rowString = "";
		if (directory.exists())
		{
			File file = new File(directory, "ComandsRegistry.txt");
			if(file.exists())
			{
				Scanner read = new Scanner(System.in);
//				String nome = read.nextLine();
				try 
				{
					FileReader fileReader = new FileReader(file);
				    BufferedReader reader = new BufferedReader(fileReader);
				    while (numberOfRows < row) 
				    {
				    	rowString = reader.readLine();
				    	numberOfRows++;
				    }
				    
				    fileReader.close();
				} 
				catch (IOException e)
				{
			    	System.err.printf("Erro na abertura do arquivo: %s.\n",
			        e.getMessage());
			    }
				read.close();
			}
		}
		return rowString;
	}
	

}
