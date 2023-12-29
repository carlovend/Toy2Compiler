package tables;

import java.util.ArrayList;

public class FieldType {

    public static class TypeFunction extends FieldType {
        private ArrayList<String> inputParams;
        private ArrayList<String> outputParams;

        public TypeFunction() {
            inputParams = new ArrayList<>();
            outputParams = new ArrayList<>();
        }

        public ArrayList<String> getInputParams() {
            return inputParams;
        }

        public void setInputParams(ArrayList<String> inputParams) {
            this.inputParams = inputParams;
        }

        public ArrayList<String> getOutputParams() {
            return outputParams;
        }

        public void setOutputParams(ArrayList<String> outputParams) {
            this.outputParams = outputParams;
        }

        public void addInputParam(String inputParm) {
            this.inputParams.add(inputParm);
        }

        public void addsListInputParams(ArrayList<String> listInputParams) {
            this.inputParams.addAll(listInputParams);
        }

        public void addOutputParam(String outputParm) {
            this.outputParams.add(outputParm);
        }

        public void addsListOutputParams(ArrayList<String> listOutputParms) {
            this.outputParams.addAll(listOutputParms);
        }

        @Override
        public String toString() {
            return "TypeFunction{" +
                    "inputParams=" + inputParams +
                    ", outputParams=" + outputParams +
                    '}';
        }
    }

    public static class TypeVar extends FieldType {
        private String type;

        public TypeVar(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }


}
