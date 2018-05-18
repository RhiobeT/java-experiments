package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtVariable;

public class DFCtVariableImpl extends DFCtElementImpl implements DFCtVariable {

  private DFCtExpression defaultExpression;
  
  
  protected DFCtVariableImpl(CtVariable<?> ctVariable, DFCtElement parent) {
    super(ctVariable, parent);
    this.defaultExpression = null;
  }

  
  public static DFCtVariable create(CtVariable<?> ctVariable, DFCtElement parent) {
    if (ctVariable instanceof CtParameter) {
      return DFCtParameterImpl.create((CtParameter<?>) ctVariable, parent);
    } else if (ctVariable instanceof CtLocalVariable) {
      return DFCtLocalVariableImpl.create((CtLocalVariable<?>) ctVariable, parent);
    } else if (ctVariable instanceof CtField) {
      return DFCtFieldImpl.create((CtField<?>) ctVariable, parent);
    } else {
      System.err.println("Error: " + ctVariable.getClass() + " not implemented yet");
      return new DFCtVariableImpl(ctVariable, parent);
    }
  }
   
  
  @Override
  public DFCtExpression getDefaultExpression() {
    return this.defaultExpression;
  }
  
  
  @Override
  public String getSimpleName() {
    return this.getOriginal().getSimpleName();
  }
  
    
  @Override
  public CtVariable<?> getOriginal() {
    return (CtVariable<?>) this.original;
  }
  
  
  @Override
  public void checkDependencies() {
    super.checkDependencies();
    if (this.getOriginal().getDefaultExpression() != null) {
      DFCtExpression expression =
          DFCtExpressionImpl.create(this.getOriginal().getDefaultExpression(), this);
      expression.checkDependencies();
      this.defaultExpression = expression;
    }    
    DFCtVariableDeclarations.addDeclaration(this);
  }
  
  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    if (this.defaultExpression != null) {
      this.defaultExpression.checkDeferredDependencies();
    }
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();    
    dependencies.add(new Dependency(this, Dependency.Kind.DEFINE, this));
    if (this.defaultExpression != null) {
      dependencies.addAll(this.defaultExpression.getDependencies());
      dependencies.add(new Dependency(this, Dependency.Kind.WRITE, this));
    }

    return dependencies;
  }
  
}
