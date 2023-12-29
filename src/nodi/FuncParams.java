package nodi;

import nodi.expr.Identifier;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class FuncParams extends DefaultMutableTreeNode implements Visitable {
    private Identifier id;
    private Type type;
    private FuncParams funcParams;

    public FuncParams(Identifier identifier, Type type) {
        super("FuncParams");
        super.add(identifier);
        super.add(type);
        this.id = identifier;
        this.type = type;
    }

    public FuncParams() {
        super("FuncParams");
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public FuncParams getFuncParams() {
        return funcParams;
    }

    public void setFuncParams(FuncParams funcParams) {
        this.funcParams = funcParams;
    }


    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
