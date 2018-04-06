package fr.rhiobet.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.visitor.chain.CtQuery;
import spoon.reflect.visitor.filter.FieldAccessFilter;

/**
 * Two passes processor that lists all the static fields in a project, then checks if they are final or could be.
 */
public class StaticFieldsProcessor extends AbstractProcessor<CtClass<?>> {

  private static int nbStaticFieldsTotal = 0;
  private static int nbFinalStaticFieldsTotal = 0;
  private static int nbNonFinalizableFieldsTotal = 0;
 
  private static boolean firstPassDone = false;
  
  private static Map<String, List<CtField<?>>> nonFinalStaticFields = new HashMap<>();
  private static Map<String, int[]> nbStaticFields = new HashMap<>();

  /**
   * Indicates that the first pass is done.
   * 
   * This function should be called after the first execution of this processor.
   */
  public static void setFirstPassDone() {
    firstPassDone = true;
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
   * Returns the total number of static fields in the project after the first pass.
   * @return the total number of static fields after the first pass
   */
  public static int getNbStaticFieldsTotal() {
    return nbStaticFieldsTotal;
  }
  
  /**
   * Returns the total number of final static fields in the project after the first pass.
   * @return the total number of final static fields after the first pass
   */
  public static int getNbFinalStaticFieldsTotal() {
    return nbFinalStaticFieldsTotal;
  }
  
  /**
   * Returns the total number of non finalizable static fields in the project after the second pass.
   * @return the total number of non finalizable static fields after the second pass
   */
  public static int getNbNonFinalizableFieldsTotal() {
    return nbNonFinalizableFieldsTotal;
  }
  
  /**
   * The process done by this processor.
   * 
   * The process of this processor will be either {@link #firstPass(CtClass)} or {@link #secondPass(CtClass)}
   * depending on whether {@link #setFirstPassDone()} was called or not.
   * <br /> To use this processor, {@link #spoon.SpoonAPI.process()} should be called twice, with a call to
   * {@link #setFirstPassDone()} in between.
   */
  @Override
  public void process(CtClass<?> element) {
    if (!firstPassDone) {
      firstPass(element);
    } else {
      secondPass(element);
    }
  }
  
  /**
   * The first pass of the process.
   * 
   * This pass will list all the static fields, final or not, in the processed class and store them.
   * @param element the class to be processed
   */
  private void firstPass(CtClass<?> element) {
    nonFinalStaticFields.put(element.getQualifiedName(), new ArrayList<>());
        
    int nbStaticFields = 0, nbStaticFinalFields = 0;
    List<CtField<?>> fields = element.getFields();
    
    for (CtField<?> field : fields) {
      if (field.isStatic()) {
        nbStaticFields++;
        if (field.isFinal()) {
          nbStaticFinalFields++;
        } else {          
          nonFinalStaticFields.get(element.getQualifiedName()).add(field);
        }
      }
    }
    
    StaticFieldsProcessor.nbStaticFields.put(element.getQualifiedName(),
        new int[] {nbStaticFields, nbStaticFinalFields, nbStaticFields - nbStaticFinalFields});
    nbStaticFieldsTotal += nbStaticFields;
    nbFinalStaticFieldsTotal += nbStaticFinalFields;
  }
  
  /**
   * The second pass of the process.
   * 
   * This pass will use the previously stored fields, and check if the value of the non final ones is changed or not
   * in the processed class.
   * <br />If their value is indeed changed, they are removed from the stored fields.
   * @param element the class to be processed
   */
  private void secondPass(CtClass<?> element) {
    Set<CtField<?>> nonFinalizable;
    for (Map.Entry<String, List<CtField<?>>> entry : nonFinalStaticFields.entrySet()) {
      nonFinalizable = new HashSet<>();
      for (CtField<?> field : entry.getValue()) {        
        CtQuery query = element.filterChildren(new FieldAccessFilter(field.getReference()));
        for (CtFieldAccess<?> fieldAccess : query.list(CtFieldAccess.class)) {
          if (fieldAccess instanceof CtFieldWrite) {
            nonFinalizable.add(fieldAccess.getVariable().getDeclaration());
          }
        }
      }
      entry.getValue().removeAll(nonFinalizable);
      int nbs[] = nbStaticFields.get(element.getQualifiedName());
      nbs[2] -= nonFinalizable.size();
      nbNonFinalizableFieldsTotal += nonFinalizable.size();
    }
  }
  
}
