package fr.rhiobet;

import java.util.List;
import java.util.Map;

import fr.rhiobet.processors.StaticFieldsProcessor;
import spoon.MavenLauncher;
import spoon.SpoonAPI;
import spoon.reflect.declaration.CtField;

public class Main {

  public static void main(String args[]) {
    callStaticFieldsProcessor("/home/rhiobet/irit/jitsi-videobridge/", MavenLauncher.SOURCE_TYPE.APP_SOURCE);
  }
 
  
  /**
   * Will perform an analysis of the static fields in a Maven project.
   * 
   * The expected results are, for each class:
   * <br />&nbsp;&nbsp;- the number of static fields
   * <br />&nbsp;&nbsp;- the number of final static fields
   * <br />&nbsp;&nbsp;- the number of finalizable static fields
   * <br />&nbsp;&nbsp;- the actual finalizable static fields
   * 
   * <br />Also, the total numbers for all the classes.
   * @param project path to the Maven project
   * @param sourceType the type of sources to consider
   */
  private static void callStaticFieldsProcessor(String project, MavenLauncher.SOURCE_TYPE sourceType) {
    SpoonAPI spoon = new MavenLauncher(project, sourceType);
  
    spoon.addProcessor("fr.rhiobet.processors.StaticFieldsProcessor");
    
    spoon.buildModel();
    spoon.process();
    StaticFieldsProcessor.setFirstPassDone();
    spoon.process();
    
    Map<String, List<CtField<?>>> nonFinalStaticFields = StaticFieldsProcessor.getNonFinalStaticFields();
    Map<String, int[]> nbStaticFields = StaticFieldsProcessor.getNbStaticFields();
    int nbStaticFieldsTotal = StaticFieldsProcessor.getNbStaticFieldsTotal();
    int nbStaticFinalFieldsTotal = StaticFieldsProcessor.getNbFinalStaticFieldsTotal();
    int nbNonFinalizableFields = StaticFieldsProcessor.getNbNonFinalizableFieldsTotal();
    
    for (Map.Entry<String, int[]> entry : nbStaticFields.entrySet()) {
      int nbs[] = entry.getValue();
      System.out.println("Class: " + entry.getKey());
      System.out.println("    " + nbs[0] + " static fields, " + nbs[1] + " final and " + nbs[2] + " finalizable");
      if (nbs[2] > 0) {
        System.out.println("    Finalizable fields:");
        for (CtField<?> field : nonFinalStaticFields.get(entry.getKey())) {
          System.out.println("        " + field.getSimpleName());
        }
      }
    }
    
    System.out.println();
    System.out.println("Total: " + nbStaticFieldsTotal + " static fields, " + nbStaticFinalFieldsTotal + " final and "
            + (nbStaticFieldsTotal - nbStaticFinalFieldsTotal - nbNonFinalizableFields) + " finalizable");
  }
  
}
