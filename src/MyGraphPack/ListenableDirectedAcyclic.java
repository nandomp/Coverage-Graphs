package MyGraphPack;
import com.mxgraph.layout.*;
import com.mxgraph.swing.*;
import com.mxgraph.view.mxGraph;

import java.awt.*;

import javax.swing.*;

import org.jgrapht.*;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;




public class ListenableDirectedAcyclic<V, E> extends DefaultListenableGraph<V, E>
implements DirectedGraph<V, E> 
{

	private static final long serialVersionUID = 1L;

	ListenableDirectedAcyclic(Class<E> edgeClass)
	{
	    super(new DirectedAcyclicGraph<V, E>(edgeClass));
	}
}



