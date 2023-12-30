package nodi.statements;

import nodi.BodyOp;
import nodi.expr.ExprOp;
import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

import java.util.ArrayList;

public class IfOp extends  Stat implements Visitable {

    private ExprOp exprOpStat;
    private BodyOp bodyOpIf;
    private ArrayList<ElifOp> elifOps;
    private BodyOp elseBody;

    private SymbolTable ifTable;
    private SymbolTable elseTable;

    private SymbolTable elifTable;


    public IfOp( ExprOp exprOpStat, BodyOp bodyOpIf, ArrayList<ElifOp> elifOps, BodyOp elseBody) {
        super("IfOp");
        super.add(exprOpStat);
        super.add(bodyOpIf);
        super.add(elseBody);
        for(ElifOp elifOp : elifOps){
            super.add(elifOp);
        }
        this.exprOpStat = exprOpStat;
        this.bodyOpIf = bodyOpIf;
        this.elifOps = elifOps;
        this.elseBody = elseBody;
    }

    public IfOp( ExprOp exprOpStat, BodyOp bodyOpIf, ArrayList<ElifOp> elifOps) {
        super("IfOp");
        super.add(exprOpStat);
        super.add(bodyOpIf);
        for(ElifOp elifOp : elifOps){
            super.add(elifOp);
        }
        this.exprOpStat = exprOpStat;
        this.bodyOpIf = bodyOpIf;
        this.elifOps = elifOps;
    }

    public IfOp( ExprOp exprOpStat, BodyOp bodyOpIf, BodyOp elseBody) {
        super("IfOp");
        super.add(exprOpStat);
        super.add(bodyOpIf);
        super.add(elseBody);

        this.exprOpStat = exprOpStat;
        this.bodyOpIf = bodyOpIf;
        this.elseBody = elseBody;

    }

    public IfOp( ExprOp exprOpStat, BodyOp bodyOpIf) {
        super("IfOp");
        super.add(exprOpStat);
        if (bodyOpIf!= null) {
        super.add(bodyOpIf);}

        this.exprOpStat = exprOpStat;
        this.bodyOpIf = bodyOpIf;
    }


    public SymbolTable getTable() {
        return this.ifTable;
    }

    public void setTable(SymbolTable table) {
        this.ifTable = table;
    }

    public SymbolTable getElseTable() {
        return elseTable;
    }

    public void setElseTable(SymbolTable elseTable) {
        this.elseTable = elseTable;
    }

    public BodyOp getBodyOpIf() {
        return bodyOpIf;
    }

    public void setBodyOpIf(BodyOp bodyOpIf) {
        this.bodyOpIf = bodyOpIf;
    }

    public ArrayList<ElifOp> getElifOps() {
        return elifOps;
    }

    public void setElifOps(ArrayList<ElifOp> elifOps) {
        this.elifOps = elifOps;
    }

    public BodyOp getElseBody() {
        return elseBody;
    }

    public void setElseBody(BodyOp elseBody) {
        this.elseBody = elseBody;
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    public SymbolTable getElifTable() {
        return elifTable;
    }

    public void setElifTable(SymbolTable elifTable) {
        this.elifTable = elifTable;
    }

    @Override
    public ArrayList<ExprOp> getExprs() {
        return super.getExprs();
    }

    public ExprOp getExprOpStat() {
        return exprOpStat;
    }

    public String toString() {
        return super.toString();
    }
}
