package Modelo;

import java.io.Serializable;

public class Parede extends Personagem implements Serializable {

    public Parede(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);
        this.bTransponivel = false; // Paredes não podem ser transpostas
        this.bMortal = false;       // Paredes não matam
    }
}
