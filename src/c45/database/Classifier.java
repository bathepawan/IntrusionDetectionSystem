/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package c45.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author SAGAR
 */
public class Classifier {
    Instances trainingSet;
    Instances testingSet;
    J48 cmodel=null;
    Evaluation eval=null;
    Instance inst;
    Classifier()
    {
    trainClassifier(System.getProperty("user.dir")+"\\dataset1.arff");
    testClassifier(System.getProperty("user.dir")+"\\dataset12.arff");
    inst=new Instance(14);
    }
 public  Instance classify(Instance instance)
 {
        try {
            double clsLabel = cmodel.classifyInstance(instance);
              instance.setClassValue(clsLabel);
            
        } catch (Exception ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
 }
 private void trainClassifier(String trainingData)
 {
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(trainingData));
                trainingSet = new Instances(reader);//Training Instances
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex)
            {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
                trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
                cmodel= new J48();
                cmodel.buildClassifier(trainingSet);
            } catch (Exception ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
 }

    private void testClassifier(String testingData) 
    {
     try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(testingData));
                testingSet = new Instances(reader);//Training Instances
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex)
            {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                eval=new Evaluation(trainingSet);
                testingSet.setClassIndex(testingSet.numAttributes() - 1);
                eval.evaluateModel(cmodel,testingSet);
            String strSummary = eval.toSummaryString();
            System.out.println(strSummary);
            double[][] cmMatrix = eval.confusionMatrix();
            System.out.println("******Confusion Matrix******");
            for(int i=0;i<2;i++)
            {
            for(int j=0;j<2;j++)
            {
                System.out.format("%4.0f  ",cmMatrix[i][j]);
            }
            System.out.println();
            }

                } catch (Exception ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}
