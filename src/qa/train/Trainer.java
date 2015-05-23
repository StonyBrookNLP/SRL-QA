/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.train;

import Util.ClearParserUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import qa.ProcessFrame;
import qa.ProcessFrameProcessor;
import qa.StanfordDepParser;
import qa.StanfordTokenizer;
import qa.dep.DependencyTree;

/**
 *
 * @author samuellouvan
 */
public class Trainer {
    private StanfordDepParser depParser;
    public Trainer()
    {
        depParser = new StanfordDepParser();
    }
    
    /**
     * 
     * @param tsvFileName the source of the process file data
     * @param modelName the model name
     */
    public void train(String tsvFileName, String modelName) throws FileNotFoundException, IOException
    {
        // load data from tsv
        ProcessFrameProcessor proc = new ProcessFrameProcessor(tsvFileName);
        proc.loadProcessData();
        ArrayList<ProcessFrame> processFrames = proc.getProcArr();
        for (ProcessFrame p : processFrames)
        {
            String rawtext = p.getRawText();
            DependencyTree tree = depParser.parse(rawtext);
            System.out.println(tree.toString());
            String conLLStr = ClearParserUtil.toClearParserFormat(tree, p);
            System.out.println(conLLStr);
            
        }
        // convert to clear parser file
        
        // run SRL train
    }
    
    public static void main(String[] args) throws IOException
    {
        Trainer trainer = new Trainer();
        trainer.train("/Users/samuellouvan/NetBeansProjects/QA/data/processes_18_may/evaporate|evaporation.tsv", "");
    }
}
