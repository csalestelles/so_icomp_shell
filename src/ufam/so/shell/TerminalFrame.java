package ufam.so.shell;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import java.io.InputStream;

import javax.swing.JTextPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Point;
import java.awt.ScrollPane;

public class TerminalFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private StyledDocument doc;
    String log, today;
    private JTextField textField;
    File raiz;
    private PastComands acess;
    
    private ScrollPane terminalScroll;
    int POSICAO_SCROLL_X = 0;
    int POSICAO_SCROLL_Y = 0;
    private JScrollBar verticalBar, horizontalBar;
    
    int rowToAcess;
    String saveState;
    
    Diretorio directory;
    
    String directoryString = Diretorio.getDirInicial();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TerminalFrame terminal = new TerminalFrame();
                    terminal.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TerminalFrame() throws IOException {
    	directory = new Diretorio();
    	setForeground(Color.WHITE);
		setTitle("JavaTerminal");
		setBackground(Color.DARK_GRAY);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		
		getContentPane().add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextPane textArea = new JTextPane();
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		scrollPane.setViewportView(textArea);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		horizontalBar = scrollPane.getHorizontalScrollBar();
        verticalBar = scrollPane.getVerticalScrollBar();
		
        directory.createLog();
        acess = new PastComands();
        
        JLabel lblNewLabel = new JLabel();
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBackground(Color.WHITE);
		scrollPane.setColumnHeaderView(lblNewLabel);
        lblNewLabel.setText(Diretorio.getDirInicial());
        
        doc = textArea.getStyledDocument();
        print("");
        
        textField.addKeyListener(new KeyAdapter()
        {
        	@Override
        	public void keyPressed(KeyEvent e)
        	{
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        		{
        			String command = textField.getText();
        			String[] initialCommand = command.split(" ");
        			if (initialCommand[0].equals("ls") && initialCommand.length <= 2)     //listar arquivos e diretorios
        			{
        				StringBuilder sb = new StringBuilder();
        				if(initialCommand.length == 1)
        				{
        					raiz = new File(Diretorio.getDynamicDir());
        					for(File f: raiz.listFiles()) {
        						String dot = f.getName().substring(0, 1);
                				if((f.isFile() || f.isDirectory()) && !dot.equals(".")) 
                				{
                					sb.append(" -" + f.getName());
                					sb.append("\n");
                				}
                			}
        					directory.writeLog(command, Diretorio.WORKING);
                			print("\n" + sb.toString());
        				}
        				else
        				{
        					raiz = new File(Diretorio.getDynamicDir() + initialCommand[1]);
                			if (raiz.exists() && raiz.isDirectory())
                			{
                				for(File f: raiz.listFiles()) {
                					String dot = f.getName().substring(0, 1);
                    				if((f.isFile() || f.isDirectory()) && !dot.equals(".")) 
                    				{
                    					sb.append(" -" + f.getName());
                    					sb.append("\n");
                    				}
                    			}
                				directory.writeLog(command, Diretorio.WORKING);
                    			print(initialCommand[1] + "\n" + sb.toString());
                			}
                			else
                			{
                				directory.writeLog(command, Diretorio.ERROR);
                				print(command + " -- erro ao acessar Diretório");
                				
                			}
        				}
        			}
        			else if (initialCommand[0].equals("cd") && initialCommand.length <= 2)      //nav -> NAVegar no Diretório
        			{
        				if (initialCommand[1].equals(".."))
        				{
        					lblNewLabel.setText(directory.backToFatherDir(Diretorio.getDynamicDir()));
        					Diretorio.setDynamicDir(lblNewLabel.getText());
        					print("");
        					directory.writeLog(command, Diretorio.WORKING);
        				}
        				else if (initialCommand[1].equals("~"))
        				{
        					directory.writeLog(command, Diretorio.WORKING);
        					Diretorio.setDynamicDir(Diretorio.getDirInicial());
        					print("");
        					lblNewLabel.setText(Diretorio.getDynamicDir());
        				}
        				else 
        				{
        					File dir = new File(Diretorio.getDynamicDir() + initialCommand[1] + "/");
        					if(dir.exists() && dir.isDirectory())
        					{
        						Diretorio.setDynamicDir(Diretorio.getDynamicDir() + initialCommand[1]);
        						lblNewLabel.setText(Diretorio.getDynamicDir());
        						directory.writeLog(command, Diretorio.WORKING);
        						print("");
        					}
        					else
        					{
        						directory.writeLog(command, Diretorio.ERROR);
        						print(command + " -- erro ao acessar Diretório");
        					}
        				}
        					
        			}
        			else if(initialCommand[0].equals("pwd"))
        			{
        				if(initialCommand.length == 1)
        				{
        					directory.writeLog(command, Diretorio.WORKING);
        					print(Diretorio.getDynamicDir());
        				}
        				else
        				{
        					directory.writeLog(command, Diretorio.ERROR);
        					print(command + " -- erro ao acessar Diretório atual");
        				}
        			}
        			else if(initialCommand[0].equals("clear") && initialCommand.length == 1)
        			{
        				int counterClear = 0;
        				while(counterClear < 33)
        				{
        					printClear();
        					counterClear++;
        				}
        				print("");
        			}
        			else if(initialCommand[0].equals("mv") && initialCommand.length == 3)
        			{
        				try 
        				{
							if(directory.move(command))
							{
								directory.writeLog(command, Diretorio.WORKING);
								print("");
							}
							else
							{
								directory.writeLog(command, Diretorio.ERROR);
								print(command + " -- erro ao acessar informações");
							}
						} 
        				catch (IOException e1) {e1.printStackTrace();}
        			}
        			
        			
        			
        			/*
        			 * A PARTIR DESSE BLOCO ELSE TRATA A EXECUCAO DOS OUTROS COMANDOS DO TERMINAL E EXECUÇAO DE PROGRAMAS
        			 * 
        			 */
        			else if (initialCommand.length == 1 && !(command.equals("")))
        			{
        				
        				try {
    						Process p = Runtime.getRuntime().exec(textField.getText().toString());
    						
    						StringBuffer str = new StringBuffer();
    			            BufferedReader in = new BufferedReader(new InputStreamReader(
    			                    p.getInputStream()));
    			            String line;
    			            
    			            BufferedReader erro = new BufferedReader(new InputStreamReader(
    			                    p.getErrorStream()));
    			            String linee;
    			            
    			            while (p.isAlive()) {
    			            	while((line = in.readLine()) != null) {
    			            		str.append(line + "\n");
    			            		//textArea.append(line + " ");
    			            	}
    			                while((linee = erro.readLine()) != null) {
    			                	str.append(linee + "\n");
    			            		//textArea.append(line + " ");
    			                }
    			            }
//    			            textArea.append(str.toString() + " ");
    			            textField.selectAll();
    					} 
        				catch (IOException e1) {System.out.print(e1.getMessage());}
    				}
        			
        				/*
        				 * ATÉ AQUI
        				 */
        			
        			textField.setText("");
        			acess.writeRegistry(command);
        			
        			rowToAcess = acess.numberOfRows();
        		}
        		else if(e.getKeyCode() == KeyEvent.VK_UP) 
        		{
        			if(rowToAcess == acess.numberOfRows())
        			{
        				saveState = textField.getText().toString();
        				String cloaded = acess.loadComand(rowToAcess);
            			textField.setText(cloaded);
        				rowToAcess--;
        			}
        			else if(rowToAcess == 4){}
        			else
        			{
        				String cloaded = acess.loadComand(rowToAcess);
            			textField.setText(cloaded);
            			rowToAcess--;
        			}
				}
        		else if(e.getKeyCode() == KeyEvent.VK_DOWN) 
				{
        			if(rowToAcess == acess.numberOfRows()+1)
        			{
        				textField.setText(saveState);
        			}
        			String cloaded = acess.loadComand(rowToAcess);
        			textField.setText(cloaded);
					rowToAcess++;
				}
        	}
        });
        
    }

    public void print(String s) {
        try 
        {
        	Diretorio dirInfo = new Diretorio();
            doc.insertString(0, dirInfo.setTerminalInfo() + " " + s +"\n", null);
        }
        catch(Exception e) { System.out.println(e); }
    }
    
    public void printClear() {
        try 
        {
            doc.insertString(0, "\n", null);
        }
        catch(Exception e) { System.out.println(e); }
    }
    
}