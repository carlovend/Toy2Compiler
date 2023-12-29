package nodi;

import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Program extends DefaultMutableTreeNode implements Visitable {
    private ArrayList<Iter> noProc;
    private Procedure proc;
    private ArrayList<Iter> iter;
    SymbolTable table;
    public Program(ArrayList<Iter> noProc, Procedure proc, ArrayList<Iter> iter) {
        super("Program");
        if (noProc!=null) {
            for (Iter i: noProc) {
                super.add(i);
            };}
        super.add(proc);
        if (iter != null) {
            for (Iter i: iter) {
                super.add(i);
            };}
        this.noProc = noProc;
        this.proc = proc;
        this.iter = iter;
    }

    public ArrayList<Iter> getNoProc() {
        return noProc;
    }

    public void setNoProc(ArrayList<Iter> noProc) {
        this.noProc = noProc;
    }

    public Procedure getProc() {
        return proc;
    }

    public void setProc(Procedure proc) {
        this.proc = proc;
    }

    public ArrayList<Iter> getIter() {
        return iter;
    }

    public void setIter(ArrayList<Iter> iter) {
        this.iter = iter;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
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
