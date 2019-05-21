package WekaPac;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import Classify.*;
import Classify.EnumClassify;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//PreProcessing Weka 
public class WekaClass {

	
	public static void main(String[] args) throws Exception {

		String Action = args[0];
		String TrainingData = args[1];
		String Trainingrff = args[2];
		String TestData = args[3];
		String Testrff = args[4];
		String SaveModelPath = args[5];
		String ModelName = args[6];
		String JsonPath = args[7];

		if(ActionEnum.ConvertToEnum(Action.toString())==ActionEnum.Rank)
		{
			//Rank the Data
			InfoGainAttributeEval eval=new InfoGainAttributeEval();
			//Load Arff file
		 	CSVLoader loader = new CSVLoader();
		    loader.setSource(new File(TrainingData));
		    Instances data = loader.getDataSet();
			data.setClass(data.attribute("Class"));
			eval.buildEvaluator(data);
			/////////////////////////////Create json file
			 BuildJsonRankedAttributes(data,JsonPath,eval);
		}
		else if(ActionEnum.ConvertToEnum(Action.toString())==ActionEnum.Classify)
		{
			ArrayList<EnumClassify> Classifiers = new ArrayList<EnumClassify>();

			//Create TrainingData
			Instances TrainData=CreateRffFile(TrainingData,Trainingrff,JsonPath.toString());
			
			
			//ClassifyAll
			 for(int i = 8; i < args.length; i++)
			 {
				 Classifiers.add(EnumClassify.ConvertToEnum(args[i].toString()));
			 }
			 BuildClassifiers(Classifiers,TrainData,SaveModelPath);

		}
		else if(ActionEnum.ConvertToEnum(Action.toString()) == ActionEnum.Test)
		{
			System.out.println("Loading Model...");
			
			//Create TestData
			Instances TestDataarff=CreateRffFile(TestData,Testrff,null);
			System.out.println("Test Arff Created");
			
			//Change Class Type String To Nominal
			TestDataarff = StringToNominalOveride(TestDataarff,SaveModelPath, TrainingData);
			String CsvPath = TrainingData.substring(0, TrainingData.lastIndexOf(File.separator));
			System.out.println("Csv Path: " + CsvPath);
			LoadAndTest(SaveModelPath,ModelName,TestDataarff, CsvPath);
			 
		}
	}
	
	public static void RemoveAttribute(String JsonPath) throws FileNotFoundException, IOException, ParseException
	{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(JsonPath));
		
	}
	
	@SuppressWarnings("unchecked")//the function json.put is unsave on java
	public static void BuildJsonRankedAttributes(Instances data,String Path,InfoGainAttributeEval eval) throws Exception
	{
		 FileWriter file = new FileWriter(Path+"\\AttributesRanks.json");
		 file.append('[');
		 for(int i=2;i<data.numAttributes()-1;i++) {
		 JSONObject json = new JSONObject(); //creates main json
		
		 json.put("Attribute", data.attribute(i).name());
		 json.put("Rank", String.valueOf(eval.evaluateAttribute(i)));
		 json.put("Wantit?(YES/NO)", "");
		 try {
			    file.write(json.toJSONString());
			    file.flush();
			    
			} catch (Exception ex) {
			    System.out.println("error: " + ex.toString());
			}
		 }
		 file.append(']');
		 file.close();
	}
	public static void LoadAndTest(String SaveModelPath,String ModelName,Instances TestDataarff, String CsvPath) throws Exception
	{
		 if(EnumClassify.ConvertToEnum(ModelName)==EnumClassify.DecisionStump)
		 {
			 DecisionStumpClassifier decisionStumpClassifier=new DecisionStumpClassifier(null);
			 decisionStumpClassifier.LoadModel(SaveModelPath);
			 System.out.println("\n===============DecisionStump===============");
			 decisionStumpClassifier.TestDataSet(TestDataarff);
			 decisionStumpClassifier.SetChangeOnCSVFile(CsvPath); 
		 }
		 else if(EnumClassify.ConvertToEnum(ModelName)==EnumClassify.J48)
		 {
			 J48TreeClassifier j48TreeClassifier=new J48TreeClassifier(null);
			 j48TreeClassifier.LoadModel(SaveModelPath);
			 System.out.println("\n===============J48Tree===============");
			 j48TreeClassifier.TestDataSet(TestDataarff);
			 j48TreeClassifier.SetChangeOnCSVFile(CsvPath); 
		 }
		 else if(EnumClassify.ConvertToEnum(ModelName)==EnumClassify.NaiveBayes)
		 {
			 NaiveBayesClassifier nb=new NaiveBayesClassifier(null);
			 nb.LoadModel(SaveModelPath);
			 System.out.println("\n===============NaiveBayes===============");
			 nb.TestDataSet(TestDataarff);
			 nb.SetChangeOnCSVFile(CsvPath); 
		 }
		 else if(EnumClassify.ConvertToEnum(ModelName)==EnumClassify.NueralNetwork)
		 {
			 NueralNetworkClassifier nueralNetworkClassifier=new NueralNetworkClassifier(null);
			 nueralNetworkClassifier.LoadModel(SaveModelPath);
			 System.out.println("\n===============Nueral Network===============");
			 nueralNetworkClassifier.TestDataSet(TestDataarff);
			 nueralNetworkClassifier.SetChangeOnCSVFile(CsvPath); 
		 }
		 else if(EnumClassify.ConvertToEnum(ModelName)==EnumClassify.RandomTree)
		 {
			 RandomTreeClassifier randomTreeClass=new RandomTreeClassifier(null);
			 randomTreeClass.LoadModel(SaveModelPath);
			 System.out.println("===============Random Tree===============");
			 randomTreeClass.TestDataSet(TestDataarff);
			 randomTreeClass.SetChangeOnCSVFile(CsvPath); 
		 }
		 else if(EnumClassify.ConvertToEnum(ModelName)==EnumClassify.svm)
		 {
			 SVMClassifier SVM=new SVMClassifier(null);
			 SVM.LoadModel(SaveModelPath);
			 System.out.println("===============Random Tree===============");
			 SVM.TestDataSet(TestDataarff);
			 SVM.SetChangeOnCSVFile(CsvPath); 
		 }
	}
	public static void BuildClassifiers(ArrayList<EnumClassify> Classifiers,Instances TrainData, String SaveModelPath) throws Exception
	{
		 for(int i=0;i<Classifiers.size();i++)
		 {
			 if(Classifiers.get(i)==EnumClassify.DecisionStump)
			 {
				DecisionStumpClassifier decisionStumpClass=new DecisionStumpClassifier(TrainData);
				decisionStumpClass.Build();
				decisionStumpClass.SaveModel(SaveModelPath);
			 }
			 else if(Classifiers.get(i)==EnumClassify.J48)
			 {
				J48TreeClassifier j48TreeClassifier=new J48TreeClassifier(TrainData);
				j48TreeClassifier.Build();
				j48TreeClassifier.SaveModel(SaveModelPath);
			 }
			 else if(Classifiers.get(i)==EnumClassify.NaiveBayes)
			 {
				 NaiveBayesClassifier nb=new NaiveBayesClassifier(TrainData);
				 nb.Build();
				 nb.SaveModel(SaveModelPath);
			 }
			 else if(Classifiers.get(i)==EnumClassify.NueralNetwork)
			 {
				NueralNetworkClassifier nueralNetworkClassifier=new NueralNetworkClassifier(TrainData);
				nueralNetworkClassifier.Build();
				nueralNetworkClassifier.SaveModel(SaveModelPath);
			 }
			 else if(Classifiers.get(i)==EnumClassify.RandomTree)
			 {
				RandomTreeClassifier randomTreeClass=new RandomTreeClassifier(TrainData);
				randomTreeClass.Build();
				randomTreeClass.SaveModel(SaveModelPath);
			 }
		 }
	}
	public static Instances CreateRffFile(String CSVFilePath,String outputFilePath,String JsonPath) throws Exception
	{
			//Load Arff file
		 	CSVLoader loader = new CSVLoader();
		    loader.setSource(new File(CSVFilePath));
		    Instances data = loader.getDataSet();
		    
		    //Delete Attribute User Stories & Test Cases
		    data.deleteAttributeAt(0);
		    data.deleteAttributeAt(0);		
		    //Filter user Attribute Selection
		    if(JsonPath!=null)
		    	data=AttributesSelection(JsonPath,data);
		    //Build File Arff
		    ArffSaver saver = new ArffSaver();		  
		    saver.setInstances(data);
		    saver.setFile(new File(outputFilePath));
		    saver.writeBatch();
		    
		    return data;
	}
	public static Instances AttributesSelection(String jsonPath,Instances data) throws FileNotFoundException, IOException, ParseException
	{
		   //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         System.out.println(jsonPath);
        try (FileReader reader = new FileReader(jsonPath))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray AttrList = (JSONArray) obj;
            for(int i=0;i<data.numAttributes();i++) {
            	for (Object var : AttrList) 
            	{ 
            		if(((JSONObject) var).get("Wantit?(YES/NO)").equals("NO"))
            		{
            			data=FindAttributeAndDelete(data,((JSONObject) var).get("Attribute").toString()); 
            		}
            			       			
            	}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
	    }
	public static Instances FindAttributeAndDelete(Instances data,String attr)
	{
		for(int i=0;i<data.numAttributes();i++)
		{
			if(data.attribute(i).name().toString().equals(attr))
			{
				data.deleteAttributeAt(i);
			}
		}
		return data;
	}
	public static Instances StringToNominalOveride(Instances data,String TrainArffPath, String TrainingDataPath) throws Exception
	{	    
		boolean YesNoFlag=false;//false thats mean the no is first value
		
		//Check the first line of the train data(yes/no)
		CSVLoader loader = new CSVLoader();
	    loader.setSource(new File(TrainingDataPath));
	    Instances train = loader.getDataSet();
	    if(train.attribute(7).value(0).equals("Yes"))	  
	    	YesNoFlag=true;
	    //Delete attribute string
        int[] indics=new int[]{0,1, 2, 3, 4};
        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(indics);
        removeFilter.setInvertSelection(true);
        removeFilter.setInputFormat(data);
        data = Filter.useFilter(data, removeFilter);
		
	    // New nominal attribute
        Instances newData = new Instances(data);
        Add filter;
        filter = new Add();
        filter.setAttributeIndex("last");
        if(!YesNoFlag)
        	filter.setNominalLabels("No,Yes");
        else
        	filter.setNominalLabels("Yes,No");
        filter.setAttributeName("Class");
        filter.setInputFormat(newData);
        newData = Filter.useFilter(newData, filter);
        data=newData;
        
        //Update File Arff
	    ArffSaver saver = new ArffSaver();		  
	    saver.setInstances(data);
	    saver.setFile(new File(TrainArffPath+"//Test.Arff"));
	    saver.writeBatch();
        
        return data;
        
	}

}
