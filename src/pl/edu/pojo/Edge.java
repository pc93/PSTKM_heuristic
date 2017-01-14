/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.pojo;

/**
 *
 * @author Czarnocki
 */
public class Edge {
    
    private int index;
    private int startNode;
    private int endNode;
    private int currentLoad;
    
    public Edge(int index, int startNode, int endNode, int currentLoad)
    {
        this.index = index;
        this.startNode = startNode;
        this.endNode = endNode;
        this.currentLoad = currentLoad;
    }
    
    public int getIndex()
    {
        return this.index;
    }
    
    public void setIndex(int index) {
		this.index = index;
	}

	public int getStartNode()
    {
        return this.startNode;
    }
    public int getEndNode()
    {
        return this.endNode;
    }
    public int getCurrentLoad()
    {
        return this.currentLoad;
    }
    public void setLoad(int newLoad)
    {
        this.currentLoad = newLoad;
    }

	@Override
	public String toString() {
		return "\n---- Edge [index=" + index + ", startNode=" + startNode
				+ ", endNode=" + endNode + ", currentLoad=" + currentLoad + "]";
	}
    
}
