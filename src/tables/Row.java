package tables;

public class Row {
    private String symbol;
    private Object node;
    private FieldType type;

    public Row(String symbol, Object node, FieldType type,String prop) {
        this.symbol = symbol;
        this.node = node;
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Row{" +
                "symbol='" + symbol + '\'' +
                ", node=" + node +
                ", type=" + type +
                '}';
    }
}
