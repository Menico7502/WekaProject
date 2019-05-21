package Classify;



public enum EnumClassify {
	DecisionStump,RandomTree,J48,NaiveBayes,NueralNetwork,svm;
	
	
    public static EnumClassify ConvertToEnum(String value){
        if(value.equalsIgnoreCase(DecisionStump.toString()))
            return EnumClassify.DecisionStump;
        else if(value.equalsIgnoreCase(RandomTree.toString()))
            return EnumClassify.RandomTree;
        else if(value.equalsIgnoreCase(J48.toString()))
            return EnumClassify.J48;
        else if(value.equalsIgnoreCase(NaiveBayes.toString()))
            return EnumClassify.NaiveBayes;
        else if(value.equalsIgnoreCase(NueralNetwork.toString()))
            return EnumClassify.NueralNetwork;
        else if(value.equalsIgnoreCase(svm.toString()))
            return EnumClassify.svm;
        else
            return null;
    }
}
