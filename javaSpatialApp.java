package javaSpatialApp;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class javaSpatialApp {

private Connection c;
public javaSpatialApp(){
createdatabaseconnection();
}
private void createdatabaseconnection() {  //db connection function
try{
 	Driver orcl = new oracle.jdbc.OracleDriver();
	DriverManager.registerDriver(orcl);
	c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:kinikar","system","Vaibhav92");
	System.out.println("Connected to Database");   	
   }
catch(Exception e){
	System.out.println("Error while connecting to Database :" + e.getMessage());
	}
}
public static void main(String[] args) {
javaSpatialApp db = new javaSpatialApp();
if(args.length == 0)
System.out.println("NO ARGUMENTS WERE ENTERED");
else if(args.length != 2 && args.length != 3 && args.length != 4 && args.length != 6)
System.out.println("INVALID NUMBER OF ARGUMENTS");
else{   //check query-type and call respective functions
String qtype = args[0].toString();		
     if(qtype.equals("window")){db.windowexecute(args);}
	else if(qtype.equals("within")){db.withinexecute(args);}
	else if(qtype.equals("nearest-neighbor")){db.nearestneighborexecute(args);}
	else if(qtype.equals("fixed")){db.fixedexecute(args);}
	else
	System.out.println("PLEASE CHECK THE COMMAND FOR CORRECT SPELLINGS OR VALID QUERY-TYPE");
    }
}
public void windowexecute(String[] args){
	String query[] = new String[10];		
     for(int i=0;i<query.length;i++) query[i]=null;
	 //query construction
	if(args[1].equals("student")){
	query[0]="SELECT A.STUDENT_ID AS S_ID FROM STUDENTS A WHERE(SDO_INSIDE(A.STUDENT_POINTS,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),MDSYS.SDO_ORDINATE_ARRAY("+args[2]+","+args[3]+","+args[4]+","+args[5]+")))='TRUE' ) ORDER BY A.STUDENT_ID";
   	query[1]="S_ID";
    }	
	if(args[1].equals("building")){
	query[0]="SELECT A.BUILDING_ID AS B_ID FROM BUILDING A WHERE(SDO_INSIDE(A.LOCATION_POINTS,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),MDSYS.SDO_ORDINATE_ARRAY("+args[2]+","+args[3]+","+args[4]+","+args[5]+")))='TRUE' ) ORDER BY A.BUILDING_ID";
   	query[1]="B_ID";
    }	
	if(args[1].equals("tramstops")){
	query[0]="SELECT A.STOP_NAME FROM TRAMSTOPS A WHERE(SDO_INSIDE(A.TRAM_POINTS,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),MDSYS.SDO_ORDINATE_ARRAY("+args[2]+","+args[3]+","+args[4]+","+args[5]+")))='TRUE' )";
    query[1]="STOP_NAME";
    }		
	output(query);
}
public void withinexecute(String[] args){
	String query[] = new String[10];		
    for(int i=0;i<query.length;i++) query[i]=null;
	//query construction
	query[0]="SELECT  A.BUILDING_ID AS ID,'B_id' FROM BUILDING A,STUDENTS S WHERE SDO_WITHIN_DISTANCE(A.LOCATION_POINTS,S.STUDENT_POINTS,'DISTANCE="+Integer.valueOf(args[2]).intValue()+"')='TRUE' AND S.STUDENT_ID='"+args[1]+"'"+" UNION "+"SELECT  T.STOP_NAME ,'Tram Stop' FROM TRAMSTOPS T,STUDENTS C WHERE SDO_WITHIN_DISTANCE(T.TRAM_POINTS,C.STUDENT_POINTS,'DISTANCE="+Integer.valueOf(args[2]).intValue()+"')='TRUE' AND C.STUDENT_ID='"+args[1]+"'";
	query[1]="ID";
	query[2]="B_id";
	output(query);
}
public void nearestneighborexecute(String[] args){
	String query[] = new String[10];		
	int x=Integer.valueOf(args[3]).intValue()+1;
    for(int i=0;i<query.length;i++) query[i]=null;
	//query construction
	if(args[1].equals("building")){
		
	query[0]="SELECT B1.BUILDING_ID AS B_ID FROM BUILDING B1 WHERE B1.BUILDING_ID<>'"+args[2]+"' AND SDO_NN(B1.LOCATION_POINTS,(SELECT B2.LOCATION_POINTS FROM BUILDING B2 WHERE B2.BUILDING_ID='"+args[2]+"'),'SDO_NUM_RES="+x+"')='TRUE'";
   	query[1]="B_ID";
    }
	if(args[1].equals("student")){
    query[0]="SELECT S1.STUDENT_ID AS S_ID FROM STUDENTS S1 WHERE S1.STUDENT_ID<>'"+args[2]+"' AND SDO_NN(S1.STUDENT_POINTS,(SELECT S2.STUDENT_POINTS FROM STUDENTS S2 WHERE S2.STUDENT_ID='"+args[2]+"'),'SDO_NUM_RES="+x+"')='TRUE'";    
	query[1]="S_ID";
	}
    if(args[1].equals("tramstops")){
    query[0]="SELECT T1.STOP_NAME AS FROM TRAMSTOPS T1 WHERE T1.STOP_NAME<>'"+args[2]+"' AND SDO_NN(T1.TRAM_POINTS,(SELECT T2.TRAM_POINTS FROM TRAMSTOPS T2 WHERE T2.STOP_NAME='"+args[2]+"'),'SDO_NUM_RES="+x+"')='TRUE'";	
    query[1]="STOP_NAME";
    }
	output(query);
}
public void fixedexecute(String[] args){
    String query[] = new String[10];		
    for(int i=0;i<query.length;i++) query[i]=null;
    int testnum = Integer.valueOf(args[1]).intValue();
    if(testnum < 0 || testnum > 5){
     System.out.println("INVALID FIXED NUMBER");
    }
	else{
	//query construction
	if(testnum == 1){
		query[0]="(SELECT S1.STUDENT_ID AS ID,'S_id' FROM STUDENTS S1,TRAMSTOPS T1 WHERE SDO_WITHIN_DISTANCE(S1.STUDENT_POINTS,T1.TRAM_POINTS,'DISTANCE=70')='TRUE' AND T1.STOP_NAME='T2OHE' UNION SELECT B1.BUILDING_ID ,'B_ID' FROM BUILDING B1,TRAMSTOPS T2 WHERE SDO_WITHIN_DISTANCE(B1.LOCATION_POINTS,T2.TRAM_POINTS,'DISTANCE=70')='TRUE' AND T2.STOP_NAME='T2OHE') INTERSECT (SELECT  S2.STUDENT_ID AS ID,'S_id'FROM STUDENTS S2,TRAMSTOPS T3 WHERE SDO_WITHIN_DISTANCE(S2.STUDENT_POINTS,T3.TRAM_POINTS,'DISTANCE=50')='TRUE'AND T3.STOP_NAME='T6SSL' UNION SELECT B2.BUILDING_ID ,'B_ID' FROM BUILDING B2,TRAMSTOPS T4 WHERE SDO_WITHIN_DISTANCE(B2.LOCATION_POINTS,T4.TRAM_POINTS,'DISTANCE=50')='TRUE' and T4.STOP_NAME='T6SSL')";
		query[1]="ID";
		query[2]="S_id";
	}	
	else if	(testnum == 2){
        query[0]="SELECT S.STUDENT_ID,T.STOP_NAME FROM STUDENTS S,TRAMSTOPS T WHERE SDO_NN(T.TRAM_POINTS,S.STUDENT_POINTS,'SDO_NUM_RES=2')='TRUE'";		
		query[1]="STUDENT_ID";
		query[2]="STOP_NAME";
	}
	else if (testnum == 3){
		query[0]="SELECT T.STOP_NAME,COUNT(*) AS COUNT FROM TRAMSTOPS T, BUILDING B WHERE SDO_WITHIN_DISTANCE(T.TRAM_POINTS,B.LOCATION_POINTS,'DISTANCE=250')='TRUE' GROUP BY T.STOP_NAME HAVING COUNT(*) = (SELECT MAX(COUNT(*)) FROM TRAMSTOPS T , BUILDING B WHERE SDO_WITHIN_DISTANCE(T.TRAM_POINTS,B.LOCATION_POINTS,'DISTANCE=250')='TRUE' GROUP BY T.STOP_NAME)";		
		query[1]="STOP_NAME";
		query[2]="COUNT";
	}	
	else if (testnum == 4){	
        query[0]="SELECT * FROM(SELECT S.STUDENT_ID,COUNT(*) AS COUNT FROM STUDENTS S,BUILDING B WHERE SDO_NN(S.STUDENT_POINTS,B.LOCATION_POINTS,'SDO_NUM_RES=1')='TRUE' GROUP BY S.STUDENT_ID ORDER BY COUNT(*) DESC) WHERE ROWNUM < 6";		
		query[1]="STUDENT_ID";
		query[2]="COUNT";
	}	
	else if (testnum == 5){
        query[0]="WITH MBR AS(SELECT ROWNUM SEQ,COLUMN_VALUE COORD FROM TABLE (SELECT SDO_AGGR_MBR(LOCATION_POINTS).SDO_ORDINATES FROM BUILDING WHERE BUILDING_NAME LIKE 'SS%')) SELECT (SELECT COORD X1 FROM MBR WHERE SEQ = 1) X1 ,(SELECT COORD Y1 FROM MBR WHERE SEQ = 2) Y1 ,(SELECT COORD X2 FROM MBR WHERE SEQ = 3) X2 ,(SELECT COORD Y2 FROM MBR WHERE SEQ = 4) Y2 FROM DUAL";		
		query[1]="X1";
		query[2]="Y1";
		query[3]="X2";
		query[4]="Y2";
	}	
	else	
       	System.out.println("INVALID FIXED NUMBER");
	}
	output(query);
}
public void output(String[] query){
Statement st=null;
try {
	st = c.createStatement();
	int cnt=0;
   	ResultSet rs = st.executeQuery(query[0]);
   	//print headers
	String h="";
	for(int i=1;i<query.length;i++){if(query[i]!=null) h+=query[i]+"  ";}		
	System.out.println(h);		
	while(rs.next()){
	cnt++;
	String c1=null;
	String c2=null;
	String c3=null;
	String c4=null;
	String out="";
	String space="";
	if(query[1]!=null) c1 = rs.getString(query[1]);
	if(query[2]!=null) c2 = rs.getString(query[2]);
	if(query[3]!=null) c3 = rs.getString(query[3]);
	if(query[4]!=null) c4 = rs.getString(query[4]);	
	//print data
    if(c1 !=null){space = getspace(query[1],c1); out=c1+space;}	
	if(c2 !=null){space = getspace(query[2],c2); out+=c2+space;}	
	if(c3 !=null){space = getspace(query[3],c3); out+=c3+space;}	
	if(c4 !=null){space = getspace(query[4],c4); out+=c4+space;}
	System.out.println(out);
   	}
	if(cnt == 0)
		System.out.println("NO ROWS PRESENT");
	} catch (SQLException e) {
		e.printStackTrace();
	}
finally{
	try {
		st.close();
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
}
}
public String getspace(String q,String value){
String space="  ";
	if(q.length() > value.length()){
		int d = q.length() - value.length();
		for(int i=0;i<=d;i++)
			space+=" ";
	}
	return space;
}

}

	
	
