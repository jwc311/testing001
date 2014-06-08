package util;

import java.util.Arrays;
import java.util.Vector;

public class Statistics {
	
	Vector<Double> arr = new Vector<Double>();
	
	public void reset(){
		arr.clear();
	}
	
	public int size(){
		return arr.size();
	}
	
	public void addValue(double val){
		arr.add(new Double(val));
	}
	
	public double getAverage(){
		
		int size = size();
		double value = this.getSum();

		if( size == 0)
			return 0;
		else
			return value/size;
	}

	public double getSTDEV(){
		double mean = getAverage();
		return getSTDEV(mean);
	}
	
	public double getSTDEV(double mean){
		double stdev = 0;
		int size = arr.size();
		double element = 0;
		for(int i = 0 ; i < size ; i++){
			element = arr.elementAt(i).doubleValue();
			stdev += Math.pow((element - mean), 2);
		}
		if(size == 0)
			return 0;
		else
			return Math.sqrt(stdev/size);
	}
	
	public double getMax(){
		Double d[] = new Double[arr.size()];
		arr.toArray(d);
		Arrays.sort(d);
		
		if(arr.size() >0)
			return d[d.length-1];
		else
			return -1;
	}
	
	public double getMin(){
		Double d[] = new Double[arr.size()];
		arr.toArray(d);
		Arrays.sort(d);
		if(arr.size() >0)
			return d[0];
		else
			return -1;
	}
	
	
	public double getSum(){
		double value = 0;
		int size = arr.size();
		for(int i = 0 ; i < size ; i++){
			value += (arr.elementAt(i)).doubleValue();
		}
		return value;
	}
	
	public String print(String header){
		header += ", Ave="+this.getAverage();
		header += ", SD="+this.getSTDEV();
		header += ", N="+this.size();
		header += ", Min=" + getMin();
		header += ", Max=" + getMax();
		System.out.println(header);
		return header;
	}
	
}
