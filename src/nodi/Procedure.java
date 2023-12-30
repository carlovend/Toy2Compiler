package nodi;

import nodi.expr.Identifier;
import nodi.expr.ProcParams;
import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Procedure extends DefaultMutableTreeNode implements Visitable {
    private ArrayList<ProcParams> procParams;
    private BodyOp body;
    private Identifier id;

    private SymbolTable table;

    public Procedure(Identifier id, ArrayList<ProcParams> procParams, BodyOp body) {
        super("Procedure");
        super.add(id);
        if (procParams!= null) {
           for (ProcParams p: procParams) {
               super.add(p);
           }
        }
        super.add(body);
        this.body = body;
        this.id =id;
        this.procParams = procParams;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    public ArrayList<ProcParams> getProcParams() {
        return procParams;
    }

    public void setProcParams(ArrayList<ProcParams> procParams) {
        this.procParams = procParams;
    }

    public BodyOp getBody() {
        return body;
    }

    public void setBody(BodyOp body) {
        this.body = body;
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
