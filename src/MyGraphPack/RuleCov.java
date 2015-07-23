package MyGraphPack;

public class RuleCov {

	
		public String id = "";
		public String rule ="";
		public String cov = "";
		int C=0;
		int NC = 0;
		int V=0;
		int NV=0;
		int F=0;
		int NF = 0;


RuleCov(String myId, String myRule, String myCov,  int numC, int numCnew, 
		int numV, int numVnew , int numF, int numFnew){
	id = myId;
	rule = myRule;
	cov = myCov;
	C=numC;
	 NC = numCnew;
	 V=numV;
	 NV=numVnew;
	 F=numF;
	 NF = numFnew;
}



}