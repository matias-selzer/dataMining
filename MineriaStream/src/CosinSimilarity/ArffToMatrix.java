package CosinSimilarity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ArffToMatrix {

	boolean listo=false;
	int cols=0;
	double[][] matrix;
	double[][] simil;
	ArrayList<String> ans= new ArrayList<String>();
	
	public void readFromFile(String file){
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; !listo &&(line = br.readLine()) != null; ) {
		        if(line.equals("@data"))listo=true;
		        cols++;
		    }
		    cols-=4;
		    for(String line; (line = br.readLine()) != null; ) {
		    	ans.add(line);
		    }
		    // line is not visible here.
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		matrix= new double[ans.size()][cols];
		simil= new double[ans.size()][ans.size()];
		
		int pos=0;
		for (String fila: ans) {
			procesarLinea(fila,pos);
			pos++;
		}
	}
	
	
	
	public void procesarLinea(String line, int pos){
		line=line.substring(1, line.length()-1);
		//System.out.println(line);
		String[] linea= line.split(",");
		for(int i=0;i<linea.length;i++){
			int indice=Integer.parseInt(linea[i].split(" ")[0]);
			double val = Double.parseDouble(linea[i].split(" ")[1]);
			
			matrix[pos][indice]=val;
		}
		
	}
	
	public void mostrarMatrix(){
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[0].length;j++){
				System.out.print(matrix[i][j]+" - ");
			}
			System.out.println();
		}
	}
	
	public void mostrarMatrixSimil(){
		for(int i=0;i<simil.length;i++){
			for(int j=0;j<simil[0].length;j++){
				System.out.print(simil[i][j]+" - ");
			}
			System.out.println();
		}
	}
	
	
	public void cosineSimilarity(){
		int t= simil.length;
		
		//para cada elemento de la matriz, recordar que solo debe hacerse la mitad
		for(int j=0;j<t;j++){
			for(int k=0;k<t;k++){
				//veo la similitud entre la fila j y la fila k
				double valor= Math.floor(calcular(j,k) * 100) / 100 ;
				simil[j][k]=valor;
				simil[k][j]=valor;
			}
		}
	}
	
	public double calcular(int j,int k){
		int t= simil.length;
		double salida=0;
		double numerador=0;
		double den1=0;
		double den2=0;
		double den;
		
		for(int i=0;i<t;i++){
			numerador+=(matrix[i][j]*matrix[i][k]);
		}
		
		for(int i=0;i<t;i++){
			den1+=(Math.pow(matrix[i][j],2));
		}
		den1=Math.sqrt(den1);
		
		for(int i=0;i<t;i++){
			den2+=(Math.pow(matrix[i][k],2));
		}
		den2=Math.sqrt(den2);
		
		den=den1*den2;
		
		salida=numerador/den;
		
		return salida;
	}
	
	
	public void matrixToArff(String file){
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println("@relation cosineSimilarity");
			writer.println();
			
			for(int i=0;i<simil.length;i++){
				writer.println("@attribute t"+i+" numeric");
			}
			writer.println();
			
			writer.println("@data");
			
			for(int i=0;i<simil.length;i++){
				for(int j=0;j<simil.length;j++){
					writer.print(simil[i][j]);
					if(j<simil.length-1)writer.print(", ");
				}
				writer.println();
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
