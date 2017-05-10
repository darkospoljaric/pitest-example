package parser;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import parser.Parser;
import parser.StateMachineParser;
import parser.TextMessage;

public class StateMachineParserTest {

	@Test
    public void testParseWithEmptyString() {
        Parser parser = new StateMachineParser();
        List<TextMessage> messages = parser.parse("");
        Assert.assertTrue(messages.isEmpty());
    }

    @Test
    public void testParseNull() {
        Parser parser = new StateMachineParser();
        List<TextMessage> messages = parser.parse(null);
        Assert.assertTrue(messages.isEmpty());
    }

    @Test
    public void testStringWithNoCode() {
        Parser parser = new StateMachineParser();
        List<TextMessage> messages = parser.parse("asdf");
        Assert.assertEquals(1, messages.size());
        Assert.assertFalse(messages.get(0).isCode());
    }

    @Test
    public void testStringWithOneCodeSnippetOnly() {
        Parser parser = new StateMachineParser();
        String snipetText = "asdf";
        List<TextMessage> messages = parser.parse("```" + snipetText + "```");

        Assert.assertEquals(1, messages.size());
        Assert.assertTrue(messages.get(0).isCode());
        Assert.assertEquals(snipetText, messages.get(0).getMessage());
    }

    @Test
    public void testStringWithOneSnippetOpeningSequenceOnEnd() {
        Parser parser = new StateMachineParser();
        String snipetText = "asdf```";
        List<TextMessage> messages = parser.parse(snipetText );

        Assert.assertEquals(1, messages.size());
        Assert.assertFalse(messages.get(0).isCode());
        Assert.assertEquals(snipetText, messages.get(0).getMessage());
    }

    @Test
    public void testStringWithOneSnippetOpeningSequenceInMiddle() {
        Parser parser = new StateMachineParser();
        String snipetText = "asdf```bcd";
        List<TextMessage> messages = parser.parse(snipetText );

        Assert.assertEquals(1, messages.size());
        Assert.assertFalse(messages.get(0).isCode());
        Assert.assertEquals(snipetText, messages.get(0).getMessage());
    }

    @Test
    public void testStringWithOneSnippetOpeningSequenceOnStart() {
        Parser parser = new StateMachineParser();
        String snipetText = "```asdf";
        List<TextMessage> messages = parser.parse(snipetText );

        Assert.assertEquals(1, messages.size());
        Assert.assertFalse(messages.get(0).isCode());
        Assert.assertEquals(snipetText, messages.get(0).getMessage());
    }

    @Test
    public void testStringWithOneCodeSnippetAndTextBeforeAndAfter() {
        Parser parser = new StateMachineParser();
        String snipetText = "asdf";
        String startText = "abc";
        String endText = "xyz";
        List<TextMessage> messages = parser.parse(startText + "```" + snipetText + "```" + endText);
        Assert.assertEquals(3, messages.size());
        Assert.assertFalse(messages.get(0).isCode());
        Assert.assertEquals(startText, messages.get(0).getMessage());
        Assert.assertTrue(messages.get(1).isCode());
        Assert.assertEquals(snipetText, messages.get(1).getMessage());
        Assert.assertFalse(messages.get(2).isCode());
        Assert.assertEquals(endText, messages.get(2).getMessage());
    }

    @Test
    public void testStringWithDoubleSnippetOpening() {
        Parser parser = new StateMachineParser();
        List<TextMessage> messages = parser.parse("``````");
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("", messages.get(0).getMessage());
    }

    @Test
    public void testStringWithDoubleSnippetOpeningAndTextBefore() {
        Parser parser = new StateMachineParser();
        String text = "asdf``````";
        List<TextMessage> messages = parser.parse(text);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(text, messages.get(0).getMessage());
        Assert.assertFalse(messages.get(0).isCode());
    }

    @Test
    public void testStringWithDoubleSnippetOpeningAndTextBeforeAndAfter() {
        Parser parser = new StateMachineParser();
        String text = "asdf``````xzy";
        List<TextMessage> messages = parser.parse(text);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(text, messages.get(0).getMessage());
        Assert.assertFalse(messages.get(0).isCode());
    }

}
