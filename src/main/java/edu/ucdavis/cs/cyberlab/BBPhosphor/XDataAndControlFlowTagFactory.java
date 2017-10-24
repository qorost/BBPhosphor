package edu.ucdavis.cs.cyberlab.BBPhosphor;

//Error would get prompted if using objectweb
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.Type;

import edu.columbia.cs.psl.phosphor.org.objectweb.asm.Type;
import edu.columbia.cs.psl.phosphor.org.objectweb.asm.Label;
import edu.columbia.cs.psl.phosphor.org.objectweb.asm.MethodVisitor;
import edu.columbia.cs.psl.phosphor.org.objectweb.asm.Opcodes;

import edu.columbia.cs.psl.phosphor.Configuration;
import edu.columbia.cs.psl.phosphor.TaintUtils;
import edu.columbia.cs.psl.phosphor.instrumenter.DataAndControlFlowTagFactory;
import edu.columbia.cs.psl.phosphor.instrumenter.LocalVariableManager;
import edu.columbia.cs.psl.phosphor.instrumenter.TaintPassingMV;
import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.struct.ControlTaintTagStack;
import edu.ucdavis.cs.cyberlab.BBPhosphor.util.TaintHelper;

import static edu.ucdavis.cs.cyberlab.BBPhosphor.util.XDebug.DebugME;


/**
 * Created by huang on 10/9/17.
 */
public class XDataAndControlFlowTagFactory extends DataAndControlFlowTagFactory {


    boolean isIgnoreAcmp;

    @Override
    public void instrumentationStarting(String className) {
        isIgnoreAcmp = className.equals("java/io/ObjectOutputStream$HandleTable");
    }

    public void stackOp(int opcode, MethodVisitor mv, LocalVariableManager lvs, TaintPassingMV adapter) {
        //DebugME("STACKOP TEST");
        super.stackOp(opcode, mv, lvs, adapter);
    }

    public void jumpOp(int opcode, int branchStarting, Label label, MethodVisitor mv, LocalVariableManager lvs, TaintPassingMV ta) {
        DebugME("Entering jumpOp. Configuration.WITH_TAGS_FOR_JUMPS = " + Configuration.WITH_TAGS_FOR_JUMPS + " opcode = " + opcode);
        if ((Configuration.IMPLICIT_TRACKING || Configuration.IMPLICIT_LIGHT_TRACKING) && !Configuration.WITHOUT_PROPOGATION) {
            switch (opcode) {
                case Opcodes.IFEQ:
                case Opcodes.IFNE:
                case Opcodes.IFLT:
                case Opcodes.IFGE:
                case Opcodes.IFGT:
                case Opcodes.IFLE:
                    //FIXME always failed with this
                    //Added by huang, to store the taint information.
                    mv.visitInsn(DUP);//duplicate the object
                    mv.visitMethodInsn(INVOKESTATIC,Type.getInternalName(TaintHelper.class), "saveTaint", "(Ljava/lang/Object;)V",false);
                    //This is correct
                    mv.visitLdcInsn("hello i coupied this! branching counting " + Integer.toString(branchStarting));
                    mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(TaintHelper.class),"printx","(Ljava/lang/String;)V",false);


                    mv.visitInsn(SWAP);
                    //FIXME mv.visitVarInsn(ALOAD, lvs.idxOfMasterControlLV);
                    DebugME("Before first lvs.getIdxOfMasterControlLV, value: " + lvs.getIdxOfMasterControlLV());
                    mv.visitVarInsn(ALOAD, lvs.getIdxOfMasterControlLV());
                    DebugME("After first lvs.getIdxOfMasterControlLV, value: " + lvs.getIdxOfMasterControlLV());
                    mv.visitInsn(SWAP);
                    //mv.visitInsn(DUP); // the taint object  (Ljava/lang/Object;)Ledu/columbia/cs/psl/phosphor/runtime/Taint;
                    //mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MultiTainter.class),"getTaint","(" + Configuration.TAINT_TAG_DESC + "L",false);
                    //mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MultiTainter.class),"getTaint","(Ljava/lang/Object;)Ledu/columbia/cs/psl/phosphor/runtime/Taint;",false);
                    //mv.visitInsn(POP);

                    //call my function to store the taint object.
                    mv.visitVarInsn(ALOAD, ta.taintTagsLoggedAtJumps[branchStarting]);
                    mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ControlTaintTagStack.class), "push", "(" + Configuration.TAINT_TAG_DESC + "Ledu/columbia/cs/psl/phosphor/struct/EnqueuedTaint;" + ")" + "Ledu/columbia/cs/psl/phosphor/struct/EnqueuedTaint;", false);
                    mv.visitVarInsn(ASTORE, ta.taintTagsLoggedAtJumps[branchStarting]);
                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.IFNULL:
                case Opcodes.IFNONNULL:

                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.IF_ICMPEQ:
                case Opcodes.IF_ICMPNE:
                case Opcodes.IF_ICMPLT:
                case Opcodes.IF_ICMPGE:
                case Opcodes.IF_ICMPGT:
                case Opcodes.IF_ICMPLE:
                    DebugME("Before SECOND lvs.getIdxOfMasterControlLV, value: " + lvs.getIdxOfMasterControlLV());

                    //T V T V
                    int tmp = lvs.getTmpLV(Type.INT_TYPE);
                    //T V T V
                    mv.visitInsn(SWAP);
                    mv.visitInsn(TaintUtils.IS_TMP_STORE);
                    mv.visitVarInsn(Configuration.TAINT_STORE_OPCODE, tmp);
                    //T V V
                    mv.visitInsn(DUP2_X1);
                    mv.visitInsn(POP2);
                    //V V T
                    //FIXME mv.visitVarInsn(ALOAD, lvs.idxOfMasterControlLV);
                    mv.visitVarInsn(ALOAD, lvs.getIdxOfMasterControlLV());
                    DebugME("after SECOND lvs.getIdxOfMasterControlLV, value: " + lvs.getIdxOfMasterControlLV());

                    mv.visitInsn(SWAP);
                    //V V C T
                    //FIXME mv.visitVarInsn(ALOAD, lvs.idxOfMasterControlLV);
                    DebugME("Before third lvs.getIdxOfMasterControlLV, value: " + lvs.getIdxOfMasterControlLV());
                    mv.visitVarInsn(ALOAD, lvs.getIdxOfMasterControlLV());
                    DebugME("After third lvs.getIdxOfMasterControlLV, value: " + lvs.getIdxOfMasterControlLV());

                    mv.visitVarInsn(Configuration.TAINT_LOAD_OPCODE, tmp);
                    lvs.freeTmpLV(tmp);
                    //V V C T CT
                    mv.visitVarInsn(ALOAD, ta.taintTagsLoggedAtJumps[branchStarting]);
                    mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ControlTaintTagStack.class), "push", "(" + Configuration.TAINT_TAG_DESC + "Ledu/columbia/cs/psl/phosphor/struct/EnqueuedTaint;" + ")" + "Ledu/columbia/cs/psl/phosphor/struct/EnqueuedTaint;", false);
                    mv.visitVarInsn(ASTORE, ta.taintTagsLoggedAtJumps[branchStarting]);
                    mv.visitVarInsn(ALOAD, ta.taintTagsLoggedAtJumps[branchStarting + 1]);
                    mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ControlTaintTagStack.class), "push", "(" + Configuration.TAINT_TAG_DESC + "Ledu/columbia/cs/psl/phosphor/struct/EnqueuedTaint;" + ")" + "Ledu/columbia/cs/psl/phosphor/struct/EnqueuedTaint;", false);
                    mv.visitVarInsn(ASTORE, ta.taintTagsLoggedAtJumps[branchStarting + 1]);
                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.IF_ACMPNE:
                case Opcodes.IF_ACMPEQ:

                    if (!isIgnoreAcmp && Configuration.WITH_UNBOX_ACMPEQ && (opcode == Opcodes.IF_ACMPEQ || opcode == Opcodes.IF_ACMPNE)) {
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(TaintUtils.class), "ensureUnboxed", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                        mv.visitInsn(SWAP);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(TaintUtils.class), "ensureUnboxed", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                        mv.visitInsn(SWAP);
                    }
                    mv.visitJumpInsn(opcode, label);

                    break;
                case Opcodes.GOTO:
                    mv.visitJumpInsn(opcode, label);
                    break;
                default:
                    throw new IllegalStateException("Unimplemented: " + opcode);
            }
        } else {
            switch (opcode) {
                case Opcodes.IFEQ:
                case Opcodes.IFNE:
                case Opcodes.IFLT:
                case Opcodes.IFGE:
                case Opcodes.IFGT:
                case Opcodes.IFLE:
                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.IF_ICMPEQ:
                case Opcodes.IF_ICMPNE:
                case Opcodes.IF_ICMPLT:
                case Opcodes.IF_ICMPGE:
                case Opcodes.IF_ICMPGT:
                case Opcodes.IF_ICMPLE:
                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.IF_ACMPEQ:
                case Opcodes.IF_ACMPNE:
                    if (!isIgnoreAcmp && Configuration.WITH_UNBOX_ACMPEQ && (opcode == Opcodes.IF_ACMPEQ || opcode == Opcodes.IF_ACMPNE)) {
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(TaintUtils.class), "ensureUnboxed", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                        mv.visitInsn(SWAP);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(TaintUtils.class), "ensureUnboxed", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                        mv.visitInsn(SWAP);
                    }
                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.GOTO:
                    //we don't care about goto
                    mv.visitJumpInsn(opcode, label);
                    break;
                case Opcodes.IFNULL:
                case Opcodes.IFNONNULL:
                    mv.visitJumpInsn(opcode, label);
                    break;
                default:
                    throw new IllegalArgumentException();
            }

        }
    }

}
