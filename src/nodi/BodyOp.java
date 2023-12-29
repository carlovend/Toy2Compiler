package nodi;


import nodi.statements.Stat;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class BodyOp extends DefaultMutableTreeNode implements Visitable {
    private VarDecl varDecl;

    private BodyOp body;
    private ArrayList<Stat> stats;
    private ArrayList<Decls> decls;

    public BodyOp(VarDecl vardecls) {
        super("BodyOp");
        super.add(vardecls);
       /* if (body!= null) {
        super.add((MutableTreeNode) body);
    }*/}


    public BodyOp(ArrayList<Stat> stat, ArrayList<Decls> decls) {
        super("BodyOp");
        for (Stat s : stat) {
            super.add(s);
        }
        for (Decls d: decls) {
            super.add(d);
        }
        this.stats = stat;
        this.decls = decls;
    }

    public BodyOp(String elseOp, BodyOp body) {
        super(elseOp);
        super.add(body);
        this.body = body;
    }

    public BodyOp() {

    }

    @Override
    public String toString() {
        return super.toString();
    }


private  ArrayList<VarDecl> varDeclArrayList = new ArrayList<>();
    public void addVarDecl(VarDecl vardecl) {
        super.add(vardecl);
        varDeclArrayList.add(vardecl);
    }




    public VarDecl getVarDecl() {
        return this.varDecl;
    }

    public void setVarDecl(VarDecl varDecl) {
        this.varDecl = varDecl;
    }



    public BodyOp getBody() {
        return body;
    }
    public ArrayList<Stat> getStats() {
        return stats;
    }

    public ArrayList<Decls> getDecls() {
        return decls;
    }

    public void setBody(BodyOp body) {
        this.body = body;
    }



    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
