/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * This program and the accompanying materials are dual-licensed under
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
/* ----------------------
 * JGraphAdapterDemo.java
 * ----------------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 03-Aug-2003 : Initial revision (BN);
 * 07-Nov-2003 : Adaptation to JGraph 3.0 (BN);
 *
 */
package MyGraphPack;

import java.awt.*;
import java.awt.geom.*;
import java.util.Iterator;

import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;
import org.jgrapht.*;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.ext.*;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.*;
// resolve ambiguity
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;




/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 * @since Aug 3, 2003
 */
public class JGraphAdapterDemo
    extends JApplet
{
	public JGraphAdapterDemo() {
	}
    

    private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(1000, 700);

    

    //
    private JGraphModelAdapter<String, DefaultEdge> jgAdapter;

    

    /**
     * An alternative starting point for this demo, to also allow running this
     * applet as an application.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
    {
        JGraphAdapterDemo applet = new JGraphAdapterDemo();
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraph Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // create a JGraphT graph
       /* ListenableGraph<String, DefaultEdge> g =
            new ListenableDirectedMultigraph<String, DefaultEdge>(
                DefaultEdge.class);*/

    	// Grafo Dirigido Aciclico
    	
             
    	DirectedAcyclicGraph<String, DefaultEdge> g =
                new DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge.class);
    	
        
       
         
    	
        // create a visualization using JGraph, via an adapter
        jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);

        JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        
        
        //Boton para agregar nuevos nodos
        
        JButton btnNewButton = new JButton("Add Node");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String [] array ={"","","",""};
        		JTextField id = new JTextField();
        		 JTextField cov = new JTextField();
        		 JTextField o1 = new JTextField();
        		 JTextField o2 = new JTextField();
        		 Object[] message = {
        		 "ID:", id,
        		 "Edges:", cov,
        		 "Other:", o1,
        		 "Other2:", o2,
        		 };
        		 int option = JOptionPane.showConfirmDialog(null, message, "Llena el formulario", JOptionPane.OK_CANCEL_OPTION);
        		 if(option == JOptionPane.OK_OPTION){
        		 array[0]=id.getText();
        		 array[1]=cov.getText();
        		 array[2]=o1.getText();
        		 array[3]=o2.getText();
        		 
        		 System.out.println(array[1]);
        		 
        		 
        		 }
        	}
        });
        
        
        getContentPane().add(btnNewButton, BorderLayout.NORTH);
        resize(DEFAULT_SIZE);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add some sample data (graph manipulated via JGraphT)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        //g.addEdge(v1, v2);
       // g.addEdge(v2, v3);
        //g.addEdge(v1, v3);
       // g.addEdge(v4, v3);

        // position vertices nicely within JGraph component
        positionVertexAt(v1, 10, 600);
        positionVertexAt(v2, 110, 600);
        positionVertexAt(v3, 210, 600);
        positionVertexAt(v4, 310, 600);

     
        
        // that's all there is to it!...
    }

    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked") // FIXME hb 28-nov-05: See FIXME below
    private void positionVertexAt(Object vertex, int x, int y)
    {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds =
            new Rectangle2D.Double(
                x,
                y,
                bounds.getWidth(),
                bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        // TODO: Clean up generics once JGraph goes generic
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }

    

    /**
     * a listenable directed multigraph that allows loops and parallel edges.
     */
    private static class ListenableDirectedMultigraph<V, E>
        extends DefaultListenableGraph<V, E>
        implements DirectedGraph<V, E>
    {
        private static final long serialVersionUID = 1L;

        ListenableDirectedMultigraph(Class<E> edgeClass)
        {
            super(new DirectedMultigraph<V, E>(edgeClass));
        }
    }
    
    
    
    
    
    private static class ListenableDirectedAcyclic<V, E>
    extends DefaultListenableGraph<V, E>
    implements DirectedGraph<V, E>
{
    
	private static final long serialVersionUID = 1L;

	ListenableDirectedAcyclic(Class<E> edgeClass)
    {
        super(new DirectedAcyclicGraph<V, E>(edgeClass));
    }
}
    
    
 
    public class Nodes {

    	public int id; 
    	
    	public int posCov;
    	public int negCov;
    	public float cert;
    	public float usab;
    	public float size;
    	public float opt;
    	
    	public int numC;
    	public int numV;
    	public int numF;
    	public int isRec;
    	
    	
    	
    	
    	public Nodes() {
    		// TODO Auto-generated constructor stub
    	}
    	public Nodes(int Myid, int MyposCov, int MynegCov, float Mycert, float Myusab, float Mysize, float Myopt, int MynumC, int MynumV, int MynumF, int MyisRec ) { 
    		
    		id=Myid; 
    		
    		posCov=MyposCov;
    		negCov=MynegCov;
    		cert=Mycert;
    		usab=Myusab;
    		size=Mysize;
    		opt=Myopt;
    		
    		numC=MynumC;
    		numV=MynumV;
    		numF=MynumF;
    		isRec=MyisRec;
    		
    		
    	}

    }
   
}

// End JGraphAdapterDemo.java
