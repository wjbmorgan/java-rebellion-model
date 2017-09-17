
/**
 * this class is a sub class of Person
 * which is cop
 * each cop has a name
 * the identity is COP
 */

public class Cop extends Person {

	Cop(String name) {
		super(name);
	}

	// @override
	public Identity getIdentity() {
		return Identity.COP;
	}

}
