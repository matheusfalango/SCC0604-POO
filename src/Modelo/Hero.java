package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;

public class Hero extends Personagem implements Serializable{
    private int vidas = 3;
    private int pontuacao = 0;
    
    public Hero(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG,linha, coluna);
        this.bTransponivel = true;
        this.bMortal = true; // A Lady Bug é mortal se colidir com inimigos
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    
    public boolean setPosicao(int linha, int coluna){
        // Salva a posição anterior antes de tentar mover
        int linhaAnterior = this.pPosicao.getLinha();
        int colunaAnterior = this.pPosicao.getColuna();
        
        // Verifica se o movimento é válido, incluindo a lógica do Turnstile e paredes
        if (!Desenho.acessoATelaDoJogo().ehMovimentoValido(this.pPosicao, linha, coluna)) {
            return false;
        }
        
        // Se for válido, move para a nova posição
        if(this.pPosicao.setPosicao(linha, coluna)){
            return true;
        }
        return false;       
    }

    public int getVidas() {
        return vidas;
    }

    public void perdeVida() {
        vidas--;
    }

    public void ganhaVida() {
        vidas++;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void ganhaPontuacao(){
        pontuacao += 10;
    }

    public boolean moveUp() {
        return setPosicao(this.getPosicao().getLinha() - 1, this.getPosicao().getColuna());
    }

    public boolean moveDown() {
        return setPosicao(this.getPosicao().getLinha() + 1, this.getPosicao().getColuna());
    }

    public boolean moveRight() {
        return setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna() + 1);
    }

    public boolean moveLeft() {
        return setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna() - 1);
    }    
    
}
