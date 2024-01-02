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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

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
        System.out.println(expr2+"BANANE NEL CULO");
        String tipoOperazione = binaryOP.getType();

        if (tipoOperazione.equals("plusOp") || tipoOperazione.equals("minusOp") || tipoOperazione.equals("timesOp") || tipoOperazione.equals("divOp")) {
            espressione = expr1 + tipoEspressione + expr2;
            return espressione;
        }

        if (tipoOperazione.equals("gtOp") || tipoOperazione.equals("geOp") || tipoOperazione.equals("ltOp") || tipoOperazione.equals("leOp") || tipoOperazione.equals("eqOp") || tipoOperazione.equals("neOp")) {
            System.out.println(expr1+"DEMONIO IL SIGNORE");
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
            constante = "\"" + constOp.getValue() + "\"";
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
        return null;
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
        System.out.println(ifOp.getExprOpStat().toString()+"SONO LA CONDIZIONE");
        String condizione = (String) ifOp.getExprOpStat().accept(this);
        writer.write("if("+condizione+") {\n");
        System.out.println(currentScope.getScope()+"TIPREGOBASTAAAAAAAAAAAAAAAAAAAAAAAA");

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
                    s.accept(this);
                }
            }

        return null;
    }
    public String typeVisit(Identifier identifier) throws Exception {

        SymbolTable tmp = currentScope;
        System.out.println(currentScope.getScope()+"TIPREGOBASTA"+identifier.getValue());
        while (currentScope != null) {
            if (currentScope.lookUp(identifier.getId()) != null) {
                Row r = currentScope.lookUp(identifier.getId());
                String type = r.getType().toString();
                System.out.println(type+"DIOCANGONOLEPUTRIDO");
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
                System.out.println(i.getValue()+i.getType());
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
                System.out.println(id+t+c);
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
        return null;
    }

    @Override
    public Object visit(Function function) throws Exception {
        return null;
    }

    @Override
    public Object visit(Iter iter) throws Exception {
        if (iter.getDecls() != null) {
            ArrayList<Row> varList= new ArrayList<>();
            for (Decls d : iter.getDecls()) {
                System.out.println(iter.getDecls().size());
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
        writer.write("void " + procedure.getId().getId() + "(");

        if (procedure.getProcParams()!=null) {
            ArrayList<String> parametri = new ArrayList<>();
            for (ProcParams p : procedure.getProcParams()) {
                String parametro = (String) p.accept(this);
                parametri.add(parametro);
            }
            writer.write(String.join(", ",parametri));

        }
        writer.write(") {\n");
        currentScope = procedure.getTable();
        if (procedure.getId().getId().equals("main")) {
            writer.write("void main(int argc, char *argv[]) {\n");
        }

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
            System.out.println("PALLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
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
