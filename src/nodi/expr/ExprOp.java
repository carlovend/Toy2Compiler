package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class ExprOp extends DefaultMutableTreeNode implements Visitable {

    private String mode;
    public ExprOp(String expr) {
        super(expr);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
