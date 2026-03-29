package at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench;

import java.io.File;
import java.io.IOException;


/**
 * Diese Klasse wird vom JFileChooser benutzt um die Ansicht aufs Dateisystem
 * festzulegen. In dieser Klasse wird dann das Hamsterprogramm-Verzeichnis als
 * Wurzel dargestellt, so dass nichts ausserhalb davon erreichbar ist.
 * 
 * @author Daniel Jasper
 */
public class HamsterFileSystemView  {



	public File createNewFolder(File containingDir) throws IOException {
		File f = new File(containingDir.getAbsolutePath() + File.separatorChar
				+ ("workbench.neuerordner"));
		for (int i = 2; f.exists(); i++) {
			f = new File(containingDir.getAbsolutePath() + File.separatorChar
					+ "workbench.neuerordner" + " " + i);

		}
		f.mkdir();
		return f;
	}

}
