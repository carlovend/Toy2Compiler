package nodi.statements;

import nodi.expr.ExprOp;
import nodi.expr.Identifier;
import nodi.expr.IoArgs;
import nodi.expr.ProcCallOp;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Stat extends DefaultMutableTreeNode implements Visitable {
    private ArrayList<Identifier> ids;
    private IfOp ifOp;
    private WhileOp whileOp;
    private ArrayList<ExprOp>exprs;
    private ProcCallOp procCallOp;
    private IoArgs ioArgs;
    public Stat(ArrayList<Identifier> ids, ArrayList<ExprOp>exprs) {
        super("Assign");
        for (Identifier i:ids) {
            super.add(i);
        }
        for (ExprOp e : exprs) {
            super.add(e);
        }

        this.exprs = exprs;
    }

    public Stat(ProcCallOp procCallOp) {
        super("ProcCallStat");
        super.add(procCallOp);
        this.procCallOp = procCallOp;
    }


    public Stat(String value,ArrayList<ExprOp> exprs) {
        super(value);
        this.exprs = exprs;
        for (ExprOp e: exprs) {
            super.add(e);
        }
    }

    public Stat(String value,IoArgs ioArgs) {
        super(value);
        if (ioArgs!= null) {
            super.add(ioArgs);}
        this.ioArgs = ioArgs;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Stat(IfOp ifOp) {
        super("Stat");
        super.add(ifOp);
        this.ifOp = ifOp;
    }

    public Stat(WhileOp whileOp) {
        super("Stat");
        super.add(whileOp);
        this.whileOp = whileOp;
    }

    public Stat(String node) {
        super(node);
    }

    public ArrayList<Identifier> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Identifier> ids) {
        this.ids = ids;
    }

    public IfOp getIfOp() {
        return ifOp;
    }

    public void setIfOp(IfOp ifOp) {
        this.ifOp = ifOp;
    }

    public WhileOp getWhileOp() {
        return whileOp;
    }

    public void setWhileOp(WhileOp whileOp) {
        this.whileOp = whileOp;
    }

    public ArrayList<ExprOp> getExprs() {
        return exprs;
    }

    public void setExprs(ArrayList<ExprOp> exprs) {
        this.exprs = exprs;
    }

    public ProcCallOp getProcCallOp() {
        return procCallOp;
    }

    public void setProcCallOp(ProcCallOp procCallOp) {
        this.procCallOp = procCallOp;
    }

    public IoArgs getIoArgs() {
        return ioArgs;
    }

    public void setIoArgs(IoArgs ioArgs) {
        this.ioArgs = ioArgs;
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
