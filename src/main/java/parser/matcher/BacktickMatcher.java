package parser.matcher;

public class BacktickMatcher implements CharacterMatcher {

	@Override
	public boolean matches(String character) {
		return "`".equals(character);
	}

}
