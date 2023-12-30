package nodi.expr;

import nodi.statements.Stat;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class ProcCallOp extends Stat implements Visitable {
    private Identifier id;
    private ArrayList<ExprOp> exprsList;

    public ProcCallOp(Identifier id, ArrayList<ExprOp> procExprs) {//caso in cui non ci sono exprs
        super("PROCCALLOP");
        for(ExprOp e:procExprs) {
            super.add(e);
        }
        super.add(id);
        this.id = id;
        this.exprsList = procExprs;
    }

    public ProcCallOp(Identifier id) {
        super("ProcCall");
        super.add(id);
        this.id = id;
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
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}