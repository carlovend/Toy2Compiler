package nodi;

import visitors.Visitable;
import visitors.Visitor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class VarDecl extends DefaultMutableTreeNode  {
    private ArrayList<Decls> decls;
    public VarDecl(ArrayList<Decls> decls) {
        super("VarDecl");
        for (Decls d : decls) {
            super.add(d);
        }

        this.decls = decls;
    }

    public ArrayList<Decls> getDecls() {
        return decls;
    }

    public void setDecls(ArrayList<Decls> decls) {
        this.decls = decls;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
