/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.experiment;

import Util.ClearParserUtil;
import Util.ProcessFrameUtil;
import clear.engine.SRLTrain;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import qa.ProcessFrame;
import qa.ProcessFrameProcessor;

/**
 *
 * @author samuellouvan
 */
public class SRLCombinedModelExp {

    ProcessFrameProcessor proc;

    @Option(name = "-f", usage = "process file", required = true, metaVar = "REQUIRED")
    private String processTsvFileName;

    @Option(name = "-o", usage = "output directory name", required = true, metaVar = "REQUIRED")
    private String outDirName;

    private ArrayList<ProcessFrame> frameArr;
    private HashMap<String, Integer> processFold;

    public SRLCombinedModelExp() throws FileNotFoundException {

        processFold = new HashMap<String, Integer>();
    }

    public void init() throws FileNotFoundException {
        proc = new ProcessFrameProcessor(processTsvFileName);
        proc.loadProcessData();
        frameArr = proc.getProcArr();
        for (int i = 0; i < frameArr.size(); i++) {
            String normName = ProcessFrameUtil.normalizeProcessName(frameArr.get(i).getProcessName());
            processFold.put(normName, 0);
        }
        File outDirHandler = new File(outDirName);
        if (outDirHandler.exists()) {
            return;
        }
        boolean success = outDirHandler.mkdir();

        if (!success) {
            System.out.println("FAILED to create output directory");
            System.exit(0);
        }
    }

    public void trainAndPredict() throws FileNotFoundException, IOException, InterruptedException {
        ArrayList<String> testFileName = new ArrayList<String>();
        ArrayList<String> trainingModelFileName = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            ProcessFrame testFrame = frameArr.get(i);
            String normalizedProcessName = ProcessFrameUtil.normalizeProcessName(testFrame.getProcessName());
            int fold = processFold.get(normalizedProcessName);
            ProcessFrameUtil.toClearParserFormat(testFrame, outDirName + "/" + normalizedProcessName + ".test.cv." + fold);  // out to <process_frame_>.test.cv.<fold>
            processFold.put(normalizedProcessName, fold + 1);

            // Get the testing data
            ArrayList<ProcessFrame> trainingFrames = new ArrayList<ProcessFrame>();
            for (int j = 0; j < frameArr.size(); j++) {
                if (i != j) {
                    trainingFrames.add(frameArr.get(j));
                }
            }
            String trainingFileName = outDirName + "/" + normalizedProcessName + ".train.combined.cv." + fold;
            String modelName = outDirName + "/" + normalizedProcessName + ".combinedmodel.cv." + fold;
            ProcessFrameUtil.toClearParserFormat(trainingFrames, trainingFileName);

            // Train trainingFrames
            SRLTrain train = new SRLTrain();
            CmdLineParser cmd = new CmdLineParser(train);

            try {
                ClearParserUtil.TRAIN_ARGS[3] = trainingFileName;
                ClearParserUtil.TRAIN_ARGS[7] = modelName;
                cmd.parseArgument(ClearParserUtil.TRAIN_ARGS);
                train.init();
                train.train();
            } catch (CmdLineException e) {
                System.err.println(e.getMessage());
                cmd.printUsage(System.err);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            // Perform prediction
        }
        Thread.sleep(10000);
        for (int i = 0; i < testFileName.size(); i++) {
            
        }
        // Prediction time
    }

    /**
     * Compute the precision, recall, F1 of the predictions by executing
     * combine.py and evaluate.py
     */
    /*public void evaluate()
     {
        
     }*/
    public static void main(String[] args) throws FileNotFoundException {
        SRLCombinedModelExp srlExp = new SRLCombinedModelExp();
        CmdLineParser cmd = new CmdLineParser(srlExp);

        try {
            cmd.parseArgument(args);
            srlExp.init();
            srlExp.trainAndPredict();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            cmd.printUsage(System.err);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
