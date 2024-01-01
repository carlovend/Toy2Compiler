package nodi.expr;

import nodi.statements.Stat;

import javax.swing.tree.DefaultMutableTreeNode;

public class IoArgs extends Stat {

    private ExprOp exprOp;
    private IoArgs ioArgs;

    private IoArgsConcat ioArgsConcat;

    public IoArgs(ExprOp exprOp) {
        super("IoArgs");
        super.add(exprOp);

        this.exprOp = exprOp;
       // this.ioArgs = ioArgs;
    }

    public IoArgs(ExprOp ioArgsConcat, IoArgs ioArgs) {
        super("IoArgs");
        super.add(ioArgsConcat);
        if (ioArgs!=null) {
        super.add(ioArgs);}
        this.ioArgs = ioArgs;
        //this.ioArgsConcat = ioArgsConcat;
    }

    public void addExpr(ExprOp expr1) {
        super.add(expr1);
    }

    public void addConcat(IoArgsConcat strings) {
        super.add(strings);
    }


}
