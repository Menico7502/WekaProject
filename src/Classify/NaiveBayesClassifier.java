package Classify;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class NaiveBayesClassifier extends Classfiers{
	Instances TrainData;
	Instances TestData;
	Evaluation eval;
	NaiveBayes nb;
	static EnumClassify type=EnumClassify.NaiveBayes;
	//C'tor NaiveBase
	public NaiveBayesClassifier(Instances TrainData)
	{
		super(TrainData,type);
		this.TrainData=TrainData;
	}
	//Build Classify
	public void Build() throws Exception
	{
		this.TrainData.setClass(this.TrainData.attribute("Class"));
		
		nb=new NaiveBayes();
		nb.buildClassifier(this.TrainData);	
		eval = new Evaluation(TrainData);
		eval.evaluateModel(nb, TrainData);
		setClassifer(nb);
		setEval(eval);	
		super.PrintResult();
		 
			
	}
}
