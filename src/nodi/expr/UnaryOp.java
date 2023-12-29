package nodi.expr;

import javax.swing.tree.MutableTreeNode;

public class UnaryOp extends ExprOp{

    private ExprOp exprOp;
    public UnaryOp(String type, ExprOp exprOp) {
        super(type);//type sarebbe il nome
        super.add(exprOp);
        this.exprOp = exprOp;
    }

    public UnaryOp(String unaryMinusOp, Object minus, ExprOp expr1) {
        super(unaryMinusOp);
        super.add((MutableTreeNode) minus);
        super.add(expr1);
    }


    public ExprOp getExprOp() {
        return exprOp;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
