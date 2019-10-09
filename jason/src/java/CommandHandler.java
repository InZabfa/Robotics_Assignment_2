import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Containers.ObjectContainer;

public class CommandHandler {
	private ObjectContainer container;

	public CommandHandler(ObjectContainer container) {
		this.container = container;
	}

	/**
	 * Gets the object container.
	 * 
	 * @return ObjectContainer
	 */
	public ObjectContainer GetObjectContainer() {
		return this.container;
	}

	/**
	 * Returns command with args as a string array
	 * 
	 * [0] - cmd [1] - property to update [2] - property value.
	 * 
	 * @param input
	 * @return string[3]
	 */
	public String[] GetCommands(String input) {
		String regex_cmd = "^(\\w+)[ ](\\w+)[ ](\\w+)";
		final Pattern pattern = Pattern.compile(regex_cmd);
		final Matcher matcher = pattern.matcher(input);

		String[] results = new String[3];

		while (matcher.find()) {
			results[0] = matcher.group(1);
			results[1] = matcher.group(2);
			results[2] = matcher.group(3);
		}

		return results;
	}

	/**
	 * Handles commands and property updates from the bot.
	 * 
	 * @param input
	 */
	public void Handle(String input) {
		String[] args = GetCommands(input);
		if (args[0] != null)
			switch (args[0].toLowerCase()) {
			case "update":
				GetObjectContainer().GetStoredValues().put(args[1], args[2]);
				break;
			}
	}
}
