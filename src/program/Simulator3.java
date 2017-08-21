package program;
import graphs.DisjakstraHeuristic;
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

import searchAgents.AStarSearchAgent;
import searchAgents.GreedySearchAgent;
import searchAgents.RealTimeAStarSearchAgent;
import normalAgents.Agent;
import normalAgents.GreedyAgent;
import normalAgents.GreedyAgentKeyCollector;
import normalAgents.HumanAgent;


public class Simulator3 {
	private static Graph graph;
	public static void main(String[] args) {
		initGraph();
		List<Graph> graphs = new ArrayList<Graph>();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Select Option:\n1.Part One: Simple Agents\n2.Part Two: Search Agents\n3.Bonus: Search Agent + Key Collector");
		int part = scanner.nextInt();
		Agent otherAgent = null;
		switch(part){
			case 1:
				System.out.println("Enter Number of Agents");
				int numberOfAgents = scanner.nextInt();
				for(int i=0; i < numberOfAgents; i++){
					System.out.println("Enter Agent Type\n1.Human\n2.Greedy\n3.Greedy Key Collector");
					int agentType = scanner.nextInt();
					System.out.println("Enter starting and goal vertex");
					int startingVertexNumber = scanner.nextInt();
					int goalVertexNumber = scanner.nextInt();
					Vertex startingVertex = graph.getVertixes().get(startingVertexNumber);
					Vertex goalVertex = graph.getVertixes().get(goalVertexNumber);
					switch(agentType){
					case 1:
						graph.addAgent(new HumanAgent(i),startingVertex,goalVertex);
						break;
					case 2:
						graph.addAgent(new GreedyAgent(i),startingVertex,goalVertex);
						break;
					case 3:
						graph.addAgent(new GreedyAgentKeyCollector(i),startingVertex,goalVertex);
						break;
					}
				}
				break;
			case 3:
				System.out.println("Enter starting Vertex for Key Collector:");
				int greedyStartingVertexNumber = scanner.nextInt();
				Vertex greedyStartingVertex = graph.getVertixes().get(greedyStartingVertexNumber);
				otherAgent = new GreedyAgentKeyCollector(0);
				graph.addAgent(otherAgent, greedyStartingVertex, greedyStartingVertex);
			case 2:
				System.out.println("Enter Agent Type:\n1.Greedy Search Agent\n2.A* Search Agent\n3.RealTime A* Search Agent");
				int type = scanner.nextInt();
				System.out.println("Enter starting and goal vertex");
				int startingVertexNumber = scanner.nextInt();
				int goalVertexNumber = scanner.nextInt();
				Vertex startingVertex = graph.getVertixes().get(startingVertexNumber);
				Vertex goalVertex = graph.getVertixes().get(goalVertexNumber);
				switch(type){
					case 1:
						graph.addAgent(new GreedySearchAgent(1,new DisjakstraHeuristic(),otherAgent),startingVertex,goalVertex);
						break;
					case 2:
						graph.addAgent(new AStarSearchAgent(1,new DisjakstraHeuristic(),otherAgent),startingVertex,goalVertex);
						break;
					case 3:
						graph.addAgent(new RealTimeAStarSearchAgent(1,new DisjakstraHeuristic(),2,otherAgent),startingVertex,goalVertex);
						break;
				}
				break;

		}
		while(!graph.hasAllAgentsFinished()){
			for (Agent a : graph.getUnfinishedAgents()){
				Vertex current = graph.getAgentCurrentVertex(a);
				Vertex destination = a.observeWorld(graph);
				if (destination!= null && current.getId() != destination.getId()){
					graphs.add(new Graph(graph));
					a.addScore(graph.getWeight(graph.getAgentCurrentVertex(a), destination));
					boolean moved = graph.moveAgent(a,destination.getId());
					if(moved)
						System.out.println("Agent "+a+" going to "+destination.getId());
					else
						System.out.println("Agent "+a+" was trying to get to locked vertex");
				}
				else{
					a.addScore(1);
					System.out.println("Agent "+a+" no-op");
					
				}
			}
			if(graphs.contains(graph)){
				System.out.println("REPEATED STATE");
			}
		}
		for (Agent a : graph.getAgents()){
			System.out.println(a.getName() + " score: "+a.getScore() );//+ " T="+((SearchAgent)a).getExpansionSteps());
		}
		scanner.close();
	}

	private static void initGraph() {
		int numberOfVertex = 0;
		double pbrc;
		int numberOfBlockaids;
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
						double probability = Double.parseDouble(splits[4]);
			           graph.getVertixes().get(currVertexId).addKeyProbability(keyId,probability);
						
						break; // notice, we do not add the keys yet, because they might not even exist.
						
					}
					break;
				case "#E":
					int v1num = Integer.parseInt(splits[1]);
					int v2num = Integer.parseInt(splits[2]);
					double weight = Double.parseDouble(splits[3].substring(1));
					graph.addEdge(v1num,v2num,weight);
					break;
				case "#PBRC":
					pbrc = Double.parseDouble(splits[1]);
					break;
				case "#B":
					numberOfBlockaids = Integer.parseInt(splits[1]);
					
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
