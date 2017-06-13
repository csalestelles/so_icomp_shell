package ufam.so.shell;

import java.awt.BorderLayout;
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
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;

public class TerminalFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private StyledDocument doc;
    private int indice = 0;
    StringBuilder log = new StringBuilder();
    private JTextField textField;
    File raiz;
    
    String diretorioInicial = "/";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TerminalFrame terminal = new TerminalFrame();
                    terminal.setVisible(true);
//                    terminal.print("terminalCaio$ "); // Using print() to print to the "Swing Terminal".
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TerminalFrame() throws IOException {
        setTitle("Terminal Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        this.setResizable(false);
        
        Path path = Paths.get("/Users/caiotelles/Desktop/logTerminal.txt");
        List<String> linhas = Files.readAllLines(path);
        
        JTextPane textPane = new JTextPane();
        textPane.setFont(new Font("Menlo", Font.BOLD, 13));
        textPane.setBounds(0, 101, 600, 577);
        contentPane.setLayout(null);
        textPane.setForeground(Color.BLACK);
        textPane.setBackground(Color.WHITE);
        textPane.setEditable(false);
        contentPane.add(textPane);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setFont(new Font("Iowan Old Style", Font.BOLD, 14));
        lblNewLabel.setBounds(16, 67, 352, 16);
        contentPane.add(lblNewLabel);
        lblNewLabel.setText(" ->    Diretório Raiz '/'");
        
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
        			if (comandoInicial[0].equals("ls"))     //listar arquivos e diretorios
        			{
        				StringBuilder sb = new StringBuilder();
        				if(comandoInicial.length == 1)
        				{
        					raiz = new File(diretorioInicial);
        					for(File f: raiz.listFiles()) {
                				if(f.isFile() || f.isDirectory()) 
                				{
                					sb.append(" -" + f.getName());
                					sb.append("\n");
                				}
                			}
                			print(comando + "\n" + sb.toString());
        				}
        				else
        				{
        					raiz = new File(diretorioInicial + comandoInicial[1]);
                			if (raiz.exists() && raiz.isDirectory())
                			{
                				for(File f: raiz.listFiles()) {
                    				if(f.isFile() || f.isDirectory()) 
                    				{
                    					sb.append(" -" + f.getName());
                    					sb.append("\n");
                    				}
                    			}
                    			print(comando + "\n" + sb.toString());
                			}
                			else
                			{
                				print(comando + " -- erro ao acessar Diretório");
                			}
        				}
        			}
        			else if (comandoInicial[0].equals("nav"))      //nav -> NAVegar no Diretório
        			{
        				if (comandoInicial[1].equals(".."))
        				{
        					String[] novoDiretorio = diretorioInicial.split("/");
        					int ultimodir = novoDiretorio.length;
//        					diretorioInicial.replaceAll("", (novoDiretorio[ultimodir-1]+"/"));
        					lblNewLabel.setText(diretorioInicial.replaceAll(novoDiretorio[ultimodir-1]+"/", ""));
        					diretorioInicial = lblNewLabel.getText();
        				}
        				else if (comandoInicial[1].equals("~"))
        				{
        					diretorioInicial = "/";
        				}
        				else 
        				{
        					File dir = new File("/"+diretorioInicial+comandoInicial[1]+"/");
        					if(dir.exists() && dir.isDirectory())
        					{
        						
        						diretorioInicial += comandoInicial[1];
        						lblNewLabel.setText(diretorioInicial);
        						print("Diretorio " + diretorioInicial + " acessado");
        					}
        					else
        					{
        						print(comando + " -- erro ao acessar Diretório");
        					}
        				}
        					
        			}
        			else if(comandoInicial[0].equals("pwd"))
        			{
        				print("Diretorio atual /" + diretorioInicial);
        			}
        			textField.setText("");
        		}
        	}
        });
        
        doc = textPane.getStyledDocument();
        
    }

    public void print(String s) {
        try {
            doc.insertString(0, s+"\n", null);
        }
        catch(Exception e) { System.out.println(e); }
    }
}