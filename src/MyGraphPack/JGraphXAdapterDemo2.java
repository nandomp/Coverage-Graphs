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

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;




public class JGraphXAdapterDemo2
    extends JFrame

{

	private static final long serialVersionUID = 1L;
	private JTable table_Rules; 
	private DefaultTableModel modelRules = new DefaultTableModel(){    
		
		private static final long serialVersionUID = 1L;

		//@Override
		//public Class getColumnClass(int column) {
		//	return getValueAt(0, column).getClass();
    //}
	};
	
		
		
	public JGraphXAdapterDemo2(  ) {
		
		
		//
		
		//setTitle("metaFLIP");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		setTitle("Statistics");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 720);
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 30, 1339, 213);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		table_Rules = new JTable(modelRules);
		
		table_Rules.setAutoCreateRowSorter(true);	
		scrollPane.setViewportView(table_Rules);
		
	}
	
	



   // private static final long serialVersionUID = 2202072534703043194L;
   // private static final Dimension DEFAULT_SIZE = new Dimension(1024, 600);
    private static JGraphXAdapterDemo2 frame;	


    private JGraphXAdapter<String, DefaultWeightedEdge> jgxAdapter;

    

  

  
    
    public static void main(String [] args)
    {
    	
    	
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//GUIMetaFLIP frame = new GUIMetaFLIP();
					frame = new JGraphXAdapterDemo2();
		
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        	
    	
    	/*JGraphAdapterDemo applet = new JGraphAdapterDemo();
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("Grapht");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);*/
        
        
    
    }



	public void fillTable(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f, ArrayList<String> classList){
		
		//System.out.println(classList.toString());
				
		modelRules.addColumn("ID");		
		modelRules.addColumn("Rule");
		//modelRules.addColumn("PosCov");
		//modelRules.addColumn("NegPos");	
		//modelRules.addColumn("Cov");	
		//modelRules.addColumn("Usab");	
		modelRules.addColumn("Size");
		Iterator<String> it = classList.iterator();
		String c;
		while (it.hasNext()){
			c = it.next();
			modelRules.addColumn("Supp[" + c + "]");
		}
		Iterator<String> it1 = classList.iterator();
		String c1;
		while (it1.hasNext()){
			c1 = it1.next();
			modelRules.addColumn("Res[" + c1 + "]");
		}
		Iterator<String> it2 = classList.iterator();
		String c2;
		while (it2.hasNext()){
			c2 = it2.next();
			modelRules.addColumn("-L[" + c2 + "]");
		}
		Iterator<String> it3 = classList.iterator();
		String c3;
		while (it3.hasNext()){
			c3 = it3.next();
			modelRules.addColumn("Opt[" + c3 + "]");
		}

		modelRules.addColumn("OptMAX");
		
		Iterator<String> it4 = classList.iterator();
		String c4;
		while (it4.hasNext()){
			c4 = it4.next();
			modelRules.addColumn("Perm[" + c4 + "]");
		}
		modelRules.addColumn("PermMAX");
				
		modelRules.addColumn("isConsolidate");
		

		Set<Nodes> vertices = f.vertexSet();
    	
		for(Nodes vertex : vertices){
    		
    		
    		Vector<Object> data = new Vector<Object>();
    		
    		data.add(vertex.id);
    		data.add(vertex.Rule);
    		data.add(vertex.size);
    		
    		Iterator<String> it5 = classList.iterator();
    		String c5;
    		while (it5.hasNext()){
    			c5 = it5.next();
    			data.add(vertex.arraySupport.get(c5));    			
    		}
    		
    		Iterator<String> it5b = classList.iterator();
    		String c5b;
    		while (it5b.hasNext()){
    			c5b = it5b.next();
    			data.add(vertex.arrayResidual.get(c5b));    			
    		}
    		
    		Iterator<String> it6 = classList.iterator();
    		String c6;
    		while (it6.hasNext()){
    			c6 = it6.next();
    			data.add(vertex.arraySupport.get(c6)-vertex.size);    			
    		}
    		
    		Iterator<String> it7 = classList.iterator();
    		String c7;
    		while (it7.hasNext()){
    			c7 = it7.next();
    			data.add(vertex.arrayOpt.get(c7));    			
    		}
    		
    		data.add(vertex.opt);
    		
    		Iterator<String> it8 = classList.iterator();
    		String c8;
    		while (it8.hasNext()){
    			c8 = it8.next();
    			data.add(vertex.arrayOblivion.get(c8));    			
    		}
    		
    		data.add(vertex.oblivion);
    		data.add(vertex.permanent);
    		
    		//modelRules.addRow(new Object[]{vertex.id,vertex.Rule,vertex.size,supP,supN,LP,LN,
    		//		vertex.optPos,vertex.optNeg,vertex.opt,vertex.oblivion,vertex.permanent});
    		
    		//System.out.println(data);
    		modelRules.addRow(data);
    	
    		
    		}
    		
		
	
		 
		 
	}
	
	public void updateTable(DirectedAcyclicGraph<Nodes,DefaultWeightedEdge> f, ArrayList<String> classList){
		
		//System.out.println("---------"+modelRules.getRowCount());
		if (modelRules.getRowCount() > 0) {
		    for (int i = modelRules.getRowCount() - 1; i > -1; i--) {
		    	modelRules.removeRow(i);
		    }
		}
		Set<Nodes> vertices = f.vertexSet();
    
    	
    	for(Nodes vertex : vertices){
    		

    		Vector<Object> data = new Vector<Object>();
    		
    		data.add(vertex.id);
    		data.add(vertex.Rule);
    		data.add(vertex.size);
    		
    		Iterator<String> it5 = classList.iterator();
    		String c5;
    		while (it5.hasNext()){
    			c5 = it5.next();
    			data.add(vertex.arraySupport.get(c5));    			
    		}
    		
    		Iterator<String> it5b = classList.iterator();
    		String c5b;
    		while (it5b.hasNext()){
    			c5b = it5b.next();
    			data.add(vertex.arrayResidual.get(c5b));    			
    		}
    		
    		Iterator<String> it6 = classList.iterator();
    		String c6;
    		while (it6.hasNext()){
    			c6 = it6.next();
    			data.add(vertex.arraySupport.get(c6)-vertex.size);    			
    		}
    		
    		Iterator<String> it7 = classList.iterator();
    		String c7;
    		while (it7.hasNext()){
    			c7 = it7.next();
    			data.add(vertex.arrayOpt.get(c7));    			
    		}
    		
    		data.add(vertex.opt);
    		
    		Iterator<String> it8 = classList.iterator();
    		String c8;
    		while (it8.hasNext()){
    			c8 = it8.next();
    			data.add(vertex.arrayOblivion.get(c8));    			
    		}
    		
    		data.add(vertex.oblivion);
    		data.add(vertex.permanent);
    		
    		//modelRules.addRow(new Object[]{vertex.id,vertex.Rule,vertex.size,supP,supN,LP,LN,
    		//		vertex.optPos,vertex.optNeg,vertex.opt,vertex.oblivion,vertex.permanent});
    		
    		modelRules.addRow(data);
    		
    		}
    		
    	
    		 
		 
	}
	
	
    public void colourMax(){
    	
    }
          
    
    
    
    
    



}


