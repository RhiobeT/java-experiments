package fr.rhiobet.spoonanalysis.dataflow;

import java.util.List;
import java.util.Set;

import spoon.reflect.declaration.CtClass;

public interface DFCtClass extends DFCtStatement {

  @Override
  public CtClass<?> getOriginal();
  
  public Set<DFCtConstructor> getConstructors();
  
  public Set<DFCtMethod> getMethods();
  
  public List<DFCtField> getFields();

  
  @Override
  public default String getRepresentation() {
    String originalString = this.getOriginal().toString();
    StringBuilder string = new StringBuilder();
    
    string.append(originalString.substring(0, originalString.indexOf("{")));
    string.append("{\n");
    
    for (DFCtField field : this.getFields()) {
      String fieldString = field.toString();
      string.append(fieldString.replaceAll("(?m)^", "    ") + "\n");
    }
    for (DFCtConstructor constructor : this.getConstructors()) {
      String constructorString = constructor.toString();
      string.append(constructorString.replaceAll("(?m)^", "    ") + "\n");     
    }
    for (DFCtMethod method : this.getMethods()) {
      String methodString = method.toString();
      string.append(methodString.replaceAll("(?m)^", "    ") + "\n");
    }
    
    string.append("}");
    
    return string.toString();
  }
  
}
