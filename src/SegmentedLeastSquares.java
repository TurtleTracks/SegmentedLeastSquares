import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SegmentedLeastSquares {
	static ArrayList<int[]> points = new ArrayList<int[]>();
	static double M[][];
	static double E[][];
	static double C = 0;
	// read from stdin, run or whatever. 
	public static void main(String[] args) throws FileNotFoundException {
		
		// Initialize
		C = Float.parseFloat(args[0]);
		File file = new File("test.txt");
		Scanner input = new Scanner(file);
		
		while (input.hasNext()) {
			int p[] = new int[2]; 
			String pstr[] = input.nextLine().split(" ");
			p[0] = Integer.parseInt(pstr[0]);
			p[1] = Integer.parseInt(pstr[1]);
			points.add(p);
		}
		input.close();
		
		M = new double[points.size()][2];
		E = new double[points.size()][points.size()];
		
		fillErrors();
		
		System.out.println("number of segments: " + SegmentedLeastSquares(points));
		
		findSegment(M.length-1);
		
    }
	
	public static int SegmentedLeastSquares(ArrayList<int[]> points)
	{
		for(int j = 0; j < M.length; j++){
			double valij[] = new double[j+1];
			for(int i = 0; i <= j; i++){
				double MI = 0;
				if(i - 1 >= 0) MI = M[i - 1][0];
				valij[i] = E[i][j] + C + MI;
			}
			double ret[] = getMin(valij);
			M[j][0] = ret[0]; 
			M[j][1] = 0;
			if(ret[1] >= 0 )
				M[j][1] = M[(int) ret[1]][1] + 1;
		}
		return (int)M[M.length - 1][1];
	}
	
	public static void findSegment(int j) {
		if(j <= 0) return;
		double min = Double.MAX_VALUE;
		int xmin = -1;
		for(int i = 0; i <= j; i++){
			double MI = 0;
			if((i-1) >= 0) MI = M[i-1][0];
			double val = E[i][j] + C + MI;
			if(val < min){
				min = val;
				xmin = i; 
			}
		}
		for(int i = xmin; i <= j; i++)
			System.out.print("("+points.get(i)[0]+", "+points.get(i)[1]+") ");
		System.out.println();
		findSegment(xmin-1);
	}

	public static void fillErrors()
	{
		for(int j = 0; j < M.length; j++){
			for(int i = 0; i < j; i++){
				double a = 0;
				double b = 0;
				double sumxy = 0;
				double sumx = 0;
				double sumxx = 0;
				double sumy = 0;
				for(int k = i; k <= j; k++){
					int p[] = points.get(k); 
					sumx += p[0];
					sumy += p[1];
					sumxy += p[0] * p[1];
					sumxx += p[0] * p[0];
				}
				double n = j - i + 1; // bug 1 (was here) 
				a = (n * sumxy - sumx * sumy) / (n * sumxx - sumx*sumx);
				b = (sumy - a * sumx) / n;
				
				double sumYdiff = 0;
				for(int k = i; k <= j; k++){
					int p[] = points.get(k);
					double _y = a * p[0] + b; // bug 2 (was here)
					sumYdiff += (_y - p[1]) * (_y - p[1]);
				}
				E[i][j] = sumYdiff;
			}
		}
	}
	
	public static double[] getMin(double[] arr){ 
	    double min = arr[0]; 
	    double idx = -1;
	    for(int i=1;i<arr.length;i++){ 
	      if(arr[i] < min){ 
	    	  min = arr[i];
	    	  idx = i;
	      } 
	    } 
	    return new double[]{min, idx}; 
	  } 
}
