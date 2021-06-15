package br.unicap.compiler.syntax;

public enum Level {
    PARSER(1),
    SEMANTIC(2),
    CODGEN(3);

    private int level;

    private Level(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
   
}
