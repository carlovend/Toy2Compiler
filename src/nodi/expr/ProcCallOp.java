package nodi.expr;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class ProcCallOp extends DefaultMutableTreeNode {
    private Identifier id;
    private ArrayList<ExprOp> exprsList;

    public ProcCallOp(Identifier id, ArrayList<ExprOp> procExprs) {//caso in cui non ci sono exprs
        super("PROCCALLOP");
        for(ExprOp e:procExprs) {
            super.add(e);
        }
        super.add(id);
        this.id = id;
    }

    public ProcCallOp(Identifier id) {
        super("ProcCall");
        super.add(id);
    }

    // caso in cui ci sono le espressioni
    /*public ProcCallOp(Identifier id, ArrayList<ExprOp> exprsList) {
        super("PROCCALLOP");
        for(ExprOp expr : exprsList) {
            super.add(expr);
            String mode = expr.getMode();
            super.add(new DefaultMutableTreeNode(mode));
        }
        super.add(id);
        this.id = id;
    }*/

    public Identifier getId(){
        return this.id;
    }

    public ArrayList<ExprOp> getExprsList(){
        return this.exprsList;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}