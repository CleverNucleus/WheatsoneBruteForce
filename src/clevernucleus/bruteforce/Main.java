package clevernucleus.bruteforce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	private static int size = 4000;
	private static int temp1 = 0;
	private static int temp2 = 100;
	private static int nominalR = 100;
	private static int voltageS = 10;
	
	private static float gain = 0.385F;
	private static float tolerance = 0.01F;
	
	private static double v1 = 0.0D;
	private static double v2 = 0.1D;
	
	public static void main(String[] par0) {
		//forceResistances(size, temp1, temp2, nominalR, voltageS, v1, v2, tolerance, gain);
		System.out.println("Time Elapsed: 1 hour 35 minutes 42 seconds");
	}
	
	/**
	 * 
	 * @param par0 int Size; the number of resistances to try.
	 * @param par1 int Temperature One.
	 * @param par2 int Temperature Two.
	 * @param par3 int RTD's Nominal Resistance.
	 * @param par4 int Voltage Supply.
	 * @param par5 double Desired Voltage for Temperature One.
	 * @param par6 double Desired Voltage for Temperature Two.
	 * @param par7 float Acceptable uncertainty for voltages. 
	 * @param par8 float RTD's Resistance Gain.
	 */
	public static void forceResistances(int par0, int par1, int par2, int par3, int par4, double par5, double par6, float par7, float par8) {
		int[] x = new int[par0];
		int[] y = new int[par0];
		int[] z = new int[par0];
		
		Map<Integer, Double[]> tagsV = new HashMap<>();
		Map<Integer, Integer[]> tagsR = new HashMap<>();
		
		for(int var = 0; var < par0; var++) {
			x[var] = var + 1;
			y[var] = var + 1;
			z[var] = var + 1;
		}
		
		int count = 0;
		
		for(int var0 = 0; var0 < par0; var0++) {
			for(int var1 = 0; var1 < par0; var1++) {
				for(int var2 = 0; var2 < par0; var2++) {
					int varX = x[var0];
					int varY = y[var1];
					int varZ = z[var2];
					
					double volt0 = Helper.wheatstoneVoltOut(par1, par3, par4, varX, varY, varZ, par8);
					double volt1 = Helper.wheatstoneVoltOut(par2, par3, par4, varX, varY, varZ, par8);
					
					if(isWithinLimits(volt0, par5, par7)) {
						if(isWithinLimits(volt1, par6, par7)) {
							tagsV.put(count, new Double[] {volt0, volt1});
							tagsR.put(count, new Integer[] {varX, varY, varZ});
							
							count++;
						}
					}
					
					if(var0 % 200 == 0 && var1 == 0 && var2 == 0) {
						System.out.println("Computing.." + var0);
					}
				}
			}
		}
		
		Map<Double, Double> voltages = new HashMap<>();
		Map<Double, Integer[]> resistances = new HashMap<>();
		
		double[] vout0 = new double[tagsV.size()];
		
		for(int var = 0; var < tagsV.size(); var++) {
			vout0[var] = tagsV.get(var)[0];
			
			voltages.put(tagsV.get(var)[0], tagsV.get(var)[1]);
			resistances.put(tagsV.get(var)[0], tagsR.get(var));
		}
		
		double voltage0 = Helper.closestValue(par5, vout0);
		double voltage1 = voltages.get(voltage0);
		
		int resX = resistances.get(voltage0)[0];
		int resY = resistances.get(voltage0)[1];
		int resZ = resistances.get(voltage0)[2];
		
		System.out.println("Voltage @ " + par1 + " C: " + voltage0);
		System.out.println("Voltage @ " + par2 + " C: " + voltage1);
		System.out.println("R1: " + resX);
		System.out.println("R2: " + resY);
		System.out.println("R3: " + resZ);
	}
	
	/**
	 * Checks to see if the input value par0 is within tolerable limits for value par1.
	 * @param par0 double Value input one.
	 * @param par1 double Value input two.
	 * @param par2 float Uncertainty.
	 * @return boolean Returns true if par0 is within limits of par1.
	 */
	private static boolean isWithinLimits(double par0, double par1, float par2) {
		return par0 >= (par1 - Helper.tolerance(par1, par2)) && par0 <= (par1 + Helper.tolerance(par1, par2));
	}
}
