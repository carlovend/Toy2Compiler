package nodi.statements;

import nodi.expr.ExprOp;
import visitors.Visitable;
import visitors.Visitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class GoWhen extends DefaultMutableTreeNode implements Visitable {
    private ExprOp condizione;
    private ArrayList<Stat> stats;
    private ArrayList<GoWhen> otherGo;
    private ArrayList<Stat> otherwise;


    public GoWhen(ExprOp condizione, ArrayList<Stat> stats, ArrayList<GoWhen> otherGo, ArrayList<Stat> otherwise) {
        super("GoWhen");
        super.add(condizione);
        for (Stat s : stats){
            super.add(s);
        }
        for (Stat s : otherwise) {
            super.add(s);
        }
        for (GoWhen g : otherGo) {
            super.add(g);
        }
        this.condizione = condizione;
        this.stats = stats;
        this.otherGo = otherGo;
        this.otherwise = otherwise;
    }


    public GoWhen(ExprOp condizione, ArrayList<Stat> stats) {
        super("GoWhen");
        super.add(condizione);
        for (Stat s : stats) {
            super.add(s);
        }
        this.condizione = condizione;
        this.stats = stats;
    }

    public GoWhen() {
        super("GoWhen");
    }

    public ExprOp getCondizione() {
        return condizione;
    }

    public void setCondizione(ExprOp condizione) {
        this.condizione = condizione;
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public void setStats(ArrayList<Stat> stats) {
        this.stats = stats;
    }

    public ArrayList<GoWhen> getOtherGo() {
        return otherGo;
    }

    public void setOtherGo(ArrayList<GoWhen> otherGo) {
        this.otherGo = otherGo;
    }

    public ArrayList<Stat> getOtherwise() {
        return otherwise;
    }

    public void setOtherwise(ArrayList<Stat> otherwise) {
        this.otherwise = otherwise;
    }

    @Override
    public Object accept(Visitor v) throws Exception {
        return v.visit(this);
    }
}
