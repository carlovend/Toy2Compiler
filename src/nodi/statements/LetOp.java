package nodi.statements;

import jdk.nashorn.internal.ir.Symbol;
import nodi.Decls;
import nodi.VarDecl;
import tables.SymbolTable;
import visitors.Visitable;
import visitors.Visitor;

import java.util.ArrayList;

public class LetOp extends Stat implements Visitable {

    private ArrayList<Decls> vardecl;
    private GoWhen goWhen;

    SymbolTable symbolTable;

    public LetOp( ArrayList<Decls> vardecl, GoWhen goWhen) {
        super("LETOP");
        super.add(goWhen);
        for (Decls d : vardecl) {
            super.add(d);
        }
        this.vardecl = vardecl;
        this.goWhen = goWhen;
    }

    public ArrayList<Decls> getVardecl() {
        return vardecl;
    }

    public void setVardecl(ArrayList<Decls> vardecl) {
        this.vardecl = vardecl;
    }

    public GoWhen getGoWhen() {
        return goWhen;
    }

    public void setGoWhen(GoWhen goWhen) {
        this.goWhen = goWhen;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
