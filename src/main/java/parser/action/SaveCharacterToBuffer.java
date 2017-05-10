package parser.action;

import parser.ParserData;

public class SaveCharacterToBuffer implements Action {

	@Override
	public void doAction(String character, ParserData parserData) {
		parserData.getBuffer().append(character);
	}

}
