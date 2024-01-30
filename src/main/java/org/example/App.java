package main.java.org.example;



import java_cup.runtime.Scanner;

import nodi.Program;
import parser_lexer.Lexer;
import parser_lexer.parser;
import visitors.CodeGenerator;
import visitors.ScopeVisitor;
import visitors.TypeVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;

/**
 * Hello world!
 *
 */
public class App {
    public App() throws FileNotFoundException, UnsupportedEncodingException {
    }

    public static void main(String[] args) throws Exception {

      /*  FileInputStream stream = new java.io.FileInputStream("src/main/java/org/example/sample.txt");
        Reader reader = new java.io.InputStreamReader(stream, "UTF-8");
        Lexer scanner = new Lexer(reader);
        Symbol token;
        File input = new File("src/main/java/org/example/sample.txt");
        parser p = new parser(new Lexer(new FileReader(input)));

        try {
            while ( !scanner.yyatEOF() ) {
                try {
                    Symbol tmp = scanner.next_token();
                    if(tmp.value != null){
                        System.out.println("<"+ sym.terminalNames[tmp.sym]+", "+ tmp.value.toString() +">");
                    } else {
                        System.out.println("<"+sym.terminalNames[tmp.sym]+">");
                    }
                    p.debug_parse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    */


        String filePath = args[0];
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        FileInputStream stream = new java.io.FileInputStream(args[0]);
        Reader reader = new java.io.InputStreamReader(stream, "UTF-8");
        Lexer scanner = new Lexer(reader);
        File prova = new File("src/main/java/org/example/speriamo.c");

        parser p = new parser((Scanner) scanner);


       /* while (!scanner.yyatEOF()) {
            try {
                Symbol token = scanner.next_token();
                if (token.value != null) {
                    System.out.println("<" + sym.terminalNames[token.sym] + ", " + token.value.toString() + ">");
                } else {
                    System.out.println("<" + sym.terminalNames[token.sym] + ">");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Rimuovi la chiamata a debug_parse all'interno del ciclo
        }*/
       // ScopeVisitor scopeVisitor = new ScopeVisitor();
        /*JTree tree;
        File file = new File(args[0]);
        p = new parser(new Lexer(new FileReader(file)));
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) p.parse().value;
        tree = new JTree(root);
        JFrame framePannello = new JFrame();
        framePannello.setSize(440, 400);
        JScrollPane treeview = new JScrollPane(tree);
        framePannello.add(treeview);
        framePannello.setVisible(true);*/
        String[] slashSplit = args[0].split("/");
        String fullName = slashSplit[slashSplit.length-1];
        String fileName = fullName.split(".txt")[0];
        CodeGenerator.FILE_NAME = fileName + ".c";
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) p.parse().value;
        //tree=new JTree(root);
        ((Program) root).accept(new ScopeVisitor());
        ((Program) root).accept(new TypeVisitor());
        ((Program) root).accept(new CodeGenerator());

        int a;}



}
