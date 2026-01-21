package Modelo;

import Auxiliar.Consts;
import Auxiliar.Posicao;
import Auxiliar.Desenho;
import java.io.Serializable;

/**
 *
 * @author 2373891
 */
public class Chaser extends Personagem implements Serializable {

    private boolean iDirectionV;
    private boolean iDirectionH;
    private int counter;
    private boolean moveHorizontalNext = true; // Alterna entre horizontal e vertical

    public Chaser(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);
        iDirectionV = true;
        iDirectionH = true;
        this.bTransponivel = true;
        this.bMortal = true;
        counter = 0;
    }

    public void computeDirection(Posicao heroPos) {
        if (heroPos.getColuna() < this.getPosicao().getColuna()) {
            iDirectionH = true;
        } else if (heroPos.getColuna() > this.getPosicao().getColuna()) {
            iDirectionH = false;
        }
        if (heroPos.getLinha() < this.getPosicao().getLinha()) {
            iDirectionV = true;
        } else if (heroPos.getLinha() > this.getPosicao().getLinha()) {
            iDirectionV = false;
        }
    }

    /* Tenta mover validando contra paredes */
    private boolean tentarMover(int novaLinha, int novaColuna) {
        try {
            // Verifica se o movimento é válido (não atravessa paredes)
            if (Desenho.acessoATelaDoJogo().ehMovimentoValido(this.pPosicao, novaLinha, novaColuna)) {
                System.out.println("chaser moveu: " + novaLinha + ", " + novaColuna);
                return this.pPosicao.setPosicao(novaLinha, novaColuna);
            }
        } catch (Exception e) {
            // Se houver erro na validação, não move
        }
        return false;
    }

    public void moveChaser() {
        if (counter == Consts.PERIOD*3) { // Movimento mais lento
            // Alterna entre horizontal e vertical - NÃO diagonal
            if (moveHorizontalNext) {
                // Tenta movimento horizontal APENAS
                if (iDirectionH) {
                    tentarMover(this.getPosicao().getLinha(), this.getPosicao().getColuna() - 1);
                } else {
                    tentarMover(this.getPosicao().getLinha(), this.getPosicao().getColuna() + 1);
                }
            } else {
                // Tenta movimento vertical APENAS
                if (iDirectionV) {
                    tentarMover(this.getPosicao().getLinha() - 1, this.getPosicao().getColuna());
                } else {
                    tentarMover(this.getPosicao().getLinha() + 1, this.getPosicao().getColuna());
                }
            }
            
            moveHorizontalNext = !moveHorizontalNext; // Alterna para próxima vez
            counter = 0;
        }
        counter++;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }
}
