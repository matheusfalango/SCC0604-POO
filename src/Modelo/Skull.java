package Modelo;

import java.io.Serializable;

public class Skull extends Chaser implements Serializable {

    public Skull(int linha, int coluna) {
        super("besouroArcade.png", linha, coluna);
        this.bTransponivel = true; // Pode ser transposto (para a colisão)
        this.bMortal = true;       // É mortal (mata o herói)
    }
}
