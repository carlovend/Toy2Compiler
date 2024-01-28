package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.MutableTreeNode;

public class UnaryOp extends ExprOp implements Visitable {

    private ExprOp exprOp;
    private String type;
    public UnaryOp(String type, ExprOp exprOp) {
        super(type);//type sarebbe il nome
        super.add(exprOp);
        this.type = type;
        this.exprOp = exprOp;
    }


    public void setExprOp(ExprOp exprOp) {
        this.exprOp = exprOp;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public ExprOp getExprOp() {
        return exprOp;
    }

    @Override
    public String toString() {
        return String.valueOf(exprOp);
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
