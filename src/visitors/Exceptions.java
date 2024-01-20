package visitors;

public class Exceptions {



    public static class NoDeclaration extends Exception {
        public NoDeclaration(String s) {
            super("Nessuna dichiarazione di "+s);
        }
    }

    public static class OnlyId extends Exception {
        public OnlyId() {
            super("Sono concessi solo id all'interno della scanf");
        }
    }

    public static class BinaryOpError extends Exception {
        public BinaryOpError(String s) {
            super("Errore nell operazione di: "+s);
        }
    }
    public static class UnaryOpError extends Exception {
        public UnaryOpError(String s) {
            super("Errore nell operazione di: "+s);
        }
    }

    public static class NonExistingFunc extends Exception{
        public NonExistingFunc(String s) {
            super("La funzione: "+s+ " non esiste");
        }
    }

    public static class NonExistingProc extends Exception{
        public NonExistingProc(String s) {
            super("La procedura: "+s+ " non esiste");
        }
    }

    public static class FuncInFunc extends Exception {
        public FuncInFunc() {
            super("Non puoi chiamare una funzione che restituisce più di un valore all'interno di un altra funzione");
        }
    }

    public static class ErrorInReturn extends Exception {
        public ErrorInReturn() {
            super("Il numero di parametri di ritorno non coincide con la firma della funzione");
        }
    }

    public static class ErrorInTypeReturn extends Exception {
        public ErrorInTypeReturn() {
            super("I tipi di ritorno non coincidono con i tipi della firma della funzione");
        }
    }

    public static class NotMatchingParameter extends Exception {
        public NotMatchingParameter() {
            super("Il numero di parametri non coincide con la firma");
        }
    }

    public static class NonMatchingTypeParameter extends Exception {
        public NonMatchingTypeParameter() {
            super("Il tipo dei parametri non coincide con la firma");
        }
    }
    public static class NonMatchingOut extends Exception {
        public NonMatchingOut() {
            super("I parametri out non coincidono con i ref");
        }
    }

    public static class AssignErrorNumber extends Exception {
        public AssignErrorNumber() {
            super("Il numero di id non coincide con il numero di espressioni");
        }
    }
    public static class AssignErrorType extends Exception {
        public AssignErrorType() {
            super("I tipi degli id non coincide con i tipi delle espressioni");
        }
    }

    public static class ConditionError extends Exception {
        public ConditionError(String s) {
            super("La condizione del: "+s+" non è valida");
        }
    }

    public static class MissingReturn extends Exception {
        public MissingReturn() {
            super("Return non presente");
        }
    }

    public static class ReturnInProc extends Exception {
        public ReturnInProc() {
            super("Il return non può essere presente nelle procedure");
        }
    }

    public static class MissingMain extends Exception {
        public MissingMain() {
            super("MAIN non presente nel programma");
        }
    }
}
