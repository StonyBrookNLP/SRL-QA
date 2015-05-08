/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.IOException;

/**
 *
 * @author samuellouvan
 */
public class ClearParserUtil {

    public static void clearParserTrain(String modelName, String trainingFileName) throws IOException {
        String[] cmdarray = new String[]{
            "java", "-classpath", System.getProperty("java.class.path"), "clear.engine.SRLTrain", "-c", "clearparser-lib/config/config_srl_en.xml",
            "-t", "clearparser-lib/config/feature_srl_en_conll09.xml", "-m", modelName, "-i", trainingFileName};

        Process trainProcess = Runtime.getRuntime().exec(cmdarray);
        StdUtil.printError(trainProcess);
    }

    public static double clearParserPredict(String modelName, String testingFileName, String predictionFileName) throws IOException {
        String[] cmdarray = new String[]{
            "java", "-classpath", System.getProperty("java.class.path"), "clear.engine.SRLPredict", "-c", "clearparser-lib/config/config_srl_en.xml", "-m", modelName, "-i", testingFileName, "-o", predictionFileName};
        Process predictProcess = Runtime.getRuntime().exec(cmdarray);
        double score = StdUtil.getPredictionConfidenceScore(predictProcess);
        //StdUtil.getRawOutput(predictProcess);
        StdUtil.printError(predictProcess);

        return score;
    }

    public static void clearParserPredictOnly(String modelName, String testingFileName, String predictionFileName) throws IOException {
        String[] cmdarray = new String[]{
            "java", "-classpath", System.getProperty("java.class.path"), "clear.engine.SRLPredict", "-c", "clearparser-lib/config/config_srl_en.xml", "-m", modelName, "-i", testingFileName, "-o", predictionFileName};
        Process predictProcess = Runtime.getRuntime().exec(cmdarray);
        //StdUtil.printError(predictProcess);
        StdUtil.printOutput(predictProcess);
    }

    public static String clearParserEval(String process, int fold, String testingFileName, String predictionFileName) throws IOException {
        String[] cmdarray = new String[]{
            "java", "-classpath", System.getProperty("java.class.path"), "clear.engine.SRLEvaluate", "-g", testingFileName, "-s", predictionFileName};
        Process evaluateProcess = Runtime.getRuntime().exec(cmdarray);
        StdUtil.printError(evaluateProcess);
        String results = StdUtil.getOutput(process, fold, evaluateProcess);
        return results;
    }
    public static void clearParserEvalOnly(String testingFileName, String predictionFileName) throws IOException {
        String[] cmdarray = new String[]{
            "java", "-classpath", System.getProperty("java.class.path"), "clear.engine.SRLEvaluate", "-g", testingFileName, "-s", predictionFileName};
        Process evaluateProcess = Runtime.getRuntime().exec(cmdarray);
        StdUtil.printOutput(evaluateProcess);
    }
    
    public static void main(String[] args) throws IOException {
        //System.out.println(System.getProperty("java.class.path"));
        clearParserPredict("/Users/samuellouvan/Downloads/clearparser-read-only/data/evaporate_evaporation.jointmodel.0",
                "/Users/samuellouvan/Downloads/clearparser-read-only/data/temp.clearparser",
                "test.out");
    }
}
