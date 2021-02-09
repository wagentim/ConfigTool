package de.etas.tef.config.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public final class Utils
{
	public static final Path getCurrentPath()
	{
		return Paths.get(IConstants.EMPTY_STRING).toAbsolutePath();
	}
	
	public static void main(String[] args)
	{
//		System.out.print(Utils.getCurrentPath().toString());
	}
	
	public static String removeSpace(String input)
	{
		if(input == null || input.isEmpty())
		{
			return IConstants.EMPTY_STRING;
		}
		
    	if(input.contains("="))
    	{
    		StringTokenizer st = new StringTokenizer(input, "=");
    		StringBuilder s = new StringBuilder();
    		int total = st.countTokens();
			int counter = 1;
			while (st.hasMoreTokens()) {
				String key = st.nextToken().trim();
				s.append(key);
				if (counter < total) {
					s.append("=");
				}
				counter++;
			}
			
			return s.toString();
    	}
    	
    	return input.trim();
	}
	
	public static boolean isContentSame(String input_1, String input_2)
	{
		if(input_1 == null && input_2 == null)
		{
			return true;
		}
		
		if(input_1.isEmpty() && input_2.isEmpty())
		{
			return true;
		}
		
		if(input_1 == null || input_2 == null)
		{
			return false;
		}
		
		if(input_1.isEmpty() || input_2.isEmpty())
		{
			return false;
		}
		
		return input_1.trim().equalsIgnoreCase(input_2.trim());
	}
}
