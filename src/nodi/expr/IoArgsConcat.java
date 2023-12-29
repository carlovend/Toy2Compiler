package nodi.expr;

import java.util.List;

public class IoArgsConcat extends ExprOp {
    public IoArgsConcat(List<ExprOp> expressions) {
        super("IoArgsConcat");
        for (ExprOp e: expressions) {
            super.add(e);
        }

    }

    public IoArgsConcat(ConstOp stringConst) {
        super("IoArgsConcat");
        super.add(stringConst);
    }

    public IoArgsConcat(ExprOp concat1, ExprOp concat2) {
        super("IoARGSconcat");
    }
}
