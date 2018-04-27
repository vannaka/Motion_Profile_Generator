package com.mammen.util;

public class Mathf
{
	private Mathf() {
        // Do not instantiate
    }

    /**
     * Rounds the specified value to the specified number of decimal places
     *
     * @param val    the number to round
     * @param places the amonut of decimal places to round to
     * @return number rounded to places
     */
    public static double round(double val, int places) {
        double tens = Math.pow(10.0, places);

        return Math.round(val * tens) / tens;
    }
    
    /**
     * Rounds the specified value to the closest specified multiple
     *
     * @param val    the number to round
     * @param multi the multiple (<1) to round to
     * @return the rounded number
     */
    public static double round( double val, double multi )
    {    	
    	return Math.round( val / multi ) * multi;
    }
}
