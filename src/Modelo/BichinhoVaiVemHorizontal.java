package Modelo;

import java.io.Serializable;

public class BichinhoVaiVemHorizontal extends Personagem implements Serializable {

    private boolean bRight;
    int iContador;

    public BichinhoVaiVemHorizontal(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);
        bRight = true;
        iContador = 0;
        this.bTransponivel = true;
        this.bMortal = true;     
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;
            if (bRight) {
                if (!this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1)) {
                    bRight = false;
                }
            } else {
                if (!this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1)) {
                    bRight = true;
                }
            }

            // bRight = !bRight; // A mudança de direção já é feita dentro do if/else
        }
        super.autoDesenho();
        iContador++;
    }
}
