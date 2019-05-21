package Classify;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
public class J48TreeClassifier extends Classfiers{
	Instances TrainData;
	Instances TestData;
	Evaluation eval;
	J48 j48;
	static EnumClassify type=EnumClassify.J48;
	//C'tor J48
	public J48TreeClassifier(Instances TrainData)
	{
		super(TrainData,type);
		this.TrainData=TrainData;
	}
	//Build Classify
	public void Build() throws Exception
	{
		this.TrainData.setClass(this.TrainData.attribute("Class"));
		j48=new J48();	
		j48.buildClassifier(this.TrainData);	
		eval = new Evaluation(TrainData);
		eval.evaluateModel(j48, TrainData);
		setClassifer(j48);
		setEval(eval);	
		super.PrintResult();
		
			
	}
}
