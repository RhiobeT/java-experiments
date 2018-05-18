package fr.rhiobet.spoonanalysis.processors;

import java.util.HashMap;
import java.util.Map;

import fr.rhiobet.spoonanalysis.dataflow.DFCtClass;
import fr.rhiobet.spoonanalysis.dataflow.DFCtClassImpl;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

public class DataflowProcessor extends AbstractProcessor<CtClass<?>> {

  private static Map<CtClass<?>, DFCtClass> processedClasses = new HashMap<>();
  private static int currentPass = 0;
  
  
  @Override
  public void process(CtClass<?> element) { 
    if (currentPass == 0) {
      DFCtClass processedClass = DFCtClassImpl.create(element);
      processedClass.checkDependencies();      
      processedClasses.put(element, processedClass);
    } else if (currentPass == 1) {
      processedClasses.get(element).checkDeferredDependencies();
    } else {
      processedClasses.get(element).checkStatementDependencies();
      System.out.println(processedClasses.get(element).toString());
    }
  }
  
  
  public static void setFirstPassDone() {
    currentPass = 1;
  }
  
    
  public static void setSecondPassDone() {
    currentPass = 2;
  }

}
