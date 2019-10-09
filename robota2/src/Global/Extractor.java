package Global;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Interfaces.Pair;

public class Extractor {
	
	public Extractor() { }
	
	/**
	 * Method used to extract the command given by the server command string
	 * @param cmd
	 * @return command name
	 */
	public String ExtractCommand(String cmd) {
		final Pattern pattern = Pattern.compile(Properties.Regex.COMMAND_NAME, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(cmd);
		while (matcher.find()) { return matcher.group(0); }
		return Properties.EMPTY_STRING;
	}
	
	
	/***
	 * Method used to extract arguments from a server command. 
	 * For instance:
	 * 		Rotate left=1 right=2
	 * 			[left] - 1
	 * 			[right] - 2
	 * @param cmd - command string
	 * @return Array of {@link Interfaces.Pair} of argument strings with value of argument.
	 */
	public ArrayList<Pair> ExtractArguments(String cmd) {
		final Pattern pattern = Pattern.compile(Properties.Regex.CAPTURE_ARGS, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(cmd);

		ArrayList<Pair> pairs = new ArrayList<Pair>();
		
		while (matcher.find())
				pairs.add(new Pair(matcher.group(1), matcher.group(2)));
		
		return pairs;
	}
}
