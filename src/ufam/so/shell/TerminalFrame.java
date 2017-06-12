package ufam.so.shell;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class TerminalFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private StyledDocument doc;
    private int indice = 0;
    StringBuilder log = new StringBuilder();

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

    public TerminalFrame() {
        setTitle("Terminal Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTextPane textPane = new JTextPane();
        textPane.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) 
        	{
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        		{
        			String str = textPane.getText();
        			StringBuilder sb = new StringBuilder();
        			File raiz = new File(str);
        			for(File f: raiz.listFiles()) {
        				if(f.isFile()) {
        					System.out.println(f.getName());
        					sb.append(f.getName());
        					sb.append("\n");
        				}
        			}
        			log.append(str);
    				log.append("\n");
        			textPane.setText("");
        			print("\n");
        			print(sb.toString());
        			print(log.toString());
        		}
        	}
        });
        textPane.setForeground(Color.GREEN);
        textPane.setBackground(Color.BLACK);
        textPane.setEditable(true);
        contentPane.add(textPane, BorderLayout.CENTER);
        doc = textPane.getStyledDocument();
    }

    public void print(String s) {
        try {
            doc.insertString(0, s+"\n", null);
        }
        catch(Exception e) { System.out.println(e); }
    }

}