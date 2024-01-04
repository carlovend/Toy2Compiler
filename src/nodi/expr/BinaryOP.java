package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

public class BinaryOP extends ExprOp implements Visitable {

    private ExprOp expr1, expr2;

    private String op;
    public BinaryOP(String type, ExprOp expr1, ExprOp expr2) {
        super(type);
        super.add(expr1);
        super.add(expr2);
        this.op = type;
        this.expr1=expr1;
        this.expr2=expr2;
    }

    public ExprOp getExpr1() {
        return expr1;
    }

    public ExprOp getExpr2() {
        return expr2;
    }

    public String toString() {return super.toString();}

    public String getOp() {
        return op;
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
