
/**
 * this class is the whole map
 * constructed by certain rows and columns
 * contains persons(agent or cop), and empty position(null)
 */

import java.util.Random;

import java.util.Vector;

public class Map {

	Random random = new Random();
	public static int maxJailTime = 30;
	public int row;
	public int col;
	public static boolean movable = true;// whether allow agents to move.

	// set agent can move or not
	public static void setMovable(boolean m) {
		movable = m;
	}

	// this method is used to set the max jail time for jailed persons
	public static void setMaxJailTime(int time) {
		maxJailTime = time;
	}

	// constructor
	public Map(int row, int col) {
		map = new Person[row][col];
		this.row = row;
		this.col = col;
	}

	public Person map[][];// the whole map
	public Vector<Agent> allAgent = new Vector<>();// vector to store agents
	public Vector<Cop> allCop = new Vector<>();// vector to store cops
	public Vector<Agent> Jail = new Vector<>();// vector to store jailed agents
	public Vector<Person> allPerson = new Vector<>();// vector to store all
														// persons

	// we assume that we can wrap around this map, which means the map is
	// infinite and each person has a full neighborhood in vision
	// this method is used to determine distance in such wrap-around map
	public double determineDistance(int a, int b, int c, int d) {
		double dis[] = new double[9];// to store every possible distance
		dis[0] = Math.sqrt(Math.abs(a - c) * Math.abs(a - c)
				+ Math.abs(b - d) * Math.abs(b - d));
		dis[1] = Math.sqrt(Math.abs(a + row - c) * Math.abs(a + row - c)
				+ Math.abs(b - d) * Math.abs(b - d));
		dis[2] = Math.sqrt(Math.abs(a - row - c) * Math.abs(a - row - c)
				+ Math.abs(b - d) * Math.abs(b - d));
		dis[3] = Math.sqrt(Math.abs(a - c) * Math.abs(a - c)
				+ Math.abs(b + col - d) * Math.abs(b + col - d));
		dis[4] = Math.sqrt(Math.abs(a - c) * Math.abs(a - c)
				+ Math.abs(b - col - d) * Math.abs(b - col - d));
		dis[5] = Math.sqrt(Math.abs(a + row - c) * Math.abs(a + row - c)
				+ Math.abs(b + col - d) * Math.abs(b + col - d));
		dis[6] = Math.sqrt(Math.abs(a + row - c) * Math.abs(a + row - c)
				+ Math.abs(b - col - d) * Math.abs(b - col - d));
		dis[7] = Math.sqrt(Math.abs(a - row - c) * Math.abs(a - row - c)
				+ Math.abs(b + col - d) * Math.abs(b + col - d));
		dis[8] = Math.sqrt(Math.abs(a - row - c) * Math.abs(a - row - c)
				+ Math.abs(b - col - d) * Math.abs(b - col - d));
		double minTemp = dis[0];
		for (int i = 1; i < 9; i++) {
			if (dis[i] < minTemp)
				minTemp = dis[i];
		}
		return minTemp;
	}

	// this method is used to initiate the map according to the probability of
	// agent & cop
	public void initMap(int agentProbability, int copProbability) {
		int agentNum = row * col * agentProbability / 100;
		int copNum = row * col * copProbability / 100;

		int count = 0;
		// count the number of cops
		while (copNum > 0) {
			int subRow = random.nextInt(row);
			int subCol = random.nextInt(col);
			if (map[subRow][subCol] == null) {
				map[subRow][subCol] = new Cop(count++ + "");
				map[subRow][subCol].setPosition(subRow, subCol);
				allCop.add((Cop) map[subRow][subCol]);
				allPerson.add(map[subRow][subCol]);
				copNum--;
			}
		}

		count = 0;
		// count the number of agents
		while (agentNum > 0) {
			int subRow = random.nextInt(row);
			int subCol = random.nextInt(col);
			if (map[subRow][subCol] == null) {
				map[subRow][subCol] = new Agent(count++ + "");
				map[subRow][subCol].setPosition(subRow, subCol);
				allAgent.add((Agent) map[subRow][subCol]);
				allPerson.add(map[subRow][subCol]);
				agentNum--;
			}
		}
	}

	// this method is used to determine each turtle's behavior at each turn
	public void action() {
		Vector<Person> allPersonTemp = new Vector<>(allPerson);// vector to
																// store persons
																// temporarily

		while (allPersonTemp.size() > 0) {
			int index = random.nextInt(allPersonTemp.size());// choose a person
																// randomly
			int subX = allPersonTemp.get(index).positionX;
			int subY = allPersonTemp.get(index).positionY;

			// if this person is "in jail", it is not supposed to act
			if (allPersonTemp.get(index).getIdentity() == Person.Identity.AGENT
					&& ((Agent) allPersonTemp.get(index))
							.getState() == Agent.State.JAILED) {
				allPersonTemp.remove(index);
				continue;
			}

			Vector<Person> temp = new Vector<>();// vector to store empty
													// position

			// we only scan positions that are in vision of this person
			for (int i = 0; i < 2 * Math.ceil(Person.vision) + 1; i++) {
				for (int j = 0; j < 2 * Math.ceil(Person.vision) + 1; j++) {
					int xTemp = (int) ((subX + i - Person.vision + row) % row);
					int yTemp = (int) ((subY + j - Person.vision + col) % col);
					if (map[xTemp][yTemp] == null && determineDistance(xTemp,
							yTemp, subX, subY) <= Person.vision) {
						Person p = new Person(null);// just save location
													// information
						p.setPosition(xTemp, yTemp);
						temp.add(p);
					}
				}
			}

			int tempXNew = 0, tempYNew = 0;// to store the new position

			// move if there is one empty position in vision
			if (temp.size() != 0 && (movable || allPersonTemp.get(index)
					.getIdentity() == Person.Identity.COP)) {
				int tempIndex = random.nextInt(temp.size());
				tempXNew = temp.get(tempIndex).positionX;
				tempYNew = temp.get(tempIndex).positionY;
				map[tempXNew][tempYNew] = map[subX][subY];
				map[subX][subY] = null;
				map[tempXNew][tempYNew].setPosition(tempXNew, tempYNew);
			} else {
				tempXNew = subX;
				tempYNew = subY;
			}

			// whether to set agent active
			if (map[tempXNew][tempYNew]
					.getIdentity() == Person.Identity.AGENT) {
				Agent agentTemp = ((Agent) map[tempXNew][tempYNew]);
				double ph = agentTemp.getPh();
				double ra = agentTemp.getRa();
				double k = 2.3;

				int copCount = 0;
				int activeAgentCount = 1;// active agent

				// count the number of cops in vision
				for (int i = 0; i < allCop.size(); i++) {
					int a = allCop.elementAt(i).positionX;
					int b = allCop.elementAt(i).positionY;
					if (determineDistance(tempXNew, tempYNew, a,
							b) <= Person.vision)
						copCount++;
				}

				// count the number of agents in vision
				for (int i = 0; i < allAgent.size(); i++) {
					if (allAgent.elementAt(i).getState() == Agent.State.ACTIVE
							&& allAgent.elementAt(i) != agentTemp) {
						int a = allAgent.elementAt(i).positionX;
						int b = allAgent.elementAt(i).positionY;
						if (determineDistance(tempXNew, tempYNew, a,
								b) <= Person.vision)
							activeAgentCount++;
					}
				}

				double eap = 1 - Math
						.exp(-k * Math.floor(copCount / activeAgentCount));
				double difference = ph * (1 - Agent.GOVERNMENTLEGITIMACY)
						- ra * eap;

				// determine whether change state or not
				if (agentTemp.getState() == Agent.State.QUIET
						&& difference > 0.1) {
					agentTemp.setState(Agent.State.ACTIVE);
				}

				if (agentTemp.getState() == Agent.State.ACTIVE
						&& difference <= 0.1) {
					agentTemp.setState(Agent.State.QUIET);
				}
			}

			// cops arrest one active agent in vision
			if (map[tempXNew][tempYNew].getIdentity() == Person.Identity.COP) {
				Cop copTemp = (Cop) map[tempXNew][tempYNew];
				for (int i = 0; i < allAgent.size(); i++) {
					Agent agentTemp = (Agent) allAgent.elementAt(i);
					int agentX = agentTemp.positionX;
					int agentY = agentTemp.positionY;
					double distance = determineDistance(tempXNew, tempYNew,
							agentX, agentY);

					if (agentTemp.getState() == Agent.State.ACTIVE
							&& distance <= Person.vision) {
						agentTemp.setState(Agent.State.JAILED);
						int jailTime = 1 + random.nextInt(maxJailTime);
						agentTemp.setJailTime(jailTime);
						map[agentX][agentY] = copTemp;
						map[tempXNew][tempYNew] = null;
						copTemp.setPosition(agentX, agentY);

						Jail.add(agentTemp);// add to be jailed
						break;
					}
				}
			}
			allPersonTemp.remove(index);
		}
	}

	// this method is used to reduce the jail time of jailed agent at each turn
	public void reduceAllJailTime() {
		for (int i = 0; i < Jail.size(); i++) {
			Jail.elementAt(i).reduceJailTime(1);

			if (Jail.elementAt(i).getJailTime() == 0) {
				int x = Jail.elementAt(i).positionX;// get the old position
				int y = Jail.elementAt(i).positionY;
				if (map[x][y] == null) {
					// if old position is null, release and set up
					Jail.elementAt(i).setState(Agent.State.QUIET);
					map[x][y] = Jail.elementAt(i);
					Jail.removeElementAt(i);
				} else {
					// choose a random position to prevent overlapping
					while (true) {
						int tempRow = random.nextInt(row);
						int tempCol = random.nextInt(col);
						if (map[tempRow][tempCol] == null) {
							Jail.elementAt(i).setState(Agent.State.QUIET);
							map[tempRow][tempCol] = Jail.elementAt(i);
							map[tempRow][tempCol].setPosition(tempRow,
									tempCol);
							Jail.removeElementAt(i);
							break;
						}
					}
				}
			}
		}
	}

	// this method is used to count the number of agents at different state
	public String countNum() {
		int quietNum = 0;
		int activeNum = 0;
		int jailedNum = 0;

		for (int i = 0; i < allAgent.size(); i++) {
			if (allAgent.elementAt(i).getState() == Agent.State.QUIET)
				quietNum++;
			if (allAgent.elementAt(i).getState() == Agent.State.ACTIVE)
				activeNum++;
		}
		jailedNum = Jail.size();

		return "quiet: " + quietNum + "\tactive: " + activeNum + "\tjailed: "
				+ jailedNum;
	}

	// this method is used to generate results in csv format
	public String toCsv() {
		int quietNum = 0;
		int activeNum = 0;
		int jailedNum = 0;

		for (int i = 0; i < allAgent.size(); i++) {
			if (allAgent.elementAt(i).getState() == Agent.State.QUIET)
				quietNum++;
			if (allAgent.elementAt(i).getState() == Agent.State.ACTIVE)
				activeNum++;
		}
		jailedNum = Jail.size();

		return quietNum + "," + activeNum + "," + jailedNum;
	}

}
