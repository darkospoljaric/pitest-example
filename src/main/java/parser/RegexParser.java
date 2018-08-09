package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parses a chat line and extracts code snippets. Supports newlines and various combinations
 *
 * @see RegexParserTest
 * @author dspoljaric
 */
public class RegexParser implements Parser {

	/**
	 * ([^`]+)? = anything except backtick zero or more times -> [^`]+, all just zero or one time<br>
	 * ([^`]+) = anything without backticks one or more times<br>
	 * (.*)? = anything zero or more times<br>
	 */
	private static final String SINGLE_BACKTICK_EXP = "([^`]+)?`([^`]+)`(.*)?";

	/**
	 * ([^`]+)?^ = anything except backtick zero or more times -> [^`]+, all just zero or one time, followed by a
	 * newline<br>
	 * (.*) = anything<br>
	 * (\n[^`]+)? = newline followed by anything except backtick zero or more times, all just zero or one times<br>
	 */
	private static final String TRIPLE_BACKTICK_EXP = "([^`]+)?^```(.*)```(\n[^`]+)?";

	public List<TextMessage> parse(String stringToParse) {
		List<TextMessage> messages = new ArrayList<TextMessage>();
		Matcher matcher = Pattern.compile(TRIPLE_BACKTICK_EXP, Pattern.DOTALL).matcher(stringToParse);

		if (!matcher.matches()) {
			messages.addAll(parseSingleLineSnippets(stringToParse));
			return messages;
		}

		String beforeTripleBacktick = matcher.group(1);
		String code = matcher.group(2);
		String afterTripleBacktick = matcher.group(3);

		messages.addAll(parseSingleLineSnippets(beforeTripleBacktick));

		if (code != null) {
			String codeWithoutNewlines = stripBeginAndEndNewline(code);
			messages.add(new TextMessage(true, codeWithoutNewlines));
		}

		messages.addAll(parseSingleLineSnippets(afterTripleBacktick));

		if (beforeTripleBacktick == null && code == null && afterTripleBacktick == null) {
			messages.addAll(parseSingleLineSnippets(stringToParse));
		}

		return messages;
	}

	private String stripBeginAndEndNewline(String code) {
		String returnValue = code;

		if (returnValue.startsWith("\n")) {
			returnValue = code.substring(1, code.length());
		}

		if (returnValue.endsWith("\n")) {
			returnValue = returnValue.substring(0, returnValue.length() - 1);
		}

		return returnValue;
	}

	private List<TextMessage> parseSingleLineSnippets(String toParse) {
		List<TextMessage> messages = new ArrayList<TextMessage>();
		if (toParse == null) {
			return messages;
		}

		Matcher matcher = Pattern.compile(SINGLE_BACKTICK_EXP, Pattern.DOTALL).matcher(toParse);

		if (!matcher.matches()) {
			messages.add(new TextMessage(false, stripBeginAndEndNewline(toParse)));
			return messages;
		}

		String beforeBacktick = matcher.group(1);
		String code = matcher.group(2);
		String afterBacktick = matcher.group(3);

		if (beforeBacktick != null) {
			messages.add(new TextMessage(false, beforeBacktick));
		}

		if (code != null) {
			messages.add(new TextMessage(true, code));
		}

		// special behavior for last group since anything is allowed in it
		if (afterBacktick != null && !"".equals(afterBacktick)) {
			int numberOfNonEmptyGroups = numberOfNonEmptyGroups(afterBacktick);
			if (numberOfNonEmptyGroups == 0) {
				messages.add(new TextMessage(false, afterBacktick));
			} else {
				messages.addAll(parseSingleLineSnippets(afterBacktick));
			}
		}

		return messages;
	}

	private int numberOfNonEmptyGroups(String toParse) {
		Matcher matcher = Pattern.compile(SINGLE_BACKTICK_EXP).matcher(toParse);

		if (!matcher.matches()) {
			return 0;
		}

		int before = matcher.group(1) != null ? 1 : 0;
		int code = matcher.group(2) != null ? 1 : 0;
		int after = matcher.group(3) != null ? 1 : 0;

		return before + code + after;
	}

}
