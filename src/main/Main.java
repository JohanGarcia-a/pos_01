package main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import vista.Principal;

public class Main {
	public static void main(String[] args) {
		try {
			// Esta línea hará que el JFileChooser se vea como el Explorador de Windows
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		EventQueue.invokeLater(() -> {
			Principal frame = new Principal();
			frame.setVisible(true);
			// Prueba para activar el workflow de GitHub Actions
		});
	}
}

