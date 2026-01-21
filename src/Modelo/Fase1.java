package Modelo;
import java.io.Serializable;
import java.util.ArrayList;

import Auxiliar.Posicao;


public class Fase1 implements Serializable {
    
    private ArrayList<Personagem> faseAtual;
    private Hero hero;

    public boolean temParedeNaPosicao(int x, int y) {
    for (Object obj : faseAtual) { // supondo que tudo herda de Objeto
        if (obj instanceof Parede) {
            Parede p = (Parede) obj;
            if (p.getPosicao().getLinha() == x && p.getPosicao().getColuna() == y) {
                return true;
            }
        }
    }
    return false;
}

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
    
    public Fase1() {
        faseAtual = new ArrayList<Personagem>();
        
        // 1. Heroi (Lady Bug)
        // Posição inicial da Lady Bug (Heroi)
        hero = new Hero("ladybug.png", 6, 6); // Usando Robbo.png temporariamente
        faseAtual.add(hero);
        
        // 2. Paredes (Elementos Estáticos Intransponíveis)

        // Paredes externas (15x15)
        for (int i = 0; i <= 15; i++) {
            // Linha superior e inferior
            faseAtual.add(new Parede("folhaBlack.png", 0, i));
            faseAtual.add(new Parede("folhas.png", 15, i));
            // Coluna esquerda e direita
            faseAtual.add(new Parede("folhas.png", i+1, 0));
            faseAtual.add(new Parede("folhas.png", i+1, 15));
        }
    

        // 3. Itens Coletáveis (Dots e Hearts)
        // Adicionar dots em todas as posições vazias dentro do labirinto (13x13)
        for (int i = 1; i <= 14; i++) {
            for (int j = 1; j <= 14; j++) {
                // Verifica se a posição não é a do herói (7, 7)
                if (i != 7 || j != 7) {
                    // Adiciona um Dot em todas as posições vazias
                    faseAtual.add(new Dot(i, j));
                
                }
            }
        }
        // (A criação dos Dots será feita após a construção completa do labirinto,
        //  para evitar que Dots acabem dentro de paredes que são adicionadas depois.)
        
        // Paredes internas (Estrutura do labirinto Lady Bug)
        // Blocos internos (4x4)
        for (int i = 3; i <= 13; i += 4) {
            for (int j = 3; j <= 13; j += 4) {
                faseAtual.add(new Parede("folhas.png", i, j));
                faseAtual.add(new Parede("folhas.png", i, j + 1));
                faseAtual.add(new Parede("folhas.png", i + 1, j));
                faseAtual.add(new Parede("folhas.png", i + 1, j + 1));
            }
        }
     
        
        // Adiciona alguns Hearts em posições estratégicas (ex: cantos)
        faseAtual.add(new Heart(1, 1));
        faseAtual.add(new Heart(1, 14));
        faseAtual.add(new Heart(14, 1));
        faseAtual.add(new Heart(14, 14));
        
        // 4. Inimigos (Skull)
        // Adiciona o primeiro inimigo (Skull) em posição segura
        Skull besouro = new Skull(2, 2);
        faseAtual.add(besouro);
        
        // 5. Portas Giratórias (Turnstiles)
        // Turnstiles nas bordas (garantindo espaço para ambas as células)
        faseAtual.add(new Turnstile(1, 7, 1));  // Topo - Horizontal (ocupa 1,7 e 1,8)
        faseAtual.add(new Turnstile(13, 7, 1)); // Baixo - Horizontal (ocupa 13,7 e 13,8)
        faseAtual.add(new Turnstile(7, 1, 0));  // Esquerda - Vertical (ocupa 7,1 e 8,1)
        faseAtual.add(new Turnstile(7, 13, 0)); // Direita - Vertical (ocupa 7,13 e 8,13)
        
        // Turnstiles intermediários
        faseAtual.add(new Turnstile(5, 5, 1));  // (5,5 e 5,6)
        faseAtual.add(new Turnstile(5, 10, 1)); // (5,10 e 5,11)
        faseAtual.add(new Turnstile(10, 5, 1)); // (10,5 e 10,6)
        faseAtual.add(new Turnstile(10, 10, 1));// (10,10 e 10,11)

        // Paredes centrais (para fechar os caminhos)
        faseAtual.add(new Parede("folhas.png", 3, 7));
        faseAtual.add(new Parede("folhas.png", 3, 9));
        faseAtual.add(new Parede("folhas.png", 5, 7));
        faseAtual.add(new Parede("folhas.png", 5, 9));
        
        faseAtual.add(new Parede("folhas.png", 7, 3));
        faseAtual.add(new Parede("folhas.png", 7, 5));
        faseAtual.add(new Parede("folhas.png", 7, 11));
        //faseAtual.add(new Parede("folhas.png", 7, 13));
        
        faseAtual.add(new Parede("folhas.png", 9, 7));
        faseAtual.add(new Parede("folhas.png", 9, 9));
        faseAtual.add(new Parede("folhas.png", 11, 7));
        faseAtual.add(new Parede("folhas.png", 11, 9));
        
        faseAtual.add(new Parede("folhas.png", 13, 7));
        faseAtual.add(new Parede("folhas.png", 13, 9));

        // Agora que todas as paredes e objetos estáticos foram adicionados,
        // preenchemos as posições vazias com Dots (exceto onde já exista algo).
        for (int i = 1; i <= 14; i++) {
            for (int j = 1; j <= 14; j++) {
                if (posicaoVazia(i, j)) {
                    faseAtual.add(new Dot(i, j));
                }
            }
        }
    }
    
    public ArrayList<Personagem> getFaseAtual() {
        return faseAtual;
    }
    
    public Hero getHero() {
        return hero;
    }
}
