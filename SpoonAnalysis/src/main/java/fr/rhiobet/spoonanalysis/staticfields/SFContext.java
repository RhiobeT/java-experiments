package fr.rhiobet.spoonanalysis.staticfields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spoon.reflect.declaration.CtField;

public class SFContext {
  
  private static int nbStaticFieldsTotal = 0;
  private static int nbFinalStaticFieldsTotal = 0;
  private static int nbNonFinalizableFieldsTotal = 0;
 
  private static boolean firstPassDone = false;
  
  private static Map<String, List<CtField<?>>> nonFinalStaticFields = new HashMap<>();
  private static Map<String, int[]> nbStaticFields = new HashMap<>();

  /**
   * Resets the context.
   * 
   * Should be called between multiple uses of the processor.
   */
  public static void reset() {
    nbStaticFieldsTotal = 0;
    nbFinalStaticFieldsTotal = 0;
    nbNonFinalizableFieldsTotal = 0;
    firstPassDone = false;
    nonFinalStaticFields = new HashMap<>();
    nbStaticFields = new HashMap<>();
  }
  
  /**
   * Sets that the first pass is done.
   * 
   * This function should be called after the first execution of the processor.
   */
  public static void setFirstPassDone() {
    firstPassDone = true;
  }
  
  /**
   * Indicates whether the first pass has been done or not.
   * 
   * @return whether the first pass has been done or not
   */
  public static boolean isFirstPassDone() {
    return firstPassDone;
  }
  
  /**
   * Returns the static fields that are not final, or not finalizable, for each class in the project.
   * @return non final fields after first pass, non finalizable fields after second pass
   */
  public static Map<String, List<CtField<?>>> getNonFinalStaticFields() {
    return nonFinalStaticFields;
  }
  
  /**
   * Returns data on the number of static fields in each class of the project.
   * 
   * For each class, the array contains 3 values after the first pass:
   * <br />&nbsp;&nbsp;- the number of static fields
   * <br />&nbsp;&nbsp;- the number of final static fields
   * <br />&nbsp;&nbsp;- the number of finalizable static fields (after the second pass)
   * @return the number of static fields and static final fields after the first pass, and also finalizable static
   * after the second pass
   */
  public static Map<String, int[]> getNbStaticFields() {
    return nbStaticFields;
  }
  
  /**
   * Sets the number of static fields in the specified class.
   * @param className the name of the wanted class
   * @param nbStatics the number of static fields in the class
   * @param nbFinalStatics the number of final static fields in the class
   * @param nbFinalizables the number of finalizable static fields in the class
   */
  public static void setNbStaticFields(String className, int nbStatics, int nbFinalStatics, int nbFinalizables) {
    nbStaticFields.put(className, new int[] {nbStatics, nbFinalStatics, nbFinalizables});
  }
  
  /**
   * Returns data on the number of static fields in the specified class.
   * 
   * The returned array contains 3 values after the first pass:
   * <br />&nbsp;&nbsp;- the number of static fields
   * <br />&nbsp;&nbsp;- the number of final static fields
   * <br />&nbsp;&nbsp;- the number of finalizable static fields (after the second pass)
   * @param className the name of the wanted class
   * @return the number of static fields and static final fields after the first pass, and also finalizable static
   * after the second pass
   */ 
  public static int[] getNbStaticFields(String className) {
    return nbStaticFields.get(className);
  }
 
  /**
   * Returns the total number of static fields in the project after the first pass.
   * @return the total number of static fields after the first pass
   */
  public static int getNbStaticFieldsTotal() {
    return nbStaticFieldsTotal;
  }
  
  /**
   * Increments the number of static fields in the project.
   * @param increment the number to use as increment
   */
  public static void incNbStaticFieldsTotal(int increment) {
    nbStaticFieldsTotal += increment;
  }
  
  /**
   * Returns the total number of final static fields in the project after the first pass.
   * @return the total number of final static fields after the first pass
   */
  public static int getNbFinalStaticFieldsTotal() {
    return nbFinalStaticFieldsTotal;
  }
  
  /**
   * Increments the number of final static fields in the project.
   * @param increment the number to use as increment
   */
  public static void incNbFinalStaticFieldsTotal(int increment) {
    nbFinalStaticFieldsTotal += increment;
  }
  
  /**
   * Returns the total number of non finalizable static fields in the project after the second pass.
   * @return the total number of non finalizable static fields after the second pass
   */
  public static int getNbNonFinalizableFieldsTotal() {
    return nbNonFinalizableFieldsTotal;
  }
  
  /**
   * Increments the total number of non finalizable static fields.
   * @param increment the number to use as increment
   */
  public static void incNbNonFinalizableFieldsTotal(int increment) {
    nbNonFinalizableFieldsTotal += increment;
  }
  
  /**
   * Adds a field to the list of non final static fields for the specified class.
   * @param className the name of the wanted class
   * @param field the field to add
   */
  public static void addNonFinalStaticField(String className, CtField<?> field) {
    if (nonFinalStaticFields.get(className) == null) {
      nonFinalStaticFields.put(className, new ArrayList<>());
    }
    nonFinalStaticFields.get(className).add(field);
  }
  
}
