package Classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instance;
import weka.core.Instances;



public class Classfiers {
	Object classifer;
	Instances TestData;
	Instances TrainData;
	Evaluation eval;
	EnumClassify type;
	ArrayList<String> YesNoRes=new ArrayList<String>();
	
	public Classfiers(Instances TrainData,EnumClassify type)
	{
		setTrainData(TrainData);
		setType(type);

	}
	
	public Object getClassifer() {
		return classifer;
	}
	public double tenFold() throws Exception
	{		
		 this.eval.crossValidateModel((Classifier) this.classifer, this.TrainData, 10, new Random(1));
		 System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));
		 return eval.pctCorrect();
	}
	public void setClassifer(Object classifer) {
		if(type==EnumClassify.DecisionStump)
			this.classifer = ((DecisionStump) classifer);
		else if(type==EnumClassify.RandomTree)
			this.classifer = ((RandomTree) classifer);
		else if(type==EnumClassify.J48)
			this.classifer = ((J48) classifer);
		else if(type==EnumClassify.NaiveBayes)
			this.classifer = ((NaiveBayes) classifer);
		else if(type==EnumClassify.NueralNetwork)
			this.classifer = ((MultilayerPerceptron) classifer);
		else if(type==EnumClassify.svm)
			this.classifer = ((SMO) classifer);
	}
	public void SaveModel(String Path) throws Exception
	{
		String out=Path+"\\"+this.type.toString()+".model";
		weka.core.SerializationHelper.write(out, getClassifer());
	}
	public void LoadModel(String Path) throws Exception
	{
		String out=Path+"\\"+this.type.toString()+".model";
		setClassifer(weka.core.SerializationHelper.read(out));
	}
	public Instances getTestData() {
		return TestData;
	}
	public void setTestData(Instances testData) {
		TestData = testData;
	}
	public Instances getTrainData() {
		return TrainData;
	}
	public void setTrainData(Instances trainData) {
		TrainData = trainData;
	}
	public Evaluation getEval() {
		return eval;
	}
	public void setEval(Evaluation eval) {
		this.eval = eval;
	}
	public EnumClassify getType() {
		return type;
	}
	public void setType(EnumClassify type) {
		this.type = type;
	}

	public void PrintResult() throws Exception
	{
		System.out.println("=== Run information ===\n");
		System.out.println("Scheme:"+getClassifer().getClass().getName()+weka.core.Utils.joinOptions(((AbstractClassifier) getClassifer()).getOptions()));
		System.out.println("Relation:   "+getTrainData().relationName());
		System.out.println("Instances:  "+getTrainData().numInstances());
		System.out.println("Attributes: "+ getTrainData().numAttributes());
		for(int i=0;i<getTrainData().numAttributes();i++)
			System.out.println("\t    "+getTrainData().attribute(i).name());
		System.out.println(getClassifer().toString());
		System.out.println(getEval().toSummaryString("\n====Summary====", false));
		System.out.println(getEval().toClassDetailsString());
		System.out.println(getEval().toMatrixString());
	}
	
	public void SetChangeOnCSVFile(String FilesPath) throws IOException{
		//File CSVFile = new File(FilesPath+"\\TestData.csv");
		  BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";
	        ArrayList<String[]> content=new ArrayList<String[]>();
	        br = new BufferedReader(new FileReader(FilesPath+"\\TestData.csv"));
        	while ((line = br.readLine()) != null) {
            // use comma as separator
        	String[] l=	line.toString().split(cvsSplitBy);
            content.add(l);
  
        	}
        	br.close();
		 for(int i=1;i<content.size();i++)
			  content.get(i)[7]=YesNoRes.get(i-1);
		 
		 
		 
		 
		 
		 File f = new File(FilesPath+"\\newTestData.csv");
		 if (f.exists()){
		     f.delete();
		 }  
		 FileWriter writer = new FileWriter(FilesPath+"\\newTestData.csv", true);
		 for(int i=0;i<content.size();i++) {
			 for(int j=0;j<8;j++) {
				 writer.append(content.get(i)[j]);
			     writer.append(cvsSplitBy);
			 }
			 
			 writer.append('\n');
			 writer.flush();

		 }
		 writer.close();
	}
	
	public void TestDataSet(Instances DataSet) throws Exception
	{
		setTestData(DataSet);
		getTestData().setClassIndex(getTestData().numAttributes()-1);

		for(int i=0;i<getTestData().numInstances();i++)
		{
			//double actualclass=getTestData().instance(i).classValue();
			//String actual=getTestData().classAttribute().value((int) (actualclass));
			Instance newInst=getTestData().instance(i);
			
			
			double pred=(((AbstractClassifier) getClassifer()).classifyInstance(newInst));
			String prdString=getTestData().classAttribute().value((int) pred);
			System.out.println(getTestData().instance(i)+":	"+prdString+"  ");
			YesNoRes.add(prdString);
		}

	}
}
