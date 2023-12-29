package nodi.expr;

import java.util.ArrayList;

public class ProcExprOp extends ExprOp {

    private ArrayList<ProcExprOp> exprs;
    private Identifier refId;
    public ProcExprOp(Identifier refId, ArrayList<ProcExprOp> exprs) {
        super("ProcExpr");
        super.add(refId);

        this.refId = refId;
        this.refId.setValue("@" + refId.getValue());

        for (ProcExprOp procExpr : exprs) {
            super.add(procExpr);
        }
    }

    public ProcExprOp(Identifier refId) {
        super("ProcExpr");
        super.add(refId);
        this.refId = refId;
        this.refId.setValue("@" + refId.getValue());
    }


    public ProcExprOp(ExprOp exprOp, ProcExprOp procExprOps) {
        super("ProcExpr");
        super.add(exprOp);
        super.add(procExprOps);

    }

    public ProcExprOp(ExprOp expr1) {
        super("ProcExpr");
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
