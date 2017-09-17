
/**
 * this class is the main class
 * used to simulate the model, show the result
 * and output the result to a csv file
 * @author Yingxuan Liu 750307 & Jiangbin Wang 728392
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out
				.println("input: agentProbability(int) & copProbability(int) "
						+ "& vision(double) & governmentLegitimacy(double) "
						+ "& maxJailTime(int) & agentMovable(boolean)");
		int agentProbability = s.nextInt();// 70
		int copProbability = s.nextInt();// 4
		double vision = s.nextDouble();// 7
		double governmentLegitimacy = s.nextDouble();// 0.82
		int maxJailTime = s.nextInt();// 30
		boolean agentMovable = s.nextBoolean();// true
		s.close();

		Map map = new Map(40, 40);// initialization
		map.initMap(agentProbability, copProbability);// initial agent density &
														// cop density
		Person.setVision(vision);// set the vision of person
		Agent.setGovernmentLegitimacy(governmentLegitimacy);// set the
															// government
															// legitimacy of
															// agent
		Map.setMaxJailTime(maxJailTime);// set the max jail time
		Map.setMovable(agentMovable);// set whether can move or not

		String fileName = agentProbability + "_" + copProbability + "_"
				+ vision + "_" + governmentLegitimacy + "_" + maxJailTime + "_"
				+ agentMovable + ".csv";// file name shows the parameters

		try {
			int time = 0;
			FileWriter fw = new FileWriter("E:\\" + fileName);// result file
			StringBuffer str = new StringBuffer();// result

			// simulation
			while (time++ < 200) {
				System.out.println("time = " + time + "\t" + map.countNum());
				str.append(time + "," + map.toCsv() + "\r\n");
				map.action();
				map.reduceAllJailTime();
			}

			fw.write(str.toString());// write result
			fw.close();
			System.out.println("Done");// show completion
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
