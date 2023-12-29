package nodi;

import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Iter extends DefaultMutableTreeNode implements Visitable {

    private Iter iter;
    private Function function;
    private Procedure proc;
    private SymbolTable table;

    private ArrayList<Decls> decls;

    public Iter(ArrayList<Decls> decls) {
        super("Iter");
        for (Decls d : decls) {
            super.add(d);
        }

        this.decls = decls;
    }

    public Iter( Function function) {
        super("Iter");
        super.add(function);
        if (iter!=null) {
            super.add(iter);}
        this.function = function;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }



    public Iter(Procedure proc, Iter iter) {
        super("Iter");
        super.add(proc);
        if (iter!=null) {
        super.add(iter);}
        this.iter = iter;
        this.proc = proc;
    }

    public Iter(Procedure proc) {
        super("Iter");
        super.add(proc);
        this.proc = proc;
    }



    public ArrayList<Decls> getDecls() {
        return decls;
    }

    public void setDecls(ArrayList<Decls> decls) {
        this.decls = decls;
    }

    public Iter getIter() {
        return iter;
    }

    public void setIter(Iter iter) {
        this.iter = iter;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Procedure getProc() {
        return proc;
    }




    public void addElementProc(Procedure proc) {
        super.add(proc);
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
