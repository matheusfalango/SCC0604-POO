package Modelo;

import java.io.Serializable;

public class Dot extends Personagem implements Serializable {

    public Dot(int linha, int coluna) {


        super("dots.png", linha, coluna); // Usando blackTile.png para representar o dot
        this.bTransponivel = true; // Pode passar por cima
        this.bMortal = false;      // Não mata
    }
}
