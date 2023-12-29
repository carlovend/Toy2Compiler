package visitors;

import nodi.*;
import nodi.expr.*;
import nodi.statements.ElifOp;
import nodi.statements.IfOp;
import nodi.statements.Stat;
import nodi.statements.WhileOp;

public interface Visitor {

    public Object visit(BinaryOP binaryOP) throws Exception;
    public Object visit(ConstOp constOp);
    public Object visit(ExprOp exprOp) throws Exception;
    public Object visit(FunCallOp funCallOp);
    public Object visit(Identifier identifier) throws Exception;
    public Object visit(IoArgs ioArgs);
    public Object visit(IoArgsConcat ioArgsConcat);
    public Object visit(ProcCallOp procCallOp);
    //public Object visit(ProcExprOp procExprOp);
    public Object visit(ProcParams procParams);
    public Object visit(UnaryOp unaryOp) throws Exception;

    public Object visit(ElifOp elifOp) throws Exception;
    public Object visit(IfOp ifOp) throws Exception;
    public Object visit(Stat stat) throws Exception;
    public Object visit(WhileOp whileOp) throws Exception;
    public Object visit(BodyOp bodyOp) throws Exception;
    public Object visit(Decls decls) throws Exception;
    public Object visit(FuncParams funcParams);
    public Object visit(Function function) throws Exception;
    public Object visit(Iter iter) throws Exception;
    public Object visit(Procedure procedure) throws Exception;
    public Object visit(Program program) throws Exception;
    public Object visit(Type type);
}
