package Main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import Vista.Principal;

public class Main {
	public static void main(String[] args) {
		try {
			// Esta línea hará que el JFileChooser se vea como el Explorador de Windows
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// El resto de tu código para iniciar la aplicación...
		EventQueue.invokeLater(() -> {
			Principal frame = new Principal();
			frame.setVisible(true);
		});
	}
}

