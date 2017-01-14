package pstkm_heuristic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.pojo.HeuristicInput;
import pl.edu.pojo.Demand;
import pl.edu.pojo.Edge;
import pl.edu.pojo.PathWithEgdes;
import edu.asu.emit.algorithm.graph.Path;
import edu.asu.emit.algorithm.graph.Graph;
import edu.asu.emit.algorithm.graph.abstraction.BaseVertex;
import edu.asu.emit.algorithm.graph.shortestpaths.YenTopKShortestPathsAlg;
import edu.asu.emit.algorithm.utils.Pair;

public class Parser {

	public static HeuristicInput parse(String graphFileName,
			String demandsFileName, int numberOfPaths) {

		Map<Demand, List<PathWithEgdes>> map = new HashMap<Demand, List<PathWithEgdes>>();
		List<Demand> demands = new ArrayList<Demand>();
		HeuristicInput heurisitcInput = new HeuristicInput();
		// get demands
		try {
			// 1. read the file and put the content in the buffer
			FileReader input = new FileReader(demandsFileName);
			BufferedReader bufRead = new BufferedReader(input);

			String line = bufRead.readLine();
			int count = 1;
			while (line != null) {
				String[] strList = line.trim().split("\\s");
				demands.add(new Demand(count, Integer.parseInt(strList[0]),
						Integer.parseInt(strList[1]), Integer
								.parseInt(strList[2])));
				line = bufRead.readLine();
				count++;
			}
			bufRead.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// load graph
		Graph graph = new Graph(graphFileName);
		System.out.println("\nVertices: " + graph.getVertexList());

		// get intial loads and fill list of edges
		Map<Pair<Integer, Integer>, Integer> edgesMap = graph.getEdges();
		List<Edge> edges = new ArrayList<Edge>();
		int count = 1;
		for (Pair<Integer, Integer> pair : edgesMap.keySet()) {
			Edge edge = new Edge(count, pair.first(), pair.second(),
					edgesMap.get(pair));
			if (!edges.isEmpty()) {
				Edge lastEdge = edges.get(edges.size() - 1);
				if ( !(lastEdge.getEndNode() == edge.getStartNode()
						&& lastEdge.getStartNode() == edge.getEndNode()) ) {
					edges.add(edge);
					count++;
				}
			} else {
				edges.add(edge);
				count++;
			}
		}

		heurisitcInput.setEdges(edges);

		// get list of paths for every demand
		for (Demand d : demands) {
			YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
			List<Path> shortest_paths_list = yenAlg.getShortestPaths(
					graph.getVertex(d.getSrcNode()),
					graph.getVertex(d.getDstNode()), numberOfPaths);
			System.out.println("\nSource node: " + d.getSrcNode()
					+ ", destination node: " + d.getDstNode());
			System.out.println("Shortest paths:" + shortest_paths_list);

			List<PathWithEgdes> paths = new ArrayList<PathWithEgdes>();
			int index = 1;
			for (Path p : shortest_paths_list) {
				List<BaseVertex> vertices = p.getVertexList();
				List<Edge> edgesOfPath = new ArrayList<Edge>();
				for (int i = 0; i < vertices.size() - 1; i++) {
					int src = vertices.get(i).getId();
					int dst = vertices.get(i + 1).getId();
					Edge edge = heurisitcInput.getEdgeByNodePair(src, dst);
					edgesOfPath.add(edge);
				}
				PathWithEgdes path = new PathWithEgdes(
						index, edgesOfPath);
				paths.add(path);
				index++;
			}

			map.put(d, paths);
		}

		heurisitcInput.setDemandPathsMap(map);

		return heurisitcInput;
	}

	public static void dumpToFile(
			Map<Demand, List<PathWithEgdes>> demandPathsMap, String fileName) {

		try {
			PrintWriter writer = new PrintWriter("output/" + fileName, "UTF-8");
			
			for (Demand d : demandPathsMap.keySet()) {

				writer.println("Demand: " + d.getId());

				// demand
				writer.println(d.getSrcNode() + " " + d.getDstNode() + " "
						+ d.getValue());

				// number of paths
				List<PathWithEgdes> paths = demandPathsMap.get(d);
				writer.println(paths.size());

				for (PathWithEgdes p : paths) {
					// number of edges in path
					writer.print(p.getIndex() + " ");
					// edges ids
					for (Edge e : p.getEdges()) {
						writer.print(e.getIndex() + " ");
					}
					writer.println();
				}
				writer.println();

			}

			writer.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.toString());
		}

	}

}
