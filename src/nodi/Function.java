package nodi;

import nodi.expr.Identifier;
import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Function extends DefaultMutableTreeNode  implements Visitable {
    private Identifier id;
    private ArrayList<Type> types;
    private ArrayList<FuncParams> func;
    private SymbolTable table;
    private BodyOp body;

    public Function(Identifier id, ArrayList<FuncParams> func, ArrayList<Type> types, BodyOp body) {
        super("Function");
        super.add(id);
        for (FuncParams f :func) {
            super.add(f);
        }
        for (Type t:types) {
            super.add(t);
        }
        super.add(body);
        this.id = id;
        this.func = func;
        this.types = types;
        this.body = body;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public ArrayList<FuncParams> getFunc() {
        return func;
    }

    public void setFunc(ArrayList<FuncParams> func) {
        this.func = func;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    public BodyOp getBody() {
        return body;
    }

    public void setBody(BodyOp body) {
        this.body = body;
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
