import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class trader {

	public static void main(String[] args) {
		String input_file = args[0];
		double average = 0.0;
		double total = 0.0;
		int DAYS_FOR_AVERAGE = 30;
		ArrayList<Double> prices = new ArrayList<>();
		int line_no = 0;
		double divedend = 0;
		int stocks_on_hand = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(input_file))) {
			File fout = new File("out.csv");
			FileOutputStream fos = new FileOutputStream(fout);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
		 
			for(String line; (line = br.readLine()) != null; ) {
		        int comma_index = line.indexOf(',');
		        if (comma_index != -1) {
		        	String date = line.substring(0, comma_index);
		        	try {
		        		double price = Double.parseDouble(line.substring(comma_index + 1));
		        		prices.add(price);
		        		if (prices.size() > DAYS_FOR_AVERAGE) {
		        			prices.remove(0);
		        			average = sum(prices) / (double) DAYS_FOR_AVERAGE;
		        			if (price > average && stocks_on_hand > 0) {
		        				osw.write(date + ", -100");
		        				stocks_on_hand -= 100;
		        			} else if (price < average && stocks_on_hand < 1000) {
		        				osw.write(date + ", 100");
		        				stocks_on_hand += 100;
		        			}
		        		} else {
		        			osw.write(date + ", 0");
		        		}
		        	} catch (Exception e) {
		        		System.out.println("Ignoring input on line number: " + line_no);
		        	}
		        }
		        ++line_no;
		    }
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e.getMessage());
		}
		
	}

	public static double sum(ArrayList<Double> prices) {
		double sum = 0.0;
		for (double price: prices) {
			sum += price;
		}
		return sum;
	}
}
