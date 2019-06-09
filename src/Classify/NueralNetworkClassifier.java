package Classify;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class NueralNetworkClassifier extends Classfiers {
	Instances TrainData;
	Instances TestData;
	Evaluation eval;
	MultilayerPerceptron multilayerPerceptron;
	static EnumClassify type=EnumClassify.NueralNetwork;
	//C'tor Nueral Network
	public NueralNetworkClassifier(Instances TrainData)
	{
		super(TrainData,type);
		this.TrainData=TrainData;
	}
	//Build Classify
	public void Build() throws Exception
	{
		this.TrainData.setClass(this.TrainData.attribute("Class"));
		multilayerPerceptron=new MultilayerPerceptron();
		multilayerPerceptron.buildClassifier(this.TrainData);	
		eval = new Evaluation(TrainData);
		eval.evaluateModel(multilayerPerceptron, TrainData);
		setClassifer(multilayerPerceptron);
		setEval(eval);	
		//super.PrintResult();
		 
			
	}
}
