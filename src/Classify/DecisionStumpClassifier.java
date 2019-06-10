package Classify;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.DecisionStump;
import weka.core.Instances;
public class DecisionStumpClassifier extends Classfiers{
	Instances TrainData;
	Instances TestData;
	Evaluation eval;
	DecisionStump decisionStump;
	static EnumClassify type=EnumClassify.DecisionStump;
	
	//C'tor Decision Stump
	public DecisionStumpClassifier(Instances TrainData)
	{
		super(TrainData,type);
		this.TrainData=TrainData;
	}
	//Build Classify
	public void Build() throws Exception
	{
		this.TrainData.setClass(this.TrainData.attribute("Class"));
		decisionStump=new DecisionStump();
		decisionStump.buildClassifier(this.TrainData);	
		eval = new Evaluation(TrainData);
		eval.evaluateModel(decisionStump, TrainData);
		setClassifer(decisionStump);
		setEval(eval);	
		super.PrintResult();
		 
			
	}

}
