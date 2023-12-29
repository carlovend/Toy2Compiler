package nodi.expr;

import nodi.Type;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class ProcParams extends DefaultMutableTreeNode implements Visitable{

    private Identifier id;
    private Type type;
    private ProcParams procs;

    public ProcParams(Identifier id, Type type, ProcParams procs) {
        super("ProcParams");
        super.add(id);
        super.add(type);
        if (procs!=null) {
            super.add(procs);
        }
        this.id = id;
        this.type = type;
        this.procs = procs;
    }

    public ProcParams(Identifier id, Type type) {
        super("ProcParams");
        super.add(id);
        super.add(type);
        this.id = id;
        this.type = type;
    }

    public Identifier getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public ProcParams() {
        super("ProcParams");
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
