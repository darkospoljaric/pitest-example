package parser.matcher;

public class AnyExceptBacktickMatcher implements CharacterMatcher {

	@Override
	public boolean matches(String character) {
		return !character.equals("`");
	}

}
