package Classify;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;

public class RandomTreeClassifier extends Classfiers{
	Instances TrainData;
	Instances TestData;
	RandomTree randomTree;
	Evaluation eval;
	static EnumClassify type=EnumClassify.RandomTree;
	//C'tor Decision Stump
	public RandomTreeClassifier(Instances TrainData)
	{
		super(TrainData,type);
		this.TrainData=TrainData;
	}

	//Build Classify
	public void Build() throws Exception
	{
		this.TrainData.setClass(this.TrainData.attribute("Class"));
		randomTree=new RandomTree();
		randomTree.buildClassifier(this.TrainData);
		eval = new Evaluation(TrainData);
		eval.evaluateModel(randomTree, TrainData);
		setClassifer(randomTree);
		setEval(eval);	
		super.PrintResult();
	}


}
