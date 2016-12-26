package program;
import gameAgents.FullyCoopAgent;
import gameAgents.GameAgent;
import gameAgents.SemiCoopAgent;
import gameAgents.ZeroSumAgent;
import graphs.Graph;
import graphs.Key;
import graphs.Lock;
import graphs.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import normalAgents.Agent;


public class Simulator2 {
	public enum GameType{
		ZERO_SUM,FULLY_COOP,SEMI_COOP,NOT_YET
	}
	private static Graph graph;
	private static GameType type;
	public static void main(String[] args) {
		initGraph();
	
		List<Graph> graphs = new ArrayList<Graph>();
		Scanner scanner = new Scanner(System.in);
		System.out.println("choose an agent type between:type 1 for zero sum"
				+ ", type 2 for semi cooprative, and type 3 for fully cooprative. ");
		int intType = scanner.nextInt();
		
		switch (intType){
		case 1:
			type=GameType.ZERO_SUM;
			break;
		case 2:
			type=GameType.SEMI_COOP;
			break;
		case 3:
			type=GameType.FULLY_COOP;
			break;
		}
		System.out.println("Enter first Agent's starting and goal vertex:");
		int starting1 = scanner.nextInt();
		int goal1 = scanner.nextInt();
		System.out.println("Enter second Agent's starting and goal vertex:");
		int starting2 = scanner.nextInt();
		int goal2 = scanner.nextInt();
		System.out.println("please enter desireable number of moves for each agent");
		int l = scanner.nextInt();
		
		
		
		GameAgent agent1;
		GameAgent agent2;
		switch(type)
		{
			case SEMI_COOP:
				agent1 = new SemiCoopAgent(1,null);
				agent2 = new SemiCoopAgent(2,agent1);
				break;
			case ZERO_SUM:
				agent1 = new ZeroSumAgent(1,null,l);
				agent2 = new ZeroSumAgent(2,agent1,l);
				break;
			case FULLY_COOP:
			default:
				agent1 = new FullyCoopAgent(1,null);
				agent2 = new FullyCoopAgent(2,(FullyCoopAgent)agent1);
				break;
		}
		

		graph.addAgent(agent1,starting1,goal1);
		agent1.setOtherAgent(agent2);
		graph.addAgent(agent2, starting2, goal2);
		
		boolean wonGame = false;

		while(!finished(graph,agent1,agent2,type) && l>0){
			l--;
			for (Agent a : graph.getUnfinishedAgents()){
				Vertex current = graph.getAgentCurrentVertex(a);
				Vertex destination = a.observeWorld(graph);
				if (destination!= null && current.getId() != destination.getId()){
					graphs.add(new Graph(graph));

					boolean moved = graph.moveAgent(a,destination.getId());
					switch(type){
					case ZERO_SUM:
						if(a.finished(graph) && !wonGame){
							a.addScore(1);
							wonGame = true;
							if(a==agent1)
								agent2.addScore(-1);//
							else
								agent1.addScore(-1);//
						}
						break;
					case SEMI_COOP:	
					case FULLY_COOP:
						a.addScore(-1);
						break;
					default:
						break;

					}


					if(moved)
						System.out.println("Agent "+a+" going to "+destination.getId());
					else
						System.out.println("Agent "+a+" was trying to get to locked vertex");
				}
				else{
					//a.addScore(1);			
					System.out.println("Agent "+a+" no-op");
					//System.exit(0);

				}
			}
			if(graphs.contains(graph)){
				System.out.println("REPEATED STATE");
			}
		}
		for (Agent a : graph.getAgents()){
			System.out.println(a + " score: "+a.getScore() );//+ " T="+((SearchAgent)a).getExpansionSteps());
		}
		scanner.close();

	}

	private static boolean finished(Graph graph, Agent agent1, Agent agent2,GameType type){
		switch(type){
		case ZERO_SUM:
			return (agent1.finished(graph) || agent2.finished(graph));
		case SEMI_COOP:
		case FULLY_COOP:
			return (agent1.finished(graph) && agent2.finished(graph));
		default:
			return false;
		}
	}

	private static void initGraph() {
		int numberOfVertex = 0;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter File Name:");
		String filename = scanner.nextLine();

		File file = new File(filename);
		BufferedReader reader = null;
		try {
			String nextLine;
			reader = new BufferedReader(new FileReader(file));
			boolean hasVertexs = false;
			while ((nextLine = reader.readLine()) != null){
				String[] splits = nextLine.split(" ");
				switch(splits[0]){
				case "#V":
					if(!hasVertexs){
						numberOfVertex = Integer.parseInt(splits[1]);
						graph = new Graph(numberOfVertex);
						hasVertexs = true;
					}
					else {
						int currVertexId = Integer.parseInt(splits[1]);
						int keyId = Integer.parseInt(splits[3]);
						String keyName = null;
						if(splits.length > 4)
							keyName = splits[4];
						if(splits[2].equals("K"))
							graph.addKeyToVertex(currVertexId,new Key(keyId,keyName));
						else
							graph.addLockToVertex(currVertexId,new Lock(keyId,keyName));
					}
					break;
				case "#E":
					int v1num = Integer.parseInt(splits[1]);
					int v2num = Integer.parseInt(splits[2]);
					double weight =1;

					//double weight = Double.parseDouble(splits[3].substring(1));
					graph.addEdge(v1num,v2num,weight);
					break;

				}
			}	
		}
		catch( IOException e){
			e.printStackTrace();
			scanner.close();

		}

		//scanner.close();
	}
}
