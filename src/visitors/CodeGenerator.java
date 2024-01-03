package visitors;

import nodi.*;
import nodi.expr.*;
import nodi.statements.ElifOp;
import nodi.statements.IfOp;
import nodi.statements.Stat;
import nodi.statements.WhileOp;
import tables.Row;
import tables.SymbolTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class CodeGenerator implements Visitor {

    private SymbolTable currentScope;
    public static String FILE_NAME;
    private static File outFile = new File("src/main/java/org/example/speriamo.c");
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

    @Override
    public Object visit(BinaryOP binaryOP) throws Exception {
        String espressione = "";
        String tipoEspressione = convertTypeOp(binaryOP.getType());

        String expr1 = (String) binaryOP.getExpr1().accept(this);
        String expr2 = (String) binaryOP.getExpr2().accept(this);

        String tipoOperazione = binaryOP.getType();

        if (tipoOperazione.equals("plusOp") || tipoOperazione.equals("minusOp") || tipoOperazione.equals("timesOp") || tipoOperazione.equals("divOp")) {
            espressione = expr1 + tipoEspressione + expr2;
            return espressione;
        }

        if (tipoOperazione.equals("gtOp") || tipoOperazione.equals("geOp") || tipoOperazione.equals("ltOp") || tipoOperazione.equals("leOp") || tipoOperazione.equals("eqOp") || tipoOperazione.equals("neOp")) {

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
        String functionName = funCallOp.getId().getId();  // Ottieni il nome della funzione

        // Ottieni gli argomenti della funzione
        List<ExprOp> arguments = funCallOp.getExprsList();
        List<String> argumentStrings = new ArrayList<>();

        // Genera il codice per ciascun argomento
        for (ExprOp argument : arguments) {
            String argumentCode = (String) argument.accept(this);
            argumentStrings.add(argumentCode);
        }

        // Genera il codice per la chiamata di funzione
        String functionCallCode = functionName + "(" + String.join(", ", argumentStrings) + ")";

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
        return null;
    }

    @Override
    public Object visit(ProcParams procParams) {
        String parametro = "";
        if (procParams.getId().getValue().equals("OUT")) {
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
        if (elifOp.getBodyOp() != null) {
            writer.write(" else if (" + (String) elifOp.getExprOp().accept(this) + ") {\n");
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

    @Override
    public Object visit(Stat stat) throws Exception {
        StringBuilder builder = new StringBuilder();
        if (stat.getValue() != null && stat.getValue().equals("WRITE")) {
            if (stat.getExprs() != null) {
                // Inizia la costruzione della printf
                builder.append("printf(");

                for (ExprOp e : stat.getExprs()) {
                    if (e instanceof Identifier) {
                        String type = typeVisit((Identifier) e);
                        if (type.equals("integer")) {
                            builder.append("\"%d\"");
                        } else if (type.equals("string")) {
                            builder.append("\"%s\"");
                        } else if (type.equals("real")) {
                            builder.append("\"%f\"");
                        }
                    } else if (e instanceof ConstOp) {
                        // Aggiungi il valore costante direttamente
                        builder.append(((ConstOp) e).getValue()).append(" ");
                    } else if (e instanceof BinaryOP) {
                        // Aggiungi l'espressione binaria valutata
                        String binaryExpr = (String) e.accept(this);
                        builder.append("\"%d\"");  // Puoi adattare questa parte in base al tipo dell'espressione binaria
                    } else {
                        // Altri casi di espressioni
                        String toAppend = (String) e.accept(this);
                        builder.append(toAppend).append(" ");
                    }
                }

                // Aggiungi l'operatore di ritorno a capo
                builder.append("");

                // Aggiungi gli argomenti per la printf
                for (ExprOp e : stat.getExprs()) {
                    if (e instanceof Identifier || e instanceof BinaryOP) {
                        builder.append(", ").append((String) e.accept(this));
                    }
                }

                builder.append(");\n");
            }
            writer.write(String.valueOf(builder));
        }


        if (stat.getValue() != null && stat.getValue().equals("READ")) {
            if (stat.getExprs() != null && stat.getExprs().size() > 0) {
                StringBuilder scanfFormat = new StringBuilder("scanf(\"");
                StringBuilder scanfArgs = new StringBuilder();

                for (ExprOp expr : stat.getExprs()) {
                    if (expr instanceof ConstOp) {
                        String t = (String) expr.accept(this);
                        writer.write("printf(" + t + ");\n");
                    } else if (expr instanceof Identifier) {
                        String type = typeVisit((Identifier) expr);
                        if (type.equals("integer_const")||type.equals("integer")) {
                            scanfFormat.append("%d ");
                        } else if (type.equals("real_const")||type.equals("real")) {
                            scanfFormat.append("%lf ");
                            if (((Identifier) expr).getValue().equals("OUT")) {
                                scanfArgs.append("").append(((Identifier) expr).getId()).append(" ");
                                continue;}
                        } else if (type.equals("string_const")||type.equals("string")) {
                            scanfFormat.append("%s ");
                            if (((Identifier) expr).getValue().equals("OUT")) {
                                scanfArgs.append("").append(((Identifier) expr).getId()).append(" ");
                                continue;}
                        }

                        // Aggiungi l'argomento per la scanf
                        scanfArgs.append("&").append(((Identifier) expr).getId()).append(" ");
                    }
                }

                // Completa la formattazione della scanf e scrivila nel file
                scanfFormat.append("\"").append(", ").append(scanfArgs).append(");\n");
                writer.write(String.valueOf(scanfFormat));
            }
        }



        if (!(stat instanceof WhileOp) && !(stat instanceof IfOp) && !(stat instanceof ElifOp) && !(stat instanceof ProcCallOp)) {
            ArrayList<String> tipi = new ArrayList<>();
            ArrayList<String> idString = new ArrayList<>();

            if (stat.getIds() != null && stat.getExprs() != null) {

                for (int i = 0; i < stat.getIds().size(); i++) {
                    Identifier identifier = stat.getIds().get(i);
                    ExprOp expression = stat.getExprs().get(i);

                    tipi.add(typeVisit(identifier));
                    idString.add(identifier.getId());
                    String d = (String) typeVisit(identifier);
                    d = convertType(d);
                    String t = (String) expression.accept(this);
                    if (expression instanceof FunCallOp) {
                        t = (String) expression.accept(this);
                    }

                    // Genera l'assegnamento
                    writer.write( d  + " " + identifier.getId() + " = " + t+ ";\n");
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

    @Override
    public Object visit(WhileOp whileOp) throws Exception {
        currentScope = whileOp.getTable();
        String condition = (String) whileOp.getExprOp().accept(this);
        writer.write("while (" + condition + ") {\n");

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
                    if (s.getValue().equals("RETURN")) {
                        continue;
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

                // Ora puoi controllare il tipo e generare il codice C di conseguenza
                if (t.equals("string_const") || t.equals("integer_const") || t.equals("real_const") || t.equals("boolean")) {
                    // Gestisci il caso in cui il tipo è una stringa

                    writer.write(convertType(t) + " " + id + "=" + c + ";\n");
                }
            }
            return null;
        }
        tipo = convertType(decls.getType().getType());

        ArrayList<Identifier> ids = decls.getIds();
        if (!ids.isEmpty()) {
            writer.write(tipo + " " + ids.get(0).getId());
        }

        for (int i = 1; i < ids.size(); i++) {
            writer.write(", " + ids.get(i).getId());
        }

        writer.write(";\n");

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

        // Restituisci il tipo della struct come tipo di ritorno della funzione
        writer.write("typedef struct " + function.getId().getId() + "_struct" + " {\n");

        // Gestisci i parametri della struct
        if (function.getFunc() != null && !function.getFunc().isEmpty()) {
            ArrayList<String> paramList = new ArrayList<>();
            int c = 0;
            for (Type param : function.getTypes()) {
                c++;
                String fieldType = convertType(param.getType());
                String fieldName = "result"+c;
                writer.write("    " + fieldType + " " + fieldName + ";\n");
            }
        }

        writer.write("} " + function.getId().getId() + "_struct;\n");

        // Genera la firma della funzione
        writer.write(function.getId().getId() + "_struct " + function.getId().getId() + "(");

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
        writer.write("    " + function.getId().getId() + "_struct s" + i + ";\n");

        if (function.getBody() != null) {
            function.getBody().accept(this);
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
                    String fieldName = "result" + (j + 1);  // Aggiungi 1 per iniziare da "result1"

                    writer.write("    s" + i + "." + fieldName + " = " + exprString + ";\n");
                }
            }} else {
                continue;
            }
        }

        writer.write("return s"+i +"\n");
        writer.write("}\n");

        currentScope = function.getTable().getFather();
        return null;
    }




    @Override
    public Object visit(Iter iter) throws Exception {
        if (iter.getDecls() != null) {
            ArrayList<Row> varList= new ArrayList<>();
            for (Decls d : iter.getDecls()) {
                varList = (ArrayList<Row>) d.accept(this);

            }
        }

        if (iter.getFunction()!=null) {
            iter.getFunction().accept(this);
        }

        if (iter.getProc()!=null) {
            Row proc = null;
            proc = (Row) iter.getProc().accept(this);
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
        if (procedure.getBody() != null) {
            procedure.getBody().accept(this);
        }

        writer.write("}\n");
        return null;
    }

    @Override
    public Object visit(Program program) throws Exception {
        writer = new FileWriter(outFile);
        addBaseLibraries();
        currentScope = program.getTable();


        if (program.getIter().size() > 0) {

            Row iterList;
            for (Iter i : program.getIter()) {
                iterList = (Row) i.accept(this);

            }
        }

        if (program.getNoProc().size() > 0) {
            Row iterList;
            for (Iter i : program.getNoProc()) {
                iterList = (Row) i.accept(this);

            }}


            if (program.getProc() != null) {
                Row proc = null;
                proc = (Row) program.getProc().accept(this);
            }


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
}
