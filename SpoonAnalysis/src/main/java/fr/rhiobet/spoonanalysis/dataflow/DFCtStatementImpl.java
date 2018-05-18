package fr.rhiobet.spoonanalysis.dataflow;

import java.util.HashSet;
import java.util.Set;

import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;

public class DFCtStatementImpl extends DFCtElementImpl implements DFCtStatement {

  private Set<DFCtStatement> statementDependencies;
  
  
  protected DFCtStatementImpl(CtStatement ctStatement, DFCtElement parent) {
    super(ctStatement, parent);
    
    this.statementDependencies = null;
  }

  
  public static DFCtStatement create(CtStatement ctStatement, DFCtElement parent) {
    if (ctStatement instanceof CtClass) {
      return DFCtClassImpl.create((CtClass<?>) ctStatement, parent);
    } else if (ctStatement instanceof CtBlock) {
      return DFCtBlockImpl.create((CtBlock<?>) ctStatement, parent);
    } else if (ctStatement instanceof CtInvocation) {
      return DFCtInvocationImpl.create((CtInvocation<?>) ctStatement, parent);
    } else if (ctStatement instanceof CtLocalVariable) {
      return DFCtLocalVariableImpl.create((CtLocalVariable<?>) ctStatement, parent);
    } else if (ctStatement instanceof CtAssignment) {
      return DFCtAssignmentImpl.create((CtAssignment<?, ?>) ctStatement, parent);
    } else if (ctStatement instanceof CtIf) {
      return DFCtIfImpl.create((CtIf) ctStatement, parent);
    } else {
      System.err.println("Error: " + ctStatement.getClass() + " not implemented yet");
      return new DFCtStatementImpl(ctStatement, parent);
    }
  }
  
  
  @Override
  public CtStatement getOriginal() {
    return (CtStatement) this.original;
  }
  
  
  @Override
  public void checkStatementDependencies() {
    this.statementDependencies = new HashSet<>();
  }
  
  
  @Override
  public void addStatementDependency(DFCtStatement statement) {
    this.statementDependencies.add(statement);
  }

 
  @Override
  public void removeStatementDependency(DFCtStatement statement) {
    this.statementDependencies.remove(statement);
  }
  
  
  @Override
  public Set<DFCtStatement> getStatementDependencies() {
    return this.statementDependencies;
  }
  
}
