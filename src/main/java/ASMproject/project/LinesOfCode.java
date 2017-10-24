package ASMproject.project;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LinesOfCode extends MethodVisitor implements Opcodes {

  private int lines = 0;

  public LinesOfCode(MethodVisitor delegate) {
    super(ASM5, delegate);
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    lines ++;
    super.visitLineNumber(line, start);
  }

    public int getLines() {
        if (lines < 1)
            System.out.printf("Lines computed to be %d, defaulting to 1\n", lines);
        return Math.max(lines, 1);
    }
}