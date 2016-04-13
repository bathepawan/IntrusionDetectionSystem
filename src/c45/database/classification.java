/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package c45.database;
/*import c45.algorithm.binary.C4;
import c45.preprocess.*;*/
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;


/**
 *
 * @author COMPSAKING
 */
public class classification {


    public static void main(String args[])
    {
        try {
            BufferedReader reader = null;
            Instances trainingSet = null;
            Instances testingSet=null;
            J48 cmodel=null;
            Evaluation eval=null;
            
            try {
                String dir = System.getProperty("user.dir");
                reader = new BufferedReader(new FileReader(dir + "\\dataset1.arff"));
                trainingSet = new Instances(reader);//Training Instances
                reader=new BufferedReader(new FileReader(dir + "\\dataset1.arff"));//Testing Instances
                testingSet=new Instances(reader);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(classification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(classification.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(classification.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
            cmodel= new J48();
            cmodel.buildClassifier(trainingSet);
            eval=new Evaluation(trainingSet);
            testingSet.setClassIndex(testingSet.numAttributes()-1);
            eval.evaluateModel(cmodel,testingSet);
            String strSummary = eval.toSummaryString();
            System.out.println(strSummary);
            double[][] cmMatrix = eval.confusionMatrix();
            System.out.println("******Confusion Matrix******");
            for(int i=0;i<5;i++)
            {
            for(int j=0;j<5;j++)
            {
                System.out.format("%4.0f  ",cmMatrix[i][j]);
            }
            System.out.println();
            }

      Instances unlabeled=new Instances(new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\dataset2.arff")));
      // set class attribute
      unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
 
 // create copy
      Instances labeled = new Instances(unlabeled);
 
 // label instances
 for (int i = 0; i < unlabeled.numInstances(); i++) {
   double clsLabel = cmodel.classifyInstance(unlabeled.instance(i));
   labeled.instance(i).setClassValue(clsLabel);
   }
                           BufferedWriter writer = new BufferedWriter(
                           new FileWriter(System.getProperty("user.dir")+"\\labeled.arff"));
                           writer.write(labeled.toString());
                           writer.newLine();
                           writer.flush();
                           writer.close();
 
        } catch (Exception ex) {
            Logger.getLogger(classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
