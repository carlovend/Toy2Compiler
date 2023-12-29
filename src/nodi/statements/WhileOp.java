package nodi.statements;

import nodi.BodyOp;
import nodi.expr.ExprOp;
import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

public class WhileOp extends Stat implements Visitable {

    private ExprOp exprOp;
    private BodyOp bodyOp;
    private SymbolTable table;
    public WhileOp( ExprOp exprOp, BodyOp bodyOp) {
        super("WhileOp");
        super.add(exprOp);
        if (bodyOp!=null) {
        super.add(bodyOp);}

        this.exprOp = exprOp;
        this.bodyOp = bodyOp;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    public SymbolTable getTable() {
        return this.table;
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
