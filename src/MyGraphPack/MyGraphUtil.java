package MyGraphPack;

import com.jgraph.algebra.JGraphFibonacciHeap.Node;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.*;
import com.mxgraph.swing.*;
import com.mxgraph.view.mxGraph;

import java.awt.*;

import javax.swing.*;

import org.jgrapht.*;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;




public class MyGraphUtil {

	public MyGraphUtil() {
		// TODO Auto-generated constructor stub
	}
	public ArrayList<DeadNodes> arrayDead = new ArrayList<DeadNodes>();
	
	
	private boolean circle = true;	
	private double X0= 350.0;
	private double Y0= 100.0;
	private double Radius = 150.0;
	
	private int NodeDistance = 50;
	private int LevelDistance =70;
	
	int nc = 0;
	int nf = 0;
	int nv = 0;
	
	void updateMMLvalues(int c, int f, int v ){
		nc = c;
		nf = f;
		nv = v;
		
	}
	
	/*
	 * Each problem MUST update the classList string with the classes used.
	 */

	ArrayList<String> classList = new ArrayList<String>();
	
	void updateClasses(String problemClases){
		if (!problemClases.equals("")){
			String[] clases = problemClases.split(";");
			for (String c : clases){
				classList.add(c);
				
			}
		}
	}
	
	String classPred = "";
	
	void updateClassPred (String c){
		classPred = c;
	}
	
	 /**
  	 * Create a new Listeneable Directed Acyclic Graph Node
  	 * 
  	 */
	void createNewNodeLG(ListenableDirectedAcyclic<String,DefaultWeightedEdge>g, String id, String edges ){
	    	
		g.addVertex(id);
		if (!edges.equals("")){
			String[] numbers = edges.split(";"); 
			for (String e : numbers)  
			{  
				//System.out.println("Conection: "+id+ ", "+ e);
				g.addEdge(id, e);
			}
			
		}
	}

	
	
	/**
	 * Generate new Directed Acyclic Graph Node
	 * 
	 */
	void createNewNodeDAG(DirectedAcyclicGraph<Nodes, DefaultWeightedEdge> f, String id, String edges, int numC, int numCnew, 
			int numV, int numVnew , int numF, int numFnew, boolean isRec, boolean isGround, String clase, String rule) throws IllegalArgumentException{
		
		//New vertex: This node is added to the DAG	
		Nodes NewNode = new Nodes();
		NewNode.id= id;
		f.addVertex(NewNode);
		 
		 
		//Initialize Maps for each class in the problem
		
		Iterator<String> iteratorClasses = classList.iterator();
		
		while(iteratorClasses.hasNext()){
			
			String c = iteratorClasses.next();
				
			NewNode.arrayClass.put(c, 0.0);
		    NewNode.arraySupport.put(c, 0.0);
		    NewNode.arrayMaxSup.put(c, 0.0);
		    NewNode.arrayOpt.put(c, 0.0);	   
		    NewNode.arrayResidual.put(c, 0.0);
		    NewNode.arrayMaxRes.put(c, 0.0);
		    
		    NewNode.arrayOblivion.put(c, 0.0);
		      
		}
			
		//edges = "v1;v2;...;vn"
		
		if (!edges.equals("")){
			String[] covVertex = edges.split(";"); 
			for (String e : covVertex)  
			{                        
				//Find the vertex covered
				Nodes Temp = getVertex(f,e);
				//Number of rules covered
				NewNode.rulesCov=covVertex.length;	
				if (f.containsVertex(Temp)){
					try {            	 
						try {  
							
							f.addDagEdge(NewNode, Temp);
							
						}catch (IllegalArgumentException  e1) {
							e1.printStackTrace();	
						}
					} catch (CycleFoundException e1) {
						e1.printStackTrace();
					}
				}
    
			}
		}
	   //RULE
		NewNode.Rule=rule;
      //IS EVIDENCE      		
		NewNode.isGround=isGround;      		
      //NUM C
      	NewNode.numC=numC;
      	NewNode.numCnew=numCnew;      	
      //NUM V
      	NewNode.numV=numV;
      	NewNode.numVnew=numVnew;
      //NUM F
      	NewNode.numF=numF;
      	NewNode.numFnew=numFnew;
      //IS REC
      	NewNode.isRec=isRec; 
      //SIZE
      	NewNode.size=sizeNode(f,NewNode);
      //CERTAINTLY
      	
      	if (isGround){//is evidence
      		
      		NewNode.clase=clase;//La que me pasan por dialogo         		
      		NewNode.arrayClass.put(clase, 1.0);
      		NewNode.arraySupport.put(clase, NewNode.size);
      		NewNode.arrayMaxSup.put(clase, NewNode.size);
      		
      		
      		     		
      	}else {
      			
      		Iterator<String> it = classList.iterator();
      		while(it.hasNext()){
      			String c = it.next();
      			NewNode.arraySupport.put(c, this.supportNode(f, NewNode, c));
      			NewNode.arrayMaxSup.put(c, this.supportNodeMAX(f, NewNode, c));
      		}

      	}
      	
      	//OPTIMALITY
    	NewNode.opt = optNode(f,NewNode);  	
    	
      	//OBLIVION
      	NewNode.oblivion = oblivionCriteria(f,NewNode);
       	
       		
      	
      	//IS THE NODE PERMANENT
      	NewNode.permanent = this.isPerm(f, NewNode);
      	
      	
      	updateDAGdescriptors(f);
      	
      	//this.printGraph(f);
      	
     
	    }
	
	
	
	/**
	 * Oblivion criteria
	 **/ 
	double oblivionCriteria(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f, Nodes target){
		
		//double oblivionNeg = 0.0;
		//double oblivionPos = 0.0;
		double oblivion = -1000000000000.0;
		
		//Looking for the best optimality per classes of the ancestors of "target"
		
		Set<Nodes> setNodes = f.vertexSet();//  f.edgesOf(source);
		Map<String, Double> arrayMAX = new HashMap<String, Double>();
		
		Iterator<String> it = classList.iterator();
		while (it.hasNext()){
			String c = it.next();
			arrayMAX.put(c, 0.0);
		}

		
		
		for (Nodes source : setNodes){
			
			if (!source.id.equals(target.id)){
				java.util.List<DefaultWeightedEdge> setEdgesPath = BellmanFordShortestPath.findPathBetween(f,  source,  target);
				//temp covers source 
				if (setEdgesPath != null ){
					
					Iterator<String> it2 = classList.iterator();
					while (it2.hasNext()){
						String c = it2.next();
							if (arrayMAX.get(c) < source.arrayOpt.get(c)){
							arrayMAX.put(c, source.arrayOpt.get(c));
						}
					}

				}
			}
		}				
		//ya tengo la mejor optimalidad por clase de los ancestros de "target"		
		
		Iterator<String> it3 = classList.iterator();
		while (it3.hasNext()){
			String c = it3.next();
			if (arrayMAX.get(c) > 0){
				target.arrayOblivion.put(c, target.arrayOpt.get(c) - arrayMAX.get(c));
			}else{
				target.arrayOblivion.put(c,target.arrayOpt.get(c));
			}
			
			if (oblivion < target.arrayOblivion.get(c)){
				oblivion = target.arrayOblivion.get(c);
			}
	
		}
				
		oblivion = (double)(int)(oblivion*1000.0)/1000.0;
		return oblivion;
	}
	
	
		
	/**
	 * Size of a node/vertex only taking into acount its properties as a vertex:
	 * Size of a vertex = 1 - number of edges incident to the vertex "source" / maximum degree of a graph "g"
	 */    
    double sizeNode(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> g, Nodes source){
    	
    	
    	    	
    	
    	//this.updateMMLvalues(21, 2,  0);
    	
    	double size = source.numC*(Math.log(nc+source.numCnew+1)/(Math.log(2))) + 
    			source.numF*(Math.log(nf+source.numFnew+1)/(Math.log(2))) + 
    			0.5*source.numV*(Math.log(nv+source.numVnew+1)/(Math.log(2)));
    	
    	//double size = source.numC*(Math.log(nc+1)/(Math.log(2))) + 
    	//    		source.numF*(Math.log(nf+1)/(Math.log(2))) + 
    	 //   			0.5*source.numV*(Math.log(nv+1)/(Math.log(2)));
    	
    	
    	size = (double)(int)(size*1000.0)/1000.0;
    	return size;
    }
    	
    
   
       
     /**
  	 * support of a Node/rule for all classes
  	 * 
  	 */ 
    double supportNode(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> g, Nodes source, String clase){
      	
      	// how many nodes 

      	Set<DefaultWeightedEdge> setEdges = g.edgesOf(source);
          Nodes sourceVertex;
          Nodes targetVertex;
          
          double dIn;
      	//double dOut= (double)g.outDegreeOf(source);
          
          double sup=0.0;
         
          
          //Residual
          sup+= source.arrayResidual.get(clase);
                    
          for (DefaultWeightedEdge e : setEdges) {
          	sourceVertex = g.getEdgeSource(e);
          	if (sourceVertex.id.equals(source.id)){//only the edges that has as a source my node (output edges)
          		
          		targetVertex = g.getEdgeTarget(e);
          		// How many input vertices has the target vertex in order to divide its CERTAINLY 
           		dIn=(double)g.inDegreeOf(targetVertex);
          		         		         		
          		sup += targetVertex.arraySupport.get(clase)/dIn;
       		        		     		
          	} 	
                 	                
          }
          return (double)(int)(sup*1000.0)/1000.0;
      }
      
      
      
    double supportNodeMAX(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> g, Nodes source, String clase){
      	
      	// how many nodes 

      	Set<DefaultWeightedEdge> setEdges = g.edgesOf(source);
          Nodes sourceVertex;
          Nodes targetVertex;
          
          double dIn;
      	//double dOut= (double)g.outDegreeOf(source);
          
          double sup=0.0;
          double numnodes = g.vertexSet().size();
          
          //Residual
          sup+= source.arrayMaxRes.get(clase);
          
          for (DefaultWeightedEdge e : setEdges) {
          	sourceVertex = g.getEdgeSource(e);
          	if (sourceVertex.id.equals(source.id)){//only the edges that has as a source my node (output edges)
          		
          		targetVertex = g.getEdgeTarget(e);
          		// How many input vertices has the target vertex in order to divide its CERTAINLY 
          		
          		//dIn=(double)g.inDegreeOf(targetVertex);
          		
          		//sup += targetVertex.supportMAXPos;
          		sup += targetVertex.arrayMaxSup.get(clase);
          		        		     		
          	} 	
                  	                
          }
          return (double)(int)(sup*1000.0)/1000.0;
      }
      
      
    
    /**
   	 * Optima   	 * lity of a Node/rule
   	 * Optimality= certainly + size + usability
   	 */    
    double optNode(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> g, Nodes source){

    	double b1 = 0.1;
    	double b2 = 0.9;
    	//double b1 = 0.5;
    	//double b2 = 0.5;
    	double opt=-1000000000.0;
    	double optMax=-2147483648.0;
    	
    	Iterator<String> it = classList.iterator();
    	while (it.hasNext()){
    		
    		String c1 = it.next();
    		opt = (double) b1*(source.arraySupport.get(c1)-source.size);
    		double exceptions = 0.0;
    		
    		Iterator<String> it2 = classList.iterator();
    		while (it2.hasNext()){
        		String c2 = it2.next();
        		if (!c1.equals(c2)){
        			exceptions+=source.arraySupport.get(c2);
        		}
    		}
    		opt -= b2*(double)exceptions;
    		
    		source.arrayOpt.put(c1, (double)(int)(opt*1000.0)/1000.0);
    		
    		if (opt > optMax)
    			optMax = opt;
    	}
    	
    	
    	return (double)(int)(optMax*1000.0)/1000.0;
    	
    	
 
     }
    
          
    int totalCons(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
    	
    	Set<Nodes> vertices = f.vertexSet();
    	int tot=0;
  	
    	for (Nodes vertexI : vertices){
    		if (vertexI.permanent){tot++;}
  			}
  	
  	
    	return tot;
  	}
    
    double StdOpt(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
    	double[] vector = totalAVGOpt(f);
    	Statistics St = new Statistics(vector);
    	return St.getStdDev();
    }
  
    double avgOpt(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
	 
  		
    	double[] vector = totalAVGOpt(f);
    	Statistics St = new Statistics(vector);
    	return St.getMean();
    }
	  
    double[] totalAVGOptCons(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
	   
	   
	   double opt = 0.0;
	   double cons = 0.0;
	   int totC = 0;
	   
	   Set<Nodes> vertices = f.vertexSet();
	   
   	
	   for (Nodes vertexI : vertices){
		   //opt+=Math.abs(vertexI.opt);
		   //if (vertexI.opt> -999999){
			   opt+=vertexI.opt;
			   if (vertexI.permanent){
				   cons+=vertexI.opt;
				   totC++;
			   }
		   //}
     			
   		}
	   opt = (double)opt/f.vertexSet().size();
	   cons = (double)cons/totC;

	   opt= (double)(int)(opt*1000.0)/1000.0;
	   cons = (double)(int)(cons*1000.0)/1000.0;
	   
	   double array[] = {opt,cons};
	   
	   return  array;
  }
   
    double[] totalAVGOpt(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
    	
    	double[] avgVector = new double[f.vertexSet().size()];
    	Set<Nodes> vertices = f.vertexSet();
    	double tot=0.0;
    	
    	int i=0;
    	for (Nodes vertexI : vertices){
    			//if (vertexI.opt>-999999){
      			//tot+=vertexI.opt;
    			avgVector[i]=vertexI.opt;
    			i++;
    			//}
    	}
    	    	
    	return avgVector;
    	}
    	
   
    
    /**
  	 * Update Descriptors of all the nodes in the graph when:
  	 * - a new node is created
  	 * - a node is deleted
  	 * - an edge is created
  	 * - an edge is deleted
  	 * 
  	 */
    void updateDAGdescriptors(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> g){
    	
    	    	
    	Iterator<Nodes> iter = new DepthFirstIterator<Nodes, DefaultWeightedEdge>(g);
    	
    	Nodes vertex;
    	
    	while (iter.hasNext()) {
            vertex = iter.next();
           //if(vertex.id.equals("b5")){
           // 	System.out.print("b5: ");
           // }
            
            this.updateDAGvertex(g, vertex);
    	}
    	
    }
    
 
    /**
  	 * Update Descriptors of 1 node in the graph when:
  	 * - a new node is created
  	 * - a node is deleted
  	 * - an edge is created
  	 * - an edge is deleted
  	 * 
  	 */
    void updateDAGvertex(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG, Nodes vertex){
    	
          	
    	vertex.size=sizeNode(DAG,vertex);  
    	
    	if (!vertex.isGround){//is NOT evidence
    		   		 
    		Iterator<String> it = classList.iterator();
    		while (it.hasNext()){
    			String c = it.next();
    			vertex.arraySupport.put(c, this.supportNode(DAG, vertex, c));
    			vertex.arrayMaxSup.put(c, this.supportNodeMAX(DAG, vertex, c));
    		}   		
    		    		
    		if (DAG.outDegreeOf(vertex)==0){
    			vertex.isLeaf=true;
    		}
    	}
    	
    	
       	//OPTIMALITY
    	vertex.opt = optNode(DAG,vertex);
    	
    	//OBLIVION
    	vertex.oblivion = oblivionCriteria(DAG,vertex);     	
  		
      	
      	//IS THE NODE PERMANENT
    	vertex.permanent = this.isPerm(DAG, vertex);
     
    }
    
	
   
			
	/**
  	 * Generate a random DAG giving as inputs the number of positive an negative examples, number of rules and if it is strict
  	 * Strict: Path between vertex? V1->V2 & V1->V3 % V2->V3 => NO edge (necessary) between V1 and V3
  	 * 
  	 */
	
	
	
	/**
  	 * Draw the graph (tree or circle)
  	 * 
  	 */
    void layoutGraph2draw(mxGraph g, boolean circle){
    	
    	if (circle){
    		
    		mxCircleLayout layout = new mxCircleLayout(g);
            layout.setResetEdges(true);
    		layout.setX0(this.X0);
            layout.setY0(this.Y0);
            layout.setRadius(this.Radius);
          
            layout.execute(g.getDefaultParent());  
            this.circle=true;
            
    	}else{
    		 mxCompactTreeLayout layout = new mxCompactTreeLayout(g);
    		 layout.setHorizontal(false);
    		 layout.setResetEdges(true);
    		 layout.setMoveTree(true);
    	     layout.setNodeDistance(this.NodeDistance);
    	     layout.setLevelDistance(this.LevelDistance);
    	     layout.setGroupPadding(10);
    	     layout.execute(g.getDefaultParent()); 
    	     this.circle=false;
    	}
    }
    
    /**
  	 * Zoom the graph (tree or circle)
  	 * 
  	 */
    void zoom(mxGraph g, boolean in){
    	
    	
    	if (in == true){
    		if (this.circle){
    			this.X0 -= 0;
    			this.Y0 += 0;
    			this.Radius += 5;
    			layoutGraph2draw(g, true);
    		}
    		else{
    			this.NodeDistance +=5;
    			this.LevelDistance += 5;
    			layoutGraph2draw(g, false);    			
    		}
    	}
    	else{
    		if (this.circle){
    			this.X0 -= 0;
    			this.Y0 -= 0;
    			this.Radius -= 5;
    			
    			layoutGraph2draw(g, true);
    		}
    		else{
    			this.NodeDistance -=5;
    			this.LevelDistance -= 5;
    			layoutGraph2draw(g, false);    			
    		}
    		
    	}
    }
    
    
    /**
  	 * Print the DAG
  	 * 
  	 */
    void printGraph(DirectedAcyclicGraph<Nodes, DefaultWeightedEdge> g){
    	  
    	System.out.println("Vertex\tPos\tNeg\tRules\tGround\tCert(+|-)\tSup(+|-)\tSize\tOblivion\tStrength\tOpt\tEdges");
    	System.out.println("-------------------------------------------------------------------------------------");
    	
    	    	Iterator<Nodes> iter =
    	            new DepthFirstIterator<Nodes, DefaultWeightedEdge>(g);
    	  
    	        Nodes vertex;
    	        while (iter.hasNext()) {
    	            vertex = iter.next();
    	            
    	            //System.out.print("My Vertex " + vertex.id + " is connected to: ");
    	            System.out.print(vertex.id+"\t"+vertex.posCov+"("+vertex.PosPath+")\t"+vertex.negCov+"("+vertex.NegPath+")\t"+
    	            vertex.rulesCov+"("+vertex.rulesCovPath+")\t"+vertex.isGround+"\t"+vertex.cert+"("+vertex.certPos+"|"
    	            		+vertex.certNeg+")"+"\t("+vertex.supportPos+"|"+vertex.supportNeg+")\t"+vertex.size+"\t"+vertex.oblivion+"\t"+vertex.strength+"\t"+vertex.opt+"\t");
    	            
    	            //Conjunto de edges asociados a cada vertex
    	            Set<DefaultWeightedEdge> setEdges = g.edgesOf(vertex);
    	            Nodes sourceVertex;
    	            Nodes targetVertex;
    	            for (DefaultWeightedEdge e : setEdges) {
    	            	sourceVertex = g.getEdgeSource(e);
    	                targetVertex = g.getEdgeTarget(e);
    	                System.out.print("("+sourceVertex.id + " -> "+ targetVertex.id+")");
    	                    	                
    	            }
    	            System.out.println("");
    	            
    	         }
    	        System.out.println("\n\n\n");
    }
    	            
    
    /**
  	 * Remove the DAG
  	 * 
  	 */
    void eraseDAG(DirectedAcyclicGraph<Nodes, DefaultWeightedEdge> g){

    	      	  
	 Set<Object> vertices = new HashSet<Object>();
     vertices.addAll(g.vertexSet());
     
     
     for (Object vertex : vertices) {
		g.removeVertex((Nodes)vertex);
     }	
}

    
    /**
   	 * Remove the LDAG
   	 * 
   	 */
    void eraseLG(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g){
	  

	
	
	 Set<Object> vertices = new HashSet<Object>();
     vertices.addAll(g.vertexSet());
     
     
     for (Object vertex : vertices) {
		g.removeVertex((String)vertex);
     }	
}	
	
    
    
    
    /**
   	 * Consolidate knowledge
   	 * 
   	 */   
    boolean isPerm(DirectedAcyclicGraph<Nodes, DefaultWeightedEdge> f, Nodes source){
    	
    	
    	//purity
    	boolean isPure =  false;
    	int eq2zero= 0;
    	Iterator<String> it = classList.iterator();
    	while (it.hasNext()){
    		String c = it.next();
    		if (!c.equals(this.classPred) &&  source.arraySupport.get(c) == 0){
    			eq2zero+=1;    			
    		}   		
    	}
    	//Solo cubre una clase, opt >0 y opt > media(opt)
    	if ( ((classList.size() - eq2zero) == 1) && (source.opt > 0.0) && (source.opt > this.avgOpt(f)) ){
    		return true;
    	}else{
    		return false;
    	}
    	
    }
    
    
    
    boolean oblivionIter (ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes, DefaultWeightedEdge> f, int i){
    	
    	double min = 1000000000.0;
    	String minId = "";
    	
    	Iterator<Nodes> iter = new DepthFirstIterator<Nodes, DefaultWeightedEdge>(f);
    	
    	Nodes vertex;
    	
    	if (i == 0){
    		return true;
    	}
    	while (iter.hasNext()) {
            
    		vertex = iter.next();
    		
    		if ((vertex.oblivion < min) && (!vertex.permanent)){
    		//if ((vertex.oblivion < min) && !vertex.permanent && (vertex.oblivion)<0.80){
    			
    			min = vertex.oblivion;
    			minId  = vertex.id;
    		}
            
    	}
    	
    	//System.out.println("Node to delete: "+minId);
    	if (!minId.equals("")){
    		this.deleteVertex(g, f, minId);
    	}
    	
    	this.updateDAGdescriptors(f);
    	    	   	
    	return oblivionIter(g,f,i-1);
    	
    }
    
       
    
    /**
   	 * Get the vertex with id is equal to id (DAG)
   	 * 
   	 */	
    Nodes getVertex (DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG, String id){
    	
    	Iterator<Nodes> iter = new DepthFirstIterator<Nodes, DefaultWeightedEdge>(DAG);
    	
    	Nodes vertex;
    	String splitID = id.split(":")[0];
    	while (iter.hasNext()) {
            vertex = iter.next();
            
            
            if (vertex.id.equals(splitID)){
            	return vertex; 
            }
    	}
    	return null;
    }
    
    
    /**
   	 * Get the vertex with id is equal to id (LDAG)
   	 * 
   	 */	
    String getVertex(ListenableDirectedAcyclic<String, DefaultWeightedEdge> LDAG, String id){
    	
    	Iterator<String> iter = new DepthFirstIterator<String, DefaultWeightedEdge>(LDAG);
    	
    	String vertex;
    	String splitID;
    	while (iter.hasNext()) {
            vertex = iter.next();
            splitID = vertex.toString().split(":")[0];
            		
            if (splitID.equals(id)){
            	return vertex; 
            }
    	}
    	return null;
    }
    

    /**
   	 * Replace the IDs of the nodes/vertex in LDAG
   	 * att = 1 -> id, 2-> opt, 3 -> sup, 4-> ob
   	 */	
    void replaceAllVertex(ListenableDirectedAcyclic<String, DefaultWeightedEdge> LDAG, DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG, int att,String clase){
    
    
    	@SuppressWarnings("unchecked")
    	
		//ListenableDirectedAcyclic<String,DefaultWeightedEdge> g2 = (ListenableDirectedAcyclic<String, DefaultWeightedEdge>) g.clone();
        // Set<String> vertices = g2.vertexSet();
        
         
         Set<Object> vertices = new HashSet<Object>();
         vertices.addAll(LDAG.vertexSet());
         
         
         for (Object vertex : vertices) {
    		replaceVertex(LDAG,DAG,(String) vertex,att,clase);
         }	
    }	
    
     
    
    /**
   	 * Replace the IDs of the nodes/vertex in LDAG
   	 *  att = 1 -> id, 2-> opt, 3 -> sup, 4-> ob
   	 */	
    void  replaceVertex(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f,  String oldVertex,  int att, String clase)
    {
          	
    		Nodes  oldVertexNode = getVertex(f, oldVertex.split(":")[0]);
    		String newID= oldVertex;
    	
    		switch (att){
    			case 1:    		
    				newID = oldVertexNode.id;
    				break;

    			case 2:    		
    				newID = oldVertex.concat(":").concat(Double.toString(oldVertexNode.opt));
    				break;

    			case 3:
    				newID = oldVertex.concat(":").concat(Double.toString(oldVertexNode.arraySupport.get(clase)));
    				break;

    			case 4: 
    				newID = oldVertex.concat(":").concat(Double.toString(oldVertexNode.oblivion));
    				break;

    		}
    
    	if (!oldVertex.equals(newID)){
    		
    		String newVertex=newID;
    		Set<DefaultWeightedEdge> relatedEdges = g.edgesOf(oldVertex);
    		g.addVertex(newVertex);
        
        
    		String sourceVertex;
    		String targetVertex;
        
    		for (DefaultWeightedEdge e : relatedEdges) {
    			sourceVertex = g.getEdgeSource(e);
    			targetVertex = g.getEdgeTarget(e);
    			if (sourceVertex.equals(oldVertex)) {
    				g.addEdge(newVertex, targetVertex);
    			} else {
    				g.addEdge(sourceVertex, newVertex);
                }
            }
        
    		g.removeVertex(oldVertex);
    	}
    }  
    
   
    
    /**
   	 * Add list edge
   	 * 
   	 */	
    void addListEdge(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f,  String v1, String edges){
    	Nodes NewNode = this.getVertex(f, v1);
    	
    	if (!edges.equals("")){
			String[] numbers = edges.split(";"); 
			for (String e : numbers)  
			{  
                         
             //mi grafo
             Nodes Temp = this.getVertex(f,e);
             //RulesCov
                        
             try {
            	 
            	try { 
            		
            		if (f.containsVertex(Temp)){
            			f.addDagEdge(NewNode, Temp);

            		}
                      	
				
            	}catch (IllegalArgumentException  e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
                 }
				
             } catch (CycleFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
             }
    
			}
		}
    	
    }
    
    
    void addListEdge(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,  String v1, String edges)  {
    	String NewNode = this.getVertex(g, v1);
    	
    	if (!edges.equals("")){
			String[] numbers = edges.split(";"); 
			for (String e : numbers)  
			{  
				try { 
					//mi grafo
					String Temp = this.getVertex(g,e);
					
						if (g.containsVertex(Temp)){
						g.addEdge(NewNode, Temp);

						}			
				}catch (IllegalArgumentException  e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
    	
   	}
   
    
    
    /**
   	 * Add a new edge between 2 vertices v1,v2 (LDAG & DAG at the same time)
   	 *  
   	 */
    void addEdge(ListenableDirectedAcyclic<String, DefaultWeightedEdge> LDAG,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG,  String v1, String v2){
    	
    	Nodes vn1 = this.getVertex(DAG, v1);
    	Nodes vn2 = this.getVertex(DAG, v2);
    	
    	try {
    		if (DAG.containsVertex(vn1) && DAG.containsVertex(vn2)){
    			DAG.addDagEdge(vn1, vn2);
    		}			
		} catch (CycleFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	String vd1 = this.getVertex(LDAG, v1);
    	String vd2 = this.getVertex(LDAG, v2);
    	
    	LDAG.addEdge(vd1, vd2);
    	
    	//this.updateDAGdescriptors(DAG);
    	
    }
    
    
    /**
   	 * Delete a vertex v1 (LDAG & DAG)
   	 *  
   	 */
    void deleteVertex(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f,  String v1){
    	
    	Nodes vn1 = this.getVertex(f, v1);
    	String vd1 = this.getVertex(g, v1);
    	
		Set<DefaultWeightedEdge> relatedEdges = g.edgesOf(vd1);
		Set<DefaultWeightedEdge> relatedEdges2 = g.edgesOf(vd1);
     

		String sourceVertex;
		String targetVertex;
		
		String sourceVertex2;
		String targetVertex2;
		
		Nodes sourceVertexN;
		Nodes targetVertexN;

		
		//Ni es evidencia ni hoja
		
		if ((vn1.isGround == false) && (vn1.isLeaf== false)){
			
			for (DefaultWeightedEdge e : relatedEdges) {
			
				sourceVertex = g.getEdgeSource(e); //me quedo los vertices que salen de v1
				targetVertex = g.getEdgeTarget(e);
			
				if (sourceVertex.equals(vd1)) { //vertices que salen de v1, nodo a borrar
					for (DefaultWeightedEdge e2 : relatedEdges2){
					
						sourceVertex2 = g.getEdgeSource(e2); //de todos los vertices que salen/llegan a v1, me quiero quedar aquellos que llegan a v1
						targetVertex2 = g.getEdgeTarget(e2);
						
						if (targetVertex2.equals(vd1)) { //todos los que llegan a v1 deben apuntar a los que apunta v1 source2 --> target1
						
							this.deleteEdge(g, f, sourceVertex, targetVertex); //delete edge between v1 and target vertex (1er bucle)
						
							java.util.List<DefaultWeightedEdge> setEdgesPath = BellmanFordShortestPath.findPathBetween(g,  sourceVertex2,  targetVertex);
							//there is a direct connection  			
					
							if (setEdgesPath == null){//there is no path between them, the only path is the previously deleted edge
								this.addEdge(g, f, sourceVertex2, targetVertex);
							}			
						}
					}	
			
				}	
			}
		
		}
		else {if ((vn1.isGround == true)){ //a ground node does not point to any other node, EVIDENCE
		
			for (DefaultWeightedEdge e : relatedEdges) {
				
				sourceVertex = g.getEdgeSource(e);
				targetVertex = g.getEdgeTarget(e);
				
				if (targetVertex.equals(vd1)) {
					
					sourceVertexN = this.getVertex(f, sourceVertex);
					targetVertexN = this.getVertex(f, targetVertex);
					
					//if (!sourceVertexN.arrOb.contains(targetVertexN.id)){ //no se ha guardado la info del mismo nodo antes
					
					Iterator<String> it = classList.iterator();
					while(it.hasNext()){
		      			String c = it.next();
		      			sourceVertexN.arrayResidual.put(c, sourceVertexN.arrayResidual.get(c) + (targetVertexN.arraySupport.get(c)/g.inDegreeOf(targetVertex)));
		      			sourceVertexN.arrayMaxRes.put(c, sourceVertexN.arrayMaxRes.get(c) + targetVertexN.arrayMaxSup.get(c));
					}
					
					//sourceVertexN.arrOb.add(targetVertexN.id);
						
					if (f.outDegreeOf(sourceVertexN) == 1) {
						sourceVertexN.isLeaf = true;
					}
					//}
					
					
					
					
				}
			}
		
		}else if(vn1.isLeaf == true){ //no evidencia, nodo regla hoja
			
			for (DefaultWeightedEdge e : relatedEdges) {
				
				sourceVertex = g.getEdgeSource(e);
				targetVertex = g.getEdgeTarget(e);
				
				if (targetVertex.equals(vd1)) {
					
					sourceVertexN = this.getVertex(f, sourceVertex);
					targetVertexN = this.getVertex(f, targetVertex);
					
					//if (!sourceVertexN.arrOb.contains(targetVertexN.id)){ //no se ha guardado la info del mismo nodo antes
					
					Iterator<String> it = classList.iterator();
					while(it.hasNext()){												
		      			String c = it.next();		      			
		      			sourceVertexN.arrayResidual.put(c, sourceVertexN.arrayResidual.get(c) + (targetVertexN.arraySupport.get(c)/g.inDegreeOf(targetVertex)));
		      			sourceVertexN.arrayMaxRes.put(c, sourceVertexN.arrayMaxRes.get(c) + targetVertexN.arrayMaxSup.get(c));
					}
									
						
					//	sourceVertexN.arrOb.add(targetVertexN.id);
						
						if (f.outDegreeOf(sourceVertexN) == 1) {
							sourceVertexN.isLeaf = true;
						}
					//}
					
					
					
					
				}
			}
		}
		
		
		}
		
    	g.removeVertex(vd1);
    	f.removeVertex(vn1);
    	
    	
    }
    
    
    /**
   	 * Delete an edge v1,v2 (LDAG & DAG)
   	 *  
   	 */
    void deleteEdge(ListenableDirectedAcyclic<String, DefaultWeightedEdge> LDAG ,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG,  String v1, String v2){
    	
    	Nodes vn1 = this.getVertex(DAG, v1);
    	Nodes vn2 = this.getVertex(DAG, v2);
    	
    	
    	DAG.removeEdge(vn1, vn2);
    	
    	    	
    	String vd1 = this.getVertex(LDAG, v1);
    	String vd2 = this.getVertex(LDAG, v2);
    	
    	LDAG.removeEdge(vd1, vd2);
    	
    	//this.updateDAGdescriptors(DAG);
    }
    
    
    
    void connectionsBellman(ListenableDirectedAcyclic<String, DefaultWeightedEdge> LDAG,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG){
    	
    	
    	Set<String> vertices1 = LDAG.vertexSet();
    	
    	Iterator<String> iter = new DepthFirstIterator<String, DefaultWeightedEdge>(LDAG);
    	
    	String vertexI;
    	
    	while (iter.hasNext()) {
           
    		vertexI = iter.next();
     
            for (String vertexJ: vertices1){
        		
        		if (!vertexI.equals(vertexJ)){
        			
        			java.util.List<DefaultWeightedEdge> setEdgesPath = BellmanFordShortestPath.findPathBetween(LDAG,  vertexI,  vertexJ);
        			//there is a direct connection
        			if (setEdgesPath != null && setEdgesPath.size()==1){
        				//remoove that edge in order to check if there is another longest connection
        				LDAG.removeEdge(vertexI, vertexJ);
        				
        				
        				java.util.List<DefaultWeightedEdge> setEdgesPath2 = BellmanFordShortestPath.findPathBetween(LDAG,  vertexI,  vertexJ);
        				
        				if (setEdgesPath2 == null){
        					//if there is another path, the edge is correctly removed, if not, we need to add again the removed vertex
        					LDAG.addEdge(vertexI, vertexJ);
        				}else{
        					DAG.removeEdge(this.getVertex(DAG, vertexI), this.getVertex(DAG, vertexJ));
        				}
        				
        				
        			}
        		}
        		
        	}
           
    	}
    	//this.updateDAGdescriptors(f);
    	
    	

	    }
 
    
    void connectionsAllpaths(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
    	
    	
    	Set<String> vertices1 = g.vertexSet();
    	
    	
    	Iterator<String> iter = new DepthFirstIterator<String, DefaultWeightedEdge>(g);
    	
    	String vertexI;
    	
    	while (iter.hasNext()) {
           
    		vertexI = this.getVertex(g, iter.next());
     
            for (String vertexJ: vertices1){
        		
            	vertexJ = this.getVertex(g, vertexJ);
        		if (!vertexI.equals(vertexJ)){
        			
        			java.util.List<DefaultWeightedEdge> setEdgesPath = BellmanFordShortestPath.findPathBetween(g,  vertexI, vertexJ);
        			//there is a direct connection
        			if (setEdgesPath != null && setEdgesPath.size()>1){
        				//there is a path (not directed) between nodes
        				//System.out.println("Edge: "+vertexI+" ,"+)
        				g.addEdge(vertexI, vertexJ);
        				try {
							f.addDagEdge(this.getVertex(f, vertexI), this.getVertex(f, vertexJ));
						} catch (CycleFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				
        			}
        		}
      
        	}
           
    	}
    	
    	//this.updateDAGdescriptors(f);
    		
	    }
    
    
    int numPermanentORground(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
    	int num = 0;
    	
    	Set<Nodes> vertices1 = f.vertexSet();
    	
    	
    	Iterator<Nodes> iter = new DepthFirstIterator<Nodes,DefaultWeightedEdge>(f);
    	
    	Nodes vertexI;
    	
    	while (iter.hasNext()) {
           
    		vertexI = iter.next();
    		
    		if (vertexI.permanent || vertexI.isGround){
    			num++;
    		}    
    	}
    	
    	return num;
    }
    
    
    
    
    ArrayList<Nodes> FromExcel(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
    	
    	ArrayList<Nodes> MyRules = ReadRulesExcel.ReadRulesExcel("C:\\Users\\Nando\\workspace\\MyGraph\\src\\MyGraphPack\\PT_Rules.xlsx");
		return MyRules;
    	
    	
    	
    }
    

    
   
  
  public ArrayList<RuleCov> RulesC = new ArrayList<RuleCov>();
  public ArrayList<ExampleCov> ExampleC = new ArrayList<ExampleCov>();
  
void statsGerl_ini(){
	  
	 //negative evidence
	String nq1 ="letter([r,s,c])-> c";
	String nq2 ="letter([r,s,c,d])-> d";
	String nq3 ="letter([r,s,c,d,s])-> s";
	String nq4 ="letter([r,s,c,d,s,t])-> t";
	String nq5 ="letter([r,s,c,d,s,t,d])-> d";
	String nq6 ="letter([r,s,c,d,s,t,d,e])-> e";
	String nq7 ="letter([r,s,c,d,s,t,d,e,t])-> t";
	String nq8 ="letter([r,s,c,d,s,t,d,e,t,u])-> u";
	String nq9 ="letter([r,s,c,d,s,t,d,e,t,u,e])-> e";
	String nq10 ="letter([r,s,c,d,s,t,d,e,t,u,e,f])-> f";
	//positive evidence
	String q10 ="letter([r,s,c,d,s,t,d,e,t,u,e,f])-> u";
	String q9 ="letter([r,s,c,d,s,t,d,e,t,u,e])->f";
	String q8 ="letter([r,s,c,d,s,t,d,e,t,u])->e";
	String q7 ="letter([r,s,c,d,s,t,d,e,t])->u";
	String q6 ="letter([r,s,c,d,s,t,d,e])->t";
	String q5 ="letter([r,s,c,d,s,t,d])->e";
	String q4 ="letter([r,s,c,d,s,t])->d";
	String q3 ="letter([r,s,c,d,s])->t";
	String q2 ="letter([r,s,c,d])->s";
	String q1 ="letter([r,s,c])->d";
	//rules
	String q117 ="letter(List) -> init(init(init(init(init(init(init(init(init(init(List))))))))))";
	String q95 ="letter(List) -> init(init(init(init(init(init(init(init(init(List)))))))))";//q117 
	String q68 ="letter(List) -> init(init(init(init(init(init(init(init(List))))))))";//q95;q117 
	String q56 ="letter(List) -> init(init(init(init(init(init(init(List)))))))";//q68;q95;q117 
	String q49 ="letter(List) -> init(init(init(init(init(init(List))))))";//q56;q68;q95;q117 
	String q31 ="letter(List) -> init(init(init(init(init(List)))))";//q49;q56;q68;q95;q117 
	String q27 ="letter(List) -> init(init(init(init(List))))";//q31;q49;q56;q68;q95;q117 
	String q18 ="letter(List) -> init(init(init(List)))";//q27;q31;q49;q56;q68;q95;q117 
	String q13 ="letter(List) -> init(init(List))";//q18;q27;q31;q49;q56;q68;q95;q117 
	String q12 ="letter(List) -> init(List)";//q13;q18;q27;q31;q49;q56;q68;q95;q117 
	String q116 ="letter(List) -> last(init(init(init(init(init(init(init(init(init(List))))))))))";
	String q98 ="letter(List) -> last(init(init(init(init(init(init(init(init(List)))))))))";//q116 
	String q75 ="letter(List) -> last(init(init(init(init(init(init(init(List))))))))";//q98;q116 
	String q55 ="letter(List) -> last(init(init(init(init(init(init(List)))))))";//q75;q98;q116 
	String q50 ="letter(List) -> last(init(init(init(init(init(List))))))";//q55;q75;q98;q116 
	String q32 ="letter(List) -> last(init(init(init(init(List)))))";//q50;q55;q75;q98;q116 
	String q28 ="letter(List) -> last(init(init(init(List))))";//q32;q50;q55;q75;q98;q116;nq3;nq5;nq7;nq9 
	String q19 ="letter(List) -> last(init(init(List)))";//q2;q4;q6;q8;q10;q28;q32;q50;q55;q75;q98;q116 
	String q14 ="letter(List) -> last(init(List))";//q19;q28;q32;q50;q55;q75;q98;q116 
	String q121 ="letter(List) -> last(last(init(init(init(init(init(init(init(List)))))))))";
	String q97 ="letter(List) -> last(last(init(init(init(init(init(init(List))))))))";//q121 
	String q83 ="letter(List) -> last(last(init(init(init(init(init(List)))))))";//q97;q121 
	String q74 ="letter(List) -> last(last(init(init(init(init(List))))))";//q83;q97;q121 
	String q54 ="letter(List) -> last(last(init(init(init(List)))))";//q74;q83;q97;q121 
	String q43 ="letter(List) -> last(last(init(init(List))))";//q54;q74;q83;q97;q121 
	String q41 ="letter(List) -> last(last(init(List)))";//q43;q54;q74;q83;q97;q121 
	String q11 ="letter(List) -> last(List)";//q14;q19;q28;q32;q50;q55;q75;q98;q116;q41;q43;q54;q74;q83;q97;q121;nq1;nq2;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10 
	String q114 ="letter(List) -> next(last(init(init(init(init(init(init(List))))))))";//q6;q8;q10 
	String q104 ="letter(List) -> next(last(init(init(init(init(init(List)))))))";//q114 
	String q51 ="letter(List) -> next(last(init(init(init(init(List))))))";//q104;q114;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10 
	String q33 ="letter(List) -> next(last(init(init(init(List)))))";//q2;q3;q4;q5;q6;q7;q8;q9;q10;q51;q104;q114 
	String q29 ="letter(List) -> next(last(init(init(List))))";//q33;q51;q104;q114 
	String q21 ="letter(List) -> next(last(init(List)))";//q29;q33;q51;q104;q114;nq2;nq4;nq6;nq8;nq10 
	String q107 ="letter(List) -> next(last(last(init(init(init(List))))))";
	String q106 ="letter(List) -> next(last(last(init(init(List)))))";//q107 
	String q91 ="letter(List) -> next(last(last(init(List))))";//q106;q107 
	String q17 ="letter(List) -> next(last(List))";//q1;q3;q5;q7;q9;q21;q29;q33;q51;q104;q114;q91;q106;q107 
	String q103 ="letter(List) -> next(next(last(init(init(init(init(init(List))))))))";
	String q81 ="letter(List) -> next(next(last(init(init(init(init(List)))))))";//q3;q5;q7;q9;q103;nq4;nq6;nq8;nq10 
	String q77 ="letter(List) -> next(next(last(init(init(init(List))))))";//q81;q103 
	String q58 ="letter(List) -> next(next(last(init(init(List)))))";//q77;q81;q103 
	String q44 ="letter(List) -> next(next(last(init(List))))";//q58;q77;q81;q103 
	String q42 ="letter(List) -> next(next(last(List)))";//q44;q58;q77;q81;q103 
	String q119 ="letter(List) -> next(next(next(last(init(init(init(init(init(List)))))))))";
	String q100 ="letter(List) -> next(next(next(last(init(init(init(init(List))))))))";//q119 
	String q80 ="letter(List) -> next(next(next(last(init(init(init(List)))))))";//q100;q119 
	String q73 ="letter(List) -> next(next(next(last(init(init(List))))))";//q80;q100;q119 
	String q69 ="letter(List) -> next(next(next(last(init(List)))))";//q73;q80;q100;q119 
	String q67 ="letter(List) -> next(next(next(last(List))))";//q69;q73;q80;q100;q119 
	String q109 ="letter(List) -> next(next(previous(last(init(init(List))))))";
	String q88 ="letter(List) -> next(next(previous(last(init(List)))))";//q109;nq2;nq4;nq6;nq8;nq10 
	String q65 ="letter(List) -> next(next(previous(last(List))))";//q1;q3;q5;q7;q9;q88;q109 
	String q120 ="letter(List) -> next(previous(last(init(init(init(init(init(init(List)))))))))";
	String q112 ="letter(List) -> next(previous(last(init(init(init(init(init(List))))))))";//q120 
	String q111 ="letter(List) -> next(previous(last(init(init(init(init(List)))))))";//q112;q120 
	String q93 ="letter(List) -> next(previous(last(init(init(init(List))))))";//q111;q112;q120;nq3;nq5;nq7;nq9 
	String q57 ="letter(List) -> next(previous(last(init(init(List)))))";//q2;q4;q6;q8;q10;q93;q111;q112;q120 
	String q46 ="letter(List) -> next(previous(last(init(List))))";//q57;q93;q111;q120 
	String q39 ="letter(List) -> next(previous(last(List)))";//q46;q57;q93;q111;q112;q120;nq1;nq2;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10 
	String q118 ="letter(List) -> previous(last(init(init(init(init(init(init(init(List)))))))))";
	String q99 ="letter(List) -> previous(last(init(init(init(init(init(init(List))))))))";//q118 
	String q84 ="letter(List) -> previous(last(init(init(init(init(init(List)))))))";//q99;q118 
	String q48 ="letter(List) -> previous(last(init(init(init(init(List))))))";//q84;q99;q118 
	String q30 ="letter(List) -> previous(last(init(init(init(List)))))";//q48;q84;q99;q118 
	String q26 ="letter(List) -> previous(last(init(init(List))))";//q30;q48;q84;q99;q118 
	String q20 ="letter(List) -> previous(last(init(List)))";//q26;q30;q48;q84;q99;q118 
	String q123 ="letter(List) -> previous(last(last(init(init(init(init(init(init(List)))))))))";
	String q102 ="letter(List) -> previous(last(last(init(init(init(init(init(List))))))))";//q123 
	String q79 ="letter(List) -> previous(last(last(init(init(init(init(List)))))))";//q102;q123 
	String q72 ="letter(List) -> previous(last(last(init(init(init(List))))))";//q79;q102;q123 
	String q70 ="letter(List) -> previous(last(last(init(init(List)))))";//q72;q79;q102;q123 
	String q64 ="letter(List) -> previous(last(last(init(List))))";//q70;q72;q79;q102;q123 
	String q16 ="letter(List) -> previous(last(List))";//q20;q26;q30;q48;q84;q99;q118;q64;q70;q72;q79;q102;q123 
	String q124 ="letter(List) -> previous(next(last(init(init(init(init(init(init(List)))))))))";
	String q113 ="letter(List) -> previous(next(last(init(init(init(init(init(List))))))))";//q124 
	String q110 ="letter(List) -> previous(next(last(init(init(init(init(List)))))))";//q113;q124 
	String q94 ="letter(List) -> previous(next(last(init(init(init(List))))))";//q110;q113;q124;nq3;nq5;nq7;nq9 
	String q52 ="letter(List) -> previous(next(last(init(init(List)))))";//q2;q4;q6;q8;q10;q94;q110;q113;q124 
	String q47 ="letter(List) -> previous(next(last(init(List))))";//q52;q94;q110;q113;q124 
	String q37 ="letter(List) -> previous(next(last(List)))";//q47;q52;q94;q110;q113;q124;nq1;nq2;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10 
	String q122 ="letter(List) -> previous(previous(last(init(init(init(init(init(init(List)))))))))";
	String q115 ="letter(List) -> previous(previous(last(init(init(init(init(init(List))))))))";//q122 
	String q96 ="letter(List) -> previous(previous(last(init(init(init(init(List)))))))";//q115;q122 
	String q78 ="letter(List) -> previous(previous(last(init(init(init(List))))))";//q96;q115;q122 
	String q53 ="letter(List) -> previous(previous(last(init(init(List)))))";//q78;q96;q115;q122 
	String q45 ="letter(List) -> previous(previous(last(init(List))))";//q53;q78;q96;q115;q122 
	String q36 ="letter(List) -> previous(previous(last(List)))";//q45;q53;q78;q96;q115;q122 
	String q108 ="letter(List) -> previous(previous(next(last(init(init(List))))))";
	String q89 ="letter(List) -> previous(previous(next(last(init(List)))))";//q108 
	String q86 ="letter(List) -> previous(previous(next(last(List))))";//q89;q108 
	String q125 ="letter(List) -> previous(previous(previous(last(init(init(init(init(init(List)))))))))";//q86;q89;q108 
	String q101 ="letter(List) -> previous(previous(previous(last(init(init(init(init(List))))))))";//q125;q86;q89;q108 
	String q82 ="letter(List) -> previous(previous(previous(last(init(init(init(List)))))))";//q101;q125;q86;q89;q108 
	String q76 ="letter(List) -> previous(previous(previous(last(init(init(List))))))";//q82;q101;q125;q86;q89;q108 
	String q71 ="letter(List) -> previous(previous(previous(last(init(List)))))";//q76;q82;q101;q125;q86;q89;q108 
	String q62 ="letter(List) -> previous(previous(previous(last(List))))";//q71;q76;q82;q101;q125;q86;q89;q108 
	  
	  
		ExampleC.add(0,new ExampleCov("q0","nada", 0, 0, 0, 0, 0, 0, "+"));
		//positive evidence
		ExampleC.add(1,new ExampleCov("q1",q1, 4, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(2,new ExampleCov("q2",q2, 5, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(3,new ExampleCov("q3",q3, 6, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(4,new ExampleCov("q4",q4, 7, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(5,new ExampleCov("q5",q5, 8, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(6,new ExampleCov("q6",q6, 9, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(7,new ExampleCov("q7",q7, 10, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(8,new ExampleCov("q8",q8, 11, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(9,new ExampleCov("q9",q9, 12, 0, 0, 0, 2, 0, "+"));
		ExampleC.add(10,new ExampleCov("q10",q10, 13, 0, 0, 0, 2, 0, "+"));
	  
		 //negative evidence
		ExampleC.add(11,new ExampleCov("nq1",nq1, 4, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(12,new ExampleCov("nq2",nq2, 5, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(13,new ExampleCov("nq3",nq3, 6, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(14,new ExampleCov("nq4",nq4, 7, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(15,new ExampleCov("nq5",nq5, 8, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(16,new ExampleCov("nq6",nq6, 9, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(17,new ExampleCov("nq7",nq7, 10, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(18,new ExampleCov("nq8",nq8, 11, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(19,new ExampleCov("nq9",nq9, 12, 0, 0, 0, 2, 0, "-"));
		ExampleC.add(20,new ExampleCov("nq10",nq10, 13, 0, 0, 0, 2, 0, "-"));
	  
	  
	 
	  	
///////////////////Rules////////////////////////////////////////////////////////////
  		
  	RulesC.add(0,new RuleCov("q0","nada","", 0, 0, 0, 0, 0, 0));
  	  	
  	RulesC.add(1,  new RuleCov("q117", "letter(List) -> init(init(init(init(init(init(init(init(init(init(List))))))))))", "", 0, 0, 2, 1, 11, 1));
  	RulesC.add(2,  new RuleCov("q95", "letter(List) -> init(init(init(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 1));
  	RulesC.add(3,  new RuleCov("q68", "letter(List) -> init(init(init(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 1));
  	RulesC.add(4,  new RuleCov("q56", "letter(List) -> init(init(init(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 1));
  	RulesC.add(5,  new RuleCov("q49", "letter(List) -> init(init(init(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 1));
  	RulesC.add(6,  new RuleCov("q31", "letter(List) -> init(init(init(init(init(List)))))", "", 0, 0, 2, 1, 6, 1));
  	RulesC.add(7,  new RuleCov("q27", "letter(List) -> init(init(init(init(List))))", "", 0, 0, 2, 1, 5, 1));
  	RulesC.add(8,  new RuleCov("q18", "letter(List) -> init(init(init(List)))", "", 0, 0, 2, 1, 4, 1));
  	RulesC.add(9,  new RuleCov("q13", "letter(List) -> init(init(List))", "", 0, 0, 2, 1, 3, 1));
  	RulesC.add(10,  new RuleCov("q12", "letter(List) -> init(List)", "", 0, 0, 2, 1, 2, 1));
  	
  	
  	RulesC.add(11,  new RuleCov("q116", "letter(List) -> last(init(init(init(init(init(init(init(init(init(List))))))))))", "", 0, 0, 2, 1, 11, 2));
  	RulesC.add(12,  new RuleCov("q98", "letter(List) -> last(init(init(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 2));
  	RulesC.add(13,  new RuleCov("q75", "letter(List) -> last(init(init(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 2));
  	RulesC.add(14,  new RuleCov("q55", "letter(List) -> last(init(init(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 2));
  	RulesC.add(15,  new RuleCov("q50", "letter(List) -> last(init(init(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 2));
  	RulesC.add(16,  new RuleCov("q32", "letter(List) -> last(init(init(init(init(List)))))", "", 0, 0, 2, 1, 6, 2));
  	RulesC.add(17,  new RuleCov("q28", "letter(List) -> last(init(init(init(List))))", "nq3;nq5;nq7;nq9", 0, 0, 2, 1, 5, 2));
  	RulesC.add(18,  new RuleCov("q19", "letter(List) -> last(init(init(List)))", "57", 0, 0, 2, 1, 4, 2));
  	RulesC.add(19,  new RuleCov("q14", "letter(List) -> last(init(List))", "", 0, 0, 2, 1, 3, 2));
  	
  	
  	RulesC.add(20,  new RuleCov("q121", "letter(List) -> last(last(init(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 2));
  	RulesC.add(21,  new RuleCov("q97", "letter(List) -> last(last(init(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 2));
  	RulesC.add(22,  new RuleCov("q83", "letter(List) -> last(last(init(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 2));
  	RulesC.add(23,  new RuleCov("q74", "letter(List) -> last(last(init(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 2));
  	RulesC.add(24,  new RuleCov("q54", "letter(List) -> last(last(init(init(init(List)))))", "", 0, 0, 2, 1, 6, 2));
  	RulesC.add(25,  new RuleCov("q43", "letter(List) -> last(last(init(init(List))))", "", 0, 0, 2, 1, 5, 2));
  	RulesC.add(26,  new RuleCov("q41", "letter(List) -> last(last(init(List)))", "", 0, 0, 2, 1, 4, 2));
  	RulesC.add(26,  new RuleCov("q11", "letter(List) -> last(List)", "nq1;nq2;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10", 0, 0, 2, 1, 2, 1));
  	
  	
  	
  	RulesC.add(27,  new RuleCov("q114", "letter(List) -> next(last(init(init(init(init(init(init(List))))))))", "q6;q8;q10", 0, 0, 2, 1, 9, 3));
  	RulesC.add(28,  new RuleCov("q104", "letter(List) -> next(last(init(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 7));
  	RulesC.add(29,  new RuleCov("q51", "letter(List) -> next(last(init(init(init(init(List))))))", "nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10", 0, 0, 2, 1, 7, 3));
  	RulesC.add(30,  new RuleCov("q33", "letter(List) -> next(last(init(init(init(List)))))", "q2;q3;q4;q5;q6;q7;q8;q9;q10", 0, 0, 2, 1, 6, 3));
  	RulesC.add(31,  new RuleCov("q29", "letter(List) -> next(last(init(init(List))))", "", 0, 0, 2, 1, 5, 3));
  	RulesC.add(32,  new RuleCov("q21", "letter(List) -> next(last(init(List)))", "nq2;nq4;nq6;nq8;nq10", 0, 0, 2, 1, 4, 3));
  	
  	
  	RulesC.add(33,  new RuleCov("q107", "letter(List) -> next(last(last(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 3));
  	RulesC.add(34,  new RuleCov("q106", "letter(List) -> next(last(last(init(init(List)))))", "", 0, 0, 2, 1, 6, 3));
  	RulesC.add(35,  new RuleCov("q91", "letter(List) -> next(last(last(init(List))))", "", 0, 0, 2, 1, 5, 3));
  	RulesC.add(36,  new RuleCov("q17", "letter(List) -> next(last(List))", "q1;q3;q5;q7;q9;", 0, 0, 2, 1, 3, 2));
  	
  	
  	RulesC.add(37,  new RuleCov("q103", "letter(List) -> next(next(last(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 3));
  	RulesC.add(38,  new RuleCov("q81", "letter(List) -> next(next(last(init(init(init(init(List)))))))", "q3;q5;q7;q9;nq4;nq6;nq8;nq10", 0, 0, 2, 1, 8, 3));
  	RulesC.add(39,  new RuleCov("q77", "letter(List) -> next(next(last(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 3));
  	RulesC.add(40,  new RuleCov("q58", "letter(List) -> next(next(last(init(init(List)))))", "", 0, 0, 2, 1, 6, 3));
  	RulesC.add(41,  new RuleCov("q44", "letter(List) -> next(next(last(init(List))))", "", 0, 0, 2, 1, 5, 3));
  	RulesC.add(42,  new RuleCov("q42", "letter(List) -> next(next(last(List)))", "", 0, 0, 2, 1, 4, 2));
  	
  	
  	RulesC.add(43,  new RuleCov("q119", "letter(List) -> next(next(next(last(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 3));
  	RulesC.add(44,  new RuleCov("q100", "letter(List) -> next(next(next(last(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 3));
  	RulesC.add(45,  new RuleCov("q80", "letter(List) -> next(next(next(last(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 3));
  	RulesC.add(46,  new RuleCov("q73", "letter(List) -> next(next(next(last(init(init(List))))))", "", 0, 0, 2, 1, 7, 3));
  	RulesC.add(47,  new RuleCov("q69", "letter(List) -> next(next(next(last(init(List)))))", "", 0, 0, 2, 1, 6, 3));
  	RulesC.add(48,  new RuleCov("q67", "letter(List) -> next(next(next(last(List))))", "", 0, 0, 2, 1, 5, 2));
  	
  	
  	RulesC.add(49,  new RuleCov("q109", "letter(List) -> next(next(previous(last(init(init(List))))))", "", 0, 0, 2, 1, 7, 4));
  	RulesC.add(50,  new RuleCov("q88", "letter(List) -> next(next(previous(last(init(List)))))", "nq2;nq4;nq6;nq8;nq10", 0, 0, 2, 1, 6, 4));
  	RulesC.add(51,  new RuleCov("q65", "letter(List) -> next(next(previous(last(List))))", "q1;q3;q5;q7;q9", 0, 0, 2, 1, 5, 3));
  	
  	
  	RulesC.add(52,  new RuleCov("q120", "letter(List) -> next(previous(last(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 4));
  	RulesC.add(53,  new RuleCov("q112", "letter(List) -> next(previous(last(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 4));
  	RulesC.add(54,  new RuleCov("q111", "letter(List) -> next(previous(last(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 4));
  	RulesC.add(55,  new RuleCov("q93", "letter(List) -> next(previous(last(init(init(init(List))))))", "nq3;nq5;nq7;nq9", 0, 0, 2, 1, 7, 4));
  	RulesC.add(56,  new RuleCov("q57", "letter(List) -> next(previous(last(init(init(List)))))", "q2;q4;q6;q8;q10", 0, 0, 2, 1, 6, 4));
  	RulesC.add(57,  new RuleCov("q46", "letter(List) -> next(previous(last(init(List))))", "", 0, 0, 2, 1, 5, 4));
  	RulesC.add(58,  new RuleCov("q39", "letter(List) -> next(previous(last(List)))", "nq1;nq2;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10", 0, 0, 2, 1, 4, 3));
  	
  	
  	RulesC.add(59,  new RuleCov("q118", "letter(List) -> previous(last(init(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 3));
  	RulesC.add(60,  new RuleCov("q99", "letter(List) -> previous(last(init(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 3));
  	RulesC.add(61,  new RuleCov("q84", "letter(List) -> previous(last(init(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 3));
  	RulesC.add(62,  new RuleCov("q48", "letter(List) -> previous(last(init(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 3));
  	RulesC.add(63,  new RuleCov("q30", "letter(List) -> previous(last(init(init(init(List)))))", "", 0, 0, 2, 1, 6, 3));
  	RulesC.add(64,  new RuleCov("q26", "letter(List) -> previous(last(init(init(List))))", "", 0, 0, 2, 1, 5, 3));
  	RulesC.add(65,  new RuleCov("q20", "letter(List) -> previous(last(init(List)))", "", 0, 0,2, 1, 4, 3));
  	
  	
  	RulesC.add(66,  new RuleCov("q123", "letter(List) -> previous(last(last(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 3));
  	RulesC.add(67,  new RuleCov("q102", "letter(List) -> previous(last(last(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 3));
  	RulesC.add(68,  new RuleCov("q79", "letter(List) -> previous(last(last(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 3));
  	RulesC.add(69,  new RuleCov("q72", "letter(List) -> previous(last(last(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 3));
  	RulesC.add(70,  new RuleCov("q70", "letter(List) -> previous(last(last(init(init(List)))))", "", 0, 0, 2, 1, 6, 3));
  	RulesC.add(71,  new RuleCov("q64", "letter(List) -> previous(last(last(init(List))))", "", 0, 0, 2, 1, 5, 3));
  	RulesC.add(72,  new RuleCov("q16", "letter(List) -> previous(last(List))", "", 0, 0, 2, 1, 4, 2));
  	
  	
  	
  	
  	RulesC.add(73,  new RuleCov("q124", "letter(List) -> previous(next(last(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 4));
  	RulesC.add(74,  new RuleCov("q113", "letter(List) -> previous(next(last(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 4));
  	RulesC.add(75,  new RuleCov("q110", "letter(List) -> previous(next(last(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 4));
  	RulesC.add(76,  new RuleCov("q94", "letter(List) -> previous(next(last(init(init(init(List))))))", "nq3;nq5;nq7;nq9", 0, 0, 2, 1, 7, 4));
  	RulesC.add(77,  new RuleCov("q52", "letter(List) -> previous(next(last(init(init(List)))))", "q2;q4;q6;q8;q10", 0, 0, 2, 1, 6, 4));
  	RulesC.add(78,  new RuleCov("q47", "letter(List) -> previous(next(last(init(List))))", "", 0, 0, 2, 1, 5, 4));
  	RulesC.add(79,  new RuleCov("q37", "letter(List) -> previous(next(last(List)))", "nq1;nq2;nq3;nq4;nq5;nq6;nq7;nq8;nq9;nq10", 0, 0, 2, 1, 4, 3));
  	
  	
  	RulesC.add(80,  new RuleCov("q122", "letter(List) -> previous(previous(last(init(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 4));
  	RulesC.add(81,  new RuleCov("q115", "letter(List) -> previous(previous(last(init(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 4));
  	RulesC.add(82,  new RuleCov("q96", "letter(List) -> previous(previous(last(init(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 4));
  	RulesC.add(83,  new RuleCov("q78", "letter(List) -> previous(previous(last(init(init(init(List))))))", "", 0, 0, 2, 1, 7, 4));
  	RulesC.add(84,  new RuleCov("q53", "letter(List) -> previous(previous(last(init(init(List)))))", "", 0, 0, 2, 1, 6, 4));
  	RulesC.add(85,  new RuleCov("q45", "letter(List) -> previous(previous(last(init(List))))", "", 0, 0, 2, 1, 5, 4));
  	RulesC.add(86,  new RuleCov("q36", "letter(List) -> previous(previous(last(List)))", "", 0, 0, 2, 1, 4, 3));
  	
  	
  	RulesC.add(87,  new RuleCov("q108", "letter(List) -> previous(previous(next(last(init(init(List))))))", "", 0, 0, 2, 1, 7, 4));
  	RulesC.add(88,  new RuleCov("q89", "letter(List) -> previous(previous(next(last(init(List)))))", "", 0, 0, 2, 1, 6, 4));
  	RulesC.add(89,  new RuleCov("q86", "letter(List) -> previous(previous(next(last(List))))", "", 0, 0, 2, 1, 5, 3));
  	
  	
  	RulesC.add(90,  new RuleCov("q125", "letter(List) -> previous(previous(previous(last(init(init(init(init(init(List)))))))))", "", 0, 0, 2, 1, 10, 4));
  	RulesC.add(91,  new RuleCov("q101", "letter(List) -> previous(previous(previous(last(init(init(init(init(List))))))))", "", 0, 0, 2, 1, 9, 4));
  	RulesC.add(92,  new RuleCov("q82", "letter(List) -> previous(previous(previous(last(init(init(init(List)))))))", "", 0, 0, 2, 1, 8, 4));
  	RulesC.add(93,  new RuleCov("q76", "letter(List) -> previous(previous(previous(last(init(init(List))))))", "", 0, 0, 2, 1, 7, 4));
  	RulesC.add(94,  new RuleCov("q71", "letter(List) -> previous(previous(previous(last(init(List)))))", "", 0, 0, 2, 1, 6, 4));
  	RulesC.add(95,  new RuleCov("q62", "letter(List) -> previous(previous(previous(last(List))))", "", 0, 0, 2, 1, 5, 3));
  	  

		
		
        
        
        
        	System.out.println("Step\tExa\tRul\tCon\tPop\tAvgOpt");
        	System.out.println("---------------------------------------------");
        	
        	
        
  }
  
  
  
  void statsICMLA_ini(){
	  
	  
	  String r1 = "move(rook,pos(b,2),pos(e,2))."; 
      String r2 = "move(rook,pos(a,5),pos(h,5))";
      String r3 = "move(rook,pos(d,2),pos(d,5))";
      String r4 = "move(rook,pos(a,4),pos(a,5))";
      String r5 = ":- move(rook,pos(f,1),pos(b,3))";
      String r6 = ":- move(rook,pos(h,4),pos(a,2))";
      
      
      String r7 = "move(rook,pos(A,2),pos(B,2))."; //r1
      String r8 = "move(rook,pos(b,B),pos(e,B))."; //r1    	        
       String r9 = "move(rook,pos(a,B),pos(h,B))."; //r2
      String r10 = "move(rook,pos(a,A),pos(h,5))."; //r2  
      String r11 = "move(rook,pos(d,B),pos(d,C))."; //r3
      String r12 = "move(rook,pos(d,2),pos(d,C))."; //r3
      String r13 = "move(rook,pos(d,B),pos(d,5))."; //r3
      String r14 = "move(rook,pos(A,2),pos(C,D))."; //r11,r7,r3,r1,
      String r15 = "move(rook,pos(A,B),pos(A,C))."; //r11,r12,r4,r13,r3
      String r16 = "move(rook,pos(A,B),pos(C,B))."; //r1,r2,r7,r8,r9
      String r17 = "move(rook,pos(A,B),pos(C,D))."; //r1-r16
      
      String k1 = "move(knight,pos(b,2),pos(c,4))."; 
      String k2 = "move(knight,pos(b,2),pos(d,3)).";
      String k3 = "move(knight,pos(c,4),pos(b,6)).";
      String k4 = "move(knight,pos(c,4),pos(a,5)).";
      String k5 = "move(knight,pos(f,1),pos(g,3)).";
      String k6 = "move(knight,pos(f,1),pos(h,2)).";
      String k7 = "move(knight,pos(f,1),pos(g,3)).";      
      String k8 = ":- move(knight,pos(f,1),pos(g,4)).";
      String k9 = ":- move(knight,pos(f,1),pos(c,2)).";
      String k10 = ":- move(knight,pos(f,1),pos(h,3)).";  
      
      
      String k11 = "move(knight,pos(A,2),pos(C,4))."; //r1    	       
      String k12 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,2)."; //k1;k3;k5;k7;k10;k13;k15;k21;k22
      String k13 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(C,C,0)."; //r1;r3;r5;r7;r10;k15 
      String k14 = "move(knight,pos(A,B),pos(C,D)) :- fdiff(A,C,1)."; //k1;k3;k5;k7;k8;k22;k23 
      String k15 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,A,0), fdiff(C,C,0).";//k1;k3;k5;k7;k10     	            	        
      String k16 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,1), fdiff(A,A,0).]"; //k2;k4;k6;k9;k18
      String k17 = "move(knight,pos(A,B),pos(C,D)) :- fdiff(A,C,E)."; //k1;k2;k3;k4;k5;k6;k7;k8;k9;k10;k14;k15;k16;k18;k19;k20;k21;k22;k23;k24;k25
      String k18 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,B,0), rdiff(B,D,1), fdiff(A,A,0).";//k2;k4;k6;k9
      String k19 = "move(knight,pos(A,B),pos(C,D)) :- fdiff(A,C,2).";//k2;k4;k6;k10;k21;k24
      String k20 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,B,0), fdiff(A,A,0).";//k1;k2;k3;k4;k5;k6;k7;k8;k9;k10
      String k21 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,C,2).";//k10
      String k22 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,C,1).";//k1;k3;k5;k7
      String k23 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,1), fdiff(A,C,1).";//
      String k24 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,1), fdiff(A,C,2).";//k2;k4;k6
      String k25 = "move(knight,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,F).";//k1;k2;k3;k4;k5;k6;k7;k8;k9;k10;k13;k15;k16;k18;k20;k21;k22;k23;k24
      
      
      

      String b1 = "move(bishop,pos(f,1),pos(c,4))."; 
      String b2 = "move(bishop,pos(c,4),pos(e,6)).";
      String b3 = "move(bishop,pos(e,6),pos(a,2)).";
      String b4 = ":- move(bishop,pos(f,1),pos(b,3)).";
      
      
      String b5 = "move(bishop,pos(A,B),pos(C,D)) :- fdiff(A,C,2)."; //b2;b6;b7   
      String b6 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,C,2).";//b2    	        
      String b7 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,C,2).";//b2
      String b8 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,3), fdiff(A,C,3).";//b1
      String b9 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,2)";//b2;b4;b6;b7
      String b10 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,E).";//b1;b2;b3;b6;b7;b8
      String b11 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,B,0), rdiff(D,D,0), fdiff(A,A,0).";//b1;b2;b3;b4
      String b12 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,F).";//b1;b2;b3;b4;b6;b7;b8;b10;b13
      String b13 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,3).";//b1;b4;b8
      
      
      
      String q1 = "move(queen,pos(f,1),pos(c,4))."; 
      String q2 = "move(queen,pos(c,4),pos(e,6)).";
      String q3 = "move(queen,pos(e,6),pos(a,2)).";
      String q4 = "move(queen,pos(b,2),pos(e,2))."; 
      String q5 = "move(queen,pos(a,5),pos(h,5)).";
      String q6 = "move(queen,pos(d,2),pos(d,5)).";    	        
      String q7 = "move(queen,pos(a,4),pos(a,5)).";
      
      
       String q8 = ":- move(queen,pos(f,1),pos(b,3)).";
      String q9 = ":- move(queen,pos(f,1),pos(b,4)).";
      String q10 = ":- move(queen,pos(e,1),pos(b,8)).";
      
      String q11 = "move(queen,pos(A,4),pos(A,5)) "; //q7  
      String q12 = "move(queen,pos(A,C),pos(A,D)) "; //q7;q6;q11;q13 ---
      String q13 = "move(queen,pos(A,B),pos(A,B)) "; 
      String q14 = "move(queen,pos(A,1),pos(C,4)) ";//q1;q9      
 	    String q15 = "move(queen,pos(A,B),pos(C,D)) :- fdiff(A,C,3)";//q1;q3;q4;q10
 	    String q16 = "move(queen,pos(A,1),pos(C,4)) ";//q1;q9
 	    String q17 = "move(queen,pos(A,B),pos(C,B)) :- fdiff(A,C,1)";//
 	    String q18 = "move(queen,pos(A,B),pos(C,B)) :- rdiff(B,B,0), fdiff(A,C,5)";//q5
 	    String q19 = "move(queen,pos(A,B),pos(C,B)) ";//q4;q5;q13;q17;q18,q27  ---
 	    String q20 = "move(queen,pos(A,B),pos(C,D)) :- fdiff(A,C,2).";//q2
 	    String q21 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E).";//q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q15;q17;q20
 	    String q22 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E),fdiff(A,C,3).";//q1;q10
 	    String q23 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E),fdiff(A,C,E).";//q1;q2;q3; ---
 	    String q24 = "move(queen,pos(A,B),pos(C,D))"; //q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23;q24;q25;q26;q27;q28;q29;q30
      
 	    
 	    
 	    
 	    
 	    
 		String x1 = "move(king,pos(a,4),pos(a,5))."; 
        String x2 = "move(king,pos(d,5),pos(d,6)).";
        String x3 = "move(king,pos(b,5),pos(c,5)).";      	        
    	String x4 = "move(king,pos(d,3),pos(e,3)).";
        String x5 = "move(king,pos(d,4),pos(e,3)).";    	        
        String x6 = "move(king,pos(f,6),pos(g,7)).";    	        
        String x7 = "move(king,pos(b,2),pos(c,3)).";   
        String x8 = ":- move(king,pos(b,2),pos(d,3)).";
        String x9 = ":- move(king,pos(b,2),pos(c,4)).";
        String x10 = ":- move(king,pos(b,2),pos(b,4)).";
        String x11 = ":- move(king,pos(b,2),pos(d,2)).";
        
        String x12 = "move(king,pos(A,B),pos(C,B)) :- fdiff(A,C,2)."; //x11
        String x13 = "move(king,pos(A,B),pos(C,B)) :- fdiff(A,C,1)."; //x3;x4;
        String x14 = "move(king,pos(A,B),pos(C,D)) :- fdiff(A,C,1)."; //x3;x4;x5;x6;x7;x9;x13;x18       
        String x15 = "move(king,pos(A,B),pos(C,D)) :- rdiff(B,B,0), rdiff(B,D,1), rdiff(D,D,0).";//x1;x2;x5;x6;x7;x8
        String x16 = "move(king,pos(A,B),pos(C,D)) :- rdiff(B,D,0), fdiff(B,B,0).";//x3;x4;x11
        String x17 = "move(king,pos(A,B),pos(C,D)) :- rdiff(B,D,0), fdiff(A,C,0).";//x16
        String x18 = "move(king,pos(A,B),pos(C,D)) :- rdiff(B,D,1), fdiff(A,C,1).";//x5;x6,x7        
        String x19 = "move(king,pos(A,B),pos(C,D))";//x1;x2;x3;x4;x5;x6;x7;x8;x9;x10;x12;x13;x14;x15;x16;x17;x18;x20
        String x20 = "move(king,pos(A,B),pos(A,C)) :- rdiff(B,D,1).";//x1;x2
          	       
   

      
        ExampleC.add(0,new ExampleCov("r0","nada", 0, 0, 0, 0, 0, 0, "+"));
  		ExampleC.add(1,new ExampleCov("r1",r1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(2,new ExampleCov("r2",r2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(3,new ExampleCov("r3",r3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(4,new ExampleCov("r4",r4, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(5,new ExampleCov("r5",r5, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(6,new ExampleCov("r6",r6, 5, 0, 0, 0, 3, 0, "-"));
  		
  		
  		ExampleC.add(7,new ExampleCov("k1",k1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(8,new ExampleCov("k2",k2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(9,new ExampleCov("k3",k3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(10,new ExampleCov("k4",k4, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(11,new ExampleCov("k5",k5, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(12,new ExampleCov("k6",k6, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(13,new ExampleCov("k7",k7, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(14,new ExampleCov("k8",k8, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(15,new ExampleCov("k9",k9, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(16,new ExampleCov("k10",k10, 5, 0, 0, 0, 3, 0, "-"));
  
  		
  		ExampleC.add(17,new ExampleCov("b1",b1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(18,new ExampleCov("b2",b2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(19,new ExampleCov("b3",b3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(20,new ExampleCov("b4",b4, 5, 0, 0, 0, 3, 0, "-"));
  		
  		ExampleC.add(21,new ExampleCov("q1",q1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(22,new ExampleCov("q2",q2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(23,new ExampleCov("q3",q3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(24,new ExampleCov("q4",q4, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(25,new ExampleCov("q5",q5, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(26,new ExampleCov("q6",q6, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(27,new ExampleCov("q7",q7, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(28,new ExampleCov("q8",q8, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(29,new ExampleCov("q9",q9, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(30,new ExampleCov("q10",q10, 5, 0, 0, 0, 3, 0, "-"));
  		
  		ExampleC.add(31,new ExampleCov("x1",x1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(32,new ExampleCov("x2",x2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(33,new ExampleCov("x3",x3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(34,new ExampleCov("x4",x4, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(35,new ExampleCov("x5",x5, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(36,new ExampleCov("x6",x6, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(37,new ExampleCov("x7",x7, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(38,new ExampleCov("x8",x8, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(39,new ExampleCov("x9",x9, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(40,new ExampleCov("x10",x10, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(41,new ExampleCov("x11",x11, 5, 0, 0, 0, 3, 0, "-"));

  		
  		
/////////////////////////////////////////////////////////////////////////////
  		
  	   


  		RulesC.add(0,new RuleCov("r0","nada","", 0, 0, 0, 0, 0, 0));
  		RulesC.add(1,  new RuleCov("r7",r7,"r1",  3, 0, 2, 2, 3, 0));
  		RulesC.add(2,  new RuleCov("r8",r8,"r1",  3, 0, 2, 1, 3, 0));
  		RulesC.add(3,  new RuleCov("r9",r9,"r2",  3, 0, 2, 1, 3, 0));
  		RulesC.add(4,  new RuleCov("r10",r10,"r2",  4, 0, 1, 1, 3, 0));
  		RulesC.add(5,  new RuleCov("r11",r11,"r3",  3, 0, 2, 2, 3, 0));
  		RulesC.add(6,  new RuleCov("r12",r12,"r3",  3, 0, 1, 1, 3, 0));
  		RulesC.add(7,  new RuleCov("r13",r13,"r3",  3, 0, 1, 1, 3, 0));
  		RulesC.add(8,  new RuleCov("r14",r14,"r12;r7;r3;r1",  3, 0, 3, 3, 3, 0));
  		RulesC.add(9,  new RuleCov("r15",r15,"r13;r11;r4;r12;r3",  3, 0, 4, 3, 3, 0));
  		RulesC.add(10,  new RuleCov("r16",r16,"r1;r2;r9;r7;r8",  3, 0, 2, 4, 3, 0));
  		RulesC.add(11,  new RuleCov("r17",r17,"r1;r2;r3;r4;r5;r6;r7;r8;r9;r10;r11;r12;r13;r14;r15;r16",  3, 0, 4, 4, 3, 0));
        
  	  

		
		
		
    	RulesC.add(12,  new RuleCov("k11",k11,"k1",  3, 0, 2, 2, 3, 0));
    	RulesC.add(13,  new RuleCov("k12",k12,"k1;k3;k5;k7;k10;k13;k15;k21;k22",   2, 0, 6, 4, 4, 1));
    	RulesC.add(14,  new RuleCov("k13",k13,"k1;k3;k5;k7;k10;k15",  3, 0, 8, 4, 5, 2));
    	RulesC.add(15,  new RuleCov("k14",k14,"k1;k3;k5;k7;k8;k22;k23",   2, 0, 6, 4, 4, 1));
    	RulesC.add(16,  new RuleCov("k15",k15,"k1;k3;k5;k7;k10",   3, 0, 8, 4, 5, 2));
    	RulesC.add(17,  new RuleCov("k16",k16,"k2;k4;k6;k9;k18",   2, 0, 8, 4, 5, 2));
    	RulesC.add(18,  new RuleCov("k17",k17,"k1;k2;k3;k4;k5;k6;k7;k8;k9;k10;k14;k15;k16;k18;k19;k20;k21;k22;k23;k24;k25",   0, 0, 7, 5, 4, 1));
    	RulesC.add(19,  new RuleCov("k18",k18,"k2;k4;k6;k9",   4, 0, 10, 4, 6, 2));
    	RulesC.add(20,  new RuleCov("k19",k19,"k2;k4;k6;k10;k21;k24",   2, 0, 6, 4, 4, 1));
    	RulesC.add(21,  new RuleCov("k20",k20,"k1;k2;k3;k4;k5;k6;k7;k8;k9;k10",   2, 0, 6, 4, 4, 1));
    	RulesC.add(22,  new RuleCov("k21",k21,"k10",   2, 0, 8, 4, 5, 2));
    	RulesC.add(23,  new RuleCov("k22",k22,"k1;k3;k5;k7",   3, 0, 8, 4, 5, 2));
    	RulesC.add(24,  new RuleCov("k23",k23,"",   3, 0, 8, 4, 5, 2));
    	RulesC.add(25,  new RuleCov("k24",k24,"k2;k4;k6",  3, 0, 8, 4, 5, 2));
    	RulesC.add(26,  new RuleCov("k25",k25,"k1;k2;k3;k4;k5;k6;k7;k8;k9;k10;k13;k15;k16;k18;k20;k21;k22;k23;k24",   1, 0, 10, 6, 5, 2));

    	
    	

        
    	RulesC.add(27,  new RuleCov("b5",b5,"b2;b6;b7", 2, 0, 6, 4, 4, 1));
    	RulesC.add(28,  new RuleCov("b6",b6,"b2", 3, 0, 8, 4, 5, 2));
    	RulesC.add(29,  new RuleCov("b7",b7,"b2", 3, 0, 8, 4, 5, 2));
    	RulesC.add(30,  new RuleCov("b8",b8,"b1", 3, 0, 8, 4, 5, 2));
    	RulesC.add(31,  new RuleCov("b9",b9,"b2;b4;b6;b7", 2, 0, 6, 4, 4, 1));
    	RulesC.add(32,  new RuleCov("b10",b10,"b1;b2;b3;b6;b7;b8", 0, 0, 10, 5, 5, 2));
    	RulesC.add(33,  new RuleCov("b11",b11,"b1;b2;b3;b4" , 4, 1, 10, 4, 6, 2));
    	RulesC.add(34,  new RuleCov("b12",b12,"b1;b2;b3;b4;b6;b7;b8;b10;b13", 1, 0, 10, 6, 5, 2));
    	RulesC.add(35,  new RuleCov("b13",b13,"b1;b4;b8", 2, 0, 9, 5, 5, 2));


    
	       // util.createNewNodeDAG(f, "q25" , "q7;q6;q4;q5" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q25);
	      //  util.createNewNodeDAG(f, "q26" , "q7;q6" , 2, 0, 8, 3, 6, 0, false, false, 0, 0.0,q26);
	      //  util.createNewNodeDAG(f, "q27" , "q4;q5" , 2, 0, 8, 3, 6, 0, false, false, 0, 0.0,q27);
	      //  util.createNewNodeDAG(f, "q28" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q28);
	      //  util.createNewNodeDAG(f, "q29" , "q1;q2;q3" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q29);
	      //  util.createNewNodeDAG(f, "q30" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" ,  2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q30);
    	

        

      	RulesC.add(36,  new RuleCov("q11",q11,"q7", 3, 0, 2, 1, 3, 0));
      	RulesC.add(37,  new RuleCov("q12",q12,"q7;q6;q11;q13", 1, 0, 4, 3, 3, 0));
      	RulesC.add(38,  new RuleCov("q13",q13,"", 1, 0, 4, 2, 3, 0));
      	RulesC.add(39,  new RuleCov("q14",q14,"q1;q9", 2, 0, 2, 2, 3, 0));
      	RulesC.add(40,  new RuleCov("q15",q15,"q1;q3;q4;q10", 2, 0, 6, 4, 4, 1));
      	RulesC.add(41,  new RuleCov("q16",q16,"q1;q9", 3, 0, 2, 2, 3, 0));
      	RulesC.add(42,  new RuleCov("q17",q17,"", 2, 0, 6, 3, 4, 1));
      	RulesC.add(43,  new RuleCov("q18",q18,"q5", 3, 0, 8, 3, 5, 2));
      	RulesC.add(44,  new RuleCov("q19",q19,"q4;q5;q13;q17;q18", 1, 0, 4, 3, 3, 0));
      	RulesC.add(45,  new RuleCov("q20",q20,"q2", 2, 0, 6, 4, 4, 1));
      	RulesC.add(46,  new RuleCov("q21",q21,"q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q15;q17;q20", 1, 0, 7, 5, 4, 1));
      	RulesC.add(47,  new RuleCov("q22",q22,"q1;q10", 2, 0, 9, 5, 5, 2));
      	RulesC.add(48,  new RuleCov("q23",q23,"q1;q2;q3", 1, 0, 10, 5, 5, 2));
      	RulesC.add(49,  new RuleCov("q24",q24,"q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23", 1, 0, 4, 4, 3, 0));
   
	        

    
        RulesC.add(50,  new RuleCov("x12",x13,"x11", 2, 0, 6, 3, 4, 1));
        RulesC.add(51,  new RuleCov("x13",x13,"x3;x4", 2, 0, 6, 3, 4, 1));
        RulesC.add(52,  new RuleCov("x14",x14,"x3;x4;x5;x6;x7;x9;x13;x18", 2, 0, 6, 4, 4, 1));
        RulesC.add(53,  new RuleCov("x15",x15,"x1;x2;x5;x6;x7;x8", 4, 0, 10, 4, 6, 2));
        RulesC.add(54,  new RuleCov("x16",x16,"x3;x4;x11", 3, 0, 8, 4, 5, 2));
        RulesC.add(55,  new RuleCov("x17",x17,"x16", 3, 0, 8, 4, 5, 2));
        RulesC.add(56,  new RuleCov("x18",x18,"x5;x6;x7", 3, 0, 8, 4, 5, 2));
        RulesC.add(57,  new RuleCov("x19",x19,"x1;x2;x3;x4;x5;x6;x7;x8;x9;x10;x12;x13;x14;x15;x16;x17;x18;x20", 2, 0, 6, 3, 4, 1));
        RulesC.add(58,  new RuleCov("x20",x20,"x1;x2", 2, 0, 6, 3, 4, 1));
        
        
        
        	System.out.println("Step\tExa\tRul\tCon\tPop\tAvgOpt");
        	System.out.println("---------------------------------------------");
        	
        	
        
  }
  
  void statsICMLA_ini_cons(){
	  
	  
	  String r1 = "move(rook,pos(b,2),pos(e,2))."; 
      String r2 = "move(rook,pos(a,5),pos(h,5))";
      String r3 = "move(rook,pos(d,2),pos(d,5))";
      String r4 = "move(rook,pos(a,4),pos(a,5))";
      String r5 = ":- move(rook,pos(f,1),pos(b,3))";
      String r6 = ":- move(rook,pos(h,4),pos(a,2))";
      
      
      String r7 = "move(rook,pos(A,2),pos(B,2))."; //r1
      String r8 = "move(rook,pos(b,B),pos(e,B))."; //r1    	        
       String r9 = "move(rook,pos(a,B),pos(h,B))."; //r2
      String r10 = "move(rook,pos(a,A),pos(h,5))."; //r2  
      String r11 = "move(rook,pos(d,B),pos(d,C))."; //r3
      String r12 = "move(rook,pos(d,2),pos(d,C))."; //r3
      String r13 = "move(rook,pos(d,B),pos(d,5))."; //r3
      String r14 = "move(rook,pos(A,2),pos(C,D))."; //r11,r7,r3,r1,
      String r15 = "move(rook,pos(A,B),pos(A,C))."; //r11,r12,r4,r13,r3
      String r16 = "move(rook,pos(A,B),pos(C,B))."; //r1,r2,r7,r8,r9
      String r17 = "move(rook,pos(A,B),pos(C,D))."; //r1-r16
      
    
      
      

      String b1 = "move(bishop,pos(f,1),pos(c,4))."; 
      String b2 = "move(bishop,pos(c,4),pos(e,6)).";
      String b3 = "move(bishop,pos(e,6),pos(a,2)).";
      String b4 = ":- move(bishop,pos(f,1),pos(b,3)).";
      
      
      String b5 = "move(bishop,pos(A,B),pos(C,D)) :- fdiff(A,C,2)."; //b2;b6;b7   
      String b6 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,C,2).";//b2    	        
      String b7 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,2), fdiff(A,C,2).";//b2
      String b8 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,3), fdiff(A,C,3).";//b1
      String b9 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,2)";//b2;b4;b6;b7
      String b10 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,E).";//b1;b2;b3;b6;b7;b8
      String b11 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,B,0), rdiff(D,D,0), fdiff(A,A,0).";//b1;b2;b3;b4
      String b12 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,F).";//b1;b2;b3;b4;b6;b7;b8;b10;b13
      String b13 = "move(bishop,pos(A,B),pos(C,D)) :- rdiff(B,D,E), fdiff(A,C,3).";//b1;b4;b8
      
      
      
      String q1 = "move(queen,pos(f,1),pos(c,4))."; 
      String q2 = "move(queen,pos(c,4),pos(e,6)).";
      String q3 = "move(queen,pos(e,6),pos(a,2)).";
      String q4 = "move(queen,pos(b,2),pos(e,2))."; 
      String q5 = "move(queen,pos(a,5),pos(h,5)).";
      String q6 = "move(queen,pos(d,2),pos(d,5)).";    	        
      String q7 = "move(queen,pos(a,4),pos(a,5)).";
      
      
       String q8 = ":- move(queen,pos(f,1),pos(b,3)).";
      String q9 = ":- move(queen,pos(f,1),pos(b,4)).";
      String q10 = ":- move(queen,pos(e,1),pos(b,8)).";
      
      String q11 = "move(queen,pos(A,4),pos(A,5)) "; //q7  
      String q12 = "move(queen,pos(A,C),pos(A,D)) "; //q7;q6;q11;q13 ---
      String q13 = "move(queen,pos(A,B),pos(A,B)) "; 
      String q14 = "move(queen,pos(A,1),pos(C,4)) ";//q1;q9      
 	    String q15 = "move(queen,pos(A,B),pos(C,D)) :- fdiff(A,C,3)";//q1;q3;q4;q10
 	    String q16 = "move(queen,pos(A,1),pos(C,4)) ";//q1;q9
 	    String q17 = "move(queen,pos(A,B),pos(C,B)) :- fdiff(A,C,1)";//
 	    String q18 = "move(queen,pos(A,B),pos(C,B)) :- rdiff(B,B,0), fdiff(A,C,5)";//q5
 	    String q19 = "move(queen,pos(A,B),pos(C,B)) ";//q4;q5;q13;q17;q18,q27  ---
 	    String q20 = "move(queen,pos(A,B),pos(C,D)) :- fdiff(A,C,2).";//q2
 	    String q21 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E).";//q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q15;q17;q20
 	    String q22 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E),fdiff(A,C,3).";//q1;q10
 	    String q23 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E),fdiff(A,C,E).";//q1;q2;q3; ---
 	    String q24 = "move(queen,pos(A,B),pos(C,D))"; //q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23;q24;q25;q26;q27;q28;q29;q30
      
 	   String q25 = "move(queen,pos(A,B),pos(C,D)) :- move(rook,pos(A,B),pos(C,D))"; //q7;q6;q4;q5 ---
       String q26 = "move(queen,pos(A,B),pos(A,D)) :- move(rook,pos(A,B),pos(A,D))"; //q7;q6
       String q27 = "move(queen,pos(A,B),pos(C,B)) :- move(rook,pos(A,B),pos(C,B))"; //q4;q5
       String q28 = "move(queen,pos(A,B),pos(C,D)) :- move(rook,pos(A,C),pos(A,C))"; //q1;q2;q3;q6;q7;q5;q4;q9;q8;q10
      
       String q29 = "move(queen,pos(A,B),pos(C,D)) :- move(bishop,pos(A,B),pos(C,D))"; //q1;q2;q3 ---
       String q30 = "move(queen,pos(A,B),pos(C,D)) :- move(bishop,pos(B,D),pos(B,D))"; //q1;q2;q3;q6;q7;q5;q4;q9;q8;q10
 	    
 	    
 	    
 	    
 	

      
        ExampleC.add(0,new ExampleCov("r0","nada", 0, 0, 0, 0, 0, 0, "+"));
  		ExampleC.add(1,new ExampleCov("r1",r1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(2,new ExampleCov("r2",r2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(3,new ExampleCov("r3",r3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(4,new ExampleCov("r4",r4, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(5,new ExampleCov("r5",r5, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(6,new ExampleCov("r6",r6, 5, 0, 0, 0, 3, 0, "-"));
  		
  		
  	
  		
  		ExampleC.add(7,new ExampleCov("b1",b1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(8,new ExampleCov("b2",b2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(9,new ExampleCov("b3",b3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(10,new ExampleCov("b4",b4, 5, 0, 0, 0, 3, 0, "-"));
  		
  		ExampleC.add(11,new ExampleCov("q1",q1, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(12,new ExampleCov("q2",q2, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(13,new ExampleCov("q3",q3, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(14,new ExampleCov("q4",q4, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(15,new ExampleCov("q5",q5, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(16,new ExampleCov("q6",q6, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(17,new ExampleCov("q7",q7, 5, 0, 0, 0, 3, 0, "+"));
  		ExampleC.add(18,new ExampleCov("q8",q8, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(19,new ExampleCov("q9",q9, 5, 0, 0, 0, 3, 0, "-"));
  		ExampleC.add(20,new ExampleCov("q10",q10, 5, 0, 0, 0, 3, 0, "-"));
  		


  		
  		
/////////////////////////////////////////////////////////////////////////////
  		
  	   


  		RulesC.add(0,new RuleCov("r0","nada","", 0, 0, 0, 0, 0, 0));
  		RulesC.add(1,  new RuleCov("r7",r7,"r1",  3, 0, 2, 2, 3, 0));
  		RulesC.add(2,  new RuleCov("r8",r8,"r1",  3, 0, 2, 1, 3, 0));
  		RulesC.add(3,  new RuleCov("r9",r9,"r2",  3, 0, 2, 1, 3, 0));
  		RulesC.add(4,  new RuleCov("r10",r10,"r2",  4, 0, 1, 1, 3, 0));
  		RulesC.add(5,  new RuleCov("r11",r11,"r3",  3, 0, 2, 2, 3, 0));
  		RulesC.add(6,  new RuleCov("r12",r12,"r3",  3, 0, 1, 1, 3, 0));
  		RulesC.add(7,  new RuleCov("r13",r13,"r3",  3, 0, 1, 1, 3, 0));
  		RulesC.add(8,  new RuleCov("r14",r14,"r12;r7;r3;r1",  3, 0, 3, 3, 3, 0));
  		RulesC.add(9,  new RuleCov("r15",r15,"r13;r11;r4;r12;r3",  3, 0, 4, 3, 3, 0));
  		RulesC.add(10,  new RuleCov("r16",r16,"r1;r2;r9;r7;r8",  3, 0, 2, 4, 3, 0));
  		RulesC.add(11,  new RuleCov("r17",r17,"r1;r2;r3;r4;r5;r6;r7;r8;r9;r10;r11;r12;r13;r14;r15;r16",  3, 0, 4, 4, 3, 0));
        
  	  

		
	
    	
    	

        
    	RulesC.add(12,  new RuleCov("b5",b5,"b2;b6;b7", 2, 0, 6, 4, 4, 1));
    	RulesC.add(13,  new RuleCov("b6",b6,"b2", 3, 0, 8, 4, 5, 2));
    	RulesC.add(14,  new RuleCov("b7",b7,"b2", 3, 0, 8, 4, 5, 2));
    	RulesC.add(15,  new RuleCov("b8",b8,"b1", 3, 0, 8, 4, 5, 2));
    	RulesC.add(16,  new RuleCov("b9",b9,"b2;b4;b6;b7", 2, 0, 6, 4, 4, 1));
    	RulesC.add(17,  new RuleCov("b10",b10,"b1;b2;b3;b6;b7;b8", 0, 0, 10, 5, 5, 2));
    	RulesC.add(18,  new RuleCov("b11",b11,"b1;b2;b3;b4" , 4, 1, 10, 4, 6, 2));
    	RulesC.add(19,  new RuleCov("b12",b12,"b1;b2;b3;b4;b6;b7;b8;b10;b13", 1, 0, 10, 6, 5, 2));
    	RulesC.add(20,  new RuleCov("b13",b13,"b1;b4;b8", 2, 0, 9, 5, 5, 2));


    
	       // util.createNewNodeDAG(f, "q25" , "q7;q6;q4;q5" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q25);
	      //  util.createNewNodeDAG(f, "q26" , "q7;q6" , 2, 0, 8, 3, 6, 0, false, false, 0, 0.0,q26);
	      //  util.createNewNodeDAG(f, "q27" , "q4;q5" , 2, 0, 8, 3, 6, 0, false, false, 0, 0.0,q27);
	      //  util.createNewNodeDAG(f, "q28" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q28);
	      //  util.createNewNodeDAG(f, "q29" , "q1;q2;q3" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q29);
	      //  util.createNewNodeDAG(f, "q30" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" ,  2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q30);
    	

        

      	RulesC.add(21,  new RuleCov("q11",q11,"q7", 3, 0, 2, 1, 3, 0));
      	RulesC.add(22,  new RuleCov("q12",q12,"q7;q6;q11;q13", 1, 0, 4, 3, 3, 0));
      	RulesC.add(23,  new RuleCov("q13",q13,"", 1, 0, 4, 2, 3, 0));
      	RulesC.add(24,  new RuleCov("q14",q14,"q1;q9", 2, 0, 2, 2, 3, 0));
      	RulesC.add(25,  new RuleCov("q15",q15,"q1;q3;q4;q10", 2, 0, 6, 4, 4, 1));
      	RulesC.add(26,  new RuleCov("q16",q16,"q1;q9", 3, 0, 2, 2, 3, 0));
      	RulesC.add(27,  new RuleCov("q17",q17,"", 2, 0, 6, 3, 4, 1));
      	RulesC.add(28,  new RuleCov("q18",q18,"q5", 3, 0, 8, 3, 5, 2));
      	RulesC.add(29,  new RuleCov("q19",q19,"q4;q5;q13;q17;q18", 1, 0, 4, 3, 3, 0));
      	RulesC.add(30,  new RuleCov("q20",q20,"q2", 2, 0, 6, 4, 4, 1));
      	RulesC.add(31,  new RuleCov("q21",q21,"q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q15;q17;q20", 1, 0, 7, 5, 4, 1));
      	RulesC.add(32,  new RuleCov("q22",q22,"q1;q10", 2, 0, 9, 5, 5, 2));
      	RulesC.add(33,  new RuleCov("q23",q23,"q1;q2;q3", 1, 0, 10, 5, 5, 2));
      	RulesC.add(34,  new RuleCov("q24",q24,"q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23", 1, 0, 4, 4, 3, 0));
  	
     
    
      	RulesC.add(35,  new RuleCov("q25",q25,"q7;q6;q4;q5",  2, 0, 8, 4, 6, 0));
    	RulesC.add(36,  new RuleCov("q26",q26,"q7;q6", 2, 0, 8, 3, 6, 0));
    	RulesC.add(37,  new RuleCov("q27",q27,"q4;q5", 2, 0, 8, 3, 6, 0));
    	RulesC.add(38,  new RuleCov("q28",q28,"q1;q2;q3;q6;q7;q5;q4;q9;q8;q10", 2, 0, 8, 4, 6, 0));
    	RulesC.add(39,  new RuleCov("q29",q29,"q1;q2;q3", 2, 0, 8, 4, 6, 0));
    	RulesC.add(40,  new RuleCov("q30",q30,"q1;q2;q3;q6;q7;q5;q4;q9;q8;q10", 2, 0, 8, 4, 6, 0));


    
       
        
        	System.out.println("Step\tExa\tRul\tCon\tPop\tAvgOpt\tAvfCons");
        	System.out.println("---------------------------------------------");
        	
        	
        
  }
  
  public ArrayList<Integer> RanE = new ArrayList<Integer>();
  public ArrayList<Integer> RanR = new ArrayList<Integer>();
  
  int indexE = 0;
  int indexR = 0;
  
  void iniIndexes(){
	  indexE = 0;
	  indexR = 0;
  }
 
  void statsICMLA_run(ListenableDirectedAcyclic<String, DefaultWeightedEdge> LDAG, DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> DAG,
		  int step, ArrayList<Integer> arrE,ArrayList<Integer> arrR, int maxE, int maxR, boolean ob, int stack,int Per){
	
	  int countR = 0;
	  int countE = 0;
	
	  ExampleCov TempE;
	  RuleCov TempR;
	  int exampleid; int ruleid;
 
	  //EVIDENCE
	  while (StdRandom.bernoulli(.5) && indexE<maxE) {
			  
			  exampleid = arrE.get(indexE); //System.out.println("indexE: "+ exampleid+"   ");
			  indexE++;

			  TempE= ExampleC.get(exampleid);	
			  
			  if (this.getVertex(DAG, TempE.id)==null){
					
				  createNewNodeDAG(DAG, TempE.id , "" ,  TempE.C, TempE.NC, TempE.V, TempE.NV, TempE.F, TempE.NF, false, true, TempE.clase, TempE.rule);
				  LDAG.addVertex(TempE.id);
				
			  } 
			  countE++;
	  }
		
	  //RULES
	  while (StdRandom.bernoulli(.5) && indexR<maxR) {
				 
			   ruleid =arrR.get(indexR);
			   indexR++;
			  
			   TempR= RulesC.get(ruleid);
			   countR++;
			 			
						 	
			   if (this.getVertex(DAG, TempR.id)==null){
				   
				   createNewNodeDAG(DAG, TempR.id , TempR.cov ,  TempR.C, TempR.NC, TempR.V, TempR.NV, TempR.F, TempR.NF, false, false, "",TempR.rule);
				   LDAG.addVertex(TempR.id);
				   this.addListEdge(LDAG, TempR.id, TempR.cov);
				   this.addListEdge(DAG, TempR.id, TempR.cov);

			   }
		  }
				  
	  this.statsICMLA_update(LDAG,DAG);//actualizo las aristas a los nodos cubiertos (pueden haber aparecido nodos nuevos que son cubiertos)
	  connectionsBellman(LDAG,DAG);
	  updateDAGdescriptors(DAG);
	  
	  int numRules = DAG.vertexSet().size();
	  //System.out.println("Step\tExamples\tRules\tTotalC\tTotalR\tAvgOpt");
	  double[] array = this.totalAVGOptCons(DAG);
	  System.out.println(step+"\t"+countE+"\t"+countR+"\t"+this.totalCons(DAG)+"\t"+numRules+"\t"+array[0]+"\t"+array[1]);
		  
	  if (ob && (numRules > stack)){ 
			  //System.out.println("OLVIDO");
		  float p= ((float)numRules/100)*Per;
		  this.oblivionIter(LDAG, DAG, (int)p);		  
	  }
	            
        
	  //this.printGraph(f);
	  
      	
  } 
 
  
  
   void statsICMLA_runR(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f,
		   int step, boolean ob, int stack, int Per, int minE, int maxE, int minR, int maxR){
	  
	 
	  int countR = 0;
	  int countE = 0;
	  int lim = 50;
	  ExampleCov TempE;
	  RuleCov TempR;
	  int exampleid; int ruleid;
	  
	  //EXAMPLES
	  while (StdRandom.bernoulli(.5)) {
		  
	     	  //añado nueva regla
			  exampleid =StdRandom.uniform(minE,maxE);
			  //Tengo los ejemplos almacenados en un vector
			  TempE= ExampleC.get(exampleid);		
			  if (this.getVertex(f, TempE.id)==null){
					
				  createNewNodeDAG(f, TempE.id , "" ,  TempE.C, TempE.NC, TempE.V, TempE.NV, TempE.F, TempE.NF, false, true, TempE.clase,TempE.rule);
				  g.addVertex(TempE.id);
					 
					  
					  } 
			  countE++;
	  }
		
	  //RULES
	  while (StdRandom.bernoulli(.5)) {
				 
			   ruleid =StdRandom.uniform(minR,maxR);
			  
			   TempR= RulesC.get(ruleid);
			   countR++;
			 			
						 	
			   if (this.getVertex(f, TempR.id)==null){
				   
				   createNewNodeDAG(f, TempR.id , TempR.cov ,  TempR.C, TempR.NC, TempR.V, TempR.NV, TempR.F, TempR.NF, false, false, "",TempR.rule);
				   g.addVertex(TempR.id);
				   this.addListEdge(g, TempR.id, TempR.cov);
				   this.addListEdge(f, TempR.id, TempR.cov);

			   }
	  }
				  

	  // añado las conexiones entre nodos		  
	  this.statsICMLA_update(g,f);
	  connectionsBellman(g,f);
	  updateDAGdescriptors(f);
	  
	  int numRules = f.vertexSet().size();
	  //System.out.println("Step\tExamples\tRules\tTotalC\tTotalR\tAvgOpt");
	  
	  double[] array = this.totalAVGOptCons(f);
	  
	 // System.out.println(step+"\t"+countE+"\t"+countR+"\t"+this.totalCons(f)+"\t"+numRules+"\t"+array[0]+"\t"+array[1]);
	     
	  
	  //escritura fichero
	    FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("C:/Users/Nando/Documents/Remplazo.txt",true);
            pw = new PrintWriter(fichero);
 
            
            pw.println(step+"\t"+countE+"\t"+countR+"\t"+this.totalCons(f)+"\t"+numRules+"\t"+array[0]+"\t"+array[1]);
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	  
	  //this.printGraph(f);
	  if (ob && (numRules > stack)){ 
		 //System.out.println("OLVIDO");
		  float p= ((float)numRules/100)*Per;
		  this.oblivionIter(g, f, (int)p);
		  
	  }
      	
  } 
  
   
   void  statsICMLA_update(ListenableDirectedAcyclic<String, DefaultWeightedEdge> g,DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f){
	   
	   
	   Set<Nodes> vertices = new HashSet<Nodes>();
	    vertices.addAll(f.vertexSet()); //vertices en mi grafo de cobertura 
	    //System.out.println("opt: " + option+ " value: "+value );
    
   	    for (Nodes vertexDAG : vertices) {
    	    	
   	    	Iterator<RuleCov> it = RulesC.iterator();
   	    	RuleCov  TempR;
   	    	while(it.hasNext()){
   	    		
   	    		TempR = it.next();
   	    		if(TempR.id.equals(vertexDAG.id)){
   	    			this.addListEdge(f, vertexDAG.id, TempR.cov);
   	    			this.addListEdge(g, vertexDAG.id, TempR.cov);

   	    		}
   	    		
   	    	}
   	    	
   	    }
	   
   }
   
   
   
   

  public static int randInt(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	  
  public static double randBern(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    

	    return Math.random();
	}
}

