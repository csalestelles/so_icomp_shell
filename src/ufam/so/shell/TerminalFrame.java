package ufam.so.shell;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyledDocument;

import javax.swing.JTextPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.ScrollPane;

public class TerminalFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private StyledDocument doc;
    String log, today;
    private JTextField textField;
    File raiz;
    private ScrollPane terminalScroll;
    
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
        setTitle("Terminal Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 700);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        this.setResizable(false);
 
		terminalScroll = new ScrollPane();
		terminalScroll.setBounds(5, 96, 600, 577);
		
		
        JTextPane textPane = new JTextPane();
        textPane.setFont(new Font("GB18030 Bitmap", Font.BOLD, 13));
        textPane.setBounds(5, 96, 600, 577);
        contentPane.setLayout(null);
        textPane.setForeground(Color.LIGHT_GRAY);
        textPane.setBackground(Color.BLACK);
        textPane.setEditable(false);
        terminalScroll.add(textPane);
        contentPane.add(terminalScroll);
 
        directory.createLog();
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Iowan Old Style", Font.BOLD, 14));
        lblNewLabel.setBounds(16, 67, 578, 16);
        contentPane.add(lblNewLabel);
        lblNewLabel.setText(Diretorio.getDirInicial());
        
        doc = textPane.getStyledDocument();
        print("");
        
        textField = new JTextField();
        textField.setBounds(6, 29, 588, 26);
        contentPane.add(textField);
        textField.setColumns(10);
        textField.addKeyListener(new KeyAdapter()
        {
        	@Override
        	public void keyPressed(KeyEvent e)
        	{
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        		{
        			String comando = textField.getText();
        			String[] comandoInicial = comando.split(" ");
        			if (comandoInicial[0].equals("ls") && comandoInicial.length <= 2)     //listar arquivos e diretorios
        			{
        				StringBuilder sb = new StringBuilder();
        				if(comandoInicial.length == 1)
        				{
        					raiz = new File(Diretorio.getDynamicDir());
        					for(File f: raiz.listFiles()) {
                				if(f.isFile() || f.isDirectory()) 
                				{
                					sb.append(" -" + f.getName());
                					sb.append("\n");
                				}
                			}
        					directory.writeLog(comando, Diretorio.WORKING);
                			print("\n" + sb.toString());
        				}
        				else
        				{
        					raiz = new File(Diretorio.getDynamicDir() + comandoInicial[1]);
                			if (raiz.exists() && raiz.isDirectory())
                			{
                				for(File f: raiz.listFiles()) {
                    				if(f.isFile() || f.isDirectory()) 
                    				{
                    					sb.append(" -" + f.getName());
                    					sb.append("\n");
                    				}
                    			}
                				directory.writeLog(comando, Diretorio.WORKING);
                    			print(comandoInicial[1] + "\n" + sb.toString());
                			}
                			else
                			{
                				directory.writeLog(comando, Diretorio.ERROR);
                				print(comando + " -- erro ao acessar Diretório");
                				
                			}
        				}
        			}
        			else if (comandoInicial[0].equals("nav") && comandoInicial.length <= 2)      //nav -> NAVegar no Diretório
        			{
        				if (comandoInicial[1].equals(".."))
        				{
        					lblNewLabel.setText(directory.backToFatherDir(Diretorio.getDynamicDir()));
        					Diretorio.setDynamicDir(lblNewLabel.getText());
        					print("");
        					directory.writeLog(comando, Diretorio.WORKING);
        				}
        				else if (comandoInicial[1].equals("~"))
        				{
        					directory.writeLog(comando, Diretorio.WORKING);
        					Diretorio.setDynamicDir(Diretorio.getDirInicial());
        					print("");
        					lblNewLabel.setText(Diretorio.getDynamicDir());
        				}
        				else 
        				{
        					File dir = new File(Diretorio.getDynamicDir() + comandoInicial[1] + "/");
        					if(dir.exists() && dir.isDirectory())
        					{
        						Diretorio.setDynamicDir(Diretorio.getDynamicDir() + comandoInicial[1]);
        						lblNewLabel.setText(Diretorio.getDynamicDir());
        						directory.writeLog(comando, Diretorio.WORKING);
        						print("");
        					}
        					else
        					{
        						directory.writeLog(comando, Diretorio.ERROR);
        						print(comando + " -- erro ao acessar Diretório");
        					}
        				}
        					
        			}
        			else if(comandoInicial[0].equals("pwd"))
        			{
        				if(comandoInicial.length == 1)
        				{
        					directory.writeLog(comando, Diretorio.WORKING);
        					print(Diretorio.getDynamicDir());
        				}
        				else
        				{
        					directory.writeLog(comando, Diretorio.ERROR);
        					print(comando + " -- erro ao acessar Diretório atual");
        				}
        			}
        			else if(comandoInicial[0].equals("clear") && comandoInicial.length == 1)
        			{
        				int counterClear = 0;
        				while(counterClear < 33)
        				{
        					printClear();
        					counterClear++;
        				}
        				print("");
        			}
        			else if(comandoInicial[0].equals("mv") && comandoInicial.length == 3)
        			{
        				try 
        				{
							if(directory.move(comando))
							{
								directory.writeLog(comando, Diretorio.WORKING);
								print("");
							}
							else
							{
								directory.writeLog(comando, Diretorio.ERROR);
								print(comando + " -- erro ao acessar informações");
							}
						} 
        				catch (IOException e1) {e1.printStackTrace();}
        			}
        			
        			textField.setText("");
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