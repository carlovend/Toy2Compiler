package visitors;


import nodi.*;
import nodi.expr.*;
import nodi.statements.*;
import tables.FieldType;
import tables.Row;
import tables.SymbolTable;
import java.util.ArrayList;
import java.util.Iterator;


public class TypeVisitor implements Visitor {

    private SymbolTable currentScope;

    private static final String[][] combinazioniAritOp= { {"integer", "integer", "integer"},
            {"string", "string", "string"},
            {"string","integer","string"},
            {"string","real","string"},
            {"string","boolean","string"},
            {"integer","string","string"},
            {"real","string","string"},
            {"boolean","string","string"},
            {"integer", "real", "real"},
            {"real", "integer", "real"},
            {"real", "real", "real"} };



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
            binaryOP.getExpr1().setType(type1);
        }
        else if (type1.contains("string")) {
            type1 = "string";
            binaryOP.getExpr1().setType(type1);
        }
        else if (type1.contains("real")) {
            type1 = "real";
            binaryOP.getExpr1().setType(type1);
        }else {

        }

        String type2 = (String) binaryOP.getExpr2().accept(this);
        if (type2.contains("integer")) {
            type2 = "integer";
            binaryOP.getExpr2().setType(type2);
        }
        else if (type2.contains("string")) {
            type2 = "string";
            binaryOP.getExpr2().setType(type2);
        }
        else if (type2.contains("real")) {
            type2 = "real";
            binaryOP.getExpr2().setType(type2);
        }else {

        }
        String opType = binaryOP.getOp();

        if (opType.equals("plusOp")||opType.equals("minusOp")||opType.equals("timesOp")||opType.equals("divOp")) {
            for (String[] c: combinazioniAritOp) {
                if (type1.equals(c[0])&&type2.equals(c[1])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exceptions.BinaryOpError(opType);
        }

        if (opType.equals("andOp")||opType.equals("orOp")) {
            for (String[] c:combinazioniBooleanOp) {
                if (type1.equals(c[0])&&type2.equals(c[2])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exceptions.BinaryOpError(opType);
        }

        if (opType.equals("gtOp")||opType.equals("geOp")||opType.equals("leOp")||opType.equals("ltOp")||opType.equals("eqOp")||opType.equals("neOp")) {
            for (String[] c: combinazioniRelOp) {
                if (type1.equals(c[0])&&type2.equals(c[1])) {
                    binaryOP.setType(c[2]);
                    return c[2];
                }
            }
            throw new Exceptions.BinaryOpError(opType);
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
        ArrayList<String> stringOfReturnType = new ArrayList<>();
        int nParametriDellaChiamata = 0;
        FieldType.TypeFunction n = (FieldType.TypeFunction) functionTypes(funCallOp.getId());
        int nParamDellaRow = n.getInputParams().size();
        if (result == null) {
            throw new Exceptions.NonExistingFunc(funCallOp.getId().getId());
        }else {
            if (funCallOp.getExprsList()!=null) {
                for (ExprOp e: funCallOp.getExprsList()) {
                    if (e instanceof FunCallOp) {
                        //ci assicuriamo che abbia un solo tipo di ritorno
                        FieldType.TypeFunction returnType = (FieldType.TypeFunction) functionTypes(((FunCallOp) e).getId());
                        stringOfReturnType.addAll(returnType.getOutputParams());
                        if (stringOfReturnType.size()>1) {
                            throw new Exceptions.FuncInFunc();
                        }
                        nParametriDellaChiamata = nParametriDellaChiamata+1;
                    }else {
                    nParametriDellaChiamata = nParametriDellaChiamata+1;
                }}
            }

            if (nParamDellaRow!=nParametriDellaChiamata) {
                throw new Exceptions.ErrorInReturn();
            }
            if (funCallOp.getExprsList()!=null) {
            for (ExprOp e: funCallOp.getExprsList()) {
                if (e instanceof FunCallOp) {continue;}
                String type = (String) e.accept(this);
                typeExpr.add(type);
            }}

            //verifichiamo se i tipi coincidono
            ArrayList<String> tipiDellaFirma = ((FieldType.TypeFunction)result.getType()).getInputParams();
            Iterator<String> iter1 = tipiDellaFirma.iterator();
            Iterator<String> iter2 = typeExpr.iterator();

            while (iter1.hasNext() && iter2.hasNext()) {
                String type = iter1.next();
                String typeOfId = iter2.next();

                if (typeOfId.equals("integer_const")){
                    typeOfId = "integer";

                }
                if (typeOfId.equals("string_const")) {
                    typeOfId = "string";

                }
                if (!type.equals(typeOfId)) {
                    throw new Exceptions.ErrorInTypeReturn();
                }
            }


        }

        return typeExpr;
    }

    @Override
    public Object visit(Identifier identifier) throws Exception {
        Row res = currentScope.lookUp(identifier.getId());

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

    throw new Exceptions.NoDeclaration(identifier.getId());
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

        //controllare se i parametri ceh diamo alla procedura coincidono con quelli che sono della procedura
        //se è una chiamata di funzioen la funzione puo restituire solo un paramrtro
        //controllare i ref e gli out
        Row r = currentScope.lookUp(procCallOp.getId().getId());
        if (r == null) {
            throw new Exceptions.NonExistingProc(procCallOp.getId().getId());
        }
        FieldType.TypeFunction t = (FieldType.TypeFunction) functionTypes(procCallOp.getId());
        int paramFromRow = t.getInputParams().size();
        ArrayList<String> typeOfParam = new ArrayList<>();
        int nParams = 0;
        ArrayList<String> outONormal = new ArrayList<>();
        ArrayList<String> stringOfReturnType = new ArrayList<>();
        if (procCallOp.getExprsList()!=null) {
            for (ExprOp e : procCallOp.getExprsList()) {
                if (e instanceof FunCallOp) {
                    //ci assicuriamo che abbia un solo tipo di ritorno
                    FieldType.TypeFunction returnType = (FieldType.TypeFunction) functionTypes(((FunCallOp) e).getId());
                    stringOfReturnType.addAll(returnType.getOutputParams());
                    if (stringOfReturnType.size()>1) {
                        throw new Exceptions.FuncInFunc();
                    }
                    nParams = nParams+1;
                    outONormal.add("NORMAL");
                }else {

                String p = (String) e.accept(this);
                if (e instanceof Identifier) {
                    outONormal.add(((Identifier) e).getValue());

                    nParams = nParams+1;
                    typeOfParam.add(p);
                    continue;
                }
                if (p.equals("integer_const")){
                    p = "integer";
                    outONormal.add("NORMAL");
                }
                if (p.equals("string_const")) {
                    p = "string";
                    outONormal.add("NORMAL");
                }if (p.equals("real_const")) {
                        p = "real";
                        outONormal.add("NORMAL");
                    }
                typeOfParam.add(p);
                nParams = 1+nParams;
                }
            }

            if (paramFromRow!=nParams) {
                throw new Exceptions.NotMatchingParameter();
            }else {
                //se il numero di parametri della chiamata e della firma coincidono ci assicuriamo che sono dello stesso tipo
                //dello stesso tipo e anche che i ref e gli out sono al posto giusto

                Iterator<String> iter1 = typeOfParam.iterator();
                FieldType.TypeFunction s= (FieldType.TypeFunction) functionTypes(procCallOp.getId());
                ArrayList<String> tipiDellaFirma = new ArrayList<>(s.getInputParams());

                Iterator<String> iter2 = tipiDellaFirma.iterator();

                while (iter1.hasNext()&& iter2.hasNext()) {
                    String tipo1 = iter1.next();
                    String tipo2 = iter2.next();

                    if (tipo1.contains("string")) {
                        tipo1 = "string";
                    }
                    if (tipo2.contains("string")) {
                        tipo2 = "string";
                    }
                    if (tipo1.contains("real")) {
                        tipo1 = "real";
                    }
                    if (tipo2.contains("real")) {
                        tipo2 = "real";
                    }
                    if (tipo2.contains("integer")) {
                        tipo2 = "integer";
                    }
                    if (tipo1.contains("integer")) {
                        tipo1 = "integer";
                    }

                    if (!tipo1.equals(tipo2)) {
                        throw new Exceptions.NonMatchingTypeParameter();
                    }

                }

                FieldType.TypeFunction typeParamFromRow = (FieldType.TypeFunction) functionTypes(procCallOp.getId());
                ArrayList<String> typeParam = new ArrayList<>();
                typeParam.addAll(typeParamFromRow.getOutputParams());
                iter1 = outONormal.iterator();
                iter2 = typeParam.iterator();

                while(iter1.hasNext()&&iter2.hasNext()) {
                    String tipo1 = iter1.next();
                    String tipo2 = iter2.next();

                    if (tipo1.equals("NORMAL")&&tipo2.equals("OUT")||tipo1.equals("REF")&&!tipo2.equals("OUT")) {
                        throw new Exceptions.NonMatchingOut();
                    }
                }
            }
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
                if (typeExpr.contains(c[0])) {
                    unaryOp.setType(c[1]);
                    return c[1];
                }
            }
            throw new Exceptions.UnaryOpError(type);
        }
        if (type.equals("notOp")) {
            for (String[] c: combinazioniNot) {
                if (typeExpr.contains(c[0])) {
                    unaryOp.setType(c[1]);
                    return c[1];
                }
            }

            throw new Exceptions.UnaryOpError(type);
        }
        return null;
    }

    @Override
    public Object visit(ElifOp elifOp) throws Exception {
        SymbolTable tmp = currentScope;
        String condizione = (String) elifOp.getExprOp().accept(this);
        if (!condizione.equals("boolean")) {
            throw new RuntimeException("La condizione non è un bool");
        }
        currentScope = elifOp.getElifTable();
        elifOp.getBodyOp().accept(this);
        currentScope = tmp;

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) throws Exception {
        SymbolTable tmp = currentScope;
        String condizione = (String) ifOp.getExprOpStat().accept(this);
        if (!condizione.equals("boolean")) {
            throw new RuntimeException("La condizione non è un bool");
        }
        currentScope = ifOp.getTable();

        ifOp.getBodyOpIf().accept(this);


        if (ifOp.getElifOps()!=null) {
            for (ElifOp e : ifOp.getElifOps()) {
                e.accept(this);
            }
        }

        if (ifOp.getElseBody().getBody() != null) {

            currentScope = ifOp.getElseTable();

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

            if (stat.getValue()!=null) {
            if (stat.getValue().equals("RETURN")) {
                if (stat.getExprs()!=null) {
                    return stat.getExprs();
                }
                return null;
            }if (stat.getValue().equals("WRITE")||stat.getValue().equals("WRITERETURN")){
                if (stat.getExprs()!=null) {
                    for (ExprOp e : stat.getExprs()) {
                        e.accept(this);
                    }

                }
            }
            }



        if (stat.getValue()!=null && stat.getValue().equals("ASSIGN")) {
            ArrayList<String> valueOfExprs = new ArrayList<>();
            ArrayList<String> tipiDestra = new ArrayList<>();
            int c = 0;
            for(Identifier i : stat.getIds()) {
                if (!funcParamId.isEmpty()){
                if(i.getId().equals(funcParamId.get(c))) {
                    throw new Exception("I parametri delle funzioni non possono essere riassegnati");
                }}
                c++;
            }
            int nDestra = 0;
            if (stat.getExprs() != null) {
                for (ExprOp e : stat.getExprs()) {
                    if (e instanceof FunCallOp) {
                        e.accept(this);
                        Row r = currentScope.lookUp(((FunCallOp) e).getId().getId());
                        Function f = null;
                        if (r!=null) {
                        if (r.getNode() instanceof Function) {
                            f = (Function) r.getNode();
                        }}else {
                            throw new Exceptions.NonExistingFunc(((FunCallOp) e).getId().getId());
                        }
                        ArrayList<String> tmp = (ArrayList<String>) f.accept(this);
                        tipiDestra.addAll(tmp);

                        nDestra = nDestra+tipiDestra.size();
                    }
                    else{
                        String tipo = (String) e.accept(this);
                        tipiDestra.add(tipo);
                        if (e instanceof BinaryOP) {
                            if (nDestra == 0 ){
                                nDestra = nDestra+1;
                            }else {
                            nDestra = nDestra;}
                        }else {
                        nDestra = nDestra+1;}
                    }
                }

                ArrayList<String> tipiSinistra = new ArrayList<>();
                for (Identifier i: stat.getIds()) {
                    String tipo = (String) i.accept(this);
                    tipiSinistra.add(tipo);
                }
                Iterator<String> iter1 = tipiDestra.iterator();
                Iterator<String> iter2 = tipiSinistra.iterator();
                if (tipiSinistra.size()!=tipiDestra.size()) {
                    throw new Exceptions.AssignErrorNumber();
                }
                while (iter1.hasNext()&&iter2.hasNext()) {
                    String t1 = iter1.next();
                    String t2 = iter2.next();

                    if (t1.contains("string")) {
                        t1 = "string";
                    }
                    if (t2.contains("string")) {
                        t2 = "string";
                    }
                    if (t1.contains("real")) {
                        t1 = "real";
                    }
                    if (t2.contains("real")) {
                        t2 = "real";
                    }
                    if (t2.contains("integer")) {
                        t2 = "integer";
                    }
                    if (t1.contains("integer")) {
                        t1 = "integer";
                    }
                    if (t1.equals("integer")&&t2.equals("real")) {
                        continue;
                    }

                    if (!t1.equals(t2)) {
                        throw new Exceptions.AssignErrorType();
                    }
                }
        }}
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
        if (stat instanceof LetOp) {
            stat.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) throws Exception {

        String condizione = (String) whileOp.getExprOp().accept(this);

        if (!condizione.equals("boolean")) {
            throw new Exceptions.ConditionError("WHILE");
        }

        currentScope = whileOp.getTable();
        whileOp.getBodyOp().accept(this);

        currentScope = whileOp.getTable().getFather();
        return null;
    }

    @Override
    public Object visit(GoWhen goWhen) throws Exception {
        if (goWhen.getCondizione()!=null) {
            String condizione = (String) goWhen.getCondizione().accept(this);
            if (!condizione.equals("boolean")) {
                throw new RuntimeException("La condizione non è bool");
            }
        }
        if (goWhen.getStats()!=null) {
            for (Stat s : goWhen.getStats()) {
                s.accept(this);
            }
        }
        if (goWhen.getOtherGo()!=null) {
            for (GoWhen g : goWhen.getOtherGo()) {
                g.accept(this);
            }
        }
        if (goWhen.getOtherwise()!=null) {
            for (Stat s :goWhen.getOtherwise()) {
                s.accept(this);
            }
        }
        return null;
    }

    @Override
    public Object visit(LetOp letOp) throws Exception {
        SymbolTable tmp = currentScope;
        currentScope = letOp.getSymbolTable();
        if (letOp.getVardecl()!=null) {
            for (Decls d : letOp.getVardecl()) {
                d.accept(this);
            }
        }

        if (letOp.getGoWhen()!=null) {
            letOp.getGoWhen().accept(this);
        }
        currentScope = tmp;
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) throws Exception {
        ArrayList<ExprOp> exprOp = new ArrayList<>();

        if (bodyOp.getStats() != null) {
            for (Stat s : bodyOp.getStats()) {
               exprOp = (ArrayList<ExprOp>) s.accept(this);
            }
        }
        if (exprOp!=null) {
            if (!exprOp.isEmpty())
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
        throw new Exceptions.NoDeclaration(identifier.getId());
    }

    boolean isReturnPresente = false;
    ArrayList<String> funcParamId = new ArrayList<>();
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
        if (function.getFunc()!=null) {
            for (FuncParams f : function.getFunc()) {
                funcParamId.add(f.getId().getId());
            }
        }
        ArrayList<ExprOp> exprs = new ArrayList<>();
        if (function.getBody()!= null) {
          exprs = (ArrayList<ExprOp>) function.getBody().accept(this);
        }
        ArrayList<String> returnType = new ArrayList<>();
        for (Type t :function.getTypes()) {
            returnType.add(t.getType());
        }

        ArrayList<String> returntipi = new ArrayList<>();
        for (Stat s : function.getBody().getStats()){
        exprs = s.getExprs();
        if (exprs!=null && s.getValue().equals("RETURN")) {

            for (ExprOp e: exprs) {
                if (e instanceof FunCallOp) {
                    FieldType.TypeFunction function1= (FieldType.TypeFunction) functionTypes(((FunCallOp) e).getId());
                    for (String str: function1.getOutputParams()) {
                        returntipi.add(str);
                    }
                    continue;
                }
                String t = (String) e.accept(this);
                returntipi.add(t);
            }
            Iterator<String> iter1 = returnType.iterator();
            Iterator<String> iter2 = returntipi.iterator();

            if (returntipi.size()!= returnType.size()) {
                throw new RuntimeException("Errore il numero di parametri di ritorno non coincide");
            }
            while (iter1.hasNext()&&iter2.hasNext()) {
                String t1 = iter1.next();
                String t2 = iter2.next();
                if (!t2.contains(t1)) {
                    throw new Exceptions.ErrorInTypeReturn();
                }
            }
            }
        }

        currentScope = tmp;


        if (!isReturnPresente) {
            throw new Exceptions.MissingReturn();
        }
        funcParamId.clear();
        return returnType;

    }
    boolean check=false;
    @Override
    public Object visit(Iter iter) throws Exception {

        if (iter.getDecls() != null) {
            ArrayList<Row> varList= new ArrayList<>();
            for (Decls d : iter.getDecls()) {

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
                    throw new Exceptions.ReturnInProc();
                }
            }
        }

        if (procedure.getBody()!=null) {
            if (procedure.getBody().getStats()!=null) {
                for (Stat s : procedure.getBody().getStats()) {
                    if (s.getValue()!=null) {
                    if (s.getValue().equals("READ")||s.getValue().equals("WRITE")) {
                        ArrayList<ProcParams> p = procedure.getProcParams();
                        int i = 0;
                       if (!p.isEmpty())
                        for (ExprOp e : s.getExprs()) {
                            if (e instanceof Identifier) {
                                for (int c = 0; c<p.size();c++) {
                                    if (((Identifier) e).getId().equals(p.get(c).getId().getId())) {
                                        ((Identifier) e).setValue(p.get(c).getId().getValue());
                                    }
                                }
                            }
                        }
                        }
                    }
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
            throw new Exceptions.MissingMain();
        }
        return null;
    }

    @Override
    public Object visit(Type type) {
        return null;
    }
}
