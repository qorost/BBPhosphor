package edu.ucdavis.cs.cyberlab.BBPhosphor.util;

import java.util.HashSet;

/**
 * Created by huang on 10/10/17.
 */
public class XDebug {

    public static boolean DEBUG = true;

    public static void DebugME(String msg) {
        if(DEBUG) System.out.println("DEBUG " + msg);
    }

    public static <T> void printHashSet(HashSet<T> s) {
        for(T t: s) {
            DebugME("\t" + t.toString());
        }
    }
}
