package ufam.so.shell;

/*
 * Alunos:
 *              CAIO ARTHUR SALES TELLES      21453444
 *              GUILHERME PEÑA CÉSPEDES   
 *              FELIPE DE MENEZES SANTOS      21453441
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.StyledDocument;

import javax.swing.JTextPane;


import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;


public class TerminalFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private StyledDocument doc;
    private JTextField textField;
    private File raiz;
    private PastComands acess;
    
    int rowToAcess;      
    private String saveState;     
    
    Diretorio directory;
    
    String directoryString = Diretorio.getDirInicial();    //O terminalSimulator parte do diretório do usuário
    													   //que é acessado a partir do método getDirInicial().

    /*
     *          FUNCAO MAIN
     */
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
    	
    	
    	/*
    	 * Itens da interface gráfica do terminalSimulator 
    	 */
    	
    	directory = new Diretorio();
    	setForeground(Color.WHITE);
		setTitle("TerminalSimulator");
		setBackground(Color.DARK_GRAY);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		setResizable(false);
		this.setSize(600, 450);
		
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
        directory.createLog();     //Cria o arquivo de Log que irá registrar os comandos utilizados e sua data
        
        acess = new PastComands(); //Cria um arquivo que armazena somente os comandos para serem acessados apertando
        						   //a seta pra cima ou pra baixo
        
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
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)   //A partir daqui tratam-se os comandos
        		{
        			String command = textField.getText();
        			String[] initialCommand = command.split(" ");
        			//COMANDO LS
        			if (initialCommand[0].equals("ls") && initialCommand.length <= 2)     
        			{
        				StringBuilder sb = new StringBuilder();
        				//SE O LS NÃO VIER ACOMPANHADO DE NENHUM DIRETÓRIO ESPECIFICADO
        				if(initialCommand.length == 1)
        				{
        					raiz = new File(Diretorio.getDynamicDir());  //ACESSA O DIRETORIO ATUAL
        					for(File f: raiz.listFiles()) {              //LOOP PARA TODOS OS ARQUIVOS
        						String dot = f.getName().substring(0, 1);
        						//ADICIONA NO STRINGBUILDER CADA ARQUIVO QUE CORRESPONDE AS CONDICOES DO IF ABAIXO
                				if((f.isFile() || f.isDirectory()) && !dot.equals(".")) 
                				{
                					sb.append(" -" + f.getName());  
                					sb.append("\n");
                				}
                			}
        					directory.writeLog(command, Diretorio.WORKING);  //ESCREVE NO LOG
                			print(command + "\n" + sb.toString());   //EXIBE O RESULTADO NO TERMINALSIMULATOR
        				}
        				//SE O LS VIER ACOMPANHADO DE UM DIRETORIO A SER BUSCADO
        				else
        				{
        					raiz = new File(Diretorio.getDynamicDir() + initialCommand[1]);
                			if (raiz.exists() && raiz.isDirectory())   //SE EXISTE O DIRETORIO 
                			{
                				//A PARTIR DAQUI O CODIGO É SEMELHANTE AO ANTERIOR
                				for(File f: raiz.listFiles()) {
                					String dot = f.getName().substring(0, 1);
                    				if((f.isFile() || f.isDirectory()) && !dot.equals(".")) 
                    				{
                    					sb.append(" -" + f.getName());
                    					sb.append("\n");
                    				}
                    			}
                				directory.writeLog(command, Diretorio.WORKING);
                    			print(command + "\n" + sb.toString());
                			}
                			//CASO NAO OCORRA NENHUM DOS DOIS CASOS ACIMA O TERMINAL REGISTRA UM ERRO
                			else
                			{
                				directory.writeLog(command, Diretorio.ERROR);
                				print(command + " -- erro ao acessar Diretório");
                				
                			}
        				}
        			}
        			//COMANDO CD
        			else if (initialCommand[0].equals("cd") && initialCommand.length <= 2)      //nav -> NAVegar no Diretório
        			{
        				//CD ..
        				if (initialCommand[1].equals(".."))
        				{
        					//RETORNA PARA O DIRETORIO PAI
        					lblNewLabel.setText(directory.backToFatherDir(Diretorio.getDynamicDir()));
        					Diretorio.setDynamicDir(lblNewLabel.getText());
        					print(command);
        					directory.writeLog(command, Diretorio.WORKING);
        				}
        				//CD ~
        				else if (initialCommand[1].equals("~"))
        				{
        					//RETORNA PARA O DIRETORIO RAIZ (O DIRETORIO RAIZ QUE FOI UTILIZADO FOI: /Users/caiotelles/)
        					directory.writeLog(command, Diretorio.WORKING);
        					Diretorio.setDynamicDir(Diretorio.getDirInicial());
        					print(command);
        					lblNewLabel.setText(Diretorio.getDynamicDir());
        				}
        				//CD diretorio
        				else 
        				{
        					File dir = new File(Diretorio.getDynamicDir() + initialCommand[1] + "/");
        					if(dir.exists() && dir.isDirectory())
        					{
        						Diretorio.setDynamicDir(Diretorio.getDynamicDir() + initialCommand[1]);
        						lblNewLabel.setText(Diretorio.getDynamicDir());
        						directory.writeLog(command, Diretorio.WORKING);
        						print(command);
        					}
        					else
        					{
        						directory.writeLog(command, Diretorio.ERROR);
        						print(command + " -- erro ao acessar Diretório");
        					}
        				}
        					
        			}
        			//COMANDO PWD
        			else if(initialCommand[0].equals("pwd"))
        			{
        				if(initialCommand.length == 1)
        				{
        					directory.writeLog(command, Diretorio.WORKING);
        					print(command + Diretorio.getDynamicDir());
        				}
        				else
        				{
        					directory.writeLog(command, Diretorio.ERROR);
        					print(command + " -- erro ao acessar Diretório atual");
        				}
        			}
        			//COMANDO CLEAR
        			else if(initialCommand[0].equals("clear") && initialCommand.length == 1)
        			{
        				int counterClear = 0;
        				while(counterClear < 38)
        				{
        					printClear();
        					counterClear++;
        				}
        				print("");
        			}
        			//COMANDO MV
        			else if(initialCommand[0].equals("mv") && initialCommand.length == 3)
        			{
        				try 
        				{//MOVE ARQUIVOS E DIRETORIOS
							if(directory.move(command))
							{
								directory.writeLog(command, Diretorio.WORKING);
								print(command);
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
        			
        			// ABRIR ARQUIVOS 
        			else if(initialCommand[0].equals("open") && initialCommand.length == 2)
        			{
        				try
        				{
        					File fileToOpen = directory.openFile(command);
        					if (fileToOpen.isFile())
        					{
        						java.awt.Desktop.getDesktop().open(directory.openFile(command));
        						directory.writeLog(command, Diretorio.WORKING);
        						print(command);
        					}
        					else
        					{
        						directory.writeLog(command, Diretorio.ERROR);
								print(command + " -- arquivo não existe");
        					}
        						
        				}
        				catch(IOException error){error.printStackTrace();}
        			}
        			
        			
        			
        			//FIM
        			
        			
        			
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
        			
        			
        			textField.setText("");   //LIMPA A AREA DE COMANDOS APÓS A EXECUCAO
        			
        			acess.writeRegistry(command);  //REGISTRA O COMANDO PARA QUE POSSA SER ACESSADO DEPOIS
        			rowToAcess = acess.numberOfRows();  //INTEIRO UTILIZADO NO ACESSO AOS COMANDOS QUE JÁ FORAM UTILZADOS
        		}
        		
        		//NAVEGA PELOS COMANDO JÁ UTILIZADOS; SETAS PARA CIMA E PARA BAIXO
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
    
    //MÉTODO QUE TRATA DE EXIBIR OS RESULTADOS DOS COMANDOS NO TERMINAL
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