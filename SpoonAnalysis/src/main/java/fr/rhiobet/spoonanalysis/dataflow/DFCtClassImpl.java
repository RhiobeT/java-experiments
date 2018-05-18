package fr.rhiobet.spoonanalysis.dataflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;

public class DFCtClassImpl extends DFCtStatementImpl implements DFCtClass {

  private List<DFCtField> fields;
  private Set<DFCtConstructor> constructors;
  private Set<DFCtMethod> methods;
  
  protected DFCtClassImpl(CtClass<?> ctClass) {
    super(ctClass, null);
    
    this.constructors = new HashSet<>();
    this.methods = new HashSet<>();
    this.fields = new ArrayList<>();  
  }
  
  
  public static DFCtClass create(CtClass<?> ctClass) {
    return new DFCtClassImpl(ctClass);
  }
  
 
  @Override
  public CtClass<?> getOriginal() {
    return (CtClass<?>) this.original;
  }
  
  
  public Set<DFCtConstructor> getConstructors() {
    return this.constructors;
  }
  
  
  public Set<DFCtMethod> getMethods() {
    return this.methods;
  }
  
  
  public List<DFCtField> getFields() {
    return this.fields;
  }
  
  
  @Override
  public void checkDependencies() {
    super.checkDependencies();
    for (CtConstructor<?> ctConstructor : this.getOriginal().getConstructors()) {
      DFCtConstructor constructor = DFCtConstructorImpl.create(ctConstructor, this);
      constructor.checkDependencies();
      this.constructors.add(constructor);
    }
    for (CtMethod<?> ctMethod : this.getOriginal().getMethods()) {
      DFCtMethod method = DFCtMethodImpl.create(ctMethod, this);
      method.checkDependencies();
      this.methods.add(method);
    }
    for (CtField<?> ctField : this.getOriginal().getFields()) {
      DFCtField field = DFCtFieldImpl.create(ctField, this);
      field.checkDependencies();
      this.fields.add(field);
    }
  }
  
  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    for (DFCtConstructor constructor : this.constructors) {
      constructor.checkDeferredDependencies();
    }
    for (DFCtMethod method : this.methods) {
      method.checkDeferredDependencies();
    }
    for (DFCtField field : this.fields) {
      field.checkDeferredDependencies();
    }
  }
  
  
  @Override
  public void checkStatementDependencies() {
    super.checkStatementDependencies();
    for (DFCtConstructor constructor : this.constructors) {
      constructor.checkStatementDependencies();
    }
    for (DFCtMethod method : this.methods) {
      method.checkStatementDependencies();
    }
  }

}
