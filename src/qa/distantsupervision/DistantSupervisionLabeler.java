package qa.distantsupervision;

import Util.StringUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import qa.ProcessFrame;
import qa.ProcessFrameProcessor;
import qa.StanfordDocumentProcessor;
import qa.StanfordLemmatizer;

/**
 *
 * @author samuellouvan
 */
public class DistantSupervisionLabeler {

    private ProcessFrameProcessor proc; // To load all the data from process frame data
    private String processFrameFilename;  // Process frame fileName
    private String corpusFile;    // Target corpus as the source for distant supervision
    private ArrayList<String> triggers;
    private ArrayList<String> enablers;
    private ArrayList<String> undergoers;
    private ArrayList<String> results;
    private ArrayList<String> relevantSentences;
    private StanfordDocumentProcessor docProcessor;

    // Constructor
    public DistantSupervisionLabeler(String processFrameFilename, String corpusFile) {
        this.processFrameFilename = processFrameFilename;
        this.corpusFile = corpusFile;
    }

    // Initialize
    // Load the data from the process frame file
    public void init() throws FileNotFoundException {
        proc = new ProcessFrameProcessor(this.processFrameFilename);
        proc.loadProcessData();
        relevantSentences = new ArrayList<String>();
        docProcessor = new StanfordDocumentProcessor();
        undergoers = new ArrayList<String>();
        enablers = new ArrayList<String>();
        triggers = new ArrayList<String>();
        results = new ArrayList<String>();
    }

    // Load all role fillers
    public void loadRoleFillers(String processName) {
        System.out.println("START LOADING ROLE FILLERS");
        ArrayList<ProcessFrame> processsFrames = proc.getProcessFrameByName(processName);
        for (ProcessFrame p : processsFrames) {
            undergoers.addAll(StringUtil.getTokenAsList(p.getUnderGoer(), ProcessFrameProcessor.SEPARATOR));
            triggers.addAll(StringUtil.getTokenAsList(p.getTrigger(), ProcessFrameProcessor.SEPARATOR));
            enablers.addAll(StringUtil.getTokenAsList(p.getEnabler(), ProcessFrameProcessor.SEPARATOR));
            results.addAll(StringUtil.getTokenAsList(p.getResult(), ProcessFrameProcessor.SEPARATOR));
        }

        System.out.println("END LOADING ROLE FILLERS");

    }

    // Get the sentences from the target corpus 
    public ArrayList<String> getSentencesFromCorpus() throws FileNotFoundException {
        StringBuilder docStr = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.corpusFile));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            docStr.append(line);
        }
        return docProcessor.getSentences(docStr.toString());
    }

    
    public static void main(String[] args) throws FileNotFoundException {
        DistantSupervisionLabeler labeler = new DistantSupervisionLabeler("./data/all_process_frame.tsv", "./data/evaporation.txt");
        labeler.init();
        labeler.loadRoleFillers("evaporation");

    }
}
