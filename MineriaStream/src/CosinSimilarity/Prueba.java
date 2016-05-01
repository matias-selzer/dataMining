package CosinSimilarity;


public class Prueba {
	
	public static void main(String[] args){
		

		
		
		ArffToMatrix lala = new ArffToMatrix();
		lala.readFromFile("data/Twitter.arff");
		lala.cosineSimilarity();
		lala.mostrarMatrixSimil();
		lala.matrixToArff("data/Simil.arff");
	}
	

	
	
}
