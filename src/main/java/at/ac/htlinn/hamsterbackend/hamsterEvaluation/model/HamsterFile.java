package at.ac.htlinn.hamsterbackend.hamsterEvaluation.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.HamsterFileFilter;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


/**
 * Diese Klasse stellt eine einzelnen Datei im Dateisystem dar. Diese Datei kann
 * entweder ein Hamsterprogramm (imperativ, objektorientiert oder Hamsterklasse)
 * oder auch ein Territorium enthalten.
 * <p>
 * In einem Hamsterprogramm wird beim Speichern jeweils ein Zeichen eingefuegt,
 * das beschreibt, um welchen Typ von Hamsterprogramm es sich dabei handelt.
 *
 * @author Daniel Jasper
 */
public class HamsterFile implements Comparable {
    /**
     * Dieses Zeichen steht fuer ein imperatives Hamsterprogramm.
     */
    public static final char IMPERATIVE = 'i';

    /**
     * Dieses Zeichen steht fuer ein objektorientiertes Hamsterprogramm.
     */
    public static final char OBJECT = 'o';

    /**
     * Dieses Zeichen steht fuer eine Hamsterklasse.
     */
    public static final char HAMSTERCLASS = 'c';

    public static final char SCHEMEPROGRAM = 's'; // Martin

    public static final char SCHEMEKONSOLE = 'k'; // Martin

    // Prolog
    public static final char PROLOGPROGRAM = 'p'; // Prolog

    // Lego
    public static final char LEGOPROGRAM = 'l'; // Christian

    public static final char TERRITORIUM = 't'; // dibo 260110

    public static final char PYTHONPROGRAM = 'y'; // Python

    public static final char RUBYPROGRAM = 'u'; // Ruby

    public static final char SCRATCHPROGRAM = 'r'; // Scratch

    public static final char FSM = 'f'; // Endlicher Automat

    public static final char FLOWCHART = 'w'; // PAP

    public static final char JAVASCRIPTPROGRAM = 'j'; // JavaScript

    /**
     *
     */
    public static final String LOCKED = "locked";

    public static final String MODIFIED = "modified";

    public static final String NAME = "name";

    /**
     * Dieses Klassenattribut zaehlt die neu erzeugten Dateien, so dass ihnen
     * jeweils ein eindeutiger Name (Neu <count>) zugeordnet werden kann.
     */
    private static int count = 1;

    private static HashMap allFiles = new HashMap();

    /**
     * Der PropertyChangeSupport hilft dabei, die PropertyChangeEvents an die
     * entsprechenden Listener weiterzuleiten.
     */
    protected PropertyChangeSupport changeSupport;

    protected File file;

    protected char type;

    /**
     * Dieses Attribut gibt an, ob die Datei seit der letzten Speicherung
     * modifiziert wurde.
     */
    protected boolean modified;

    /**
     * Dieses Attribut gibt an, ob die Datei zur Zeit gesperrt ist. Eine
     * gesperrte Datei wird gerade ausgefuehrt und darf nicht bearbeitet werden.
     */
    protected boolean locked;

    /**
     * Mit diesem Konstruktor wird ein HamsterFile erzeugt, das keine
     * Repraesentation im Dateisystem hat. Der Typ der Datei wird festgelegt und
     * die Datei erhaelt automatisch einen Namen.
     *
     * @param type Der Typ der Datei
     */
    public HamsterFile(char type, String path) {
        changeSupport = new PropertyChangeSupport(this);
        this.type = type;
        file = new File(path);
    }

    public HamsterFile(String code, char type, String path) {
        this(type, path);
        PrintWriter senke = null;
        try {
            senke = new PrintWriter(new FileWriter(file));
            senke.println(code);
            senke.close();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (senke != null) {
                    senke.close();
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Dieser Konstruktor kann nicht direkt sondern nur ueber die Methode
     * getHamsterFile() aufgerufen werden. Er erzeugt ein HamsterFile, das die
     * uebergebene Datei im Dateisystem darstellt.
     *
     * @param file Die dargestellte Datei
     */
    protected HamsterFile(File file) {
        changeSupport = new PropertyChangeSupport(this);
        this.file = file;
    }

    /**
     * Diese Methode liefert einen HamsterFile, der die Datei repraesentiert,
     * die durch absolute angegeben ist. Es wird dabei darauf geachtet, dass
     * jede Datei nur durch genau ein Objekt repraesentiert wird.
     *
     * @param absolute Der Pfad zu der Datei
     * @return Das HamsterFile zu der Datei
     */
    public static HamsterFile getHamsterFile(String absolute) {
        if (allFiles.get(absolute) == null) {
            File f = new File(absolute);
            HamsterFile file = null;

            file = new HamsterFile(new File(absolute));

            allFiles.put(absolute, file);
            return file;
        } else {
            return (HamsterFile) allFiles.get(absolute);
        }
    }

    // dibo 05032010
    public static HamsterFile createHamsterFile(String absolute, char type) {
        if (allFiles.get(absolute) == null) {
            File f = new File(absolute);
            HamsterFile file = null;

            file = new HamsterFile(new File(absolute));

            allFiles.put(absolute, file);
            return file;
        } else {
            return (HamsterFile) allFiles.get(absolute);
        }
    }

    public static HamsterFile getHamsterFile(File file) {
        return getHamsterFile(file.getAbsolutePath());
    }

    public String toString() {
        return getName();
    }

    public File getFile() {
        return file;
    }

    public void delete() {
        if (!exists())
            return;
        if (isDirectory()) {
            recursiveDelete(file);
        } else {
            String name = file.getAbsolutePath();
            if (name.endsWith(".ham")) {
                name = name.substring(0, name.length() - ".ham".length());
                File parent = file.getParentFile();
                File[] filesInDirectory = parent.listFiles();
                for (int i = 0; i < filesInDirectory.length; i++) {
                    String f = filesInDirectory[i].getAbsolutePath();
                    if (!f.endsWith(".class"))
                        continue;
                    if (!f.startsWith(name + "$"))
                        continue;
                    filesInDirectory[i].delete();
                }
                new File(name + ".class").delete();
                new File(name + ".java").delete();
                /* lego */
                new File(name + "Lego.class").delete();
                new File(name + "Lego.java").delete();
                new File(name + "Lego.nxj").delete();
            }
            file.delete();
        }
    }

    protected void deleteClassFiles() {
        String name = file.getAbsolutePath();
        if (!name.endsWith(".ham"))
            return;
        name = name.substring(0, name.length() - ".ham".length());
        File parent = file.getParentFile();
        String[] filesInDirectory = parent.list();
        for (int i = 0; i < filesInDirectory.length; i++) {
            if (!filesInDirectory[i].endsWith(".class"))
                continue;
            if (!filesInDirectory[i].startsWith(name + "$"))
                continue;
            new File(parent, filesInDirectory[i]).delete();
        }
        new File(name + ".class").delete();
    }

    public static void recursiveDelete(File file) {
        boolean isLink = true;
        try {
            String realDir = file.getCanonicalPath();
            isLink = !realDir.equals(file.getAbsolutePath());
        } catch (IOException e) {
        }

        File[] files = file.listFiles();
        if (!isLink && files != null) {
            for (int i = 0; i < files.length; i++)
                recursiveDelete(files[i]);
        }
        file.delete();
    }

    public boolean move(File newName) {
        if (file.renameTo(newName)) {
            deleteClassFiles();
            return true;
        } else {
            return false;
        }
    }

    public String getDir() {
        if (isDirectory())
            return file.getAbsolutePath();
        else
            return file.getParent();
    }

    public void copy(File toDir) {
        recursiveCopy(file, toDir);
    }

    static void recursiveCopy(File file, File toDir) {
        File newFile = new File(toDir, file.getName());
        if (newFile.equals(file))
            return;
        if (newFile.exists())
            return;
        if (file.isDirectory()) {
            newFile.mkdir();
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++)
                recursiveCopy(files[i], newFile);
        } else {
            HamsterFile oldHamsterFile = getHamsterFile(file);
            HamsterFile newHamsterFile = createHamsterFile(
                    newFile.getAbsolutePath(), oldHamsterFile.getType());
            newHamsterFile.setType(oldHamsterFile.getType());

            newHamsterFile.save(oldHamsterFile.load());
        }

    }

    /**
     * Diese Methode prueft, ob es sich bei der Datei um ein Verzeichnis
     * handelt.
     *
     * @return true wenn Verzeichnis, false sonst
     */
    public boolean isDirectory() {
        return file.exists() && file.isDirectory();
    }

    public boolean isEmpty() {
        return !isDirectory() || file.listFiles().length == 0;
    }

    /**
     * Diese Methode prueft, ob es sich beim dem HamsterFile um eine Datei
     * handelt, die wirklich im Dateisystem existiert oder nur eine virtuelle
     * Datei.
     *
     * @return true wenn echte Datei, false sonst
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Diese Methode prueft, ob es sich bei der Datei um ein Programm handelt
     *
     * @return true wenn Programm, false sonst
     */
    public boolean isProgram() {
        return HamsterFileFilter.HAM_FILTER.accept(file) && !file.isDirectory();
    }

    /**
     * Diese Methode prueft, ob es sich bei der Datei um ein Territorium handelt
     *
     * @return true wenn Territorium, false sonst
     */
    public boolean isTerrain() {
        return HamsterFileFilter.TER_FILTER.accept(file) && !file.isDirectory();
    }

    public String getName() {
        if (file.getName().endsWith(".ham") || file.getName().endsWith(".ter"))
            return file.getName().substring(0, file.getName().length() - 4);
        return file.getName();
    }

    public String getAbsoluteJava() {
        try {
            String path = getAbsolute();
            return path.substring(0, path.lastIndexOf(".")) + ".java";
        } catch (Exception exc) {
            return null;
        }
    }

    /* lego */
    public String getAbsoluteJavaLego() {
        String path = getAbsolute();
        return path.substring(0, path.lastIndexOf(".")) + "Lego.java";
    }

    public String getAbsoluteClass() {
        String path = getAbsolute();
        return path.substring(0, path.lastIndexOf(".")) + ".class";
    }

    public String getAbsolute() {
        return file.getAbsolutePath();
    }

    public HamsterFile[] getChildren() {
        File[] files = file.listFiles(HamsterFileFilter.HAM_FILTER);
        HamsterFile[] hFiles = new HamsterFile[files.length];
        for (int i = 0; i < hFiles.length; i++) {
            hFiles[i] = getHamsterFile(files[i]);
        }
        return hFiles;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof HamsterFile))
            return false;
        return ((HamsterFile) obj).file.equals(file);
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String load() {
        if (!exists()) {
            if (type == IMPERATIVE) {
                return "void main() {\n    \n}\n";
            } else if (type == OBJECT) {
                return "void main() {\n    \n}\n";
            } else if (type == HAMSTERCLASS) {
                return "class " + "at.ac.htlinn.hamsterbackend.model.meinhamster"
                        + " extends Hamster {\n    \n}\n";
            } else if (type == SCHEMEPROGRAM) { // Martin
                return "(define (start Territorium)\n()\n)";
            } else if (type == PROLOGPROGRAM) { // Prolog
                return "main :-\n    vornFrei,\n    vor.\n";
            } else if (type == PYTHONPROGRAM) { // Python
                return "if vornFrei():\n    vor()\n";
            } else if (type == JAVASCRIPTPROGRAM) { // JavaScript
                return "if (vornFrei())\n    vor();\n";
            } else if (type == RUBYPROGRAM) { // Ruby
                return "if vornFrei\n    vor\nend";
            } else if (type == SCRATCHPROGRAM) { // Scratch
                return "";
            } else if (type == FSM) { // FSM
                return "";
            } else if (type == FLOWCHART) { // Flowchart
                return "";
            } else if (type == LEGOPROGRAM) {
                return "void main() {\n    \n}\n";

            } else {
                return "";
            }
        }
        byte[] buffer = null;
        try {
            FileInputStream input = new FileInputStream(file);
            buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String s = new String(buffer);
        if (isTerrain()) { // dibo 260110
            type = TERRITORIUM;
        } else if (isProgram()) {
            //TODO: check why type changes to i!
            type = IMPERATIVE;
            int pos = s.indexOf("*/");
            if (pos > 0 && pos + 2 < s.length()) {
                String typeString = s.substring(0, pos);
                if (typeString.equals("/*object-oriented program")) {
                    s = s.substring(pos + 2);
                    type = OBJECT;
                } else if (typeString.equals("/*class")) {
                    s = s.substring(pos + 2);
                    type = HAMSTERCLASS;
                } else if (typeString.equals("/*scheme program")) { // Martin
                    s = s.substring(pos + 2);
                    type = SCHEMEPROGRAM;
                } else if (typeString.equals("/*prolog program")) { // Prolog
                    s = s.substring(pos + 2);
                    type = PROLOGPROGRAM;
                } else if (typeString.equals("/*python program")) { // Python
                    s = s.substring(pos + 2);
                    type = PYTHONPROGRAM;
                } else if (typeString.equals("/*javascript program")) { // JavaScript
                    s = s.substring(pos + 2);
                    type = JAVASCRIPTPROGRAM;
                } else if (typeString.equals("/*ruby program")) { // Ruby
                    s = s.substring(pos + 2);
                    type = RUBYPROGRAM;
                } else if (typeString.equals("/*lego program")) {
                    s = s.substring(pos + 2);
                    type = LEGOPROGRAM;
                } else if (typeString.equals("/*imperative program")) {
                    s = s.substring(pos + 2);
                }
            }
        }
        return s;
    }


    public void save(String text) {
        // if (isProgram()) {
        if (type == IMPERATIVE) {
            text = "/*imperative program*/" + text;
        } else if (type == OBJECT) {
            text = "/*object-oriented program*/" + text;
        } else if (type == HAMSTERCLASS) {
            text = "/*class*/" + text;
        } else if (type == SCHEMEPROGRAM) { // Martin
            text = "/*scheme program*/" + text;
        } else if (type == PROLOGPROGRAM) { // Prolog
            text = "/*prolog program*/" + text;
        } else if (type == PYTHONPROGRAM) { // Python
            text = "/*python program*/" + text;
        } else if (type == JAVASCRIPTPROGRAM) { // JavaScript
            text = "/*javascript program*/" + text;
        } else if (type == RUBYPROGRAM) { // Ruby
            text = "/*ruby program*/" + text;
        } else if (type == SCRATCHPROGRAM || type == FSM || type == FLOWCHART) { // Scratch,
            // FSM
            // no prefix
        }/* Lego-Christian */ else if (type == LEGOPROGRAM) {
            text = "/*lego program*/" + text;
        }
        // }
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.write(text.getBytes());
            output.close();
            setModified(false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public File[] listFiles() {
        return file.listFiles();
    }

    public boolean isModified() {
        return modified;
    }

    public long lastModified() {
        return file.lastModified();
    }

    public void setModified(boolean modified) {
        if (this.modified == modified)
            return;
        boolean oldValue = this.modified;
        this.modified = modified;
        changeSupport.firePropertyChange(MODIFIED, oldValue, modified);
    }

    /**
     * Diese Methode liefert den Typ der Datei.
     *
     * @return Der Typ der Datei
     */
    public char getType() {
        if (type == 0) {
            load();
        }
        return type;
    }

    /**
     * Mit dieser Methoden kann der Typ der Datei gesetzt werden.
     *
     * @param type Der neue Typ
     */
    public void setType(char type) {
        this.type = type;
    }

    /**
     * Diese Methode fragt ab, ob die Datei gesperrt ist.
     *
     * @return Der Wert von locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Mit dieser Methode wird das locked-Attribut gesetzt.
     *
     * @param locked Der neue Wert von locked
     */
    public void setLocked(boolean locked) {
        if (this.locked == locked)
            return;
        boolean oldValue = this.locked;
        this.locked = locked;
        changeSupport.firePropertyChange(LOCKED, oldValue, locked);
    }

    public int compareTo(Object o) {
        if (!(o instanceof HamsterFile))
            return 0;
        HamsterFile h = (HamsterFile) o;
        return file.compareTo(h.file);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}