package at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model.HamsterLexer;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model.JavaToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Christian
 */
public class LegoPrecompiler {

    String text;
    int pos;
    private LinkedList list = new LinkedList();
    PrintWriter out;

    /**
     * Creates a new instance of LegoPrecompiler
     */
    public LegoPrecompiler() {
    }


    public JavaToken nextToken(HamsterLexer lexer) throws IOException {
        JavaToken t = lexer.nextToken();
        while (t != null && (t.isComment() | t.isWhiteSpace()))
            t = lexer.nextToken();
        return t;
    }


    public void precompile(HamsterFile file) {
//                list.add("double");
//                list.add("int");
//                list.add("float");
//                list.add("boolean");
//                list.add("long");
//                list.add("byte");
//                list.add("char");
//                list.add("short");
//                list.add("String");
//                list.add("void");
//                String text = file.load();
//                char type = file.getType();
//                String className = file.getName()+"Lego";
//                String javaName = file.getAbsoluteJavaLego();
//                PrintWriter out = new PrintWriter(new FileWriter(javaName));
//                
//                HamsterLexer lexer = new HamsterLexer();
//                lexer.init(0, 0, text);
//                int pos = 0;
//                
//                JavaToken t = nextToken(lexer);
//                // imports
//                while (t != null && t.getText().equals("import")) {
//                        t = nextToken(lexer);
//                        while (t != null && !t.getText().equals(";"))
//                                t = nextToken(lexer);
//                        t = nextToken(lexer);
//                }
////                out.print("import .hamsterEvaluation.lego.at.ac.htlinn.hamsterbackend.model.LegoHamster; ");
//                if (t != null) {
//                        out.print(text.substring(pos, t.getStart()));
//                        pos = t.getStart();
//                        out.print("public class " + className
//                                + " extends .hamsterEvaluation.lego.at.ac.htlinn.hamsterbackend.model.LegoHamster { ");
//                }
//                
//                String test = " ";
//                boolean methode = false;
//                int value = 0;
//                int start = 0;
//                while(t != null){
//                        start = t.getStart();
//                        pos = start;
//                        
//                        if(list.contains(t.getText())){
//                                value = start;
//                                out.print("\npublic static ");
//                                start = t.getStart();
//                                t = nextToken(lexer);
//                                if(t != null && t.getText().equals("main")){
//                                        methode = true;
//                                        t = nextToken(lexer);
//                                        if (t.getText() == null)
//						break;
//                                        if(t != null && t.getText().equals("(")){
//                                                t = nextToken(lexer);
//                                                start = t.getStart();
//                                                test = text.substring(pos, start);
//                                                out.print(text.substring(pos, start));
//                                                out.print("String[] args");
//                                                pos = start;
//                                        }
//                                }else if (t != null){
//                                        t = nextToken(lexer);
//                                        if (t.getText() == null)
//						break;
//                                        if(t != null && t.getText().equals("(")){
//                                                methode = true;
//                                                t = nextToken(lexer);
//                                                start = t.getStart();
//                                                test = text.substring(pos, start);
//                                                out.print(text.substring(pos, start));
//                                                pos = start; 
//                                        }
//                                }
//                                int klammer = 0;
//                                while(t != null && methode){
//                                        t = nextToken(lexer);
//                                        if(t != null && t.getText().equals("{")){
//                                                klammer++;
//                                        }else if(t != null && t.getText().equals("}")){
//                                                klammer--;
//                                                if(klammer == 0)
//                                                        break;
//                                        }
//                                }        
//                        }
//                        if(t.getText().equals("}")){
//                                        start = t.getStart();
//                                        test = text.substring(pos, start+1);
//                                        out.print(text.substring(pos, start+1));
//                        }else if(t.getText().equals(";")){
//                                        start = t.getStart();
//                                        test = text.substring(value, start+1);
//                                        out.print(text.substring(value, start+1));
//                        }
//                        methode = false;
//                        if(t == null)
//                                break;
//                        t = nextToken(lexer);
//                        
//                }
//                test = text.substring(value);
//                out.print("}");
//                out.close();
        try {
            String text = file.load();
            char type = file.getType();
            String className = file.getName() + "Lego";
            String javaName = file.getAbsoluteJavaLego();
            PrintWriter out = new PrintWriter(new FileWriter(javaName));

            HamsterLexer lexer = new HamsterLexer();
            lexer.init(0, 0, text);
            int pos = 0;

            JavaToken t = nextToken(lexer);
            // imports
            while (t != null && t.getText().equals("import")) {
                t = nextToken(lexer);
                while (t != null && !t.getText().equals(";"))
                    t = nextToken(lexer);
                t = nextToken(lexer);
            }
//                out.print("import .hamsterEvaluation.lego.at.ac.htlinn.hamsterbackend.model.LegoHamster; ");
            if (t != null) {
                out.print(text.substring(pos, t.getStart()));
                pos = t.getStart();
                out.print("public class " + className
                        + " extends .hamsterEvaluation.lego.at.ac.htlinn.hamsterbackend.model.LegoHamster { ");
            }
            while (t != null && !(type == HamsterFile.HAMSTERCLASS)) {
                if (t.getText().equals("void")) {
                    int start = t.getStart();
                    t = nextToken(lexer);
                    if (t.getText() == null)
                        break;
                    if (t.getText().equals("main")) {
                        t = nextToken(lexer);
                        if (t.getText() == null)
                            break;
                        if (t.getText().equals("(")) {
                            t = nextToken(lexer);
                            if (t.getText() == null)
                                break;
                            if (t.getText().equals(")")) {
                                out.print(text.substring(pos, start));
                                pos = start;
                                out.print("public ");
                            }
                        }
                    }
                }
                t = nextToken(lexer);
            }

            out.print(text.substring(pos));
            out.print("public static void main(String[] args) { " +
                    "(new " + className + "()).main();" +
                    " }");
            out.print("}");
            out.close();

        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

}
