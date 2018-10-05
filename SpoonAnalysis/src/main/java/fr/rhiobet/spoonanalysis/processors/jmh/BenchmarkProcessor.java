package fr.rhiobet.spoonanalysis.processors.jmh;

import spoon.processing.AbstractAnnotationProcessor;
import org.openjdk.jmh.annotations.Benchmark;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.reflect.visitor.filter.AnnotationFilter;
import spoon.reflect.visitor.filter.InvocationFilter;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkProcessor extends AbstractAnnotationProcessor<Benchmark, CtMethod> {

    private static CtModel model = null;


    @Override
    public void process(Benchmark benchmark, CtMethod method) {
        System.out.println("Benchmark: " + method.getSimpleName());
        CtBlock<?> block = method.getBody();
        this.dceCheck(block);
        this.cfCheck(block);
    }


    private void dceCheck(CtBlock<?> block) {
        List<CtElement> elements = block.filterChildren(new AbstractFilter<CtElement>() {
            @Override
            public Class<CtElement> getType() {
                return CtElement.class;
            }

            @Override
            public boolean matches(CtElement element) {
                return (element instanceof CtLocalVariable) || (element instanceof CtVariableRead)
                        || (element instanceof CtFieldWrite) || (element instanceof CtInvocation);
            }
        }).list();

        List<CtLocalVariable> localVariables = new ArrayList<>();
        List<CtVariable> assignments = new ArrayList<>();
        List<CtExecutable> executables = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof CtLocalVariable) {
                // If the element is a variable declaration, it needs to be used later
                CtLocalVariable localVariable = (CtLocalVariable) elements.get(i);
                localVariables.add(localVariable);
                for (int j = i + 1; j < elements.size(); j++) {
                    if (elements.get(j) instanceof CtVariableRead) {
                        CtVariableRead variableRead = (CtVariableRead) elements.get(j);
                        if (localVariable.getReference().equals(variableRead.getVariable())) {
                            localVariables.remove(localVariable);
                            break;
                        }
                    }
                }
            } else if (elements.get(i) instanceof CtVariableWrite) {
                // Else if the element is an assignment, the receiver must be public or used later
                CtVariableWrite variableWrite = (CtVariableWrite) elements.get(i);
                if (!variableWrite.getVariable().getModifiers().contains(ModifierKind.PUBLIC)) {
                    assignments.add(variableWrite.getVariable().getDeclaration());
                    for (int j = i + 1; j < elements.size(); j++) {
                        if (elements.get(j) instanceof CtVariableRead) {
                            CtVariableRead variableRead = (CtVariableRead) elements.get(j);
                            if (variableWrite.getVariable().equals(variableRead.getVariable())) {
                                assignments.remove(variableWrite.getVariable().getDeclaration());
                                break;
                            }
                        }
                    }
                }
            } else if (elements.get(i) instanceof CtInvocation) {
                // Else if the element is a non void method call, the result must be used
                CtInvocation invocation = (CtInvocation) elements.get(i);
                if (!invocation.getType().equals(new TypeFactory().VOID_PRIMITIVE)) {
                    if (invocation.getRoleInParent().equals(CtRole.STATEMENT)) {
                        executables.add(invocation.getExecutable().getExecutableDeclaration());
                    }
                }
            }
        }

        if (localVariables.size() > 0) {
            System.out.println("  Some local variables are never used:");
            for (CtLocalVariable localVariable : localVariables) {
                System.out.println("    " + localVariable.getSimpleName());
            }
        }
        if (assignments.size() > 0) {
            System.out.println("  Some assignments are useless:");
            for (CtVariable assignment : assignments) {
                System.out.println("    " + assignment.getSimpleName());
            }
        }
        if (executables.size() > 0) {
            System.out.println("  Some method results are ignored:");
            for (CtExecutable executable : executables) {
                System.out.println("    " + executable.getSimpleName());
            }
        }
    }


    private void cfCheck(CtBlock block) {
        if (model == null) {
            return;
        }

        List<CtInvocation<?>> invocations = block.filterChildren(new AbstractFilter<CtInvocation>() {
            @Override
            public Class<CtInvocation> getType() {
                return CtInvocation.class;
            }

            @Override
            public boolean matches(CtInvocation invocation) {
                return true;
            }
        }).list();

        for (int i = 0; i < invocations.size(); i++) {
            // Here we get all the arguments for each invocation
            List<CtExpression<?>> arguments = invocations.get(i).getArguments();

            // Then we look for the other calls to the executable of this invocation
            List<CtInvocation> calls =
                    model.filterChildren(new InvocationFilter(invocations.get(i).getExecutable())).list();
            for (int j = 0; j < calls.size(); j++) {
                // We remove the calls made in other benchmarks, we only want real life situations
                if (calls.get(j).getParent(new AnnotationFilter<>(Benchmark.class)) != null) {
                    calls.remove(j);
                    j--;
                }
            }
            for (CtInvocation<?> call : calls) {
                // For each other call, we also get the arguments
                List<CtExpression<?>> callArguments = call.getArguments();

                // We then compare the arguments type between the first invocation and the other calls
                for (int j = 0; j < arguments.size(); j++) {
                    // If the argument is null, it was already checked
                    if (arguments.get(j) == null) {
                        continue;
                    }
                    if (arguments.get(j) instanceof CtVariableRead && callArguments.get(j) instanceof CtVariableRead) {
                        // If the argument is a variable read, we check the modifiers
                        if (((CtVariableRead<?>) arguments.get(j)).getVariable().getModifiers().equals(
                                ((CtVariableRead<?>) callArguments.get(j)).getVariable().getModifiers())) {
                            // If both sets are the same, perfect, we won't check this argument again
                            arguments.set(j, null);
                        }
                    } else if (!(arguments.get(j) instanceof CtVariableRead)
                            && arguments.get(j).getClass().equals(callArguments.get(j).getClass())) {
                        // Else, we check the class (this should definitely be improved)
                        arguments.set(j, null);
                    }
                }
            }

            // We check all the arguments are now null
            boolean done = true;
            for (CtExpression argument : invocations.get(i).getArguments()) {
                if (argument != null) {
                    done = false;
                    break;
                }
            }
            if (done) {
                invocations.remove(i);
                i--;
            }
        }

        if (invocations.size() > 0) {
            System.out.println("  Constant folding could happen during the following calls:");
            for (CtInvocation invocation : invocations) {
                System.out.println("    " + invocation);
            }
        }
    }


    public static void setModel(CtModel newModel) {
        model = newModel;
    }
}
