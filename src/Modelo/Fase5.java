package Modelo;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import Auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class Fase5 implements Serializable {
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

    public Fase5() {
        faseAtual = new ArrayList<Personagem>();
        hero = new Hero("ladybug.png", 7, 7);
        faseAtual.add(hero);

        // Bordas (usa PNGs existentes nas fases anteriores)
        for (int i = 0; i <= 15; i++) {
            faseAtual.add(new Parede("folhaBlack.png", 0, i));
            faseAtual.add(new Parede("folhas.png", 15, i));
            faseAtual.add(new Parede("folhas.png", i, 0));
            faseAtual.add(new Parede("folhas.png", i, 15));
        }

        // Obstáculos: labirinto denso - blocos 2x2 usando mesma textura
        for (int i = 2; i <= 13; i += 3) {
            for (int j = 2; j <= 13; j += 3) {
                faseAtual.add(new Parede("folhas.png", i, j));
                faseAtual.add(new Parede("folhas.png", i, j+1));
                faseAtual.add(new Parede("folhas.png", i+1, j));
                faseAtual.add(new Parede("folhas.png", i+1, j+1));
            }
        }

        // Inimigos: BOSS: aumenta o desafio — mais inimigos agrupados
        faseAtual.add(new Chaser("skull.png", 2, 2));
        faseAtual.add(new Chaser("skull.png", 2, 13));
        faseAtual.add(new Chaser("skull.png", 13, 2));
        faseAtual.add(new Skull(13, 2));
        faseAtual.add(new Skull(13, 13));
        faseAtual.add(new ZigueZague("zig.png", 13, 13));
        faseAtual.add(new Esfera("sphere.png", 7, 2));

        // Pequenos power-ups
        faseAtual.add(new Heart(1, 14));
        faseAtual.add(new Heart(14, 1));

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
