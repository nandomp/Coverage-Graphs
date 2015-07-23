/* This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */


package MyGraphPack;

import com.mxgraph.swing.*;

import java.awt.*;

import javax.swing.*;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


public class JGraphXAdapterDemo
    extends JApplet
{
	public JGraphXAdapterDemo() {
		
		
		
	}


    private static final long serialVersionUID = 2202072534703043194L;
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 600);
    	
    // Acyclic directed graph to DRAW
    private ListenableDirectedAcyclic<String, DefaultWeightedEdge> g =
            new ListenableDirectedAcyclic<String, DefaultWeightedEdge>(
            		DefaultWeightedEdge.class);
    	
    // Acyclic directed graph
    private	DirectedAcyclicGraph<Nodes, DefaultWeightedEdge> f =
                new DirectedAcyclicGraph<Nodes, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    private JGraphXAdapter<String, DefaultWeightedEdge> jgxAdapter;

    
    // My graph functions
    private MyGraphUtil util = new MyGraphUtil();
    
    //Statistics
    JGraphXAdapterDemo2 frameStatistics = new JGraphXAdapterDemo2();
    
    private boolean circle=true;
    
    private boolean connected=true;
    private JMenuItem mntmConnect;
    private JButton btnStats;
    
    private boolean stats=false;
    
    private int chess=0;
  
    
    public static void main(String [] args)
    {
        JGraphAdapterDemo applet = new JGraphAdapterDemo();
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("Grapht");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        
    
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
       
    
    
    	// create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<String, DefaultWeightedEdge>(g);
        
        mxGraphComponent mxGraphComponent_ = new mxGraphComponent(jgxAdapter);
        mxGraphComponent_.setGridColor(Color.RED);
        getContentPane().add(mxGraphComponent_);
        
        
      
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        
        
        JButton btnNewButton = new JButton("New Edge");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String [] array ={"","",""};
        		JTextField v1 = new JTextField();
        		 JTextField v2 = new JTextField();
        		 JTextField other = new JTextField();
        		 
        		 Object[] message = {
        		 "v1:", v1,
        		 "v2:", v2,
        		 "other:", other 		 
        		 
        		 };
        		 
        		 
        		 int option = JOptionPane.showConfirmDialog(null, message, "Rellena el formulario", JOptionPane.OK_CANCEL_OPTION);
        		 if(option == JOptionPane.OK_OPTION){
        			

        			util.addEdge(g, f,  v1.getText(), v2.getText());
        		    
        			util.updateDAGdescriptors(f);
        			
        			util.printGraph(f);
	            
	            //positioning via jgraphx layouts
	            
	            util.layoutGraph2draw(jgxAdapter,circle);
	              
	     		 
        		 } 
        		 
        	}
        		
        	
        });
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        //Botón para crear nuevo nodo     
        JButton btnNewNode = new JButton("New Node");
        panel.add(btnNewNode);
        btnNewNode.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String [] array ={"","","","","","","","","",""};
        		JTextField id = new JTextField();
        		 JTextField cov = new JTextField();
        		 JTextField ground = new JTextField();
        		 JTextField cert = new JTextField();
        		 JTextField usab = new JTextField();
        		 JTextField numC = new JTextField();
        		 JTextField numV = new JTextField();
        		 JTextField numF = new JTextField();
        		 JTextField isRec = new JTextField();
        		 JTextField Rule = new JTextField();
        		 Object[] message = {
        		 "ID:", id,
        		 "Edges:", cov,
        		 "Is ground:", ground,
        		 "Certaintly:", cert,
        		 "Usability:", usab,
        		 "numC:", numC,
        		 "numV:", numV,
        		 "numF:", numF,
        		 "isRec:", isRec,   
        		 "Rule:", Rule
        		 
        		 };
        		 
        		 
        		 int option = JOptionPane.showConfirmDialog(null, message, "Rellena el formulario", JOptionPane.OK_CANCEL_OPTION);
        		 if(option == JOptionPane.OK_OPTION){
        			
        		 array[0]=id.getText();
        		 array[1]=cov.getText();
        		 array[2]=ground.getText();
        		 array[3]=cert.getText();
        		 array[4]=usab.getText();
        		 array[5]=numC.getText();
        		 array[6]=numV.getText();
        		 array[7]=numF.getText();
        		 array[8]=isRec.getText();
        		 array[9]=Rule.getText();
        		 
        		 //Generamos nodos nuevos       		 
        		 fillTheGraphsNodes(array);
        		
        		// createNewNodeDAG(f, String id, String edges, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)                 
	            	            
	            //util.printGraph(f);
	            
	            //positioning via jgraphx layouts
        		
        		util.updateDAGdescriptors(f);
	            
	            util.layoutGraph2draw(jgxAdapter,circle);
	              
	     		 
        		 } 
        		 
        	}
        });
        
        JButton btnDeleteNode = new JButton("Delete Node");
        btnDeleteNode.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String [] array ={""};
        		JTextField id = new JTextField();
        		
        		 Object[] message = {
        		 "ID:", id 		       		 
        		 
        		 };
        		 
        		 
        		 int option = JOptionPane.showConfirmDialog(null, message, "Rellena el formulario", JOptionPane.OK_CANCEL_OPTION);
        		 if(option == JOptionPane.OK_OPTION){
        			
        			 
        		 //g.removeVertex(id.getText());
        		// Nodes remove = util.getVertex(f, id.getText());
        		 //f.removeVertex(remove);
        		 
        		 util.deleteVertex(g, f, id.getText());
         		 
        		 util.updateDAGdescriptors(f);
                   	            
        		 util.printGraph(f);
	            
	            //positioning via jgraphx layouts
	            
        		 util.layoutGraph2draw(jgxAdapter,circle);
	              
        		
        		
        		 }}
        });
        panel.add(btnDeleteNode);
        panel.add(btnNewButton);
        
        JButton btnDeleteEdge = new JButton("Delete Edge");
        btnDeleteEdge.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		String [] array ={"","",""};
        		JTextField v1 = new JTextField();
        		 JTextField v2 = new JTextField();
        		 JTextField other = new JTextField();
        		 
        		 Object[] message = {
        		 "v1:", v1,
        		 "v2:", v2,
        		 "other:", other 		 
        		 
        		 };
        		 
        		 
        		 int option = JOptionPane.showConfirmDialog(null, message, "Rellena el formulario", JOptionPane.OK_CANCEL_OPTION);
        		 if(option == JOptionPane.OK_OPTION){
        			

        			util.deleteEdge(g, f, v1.getText(), v2.getText());
        		    
        			util.updateDAGdescriptors(f);
        			
        			util.printGraph(f);
	            
	            //positioning via jgraphx layouts
	            
	            util.layoutGraph2draw(jgxAdapter,circle);
	              
	     		 
        		 } 
        		
        	}
        });
        panel.add(btnDeleteEdge);
        
        JButton btnOblivion = new JButton("Oblivion");
        btnOblivion.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
       		 util.oblivionIter(g, f, 1);
       		 util.updateDAGdescriptors(f);
       		 frameStatistics.updateTable(f,util.classList);
       		 
        	}
        });
        panel.add(btnOblivion);
        
        boolean primero = false;
      
        
        JButton button = new JButton("+");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.zoom(jgxAdapter, true);
        	}
        });
        panel.add(button);
        
            
        
        JButton button_1 = new JButton("-");
        button_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		util.zoom(jgxAdapter, false);
        	}
        });
        panel.add(button_1);
        
        btnStats = new JButton("Stats");
        btnStats.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.updateDAGdescriptors(f);
        		
        		if (!stats){        			
        			frameStatistics.setVisible(true);
        			frameStatistics.fillTable(f,util.classList);
    				btnStats.setText("Update");
    				stats=true;
        		}else{
        			frameStatistics.updateTable(f,util.classList);
        			
        		}
        		
        	}
        });
        
        
        
        panel.add(btnStats);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu mnMenu = new JMenu("Actions");
        menuBar.add(mnMenu);
        
        JMenuItem mntmDeleteGraph = new JMenuItem("Delete Graph");
        mntmDeleteGraph.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.eraseDAG(f);
        		util.eraseLG(g);  
        		
        		
        	}
        });
        
        mntmConnect = new JMenuItem("Bellman");
        mntmConnect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		if (connected){
        			util.connectionsBellman(g, f);
            		util.updateDAGdescriptors(f);
            		util.printGraph(f);
            		mntmConnect.setText("All Connections");
            		connected = false;
        		}else{
        			util.connectionsAllpaths(g, f);
        			mntmConnect.setText("Bellman");
        			util.updateDAGdescriptors(f);
        			util.printGraph(f);
            		connected = true;
        		}
        		
        		
        		
        	}
        });
        mnMenu.add(mntmConnect);
        mnMenu.add(mntmDeleteGraph);
        
               
        
        
        JMenuItem mntmPrintDag = new JMenuItem("Print");
        mntmPrintDag.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.printGraph(f);
        	}
        });
        mnMenu.add(mntmPrintDag);
        
       
        
        
        
        
        
        
        JMenuItem mntmOblivionIt = new JMenuItem("Oblivion Iter");
        mntmOblivionIt.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String [] array ={""};
        		JTextField val = new JTextField();
        		 
        		
        		 
        		 Object[] message = {
        				 "#iter:", val,
 
        		 };
        		 
        		        	
        		 int option = JOptionPane.showConfirmDialog(null, message, "Rellena el formulario", JOptionPane.OK_CANCEL_OPTION);
        		 
        		 if(option == JOptionPane.OK_OPTION){
        			
        		
        			 util.oblivionIter(g, f, Integer.parseInt(val.getText()));
        			 
        			 
        			
     
	      			
        		util.layoutGraph2draw(jgxAdapter,circle);
        		util.printGraph(f);
        		util.updateDAGdescriptors(f);
        		util.printGraph(f);
        		
        		 }
        		
        		
        	}
        });
        mnMenu.add(mntmOblivionIt);
        
        JMenuItem mntmUpdate = new JMenuItem("Update");
        mntmUpdate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.updateDAGdescriptors(f);
        	}
        });
        mnMenu.add(mntmUpdate);
        
        JMenu mnNewMenu = new JMenu("View");
        menuBar.add(mnNewMenu);
        

        
        JMenuItem mntmNewMenuItem_2 = new JMenuItem("Id");
        mntmNewMenuItem_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.replaceAllVertex(g,f,1,"");
        		
        		util.layoutGraph2draw(jgxAdapter, circle);
        		
        	}
        });
        
        JMenuItem mntmCircle = new JMenuItem("Circle");
        mnNewMenu.add(mntmCircle);
        mntmCircle.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	
        		util.layoutGraph2draw(jgxAdapter,true);
        		circle= true;
        	
        	}
        });
        
        JMenuItem mntmTree = new JMenuItem("Tree");
        mnNewMenu.add(mntmTree);
        mntmTree.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.layoutGraph2draw(jgxAdapter,false);
        		circle=false;
        	}
        });
        mnNewMenu.add(mntmNewMenuItem_2);
      
        
        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Optimality");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.replaceAllVertex(g,f,2,"");
        		
        		util.layoutGraph2draw(jgxAdapter, circle);
        	}
        });
        mnNewMenu.add(mntmNewMenuItem_1);
        
        JMenuItem mntmSupport = new JMenuItem("Support");
        mntmSupport.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.replaceAllVertex(g,f,3,"+");
        		
        		util.layoutGraph2draw(jgxAdapter, circle);
        		
        	}
        });
        
        JMenuItem mntmOblivion = new JMenuItem("Permanence");
        mntmOblivion.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.replaceAllVertex(g,f,4,"");
        		
        		util.layoutGraph2draw(jgxAdapter, circle);
        	}
        });
        mnNewMenu.add(mntmOblivion);
        mnNewMenu.add(mntmSupport);
        
        
        JMenu mnExamples = new JMenu("Examples");
        menuBar.add(mnExamples);
        
        JMenuItem mntmFamily = new JMenuItem("Family");
        mntmFamily.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String r1 = "daughter(mary,ann)"; 
    	        String r2 = "daughter(eve,tom)";
    	        String r3 = "daughter(tom,ann)";
    	        String r4 = "daughter(eve,ann)";
    	        
    	        String r20 = "daughter(eve,tom)<-female(eve)";
    	        String r35 = "daughter(eve,Y)<-female(eve)";
    	        String r58 = "daughter(mary,ann)<-female(mary),parent(ann,mary)";
    	        String r59 = "daughter(eve,tom)<-female(eve),parent(tom,eve)";
    	        
    	        String r73 = "daughter(X,tom)<-female(X),parent(tom,X)";
    	        String r100 = "daughter(X,Y)<-female(Y),parent(Y,mary)";
    	    	 
    	        String r5 = "daughter(cris,tom)";
    	        
    	        String r110 = "daughter(X,Y)<-female(X),parent(Y,X)";
    	        
    	        String r138 = "daughter(V,W)<-female(X),parent(Y,Z)";
    	        
    	        String r6 = "son(josed,ann)"; 
    	        String r7 = "son(john,josef)";
    	        String r8 = "son(tom,eve)";
    	        
      	        String r130 = "son(X,Y)<-male(X)";
    	        String r143 = "son(josef,Y)<-male(X),parent(Y,josef)";
    	        String r161 = "son(X,Y)<-male(Y),parent(Y,X)";
    	        String r166 = "son(X,eve)<-male(X)";
    	        
    	        
    	      /* String r12 = "r12";
    	        String r13 = "r13";
    	        String r14 = "r14";
    	        String r15 = "r15";
    	        String r16 = "r16";
    	        String r17 = "r17";*/
    	        
    	         
    	        

    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("r1");
    	        g.addVertex("r2");
    	        g.addVertex("r3");
    	        g.addVertex("r4");
    	        g.addVertex("r5");
    	    //   g.addVertex("r6");
    	     //   g.addVertex("r7");
    	     //   g.addVertex("r8");
    	        g.addVertex("r20");
    	        g.addVertex("r35");
    	  //      g.addVertex("r58");
    	        g.addVertex("r59");
    	        g.addVertex("r73");
    	        g.addVertex("r100");
    	        g.addVertex("r110");
    	        g.addVertex("r138");
    	      //  g.addVertex("r130");
    	      //  g.addVertex("r143");
    	       // g.addVertex("r161");
    	       // g.addVertex("r166");
    	        
    	        g.addEdge("r20", "r2");
    	        g.addEdge("r35", "r20");
    	        g.addEdge("r35", "r4");
    	      //  g.addEdge("r58", "r1");
    	        g.addEdge("r59", "r2");
    	        g.addEdge("r20", "r2");
    	        g.addEdge("r73", "r59");
    	        g.addEdge("r100", "r1");
    	        g.addEdge("r100", "r3");
    	        g.addEdge("r100", "r4");
    	        g.addEdge("r73", "r5");
    	        g.addEdge("r110", "r73");
    	        g.addEdge("r110", "r1");
    	        g.addEdge("r138", "r110");
    	        g.addEdge("r138", "r100");
    	        
    	     //   g.addEdge("r130", "r6");
    	     //   g.addEdge("r130", "r7");
    	     //   g.addEdge("r130", "r8");
    	     //   g.addEdge("r143", "r6");
    	     //   g.addEdge("r161", "r143");
    	     //   g.addEdge("r161", "r7");
    	     //   g.addEdge("r166", "r8");
    	        
    	        
    	        
    	        util.updateClasses("+;-");
    	        util.updateMMLvalues(6, 1, 0);
    	      
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	        util.createNewNodeDAG(f, "r1" , "" , 6, 0, 0, 0, 1, 0, false, true, "+",r1);
    	        util.createNewNodeDAG(f, "r2" , "" , 6, 0, 0, 0, 1, 0, false, true, "+",r2);
    	        util.createNewNodeDAG(f, "r3" , "" ,  6, 0, 0, 0, 1, 0,false, true, "-",r3);
    	        util.createNewNodeDAG(f, "r4" , "" ,  6, 0, 0, 0, 1, 0, false, true, "-",r4);
    	        util.createNewNodeDAG(f, "r5" , "" ,  6, 0, 0, 0, 1, 0, false, true, "+",r5);
    	       // util.createNewNodeDAG(f, "r6" , "" ,  6, 0, 0, 0, 1, 0, false, true, 0, 1.0,r6);
    	       // util.createNewNodeDAG(f, "r7" , "" ,  6, 0, 0, 0, 1, 0,false, true, 0, 1.0,r7);
    	       // util.createNewNodeDAG(f, "r8" , "" , 6, 0, 0, 0, 1, 0,false, true, 0, -1.0,r8);
    	        
    	        
    	        util.createNewNodeDAG(f, "r100" , "r1;r3;r4" ,  1, 0, 4, 2, 3, 2,false, false, "",r100);
    	       // util.createNewNodeDAG(f, "r58" , "r1" , 5, 0, 0, 0, 3, 2,false, false, 0, 0.0,r58);
    	        util.createNewNodeDAG(f, "r59" , "r2" ,  5, 0, 0, 0, 3, 2,false, false, "",r59);
    	        util.createNewNodeDAG(f, "r20" , "r2" ,  3, 0, 0, 0, 2, 1,false, false, "",r20);
    	        util.createNewNodeDAG(f, "r35" , "r4;r20" ,  2, 0, 1, 1, 2, 1,false, false, "",r35);
    	        util.createNewNodeDAG(f, "r73" , "r59;r5" ,  2, 0, 3, 1, 3, 2,false, false, "",r73);
    	        util.createNewNodeDAG(f, "r110" , "r73;r1" ,  0, 0, 5, 2, 3, 2,false, false, "",r110);
    	        util.createNewNodeDAG(f, "r138" , "r110;r100" ,  0, 0, 5, 5, 3, 2,false, false, "",r138);
    	        
    	      // util.createNewNodeDAG(f, "r130" , "r6;r7;r8" ,  0, 0, 3, 2, 2, 1, false, false, 0, 0.0,r130);
    	      //  util.createNewNodeDAG(f, "r143" , "r6" ,  0, 0, 5, 2, 3, 2, false, false, 0, 0.0,r143);
    	      //  util.createNewNodeDAG(f, "r161" , "r143;r7" ,  0, 0, 5, 2, 3, 1, false, false, 0, 0.0,r161);
    	      //  util.createNewNodeDAG(f, "r166" , "r8" ,  1, 0, 2, 1, 2, 1, false, false, 0, 0.0,r166);
    	        
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("All Connections");
        		connected = false;
        	}
        });
        
        JMenuItem mntmToy = new JMenuItem("Toy");
        mntmToy.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
    		 	String r1 = "r1"; 
    	        String r2 = "r2";
    	        String r3 = "r3";
    	        String r4 = "r4";
    	        String r5 = "r5";
    	        String r6 = "r6"; 
    	        String r7 = "r7";
    	        String r8 = "r8";
    	        String r9 = "r9";
    	        String r10 = "r10";
    	        String r11 = "r11";
    	        
    	      /* String r12 = "r12";
    	        String r13 = "r13";
    	        String r14 = "r14";
    	        String r15 = "r15";
    	        String r16 = "r16";
    	        String r17 = "r17";*/
    	        
    	         
    	        

    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex(r1);
    	        g.addVertex(r2);
    	        g.addVertex(r3);
    	        g.addVertex(r4);
    	        g.addVertex(r5);
    	        g.addVertex(r6);
    	        g.addVertex(r7);
    	        g.addVertex(r8);
    	        g.addVertex(r9);
    	        g.addVertex(r10);
    	        g.addVertex(r11);
    	        
    	       /*g.addVertex(r12);
    	        g.addVertex(r13);
    	        g.addVertex(r14);
    	        g.addVertex(r15);
    	        g.addVertex(r16);
    	        g.addVertex(r17);*/
    	        
    	        
    	       


    	        g.addEdge(r5, r1);
    	        g.addEdge(r5, r2);
    	        g.addEdge(r6, r2);
    	        g.addEdge(r6, r3);
    	        g.addEdge(r7, r3);
    	        g.addEdge(r7, r4);
    	        g.addEdge(r8, r5);
    	        g.addEdge(r8, r6);
    	        g.addEdge(r9, r6);
    	        g.addEdge(r9, r7);
    	        g.addEdge(r10, r8);
    	        g.addEdge(r10, r9);
    	        g.addEdge(r11, r8);
    	        g.addEdge(r11, r9);
    	        
    	       /* g.addEdge(r12, r1);
    	        g.addEdge(r13, r1);
    	        g.addEdge(r14, r1);
    	        g.addEdge(r15, r1);
    	        g.addEdge(r16, r1);
    	        g.addEdge(r17, r12);
    	        g.addEdge(r17, r13);
    	        g.addEdge(r17, r14);
    	        g.addEdge(r17, r15);
    	        g.addEdge(r17, r16);
    	        g.addEdge(r8, r17);*/
    	        
    	        
    	        
    	        
    	        
    	      
    	        
    	      
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	        util.createNewNodeDAG(f, "r1" , "" , 0, 0, 0, 0, 0, 0, false, true, "+","");
    	        util.createNewNodeDAG(f, "r2" , "" , 0, 0, 0, 0, 0, 0, false, true, "+","");
    	        util.createNewNodeDAG(f, "r3" , "" ,  0, 0, 0,  0, 0, 0, false, true, "-","");
    	        util.createNewNodeDAG(f, "r4" , "" ,  0, 0, 0, 0, 0, 0, false, true, "-","");
    	        util.createNewNodeDAG(f, "r5" , "r1;r2" ,  0, 0, 0, 0, 0, 0, false, false, "","");
    	        util.createNewNodeDAG(f, "r6" , "r2;r3" ,  0, 0, 0, 0, 0, 0, false, false, "","");
    	        util.createNewNodeDAG(f, "r7" , "r3;r4" ,  0, 0, 0, 0, 0, 0, false, false, "","");
    	        
    	        util.createNewNodeDAG(f, "r8" , "r5;r6" ,  0, 0, 0, 0, 0, 0, false, false, "","");//r17
    	        util.createNewNodeDAG(f, "r9" , "r6;r7" ,  0, 0, 0, 0, 0, 0, false, false, "","");
    	        
    	        util.createNewNodeDAG(f, "r10" , "r8;r9" ,  0, 0, 0, 0, 0, 0, false, false, "","");
    	        util.createNewNodeDAG(f, "r11" , "r8;r9" ,  0, 0, 0, 0, 0, 0, false, false, "","");
    	        
    	        
    	       /* util.createNewNodeDAG(f, "r12" , "r1" ,  0, 0, 0, false, false, 0, 0.0,"");
    	        util.createNewNodeDAG(f, "r13" , "r1" ,  0, 0, 0, false, false, 0, 0.0,"");
    	        util.createNewNodeDAG(f, "r14" , "r1" ,  0, 0, 0, false, false, 0, 0.0,"");
    	        util.createNewNodeDAG(f, "r15" , "r1" ,  0, 0, 0, false, false, 0, 0.0,"");
    	        util.createNewNodeDAG(f, "r16" , "r1" ,  0, 0, 0, false, false, 0, 0.0,"");
    	        util.createNewNodeDAG(f, "r17" , "r12,r13,r14,r15,r16" ,  0, 0, 0, false, false, 0, 0.0,"");
    	        
    	       */ 
    	        
    	
    	        util.updateDAGdescriptors(f);
    	        
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("All Connections");
        		connected = false;
    	        //util.printGraph(f);
        	}
        });
        mnExamples.add(mntmToy);
        mnExamples.add(mntmFamily);
        
        JMenuItem mntmChess = new JMenuItem("Chess Rook");
        mntmChess.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.updateClasses("+;-");
        		util.updateMMLvalues(21, 2,  0);
        		// ROOK
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
    	        
    	        
    
    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("r1");
    	        g.addVertex("r2");
    	        g.addVertex("r3");
    	        g.addVertex("r4");
    	        g.addVertex("r5");
    	        g.addVertex("r6");
    	        g.addVertex("r7");
    	        g.addVertex("r8");
    	        g.addVertex("r9");
    	        g.addVertex("r10");
    	        g.addVertex("r11");
    	        g.addVertex("r12");
    	        g.addVertex("r13");
    	        g.addVertex("r14");
    	        g.addVertex("r15");
    	        g.addVertex("r16");
    	        g.addVertex("r17");
    	      
    	        
    	        g.addEdge("r7", "r1");
    	        g.addEdge("r8", "r1");
    	        g.addEdge("r9", "r2");
    	        g.addEdge("r10", "r2");
    	        g.addEdge("r11", "r3");
    	        g.addEdge("r12", "r3");
    	        g.addEdge("r13", "r3");
    	        
    	        g.addEdge("r14", "r1");
    	        g.addEdge("r14", "r3");
    	        g.addEdge("r14", "r7");
    	        g.addEdge("r14", "r12");
    	        
    	        
    	        g.addEdge("r15", "r3");
    	        g.addEdge("r15", "r4");
    	        g.addEdge("r15", "r13");
    	        g.addEdge("r15", "r11");
    	        g.addEdge("r15", "r12");
    	        
    	        
    	        g.addEdge("r16", "r1");
    	        g.addEdge("r16", "r2");
    	        g.addEdge("r16", "r7");
    	        g.addEdge("r16", "r8");
    	        g.addEdge("r16", "r9");
    	        
    	        g.addEdge("r17", "r1");
    	        g.addEdge("r17", "r2");
    	        g.addEdge("r17", "r3");
    	        g.addEdge("r17", "r4");
    	        g.addEdge("r17", "r5");
    	        g.addEdge("r17", "r6");
    	        g.addEdge("r17", "r7");
    	        g.addEdge("r17", "r8");
    	        g.addEdge("r17", "r9");
    	        g.addEdge("r17", "r10");
    	        g.addEdge("r17", "r11");
    	        g.addEdge("r17", "r12"); 
    	        g.addEdge("r17", "r13");
    	        g.addEdge("r17", "r14");
    	        g.addEdge("r17", "r15");
    	        g.addEdge("r17", "r16");
    	        
    	      
    	        
    	      
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	        util.createNewNodeDAG(f, "r1" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",r1);
    	        util.createNewNodeDAG(f, "r2" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",r2);
    	        util.createNewNodeDAG(f, "r3" , "" ,  5, 0, 0, 0, 3, 0, false, true, "+",r3);
    	        util.createNewNodeDAG(f, "r4" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",r4);
    	        util.createNewNodeDAG(f, "r5" , "" ,  5, 0, 0, 0, 3, 0, false, true, "-",r5);
    	        util.createNewNodeDAG(f, "r6" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",r6);
    	        
    	        
    	        util.createNewNodeDAG(f, "r7" , "r1" ,  3, 0, 2, 2, 3, 0, false, false, "",r7);
    	        util.createNewNodeDAG(f, "r8" , "r1" ,  3, 0, 2, 1, 3, 0, false, false, "",r8);
    	        util.createNewNodeDAG(f, "r9" , "r2" ,  3, 0, 2, 1, 3, 0, false, false,"",r9);
    	        util.createNewNodeDAG(f, "r10" , "r2" ,  4, 0, 1, 1, 3, 0, false, false, "",r10);
    	        
    	        util.createNewNodeDAG(f, "r11" , "r3" ,  3, 0, 2, 2, 3, 0, false, false, "",r11);
    	        util.createNewNodeDAG(f, "r12" , "r3" ,  3, 0, 1, 1, 3, 0, false, false, "",r12);
    	        util.createNewNodeDAG(f, "r13" , "r3;" ,  3, 0, 1, 1, 3, 0, false, false, "",r13);
    	        util.createNewNodeDAG(f, "r14" , "r12;r7;r3;r1" ,  3, 0, 3, 3, 3, 0, false, false, "",r14);
    	        util.createNewNodeDAG(f, "r15" , "r13;r11;r4;r12;r3" ,  3, 0, 4, 3, 3, 0, false, false, "",r15);
    	        util.createNewNodeDAG(f, "r16" , "r1;r2;r9;r7;r8" ,  3, 0, 2, 4, 3, 0, false, false,"",r16);
    	        util.createNewNodeDAG(f, "r17" , "r1;r2;r3;r4;r5;r6;r7;r8;r9;r10;r11;r12;r13;r14;r15;r16" ,  3, 0, 4, 4, 3, 0,  false, false, "",r17);
    	        
    	      
    	     
    	       // util.createNewNodeDAG(f, "r161" , "r143;r7" ,  0, 0, 0, false, false, 0, 0.0,r161);
    	       // util.createNewNodeDAG(f, "r166" , "r8" ,  0, 0, 0, false, false, 0, 0.0,r166);
    	      
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("Bellman");
        		connected = true;
        		
        	}
        });
        mnExamples.add(mntmChess);
        
        JMenuItem mntmChess_1 = new JMenuItem("Chess Knight");
        mntmChess_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.updateClasses("+;-");
        		util.updateMMLvalues(21, 2,  0);
        		// KNIGHT
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
    	        
    	        
     	       
    	        
     	        
     	        
     	      
    
    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("k1");
    	        g.addVertex("k2");
    	        g.addVertex("k3");
    	        g.addVertex("k4");
    	        g.addVertex("k5");
    	        g.addVertex("k6");
    	        g.addVertex("k7");
    	        g.addVertex("k8");
    	        g.addVertex("k9");
    	        g.addVertex("k10");
    	        g.addVertex("k11");
    	        g.addVertex("k12");
    	        g.addVertex("k13");
    	        g.addVertex("k14");
    	        g.addVertex("k15");
    	        g.addVertex("k16");
    	        g.addVertex("k17");
    	        g.addVertex("k18");
    	        g.addVertex("k19");
    	        g.addVertex("k20");
    	        g.addVertex("k21");
    	        g.addVertex("k22");
    	        g.addVertex("k23");
    	        g.addVertex("k24");
    	        g.addVertex("k25");
    	        
    	        
    	        
    	        g.addEdge("k11", "k1");
    	        
    	        g.addEdge("k12", "k1");
    	        g.addEdge("k12", "k3");
    	        g.addEdge("k12", "k5");
    	        g.addEdge("k12", "k7");
    	        g.addEdge("k12", "k10");
    	        g.addEdge("k12", "k13");
    	        g.addEdge("k12", "k15");
    	        g.addEdge("k12", "k21");
    	        g.addEdge("k12", "k22");
    	        
    	        
    	        
    	        g.addEdge("k13", "k1");
    	        g.addEdge("k13", "k3");
    	        g.addEdge("k13", "k5");
    	        g.addEdge("k13", "k7");
    	        g.addEdge("k13", "k10");
    	        g.addEdge("k13", "k15");
    	        
    	   
    	        g.addEdge("k14", "k1");
    	        g.addEdge("k14", "k3");
    	        g.addEdge("k14", "k5");
    	        g.addEdge("k14", "k7");
    	        g.addEdge("k14", "k8");
    	        g.addEdge("k14", "k22");
    	        g.addEdge("k14", "k23");
    	   
    	        
    	        g.addEdge("k15", "k1");
    	        g.addEdge("k15", "k3");
    	        g.addEdge("k15", "k5");
    	        g.addEdge("k15", "k7");
    	        g.addEdge("k15", "k10");
    	  
    	        g.addEdge("k16", "k2");
    	        g.addEdge("k16", "k4");
    	        g.addEdge("k16", "k6");
    	        g.addEdge("k16", "k9");
    	        g.addEdge("k16", "k18");
    	        
    	        
    	        g.addEdge("k17", "k1");
    	        g.addEdge("k17", "k2");
    	        g.addEdge("k17", "k3");
    	        g.addEdge("k17", "k4");
    	        g.addEdge("k17", "k5");
    	        g.addEdge("k17", "k6");
    	        g.addEdge("k17", "k7");
    	        g.addEdge("k17", "k8");
    	        g.addEdge("k17", "k9");
    	        g.addEdge("k17", "k10");
    	        g.addEdge("k17", "k14");
    	        g.addEdge("k17", "k15");
    	        g.addEdge("k17", "k16");
    	        g.addEdge("k17", "k18");
    	        g.addEdge("k17", "k19");
    	        g.addEdge("k17", "k20");
    	        g.addEdge("k17", "k21");
    	        g.addEdge("k17", "k22");
    	        g.addEdge("k17", "k23");
    	        g.addEdge("k17", "k24");
    	        g.addEdge("k17", "k25");
    	        
    	        g.addEdge("k18", "k2");
    	        g.addEdge("k18", "k4");
    	        g.addEdge("k18", "k6");
    	        g.addEdge("k18", "k9");
    	        
    	        g.addEdge("k19", "k2");
    	        g.addEdge("k19", "k4");
    	        g.addEdge("k19", "k6");
    	        g.addEdge("k19", "k10");
    	        g.addEdge("k19", "k21");
    	        g.addEdge("k19", "k24");
    	        
    	        g.addEdge("k20", "k1");
    	        g.addEdge("k20", "k2");
    	        g.addEdge("k20", "k3");
    	        g.addEdge("k20", "k4");
    	        g.addEdge("k20", "k5");
    	        g.addEdge("k20", "k6");
    	        g.addEdge("k20", "k7");
    	        g.addEdge("k20", "k8");
    	        g.addEdge("k20", "k9");
    	        g.addEdge("k20", "k10");
    	        
    	        g.addEdge("k21", "k10");
    	        
    	        g.addEdge("k22", "k1");
    	        g.addEdge("k22", "k3");
    	        g.addEdge("k22", "k5");
    	        g.addEdge("k22", "k7");
    	        
    	        g.addEdge("k24", "k2");
    	        g.addEdge("k24", "k4");
    	        g.addEdge("k24", "k6");
    	       
    	        g.addEdge("k25", "k1");
    	        g.addEdge("k25", "k2");
    	        g.addEdge("k25", "k3");
    	        g.addEdge("k25", "k4");
    	        g.addEdge("k25", "k5");
    	        g.addEdge("k25", "k6");
    	        g.addEdge("k25", "k7");
    	        g.addEdge("k25", "k8");
    	        g.addEdge("k25", "k9");
    	        g.addEdge("k25", "k10");
    	        g.addEdge("k25", "k13");
    	        g.addEdge("k25", "k15");
    	        g.addEdge("k25", "k16");
    	        g.addEdge("k25", "k18");
    	        g.addEdge("k25", "k20");
    	        g.addEdge("k25", "k21");
    	        g.addEdge("k25", "k22");
    	        g.addEdge("k25", "k23");
    	        g.addEdge("k25", "k24");
    	        
    	        
    	      
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	        util.createNewNodeDAG(f, "k1" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",k1);
    	        util.createNewNodeDAG(f, "k2" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",k2);
    	        util.createNewNodeDAG(f, "k3" , "" ,  5, 0, 0, 0, 3, 0, false, true, "+",k3);
    	        util.createNewNodeDAG(f, "k4" , "" ,  5, 0, 0, 0, 3, 0, false, true, "+",k4);
    	        util.createNewNodeDAG(f, "k5" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",k5);
    	        util.createNewNodeDAG(f, "k6" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",k6);
    	        util.createNewNodeDAG(f, "k7" , "" ,  5, 0, 0, 0, 3, 0, false, true, "+",k7);
    	        util.createNewNodeDAG(f, "k8" , "" ,  5, 0, 0, 0, 3, 0, false, true, "-",k8);
    	        util.createNewNodeDAG(f, "k9" , "" ,  5, 0, 0, 0, 3, 0, false, true, "-",k9);
    	        util.createNewNodeDAG(f, "k10" , "" ,  5, 0, 0, 0, 3, 0, false, true, "-",k10);
    	        
    	        
    	        
    	        util.createNewNodeDAG(f, "k11" , "k1" ,  3, 0, 2, 2, 3, 0,  false, false, "",k11);
    	        util.createNewNodeDAG(f, "k15" , "k1;k3;k5;k7;k10" ,   3, 0, 8, 4, 5, 2,  false, false, "",k15);
    	        util.createNewNodeDAG(f, "k13" , "k1;k3;k5;k7;k10;k15" ,  3, 0, 8, 4, 5, 2,  false, false, "",k13);
    	        util.createNewNodeDAG(f, "k18" , "k2;k4;k6;k9" ,   4, 0, 10, 4, 6, 2,  false, false, "",k18);
    	        util.createNewNodeDAG(f, "k16" , "k2;k4;k6;k9;k18" ,   2, 0, 8, 4, 5, 2,  false, false, "",k16);
       	        
    	        
    	        util.createNewNodeDAG(f, "k20" , "k1;k2;k3;k4;k5;k6;k7;k8;k9;k10" ,   2, 0, 6, 4, 4, 1,  false, false, "",k20);
    	        util.createNewNodeDAG(f, "k21" , "k10" ,   2, 0, 8, 4, 5, 2,  false, false, "",k21);
    	        util.createNewNodeDAG(f, "k22" , "k1;k3;k5;k7" ,   3, 0, 8, 4, 5, 2,  false, false, "",k22);
    	        util.createNewNodeDAG(f, "k23" , "" ,   3, 0, 8, 4, 5, 2,  false, false, "",k23);
    	        util.createNewNodeDAG(f, "k24" , "k2;k4;k6" ,  3, 0, 8, 4, 5, 2,  false, false, "",k24);    	
    	        
    	        util.createNewNodeDAG(f, "k19" , "k2;k4;k6;k10;k21;k24" ,   2, 0, 6, 4, 4, 1,  false, false, "",k19);
    	        util.createNewNodeDAG(f, "k14" , "k1;k3;k5;k7;k8;k22;k23" ,   2, 0, 6, 4, 4, 1,  false, false, "",k14);
    	        util.createNewNodeDAG(f, "k12" , "k1;k3;k5;k7;k10;k13;k15;k21;k22" ,   2, 0, 6, 4, 4, 1,  false, false, "",k12);
    	        util.createNewNodeDAG(f, "k25" , "k1;k2;k3;k4;k5;k6;k7;k8;k9;k10;k13;k15;k16;k18;k20;k21;k22;k23;k24" ,   1, 0, 10, 6, 5, 2,  false, false, "",k25);
    	        util.createNewNodeDAG(f, "k17" , "k1;k2;k3;k4;k5;k6;k7;k8;k9;k10;k14;k15;k16;k18;k19;k20;k21;k22;k23;k24;k25" ,   0, 0, 7, 5, 4, 1,  false, false, "",k17);
    	        
    	       // util.createNewNodeDAG(f, "k161" , "k143;k7" ,  0, 0, 0, false, false, 0, 0.0,k161);
    	       // util.createNewNodeDAG(f, "k166" , "k8" ,  0, 0, 0, false, false, 0, 0.0,k166);
    	        
    	        
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("Bellman");
        		connected = true;
        		
        	}
        });
        mnExamples.add(mntmChess_1);
        
        JMenuItem mntmChessBishop = new JMenuItem("Chess Bishop");
        mntmChessBishop.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.updateClasses("+;-");
        		util.updateMMLvalues(21, 2,  0);
        		// BISHOP
        
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
    	        
    	        
    	        
    	        
    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("b1");
    	        g.addVertex("b2");
    	        g.addVertex("b3");
    	        g.addVertex("b4");
    	        g.addVertex("b5");
    	        g.addVertex("b6");
    	        g.addVertex("b7");
    	        g.addVertex("b8");
    	        g.addVertex("b9");
    	        g.addVertex("b10");
    	        g.addVertex("b11");
    	        g.addVertex("b12");
    	        g.addVertex("b13");
 
    	        
    	           	        
    	        g.addEdge("b5", "b2");
    	        g.addEdge("b5", "b6");
    	        g.addEdge("b5", "b7");
    	        
    	        g.addEdge("b6", "b2");
    	        g.addEdge("b7", "b2");
    	        g.addEdge("b8", "b1");
    	        
    	        g.addEdge("b9", "b2");
    	        g.addEdge("b9", "b6");
    	        g.addEdge("b9", "b7");
    	        
    	        g.addEdge("b9", "b4");
    	        
    	        g.addEdge("b10", "b1");
    	        g.addEdge("b10", "b2");
    	        g.addEdge("b10", "b3");
    	        g.addEdge("b10", "b6");
    	        g.addEdge("b10", "b7");
    	        g.addEdge("b10", "b8");
    	        
    	        g.addEdge("b11", "b1");
    	        g.addEdge("b11", "b2");
    	        g.addEdge("b11", "b3");
    	        g.addEdge("b11", "b4");

    	        
    	        g.addEdge("b12", "b1");
    	        g.addEdge("b12", "b2");
    	        g.addEdge("b12", "b3");
    	        g.addEdge("b12", "b4");
    	        g.addEdge("b12", "b6");
    	        g.addEdge("b12", "b7");
    	        g.addEdge("b12", "b8");
    	        g.addEdge("b12", "b10");
    	        g.addEdge("b12", "b13");
    
    	        
    	        g.addEdge("b13", "b1");
    	        g.addEdge("b13", "b4");
    	        g.addEdge("b13", "b8");
    	        
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	   
    	        
    	        
    	        util.createNewNodeDAG(f, "b1" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",b1);
    	        util.createNewNodeDAG(f, "b2" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",b2);
    	        util.createNewNodeDAG(f, "b3" , "" ,  5, 0, 0, 0, 3, 0, false, true, "+",b3);
    	        util.createNewNodeDAG(f, "b4" , "" ,  5, 0, 0, 0, 3, 0, false, true, "-",b4);

    	        util.createNewNodeDAG(f, "b6" , "b2" , 3, 0, 8, 4, 5, 2, false, false, "",b6);
    	        util.createNewNodeDAG(f, "b7" , "b2" , 3, 0, 8, 4, 5, 2, false, false, "",b7);
    	        
    	        util.createNewNodeDAG(f, "b8" , "b1" , 3, 0, 8, 4, 5, 2, false, false, "",b8);
    	        
    	        util.createNewNodeDAG(f, "b5" , "b2;b6;b7" , 2, 0, 6, 4, 4, 1, false, false, "",b5);
    	        util.createNewNodeDAG(f, "b9" , "b2;b4;b6;b7" , 2, 0, 6, 4, 4, 1, false, false, "",b9);
    	        
    	        util.createNewNodeDAG(f, "b10" , "b1;b2;b3;b6;b7;b8" , 0, 0, 10, 5, 5, 2, false, false, "",b10);
    	        util.createNewNodeDAG(f, "b11" , "b1;b2;b3;b4" , 4, 1, 10, 4, 6, 2, false, false, "",b11);
    	        util.createNewNodeDAG(f, "b13" , "b1;b4;b8" , 2, 0, 9, 5, 5, 2, false, false, "",b13);
    	        util.createNewNodeDAG(f, "b12" , "b1;b2;b3;b4;b6;b7;b8;b10;b13" , 1, 0, 10, 6, 5, 2, false, false, "",b12);
    	     
    	        
    	        
    	

    	        
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("Bellman");
        		connected = true;
        		
        		
        		
        		
        		
        	}
        });
        mnExamples.add(mntmChessBishop);
        
        JMenuItem mntmChessQueen = new JMenuItem("Chess Queen");
        mntmChessQueen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.updateClasses("+;-");
        		util.updateMMLvalues(21, 2,  0);
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
    	   	    String q23 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E),fdiff(A,C,E).";//q1;q2;q3;q22 ---
    	   	    String q24 = "move(queen,pos(A,B),pos(C,D))"; //q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23;q24;q25;q26;q27;q28;q29;q30
    	        
    	
    	      //  String q25 = "move(queen,pos(A,B),pos(C,D)) :- move(rook,pos(A,B),pos(C,D))"; //q7;q6;q4;q5 ---
    	      //  String q26 = "move(queen,pos(A,B),pos(A,D)) :- move(rook,pos(A,B),pos(A,D))"; //q7;q6
    	      //  String q27 = "move(queen,pos(A,B),pos(C,B)) :- move(rook,pos(A,B),pos(C,B))"; //q4;q5
    	       // String q28 = "move(queen,pos(A,B),pos(C,D)) :- move(rook,pos(A,C),pos(A,C))"; //q1;q2;q3;q6;q7;q5;q4;q9;q8;q10
    	       
    	      //  String q29 = "move(queen,pos(A,B),pos(C,D)) :- move(bishop,pos(A,B),pos(C,D))"; //q1;q2;q3 ---
    	      //  String q30 = "move(queen,pos(A,B),pos(C,D)) :- move(bishop,pos(B,D),pos(B,D))"; //q1;q2;q3;q6;q7;q5;q4;q9;q8;q10
    	        		
    	        	       
    	        
    	        
    	        
    	        
    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("q1");
    	        g.addVertex("q2");
    	        g.addVertex("q3");
    	        g.addVertex("q4");
    	        g.addVertex("q5");
    	        g.addVertex("q6");
    	        g.addVertex("q7");
    	        g.addVertex("q8");
    	        g.addVertex("q9");
    	        g.addVertex("q10");
    	        g.addVertex("q11");
    	        g.addVertex("q12");
    	        g.addVertex("q13");
    	        g.addVertex("q14");
    	        g.addVertex("q15");
    	        g.addVertex("q16");
    	        g.addVertex("q17");
    	        g.addVertex("q18");
    	        g.addVertex("q19");
    	        g.addVertex("q20");
    	        g.addVertex("q21");
    	        g.addVertex("q22");
    	        g.addVertex("q23");
    	        g.addVertex("q24");
    	     //   g.addVertex("q25");
    	      //  g.addVertex("q26");
    	     //   g.addVertex("q27");
    	     //   g.addVertex("q28");
    	     //   g.addVertex("q29");
    	     //   g.addVertex("q30");
    	        
    	           	        
    	        g.addEdge("q11", "q7");
    	        
    	        g.addEdge("q12", "q7");g.addEdge("q12", "q6"); g.addEdge("q12", "q11");g.addEdge("q12", "q13");
    	        
    	        g.addEdge("q14", "q1");g.addEdge("q14", "q9");
    	        
    	        g.addEdge("q15", "q1");g.addEdge("q15", "q3");g.addEdge("q15", "q4");g.addEdge("q15", "q10");
    	        
    	        g.addEdge("q16", "q1");g.addEdge("q16", "q9");
    	        
    	        g.addEdge("q18", "q5");
    	        
    	        g.addEdge("q19", "q4");g.addEdge("q19", "q5"); g.addEdge("q19", "q13");g.addEdge("q19", "q17"); g.addEdge("q19", "q18");//g.addEdge("q19", "q27");
    	        
    	        g.addEdge("q20", "q2");
    	        
    	        g.addEdge("q21", "q1");g.addEdge("q21", "q2");g.addEdge("q21", "q3");g.addEdge("q21", "q4");g.addEdge("q21", "q5");g.addEdge("q21", "q6");
    	        g.addEdge("q21", "q7");g.addEdge("q21", "q8");g.addEdge("q21", "q9");g.addEdge("q21", "q10");g.addEdge("q21", "q15");g.addEdge("q21", "q17");g.addEdge("q21", "q20");
    	        
    	        g.addEdge("q22", "q1");g.addEdge("q22", "q10");
    	        
    	        g.addEdge("q23", "q1");g.addEdge("q23", "q2");g.addEdge("q23", "q3");
    	        
    	        g.addEdge("q24", "q1");g.addEdge("q24", "q2");g.addEdge("q24", "q3");g.addEdge("q24", "q4");g.addEdge("q24", "q5");g.addEdge("q24", "q6");
    	        g.addEdge("q24", "q7");g.addEdge("q24", "q8");g.addEdge("q24", "q9");g.addEdge("q24", "q10");g.addEdge("q24", "q11");g.addEdge("q24", "q12");g.addEdge("q24", "q13");
    	        g.addEdge("q24", "q14");g.addEdge("q24", "q15");g.addEdge("q24", "q16");g.addEdge("q24", "q17");g.addEdge("q24", "q18");g.addEdge("q24", "q19");
    	        g.addEdge("q24", "q20");g.addEdge("q24", "q21");g.addEdge("q24", "q22");g.addEdge("q24", "q23");
    	       // g.addEdge("q24", "q25");g.addEdge("q24", "q26");g.addEdge("q24", "q27");g.addEdge("q24", "q28"); g.addEdge("q24", "q29");
    	     //   g.addEdge("q24", "q30");
    	        
    	        
    	        
    	     //   g.addEdge("q25", "q7");g.addEdge("q25", "q6");g.addEdge("q25", "q4");g.addEdge("q25", "q5");
    	        
    	      //  g.addEdge("q26", "q7");g.addEdge("q26", "q6");
    	        
    	      //  g.addEdge("q27", "q4");g.addEdge("q27", "q5");
    	        
    	       // g.addEdge("q28", "q1");g.addEdge("q28", "q2"); g.addEdge("q28", "q3");g.addEdge("q28", "q4"); g.addEdge("q28", "q5");g.addEdge("q28", "q6");
    	       // g.addEdge("q28", "q7");g.addEdge("q28", "q8"); g.addEdge("q28", "q9");g.addEdge("q28", "q10");
    	        
    	        
    	      //  g.addEdge("q29", "q1");g.addEdge("q29", "q2"); g.addEdge("q29", "q3");
    	        
    	      //  g.addEdge("q30", "q1");g.addEdge("q30", "q2"); g.addEdge("q30", "q3");g.addEdge("q30", "q4"); g.addEdge("q30", "q5");g.addEdge("q30", "q6");
    	      //  g.addEdge("q30", "q7");g.addEdge("q30", "q8"); g.addEdge("q30", "q9");g.addEdge("q30", "q10");
    	      //  
    	        
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	   
    	        
    	        
    	       
    	        util.createNewNodeDAG(f, "q1" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q1);
    	        util.createNewNodeDAG(f, "q2" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q2);
    	        util.createNewNodeDAG(f, "q3" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q3);
    	        util.createNewNodeDAG(f, "q4" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q4);
    	        util.createNewNodeDAG(f, "q5" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q5);
    	        util.createNewNodeDAG(f, "q6" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q6);
    	        util.createNewNodeDAG(f, "q7" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q7);
    	        util.createNewNodeDAG(f, "q8" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",q8);
    	        util.createNewNodeDAG(f, "q9" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",q9);
    	        util.createNewNodeDAG(f, "q10" , "", 5, 0, 0, 0, 3, 0, false, true, "-",q10);

    	        	        
    	        
    	        util.createNewNodeDAG(f, "q11" , "q7" , 3, 0, 2, 1, 3, 0, false, false, "",q11);
    	        util.createNewNodeDAG(f, "q13" , "" , 1, 0, 4, 2, 3, 0, false, false, "",q13);

    	        
    	        util.createNewNodeDAG(f, "q14" , "q1;q9" , 2, 0, 2, 2, 3, 0, false, false, "",q14);
    	        util.createNewNodeDAG(f, "q15" , "q1;q3;q4;q10" , 2, 0, 6, 4, 4, 1, false, false, "",q15);    	        
    	        util.createNewNodeDAG(f, "q16" , "q1;q9" , 3, 0, 2, 2, 3, 0, false, false, "",q16);    	        
    	        util.createNewNodeDAG(f, "q17" , "" , 2, 0, 6, 3, 4, 1, false, false, "",q17);
    	        
    	        util.createNewNodeDAG(f, "q18" , "q5" , 3, 0, 8, 3, 5, 2, false, false, "",q18);
    	        
    	       // util.createNewNodeDAG(f, "q25" , "q7;q6;q4;q5" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q25);
    	      //  util.createNewNodeDAG(f, "q26" , "q7;q6" , 2, 0, 8, 3, 6, 0, false, false, 0, 0.0,q26);
    	      //  util.createNewNodeDAG(f, "q27" , "q4;q5" , 2, 0, 8, 3, 6, 0, false, false, 0, 0.0,q27);
    	      //  util.createNewNodeDAG(f, "q28" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q28);
    	      //  util.createNewNodeDAG(f, "q29" , "q1;q2;q3" , 2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q29);
    	      //  util.createNewNodeDAG(f, "q30" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" ,  2, 0, 8, 4, 6, 0, false, false, 0, 0.0,q30);
    	        
    	        
    	        util.createNewNodeDAG(f, "q12" , "q7;q6;q11;q13" , 1, 0, 4, 3, 3, 0, false, false, "",q12);
    	        util.createNewNodeDAG(f, "q19" , "q4;q5;q13;q17;q18" , 1, 0, 4, 3, 3, 0, false, false, "",q19);
    	        
    	        util.createNewNodeDAG(f, "q20" , "q2" , 2, 0, 6, 4, 4, 1,false, false, "",q20);
    	        util.createNewNodeDAG(f, "q21" , "q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q15;q17;q20" , 1, 0, 7, 5, 4, 1, false, false,"",q21);
    	        util.createNewNodeDAG(f, "q22" , "q1;q10" , 2, 0, 9, 5, 5, 2, false, false, "",q22);
    	        util.createNewNodeDAG(f, "q23" , "q1;q2;q3" , 1, 0, 10, 5, 5, 2, false, false, "",q23);
    	        util.createNewNodeDAG(f, "q24" , "q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23;" , 1, 0, 4, 4, 3, 0, false, false, "",q24);
    
    
    	        
    	        
    	

    	        
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("Bellman");
        		connected = true;
        		
        		
        		
        		
        	}
        });
        mnExamples.add(mntmChessQueen);
        
        JMenuItem mntmChessKing = new JMenuItem("Chess King");
        mntmChessKing.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		util.updateClasses("+;-");
        		util.updateMMLvalues(21, 2,  0);
        		//KING
        		
        		//move(king,pos(A,B),pos(C,D)) :- rdiff(B,D,1), fdiff(A,C,1).
        		//move(king,pos(A,B),pos(A,C)) :- rdiff(B,C,1).
        		//move(king,pos(A,B),pos(C,B)) :- fdiff(A,C,1).

                
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
    	          	       
    	   
    	
    	        
    	        	       
    	        
    	        
    	        
    	        
    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("x1");
    	        g.addVertex("x2");
    	        g.addVertex("x3");
    	        g.addVertex("x4");
    	        g.addVertex("x5");
    	        g.addVertex("x6");
    	        g.addVertex("x7");
    	        g.addVertex("x8");
    	        g.addVertex("x9");
    	        g.addVertex("x10");
    	        g.addVertex("x11");
    	        g.addVertex("x12");
    	        g.addVertex("x13");
    	        g.addVertex("x14");
    	        g.addVertex("x15");
    	        g.addVertex("x16");
    	        g.addVertex("x17");
    	        g.addVertex("x18");
    	        g.addVertex("x19");
    	        g.addVertex("x20");
    	     
   
    	  
    	          	       
    	        
    	           	        
    	        g.addEdge("x12", "x11");
    	        
    	        g.addEdge("x13", "x3");g.addEdge("x13", "x4");
    	        
    	        g.addEdge("x14", "x3");g.addEdge("x14", "x4");g.addEdge("x14", "x5");g.addEdge("x14", "x6");g.addEdge("x14", "x7");
    	        g.addEdge("x14", "x9");g.addEdge("x14", "x13");g.addEdge("x14", "x18");
    	        
    	        g.addEdge("x15", "x1");g.addEdge("x15", "x2");g.addEdge("x15", "x5");g.addEdge("x15", "x6");g.addEdge("x15", "x7");g.addEdge("x15", "x8");
    	        
    	        g.addEdge("x16", "x3");g.addEdge("x16", "x4");g.addEdge("x16", "x11");
    	        
    	        g.addEdge("x17", "x16");g.addEdge("x17", "x16");
    	        
    	        g.addEdge("x18", "x5");g.addEdge("x18", "x6");g.addEdge("x18", "x7");
    	        
    	        g.addEdge("x19", "x1");g.addEdge("x19", "x2");g.addEdge("x19", "x3");g.addEdge("x19", "x4");g.addEdge("x19", "x5");g.addEdge("x19", "x6");
    	        g.addEdge("x19", "x7");g.addEdge("x19", "x8");g.addEdge("x19", "x9");g.addEdge("x19", "x10");
    	        g.addEdge("x19", "x12");g.addEdge("x19", "x13");g.addEdge("x19", "x14");g.addEdge("x19", "x15");g.addEdge("x19", "x16");g.addEdge("x19", "x17");
    	        g.addEdge("x19", "x8");g.addEdge("x19", "x20");
    	        
    	        g.addEdge("x20", "x1");g.addEdge("x20", "x2");
    	        
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	   
    	        
    	
    	        util.createNewNodeDAG(f, "x1" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x1);
    	        util.createNewNodeDAG(f, "x2" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x2);
    	        util.createNewNodeDAG(f, "x3" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x3);
    	        util.createNewNodeDAG(f, "x4" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x4);
    	        util.createNewNodeDAG(f, "x5" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x5);
    	        util.createNewNodeDAG(f, "x6" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x6);
    	        util.createNewNodeDAG(f, "x7" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",x7);
    	        
    	        util.createNewNodeDAG(f, "x8" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",x8);
    	        util.createNewNodeDAG(f, "x9" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",x9);
    	        util.createNewNodeDAG(f, "x10" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",x10);
    	        util.createNewNodeDAG(f, "x11" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",x11);
    	        
    	            	        	           	        
    	        util.createNewNodeDAG(f, "x12" , "x11" , 2, 0, 6, 3, 4, 1, false, false, "",x12);
    	        util.createNewNodeDAG(f, "x13" , "x3;x4;" , 2, 0, 6, 3, 4, 1, false, false, "",x13);
    	        
    	        util.createNewNodeDAG(f, "x15" , "x1;x2;x5;x6;x7;x8" , 4, 0, 10, 4, 6, 2, false, false, "",x15);
    	        util.createNewNodeDAG(f, "x16" , "x3;x4;x11" , 3, 0, 8, 4, 5, 2, false, false,"",x16);
    	        util.createNewNodeDAG(f, "x17" , "x16" , 3, 0, 8, 4, 5, 2, false, false, "",x17);
    	        util.createNewNodeDAG(f, "x18" , "x5;x6;x7" , 3, 0, 8, 4, 5, 2, false, false, "",x18);
    	        
    	        util.createNewNodeDAG(f, "x20" , "x1;x2" , 2, 0, 6, 3, 4, 1, false, false, "",x20);
    	        
    	        util.createNewNodeDAG(f, "x14" , "x3;x4;x5;x6;x7;x9;x13;x18" , 2, 0, 6, 4, 4, 1, false, false, "",x14);
    	        util.createNewNodeDAG(f, "x19" , "x1;x2;x3;x4;x5;x6;x7;x8;x9;x10;x12;x13;x14;x15;x16;x17;x18;x20" , 2, 0, 6, 3, 4, 1, false, false, "",x19);
    	    
    	

    	     
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("Bellman");
        		connected = true;
        		
        		
        		
        		
        	}
        });
        
        JMenuItem mntmChessQueenC = new JMenuItem("Chess Queen II");
        mntmChessQueenC.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		util.updateMMLvalues(21, 2,  0);
        		util.updateClasses("+;-");
        		
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
    	   	    String q23 = "move(queen,pos(A,B),pos(C,D)) :- rdiff(B,D,E),fdiff(A,C,E).";//q1;q2;q3;q22 ---
    	   	    String q24 = "move(queen,pos(A,B),pos(C,D))"; //q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23;q24;q25;q26;q27;q28;q29;q30
    	        
    	
    	        String q25 = "move(queen,pos(A,B),pos(C,D)) :- move(rook,pos(A,B),pos(C,D))"; //q7;q6;q4;q5 ---
    	        String q26 = "move(queen,pos(A,B),pos(A,D)) :- move(rook,pos(A,B),pos(A,D))"; //q7;q6
    	        String q27 = "move(queen,pos(A,B),pos(C,B)) :- move(rook,pos(A,B),pos(C,B))"; //q4;q5
    	        String q28 = "move(queen,pos(A,B),pos(C,D)) :- move(rook,pos(A,C),pos(A,C))"; //q1;q2;q3;q6;q7;q5;q4;q9;q8;q10
    	       
    	        String q29 = "move(queen,pos(A,B),pos(C,D)) :- move(bishop,pos(A,B),pos(C,D))"; //q1;q2;q3 ---
    	        String q30 = "move(queen,pos(A,B),pos(C,D)) :- move(bishop,pos(B,D),pos(B,D))"; //q1;q2;q3;q6;q7;q5;q4;q9;q8;q10
    	        		
    	        	       
    	        
    	        
    	        
    	        
    	        // add some sample data (graph manipulated via JGraphX)
    	        g.addVertex("q1");
    	        g.addVertex("q2");
    	        g.addVertex("q3");
    	        g.addVertex("q4");
    	        g.addVertex("q5");
    	        g.addVertex("q6");
    	        g.addVertex("q7");
    	        g.addVertex("q8");
    	        g.addVertex("q9");
    	        g.addVertex("q10");
    	        g.addVertex("q11");
    	        g.addVertex("q12");
    	        g.addVertex("q13");
    	        g.addVertex("q14");
    	        g.addVertex("q15");
    	        g.addVertex("q16");
    	        g.addVertex("q17");
    	        g.addVertex("q18");
    	        g.addVertex("q19");
    	        g.addVertex("q20");
    	        g.addVertex("q21");
    	        g.addVertex("q22");
    	        g.addVertex("q23");
    	        g.addVertex("q24");
    	        g.addVertex("q25");
    	        g.addVertex("q26");
    	        g.addVertex("q27");
    	        g.addVertex("q28");
    	        g.addVertex("q29");
    	        g.addVertex("q30");
    	        
    	           	        
    	        g.addEdge("q11", "q7");
    	        
    	        g.addEdge("q12", "q7");g.addEdge("q12", "q6"); g.addEdge("q12", "q11");g.addEdge("q12", "q13");
    	        
    	        g.addEdge("q14", "q1");g.addEdge("q14", "q9");
    	        
    	        g.addEdge("q15", "q1");g.addEdge("q15", "q3");g.addEdge("q15", "q4");g.addEdge("q15", "q5");
    	        
    	        g.addEdge("q16", "q1");g.addEdge("q16", "q9");
    	        
    	        g.addEdge("q18", "q5");
    	        
    	        g.addEdge("q19", "q4");g.addEdge("q19", "q5"); g.addEdge("q19", "q13");g.addEdge("q19", "q17"); g.addEdge("q19", "q18");g.addEdge("q19", "q27");
    	        
    	        g.addEdge("q20", "q2");
    	        
    	        g.addEdge("q21", "q1");g.addEdge("q21", "q2");g.addEdge("q21", "q3");g.addEdge("q21", "q4");g.addEdge("q21", "q5");g.addEdge("q21", "q6");
    	        g.addEdge("q21", "q7");g.addEdge("q21", "q8");g.addEdge("q21", "q9");g.addEdge("q21", "q10");g.addEdge("q21", "q15");g.addEdge("q21", "q17");g.addEdge("q21", "q20");
    	        
    	        g.addEdge("q22", "q1");g.addEdge("q22", "q10");
    	        
    	        g.addEdge("q23", "q1");g.addEdge("q23", "q2");g.addEdge("q23", "q3");
    	        
    	        g.addEdge("q24", "q1");g.addEdge("q24", "q2");g.addEdge("q24", "q3");g.addEdge("q24", "q4");g.addEdge("q24", "q5");g.addEdge("q24", "q6");
    	        g.addEdge("q24", "q7");g.addEdge("q24", "q8");g.addEdge("q24", "q9");g.addEdge("q24", "q10");g.addEdge("q24", "q11");g.addEdge("q24", "q12");g.addEdge("q24", "q13");
    	        g.addEdge("q24", "q14");g.addEdge("q24", "q15");g.addEdge("q24", "q16");g.addEdge("q24", "q17");g.addEdge("q24", "q18");g.addEdge("q24", "q19");
    	        g.addEdge("q24", "q20");g.addEdge("q24", "q21");g.addEdge("q24", "q22");g.addEdge("q24", "q23");
    	        g.addEdge("q24", "q25");g.addEdge("q24", "q26");g.addEdge("q24", "q27");g.addEdge("q24", "q28"); g.addEdge("q24", "q29");
    	        g.addEdge("q24", "q30");
    	        
    	        
    	        
    	        g.addEdge("q25", "q7");g.addEdge("q25", "q6");g.addEdge("q25", "q4");g.addEdge("q25", "q5");
    	        
    	        g.addEdge("q26", "q7");g.addEdge("q26", "q6");
    	        
    	        g.addEdge("q27", "q4");g.addEdge("q27", "q5");
    	        
    	        g.addEdge("q28", "q1");g.addEdge("q28", "q2"); g.addEdge("q28", "q3");g.addEdge("q28", "q4"); g.addEdge("q28", "q5");g.addEdge("q28", "q6");
    	        g.addEdge("q28", "q7");g.addEdge("q28", "q8"); g.addEdge("q28", "q9");g.addEdge("q28", "q10");
    	        
    	        
    	        g.addEdge("q29", "q1");g.addEdge("q29", "q2"); g.addEdge("q29", "q3");
    	        
    	        g.addEdge("q30", "q1");g.addEdge("q30", "q2"); g.addEdge("q30", "q3");g.addEdge("q30", "q4"); g.addEdge("q30", "q5");g.addEdge("q30", "q6");
    	        g.addEdge("q30", "q7");g.addEdge("q30", "q8"); g.addEdge("q30", "q9");g.addEdge("q30", "q10");
    	        
    	        
    	        
    	        String [] array ={""};
    	        //createNewNodeDAG( f, String id, String edges, int pos,int neg, int rules, int numC, int numV, int numF, boolean isRec, boolean isGround, int usab, double cert)
    	   
    	        
    	        
    	       
    	        util.createNewNodeDAG(f, "q1" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q1);
    	        util.createNewNodeDAG(f, "q2" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q2);
    	        util.createNewNodeDAG(f, "q3" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q3);
    	        util.createNewNodeDAG(f, "q4" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q4);
    	        util.createNewNodeDAG(f, "q5" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q5);
    	        util.createNewNodeDAG(f, "q6" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q6);
    	        util.createNewNodeDAG(f, "q7" , "" , 5, 0, 0, 0, 3, 0, false, true, "+",q7);
    	        util.createNewNodeDAG(f, "q8" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",q8);
    	        util.createNewNodeDAG(f, "q9" , "" , 5, 0, 0, 0, 3, 0, false, true, "-",q9);
    	        util.createNewNodeDAG(f, "q10" , "", 5, 0, 0, 0, 3, 0, false, true, "-",q10);

    	        	        
    	        
    	        util.createNewNodeDAG(f, "q11" , "q7" , 3, 0, 2, 1, 3, 0, false, false, "",q11);
    	        util.createNewNodeDAG(f, "q13" , "" , 1, 0, 4, 2, 3, 0, false, false, "",q13);

    	        
    	        util.createNewNodeDAG(f, "q14" , "q1;q9" , 2, 0, 2, 2, 3, 0, false, false, "",q14);
    	        util.createNewNodeDAG(f, "q15" , "q1;q3;q4;q10" , 2, 0, 6, 4, 4, 1, false, false, "",q15);    	        
    	        util.createNewNodeDAG(f, "q16" , "q1;q9" , 3, 0, 2, 2, 3, 0, false, false, "",q16);    	        
    	        util.createNewNodeDAG(f, "q17" , "" , 2, 0, 6, 3, 4, 1, false, false, "",q17);
    	        
    	        util.createNewNodeDAG(f, "q18" , "q5" , 3, 0, 8, 3, 5, 2, false, false,"",q18);
    	        
    	        util.createNewNodeDAG(f, "q25" , "q7;q6;q4;q5" , 2, 0, 8, 4, 6, 0, false, false, "",q25);
    	        util.createNewNodeDAG(f, "q26" , "q7;q6" , 2, 0, 8, 3, 6, 0, false, false, "",q26);
    	        util.createNewNodeDAG(f, "q27" , "q4;q5" , 2, 0, 8, 3, 6, 0, false, false, "",q27);
    	        util.createNewNodeDAG(f, "q28" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" , 2, 0, 8, 4, 6, 0, false, false, "",q28);
    	        util.createNewNodeDAG(f, "q29" , "q1;q2;q3" , 2, 0, 8, 4, 6, 0, false, false, "",q29);
    	        util.createNewNodeDAG(f, "q30" , "q1;q2;q3;q6;q7;q5;q4;q9;q8;q10" ,  2, 0, 8, 4, 6, 0, false, false, "",q30);
    	        
    	        
    	        util.createNewNodeDAG(f, "q12" , "q7;q6;q11;q13" , 1, 0, 4, 3, 3, 0, false, false, "",q12);
    	        util.createNewNodeDAG(f, "q19" , "q4;q5;q13;q17;q18;q27" , 1, 0, 4, 3, 3, 0, false, false, "",q19);
    	        
    	        util.createNewNodeDAG(f, "q20" , "q2" , 2, 0, 6, 4, 4, 1,false, false, "",q20);
    	        util.createNewNodeDAG(f, "q21" , "q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q15;q17;q20" , 1, 0, 7, 5, 4, 1, false, false, "",q21);
    	        util.createNewNodeDAG(f, "q22" , "q1;q10" , 2, 0, 9, 5, 5, 2, false, false, "",q22);
    	        util.createNewNodeDAG(f, "q23" , "q1;q2;q3" , 1, 0, 10, 5, 5, 2, false, false, "",q23);
    	        util.createNewNodeDAG(f, "q24" , "q1;q2;q3;q4;q5;q6;q7;q8;q9;q10;q11;q12;q13;q14;q15;q16;q18;q19;q20;q21;q22;q23;q25;q26;q27;q28;q29;q30" , 1, 0, 4, 4, 3, 0, false, false, "",q24);
    
    
    	        
    	        
    	

    	        
    	        
    	        util.layoutGraph2draw(jgxAdapter,circle);
    	        
    			mntmConnect.setText("Bellman");
        		connected = true;
        		
        	}
        });
        mnExamples.add(mntmChessQueenC);
        mnExamples.add(mntmChessKing);
        
        JMenuItem mntmChessExp1 = new JMenuItem("Chess Exp 1");
        mntmChessExp1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		int t=500;
        		int steps = t;
        		
        		util.updateClasses("+;-");
        		util.statsICMLA_ini();//genero nodos y vertices entre ellos
        		
        		int minE= 1;
        		int maxE = 42; 
        		int minR= 1;
        		int maxR = 59;
        		
        		boolean rem = true;//hay reemplazo
        		boolean ob = true;
        		int stack = 50;
        		int obP= 50;
        		StdRandom.setSeed(1000);
        		
        		if (rem){
        			
        		
        			while (t>0){
        				util.updateMMLvalues(21, 2,  0);
        				frameStatistics.fillTable(f,util.classList);        		
        				util.statsICMLA_runR(g,f,steps-t+1,ob,stack, obP,minE,maxE,minR,maxR);
        				//util.updateDAGdescriptors(f);
        				frameStatistics.updateTable(f,util.classList);
        				t--;
        			}
        			System.out.println("FIN");
        			
        		}else{//sin reemplazo
        		
        			final int nE = 41;
            		final int nR = 58;
            	    final ArrayList<Integer> arrE = new ArrayList<Integer>(nE); 
            	    final ArrayList<Integer> arrR = new ArrayList<Integer>(nR); 
            	    for (int i = 1; i <= nE; i++) {
            	        arrE.add(i);
            	    }
            	    for (int j = 1; j <= nR; j++) {
            	        arrR.add(j);
            	    }
            	    
            	    Collections.shuffle(arrE);
            	    Collections.shuffle(arrR);
            	    
            	  
            	    
            	    while (t>0){
        				util.updateMMLvalues(21, 2,  0);
        				frameStatistics.fillTable(f,util.classList);        		
        				 util.statsICMLA_run(g,f,steps-t+1,arrE,arrR,nE,nR,ob,stack, obP);
        				//util.updateDAGdescriptors(f);
        				 frameStatistics.updateTable(f,util.classList);
        				t--;
        			}
            	    
        		}
        		
        		
        		
        		
        	}
        });
        mnExamples.add(mntmChessExp1);
        
        JMenuItem mntmChessExp2 = new JMenuItem("Chess Exp 2");
        mntmChessExp2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		StdRandom.setSeed(1);
        		int t=100;
        		int steps = t;
        		util.updateClasses("+;-");
        		util.statsICMLA_ini_cons();
        		int minE= 1;
        		int maxE = 11; 
        		int minR= 1;
        		int maxR = 21;
        		
        		boolean rem = true;
        		boolean ob = true;
        		int stack = 15;
        		int obP= 50;
        		
        		if (rem){
        			
        		
        			while (t>0){
        				util.updateMMLvalues(21, 2,  0);
        				frameStatistics.fillTable(f,util.classList);        		
        				util.statsICMLA_runR(g,f,steps-t+1,ob,stack, obP,minE,maxE,minR,maxR);
        				//util.updateDAGdescriptors(f);
        				frameStatistics.updateTable(f,util.classList);
        				t--;
        			}
        			t=100;
        			
        			while (t>0){
        				minE= 11;
                		maxE = 21; 
                		minR= 21;
                		maxR = 41;//41 o 35
        				util.statsICMLA_runR(g,f,steps-t+1,ob,stack, obP,minE,maxE,minR,maxR);
        				//util.updateDAGdescriptors(f);
        				//frame.updateTable(f);
        				t--;
        			}
        			
        		}else{//sin reemplazo
        		
        			 int nE = 10;
            		 int nR = 20;
            	     ArrayList<Integer> arrE = new ArrayList<Integer>(nE); 
            	     ArrayList<Integer> arrR = new ArrayList<Integer>(nR); 
            	    for (int i = 1; i <= nE; i++) {
            	        arrE.add(i);
            	    }
            	    for (int j = 1; j <= nR; j++) {
            	        arrR.add(j);
            	    }
            	    
            	    Collections.shuffle(arrE);
            	    Collections.shuffle(arrR);
            	    
            	  
            	    
            	    while (t>0){
            	    	
        				util.updateMMLvalues(21, 2,  0);
        				frameStatistics.fillTable(f,util.classList);        		
        				 util.statsICMLA_run(g,f,steps-t+1,arrE,arrR,nE,nR,ob,stack, obP);
        				//util.updateDAGdescriptors(f);
        				 frameStatistics.updateTable(f,util.classList);
        				t--;
        			}
            	    
            	    util.iniIndexes();
            	    t=100;
            	    nE = 20;
            		nR = 40;
            		ArrayList<Integer> arrE2 = new ArrayList<Integer>(10); 
            		ArrayList<Integer> arrR2 = new ArrayList<Integer>(20); 
            	    for (int i = 11; i <= nE; i++) {
            	        arrE2.add(i);
            	    }
            	    for (int j = 21; j <= nR; j++) {
            	        arrR2.add(j);
            	    }
            	    
            	    Collections.shuffle(arrE2);
            	    Collections.shuffle(arrR2);
            	    while(t>0){
            	    	
            	    	util.statsICMLA_run(g,f,steps-t+1,arrE2,arrR2,10,20,ob,stack, obP);
        				//util.updateDAGdescriptors(f);
            	    	frameStatistics.updateTable(f,util.classList);
        				t--;
            	    	
            	    }
            	    
        		}
        		
        	}
        });
        mnExamples.add(mntmChessExp2);
        
        JMenuItem mntmChessExp3 = new JMenuItem("Chess Exp 3");
        mntmChessExp3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		int t=500;
        		int steps = t;
        		util.updateClasses("+;-");
        		util.statsICMLA_ini();//genero nodos y vertices entre ellos
        		
        		int minE= 1;
        		int maxE = 42; 
        		int minR= 1;
        		int maxR = 59;
        		
        		boolean rem = true;//hay reemplazo
        		boolean ob = false;
        		
        		
        		
        		
        		util.eraseDAG(f);
        		util.eraseLG(g);  
        		for (int stack = 20; stack <= 20; stack +=10){
        			for (int obP = 25; obP <=25; obP+=25){
        				for (int i = 1; i <= 10; i++){
        					
        					StdRandom.setSeed(1000+i); 
        					util.eraseDAG(f);
        	        		util.eraseLG(g);  	
        					
        	        		while (t>0){
        	        			util.updateMMLvalues(21, 2,  0);
        	        			frameStatistics.fillTable(f,util.classList);        		
            					util.statsICMLA_runR(g,f,steps-t+1,ob,stack, obP,minE,maxE,minR,maxR);
            					//util.updateDAGdescriptors(f);
            					frameStatistics.updateTable(f,util.classList);
            				
            					t--;
        	        		}
        	        		t=500;
        	        		System.out.println("FIN: Stack = "+stack+", Oblivion= "+obP+ ", i= "+ i);
            			
            			
            			
            			 //escritura fichero
        	        		FileWriter fichero = null;
        	        		PrintWriter pw = null;
        	        		try
        	        		{
        	        			fichero = new FileWriter("C:/Users/Nando/Documents/Stats.txt",true);
        	        			pw = new PrintWriter(fichero); 
            	            
        	        			Set<Nodes> vertices = f.vertexSet();
                	    	
        	        			pw.print("Stack = "+stack+", Oblivion= "+obP+ ", i= "+ i + " -> ");
        	        			for (Nodes vertexI : vertices){
        	        				if (vertexI.permanent){
        	        					pw.print(vertexI.id+", ");
        	        				}
        	        			}
        	        			pw.println("");        	            
            	           
            	 
        	        		} catch (Exception e2) {
        	        			e2.printStackTrace();
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
            	        
            	        
            			
            			
        				}
        			}
        			
        		}	
        	}
        });
        mnExamples.add(mntmChessExp3);
        
        JMenuItem mntmIQgerlExp1 = new JMenuItem("Gerl IQ letter Exp 1");
        mntmIQgerlExp1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		int t=500;
        		int steps = t;
        		
        		util.updateClasses("+;-");
        		util.updateClassPred("+");
        		util.statsGerl_ini();//genero nodos y vertices entre ellos
        		
        		int minE= 1;
        		int maxE = 21; 
        		int minR= 1;
        		int maxR = 96;
        		
        		boolean rem = true;//hay reemplazo
        		boolean ob = true;
        		int stack = 50;
        		int obP= 50;
        		StdRandom.setSeed(1000);
        		
        		if (rem){
        			
        			frameStatistics.setVisible(true);
        			frameStatistics.fillTable(f,util.classList);
    				btnStats.setText("Update");
    				stats=true;
 
        			while (t>0){
        				util.updateMMLvalues(9, 1,  0);
        				   		
        				util.statsICMLA_runR(g,f,steps-t+1,ob,stack, obP,minE,maxE,minR,maxR);
        				//util.updateDAGdescriptors(f);
        				frameStatistics.updateTable(f,util.classList);
        				t--;
        			}
        			System.out.println("FIN");
        			
        		}else{//sin reemplazo
        		
        			final int nE = 20;
            		final int nR = 96;
            	    final ArrayList<Integer> arrE = new ArrayList<Integer>(nE); 
            	    final ArrayList<Integer> arrR = new ArrayList<Integer>(nR); 
            	    for (int i = 1; i <= nE; i++) {
            	        arrE.add(i);
            	    }
            	    for (int j = 1; j <= nR; j++) {
            	        arrR.add(j);
            	    }
            	    
            	    Collections.shuffle(arrE);
            	    Collections.shuffle(arrR);
            	    
            	  
            	    frameStatistics.fillTable(f,util.classList);  
            	    while (t>0){
        				util.updateMMLvalues(9, 1,  0);
        				      		
        				 util.statsICMLA_run(g,f,steps-t+1,arrE,arrR,nE,nR,ob,stack, obP);
        				//util.updateDAGdescriptors(f);
        				 frameStatistics.updateTable(f,util.classList);
        				t--;
        			}
            	    
        		}
        	}
        });
        mnExamples.add(mntmIQgerlExp1);
        resize(DEFAULT_SIZE);

       
        
        
        
       

        // positioning via jgraphx layouts
        
        

        // that's all there is to it!...
    }


  
    //Only the example
    void  fillTheGraphsNodes(String [] array){
    	//VERTEX
		 
    	 //String splitID= util.getVertex(g, array[0]);
		//Listeneable	
    	 g.addVertex(array[0]);
		 //grafo con info
    	 Nodes NewNode = new Nodes();
		 NewNode.id= array[0];
		 f.addVertex(NewNode);
		 
		 
		//EDGES
		if (!array[1].equals("")){
			
		
		String[] numbers = array[1].split(";");        			
		//System.out.print(numbers[0]);
		for (String e : numbers)  
           {  
              //grafo listeneable
              g.addEdge(array[0], util.getVertex(g, e));
              //System.out.println(e);  
             
              
              //mi grafo, conseguimos el vertice
              Nodes Temp = util.getVertex(f,e);
              //RulesCov
              NewNode.rulesCov=numbers.length;	
              
              try {
				f.addDagEdge(NewNode, Temp);
				if (Temp.isGround == true){
					if (Temp.cert == 1.0){
						NewNode.posCov+=1;
					}else {if (Temp.cert == -1.0){
						NewNode.negCov+=1;
					}
					}
				}
				
              } catch (CycleFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
           }
     
           }
		}
       //System.out.println(array[4]);
       //IS GROUND?
       if (!array[2].equals("")){
       	if  (Integer.parseInt(array[2]) == 0) {
       		NewNode.isGround=false;
       		//NewNode.cert=util.certNode(f,NewNode);
      		
      		//NewNode.supportPos=util.supportPosNode(f,NewNode);
      		//NewNode.supportNeg=util.supportNegNode(f,NewNode);
      		
       		}else{ if (Integer.parseInt(array[2]) < 0){
       			NewNode.isGround=true;
       			NewNode.cert=-1.0;
       			NewNode.supportPos=0.0;
       			NewNode.supportNeg=-1.0;
       			NewNode.certNeg=-1.0;
       			NewNode.certPos=0.0;
       			NewNode.grnNeg=0.0;
       			NewNode.grnPos=-1.0;
       			}else {
       				NewNode.isGround=true;
       				NewNode.cert=1.0;
       				NewNode.supportPos=1.0;
       				NewNode.supportNeg=0.0;
       				NewNode.certNeg=0;
       				NewNode.certPos=1.0;
       				NewNode.grnNeg=-1.0;
           			NewNode.grnPos=0.0;
       				}
       			}
       		}
       //USABILITY
       //if (!array[4].equals("")){
       	//NewNode.usab=Integer.parseInt(array[4]);
		//}else{
		//	NewNode.usab=util.usabNode(f, NewNode);
		//}
       //NUM C
       if(!array[5].equals("")){
       	NewNode.numC=Integer.parseInt(array[5]);
       }else{
    	   NewNode.numC=0;
       }
       //NUM V
       if(!array[6].equals("")){
       	NewNode.numV=Integer.parseInt(array[6]);
       }else{
    	   NewNode.numV=0;
       }
       //NUM F
       if(!array[7].equals("")){
       	NewNode.numF=Integer.parseInt(array[7]);
       }else{
    	   NewNode.numF=0;
       }
       //IS REC
       if(!array[8].equals("")){
       	NewNode.numF=Integer.parseInt(array[8]);       	
       	if  (Integer.parseInt(array[8]) == 0) {
       		NewNode.isRec=false;	
       	}else{NewNode.isRec=true;}	
		}else{
	    	   NewNode.isRec=false;
	       }
       	     
       //SIZE
        NewNode.size=util.sizeNode(f, NewNode);

       //OPTIMALITY
       // NewNode.optPos=util.optNodePos(f,NewNode);
       // NewNode.optNeg=util.optNodeNeg(f,NewNode);
       	NewNode.opt=util.optNode(f,NewNode);
      
       	//OBLIVION
       	
       	NewNode.oblivion=util.oblivionCriteria(f, NewNode);
       
       	//STRENGTH
       	//NewNode.strength = util.strengthNode(f,NewNode);
       	//RULE
       	NewNode.Rule = array[9];
       	
       	
    }
    
          
    
    
    
    
    



}

