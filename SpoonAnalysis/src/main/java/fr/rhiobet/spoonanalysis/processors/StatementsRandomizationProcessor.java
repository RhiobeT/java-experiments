package fr.rhiobet.spoonanalysis.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.rhiobet.spoonanalysis.dataflow.DFCtBlock;
import fr.rhiobet.spoonanalysis.dataflow.DFCtClass;
import fr.rhiobet.spoonanalysis.dataflow.DFCtConstructor;
import fr.rhiobet.spoonanalysis.dataflow.DFCtIf;
import fr.rhiobet.spoonanalysis.dataflow.DFCtMethod;
import fr.rhiobet.spoonanalysis.dataflow.DFCtStatement;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;

public class StatementsRandomizationProcessor extends AbstractProcessor<CtClass<?>> {

  @Override
  public void process(CtClass<?> element) {
    DFCtClass processedClass = DataflowProcessor.getProcessedClass(element);
    
    for (DFCtConstructor constructor : processedClass.getConstructors()) {
      for (CtConstructor<?> oldConstructor : element.getConstructors()) {
        if (oldConstructor.equals(constructor.getOriginal())) {
          oldConstructor.setBody(this.processBlock(constructor.getBody()));
          System.out.println(oldConstructor);
        }
      }
    }
    
    for (DFCtMethod method : processedClass.getMethods()) {
      for (CtMethod<?> oldMethod : element.getMethods()) {
        if (oldMethod.equals(method.getOriginal())) {
          oldMethod.setBody(this.processBlock(method.getBody()));
          System.out.println(oldMethod);
        }
      }
    }
  }
  
  
  private CtStatement processStatement(DFCtStatement statement) {
    if (statement == null) {
      return null;
    } else if (statement instanceof DFCtBlock) {
      return this.processBlock((DFCtBlock) statement);
    } else if (statement instanceof DFCtIf) {
      return this.processIf((DFCtIf) statement);
    } else {
      return statement.getOriginal();
    }
  }
  
  
  private CtBlock<?> processBlock(DFCtBlock block) {    
    Random random = new Random();
    List<DFCtStatement> statements = new ArrayList<>(block.getStatements());
    List<DFCtStatement> statementDependencies = new ArrayList<>();
    CtBlock<?> newBlock = block.getOriginal().clone();
    newBlock.setStatements(new ArrayList<>());
    while (statements.size() > 0) {
      DFCtStatement statement = statements.get(random.nextInt(statements.size()));
      if (statementDependencies.containsAll(statement.getStatementDependencies())) {
        statementDependencies.add(statement);
        newBlock.addStatement(this.processStatement(statement));
        statements.remove(statement);
      }
    }
    return newBlock;
  }
  
  
  private CtIf processIf(DFCtIf ifStatement) {
    CtIf newIf = ifStatement.getOriginal().clone();
    newIf.setThenStatement(this.processStatement(ifStatement.getThenStatement()));
    newIf.setElseStatement(this.processStatement(ifStatement.getElseStatement()));
    return newIf;
  }

}
