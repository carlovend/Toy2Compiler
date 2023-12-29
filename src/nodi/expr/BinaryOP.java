package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

public class BinaryOP extends ExprOp implements Visitable {

    private ExprOp expr1, expr2;


    public BinaryOP(String type, ExprOp expr1, ExprOp expr2) {
        super(type);
        super.add(expr1);
        super.add(expr2);
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

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
