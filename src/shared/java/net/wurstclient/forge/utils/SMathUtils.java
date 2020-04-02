package net.wurstclient.forge.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SMathUtils {
	 public static double roundToPlace(double value, int places)
	    {
	        if (places < 0)
	        {
	            throw new IllegalArgumentException();
	        }

	        BigDecimal bd = new BigDecimal(value);
	        bd = bd.setScale(places, RoundingMode.HALF_UP);
	        return bd.doubleValue();
	    }

	    public static int randomize(int max, int min)
	    {
	        return -min + (int)(Math.random() * ((max - (-min)) + 1));
	    }

	    public static double getIncremental(double val, double inc)
	    {
	        double one = 1 / inc;
	        return Math.round(val * one) / one;
	    }

	    public static boolean isInteger(String s)
		{
			try
			{
				Integer.parseInt(s);
				return true;
				
			}catch(NumberFormatException e)
			{
				return false;
			}
		}
		
		public static boolean isDouble(String s)
		{
			try
			{
				Double.parseDouble(s);
				return true;
				
			}catch(NumberFormatException e)
			{
				return false;
			}
		}
		
		public static int floor(float value)
		{
			int i = (int)value;
			return value < i ? i - 1 : i;
		}
		
		public static int floor(double value)
		{
			int i = (int)value;
			return value < i ? i - 1 : i;
		}
		
		public static int clamp(int num, int min, int max)
		{
			return num < min ? min : num > max ? max : num;
		}
		
		public static float clamp(float num, float min, float max)
		{
			return num < min ? min : num > max ? max : num;
		}
		
		public static double clamp(double num, double min, double max)
		{
			return num < min ? min : num > max ? max : num;
		}
	    
	    public static boolean isInteger(Double variable)
	    {
	        return (variable == Math.floor(variable)) && !Double.isInfinite(variable);
	    }
	}

