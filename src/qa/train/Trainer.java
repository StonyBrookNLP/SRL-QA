/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.train;

import Util.ClearParserUtil;
import Util.GlobalVariable;
import clear.engine.SRLTrain;
import static edu.stanford.nlp.util.logging.RedwoodConfiguration.Handlers.file;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import qa.ProcessFrame;
import qa.ProcessFrameProcessor;
import qa.StanfordDepParser;
import qa.StanfordTokenizer;
import qa.dep.DependencyTree;
import qa.util.FileUtil;

/**
 *
 * @author samuellouvan
 */
public class Trainer {

    private String PROJECT_PATH = "/Users/samuellouvan/NetBeansProjects/QA";

    String[] trainingArgs = {"-c", GlobalVariable.PROJECT_DIR.concat("/config/config_srl_en.xml"),
        "-i", "",
        "-t", GlobalVariable.PROJECT_DIR.concat("config/feature_srl_en_conll09.xml"),
        "-m", ""};

    /**
     *
     * @param tsvFileName the source of the process file data
     * @param modelName the model name
     */
    public void train(String dirName, String tsvFileName, String clearParserFileName) throws FileNotFoundException, IOException {
        // load data from tsv
        ProcessFrameProcessor proc = new ProcessFrameProcessor(dirName+tsvFileName);
        proc.loadProcessData();
        proc.toClearParserFormat(dirName+clearParserFileName); // it will create tsvFileName.clearparser

        SRLTrain train = new SRLTrain();
        CmdLineParser cmd = new CmdLineParser(train);

        try {
            trainingArgs[3] = dirName + clearParserFileName;
            trainingArgs[7] = dirName + FileUtil.getFileNameWoExt(tsvFileName) + ".model";
            System.out.println(Arrays.toString(trainingArgs));
            cmd.parseArgument(trainingArgs);
            train.init();
            train.train();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            cmd.printUsage(System.err);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) throws IOException {
        Trainer trainer = new Trainer();
        trainer.train("/Users/samuellouvan/NetBeansProjects/QA/data/processes_18_may/","evaporate|evaporation.tsv", "evaporation.clearparser");
    }
}
