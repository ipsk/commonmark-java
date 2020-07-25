package org.commonmark.internal.inline;

import org.commonmark.internal.util.Escaping;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Node;
import org.commonmark.node.Text;

import java.util.regex.Pattern;

/**
 * Parse a backslash-escaped special character, adding either the escaped  character, a hard line break
 * (if the backslash is followed by a newline), or a literal backslash to the block's children.
 */
public class BackslashInlineParser implements InlineContentParser {

    private static final Pattern ESCAPABLE = Pattern.compile('^' + Escaping.ESCAPABLE);

    @Override
    public ParsedInline tryParse(InlineParserState inlineParserState, Node previous) {
        Scanner scanner = inlineParserState.scanner();
        // Backslash
        scanner.skip();

        char next = scanner.peek();
        if (next == '\n') {
            scanner.skip();
            return ParsedInline.of(new HardLineBreak(), scanner.consumed());
        } else if (ESCAPABLE.matcher(String.valueOf(next)).matches()) {
            scanner.skip();
            return ParsedInline.of(new Text(String.valueOf(next)), scanner.consumed());
        } else {
            return ParsedInline.of(new Text("\\"), scanner.consumed());
        }
    }
}
