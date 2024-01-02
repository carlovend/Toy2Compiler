package visitors;

import nodi.*;
import nodi.expr.*;
import nodi.statements.ElifOp;
import nodi.statements.IfOp;
import nodi.statements.Stat;
import nodi.statements.WhileOp;
import tables.FieldType;
import tables.Row;
import tables.SymbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TypeVisitor implements Visitor {

    private SymbolTable currentScope;

    private static final String[][] combinazioniAritOp= { {"integer", "integer", "integer"},
            {"integer", "real", "real"},
            {"real", "integer", "real"},
            {"real", "real", "real"} };

    private static final String[][] combinazioniStrOp= { {"string", "string", "string"},
            {"string", "real","string"},
            {"string","integer","string"},
            {"integer","string","string"},
            {"real","string","string"},
            };

    private static final String[][] combinazioniBooleanOp= { {"boolean", "boolean", "boolean"} };

    private static final String[][] combinazioniRelOp= { {"integer", "integer", "boolean"},
            {"real", "real", "boolean"},
            {"integer", "real", "boolean"},
            {"real", "integer", "boolean"},
            {"string","string","boolean"},
            {"boolean","boolean","boolean"},
            };

    private static final String[][] combinazioniMinus= { {"integer", "integer"},
            {"real", "real"} };


    private static final String[][] combinazioniNot= { {"boolean", "boolean"} };

    private static final String[][] compatibilita= {{"integer","integer"},
            {"real","real"},
            {"real","integer"},
            {"string","string"},
            {"boolean","boolean"},
            {"void","void"}};
    @Override
    public Object visit(BinaryOP binaryOP) throws Exception {
        String type1 = (String) binaryOP.getExpr1().accept(this);
        if (type1.contains("integer")) {
            type1 = "integer";
        }
        else if (type1.contains("string")) {
            type1 = "string";
        }
        else if (type1.contains("real")) {
            type1 = "real";
        }else {

        }

        String type2 = (String) binaryOP.getExpr2().accept(this);

        String opType = binaryOP.getType();
        if (opType.equals("plusOp")||opType.equals("minusOp")||opType.equals("timesOp")||opType.equals("divOp")) {
            for (String[] c: combinazioniAritOp) {
                if (type1.equals(c[0])&&type2.equals(c[1])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            return new Exception("errore nella binary");
        }

        if (opType.equals("andOp")||opType.equals("orOp")) {
            for (String[] c:combinazioniBooleanOp) {
                if (type1.equals(c[0])&&type2.equals(c[2])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exception("BooleanOp erroreeee");
        }

        if (opType.equals("gtOp")||opType.equals("geOp")||opType.equals("leOp")||opType.equals("ltOp")||opType.equals("eqOp")||opType.equals("neOp")) {
            for (String[] c: combinazioniRelOp) {
                if (type1.equals(c[0])&&type2.equals(c[1])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exception("Errore nelle relOp");
        }

        return new Exception("Binay non valida");
    }

    @Override
    public Object visit(ConstOp constOp) {
        String type = constOp.getType();
        if (type.equals("boolean")) {
            constOp.setType("boolean");
            return "boolean";
        }
        if (type.equals("integer_const")) {
            constOp.setType("integer");
            return "integer";
        }
        if (type.equals("real_const")) {
            constOp.setType("real");
            return "real";
        }
        if (type.equals("string_const")) {
            constOp.setType("string");
            return "string";
        }


        return null;
    }

    @Override
    public Object visit(ExprOp exprOp) throws Exception {

        String type = null;
        if (exprOp instanceof ConstOp) {
            type = (String) exprOp.accept(this);
        }

        if (exprOp instanceof BinaryOP) {
            type = (String) exprOp.accept(this);
        }

        if (exprOp instanceof Identifier) {
            type = (String) exprOp.accept(this);

        }

        if (exprOp instanceof UnaryOp) {
            type = (String) exprOp.accept(this);
        }

        return type;
    }

    @Override
    public Object visit(FunCallOp funCallOp) throws Exception {
        ArrayList<String> typeExpr = new ArrayList<>();
        Row result = currentScope.lookUp(funCallOp.getId().getId());
        System.out.println("SONO NELLA FUNZIONE"+funCallOp.getId().getId());
        if (result == null) {
            throw new Exception("La funzione non esiste");
        }else {
            FieldType.TypeFunction dec = (FieldType.TypeFunction) result.getType();
            ArrayList<String> inParam = dec.getInputParams();
            int nParametri = inParam.size();
            int nParamChiam = 0;
            if (funCallOp.getExprsList() != null) {
                nParamChiam = funCallOp.getExprsList().size();

                if (nParametri != nParamChiam) {
                    throw new Exception("Il numero di parametri non coincide");
                }else {

                    for (ExprOp e: funCallOp.getExprsList()) {
                        String type = (String) e.accept(this);
                        typeExpr.add(type);
                    }
                    //VERIFICHIAMO CHE I TIPI COINCIDANO
                    ArrayList<String> tipiDellaChiama = ((FieldType.TypeFunction) result.getType()).getInputParams();
                    Iterator<String> iter1 = Arrays.asList(String.valueOf(tipiDellaChiama)).iterator();
                    Iterator<String> iter2 = Arrays.asList(String.valueOf(typeExpr)).iterator();

                    while (iter1.hasNext() && iter2.hasNext()) {
                        String type = iter1.next();
                        String typeOfId = iter2.next();
                        System.out.println(type+typeOfId);
                        if (!type.equals(typeOfId)) {
                            throw new Exception("Tipi non validi");
                        }
                    }
                }
            }
        }


        return typeExpr;
    }

    @Override
    public Object visit(Identifier identifier) throws Exception {
        Row res = currentScope.lookUp(identifier.getId());
        System.out.println(identifier.getId());
       /* if (res == null) {
            throw new Exception("Nessuna dichiarazione");
        }else {
            if (res.getType() instanceof FieldType.TypeFunction) {
                return ((FieldType.TypeFunction) res.getType()).getOutputParams().get(0);
            }else {
                identifier.setType((((FieldType.TypeVar) res.getType()).getType()));
                return ((FieldType.TypeVar) res.getType()).getType();
            }
        }*/

        SymbolTable tmp = currentScope;

        while (currentScope != null) {
            if (currentScope.lookUp(identifier.getId()) != null) {
                Row r = currentScope.lookUp(identifier.getId());
                String type = r.getType().toString();
                return type;
            }else {
                currentScope = currentScope.getFather();
            }
        }

        currentScope = tmp;
        System.out.println("PRIMA DI ECCEZZIONE"+identifier.getId());
    throw new Exception("Nessuna dichiarazione");
    }

    @Override
    public Object visit(IoArgs ioArgs) {
        return null;
    }

    @Override
    public Object visit(IoArgsConcat ioArgsConcat) {
        return null;
    }

    @Override
    public Object visit(ProcCallOp procCallOp) throws Exception {
        ArrayList<String> typeExpr = new ArrayList<>();
        Row r = currentScope.lookUp(procCallOp.getId().getId());
        FieldType.TypeFunction dec = (FieldType.TypeFunction) r.getType();
        ArrayList<String> inputParams= dec.getInputParams();
        ArrayList<String> parametriConOut = ((FieldType.TypeFunction) r.getType()).getOutputParams();
        int nParametriConOut = parametriConOut.size();
        int nParametri = inputParams.size();
        int nProcParams = 0;
        if (r == null) {
            throw new Exception("LA PROCEDURA NON ESISTE");
        }else {
            if (procCallOp.getExprsList()!=null) {
                for (ExprOp e: procCallOp.getExprsList()) {
                    if (e instanceof FunCallOp) {
                        System.out.println("CURRENTSCOPE NEL PROCCALL");
                         typeExpr = (ArrayList<String>) e.accept(this);
                    }else {
                        String t = (String) e.accept(this);
                        typeExpr.add(t);
                    }
                }

                nProcParams = nProcParams +typeExpr.size();
                if (nParametri!=nProcParams) {
                    System.out.println(nProcParams+"SUCAMI"+nParametri+"SUCAMI2"+typeExpr.size());

                    throw new Exception("I PARAMETRI DELLA CHIAMATA DELLA PORC NON COINCIDONO");
                }
                ArrayList<String> tipiDellaChiamata = ((FieldType.TypeFunction) r.getType()).getInputParams();
                Iterator<String> iter1 = Arrays.asList(String.valueOf(tipiDellaChiamata)).iterator();
                 Iterator<String> iter2 = Arrays.asList(String.valueOf(typeExpr)).iterator();
                while (iter1.hasNext() && iter2.hasNext()) {
                    String type = iter1.next();
                    String typeOfId = iter2.next();
                    System.out.println(type+typeOfId);
                    if (!type.equals(typeOfId)) {
                        throw new Exception("Tipi non validi nella procedura");
                    }
            }
                ArrayList<String> refValue = new ArrayList<>();
                List<String> list1 = ((FieldType.TypeFunction) r.getType()).getOutputParams();
            if (procCallOp.getExprsList()!=null) {
            for (ExprOp e : procCallOp.getExprsList()) {
                if (e instanceof Identifier) {
                    refValue.add(((Identifier) e).getValue());
                }
            }}
                List<String> list2 = (refValue);
            if (list1.size()!=list2.size()) {
                throw new Exception("ERRORE NEI REF E NEGLI OUT");
            }
                for (int i = 0; i < list1.size() && i < list2.size(); i++) {
                    String type = list1.get(i);
                    String typeOfId = list2.get(i);
                    System.out.println(type + typeOfId + " CONTROLLO I REF");
                    if ((type.equals("NORMAL") && typeOfId.equals("REF")) || (type.equals("OUT") && !typeOfId.equals("REF"))) {
                        throw new Exception("Tipi non validi per i ref e per gli out");
                    }
                }
            }
        }

        if (nProcParams!=nParametri) {
            System.out.println(nProcParams+nParametri+"PORCODIOOOOOOOOOOOOOOOOOOOOOO");
            throw new Exception("I PARAMETRI DELLA CHIAMATA DELLA PORC NON COINCIDONO");
        }
        return null;
    }

    @Override
    public Object visit(ProcParams procParams) {
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) throws Exception {

        String typeExpr = (String) unaryOp.getExprOp().accept(this);
        String type = unaryOp.getType();

        if (type.equals("unaryMinusOp")) {
            for (String[] c : combinazioniMinus) {
                if (typeExpr.equals(c[0])) {
                    unaryOp.setType(c[1]);
                    return c[1];
                }
            }
            throw new Exception("Errore nella minus");
        }
        if (type.equals("notOp")) {
            for (String[] c: combinazioniNot) {
                if (typeExpr.equals(c[0])) {
                    unaryOp.setType(c[1]);
                    return c[1];
                }
            }

            throw new Exception("Errore nella not");
        }
        return null;
    }

    @Override
    public Object visit(ElifOp elifOp) throws Exception {
        SymbolTable tmp = currentScope;
        String condizione = (String) elifOp.getExprOp().accept(this);

        currentScope = elifOp.getElifTable();
        elifOp.getBodyOp().accept(this);
        currentScope = tmp;

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) throws Exception {
        SymbolTable tmp = currentScope;
        String condizione = (String) ifOp.getExprOpStat().accept(this);
        currentScope = ifOp.getTable();
        System.out.println(currentScope +"HO APPENA SETTATO CURRENT DI IF");
        ifOp.getBodyOpIf().accept(this);


        if (ifOp.getElifOps()!=null) {
            for (ElifOp e : ifOp.getElifOps()) {
                e.accept(this);
            }
        }

        if (ifOp.getElseBody().getBody() != null) {

            currentScope = ifOp.getElseTable();
            System.out.println("SONO NELL ELSE E VEDO LO SCOPE"+currentScope.getScope());
            ifOp.getElseBody().getBody().accept(this);
            for (Stat s : ifOp.getElseBody().getBody().getStats()) {
                if (s.getValue()!=null&&s.getValue().equals("RETURN")) {
                    isReturnPresente = true;
                }

            }
        }
        currentScope = tmp;
        return null;
    }

    @Override
    public Object visit(Stat stat) throws Exception {
            System.out.println("SONO NEL FOTTUTO STAT"+stat.getValue());
            if (stat.getValue()!=null) {
            if (stat.getValue().equals("RETURN")) {
                if (stat.getExprs()!=null) {
                    return stat.getExprs();
                }
                return null;
            }else {
            return null;
            }
            }



        if (!(stat instanceof WhileOp) && !(stat instanceof IfOp) && !(stat instanceof ElifOp) && !(stat instanceof ProcCallOp)) {

            if (!stat.getExprs().isEmpty()) {
                if (stat.getExprs().get(0) instanceof FunCallOp) {

                    Row r = currentScope.lookUp(((FunCallOp) stat.getExprs().get(0)).getId().getId()) ;
                    System.out.println(r.getNode());
                    Function f = null;
                    if (r.getNode() instanceof Function) {
                        f = (Function) r.getNode();
                    }else {
                        throw new Exception("Non è una function");
                    }
                    System.out.println("sono una function");
                    ArrayList<String>  returnType = (ArrayList<String>) f.accept(this);
                    Iterator<String> iter1 = Arrays.asList(String.valueOf(returnType)).iterator();
                    ArrayList<String> ids = new ArrayList<>();
                    for (Identifier i: stat.getIds()) {
                        String a = (String) i.accept(this);
                        ids.add(a);
                    }
                    Iterator<String> iter2 = Arrays.asList(String.valueOf(ids)).iterator();
                    while (iter1.hasNext() && iter2.hasNext()) {
                        String type = iter1.next();
                        String typeOfId = iter2.next();
                        System.out.println(type+typeOfId);
                        if (!type.equals(typeOfId)) {
                            throw new Exception("Tipi non validi");
                        }
                    }
                }
                else {
                    ArrayList<String> ids1 = new ArrayList<>();
                    if (stat.getIds()!=null) {
                    for (Identifier i : stat.getIds()) {
                        String a  = (String) i.accept(this);
                        ids1.add(a);
                    }}
                    ArrayList<String> ids2 = new ArrayList<>();
                    for (ExprOp e: stat.getExprs()) {
                        String type = (String) e.accept(this);
                        ids2.add(type);
                    }
                    Iterator<String> iter1 = Arrays.asList(String.valueOf(ids1)).iterator();
                    Iterator<String> iter2 = Arrays.asList(String.valueOf(ids2)).iterator();
                    while (iter1.hasNext() && iter2.hasNext()) {
                        String type = iter1.next();
                        String typeOfId = iter2.next();
                        System.out.println(type+typeOfId);
                        if (!typeOfId.contains(type.substring(1,6))) {
                            throw new Exception("Tipi non validi");
                        }
                    }

                }
            }
            if (stat.getIds() != null){
            for (Identifier i: stat.getIds()) {
                i.accept(this);
            }}
            if (!stat.getExprs().isEmpty()) {
                if (stat.getExprs().get(0) instanceof FunCallOp) {
                    stat.getExprs().get(0).accept(this);
                }
            }
        }


        if (stat instanceof WhileOp) {
            stat.accept(this);
        }

        if (stat instanceof ProcCallOp) {
            stat.accept(this);
        }

        if (stat instanceof IfOp) {
            stat.accept(this);
        }

        if (stat instanceof ElifOp) {
            stat.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) throws Exception {

        String condizione = (String) whileOp.getExprOp().accept(this);

        if (!condizione.equals("boolean")) {
            throw new Exception("Condizione while non valida");
        }

        currentScope = whileOp.getTable();
        whileOp.getBodyOp().accept(this);

        currentScope = whileOp.getTable().getFather();
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) throws Exception {
        ArrayList<ExprOp> exprOp = new ArrayList<>();

        if (bodyOp.getStats() != null) {
            for (Stat s : bodyOp.getStats()) {
                s.accept(this);
            }
        }
        if (!exprOp.isEmpty()) {
            return exprOp;
        }
        return null;
    }

    @Override
    public Object visit(Decls decls) throws Exception {

        return null;
    }

    @Override
    public Object visit(FuncParams funcParams) {
        return null;
    }


    public Object functionTypes(Identifier identifier) throws Exception {
        SymbolTable tmp = currentScope;

        while (currentScope != null) {
            if (currentScope.lookUp(identifier.getId()) != null) {
                Row r = currentScope.lookUp(identifier.getId());
                FieldType.TypeFunction type = (FieldType.TypeFunction) r.getType();
                return type;
            }else {
                currentScope = currentScope.getFather();
            }
        }

        currentScope = tmp;
        throw new Exception("Nessuna dichiarazione");
    }

    boolean isReturnPresente = false;
    @Override
    public Object visit(Function function) throws Exception {
        isReturnPresente = false;
        for (Stat s: function.getBody().getStats()) {
            if (s.getValue()!=null) {
                if (s.getValue().equals("RETURN")) {
                    isReturnPresente=true;
            }

            }
        }
        SymbolTable tmp = currentScope;
        currentScope = function.getTable();
        System.out.println("sono nella funzione"+currentScope.getScope());
        ArrayList<ExprOp> exprs = new ArrayList<>();
        if (function.getBody()!= null) {
          exprs = (ArrayList<ExprOp>) function.getBody().accept(this);
        }
        ArrayList<String> returnType = new ArrayList<>();
        for (Type t :function.getTypes()) {
            returnType.add(t.getType());
        }
        //TODO controlllare che tipi di ritorno e return coincidono
        ArrayList<String> returntipi = new ArrayList<>();
        if (exprs!=null ) {


            for (ExprOp e: exprs) {
                String t = (String) e.accept(this);
                if (e instanceof FunCallOp) {
                    FieldType.TypeFunction function1= (FieldType.TypeFunction) functionTypes(((FunCallOp) e).getId());
                    for (String s: function1.getOutputParams()) {
                        returntipi.add(s);
                    }
                    continue;
                }
                returntipi.add(t);
            }
            Iterator<String> iter1 = Arrays.asList(String.valueOf(returnType)).iterator();
            Iterator<String> iter2 = Arrays.asList(String.valueOf(returntipi)).iterator();

            while (iter1.hasNext()&&iter2.hasNext()) {
                String t1 = iter1.next();
                String t2 = iter2.next();
                System.out.println(t1+t2);
                if (!t1.equals(t2)) {
                    throw new Exception("Tipi non validi");
                }
            }
        }
        System.out.println("sono nella funzione prima di riassegnare"+currentScope.getScope());
        currentScope = tmp;
        System.out.println("sono nella funzione doppo aver riassegnato"+currentScope.getScope());

        if (!isReturnPresente) {
            throw new Exception("RETURN NON PRESENTE");
        }
        return returnType;

    }
    boolean check=false;
    @Override
    public Object visit(Iter iter) throws Exception {

        if (iter.getDecls() != null) {
            ArrayList<Row> varList= new ArrayList<>();
            for (Decls d : iter.getDecls()) {
                System.out.println(iter.getDecls().size());
                varList = (ArrayList<Row>) d.accept(this);

            }
        }

        if (iter.getFunction()!=null) {
            iter.getFunction().accept(this);
        }

        if (iter.getProc()!=null) {
                Row proc = null;
            proc = (Row) iter.getProc().accept(this);
            if (iter.getProc().getId().getId().equals("main")) {
                check = true;
            }
        }


        return null;
    }

    @Override
    public Object visit(Procedure procedure) throws Exception {
        SymbolTable tmp = currentScope;
        currentScope = procedure.getTable();
        for (Stat s: procedure.getBody().getStats()) {
            if (s.getValue()!=null) {
                if (s.getValue().equals("RETURN")) {
                    throw new Exception("NELLE PROCEDURE NON DEVE ESSERE PRESENTE");
                }
            }
        }

        if (procedure.getBody()!=null) {
            procedure.getBody().accept(this);
        }
        currentScope = tmp;
        return null;
    }

    @Override
    public Object visit(Program program) throws Exception {

        currentScope = program.getTable();
        if (program.getProc()!=null) {
            Row proc = null;
            proc = (Row) program.getProc().accept(this);
            if (program.getProc().getId().getId().equals("main")) {
                check = true;
            }
        }

        if (program.getIter().size()>0) {
            System.out.println("PALLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            Row iterList;
            for (Iter i : program.getIter()) {
                iterList = (Row) i.accept(this);

            }
        }

        if (program.getNoProc().size()>0) {
            Row iterList;
            for (Iter i : program.getNoProc()) {
                iterList = (Row) i.accept(this);

            }
        }
        if (!check) {
            throw new Exception("MAIN NON PRESENTE");
        }
        return null;
    }

    @Override
    public Object visit(Type type) {
        return null;
    }
}
