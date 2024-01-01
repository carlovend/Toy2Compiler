package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class ExprOp extends DefaultMutableTreeNode implements Visitable {

    private String mode;
    private String type;
    boolean isId = false;
    public ExprOp(String expr) {
        super(expr);
    }
    private boolean dollar = false;


    public void setIsId() {
        this.isId = true;
    }
    public void setDollar() {
        this.dollar = true;
    }

    public boolean isDollar() {
        return this.dollar;
    }
    public boolean isId() {
        return isId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
