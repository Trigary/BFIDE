package com.gmail.stefvanschiedev.bfide.psi;

import com.gmail.stefvanschiedev.bfide.execution.RunConfiguration;
import com.gmail.stefvanschiedev.bfide.utils.TextRange;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Queue;

/**
 * Represents an input byte instruction in BrainFuck
 */
public class PsiInputByteElement extends PsiElement {

    public PsiInputByteElement(TextRange range, PsiElement parent) {
        super(range, parent);
    }

    @Override
    public int execute(long[] cells, int pointer, RunConfiguration configuration) {
        try {
            int input = configuration.getInput().read();
            if (configuration.getMaxCellValue() < Byte.MAX_VALUE) {
                //The cell values are in the signed byte range,
                //therefore put the input in the signed byte range as well
                input -= Byte.MIN_VALUE;
            }

            cells[pointer] = input;
            return pointer;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading input", e);
        }
    }

    @Override
    public String toString() {
        return ",";
    }

    public static class Builder implements PsiBuilder<PsiInputByteElement> {

        @Override
        public int parse(String text, int offset, @Nullable PsiElement parent, Queue<PsiElement> holder) {
            if (!text.startsWith(","))
                return -1;

            holder.add(new PsiInputByteElement(new TextRange(offset, offset + 1), parent));
            return 1;
        }
    }
}