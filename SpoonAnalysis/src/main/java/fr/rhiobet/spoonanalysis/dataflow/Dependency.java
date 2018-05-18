package fr.rhiobet.spoonanalysis.dataflow;

public class Dependency {
   
  public enum Kind {
    READ("Read"), WRITE("Write"), DEFINE("Define");
    
    Kind(String representation) {
      this.representation = representation;
    }
    
    private String representation;
    
    public String getRepresentation() {
      return this.representation;
    }
  }
  
 
  private DFCtElementImpl element;
  private Kind kind;
  private DFCtVariable variable;
 
  
  public Dependency(DFCtElementImpl element, Kind kind, DFCtVariable variable) {
    this.element = element;
    this.kind = kind;
    this.variable = variable;
  }
  
  
  public DFCtElementImpl getElement() {
    return this.element;
  }
  
  
  public Kind getKind() {
    return this.kind;
  }
  
  
  public DFCtVariable getVariable() {
    return this.variable;
  }

  
  @Override
  public int hashCode() {
    return this.kind.hashCode() ^ this.variable.hashCode();
  }
  

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Dependency) {
      Dependency dependency = (Dependency) obj;
      return this.kind.equals(dependency.kind) && this.variable.equals(dependency.variable);
    } else {
      return false;
    }
  }
  
  
  @Override
  public String toString() {
    return this.kind.getRepresentation() + " on " + this.variable.getSimpleName();
  }

}
