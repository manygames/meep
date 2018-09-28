package br.com.manygames.meep.preferences;

public enum Layouts {
    LINEAR(1), STAGGEREDGRID(2);

    private final int id;

    Layouts(int id) {
        this.id = id;
    }

    int id(){
        return id;
    }

    public static Layouts getEnum(int index){
        return Layouts.values()[index];
    }
}
