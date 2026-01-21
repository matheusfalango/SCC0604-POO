package Controler;

import Auxiliar.Posicao;
import Modelo.Chaser;
import Modelo.Dot;
import Modelo.Heart;
import Modelo.Hero;
import Modelo.Parede;
import Modelo.Personagem;
import Modelo.Skull;
import Modelo.Turnstile;
import java.util.ArrayList;

public class ControleDeJogo {
    
    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            Personagem personagem = e.get(i);
            if (personagem instanceof Dot || personagem instanceof Parede) {
                personagem.autoDesenho();
            }
        }

        for (int i = 0; i < e.size(); i++) {
            Personagem personagem = e.get(i);
            if (personagem instanceof Turnstile) {
                personagem.autoDesenho();
            }
        }

        for (int i = 0; i < e.size(); i++) {
            Personagem personagem = e.get(i);
            if (!(personagem instanceof Dot) && !(personagem instanceof Parede) && !(personagem instanceof Turnstile)) {
                personagem.autoDesenho();
            }
        }
    }
    
    public void processaTudo(ArrayList<Personagem> umaFase) {
        Hero hero = (Hero) umaFase.get(0);
        Personagem pIesimoPersonagem;
        
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
            
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                if (pIesimoPersonagem.isbTransponivel()) {
                    if (pIesimoPersonagem instanceof Dot || pIesimoPersonagem instanceof Heart) {
                        // Coleta de item: remove o item da fase
                        umaFase.remove(pIesimoPersonagem);
                        if(pIesimoPersonagem instanceof Heart){
                            hero.ganhaVida();
                        }
                        if(pIesimoPersonagem instanceof Dot){
                            hero.ganhaPontuacao();
                        }
                        
                    } else if (pIesimoPersonagem.isbMortal()) {
                        if(pIesimoPersonagem instanceof Skull){
                        // Colisão com inimigo: Reposiciona o herói (simulando a morte e o respawn)
                            hero.perdeVida();
                            if(hero.getVidas() == 0) {
                                // Fim de jogo
                                Tela.mostraGameOver(); // você implementa essa função na tela
                            } else {
                                hero.setPosicao(6, 6); // respawn
                            }
                    }
                        // TO-DO: Implementar lógica de perda de vida/fim de jogo
                    }
                }
            }
        }
                
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
                        
            if (pIesimoPersonagem instanceof Chaser) {
                ((Chaser) pIesimoPersonagem).computeDirection(hero.getPosicao());
                ((Chaser) pIesimoPersonagem).moveChaser();
            }
        }
    }

    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
            
            if (pIesimoPersonagem instanceof Turnstile) {
                Turnstile turnstile = (Turnstile) pIesimoPersonagem;
                if (turnstile.ehPosicaoCentral(p)) {
                    return false;
                }
                
                if (turnstile.ehPosicaoTransponivel(p)) {
                    continue; 
                }
            }
            
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean ehMovimentoValido(ArrayList<Personagem> umaFase, Posicao pAtual, int novaLinha, int novaColuna) {
        Posicao pNova = new Posicao(novaLinha, novaColuna);
        
        if (!ehPosicaoValida(umaFase, pNova)) {
            return false;
        }

    // 3. Verifica a lógica do Turnstile
    for (int i = 1; i < umaFase.size(); i++) {
        Personagem pIesimoPersonagem = umaFase.get(i);

        
        return true;
    }
    return false;
}

    /* Método separado para rotacionar turnstiles após o movimento */
    public void processaTurnstiles(ArrayList<Personagem> umaFase, Posicao pAnterior, Posicao pNova) {
        int direcaoLinha = pNova.getLinha() - pAnterior.getLinha();
        int direcaoColuna = pNova.getColuna() - pAnterior.getColuna();
        
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            
            if (pIesimoPersonagem instanceof Turnstile) {
                Turnstile turnstile = (Turnstile) pIesimoPersonagem;
                
                // Se o herói passou por qualquer posição do turnstile
                if (turnstile.ocupaPosicao(pNova)) {
                    // Rotaciona baseado na direção do movimento
                    turnstile.girar();
                }
            }
        }
    }

    
    /* Verifica se não há mais Dots na fase */
    public boolean temDotsNaFase(ArrayList<Personagem> umaFase) {
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            if (pIesimoPersonagem instanceof Dot) {
                return true;
            }
        }
        return false;
    }
    
    /* Conta quantos Dots ainda faltam ser coletados */
    public int contaDotsNaFase(ArrayList<Personagem> umaFase) {
        int contagem = 0;
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            if (pIesimoPersonagem instanceof Dot) {
                contagem++;
            }
        }
        return contagem;
    }
}
