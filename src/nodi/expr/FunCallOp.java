package nodi.expr;

import nodi.statements.Stat;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FunCallOp extends ExprOp implements Visitable {
    private Identifier id;
    private ArrayList<ExprOp> exprsList;

    public FunCallOp(Identifier id) {//caso in cui non ci sono exprs
        super("FUNCALLOP");
        super.add(id);
        this.id = id;
    }

    // caso in cui ci sono le espressioni
    public FunCallOp(Identifier id, ArrayList<ExprOp> exprsList) {
        super("FUNCALLOP");
        for(ExprOp expr : exprsList) {
            super.add(expr);

        }
        super.add(id);
        this.id = id;
        this.exprsList = exprsList;
    }

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

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
