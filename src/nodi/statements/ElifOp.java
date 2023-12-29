package nodi.statements;

import nodi.BodyOp;
import nodi.expr.ExprOp;
import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

public class ElifOp extends Stat implements Visitable {
    private ExprOp exprOp;
    private BodyOp bodyOp;

    private SymbolTable elifTable;
    public ElifOp(ExprOp exprOp, BodyOp bodyOp) {
        super("ElifOp");
        super.add(exprOp);
        if (bodyOp!=null) {
        super.add(bodyOp);}

        this.exprOp = exprOp;
        this.bodyOp = bodyOp;
    }

    public SymbolTable getElifTable() {
        return elifTable;
    }

    public void setElifTable(SymbolTable elifTable) {
        this.elifTable = elifTable;
    }

    public ExprOp getExprOp() {
        return exprOp;
    }

    public void setExprOp(ExprOp exprOp) {
        this.exprOp = exprOp;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
