package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.code.CtBinaryOperator;

public class DFCtBinaryOperatorImpl extends DFCtExpressionImpl implements DFCtBinaryOperator {

  private DFCtExpression leftHandOperand;
  private DFCtExpression rightHandOperand;
  
  
  protected DFCtBinaryOperatorImpl(CtBinaryOperator<?> ctBinaryOperator, DFCtElement parent) {
    super(ctBinaryOperator, parent);
    
    this.leftHandOperand = null;
    this.rightHandOperand = null;
  }

  
  public static DFCtBinaryOperator create(CtBinaryOperator<?> ctBinaryOperator, DFCtElement parent) {
    return new DFCtBinaryOperatorImpl(ctBinaryOperator, parent);
  }
  
  
  @Override
  public CtBinaryOperator<?> getOriginal() {
    return (CtBinaryOperator<?>) this.original;
  }

  
  @Override
  public DFCtExpression getLeftHandOperand() {
    return this.leftHandOperand;
  }

  
  @Override
  public DFCtExpression getRightHandOperand() {
    return this.rightHandOperand;
  }
  
  
  @Override
  public void checkDependencies() {
    super.checkDependencies();
    this.leftHandOperand = DFCtExpressionImpl.create(this.getOriginal().getLeftHandOperand(), this);
    this.rightHandOperand = DFCtExpressionImpl.create(this.getOriginal().getRightHandOperand(), this);
    this.leftHandOperand.checkDependencies();
    this.rightHandOperand.checkDependencies();
  }

  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    this.leftHandOperand.checkDeferredDependencies();
    this.rightHandOperand.checkDeferredDependencies();
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();
    dependencies.addAll(this.leftHandOperand.getDependencies());
    dependencies.addAll(this.rightHandOperand.getDependencies());
    
    return dependencies;
  }
  
}
