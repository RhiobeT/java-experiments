package fr.rhiobet.spoonanalysis.processors;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.rhiobet.spoonanalysis.staticfields.SFContext;
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
  
  /**
   * The process done by this processor.
   * 
   * The process of this processor will be either {@link #firstPass(CtClass)} or {@link #secondPass(CtClass)}
   * depending on whether {@link #fr.rhiobet.spoonanalysis.staticfields.SFContext.setFirstPassDone()} was called or not.
   * <br /> To use this processor, {@link #spoon.SpoonAPI.process()} should be called twice, with a call to
   * {@link #fr.rhiobet.spoonanalysis.staticfields.SFContext.setFirstPassDone()} in between.
   */
  @Override
  public void process(CtClass<?> element) {
    if (!SFContext.isFirstPassDone()) {
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
    int nbStaticFields = 0, nbStaticFinalFields = 0;
    List<CtField<?>> fields = element.getFields();
    
    for (CtField<?> field : fields) {
      if (field.isStatic()) {
        nbStaticFields++;
        if (field.isFinal()) {
          nbStaticFinalFields++;
        } else {          
          SFContext.addNonFinalStaticField(element.getQualifiedName(), field);
        }
      }
    }
    
    SFContext.setNbStaticFields(element.getQualifiedName(),
        nbStaticFields, nbStaticFinalFields, nbStaticFields - nbStaticFinalFields);
    SFContext.incNbStaticFieldsTotal(nbStaticFields);
    SFContext.incNbFinalStaticFieldsTotal(nbStaticFinalFields);
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
    for (Map.Entry<String, List<CtField<?>>> entry : SFContext.getNonFinalStaticFields().entrySet()) {
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
      int nbs[] = SFContext.getNbStaticFields(element.getQualifiedName());
      nbs[2] -= nonFinalizable.size();
      SFContext.incNbNonFinalizableFieldsTotal(nonFinalizable.size());
    }
  }
  
}
