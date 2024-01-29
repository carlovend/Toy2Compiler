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

public class ScopeVisitor implements Visitor{

    private SymbolTable father = null;
    @Override
    public Object visit(BinaryOP binaryOP) throws Exception {
        binaryOP.getExpr1().accept(this);
        binaryOP.getExpr2().accept(this);
        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {

        String type = constOp.getType();
        if (type.equals("real_const")) {
            return "real";
        }

        if (type.equals("integer_const")) {
            return "integer";
        }

        if (type.equals("string_const")) {
            return "string";
        }

        if (type.equals("boolean")) {
            return "boolean";
        }

        return null;
    }

    @Override
    public Object visit(ExprOp exprOp) throws Exception {
        if (exprOp instanceof UnaryOp) {
            String t = (String) exprOp.accept(this);
            return false;
        }
        if (exprOp instanceof Identifier) {
            String t = (String) exprOp.accept(this);
            return true;
        }
        if (exprOp instanceof ConstOp) {
         String t = (String) exprOp.accept(this);
            return false;
        }
        if (exprOp instanceof BinaryOP) {
            String t = (String) exprOp.accept(this);
            return false;
        }


        return null;
    }

    @Override
    public Object visit(FunCallOp funCallOp) throws Exception {
        if (funCallOp.getExprsList()!=null) {
            for (ExprOp e : funCallOp.getExprsList()) {
                e.accept(this);
            }
        }
        return null;
    }

    @Override
    public Object visit(Identifier identifier) throws Exception {
        SymbolTable tmp = father;
        while (father!=null) {
        if (father.lookUp(identifier.getId())!=null) {
            return true;
        }else {
            father = father.getFather();}
        }
        father = tmp;
        throw new Exceptions.NoDeclaration(identifier.getId());
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
        if (procCallOp.getExprsList()!=null) {
        if (procCallOp.getExprsList().size()>0) {
            for (ExprOp e : procCallOp.getExprsList()) {
                    e.accept(this);
            }}
        }
        return null;
    }

    @Override
    public Object visit(ProcParams procParams) {
        ArrayList<Row> rows = new ArrayList<>();
        if (procParams != null) {
            Row r = new Row(procParams.getId().getId(),ProcParams.class,new FieldType.TypeVar(procParams.getType().getType()),"");
            rows.add(r);
        }
        return rows;

    }

    @Override
    public Object visit(UnaryOp unaryOp) throws Exception {
        unaryOp.getExprOp().accept(this);
        return null;
    }

    @Override
    public Object visit(ElifOp elifOp) throws Exception {

        elifOp.setElifTable(new SymbolTable());
        SymbolTable table = elifOp.getElifTable();
        table.setScope(elifOp.toString());
        table.setFather(father);

        if (elifOp.getBodyOp() != null) {
            father = elifOp.getElifTable();
            elifOp.getBodyOp().accept(this);
        }
        father = elifOp.getElifTable().getFather();
        return null;
    }

    @Override
    public Object visit(IfOp ifOp) throws Exception {
        SymbolTable tmp = father;

        ifOp.setTable(new SymbolTable());
        SymbolTable table = ifOp.getTable();
        table.setScope("If-Then");
        table.setFather(father);
        ifOp.setElseTable(new SymbolTable());
        SymbolTable elseTable = ifOp.getElseTable();
        elseTable.setScope("Else-Then");
        elseTable.setFather(ifOp.getTable());

        if (ifOp.getBodyOpIf() != null) {
            father = ifOp.getTable();
            ifOp.getBodyOpIf().accept(this);
        }

        if (ifOp.getElifOps() != null) {
            for (ElifOp e: ifOp.getElifOps()) {
                e.accept(this);
            }
        }

        if (ifOp.getElseBody().getBody() != null) {
            father = ifOp.getElseTable();

            ifOp.getElseBody().getBody().accept(this);
            father = ifOp.getElseTable().getFather();
        }

        return null;
    }

    @Override
    public Object visit(Stat stat) throws Exception {

        if (stat.getValue()!=null) {
            if (stat.getValue().equals("WRITE")||stat.getValue().equals("WRITERETURN")) {
                if (!stat.getExprs().isEmpty()) {
                    for (ExprOp e : stat.getExprs()) {
                        e.accept(this);
                    }
                }
                return null;
            }
            if (stat.getValue().equals("READ")) {
                for (ExprOp exprOp: stat.getExprs()) {

                    if (exprOp.isDollar()  && !exprOp.isId()) {
                        throw new Exceptions.OnlyId();
                    }
                    exprOp.accept(this);
                }
            return null;
            }
        }

        if (!(stat instanceof WhileOp) && !(stat instanceof IfOp) && !(stat instanceof ElifOp) && !(stat instanceof ProcCallOp)) {


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
        whileOp.setTable(new SymbolTable());
        SymbolTable table = whileOp.getTable();
        table.setScope("While");
        table.setFather(father);

        if (whileOp.getBodyOp() != null) {
            father = whileOp.getTable();
            whileOp.getBodyOp().accept(this);
        }
        father = whileOp.getTable().getFather();
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) throws Exception {

        if (bodyOp.getDecls()!=null) {
            ArrayList<Row> varList;
            for (Decls d:bodyOp.getDecls()) {

                varList = (ArrayList<Row>) d.accept(this);
                for (Row r:varList) {
                    father.addRow(r);
                }
            }
        }

         if (bodyOp.getStats() != null) {

             for (Stat s:bodyOp.getStats()) {
                 if (!(s instanceof WhileOp) && !(s instanceof IfOp) && !(s instanceof ElifOp) &&!(s instanceof ProcCallOp)) {
                     if (s.getIds() != null && !s.getIds().isEmpty() || s.getValue().equals("READ")||s.getValue().equals("WRITE")) {
                         s.accept(this);
                     }
                 }
                 if (s instanceof WhileOp) {
                     s.accept(this);
                 }
                 if (s instanceof IfOp) {
                     s.accept(this);
                 }
                 if (s instanceof ElifOp) {
                     s.accept(this);
                 }
                if (s instanceof ProcCallOp) {

                    s.accept(this);
                }
             }
         }

        return null;
    }

    @Override
    public Object visit(Decls decls) throws Exception {
        ArrayList<Row> rows = new ArrayList<>();
        int c = 0;

        if (decls != null && decls.getIds() != null) {
            for (Identifier id : decls.getIds()) {

                String type = null;
                if (decls.getType()!=null) {
                    type = decls.getType().getType();
                } else if (decls.getType1().equals("Assign")) {
                    if (decls.getIds().size()!=decls.getConsts().size()) {
                        throw new RuntimeException("Errore nell assign il numero dell assegnazione non coincide con gli id");
                    }
                        type = decls.getConsts().get(c).getType();
                        c++;
                }


                FieldType.TypeVar t = new FieldType.TypeVar(type);
                Row row = new Row(id.getId(),Decls.class,t,type);
                rows.add(row);
            }
        }

        return rows;
    }


    @Override
    public Object visit(FuncParams funcParams) {
        ArrayList<Row> rows = new ArrayList<>();
        if (funcParams != null) {
            Row r = new Row(funcParams.getId().getId(),FuncParams.class,new FieldType.TypeVar(funcParams.getType().getType()),funcParams.getType().getType());
            rows.add(r);
        }
        return rows;
    }

    @Override
    public Object visit(Function function) throws Exception {
        function.setTable(new SymbolTable());
        SymbolTable symbolTable = function.getTable();
        symbolTable.setFather(father);
        symbolTable.setScope(function.getId().getId()+function.toString());

        Row functionIdRow = new Row(function.getId().getId(), Function.class, new FieldType.TypeVar(function.getId().getValue()), "");
        symbolTable.addRow(functionIdRow);

        if (function.getFunc()!=null) {
            for (FuncParams f: function.getFunc()) {
                ArrayList<Row> procList = (ArrayList<Row>) f.accept(this);
                for (Row r: procList) {
                    symbolTable.addRow(r);
                }
            }
        }

        if (function.getBody() != null) {

            father = function.getTable();
            function.getBody().accept(this);
        }
        FieldType.TypeFunction t = new FieldType.TypeFunction();
        for (FuncParams f : function.getFunc()) {
            t.addInputParam(f.getType().getType());
        }
        for (Type type: function.getTypes()) {
            t.addOutputParam(type.getType());
        }
        father = symbolTable.getFather();
        return new Row(function.getId().getId(),function,t,"");
    }

    int c =0;
    @Override
    public Object visit(Iter iter) throws Exception {


        if (iter.getDecls() != null) {
            ArrayList<Row> varList= new ArrayList<>();
            for (Decls d : iter.getDecls()) {

                varList = (ArrayList<Row>) d.accept(this);
                for (Row r : varList) {
                    father.addRow(r);
                }
            }

        }
        if (iter.getFunction()!=null) {
            Row functionList;
                functionList = (Row) iter.getFunction().accept(this);

                    father.addRow(functionList);
        }

        if (iter.getProc()!=null) {
            Row proc = null;
            proc = (Row) iter.getProc().accept(this);
            father.addRow(proc);
        }
        FieldType t = new FieldType();

        return null;
    }

    @Override
    public Object visit(Procedure procedure) throws Exception {

        procedure.setTable(new SymbolTable());
        SymbolTable symbolTable = procedure.getTable();
        symbolTable.setFather(father);
        symbolTable.setScope(procedure.getId().getId()+procedure.toString());

        Row functionIdRow = new Row(procedure.getId().getId(), Function.class, new FieldType.TypeVar(procedure.getId().getValue()), "");
        symbolTable.addRow(functionIdRow);

        if (procedure.getProcParams()!=null) {
            for (ProcParams p: procedure.getProcParams()) {
                ArrayList<Row> procList = (ArrayList<Row>) p.accept(this);
                for (Row r: procList) {
                    symbolTable.addRow(r);
                }
            }
        }

        if (procedure.getBody() != null) {

            father = procedure.getTable();
            procedure.getBody().accept(this);
        }

        FieldType.TypeFunction t = new FieldType.TypeFunction();
        if (procedure.getProcParams()!=null) {
        for (ProcParams p : procedure.getProcParams()) {
            t.addInputParam(p.getType().getType());
            if (p.getId().getValue()!= null) {
                t.addOutputParam(p.getId().getValue());
            }
        }}
        father = symbolTable.getFather();
        return new Row(procedure.getId().getId(),procedure,t,"");
    }

    @Override
    public Object visit(Program program) throws Exception {

        program.setTable(new SymbolTable());
        SymbolTable symbolTable = program.getTable();
        symbolTable.setScope("Global");
        symbolTable.setFather(null);
        FieldType.TypeFunction t = new FieldType.TypeFunction();

        father = symbolTable;

        if (program.getIter().size()>0) {

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


        if (program.getProc()!=null) {
            Row proc = null;
            proc = (Row) program.getProc().accept(this);

            symbolTable.addRow(proc);
        }


        return null;
    }

    @Override
    public Object visit(Type type) {
        return null;
    }



}
