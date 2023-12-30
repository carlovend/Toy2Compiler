package visitors;

import nodi.*;
import nodi.expr.*;
import nodi.statements.ElifOp;
import nodi.statements.IfOp;
import nodi.statements.Stat;
import nodi.statements.WhileOp;
import tables.FieldType;
import tables.Row;
import tables.SymbolTable;

import java.util.ArrayList;

public class TypeVisitor implements Visitor {

    private SymbolTable currentScope;

    private static final String[][] combinazioniAritOp= { {"integer", "integer", "integer"},
            {"integer", "real", "real"},
            {"real", "integer", "real"},
            {"real", "real", "real"} };

    private static final String[][] combinazioniStrOp= { {"string", "string", "string"},
            {"string", "real","string"},
            {"string","integer","string"},
            {"integer","string","string"},
            {"real","string","string"},
            };

    private static final String[][] combinazioniBooleanOp= { {"boolean", "boolean", "boolean"} };

    private static final String[][] combinazioniRelOp= { {"integer", "integer", "boolean"},
            {"real", "real", "boolean"},
            {"integer", "real", "boolean"},
            {"real", "integer", "boolean"},
            {"string","string","boolean"},
            };

    private static final String[][] combinazioniMinus= { {"integer", "integer"},
            {"real", "real"} };


    private static final String[][] combinazioniNot= { {"boolean", "boolean"} };

    private static final String[][] compatibilita= {{"integer","integer"},
            {"real","real"},
            {"real","integer"},
            {"string","string"},
            {"boolean","boolean"},
            {"void","void"}};
    @Override
    public Object visit(BinaryOP binaryOP) throws Exception {
        String type1 = (String) binaryOP.getExpr1().accept(this);
        String type2 = (String) binaryOP.getExpr2().accept(this);

        String opType = binaryOP.getMode();
        if (opType.equals("plusOp")||opType.equals("minusOp")||opType.equals("timesOp")||opType.equals("divOp")) {
            for (String[] c: combinazioniAritOp) {
                if (type1.equals(c[0])&&type2.equals(c[1])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            return new Exception("errore nella binary");
        }

        if (opType.equals("andOp")||opType.equals("orOp")) {
            for (String[] c:combinazioniBooleanOp) {
                if (type1.equals(c[0])&&type2.equals(c[2])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exception("BooleanOp erroreeee");
        }

        if (opType.equals("gtOp")||opType.equals("geOp")||opType.equals("leOp")||opType.equals("ltOp")||opType.equals("eqOp")||opType.equals("neOp")) {
            for (String[] c: combinazioniRelOp) {
                if (type1.equals(c[0])&&type2.equals(c[1])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exception("Errore nelle relOp");
        }

        return new Exception("Binay non valida");
    }

    @Override
    public Object visit(ConstOp constOp) {
        String type = constOp.getType();
        if (type.equals("boolean_const")) {
            constOp.setType("boolean");
            return "boolean";
        }
        if (type.equals("integer_const")) {
            constOp.setType("integer");
            return "integer";
        }
        if (type.equals("real_const")) {
            constOp.setType("real");
            return "real";
        }
        if (type.equals("string_const")) {
            constOp.setType("string");
            return "string";
        }


        return null;
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

        return null;
    }

    @Override
    public Object visit(FunCallOp funCallOp) throws Exception {

        Row result = currentScope.lookUp(funCallOp.getId().getId());
        if (result == null) {
            throw new Exception("La funzione non esiste");
        }else {
            FieldType.TypeFunction dec = (FieldType.TypeFunction) result.getType();
            ArrayList<String> inParam = dec.getInputParams();
            int nParametri = inParam.size();
            int nParamChiam = 0;
            if (funCallOp.getExprsList() != null) {
                nParamChiam = funCallOp.getExprsList().size();

                if (nParametri != nParamChiam) {
                    throw new Exception("Il numero di parametri non coincide");
                }else {
                    for (int i = 0; i<nParamChiam; i++) {
                        ExprOp exprOp = funCallOp.getExprsList().get(i);
                        String tipo = (String) exprOp.accept(this);
                    }
                }
            }
        }


        return null;
    }

    @Override
    public Object visit(Identifier identifier) throws Exception {
        Row res = currentScope.lookUp(identifier.getId());
        if (res == null) {
            throw new Exception("Nessuna dichiarazione");
        }else {
            if (res.getType() instanceof FieldType.TypeFunction) {
                return ((FieldType.TypeFunction) res.getType()).getOutputParams().get(0);
            }else {
                identifier.setType((((FieldType.TypeVar) res.getType()).getType()));
                return ((FieldType.TypeVar) res.getType()).getType();
            }
        }

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
    public Object visit(ProcCallOp procCallOp) {
        return null;
    }

    @Override
    public Object visit(ProcParams procParams) {
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) throws Exception {

        String typeExpr = (String) unaryOp.getExprOp().accept(this);
        String type = unaryOp.getType();

        if (type.equals("unaryMinusOp")) {
            for (String[] c : combinazioniMinus) {
                if (typeExpr.equals(c[0])) {
                    unaryOp.setType(c[1]);
                    return c[1];
                }
            }
            throw new Exception("Errore nella minus");
        }
        if (type.equals("notOp")) {
            for (String[] c: combinazioniNot) {
                if (typeExpr.equals(c[0])) {
                    unaryOp.setType(c[1]);
                    return c[1];
                }
            }

            throw new Exception("Errore nella not");
        }
        return null;
    }

    @Override
    public Object visit(ElifOp elifOp) throws Exception {
        String condizione = (String) elifOp.getExprOp().accept(this);

        currentScope = elifOp.getElifTable();
        elifOp.getBodyOp().accept(this);
        currentScope = elifOp.getElifTable().getFather();

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) throws Exception {

        String condizione = (String) ifOp.getExprOpStat().accept(this);

        currentScope = ifOp.getTable();
        ifOp.getBodyOpIf().accept(this);
        currentScope = ifOp.getTable().getFather();

        if (ifOp.getElseBody() != null) {
            currentScope = ifOp.getElseTable();
            ifOp.getElseBody().accept(this);
            currentScope = ifOp.getTable().getFather();
        }

        return null;
    }

    @Override
    public Object visit(Stat stat) throws Exception {


        if (!(stat instanceof WhileOp) && !(stat instanceof IfOp) && !(stat instanceof ElifOp) && !(stat instanceof ProcCallOp)) {

            if (!stat.getExprs().isEmpty()) {
                if (stat.getExprs().get(0) instanceof FunCallOp) {
                    Row r = currentScope.lookUp(((FunCallOp) stat.getExprs().get(0)).getId().getId()) ;
                    Function f = (Function) r.getNode();
                    f.accept(this);
                }
            }

            for (Identifier i: stat.getIds()) {
                i.accept(this);
            }
            if (!stat.getExprs().isEmpty()) {
                if (stat.getExprs().get(0) instanceof FunCallOp) {
                    stat.getExprs().get(0).accept(this);
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

        String condizione = (String) whileOp.getExprOp().accept(this);

        if (!condizione.equals("boolean")) {
            throw new Exception("Condizione while non valida");
        }

        currentScope = whileOp.getTable();
        whileOp.getBodyOp().accept(this);

        currentScope = whileOp.getTable().getFather();
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) throws Exception {
        return null;
    }

    @Override
    public Object visit(Decls decls) throws Exception {

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
            Row functionList;
            functionList = (Row) iter.getFunction().accept(this);
        }

        if (iter.getProc()!=null) {
            Row proc = null;
            proc = (Row) iter.getProc().accept(this);
        }

        return null;
    }

    @Override
    public Object visit(Procedure procedure) throws Exception {
        return null;
    }

    @Override
    public Object visit(Program program) throws Exception {

        currentScope = program.getTable();
        if (program.getProc()!=null) {
            Row proc = null;
            proc = (Row) program.getProc().accept(this);

        }

        if (program.getIter().size()>0) {
            System.out.println("PALLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            Row iterList;
            for (Iter i : program.getIter()) {
                iterList = (Row) i.accept(this);

            }
        }

        if (program.getNoProc().size()>0) {
            Row iterList;
            for (Iter i : program.getNoProc()) {
                iterList = (Row) i.accept(this);

            }
        }

        return null;
    }

    @Override
    public Object visit(Type type) {
        return null;
    }
}
