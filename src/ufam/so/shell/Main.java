package ufam.so.shell;

import java.io.File;

public class Main {

	public static void main(String[] args)
	{
		String caminho = javax.swing.JOptionPane.showInputDialog(
				"digite o diretorio a ser listado");
		StringBuilder sb = new StringBuilder();
		File raiz = new File(caminho);
		for(File f: raiz.listFiles()) {
			if(f.isFile()) {
				System.out.println(f.getName());
				sb.append(f.getName());
				sb.append("\n");
			}
		}
		javax.swing.JOptionPane.showMessageDialog(null, sb.toString());
	}
}
