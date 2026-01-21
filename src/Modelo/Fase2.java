package Modelo;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import Auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;
import java.io.Serializable;
import java.util.ArrayList;


public class Fase2 implements Serializable {
    
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
        for (Object obj : faseAtual) {
            if (obj instanceof Personagem) {
                Personagem p = (Personagem) obj;
                if (p.getPosicao().getLinha() == x && p.getPosicao().getColuna() == y) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Fase2() {
        faseAtual = new ArrayList<Personagem>();
        
        // 1. Heroi (Lady Bug)
        hero = new Hero("ladybug.png", 10, 10);
        faseAtual.add(hero);
        
        // 2. Paredes (Elementos Estáticos Intransponíveis)
        // Paredes externas (15x15)
        for (int i = 0; i <= 15; i++) {
            faseAtual.add(new Parede("folhas.png", 0, i));
            faseAtual.add(new Parede("folhas.png", 15, i));
            faseAtual.add(new Parede("folhas.png", i, 0));
            faseAtual.add(new Parede("folhas.png", i, 15));
        }
        
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
        
        // Adiciona Hearts em posições estratégicas
        faseAtual.add(new Heart(1, 1));
        faseAtual.add(new Heart(14, 14));
        
        // 3. Inimigos (Skull)
        Skull skull = new Skull(2, 2);
        faseAtual.add(skull);
        
        // 4. Portas Giratórias (Turnstiles)
        faseAtual.add(new Turnstile(2, 7, 0));
        faseAtual.add(new Turnstile(14, 7, 0));
        faseAtual.add(new Turnstile(7, 2, 1));
        faseAtual.add(new Turnstile(7, 14, 1));
        
        faseAtual.add(new Turnstile(4, 4, 0));
        faseAtual.add(new Turnstile(4, 12, 0));
        faseAtual.add(new Turnstile(12, 4, 0));
        faseAtual.add(new Turnstile(12, 12, 0));
        
        faseAtual.add(new Turnstile(4, 7, 1));
        faseAtual.add(new Turnstile(12, 7, 1));
        faseAtual.add(new Turnstile(7, 4, 0));
        faseAtual.add(new Turnstile(7, 12, 0));

        // Paredes centrais
        faseAtual.add(new Parede("folhas.png", 3, 7));
        faseAtual.add(new Parede("folhas.png", 3, 9));
        faseAtual.add(new Parede("folhas.png", 5, 7));
        faseAtual.add(new Parede("folhas.png", 5, 9));
        
        faseAtual.add(new Parede("folhas.png", 7, 3));
        faseAtual.add(new Parede("folhas.png", 7, 5));
        faseAtual.add(new Parede("folhas.png", 7, 11));
        faseAtual.add(new Parede("folhas.png", 7, 13));
        
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
