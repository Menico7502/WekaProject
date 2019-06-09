package WekaPac;

public enum ActionEnum {
	Classify,Test,Rank,tenFold;
	
    public static ActionEnum ConvertToEnum(String value){
        if(value.equalsIgnoreCase(Classify.toString()))
            return ActionEnum.Classify;
        else if(value.equalsIgnoreCase(Test.toString()))
            return ActionEnum.Test;
        else if(value.equalsIgnoreCase(Rank.toString()))
            return ActionEnum.Rank;
        else if(value.equalsIgnoreCase(tenFold.toString()))
            return ActionEnum.tenFold;
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
    	if(e==tenFold)
            return "tenFold";
        return null;
    }

}
