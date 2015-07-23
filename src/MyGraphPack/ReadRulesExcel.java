package MyGraphPack;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadRulesExcel {
	
	
	public static void main(String[] args) {

		
		//System.out.println(System.getProperty("user.dir"));
		//ReadRulesExcel("C:\\Users\\Nando\\workspace\\jgrapht-demo\\ExcelFiles\\PT_Rules.xlsx");		
		
		String id = "r1:";
		String splitID = id.split(":")[0];
		System.out.println(splitID);
		
			System.out.println(id.contains(":"));
		
	}


	public static ArrayList<Nodes> ReadRulesExcel(String File) {
		
		
		// TODO Auto-generated constructor stub
			//File= "PT_Rules.xlsx"
			
			int rule=0;
			//List listA = new ArrayList<String>; //List listB = Arrays.asList("A", "B", "C")
			
			//ArrayList<RulesInf> Myrules =  new ArrayList<RulesInf>();
			ArrayList<Nodes> myRules = new ArrayList<Nodes>();
			
			
			try
		        {
					System.out.println(System.getProperty("user.dir"));
		            FileInputStream file = new FileInputStream(new File(File));
		 
		            //Create Workbook instance holding reference to .xlsx file
		            XSSFWorkbook workbook = new XSSFWorkbook(file);
		 
		            //Get first/desired sheet from the workbook
		            XSSFSheet sheet = workbook.getSheetAt(0);//Playtennis
		 
		            //Iterate through each rows one by one
		            Iterator<Row> rowIterator = sheet.iterator();
		            
		            Row row = rowIterator.next();//titles
		            row= rowIterator.next();//titles2
		            row = rowIterator.next(); //values	
		           
		            
		            //while (rowIterator.hasNext()) 
		            while (row.getCell(0)!= null)
		            {  	
		            	
		                
		                
		                
		                
		                Nodes tempRule = new Nodes();
		                
		                tempRule.id= Integer.toString((int)row.getCell(1).getNumericCellValue());          
		                tempRule.Rule= (row.getCell(2).getStringCellValue());
		                		                
		                tempRule.PosPath = (int)(row.getCell(3).getNumericCellValue());
		                tempRule.posCov = tempRule.PosPath;
		                tempRule.NegPath = (int)(row.getCell(5).getNumericCellValue());
		                tempRule.negCov = tempRule.NegPath;
		                
		                tempRule.rulesCovPath = (int)(row.getCell(7).getNumericCellValue()) + 
		                		(int)(row.getCell(9).getNumericCellValue()) + tempRule.PosPath + tempRule.NegPath;
		                tempRule.rulesCov=tempRule.rulesCovPath;
		                
		                tempRule.numC= (int)(row.getCell(11).getNumericCellValue());
		                tempRule.numV= (int)(row.getCell(12).getNumericCellValue());
		                tempRule.numF= (int)(row.getCell(13).getNumericCellValue());
		                tempRule.isRec= (int)(row.getCell(14).getNumericCellValue()) == 0 ? false : true;
		                tempRule.isGround = ((int)(row.getCell(16).getNumericCellValue()) == 1.0 || (int)(row.getCell(16).getNumericCellValue()) == -1.0) ? true : false;
		                if (tempRule.isGround) {
		                	tempRule.cert = row.getCell(16).getNumericCellValue();
		                }
		                
		                
		               //temp.PosCov= row.getCell(4).getStringCellValue()
		                
		                 
		                
		                String tempPos= row.getCell(4).getStringCellValue();
		                String tempNeg= row.getCell(6).getStringCellValue();
		                String tempRules= row.getCell(8).getStringCellValue();
		                String tempRules2= row.getCell(10).getStringCellValue();
		                
		                tempPos= tempPos.replace("{", "");
		                tempPos= tempPos.replace("}", "");
		                tempNeg= tempNeg.replace("{", "");
		                tempNeg= tempNeg.replace("}", "");
		                tempRules= tempRules.replace("{", "");
		                tempRules= tempRules.replace("}", "");
		                tempRules2= tempRules2.replace("{", "");
		                tempRules2= tempRules2.replace("}", "");

		                
		                
		                String tempTotalCov="";
		                if (!tempPos.equals("")){
		                	tempTotalCov = tempPos.trim();
		                }
		                if (!tempNeg.equals("")){
		                	tempTotalCov = tempTotalCov.concat(";");
		                	tempTotalCov = tempTotalCov.concat(tempNeg.trim());		                
		                }
		                if (!tempRules.equals("")){
		                	tempTotalCov = tempTotalCov.concat(";");
		                	tempTotalCov = tempTotalCov.concat(tempRules.trim());
		               // tempTotalCov = tempTotalCov.concat(",");
		                }
		                if (!tempRules2.equals("")){
		                	tempRule.PosteriorCov=tempRules2.trim();
		                	//tempTotalCov = tempTotalCov.concat(tempRules2.trim());
		                }
		                
		                
		                
		                tempRule.Cov= tempTotalCov;
		                
		                tempRule.print();
		                
		                myRules.add(tempRule);
		                
		                row = rowIterator.next(); //values	
		            }
		            
		               
		            file.close();
		        } 
		        catch (Exception e) 
		        {
		            e.printStackTrace();
		        }
			
			return myRules;
		}
		
		
}


