package org.commonmark.internal;

import org.commonmark.node.Block;
import org.commonmark.node.LinkReferenceDefinition;
import org.commonmark.node.Paragraph;
import org.commonmark.parser.InlineParser;
import org.commonmark.parser.block.AbstractBlockParser;
import org.commonmark.parser.block.BlockContinue;
import org.commonmark.parser.block.ParserState;

import java.util.List;

public class ParagraphParser extends AbstractBlockParser {

    private final Paragraph block = new Paragraph();
    private LinkReferenceDefinitionParser linkReferenceDefinitionParser = new LinkReferenceDefinitionParser();

    @Override
    public boolean canHaveLazyContinuationLines() {
        return true;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        if (!state.isBlank()) {
            return BlockContinue.atIndex(state.getIndex());
        } else {
            return BlockContinue.none();
        }
    }

    @Override
    public void addLine(CharSequence line) {
        linkReferenceDefinitionParser.parse(line);
    }

    @Override
    public void closeBlock() {
        if (linkReferenceDefinitionParser.getParagraphLines().isEmpty()) {
            block.unlink();
        }
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
        List<CharSequence> lines = linkReferenceDefinitionParser.getParagraphLines();
        if (!lines.isEmpty()) {
            inlineParser.parse(lines, block);
        }
    }

    public List<CharSequence> getParagraphLines() {
        return linkReferenceDefinitionParser.getParagraphLines();
    }

    public List<LinkReferenceDefinition> getDefinitions() {
        return linkReferenceDefinitionParser.getDefinitions();
    }
}
