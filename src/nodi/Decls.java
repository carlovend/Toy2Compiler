package nodi;



import nodi.expr.ConstOp;
import nodi.expr.Identifier;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Decls extends DefaultMutableTreeNode implements Visitable {
    private ArrayList<Identifier> ids;
    private Type type;
    private ArrayList<Decls> decls;
    String type1;
    private ArrayList<ConstOp> consts;


    public Decls(ArrayList<Identifier> ids, Type type, String type1) {
        super("DeclsOp");
        for (Identifier id: ids) {
            super.add(id);
        }
        super.add(type);
        super.add(new DefaultMutableTreeNode(type1));

        this.ids = ids;
        this.type = type;
        this.type1 = type1;
    }

    public ArrayList<Identifier> getIds() {
        return ids;
    }


    public Decls(ArrayList<Identifier> ids, ArrayList<ConstOp> consts, String type1) {
        super("DeclsOp");
        for (Identifier id: ids) {
            super.add(id);
        }
        for (ConstOp c : consts) {
            super.add(c);
        }
        super.add(new DefaultMutableTreeNode(type1));

        this.ids = ids;
        this.consts = consts;
        this.type1 = type1;
    }

    public String getType1() {
        return type1;
    }

    public ArrayList<ConstOp> getConsts() {
        return consts;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString()+getType();
    }


    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
