package tables;

import java.util.ArrayList;

public class SymbolTable {

    private SymbolTable father;
    private String scope;
    private ArrayList<Row> rowList;
    private static boolean shadowing = true;

    public SymbolTable() {
        father=null;
        rowList = new ArrayList<>();
    }

    public SymbolTable(SymbolTable father, ArrayList<Row> rowList) {
        this.father = father;
        this.rowList = rowList;
    }

    public SymbolTable(SymbolTable father, String scope, ArrayList<Row> rowList) {
        this.father = father;
        this.scope = scope;
        this.rowList = rowList;
    }


    public Row lookUp(String id) {
        SymbolTable table = this;
        while(table != null) {
            for (Row r: table.rowList) {
                if (r.getSymbol().equals(id)) {
                    return r;
                }
            }
            table = table.father;
        }
        return null;
    }

    public SymbolTable getFather() {
        return father;
    }

    public void setFather(SymbolTable father) {
        this.father = father;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public ArrayList<Row> getRowList() {
        return rowList;
    }

    public void setRowList(ArrayList<Row> rowList) {
        this.rowList = rowList;
    }

    public boolean isShadowing() {
        return shadowing;
    }

    public void setShadowing(boolean shadowing) {
        this.shadowing = shadowing;
    }


    public boolean isSymbolPresent(String symbol) {
        for (Row r: this.rowList) {
            if (r.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public void addRow(Row row) throws Exception {

        if (shadowing) {
            if (isSymbolPresent(row.getSymbol())||(row.getSymbol().equals(scope)&& !row.getSymbol().equals("Root"))) {
                //TODO FARE ECCEZIONE
                throw new Exception("gIA DICHIARATO");
            }
            else {
                rowList.add(row);
            }
        } else {
            SymbolTable table = this;
            while (table != null) {
                if (table.isSymbolPresent(row.getSymbol()) || (row.getSymbol().equals(table.scope)&&!row.getSymbol().equals("Root"))) {
                    throw new Exception(row.getSymbol());
                }else {
                    table = table.getFather();
                }
                rowList.add(row);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Scope: ").append(scope).append("\n");

        if (father != null) {
            sb.append("Father Scope: ").append(father.scope).append("\n");
        } else {
            sb.append("Father Scope: null\n");
        }

        sb.append("Symbols:\n");
        for (Row row : rowList) {
            sb.append("  ").append(row).append("\n");
        }

        return sb.toString();
    }

}
