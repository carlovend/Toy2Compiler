package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class Identifier extends ExprOp implements Visitable {
    private String id; //sarebbe il lessemam
    private String value;
    public Identifier(String id, String value){
        super("Identifier");
        super.add(new DefaultMutableTreeNode(id));
        super.add(new DefaultMutableTreeNode(value));
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return super.toString();
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
