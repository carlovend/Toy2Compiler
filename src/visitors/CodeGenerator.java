package visitors;

import javafx.util.Builder;
import nodi.*;
import nodi.expr.*;
import nodi.statements.ElifOp;
import nodi.statements.IfOp;
import nodi.statements.Stat;
import nodi.statements.WhileOp;
import tables.FieldType;
import tables.Row;
import tables.SymbolTable;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeGenerator implements Visitor {

    private SymbolTable currentScope;
    public static String FILE_NAME;
    public static File outFile;
    private static FileWriter writer;


    public String convertTypeOp(String typeOp) {

        String tipoOprazione = "";
        if (typeOp.equals("plusOp"))
            tipoOprazione = "+";
        if (typeOp.equals("minusOp"))
            tipoOprazione = "-";
        if (typeOp.equals("timesOp"))
            tipoOprazione = "*";
        if (typeOp.equals("divOp"))
            tipoOprazione = "/";
        if (typeOp.equals("gtOp"))
            tipoOprazione = ">";
        if (typeOp.equals("geOp"))
            tipoOprazione = ">=";
        if (typeOp.equals("ltOp"))
            tipoOprazione = "<";
        if (typeOp.equals("leOp"))
            tipoOprazione = "<=";
        if (typeOp.equals("neOp"))
            tipoOprazione = "!=";
        if (typeOp.equals("eqOp"))
            tipoOprazione = "==";
        if (typeOp.equals("andOp"))
            tipoOprazione = "&&";
        if (typeOp.equals("orOp"))
            tipoOprazione = "||";
        if (typeOp.equals("unaryMinusOp"))
            tipoOprazione = "-1*";
        if (typeOp.equals("notOp"))
            tipoOprazione = "!";

        return tipoOprazione;
    }

    public String convertType(String type) {
        if (type.equals("integer")) type = "int";
        if (type.equals("boolean")) type = "bool";
        if (type.equals("string")) type = "char*";
        if (type.equals("string_const")) type = "char*";
        if (type.equals("real")) type = "float";
        if (type.equals("integer_const")) type = "int";
        if (type.equals("real_const")) type = "float";

        return type;
    }
    boolean isString = false;
    @Override
    public Object visit(BinaryOP binaryOP) throws Exception {

        String espressione = "";
        String expr1 = "";
        String expr2 = "";
        String tipoEspressione = convertTypeOp(binaryOP.getOp());



        expr1 = (String) binaryOP.getExpr1().accept(this);
        expr2 = (String) binaryOP.getExpr2().accept(this);

        if (binaryOP.getExpr1() instanceof Identifier) {
            binaryOP.getExpr1().setType(typeVisit((Identifier) binaryOP.getExpr1()));
        }
        if (binaryOP.getExpr2() instanceof Identifier) {
            binaryOP.getExpr2().setType(typeVisit((Identifier) binaryOP.getExpr2()));
        }

        if (binaryOP.getExpr1() instanceof FunCallOp) {
            binaryOP.getExpr1().setType(typeVisit(((FunCallOp) binaryOP.getExpr1()).getId()));
        }
        if (binaryOP.getExpr2() instanceof FunCallOp) {
            binaryOP.getExpr2().setType(typeVisit(((FunCallOp) binaryOP.getExpr2()).getId()));
        }

        String tipoOperazione = binaryOP.getOp();
        if (binaryOP.getExpr1() instanceof Identifier) {
            expr1 = estraiIdentificatori(((Identifier) binaryOP.getExpr1()).getId(),outParam);
        }
        if (binaryOP.getExpr2() instanceof Identifier) {
            expr2 = estraiIdentificatori(((Identifier) binaryOP.getExpr2()).getId(),outParam);
        }


        if (tipoOperazione.equals("plusOp") || tipoOperazione.equals("minusOp") || tipoOperazione.equals("timesOp") || tipoOperazione.equals("divOp")) {
            if (binaryOP.getExpr1().getType().contains("string")&&binaryOP.getExpr2().getType().contains("string")) {
                expr1 = "str_concat("+expr1+","+expr2+")";
                return expr1;
            }
            if (binaryOP.getExpr1().getType().contains("integer")&&binaryOP.getExpr2().getType().contains("string")) {
               expr1 = "str_concat("+"integer_to_str("+expr1+"),"+expr2+")";
               return expr1;
            }
            if (binaryOP.getExpr1().getType().contains("string")&&binaryOP.getExpr2().getType().contains("integer")) {
                expr2 = "str_concat("+expr1+", integer_to_str("+expr2+"))";
                return expr2;
            }
            if (binaryOP.getExpr1().getType().contains("real")&&binaryOP.getExpr2().getType().contains("string")) {
                expr1 = "str_concat("+"real_to_str("+expr1+"),"+expr2+")";
                return expr1;
            }
            if (binaryOP.getExpr1().getType().contains("string")&&binaryOP.getExpr2().getType().contains("real")) {
                expr2 = "str_concat("+expr1+", real_to_str("+expr2+"))";
                return expr2;
            }
            if (binaryOP.getExpr1().getType().contains("boolean")&&binaryOP.getExpr2().getType().contains("string")) {
                expr1 = "str_concat("+"bool_to_str("+expr1+"),"+expr2+")";
                return expr1;
            }
            if (binaryOP.getExpr1().getType().contains("string")&&binaryOP.getExpr2().getType().contains("boolean")) {
                expr2 = "str_concat("+expr1+", bool_to_str("+expr2+"))";
                return expr2;
            }
            espressione = expr1 + tipoEspressione + expr2;
            return espressione;
        }
        System.out.println(binaryOP.getExpr1().getType()+"culi"+tipoOperazione);
        if (tipoOperazione.equals("gtOp") || tipoOperazione.equals("geOp") || tipoOperazione.equals("ltOp") || tipoOperazione.equals("leOp") || tipoOperazione.equals("eqOp") || tipoOperazione.equals("neOp")) {
            if (tipoOperazione.equals("eqOp")&&(binaryOP.getExpr1().getType().contains("string")||binaryOP.getExpr2().getType().contains("string"))) {
                return espressione = "strcmp("+expr1+","+expr2+")==0";
            }
            if (tipoOperazione.equals("neOp")&&binaryOP.getExpr1().getType().contains("string")||binaryOP.getExpr2().getType().contains("string")) {
                return espressione = "strcmp("+expr1+","+expr2+")!=0";
            }
            espressione = expr1 + tipoEspressione + expr2;
            return espressione;
        }

        if (tipoOperazione.equals("andOp") | tipoOperazione.equals("orOp")) {

            espressione = expr1 + tipoEspressione + expr2;
            return espressione;
        }



        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {
        String constante = "";
        if (constOp.getType().equals("integer_const")) {
            constante = constOp.getValue();
        }
        if (constOp.getType().equals("real_const")) {
            constante = constOp.getValue();
        }

        if (constOp.getType().equals("string_const")) {
            constante =  constOp.getValue() ;
        }
        if (constOp.getType().equals("boolean")) {
            constante = constOp.getValue();
        }

        return constante;
    }

    @Override
    public Object visit(ExprOp exprOp) throws Exception {
        String type = null;
        if (exprOp instanceof ConstOp) {
            type = (String) exprOp.accept(this);
        }

        if (exprOp instanceof BinaryOP) {
            type = (String) exprOp.accept(this);
        }

        if (exprOp instanceof Identifier) {
            type = (String) exprOp.accept(this);

        }

        if (exprOp instanceof UnaryOp) {
            type = (String) exprOp.accept(this);
        }

        return type;
    }

    @Override
    public Object visit(FunCallOp funCallOp) throws Exception {
        String functionName = funCallOp.getId().getId();

        // Ottieni gli argomenti della funzione
        List<ExprOp> arguments = funCallOp.getExprsList();
        List<String> argumentStrings = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        // Genera il codice per ciascun argomento
        if (funCallOp.getExprsList()!=null) {
            for (int i = 0; i < arguments.size(); i++) {
                ExprOp argument = arguments.get(i);

                if (argument instanceof ConstOp) {
                    String argumentCode = (String) argument.accept(this);
                    String t = ((ConstOp) argument).getValue().replace("\"", "");
                    builder.append(argumentCode);

                    if (i < arguments.size() - 1) {
                        builder.append(",");
                    }
                } else if (argument instanceof BinaryOP) {
                    String res = (String) argument.accept(this);
                    builder.append(res);
                } else {
                    String res = (String) argument.accept(this);
                    builder.append(res);

                    if (i < arguments.size() - 1) {
                        builder.append(",");
                    }
                }
            }}
       /* if (builder.length()>0) {
        builder.replace(builder.length()-1,builder.length(),"");}*/
        // Genera il codice per la chiamata di funzione
        String functionCallCode = functionName + "(" + String.join(", ", builder) + ")";

        // Restituisci il codice generato
        return functionCallCode;
    }


    @Override
    public Object visit(Identifier identifier) throws Exception {

        SymbolTable tmp = currentScope;

        while (currentScope != null) {
            if (currentScope.lookUp(identifier.getId()) != null) {
                Row r = currentScope.lookUp(identifier.getId());
                String type = r.getType().toString();
                return identifier.getId();
            } else {
                currentScope = currentScope.getFather();
            }
        }
        currentScope = tmp;
        return null;
    }

    @Override
    public Object visit(IoArgs ioArgs) {
        return null;
    }

    @Override
    public Object visit(IoArgsConcat ioArgsConcat) {
        return null;
    }

    @Override
    public Object visit(ProcCallOp procCallOp) throws Exception {
        writer.write(procCallOp.getId().getId() + "(");
        if (procCallOp.getExprsList() != null) {
            int exprCount = procCallOp.getExprsList().size();
            Row out = currentScope.lookUp(procCallOp.getId().getId());
            FieldType.TypeFunction typefield = (FieldType.TypeFunction) out.getType();
            ArrayList<String> normalOutParams = typefield.getOutputParams();
            for (int i = 0; i < exprCount; i++) {
                ExprOp e = procCallOp.getExprsList().get(i);
                String t = (String) e.accept(this);
                String tipo = "";
                if (e instanceof Identifier) {
                tipo = typeVisit((Identifier) e);

                }
                if (normalOutParams.get(i).equals("NORMAL")||tipo.contains("string")) {
                    writer.write(t);
                }else {
                    writer.write("&"+t);
                }
                // Verifica se l'iterazione corrente non è l'ultima
                if (i < exprCount - 1) {
                    writer.write(",");
                }
            }
        }
        writer.write(");\n");
        return null;
    }

    ArrayList<String> outParam = new ArrayList<>();
    @Override
    public Object visit(ProcParams procParams) {
        String parametro = "";
        if (procParams.getId().getValue().equals("OUT")) {
            outParam.add(procParams.getId().getId());
            if (procParams.getType().getType().equals("string")) {
                parametro = convertType(procParams.getType().getType())+" "+procParams.getId().getId();
                return  parametro;
            }
            parametro = convertType(procParams.getType().getType())+"* "+procParams.getId().getId();
        }
        else {
            parametro = convertType(procParams.getType().getType())+" "+procParams.getId().getId();
        }
        return parametro;
    }

    @Override
    public Object visit(UnaryOp unaryOp) throws Exception {
        String espressione = "";
        String expr = (String) unaryOp.getExprOp().accept(this);
        String tipoOperazione = unaryOp.getType();
        String operazione = convertTypeOp(tipoOperazione);

        if (tipoOperazione.equals("unaryMinusOp") || tipoOperazione.equals("notOp")) {
            espressione = operazione + "(" + expr + ")";
            return espressione;
        }
        return null;
    }

    @Override
    public Object visit(ElifOp elifOp) throws Exception {
        currentScope = elifOp.getElifTable();
        String condizione = (String) elifOp.getExprOp().accept(this);

        if (elifOp.getBodyOp() != null) {
            writer.write(" else if (" + condizione + ") {\n");
            elifOp.getBodyOp().accept(this);
        }
        writer.write("}\n");
        currentScope = elifOp.getElifTable().getFather();
        return null;
    }

    @Override
    public Object visit(IfOp ifOp) throws Exception {
        currentScope = ifOp.getTable();
        String condizione = (String) ifOp.getExprOpStat().accept(this);

        writer.write("if("+condizione+") {\n");
        if (ifOp.getBodyOpIf()!=null) {
            ifOp.getBodyOpIf().accept(this);
            writer.write("}\n ");
        }

        if (ifOp.getElifOps()!=null) {
            for (ElifOp e: ifOp.getElifOps()) {
                e.accept(this);
            }
        }

        if (ifOp.getElseBody().getBody()!=null) {
            writer.write(" else {\n");
            currentScope = ifOp.getElseTable();
            ifOp.getElseBody().getBody().accept(this);
            writer.write("}\n");
            currentScope = ifOp.getElseTable().getFather();
        }

        currentScope = ifOp.getTable().getFather();
        return null;
    }
    int nStruct = 0;
    @Override
    public Object visit(Stat stat) throws Exception {
        StringBuilder builder = new StringBuilder();

        if (stat.getValue().equals("RETURN")) {
            for (ExprOp e : stat.getExprs()) {
                String t = (String) e.accept(this);

                writer.write("return " + t+ ";\n" );
            }

        }

        if (stat.getValue() != null && stat.getValue().equals("WRITE")) {
            String toAppend = "";
            if (stat.getExprs() != null) {
                // Inizia la costruzione della printf
                builder.append("printf(\"");

                for (ExprOp e : stat.getExprs()) {

                    if (e instanceof Identifier) {
                        String type = typeVisit((Identifier) e);
                        if (type.contains("integer")) {
                            builder.append("%d");
                        } else if (type.contains("string")) {
                            builder.append("%s");
                        } else if (type.contains("real")) {
                            builder.append("%f");
                        }
                    } else if (e instanceof ConstOp) {
                        // Aggiungi il valore costante direttamente
                        String t = ((ConstOp) e).getValue().replace("\"","");
                        builder.append(t).append(" ");

                    } else if (e instanceof BinaryOP) {
                        // Aggiungi l'espressione binaria valutata

                        String binaryExpr = (String) e.accept(this);

                        writer.append("");
                        if (e.getType()!=null) {

                        if (e.getType().equals("integer")) {
                        builder.append("%d ");}
                        if (e.getType().equals("real")) {
                            builder.append("%f ");}
                        if (e.getType().equals("string")) {
                            builder.append("%s ");}}
                    } else if (e instanceof FunCallOp) {
                            Row r = currentScope.lookUp(((FunCallOp) e).getId().getId());
                            FieldType.TypeFunction type = (FieldType.TypeFunction) r.getType();
                            String tipo = type.getOutputParams().get(0);
                            if (tipo.contains("integer")) {
                                builder.append("%d ");
                            }
                        if (tipo.contains("real")) {
                            builder.append("%f ");
                        }
                        if (tipo.contains("string")) {
                            builder.append("%s ");
                        }


                    } else {
                        // Altri casi di espressioni
                        System.out.println("sassi");
                         toAppend = (String) e.accept(this);
                        builder.append(toAppend).append(" ");
                    }
                }

                // Aggiungi l'operatore di ritorno a capo
                    builder.append("\"");
                if (stat.getExprs().isEmpty()) {
                    writer.write(");\n");
                }else {
                // Aggiungi gli argomenti per la printf
                for (ExprOp e : stat.getExprs()) {
                    if (e instanceof Identifier || e instanceof BinaryOP|| e instanceof FunCallOp) {
                        builder.append(", ").append((String) e.accept(this));
                    }
                }

                    builder.append(");\n");
                }


                writer.write(String.valueOf(builder));
            }

        }

        if (stat.getValue() != null && stat.getValue().equals("WRITERETURN")) {

            if (stat.getExprs() != null&& !stat.getExprs().isEmpty()) {
                builder.append("printf(\"");
                // Inizia la costruzione della printf
                for (ExprOp e : stat.getExprs()) {

                    if (e instanceof Identifier) {
                        String type = typeVisit((Identifier) e);
                        if (type.contains("integer")) {
                            builder.append("%d");
                        } else if (type.contains("string")) {
                            builder.append("%s");
                        } else if (type.contains("real")) {
                            builder.append("%f");
                        }
                    } else if (e instanceof ConstOp) {
                        // Aggiungi il valore costante direttamente
                        String t = ((ConstOp) e).getValue().replace("\"","");
                        builder.append(t).append(" ");
                    } else if (e instanceof BinaryOP) {
                        // Aggiungi l'espressione binaria valutata

                        String binaryExpr = (String) e.accept(this);
                        writer.append("");
                        builder.append("%d ");
                    }
                    else if (e instanceof FunCallOp) {
                        Row r = currentScope.lookUp(((FunCallOp) e).getId().getId());
                        FieldType.TypeFunction type = (FieldType.TypeFunction) r.getType();
                        String tipo = type.getOutputParams().get(0);
                        if (tipo.contains("integer")) {
                            builder.append("%d ");
                        }
                        if (tipo.contains("real")) {
                            builder.append("%f ");
                        }
                        if (tipo.contains("string")) {
                            builder.append("%s ");
                        }

                    }else {
                        // Altri casi di espressioni
                        String toAppend = (String) e.accept(this);
                        builder.append(toAppend).append(" ");
                    }
                }

                // Aggiungi l'operatore di ritorno a capo
                builder.append("\\n\"");
                if (stat.getExprs().isEmpty()) {
                    writer.write("\\n);\n");
                }else {
                    // Aggiungi gli argomenti per la printf
                    for (ExprOp e : stat.getExprs()) {
                        if (e instanceof Identifier || e instanceof BinaryOP|| e instanceof FunCallOp) {
                            builder.append(", ").append((String) e.accept(this));
                        }
                    }

                    builder.append(");\n");
                }


                writer.write(String.valueOf(builder));
            }else {
                writer.write("printf(\"\\n\");\n");
            }

        }


        if (stat.getValue() != null && stat.getValue().equals("READ")) {
            if (stat.getExprs() != null && stat.getExprs().size() > 0) {
                ArrayList<ExprOp> expr = stat.getExprs();
                for (int i = 0; i< expr.size(); i++) {
                    if (expr.get(i) instanceof ConstOp) {
                        String t = (String) expr.get(i).accept(this);
                        writer.write("printf("+t+");\n");
                        if (expr.get(i+1) instanceof Identifier ) {
                            String tipo = typeVisit((Identifier) expr.get(i+1));
                            if (tipo.equals("integer")||tipo.equals("integer_const")) {
                                writer.write("scanf(\"%d\",");
                                if (((Identifier) expr.get(i+1)).getValue().equals("OUT")) {
                                    writer.write(((Identifier) expr.get(i+1)).getId()+");\n");
                                }else{
                                    writer.write("&"+((Identifier) expr.get(i+1)).getId()+");\n");
                                }
                                i++;
                            } else if (tipo.contains("real")) {
                                writer.write("scanf(\"%f\",");
                                if (((Identifier) expr.get(i+1)).getValue().equals("OUT")) {
                                    writer.write(((Identifier) expr.get(i+1)).getId()+");\n");
                                }else{
                                    writer.write("&"+((Identifier) expr.get(i+1)).getId()+");\n");
                                }
                            i++;
                            }else if (tipo.contains("string")) {
                                writer.write("scanf(\"%s\",");
                                writer.write(((Identifier) expr.get(i+1)).getId()+");\n");
                                i++;
                        }}
                    }else {
                        if (expr.get(i) instanceof Identifier ) {
                            String tipo = typeVisit((Identifier) expr.get(i));
                            if (tipo.equals("integer")||tipo.equals("integer_const")) {
                                writer.write("scanf(\"%d\",");
                                if (((Identifier) expr.get(i)).getValue().equals("OUT")) {
                                    writer.write(((Identifier) expr.get(i)).getId()+");\n");
                                }else{
                                    writer.write("&"+((Identifier) expr.get(i)).getId()+");\n");
                                }
                                i++;
                            } else if (tipo.contains("real")) {
                                writer.write("scanf(\"%f\",");
                                if (((Identifier) expr.get(i)).getValue().equals("OUT")) {
                                    writer.write(((Identifier) expr.get(i)).getId()+");\n");
                                }else{
                                    writer.write("&"+((Identifier) expr.get(i)).getId()+");\n");
                                }
                                i++;
                            }else if (tipo.contains("string")) {
                                writer.write("scanf(\"%s\",");
                                writer.write(((Identifier) expr.get(i)).getId()+");\n");
                                i++;
                            }}
                    }
                }
            }
        }



        if (stat.getValue()!=null && stat.getValue().equals("ASSIGN")) {
            ArrayList<String> tipi = new ArrayList<>();
            StringBuilder assignBuilder = new StringBuilder();

            int n = 0;

            ArrayList<String> idString = new ArrayList<>();
            StringBuilder builder1 = new StringBuilder();
            if (stat.getIds()!=null) {
                for (Identifier i: stat.getIds()) {
                    String valore = typeVisit(i);
                    if (valore.contains("string")) {
                        idString.add(i.getId());
                        for (String r : daReallocare) {
                            if (r.equals(i.getId())) {
                                writer.write("free("+r+");\n");
                            }
                        }
                    }else {
                        idString.add(estraiIdentificatori(i.getId(),outParam));
                    }
                }}
            if(stat.getExprs()!=null) {
                for (ExprOp e : stat.getExprs()) {

                    if (e instanceof FunCallOp) {
                        String t = (String) e.accept(this);

                        Row r = currentScope.lookUp(((FunCallOp) e).getId().getId());
                        Function f = (Function) r.getNode();
                        if (f.getTypes().size()>1) {
                            writer.write(((FunCallOp) e).getId().getId()+"_struct r"+nStruct +" = " + t +";\n");

                            for (int i = 0; i<f.getTypes().size(); i++) {
                                writer.write(idString.get(i)+" = "+"r"+nStruct+".result"+(i+1)+";\n");
                            }
                            if (!f.getTypes().isEmpty()) {
                                idString.subList(0, f.getTypes().size()).clear();
                            }
                            nStruct++;
                        } else if (f.getTypes().size()==1) {
                            boolean isChar = false;
                            for (String reall : daReallocare) {
                                if (reall.equals(idString.get(0))) {
                                    isChar = true;
                                    builder1.append(idString.get(0));
                                    assignBuilder.append(" = ");
                                    assignBuilder.append("strdup("+t+")\n");
                                }
                            }
                            if (!isChar) {
                                builder1.append(idString.get(0));
                                assignBuilder.append(" = ");
                                assignBuilder.append(t);
                            }
                        } else {
                            if (stat.getExprs().size()==1) {
                                boolean isChar = false;
                                for (String reall : daReallocare) {
                                    if (reall.equals(idString.get(0))) {
                                        isChar = true;
                                        builder1.append(idString.get(0));
                                        assignBuilder.append(" = ");
                                        assignBuilder.append("strdup("+t+")\n");
                                    }
                                }
                                if (!isChar) {
                                    builder1.append(idString.get(0));
                                    assignBuilder.append(" = ");
                                    assignBuilder.append(t);
                                }}

                        }
                    }else {

                        if (e instanceof Identifier) {
                            if (typeVisit((Identifier) e).contains("string")) {
                                String str = (String) e.accept(this);
                                str = estraiIdentificatori(str,outParam);
                                writer.write("strcpy("+idString.get(0)+","+str+");\n");
                                idString.remove(0);
                                n++;
                                continue;
                            }
                        }

                        assignBuilder.append(" = ");
                        String t = (String) e.accept(this);

                        if (e instanceof Identifier) {
                            t = estraiIdentificatori(t,outParam);
                            writer.write("strcpy");
                        }

                        boolean isChar = false;
                        builder1.append(idString.get(n));

                        for (String r : daReallocare) {
                            if (r.equals(idString.get(n))) {
                                isChar = true;
                                assignBuilder.append("strdup("+t+")");
                            }
                        }
                        if (!isChar) {
                            assignBuilder.append(t);}
                        idString.remove(0);
                        n++;

                    }
                    writer.write(String.valueOf(builder1));
                    if (assignBuilder.length()!=0) {
                        writer.write(assignBuilder +";\n");}
                    builder1.setLength(0);
                    assignBuilder.setLength(0);
                }


            }

        }

        if (stat instanceof WhileOp) {
            stat.accept(this);
        }

        if (stat instanceof ProcCallOp) {
            stat.accept(this);
        }

        if (stat instanceof IfOp) {
            stat.accept(this);
        }

        if (stat instanceof ElifOp) {
            stat.accept(this);
        }
        return null;
    }
    // Funzione per estrarre identificatori e aggiungere '*'
    public static String estraiIdentificatori(String inputString,ArrayList<String>arrayListID) {
        // Pattern per identificatori (sequenze di caratteri alfanumerici)
        Pattern pattern = Pattern.compile("\\b[a-zA-Z]\\w*\\b");
        Matcher matcher = pattern.matcher(inputString);

        // StringBuffer per costruire la nuova stringa
        StringBuffer result = new StringBuffer();

        // Trova e sostituisci ogni identificatore
        while (matcher.find()) {
            String identifier = matcher.group();

            // Verifica se l'identificatore è presente nell'ArrayList di ID
            if (arrayListID.contains(identifier)) {
                matcher.appendReplacement(result, "*" + identifier );
            } else {
                matcher.appendReplacement(result, identifier);
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }
    @Override
    public Object visit(WhileOp whileOp) throws Exception {
        currentScope = whileOp.getTable();
        String condizione = (String) whileOp.getExprOp().accept(this);
        condizione = estraiIdentificatori(condizione,outParam);
        writer.write("while (" + condizione + ") {\n");

        if (whileOp.getBodyOp() != null) {
            whileOp.getBodyOp().accept(this);
        }

        writer.write("}\n");


        currentScope = whileOp.getTable().getFather();

        return null;
    }


    @Override
    public Object visit(BodyOp bodyOp) throws Exception {
        if (bodyOp.getDecls()!=null) {
            for (Decls d: bodyOp.getDecls()) {
                d.accept(this);
            }
        }
            if (bodyOp.getStats()!=null) {
                ArrayList<Stat> reverse = bodyOp.getStats();
                Collections.reverse(reverse);
                for (Stat s : reverse) {
                    if (s.getValue()!=null) {
                        //Row r = currentScope.lookUp(currentScope.getScope());

                    if (s.getValue().equals("RETURN")) {
                       // s.accept(this);
                    }

                }s.accept(this);}
            }

        return null;
    }
    public String typeVisit(Identifier identifier) throws Exception {

        SymbolTable tmp = currentScope;

        while (currentScope != null) {
            if (currentScope.lookUp(identifier.getId()) != null) {
                Row r = currentScope.lookUp(identifier.getId());
                String type = r.getType().toString();

                return type;
            } else {
                currentScope = currentScope.getFather();
            }
        }
        currentScope = tmp;
        return null;
    }
    ArrayList<String> daReallocare = new ArrayList<>();
    @Override
    public Object visit(Decls decls) throws Exception {
        String tipo = null;
        ArrayList<String> tipi = new ArrayList<>();
        ArrayList<String> idString = new ArrayList<>();
        if (decls.getType() == null) {
            for (Identifier i : decls.getIds()) {

                tipi.add(typeVisit(i));
                idString.add(i.getId());
            }
            ArrayList<String> constant = new ArrayList<>();
            for (ConstOp c : decls.getConsts()) {
                constant.add(c.getValue());
            }
            Iterator<String> iter1 = idString.iterator();
            Iterator<String> iter2 = tipi.iterator();
            Iterator<String> iter3 = constant.iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                String id = iter1.next();
                String t = iter2.next();
                String c = iter3.next();


                if ( t.equals("integer_const") || t.equals("real_const") || t.equals("boolean")) {


                    writer.write(convertType(t) + " " + id + "=" + c + ";\n");
                }else if (t.equals("string_const") ) {
                    writer.write(convertType(t) + " " + id + "=" + "strdup("+c+")" + ";\n");
                    daReallocare.add(id);
                }
            }
            return null;
        }
        tipo = convertType(decls.getType().getType());

        ArrayList<Identifier> ids = decls.getIds();
        if (tipo.equals("char*")) {
        if (!ids.isEmpty()) {

            writer.write(tipo + " " + ids.get(0).getId());
            writer.write(" = (char *)malloc(100 * sizeof(char));\n");
        }
        for (int i = 1; i < ids.size(); i++) {
            writer.write(tipo + " " + ids.get(i).getId());
            writer.write(" = (char *)malloc(100 * sizeof(char));\n");
        }}else {
            if (!ids.isEmpty()) {
                writer.write(tipo + " " + ids.get(0).getId());
            }
            for (int i = 1; i < ids.size(); i++) {
                writer.write(", " + ids.get(i).getId());
            }
            writer.write(";\n");
        }



        return null;
    }

    @Override
    public Object visit(FuncParams funcParams) {
        String paramType = convertType(funcParams.getType().getType());
        String paramName = funcParams.getId().getId();

        return paramType + " " + paramName;


    }
    int i = 0;
    private void generateStruct(String structName, ArrayList<FuncParams> params) throws IOException {
        writer.write("struct " + structName + " {\n");

        // Definizione dei campi della struct
        for (FuncParams param : params) {
            String fieldType = convertType(param.getType().getType());
            String fieldName = param.getId().getId();
            writer.write("    " + fieldType + " " + fieldName + ";\n");
        }

        writer.write("};\n");
    }



    @Override
    public Object visit(Function function) throws Exception {
        currentScope = function.getTable();
        i++;

        // Se ci sono almeno due parametri, genera la struct
        if (function.getFunc() != null && function.getTypes().size() > 1) {
            writer.write("typedef struct " + function.getId().getId() + "_struct" + " {\n");

            ArrayList<String> paramList = new ArrayList<>();
            int c = 0;

            for (Type param : function.getTypes()) {
                c++;
                String fieldType = convertType(param.getType());
                String fieldName = "result" + c;
                writer.write("    " + fieldType + " " + fieldName + ";\n");
            }

            writer.write("} " + function.getId().getId() + "_struct;\n");

            // Controlla se il tipo di ritorno è uguale al tipo del primo parametro
            writer.write(function.getId().getId() + "_struct "+ function.getId().getId() +"(" );
        }

        if (function.getTypes().size()==1) {

            writer.write(convertType(String.valueOf(function.getTypes().get(0).getType())) +" "+ function.getId().getId() + "(");
        }
        // Gestisci i parametri della funzione
        if (function.getFunc() != null && !function.getFunc().isEmpty()) {
            ArrayList<String> paramList = new ArrayList<>();
            for (FuncParams param : function.getFunc()) {
                String paramInfo = (String) param.accept(this);
                paramList.add(paramInfo);
            }
            writer.write(String.join(", ", paramList));
        }

        writer.write(") {\n");
        //caso in cui ci sono piu tipi da restituire
        if (function.getTypes().size()>1) {
        writer.write("    " + function.getId().getId() + "_struct s" + i + ";\n");
            if (function.getBody() != null) {
                if (function.getBody().getDecls()!=null) {
                    for (Decls d : function.getBody().getDecls()) {
                        d.accept(this);
                    }
                }
            }

            for (Stat s : function.getBody().getStats()) {
                if (s instanceof ProcCallOp) {
                    s.accept(this);
                }
            }
            for (Stat s : function.getBody().getStats()) {

                if (s.getValue()!=null) {
                    if (s.getValue().equals("RETURN")) {
                        // Ottieni tutte le espressioni di ritorno e parametri della funzione
                        List<ExprOp> returnExprs = s.getExprs();
                        List<Type> returnTypes = function.getTypes();

                        // Assumendo che il numero di espressioni di ritorno e tipi sia lo stesso
                        for (int j = 0; j < returnExprs.size(); j++) {
                            ExprOp returnExpr = returnExprs.get(j);
                            Type returnType = returnTypes.get(j);

                            String exprString = (String) returnExpr.accept(this);
                            System.out.println(exprString+"sassi");
                            String fieldName = "result" + (j + 1);  // Aggiungi 1 per iniziare da "result1"

                            writer.write("    s" + i + "." + fieldName + " = " + exprString + ";\n");
                        }
                    }
                }

            }
            writer.write("return s"+i +";\n");
            writer.write("}\n");
            return null;
        }

        if (function.getBody() != null) {
            function.getBody().accept(this);
        }

        for(Stat s: function.getBody().getStats()) {
            if (s.getValue()!=null) {
                if (s.getValue().equals("RETURN")) {
                    // Ottieni tutte le espressioni di ritorno e parametri della funzione
                    List<ExprOp> returnExprs = s.getExprs();
                    if (returnExprs.get(0) instanceof Identifier) {
                        // writer.write("return "+((Identifier) returnExprs.get(0)).getId() +";\n"); }
                    }
                }}
        }

        writer.write("}\n");
        daReallocare.clear();
        currentScope = function.getTable().getFather();
        return null;
    }



Procedure pMain = null;
    @Override
    public Object visit(Iter iter) throws Exception {



        if (iter.getFunction()!=null) {
            iter.getFunction().accept(this);
        }

        if (iter.getProc()!=null) {
            Row proc = null;
            if (iter.getProc().getId().getId().equals("main")) {
                pMain = iter.getProc();

            }else {
                proc = (Row) iter.getProc().accept(this);
            }
        }
        return null;
    }

    @Override
    public Object visit(Procedure procedure) throws Exception {
        if (!procedure.getId().getId().equals("main")) {

            writer.write("void " + procedure.getId().getId() + "(");
        }

        currentScope = procedure.getTable();
        if (procedure.getId().getId().equals("main")) {
            writer.write("void main(int argc, char *argv[]) {\n");
        }

        if (procedure.getProcParams()!=null) {
            ArrayList<String> parametri = new ArrayList<>();
            for (ProcParams p : procedure.getProcParams()) {
                String parametro = (String) p.accept(this);
                parametri.add(parametro);
            }
            writer.write(String.join(", ",parametri));

        }
        if (!procedure.getId().getId().equals("main")) {
        writer.write(") {\n");}
        Stat assign = null;
        if (procedure.getBody() != null) {
            if (procedure.getBody().getStats()!=null) {
                for (Stat s : procedure.getBody().getStats()) {
                    if (!(s instanceof WhileOp) && !(s instanceof IfOp) && !(s instanceof ElifOp) && !(s instanceof ProcCallOp)&&s.getValue()==null) {
                        assign = s;
                    }
                }
            }
            procedure.getBody().accept(this);
        outParam.clear();
        }
        outParam.clear();
        daReallocare.clear();
        writer.write("}\n");
        return null;
    }

    @Override
    public Object visit(Program program) throws Exception {
        outFile = new File(FILE_NAME);

        if (!(new File("test_files" + File.separator + "c_out" + File.separator)).exists()) {

            new File(("test_files" + File.separator + "c_out" + File.separator)).mkdirs();
        }
        outFile = new File("test_files" + File.separator + "c_out" + File.separator + FILE_NAME);
        outFile.createNewFile();
        writer = new FileWriter(outFile);
        addBaseLibraries();
        addHelperFunctions();
        currentScope = program.getTable();

        ArrayList<Iter> iters = program.getIter();
        iters.addAll(program.getNoProc());

        for (Iter i : iters) {
            if (i.getDecls()!=null) {
                for (Decls d: i.getDecls()) {
                    d.accept(this);
                }
            }
        }

        if (!program.getNoProc().isEmpty()) {
            Row iterList;
            ArrayList<Iter> reverse = program.getNoProc();

            for (Iter i : program.getNoProc()) {
                iterList = (Row) i.accept(this);

            }}

        if (!program.getIter().isEmpty()) {

            Row iterList;
            for (Iter i : program.getIter()) {
                iterList = (Row) i.accept(this);

            }
        }


            if (program.getProc() != null) {
                Row proc = null;
                proc = (Row) program.getProc().accept(this);

            }
            if (pMain!=null) {
            pMain.accept(this);}
            writer.close();
            return null;
        }


    @Override
    public Object visit(Type type) {
        return null;
    }


    //librerie base di C
    public void addBaseLibraries() throws IOException {
        writer.write("#include <stdio.h>\n");
        writer.write("#include <stdlib.h>\n");
        writer.write("#include <string.h>\n");
        writer.write("#include <math.h>\n");
        writer.write("#include <unistd.h>\n");
        writer.write("#include <stdbool.h>\n");
        writer.write("#define MAXCHAR 512\n");
    }



    public void addHelperFunctions() throws IOException {
        writer.write("char* integer_to_str(int i){\n");
        writer.write("int length= snprintf(NULL,0,\"%d\",i);\n");
        writer.write("char* result=malloc(length+1);\n");
        writer.write("snprintf(result,length+1,\"%d\",i);\n");
        writer.write("return result;\n");
        writer.write("}\n");

        writer.write("char* real_to_str(float i){\n");
        writer.write("int length= snprintf(NULL,0,\"%f\",i);\n");
        writer.write("char* result=malloc(length+1);\n");
        writer.write("snprintf(result,length+1,\"%f\",i);\n");
        writer.write("return result;\n");
        writer.write("}\n");

        writer.write("char* char_to_str(char i){\n");
        writer.write("int length= snprintf(NULL,0,\"%c\",i);\n");
        writer.write("char* result=malloc(length+1);\n");
        writer.write("snprintf(result,length+1,\"%c\",i);\n");
        writer.write("return result;\n");
        writer.write("}\n");

        writer.write("char* bool_to_str(bool i){\n");
        writer.write("int length= snprintf(NULL,0,\"%d\",i);\n");
        writer.write("char* result=malloc(length+1);\n");
        writer.write("snprintf(result,length+1,\"%d\",i);\n");
        writer.write("return result;\n");
        writer.write("}\n");

        writer.write("char* str_concat(char* str1, char* str2){\n");
        writer.write("char* result=malloc(sizeof(char)*MAXCHAR);\n");
        writer.write("result=strcat(result,str1);\n");
        writer.write("result=strcat(result,str2);\n");
        writer.write("return result;}\n");

        writer.write("\n");
        writer.write("char* read_str(){\n");
        writer.write("char* str=malloc(sizeof(char)*MAXCHAR);\n");
        writer.write("scanf(\"%s\",str);\n");
        writer.write("return str;}\n");

        writer.write("\n");
        writer.write("int str_to_bool(char* expr){\n");
        writer.write("int i=0;\n");
        writer.write("if ( (strcmp(expr, \"true\")==0) || (strcmp(expr, \"1\"))==0 )\n");
        writer.write("i=1;\n");
        writer.write("if ( (strcmp(expr, \"false\")==0) || (strcmp(expr, \"0\"))==0 )\n");
        writer.write("i=0;\n");
        writer.write("return i;}\n");
    }
}
