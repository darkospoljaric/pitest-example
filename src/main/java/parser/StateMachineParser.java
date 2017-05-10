package parser;

import java.util.List;

import parser.action.Action;
import parser.statemachine.ParserState;
import parser.statemachine.Transition;

public class StateMachineParser implements Parser {

	// initial parser state
	private ParserState state;

	public StateMachineParser() {
		ParserState.init();
		state = ParserState.TEXT_ENTRY;
	}

	public List<TextMessage> parse(String stringToParse) {
		ParserData parserData = new ParserData();
		if (stringToParse == null || stringToParse.length() == 0) {
			return parserData.getMessages();
		}

		String localCopy = new String(stringToParse);
		while (localCopy.length() > 0) {
			String nextChar = localCopy.substring(0, 1);
			localCopy = localCopy.substring(1, localCopy.length());

			Transition transition = state.getTransitionFor(nextChar);

			for (Action action : transition.getActions()) {
				action.doAction(nextChar, parserData);
			}

			state = transition.getNextState();

			if (localCopy.length() == 0 && parserData.getBuffer().toString().length() > 0) {
				handleEnd(parserData);
			}
		}

		return parserData.getMessages();
	}

	private void handleEnd(ParserData parserData) {
		List<TextMessage> messages = parserData.getMessages();
		if (messages.isEmpty()) {
			TextMessage textMessage = new TextMessage(false, parserData.getBuffer().toString());
			messages.add(textMessage);
			return;
		}

		TextMessage lastMessage = messages.get(messages.size() - 1);
		if (!lastMessage.isCode()) {
			lastMessage.setMessage(lastMessage.getMessage() + parserData.getBuffer().toString());
		} else {
			TextMessage textMessage = new TextMessage(false, parserData.getBuffer().toString());
			messages.add(textMessage);
		}
	}

}
