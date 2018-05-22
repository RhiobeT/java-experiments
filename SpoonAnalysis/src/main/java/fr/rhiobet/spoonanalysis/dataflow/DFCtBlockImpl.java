package fr.rhiobet.spoonanalysis.dataflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;

public class DFCtBlockImpl extends DFCtStatementImpl implements DFCtBlock {

  private List<DFCtStatement> statements;
  
  
  protected DFCtBlockImpl(CtBlock<?> ctBlock, DFCtElement parent) {
    super(ctBlock, parent);
    
    this.statements = new ArrayList<>();
  }
  
  
  public static DFCtBlock create(CtBlock<?> ctBlock, DFCtElement parent) {
    return new DFCtBlockImpl(ctBlock, parent);
  }
  
  
  @Override
  public List<DFCtStatement> getStatements() {
    return this.statements;
  }
  
  
  @Override
  public CtBlock<?> getOriginal() {
    return (CtBlock<?>) this.original;
  }
  
  
  @Override
  public void checkDependencies() {
    super.checkDependencies();
    for (CtStatement ctStatement : this.getOriginal().getStatements()) {
      DFCtStatement statement = DFCtStatementImpl.create(ctStatement, this);
      statement.checkDependencies();
      this.statements.add(statement);
    }
  }
  
  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    for (DFCtStatement statement : this.statements) {
      statement.checkDeferredDependencies();
    }
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();
    for (DFCtStatement statement : this.statements) {
      for (Dependency dependency : statement.getDependencies() ) {
        // Here we keep only the dependencies for variables declared outside this block
        if (!dependency.getVariable().getOriginal().hasParent(this.original)) {
          dependencies.add(dependency);
        }
      }
    }
    
    return dependencies;
  }
  
  
  @Override
  public void checkStatementDependencies() {
    super.checkStatementDependencies();
    
    // Computing of the statement dependencies
    for (int i = this.statements.size() - 1; i >= 0; i--) {
      DFCtStatement statement = this.statements.get(i);
      statement.checkStatementDependencies();
      for (Dependency dependency : statement.getDependencies()) {
        if (dependency.getKind() == Dependency.Kind.READ) {
          for (int j = i - 1; j >= 0; j--) {
            DFCtStatement statementDependency = this.statements.get(j);
            for (Dependency dependency2 : statementDependency.getDependencies()) {
              // We look for two statements having a dependency on the same variable,
              // the last in the block being a read and the first being a define or a write
              if (dependency.getVariable().equals(dependency2.getVariable())
                  && (dependency2.getKind() == Dependency.Kind.DEFINE
                      || dependency2.getKind() == Dependency.Kind.WRITE)) {
                statement.addStatementDependency(statementDependency);
              }
            }
          }
        } else if (dependency.getKind() == Dependency.Kind.WRITE) {
          for (int j = i - 1; j >= 0; j--) {
            DFCtStatement statementDependency = this.statements.get(j);
            for (Dependency dependency2 : statementDependency.getDependencies()) {
              // We look for two statements having a dependency on the same variable,
              // the last in the block being a write and the first being a define
              if (dependency.getVariable().equals(dependency2.getVariable())
                  && dependency2.getKind() == Dependency.Kind.DEFINE) {
                statement.addStatementDependency(statementDependency);
              }
            }
          }
        }
      }
    }
 
    // Reduction of the statement dependencies included in this block
    // Here we remove the dependencies of dependencies on which we also depend
    for (int i = this.statements.size() - 1; i >= 0; i--) {
      DFCtStatement statement = this.statements.get(i);
      List<DFCtStatement> dependencyList = new ArrayList<>(statement.getStatementDependencies());
      for (int j = 0; j < dependencyList.size(); j++) {
        for (int k = j + 1; k < dependencyList.size(); k++) {
          if (dependencyList.get(j).getStatementDependencies().contains(dependencyList.get(k))) {
            statement.removeStatementDependency(dependencyList.get(k));
            dependencyList.remove(k);
          }
        }
      }
    }
  }

}
