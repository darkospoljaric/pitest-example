package parser;

import java.util.List;

public interface Parser {

	List<TextMessage> parse(String stringToParse);
}
