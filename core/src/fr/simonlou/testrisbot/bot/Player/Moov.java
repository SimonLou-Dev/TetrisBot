package fr.simonlou.testrisbot.bot.Player;

public enum Moov {
    wt (0),
    mr (1),
    ml (2),
    dw (3),
    dr (4),
    tu (5);

    private final int code;

    Moov(int code) {
        this.code = code;
    }

    public Moov getMoov(){
        return this;
    }

    public int getCode(){
        return this.code;
    }


}
