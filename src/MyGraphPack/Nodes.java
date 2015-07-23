package MyGraphPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nodes {

	public String id; 
	
	public int posCov;
	public int PosPath;
	public int negCov;
	public int NegPath;
	public int rulesCov;
	public int rulesCovPath;
	public double cert;
	public double certPos;
	public double certPosFix;
	public double certNeg;
	public double certNegFix;
	public int usab;
	public double size;
	public double sizeRule;
	public double optPos;
	public double optNeg;
	public double opt;
	
	
	//public ArrayList<Double> arrayClass = new ArrayList<Double>();
	//public ArrayList<Double> arraySupport = new ArrayList<Double>();
	//public ArrayList<Double> arrayOpt = new ArrayList<Double>();
	
	
	public Map<String, Double> arrayClass = new HashMap<String, Double>();
	public Map<String, Double> arraySupport = new HashMap<String, Double>();
	public Map<String, Double> arrayOpt = new HashMap<String, Double>();
	public Map<String, Double> arrayResidual = new HashMap<String, Double>();
	
	
	public Map<String, Double> arrayMaxSup = new HashMap<String, Double>();
	public Map<String, Double> arrayMaxRes = new HashMap<String, Double>();

	public Map<String, Double> arrayOblivion = new HashMap<String, Double>();
	
	
	public String clase;
	//public ArrayList<Double> arrayCoverage = new ArrayList<Double>();
	//public ArrayList<Double> arrayCoveragePath = new ArrayList<Double>();
	

	
	
	public double grnPos;
	public double grnNeg;
	
	
	public double supportPos;
	public double supportNeg;
	public double supportMAXPos;
	public double supportMAXNeg;	
	public double supportPosFix=0.0;
	public double supportNegFix=0.0;
	public double supportMAXPosFix=0.0;
	public double supportMAXNegFix=0.0;
	public double confidence;
	public double oblivion;
	public double strength;
	
	public double acc; 
	
	ArrayList<String> arrOb = new ArrayList<String>();
	
	
	
	public boolean permanent = false;
	public int permCount = 0;
	
	public int numC;
	public int numV;
	public int numF;
	public int numCnew;
	public int numVnew;
	public int numFnew;
	
	public boolean isRec;
	public boolean isGround;
	public boolean isLeaf;
	
	public String Rule;
	public String Cov;
	public String PosteriorCov;
	
	//WEB values
	
	public double PR;
	public double PRauxIn=0.0;
	public double PRauxOut=0.0;
	public double aut;
	public double hub;
	
	
	
	public String getId(){
		return id;
		}
	
	public Nodes() {
		// TODO Auto-generated constructor stub
	}
	
	public Nodes(String Myid){
		id = Myid;
	}
	public Nodes(String Myid, int MyposCov, int MynegCov, int MyrulesCov, int MyrulesCovPath, float Mycert, int Myusab, double Mysize, double Myopt, int MynumC, int MynumV, int MynumF, boolean MyisRec, boolean MyisGround ) { 
		
		id=Myid; 
		
		posCov=MyposCov;
		negCov=MynegCov;
		rulesCov=MyrulesCov;
		rulesCovPath=MyrulesCovPath;
		
		cert=Mycert;
		usab=Myusab;
		size=Mysize;
		opt=Myopt;
		
		numC=MynumC;
		numV=MynumV;
		numF=MynumF;
		isRec=MyisRec;
		isGround= MyisGround;
		
		
	}
	
	public double calcOptimallity(Nodes node){
			
		return 0.0;
					
	}
	
	public double calcSize(Nodes node, int c, int v, int f){
		
		return f * (Math.log(node.numF+1)/Math.log(2)) +  c * (Math.log(node.numC+1)/Math.log(2)) + v * (Math.log(node.numV+1)/Math.log(2)); 
		
	}

	
	
	public String toString(){	
		
		return id;
			
		
	}
	
	public void print(){
		
		System.out.println("Id: "+ id + " posCov: " + posCov + " PosPath: "+ PosPath  + 
				" negCov: "+ negCov + " NegPath: "+ NegPath + " rulesCov: "+ rulesCov + 
				" rulesCovPath: "+ rulesCovPath + " cert: "+ cert + " usab: "+ usab + 
				" size: "+ sizeRule + " opt: "+ opt + " numC: "+ numC + " numV: "+ numV + 
				" numF: "+ numF + " isRec: "+ isRec + " isGround: "+ isGround + " Rule: "+ Rule+ 
				" Cov: "+ Cov);
	}

}
