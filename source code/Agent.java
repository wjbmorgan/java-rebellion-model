
/**
 * this class is a sub class of Person
 * which is agent
 * each agent has a name, and fixed government legitimacy
 * random perceived hardship and risk aversion
 * and also jail time(0 if not jailed)
 * the identity is AGENT
 */

import java.util.Random;

public class Agent extends Person {

	Random random = new Random();
	public static double GOVERNMENTLEGITIMACY = 0; // set when test
	private double perceivedHardship = random.nextDouble();
	private double riskAversion = random.nextDouble();
	private int jailTime = 0;

	// there are three states: quiet, active, jailed
	public static enum State {
		QUIET, ACTIVE, JAILED
	};

	Agent(String name) {
		super(name);
	}

	private State state = State.QUIET;// initial state

	// this method is used to set the government legitimacy of agent
	public static void setGovernmentLegitimacy(double gl) {
		GOVERNMENTLEGITIMACY = gl;
	}

	// @override
	public Identity getIdentity() {
		return Identity.AGENT;
	}

	// this method is used to get the state of agent
	public State getState() {
		return state;
	}

	// this method is used to set the state of agent
	public void setState(State state) {
		this.state = state;
	}

	// this method is used to get the jail time of agent
	public int getJailTime() {
		return jailTime;
	}

	// this method is used to set the jail time of agent
	public void setJailTime(int jailTime) {
		this.jailTime = jailTime;
	}

	// this method is used to get the perceived hardship of agent
	public double getPh() {
		return perceivedHardship;
	}

	// this method is used to get the risk aversion of agent
	public double getRa() {
		return riskAversion;
	}

	// this method is used to reduce the jail time of jailed agent
	public void reduceJailTime(int rTime) {
		jailTime -= rTime;
	}

}
