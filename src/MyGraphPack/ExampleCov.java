package MyGraphPack;

public class ExampleCov {
	
	public String id = "";
	public String rule ="";
	//public String cov = "";
	int C=0;
	int NC = 0;
	int V=0;
	int NV=0;
	int F=0;
	int NF = 0;
	String clase = "";
	


	ExampleCov(String myId, String myRule,  int numC, int numCnew, 
			int numV, int numVnew , int numF, int numFnew, String cl){
		 id = myId;
		 rule = myRule;
		 C=numC;
		 NC = numCnew;
		 V=numV;
		 NV=numVnew;
		 F=numF;
		 NF = numFnew;
		 clase = cl;
		
	}
}                                                                                                
