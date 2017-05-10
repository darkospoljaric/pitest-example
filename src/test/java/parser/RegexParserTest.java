package parser;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import parser.RegexParser;
import parser.TextMessage;

public class RegexParserTest {

	@Test
	public void testParseWithOneInlineCodeSnipetOnly() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("`code`");

		Assert.assertEquals(1, messages.size());
        Assert.assertTrue(messages.get(0).isCode());
        Assert.assertEquals("code", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithOneTextOnly() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("text");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("text", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithOneInlineCodeSnipetAndTextBefore() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("asdf`code`");

		Assert.assertEquals(2, messages.size());
        Assert.assertFalse(messages.get(0).isCode());
        Assert.assertEquals("asdf", messages.get(0).getMessage());
        Assert.assertTrue(messages.get(1).isCode());
        Assert.assertEquals("code", messages.get(1).getMessage());
	}

	@Test
	public void testParseWithOneInlineCodeSnipetAndTextAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("`code`asdf");

		Assert.assertEquals(2, messages.size());
		Assert.assertTrue(messages.get(0).isCode());
		Assert.assertEquals("code", messages.get(0).getMessage());
		Assert.assertFalse(messages.get(1).isCode());
		Assert.assertEquals("asdf", messages.get(1).getMessage());
	}

	@Test
	public void testParseWithOneInlineCodeSnipetAndTextBeforeAndAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("aaa`code`bbb");

		Assert.assertEquals(3, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("aaa", messages.get(0).getMessage());
		Assert.assertTrue(messages.get(1).isCode());
		Assert.assertEquals("code", messages.get(1).getMessage());
		Assert.assertFalse(messages.get(2).isCode());
		Assert.assertEquals("bbb", messages.get(2).getMessage());
	}

	@Test
	public void testParseWithTwoInlineCodeSnipetsAndTextBeforeAndAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("aaa`code`bbb`code2`ccc");

		Assert.assertEquals(5, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("aaa", messages.get(0).getMessage());
		Assert.assertTrue(messages.get(1).isCode());
		Assert.assertEquals("code", messages.get(1).getMessage());
		Assert.assertFalse(messages.get(2).isCode());
		Assert.assertEquals("bbb", messages.get(2).getMessage());
		Assert.assertTrue(messages.get(3).isCode());
		Assert.assertEquals("code2", messages.get(3).getMessage());
		Assert.assertFalse(messages.get(4).isCode());
		Assert.assertEquals("ccc", messages.get(4).getMessage());
	}

	@Test
	public void testParseWithOneNewlineCodeSnipetOnly() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```code```");

		Assert.assertEquals(1, messages.size());
        Assert.assertTrue(messages.get(0).isCode());
        Assert.assertEquals("code", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithOneNewlineCodeSnipetAndTextBefore() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("abc```code```");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("abc```code```", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithOneNewlineCodeSnipetAndTextAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```code```abc");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("```code```abc", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithOneNewlineCodeSnipetAndTextInNewLineAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```code```\nabc");

		Assert.assertEquals(2, messages.size());
		Assert.assertTrue(messages.get(0).isCode());
		Assert.assertEquals("code", messages.get(0).getMessage());
		Assert.assertFalse(messages.get(1).isCode());
		Assert.assertEquals("abc", messages.get(1).getMessage());
	}

	@Test
	public void testParseWithOneNewlineCodeSnipetContainingInlineSnippet() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```\nabc`code`\n```");

		Assert.assertEquals(1, messages.size());
		Assert.assertTrue(messages.get(0).isCode());
		Assert.assertEquals("abc`code`", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithOneEmptyInlineAndTextBeforeAndAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("aaa``bbb");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("aaa``bbb", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithSingleStartingAndSingleEndingbacktick() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("``");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("``", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithTripleStartintAndClosingBacktickAndTextBeforeAndAfter() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("aaa```code```bbb\nccc```code2```eee");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("aaa```code```bbb\nccc```code2```eee", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithTripleOpeningBacktickAndTextInNewline() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```\ncode```");

		Assert.assertEquals(1, messages.size());
		Assert.assertTrue(messages.get(0).isCode());
		Assert.assertEquals("code", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithTripleOpeningBacktickAndTextInNewlineAndClosingBackticksInNewline() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```\ncode\n```");

		Assert.assertEquals(1, messages.size());
		Assert.assertTrue(messages.get(0).isCode());
		Assert.assertEquals("code", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithTextThenTripleOpeningBacktickThenCodeInNewlineAndClosingBackticksInNewline() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("abc```\ncode\n```");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("abc```\ncode\n```", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithTripleOpeningBacktickThenCodeInNewlineThenClosingBackticksInNewlineAndTextInSameLine() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```\ncode\n```abc");

		Assert.assertEquals(1, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("```\ncode\n```abc", messages.get(0).getMessage());
	}

	@Test
	public void testParseWithInlineCodeThenTripleBacktickThenNewlineThenCodeThenNewlineAndClosingTripleBackick() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("`code````\ncode\n```");

		Assert.assertEquals(2, messages.size());
		Assert.assertTrue(messages.get(0).isCode());
		Assert.assertEquals("code", messages.get(0).getMessage());
		Assert.assertFalse(messages.get(1).isCode());
		Assert.assertEquals("```\ncode\n```", messages.get(1).getMessage());
	}

	@Test
	@Ignore
	public void testParseVeryComplicated() {
		RegexParser parser = new RegexParser();
		List<TextMessage> messages = parser.parse("```\ncode\n````code2`");

		Assert.assertEquals(2, messages.size());
		Assert.assertFalse(messages.get(0).isCode());
		Assert.assertEquals("```\ncode\n```", messages.get(0).getMessage());
		Assert.assertTrue(messages.get(1).isCode());
		Assert.assertEquals("code2", messages.get(1).getMessage());
	}

}
