package Classify;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
public class SVMClassifier extends Classfiers{
	Instances TrainData;
	Instances TestData;
	SMO svm;;
	Evaluation eval;
	static EnumClassify type=EnumClassify.RandomTree;
	//C'tor Decision Stump
	public SVMClassifier(Instances TrainData)
	{
		super(TrainData,type);
		this.TrainData=TrainData;
	}

	//Build Classify
	public void Build() throws Exception
	{
		this.TrainData.setClass(this.TrainData.attribute("Class"));
		svm=new SMO();
		svm.buildClassifier(this.TrainData);
		eval = new Evaluation(TrainData);
		eval.evaluateModel(svm, TrainData);
		setClassifer(svm);
		setEval(eval);	
		super.PrintResult();
	}


}
