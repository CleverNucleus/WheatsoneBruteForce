package clevernucleus.bruteforce;

public class Helper {
	
	/**
	 * Getter. 
	 * @param par0 double Value.
	 * @param par1 float Uncertainty (e.g. 0.1F is +/- 10%).
	 * @return double Returns a double with a tolerance, or just the tolerance if the value is 0.
	 */
	public static double tolerance(double par0, float par1) {
		double var0 = par0 * (double)par1;
		
		return var0 == 0D ? (double) par1 : var0;
	}
	
	/**
	 * Getter. Compares all array values(par1) to a set value(par0) and returns the closest one.
	 * @param par0 double The value being compared.
	 * @param par1 double[] The array of values.
	 * @return double Returns the closest value from par1 to par0.
	 */
	public static double closestValue(double par0, double[] par1) {
		double var0 = Math.abs(par1[0] - par0);
		int var1 = Integer.MAX_VALUE;
		
		for(int var = 1; var < par1.length; var++) {
			double var2 = Math.abs(par1[var] - par0);
			
			if(var2 < var0) {
				var1 = var;
				var0 = var2;
			}
		}
		
		return par1[var1];
	}
	
	/**
	 * Getter. The circuit R1/R2 || R3/Rx.
	 * @param par0 int Temperature in Celsius.
	 * @param par1 int Nominal Resistance (e.g. 100 Ohms).
	 * @param par2 int Supply Voltage (e.g. 5 Volts).
	 * @param par3 int Resistance R1.
	 * @param par4 int Resistance R2.
	 * @param par5 int Resistance R3.
	 * @param par6 float RTD's Resistance/Temperature gain (e.g. 0.5 Ohms/C).
	 * @return double Returns the positive measured voltage from a wheatstone bridge.
	 */
	public static double wheatstoneVoltOut(int par0, int par1, int par2, int par3, int par4, int par5, float par6) {
		double var0 = (double)((par6 * par0) + par1);
		double var1 = (double)(par2 * (((double)par4 / (double)(par4 + par3)) - (var0 / (double)(var0 + par5))));
		
		return var1 < 0 ? (-1D) * var1 : var1;
	}
}
