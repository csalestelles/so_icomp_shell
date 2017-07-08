package ufam.so.shell;

import java.io.BufferedReader;
import java.io.File;
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

//CLASSE QUE TRATA DA NAVEGACAO PELOS COMANDOS JA UTILIZADOS (SETAS PARA CIMA E PARA BAIXO)
public class PastComands 
{
	
	private static final String INITIAL_DIR = System.getProperty("user.home") + "/";
	private static final String USER = System.getProperty("user.name");
	
	private static final File directory =  new File(INITIAL_DIR, "Log");

	Date date;
	private DateFormat formater = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm':'ss'h'");
	
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	
	//METODO CONSTRUTOR CRIAR O ARQUIVO DE REGISTRO DOS COMANDOS
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
	
	//ESCREVE O COMANDO NO REGISTRO
	public void writeRegistry(String registry)  
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
	
	//CONTA O NUMERO DE LINHAS (COMANDOS) NO REGISTRO
	public int numberOfRows() 
	{
		int numberOfRows = 0;
		LineNumberReader linhaLeitura;
		if (directory.exists())
		{
			File file = new File(directory, "ComandsRegistry.txt");
			if (file.exists())
			{
				try
				{
					linhaLeitura = new LineNumberReader(new FileReader(file));
					linhaLeitura.skip(file.length());
					numberOfRows = linhaLeitura.getLineNumber();
					linhaLeitura.close();
				}
				catch(IOException erro){erro.printStackTrace();}
			}
		}
		
		return numberOfRows;
	}
	
	//CARREGA UM COMANDO A PARTIR DO NUMERO DA LINHA QUE É PASSADO COMO PARÂMETRO (ROW)
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
