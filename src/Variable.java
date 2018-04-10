import java.util.ArrayList;

public class Variable {

	//private int domainLB;
	//private int domainUB;
    private int id;
	private ArrayList<Integer> domain;
	
	
	public Variable(int id, int domainLB, int domainUB) {
		this.id = id;
		
		this.domain = new ArrayList<>();
		
		//TODO dont need i
		for (int i = 0, j = domainLB; j <= domainUB; i++, j++) {
		    domain.add(j);
		}
	}
	
	@Override 
	public boolean equals(Object other) {
	    Variable otherVar = (Variable) other;
	    return this.id == otherVar.id;
	}
	
	public int getId() {
	    return this.id;
	}
	
	public void setId(int id) {
	    this.id = id;
	}

	public int getDomainLB() {
		return this.domain.get(0);
	}

//	public void setDomainLB(int domainLB) {
//		this.domainLB = domainLB;
//	}

	public int getDomainUB() {
		return this.domain.get(this.domain.size());
	}

//	public void setDomainUB(int domainUB) {
//		this.domainUB = domainUB;
//	}

	public ArrayList<Integer> getDomain() {
		return domain;
	}

    public void dropVal(int val) {
        /* Need to convert to integer because remove(int) removes by index */
        this.domain.remove(new Integer(val));
    }
	
	
}
