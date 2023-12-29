package nodi.expr;

import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class ConstOp extends ExprOp implements Visitable {
    private String value; //sarebbe il lessemam
    private String type;

    public ConstOp(String type, String value){
        super(type+value);
        super.add(new DefaultMutableTreeNode(type));
        super.add(new DefaultMutableTreeNode(value));
        this.type=type;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ConstOp{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
