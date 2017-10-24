package edu.ucdavis.cs.cyberlab.BBPhosphor.util;

import edu.columbia.cs.psl.phosphor.TaintUtils;
import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

/**
 * Created by huang on 10/23/17.
 */
public class TaintHelper {

    public static void saveTaint(Object o) {
        Taint to = TaintUtils.getTaintObj(o);
        //Taint to = MultiTainter.getTaint(o);
        assert (to != null);
        System.out.println(to.toString());
    }

    public static void printx(String s) {
        System.out.println(s);
    }
}
