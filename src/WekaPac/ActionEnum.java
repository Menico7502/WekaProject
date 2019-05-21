package WekaPac;

public enum ActionEnum {
	Classify,Test,Rank;
	
    public static ActionEnum ConvertToEnum(String value){
        if(value.equalsIgnoreCase(Classify.toString()))
            return ActionEnum.Classify;
        else if(value.equalsIgnoreCase(Test.toString()))
            return ActionEnum.Test;
        else if(value.equalsIgnoreCase(Rank.toString()))
            return ActionEnum.Rank;
        else
            return null;
    }
    public static String ConvertToString(ActionEnum e){
    	if(e==Classify)
            return "Classify";
    	if(e==Test)
            return "Test";
    	if(e==Rank)
            return "Rank";
        return null;
    }

}
