package Modelo;

import java.io.Serializable;

public class Heart extends Personagem implements Serializable {

    public Heart(int linha, int coluna) {
        super("coracao.png", linha, coluna);
        this.bTransponivel = true; // Pode passar por cima
        this.bMortal = false;      // Não mata
    }
}
