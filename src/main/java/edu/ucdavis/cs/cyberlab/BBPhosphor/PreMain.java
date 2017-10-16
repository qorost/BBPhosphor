package edu.ucdavis.cs.cyberlab.BBPhosphor;

import java.lang.instrument.Instrumentation;
import edu.columbia.cs.psl.phosphor.Configuration;
import edu.columbia.cs.psl.phosphor.instrumenter.DataAndControlFlowTagFactory;
import edu.columbia.cs.psl.phosphor.struct.ControlTaintTagStack;

import static edu.ucdavis.cs.cyberlab.BBPhosphor.util.XDebug.DebugME;

/**
 * Created by huang on 10/9/17.
 */
public class PreMain {

    public static void premain$$PHOSPHORTAGGED(String args, Instrumentation inst, ControlTaintTagStack ctrl) {
        DebugME("hello from premain$$PHOSPHORTAGGED of XPhosphor!");
        Configuration.IMPLICIT_TRACKING = true;
        Configuration.MULTI_TAINTING = true;
        Configuration.init();
        premain(args, inst);
    }

    public static void premain(String args, Instrumentation inst) {
        DebugME("hello from premain of BBPhosphor!");
        DebugME("Input args: " + args);

        Configuration.WITH_TAGS_FOR_JUMPS = true;
        DebugME("Configuration.with_tags_for_jumps: " + Configuration.WITH_TAGS_FOR_JUMPS);


        Configuration.taintTagFactory = new DataAndControlFlowTagFactory();
        edu.columbia.cs.psl.phosphor.PreMain.premain(args, inst);
    }
}
