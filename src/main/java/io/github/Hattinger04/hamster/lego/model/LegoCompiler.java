package io.github.Hattinger04.hamster.lego.model;

import com.sun.tools.javac.Main;

import io.github.Hattinger04.hamster.compiler.model.HamsterLexer;
import io.github.Hattinger04.hamster.compiler.model.JavaToken;
import io.github.Hattinger04.hamster.model.HamsterFile;
import io.github.Hattinger04.hamster.workbench.Utils;
import io.github.Hattinger04.hamster.workbench.Workbench;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Mithilfe des LegoCompilers werden aus den Hamsterprogrammen 
 * class-Dateien erstellt, die f�r die nxj Code-Erstellung 
 * ben�tigt werden.
 */
public class LegoCompiler {

        /**
         * In diese Temporaere Datei werden die javac-Fehlermeldungen geschrieben,
         * bevor sie dann wieder eingelesen werden.
         */
        public static final String TEMP_FILE = "compiler.tmp";
        
        /**
         * Der Classpath waehrend der Kompilierung.
         */
        protected String classpath;
        
        /**
         * Der Kompiler setzt nur den Java Klassenpfad.
         */
        public LegoCompiler() {
                classpath = System.getProperty("java.class.path");
        }
        
        /**
	 * Mit dieser Methode wird das Kompilieren angestossen.
	 * 
	 * @param file
	 *            Die zu kompilierende Datei
	 */
        public void compile(HamsterFile file){
                
                String newClasspath = file.getAbsolute().substring(0,
                        file.getAbsolute().lastIndexOf(Utils.FSEP))
                        + Utils.PSEP
                        + classpath
                        + Utils.PSEP
                        + Workbench.getWorkbench().getProperty("classpath", "");
                String[] args = {"-g", "-classpath", newClasspath, file.getAbsoluteJavaLego()};
                
                try {
                        PrintWriter writer = new PrintWriter(new FileWriter(Utils.HOME
                                + Utils.FSEP + TEMP_FILE));
                        Main.compile(args, writer);
                        writer.close();
//                        new File(file.getAbsoluteJavaLego()).delete();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        
}
