
public class Variable {

	//private int domainLB;
	//private int domainUB;
    private int id;
	private int[] domain;
	
	
	public Variable(int id, int domainLB, int domainUB) {
		this.id = id;
		
		this.domain = new int[domainUB - domainLB + 1];
		for (int i = 0, j = domainLB; j <= domainUB; i++, j++) {
			this.domain[i] = j;
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
		return this.domain[0];
	}

//	public void setDomainLB(int domainLB) {
//		this.domainLB = domainLB;
//	}

	public int getDomainUB() {
		return this.domain[this.domain.length];
	}

//	public void setDomainUB(int domainUB) {
//		this.domainUB = domainUB;
//	}

	public int[] getDomain() {
		return domain;
	}
	
	
}
