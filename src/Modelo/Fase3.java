package Modelo;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import Auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class Fase3 implements Serializable {
    private ArrayList<Personagem> faseAtual;
    private Hero hero;

    private boolean posicaoVazia(int x, int y) {
        for (Personagem p : faseAtual) {
            // Se for um Turnstile, verifica se a posição é ocupada por qualquer uma das 3 células
            if (p instanceof Turnstile) {
                Turnstile t = (Turnstile) p;
                if (t.ocupaPosicao(new Posicao(x, y))) {
                    return false;
                }
            } else if (p.getPosicao().getLinha() == x && p.getPosicao().getColuna() == y) {
                return false;
            }
        }
        return true;
    }

    public Fase3() {
        faseAtual = new ArrayList<Personagem>();
        hero = new Hero("ladybug.png", 8, 8);
        faseAtual.add(hero);

        // Bordas (usa pngs das fases anteriores)
        for (int i = 0; i <= 15; i++) {
            faseAtual.add(new Parede("folhaBlack.png", 0, i));
            faseAtual.add(new Parede("folhas.png", 15, i));
            faseAtual.add(new Parede("folhas.png", i, 0));
            faseAtual.add(new Parede("folhas.png", i, 15));
        }

        // Obstáculos: labirinto em forma de espiral usando as mesmas texturas
        for (int i = 2; i <= 13; i++) {
            faseAtual.add(new Parede("folhas.png", 2, i));
        }
        for (int i = 2; i <= 13; i++) {
            faseAtual.add(new Parede("folhas.png", i, 13));
        }
        for (int i = 3; i <= 13; i++) {
            faseAtual.add(new Parede("folhas.png", 13, i));
        }
        for (int i = 3; i <= 12; i++) {
            faseAtual.add(new Parede("folhas.png", i, 3));
        }

        // Garantir saída do núcleo: construir um corredor para a direita a partir de (8,8)
        // Remove paredes nas posições (8,9) .. (8,15) se existirem, garantindo passagem
        int targetRow = 8;
        for (int col = 9; col <= 15; col++) {
            for (int idx = 0; idx < faseAtual.size(); idx++) {
                Personagem p = faseAtual.get(idx);
                if (p instanceof Parede) {
                    if (p.getPosicao().getLinha() == targetRow && p.getPosicao().getColuna() == col) {
                        faseAtual.remove(idx);
                        break;
                    }
                }
            }
        }

        // Turnstiles e corações
        faseAtual.add(new Heart(1, 7));
        faseAtual.add(new Heart(14, 7));
        faseAtual.add(new Turnstile(4, 4, 1));
        faseAtual.add(new Turnstile(10, 10, 0));

        // Inimigos: aumenta dificuldade — mais Chasers e Skulls
        faseAtual.add(new Skull(7, 2));
        faseAtual.add(new Skull(3, 7));
        faseAtual.add(new ZigueZague("zig.png", 11, 4));

        // Preenche com Dots nas posições vazias
        for (int i = 1; i <= 14; i++) {
            for (int j = 1; j <= 14; j++) {
                if (posicaoVazia(i, j)) {
                    faseAtual.add(new Dot(i, j));
                }
            }
        }
    }

    public ArrayList<Personagem> getFaseAtual() { return faseAtual; }
    public Hero getHero() { return hero; }
}
