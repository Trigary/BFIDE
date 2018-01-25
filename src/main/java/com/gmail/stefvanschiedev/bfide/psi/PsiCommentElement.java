package com.gmail.stefvanschiedev.bfide.psi;

import com.gmail.stefvanschiedev.bfide.execution.RunConfiguration;
import com.gmail.stefvanschiedev.bfide.utils.TextRange;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represent a comment in BrainFuck. Every character that doesn't match
 * any of the instruction characters will be a comment.
 */
public class PsiCommentElement extends PsiElement {

    private final String comment;

    public PsiCommentElement(TextRange range, PsiElement parent, String comment) {
        super(range, parent);
        this.comment = comment;
    }

    @Override
    public int execute(long[] cells, int pointer, RunConfiguration configuration) {
        return pointer;
    }

    @Override
    public String toString() {
        return comment;
    }

    public static class Builder implements PsiBuilder<PsiCommentElement> {

        private final Pattern pattern = Pattern.compile("[^><+\\-.,\\[\\]]+");

        @Override
        public int parse(String text, int offset, @Nullable PsiElement parent, Queue<PsiElement> holder) {
            Matcher matcher = pattern.matcher(text);
            if (!matcher.find() || matcher.start() != 0)
                return -1;

            holder.add(new PsiCommentElement(new TextRange(offset, offset + matcher.end()),
                    parent, text.substring(0, matcher.end())));
            return matcher.end();
        }
    }
}