
/**
 * this class is person object
 * each person has a name and coordinate
 * the vision of person is set when test
 */

public class Person {

	// there are two identities: agent and cop
	public static enum Identity {
		AGENT, COP
	};

	// constructor
	Person(String name) {
		this.name = name;
	}

	public int positionX;// x coordinate
	public int positionY;// y coordinate
	public String name;
	public static double vision;

	// this method is used to set vision of person
	public static void setVision(double v) {
		vision = v;
	}

	// this method is used to set position of person
	public void setPosition(int x, int y) {
		positionX = x;
		positionY = y;
	}

	// this method is used to get identity of person
	public Identity getIdentity() {
		return null;
	}

}
