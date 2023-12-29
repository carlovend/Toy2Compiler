package nodi;

import javax.swing.tree.DefaultMutableTreeNode;

public class Type extends DefaultMutableTreeNode {
    String type;

    public Type(String type) {
        super("Type");
        super.add(new DefaultMutableTreeNode(type));
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
