package com.rpg2014.spiderman.parsers;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.AbstractMap;

import com.rpg2014.spiderman.GroupMe.GroupMeCallback;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.SpidermanCommand;

public class SpidermanParser {

	private static SpidermanLogger logger = SpidermanLogger.getInstance();

	public static SpidermanCommand parseCommand(final String commandString) {

		SpidermanCommand retCommand = null;
		try {
		retCommand = SpidermanCommand.valueOf(commandString);
		}catch (IllegalArgumentException e) {
			retCommand = SpidermanCommand.HELP;
		}
		logger.logInfo("Found command: " + retCommand.name(), SpidermanParser.class.getSimpleName());
		
		return retCommand;
	}

	public static Map.Entry<SpidermanCommand, List<String>> getCommandEntry(final GroupMeCallback callback) {
		String[] commandArr = callback.getText().toLowerCase().replace("@spiderman", "").trim().split(":");// );

		// removes the : + replaces spaces with underscore
		
		SpidermanCommand command = parseCommand(
				commandArr[0].toUpperCase().replace(":", "").trim().replaceAll(" ", "_"));
		List<String> argList = new ArrayList<>();
		if (commandArr.length > 1) {
			String[] args = commandArr[1].split(",");
			logger.logInfo("args = " + commandArr[1], SpidermanParser.class.getSimpleName());
			
			for (int i = 0; i < args.length; i++) {
				argList.add(args[i].trim());
				// logger.logDebug("Found argument: " +args[i],
				// SpidermanParser.class.getSimpleName());
			}
		}

		Map.Entry<SpidermanCommand, List<String>> commandEntry = new AbstractMap.SimpleEntry<SpidermanCommand, List<String>>(
				command, argList);

		return commandEntry;

	}
}