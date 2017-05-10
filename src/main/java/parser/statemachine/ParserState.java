package parser.statemachine;

import parser.action.Action;
import parser.action.FlushBufferToCode;
import parser.action.FlushBufferToText;
import parser.matcher.AnyExceptBacktickMatcher;
import parser.matcher.BacktickMatcher;
import parser.matcher.CharacterMatcher;

public enum ParserState {

	//@formatter:off
	TEXT_ENTRY( new Transition[] { to("TEXT_ENTRY", withAny()), to("OPENING_1",  withBt()) }),
	OPENING_1(  new Transition[] { to("OPENING_2",  withBt()),  to("TEXT_ENTRY", withAny()) }),
	OPENING_2(  new Transition[] { to("CODE_ENTRY", withBt(), flushToText()),  to("TEXT_ENTRY", withAny()) }),
	CODE_ENTRY( new Transition[] { to("CODE_ENTRY", withAny()), to("CLOSING_1",  withBt()) }),
	CLOSING_1(  new Transition[] { to("CLOSING_2",  withBt()),  to("CODE_ENTRY", withAny()) }),
	CLOSING_2(  new Transition[] { to("TEXT_ENTRY", withBt(), flushToCode()),  to("TEXT_ENTRY", withAny()) });
	//@formatter:on

	private Transition[] transitions;

	public static void init() {
		for (ParserState state : ParserState.values()) {
			for (Transition transition : state.transitions) {
				transition.init();
			}
		}
	}

	ParserState(Transition[] possibleTransitions) {
		transitions = possibleTransitions;
	}

	public Transition getTransitionFor(String character) {
		for (Transition transition : this.transitions) {
			if (transition.getTriggerCharacter().matches(character)) {
				return transition;
			}
		}

		return null;
	}

	private static Transition to(String state, CharacterMatcher matcher) {
		return new Transition(state, matcher);
	}

	private static Transition to(String state, CharacterMatcher matcher, Action action) {
		return new Transition(state, matcher, action);
	}

	private static AnyExceptBacktickMatcher withAny() {
		return new AnyExceptBacktickMatcher();
	}

	private static BacktickMatcher withBt() {
		return new BacktickMatcher();
	}

	private static Action flushToText() {
		return new FlushBufferToText();
	}

	private static Action flushToCode() {
		return new FlushBufferToCode();
	}

}
