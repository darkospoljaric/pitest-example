package parser.statemachine;

import java.util.ArrayList;
import java.util.List;

import parser.action.Action;
import parser.action.SaveCharacterToBuffer;
import parser.matcher.CharacterMatcher;

public final class Transition {
	private ParserState nextState;
	private String nextStateString;
	private CharacterMatcher triggerCharacter;
	private List<Action> actions;

	public Transition(String nextState, CharacterMatcher matcher) {
		this.nextStateString = nextState;
		this.triggerCharacter = matcher;
		this.actions = new ArrayList<Action>();
		// default action
		actions.add(new SaveCharacterToBuffer());
	}

	public Transition(String nextState, CharacterMatcher matcher, Action action) {
		this(nextState, matcher);
		this.actions.add(action);
	}

	public List<Action> getActions() {
		return actions;
	}

	public ParserState getNextState() {
		return nextState;
	}

	public void setNextState(ParserState nextState) {
		this.nextState = nextState;
	}

	public CharacterMatcher getTriggerCharacter() {
		return triggerCharacter;
	}

	public void setTriggerCharacter(CharacterMatcher triggerCharacter) {
		this.triggerCharacter = triggerCharacter;
	}

	public void init() {
		this.nextState = ParserState.valueOf(nextStateString);
	}

}
