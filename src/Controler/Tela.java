package Controler;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Modelo.Fase1;
import Modelo.Fase2;
import Modelo.Fase3;
import Modelo.Fase4;
import Modelo.Fase5;
import Modelo.Hero;
import Modelo.Personagem;
import java.awt.Color;
import java.awt.Font;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private Fase1 fase1;
    private Fase2 fase2;
    private Fase3 fase3;
    private Fase4 fase4;
    private Fase5 fase5;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private final Set<Integer> teclasPressionadas = new HashSet<>();
    private int faseAtualNumero = 1;
    private static boolean pausado = false;
    
    public Tela() {
        initComponents();
        Desenho.setCenario(this);
        this.addMouseListener(this);
        /*mouse*/
        this.addKeyListener(this);
        /*teclado*/
 /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        fase1 = new Fase1();
        faseAtual = fase1.getFaseAtual();
        hero = fase1.getHero();
        this.atualizaCamera();
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }
    
    public boolean ehMovimentoValido(Posicao pAtual, int novaLinha, int novaColuna) {
        return cj.ehMovimentoValido(this.faseAtual, pAtual, novaLinha, novaColuna);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /**
         * ***********Desenha cenário de fundo*************
         */
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "blackTile.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (!this.faseAtual.isEmpty()) {
            if (!pausado) {
                this.cj.processaTudo(faseAtual);
            }

            this.cj.desenhaTudo(faseAtual);
            
            // Atualiza o contador de dots no título
            int dotsRestantes = cj.contaDotsNaFase(faseAtual);
            this.setTitle("Fase " + faseAtualNumero + " | Dots: " + dotsRestantes + " | Cell: (" + (hero.getPosicao().getLinha()) + ", " + (hero.getPosicao().getColuna()) + ")");
            
            // Verifica se todos os dots foram coletados e transiciona entre fases
            if (faseAtualNumero == 1 && !cj.temDotsNaFase(faseAtual)) {
                System.out.println("\n[FASE COMPLETADA] Fase 1 concluída! Passando para Fase 2...");
                fase2 = new Fase2();
                faseAtual = fase2.getFaseAtual();
                hero = fase2.getHero();
                faseAtualNumero = 2;
                this.atualizaCamera();
                System.out.println("[FASE 2 INICIADA] Dots iniciais na Fase 2: " + cj.contaDotsNaFase(faseAtual));
            } else if (faseAtualNumero == 2 && !cj.temDotsNaFase(faseAtual)) {
                System.out.println("\n[FASE COMPLETADA] Fase 2 concluída! Passando para Fase 3...");
                fase3 = new Fase3();
                faseAtual = fase3.getFaseAtual();
                hero = fase3.getHero();
                faseAtualNumero = 3;
                this.atualizaCamera();
                System.out.println("[FASE 3 INICIADA] Dots iniciais na Fase 3: " + cj.contaDotsNaFase(faseAtual));
            } else if (faseAtualNumero == 3 && !cj.temDotsNaFase(faseAtual)) {
                System.out.println("\n[FASE COMPLETADA] Fase 3 concluída! Passando para Fase 4...");
                fase4 = new Fase4();
                faseAtual = fase4.getFaseAtual();
                hero = fase4.getHero();
                faseAtualNumero = 4;
                this.atualizaCamera();
                System.out.println("[FASE 4 INICIADA] Dots iniciais na Fase 4: " + cj.contaDotsNaFase(faseAtual));
            } else if (faseAtualNumero == 4 && !cj.temDotsNaFase(faseAtual)) {
                System.out.println("\n[FASE COMPLETADA] Fase 4 concluída! Passando para Fase 5...");
                fase5 = new Fase5();
                faseAtual = fase5.getFaseAtual();
                hero = fase5.getHero();
                faseAtualNumero = 5;
                this.atualizaCamera();
                System.out.println("[FASE 5 INICIADA] Dots iniciais na Fase 5: " + cj.contaDotsNaFase(faseAtual));
            } else if (faseAtualNumero == 5 && !cj.temDotsNaFase(faseAtual)) {
                System.out.println("\n[CONGRATULATIONS] Você venceu todas as fases!");
                javax.swing.JOptionPane.showMessageDialog(null, "PARABÉNS! Você venceu todas as fases!");
                pausado = true;
                // Reinicia no nível 1
                fase1 = new Fase1();
                faseAtual = fase1.getFaseAtual();
                hero = fase1.getHero();
                faseAtualNumero = 1;
                this.atualizaCamera();
            }
            
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString("VIDAS: " + hero.getVidas(), 20, 15);
            g2.setColor(Color.WHITE);
            g2.drawString("PONTUACAO: " + hero.getPontuacao() , 150, 15);
            g2.drawString("FASE: " + faseAtualNumero + " | DOTS: " + dotsRestantes, 370, 15);
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

     private void atualizaCamera() {
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();
        }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }
    
    // Substitua o método keyPressed inteiro por este:

    public void keyPressed(KeyEvent e) {
        try {
            if (teclasPressionadas.contains(e.getKeyCode()))
                return;

            teclasPressionadas.add(e.getKeyCode());
            
            // Salva a posição anterior
            Posicao posicaoAnterior = new Posicao(hero.getPosicao().getLinha(), hero.getPosicao().getColuna());
            boolean moveu = false;
            
            if (e.getKeyCode() == KeyEvent.VK_T) {
                // Reinicializa a fase 1
                System.out.println("\n[REINÍCIO] Fase 1 reiniciada!");
                fase1 = new Fase1();
                faseAtual = fase1.getFaseAtual();
                hero = fase1.getHero();
                faseAtualNumero = 1;
                this.atualizaCamera();
                System.out.println("[FASE 1] Dots iniciais: " + cj.contaDotsNaFase(faseAtual));
            } else if (e.getKeyCode() == KeyEvent.VK_N) {
                // Pula para a próxima fase manualmente (atalho 'N')
                try {
                    System.out.println("\n[ATALHO] Pulando para a próxima fase...");
                    if (faseAtualNumero == 1) {
                        fase2 = new Fase2();
                        faseAtual = fase2.getFaseAtual();
                        hero = fase2.getHero();
                        faseAtualNumero = 2;
                    } else if (faseAtualNumero == 2) {
                        fase3 = new Fase3();
                        faseAtual = fase3.getFaseAtual();
                        hero = fase3.getHero();
                        faseAtualNumero = 3;
                    } else if (faseAtualNumero == 3) {
                        fase4 = new Fase4();
                        faseAtual = fase4.getFaseAtual();
                        hero = fase4.getHero();
                        faseAtualNumero = 4;
                    } else if (faseAtualNumero == 4) {
                        fase5 = new Fase5();
                        faseAtual = fase5.getFaseAtual();
                        hero = fase5.getHero();
                        faseAtualNumero = 5;
                    } else if (faseAtualNumero == 5) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Última fase atingida. Reiniciando no nível 1.");
                        fase1 = new Fase1();
                        faseAtual = fase1.getFaseAtual();
                        hero = fase1.getHero();
                        faseAtualNumero = 1;
                    }
                    this.atualizaCamera();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                pausado = !pausado;
                System.out.println("pausado: " + pausado);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                moveu = hero.moveUp();  
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moveu = hero.moveDown();  
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveu = hero.moveLeft();  
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveu = hero.moveRight();  
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                File tanque = new File("POO.dat");
                tanque.createNewFile();
                FileOutputStream canoOut = new FileOutputStream(tanque);
                ObjectOutputStream serializador = new ObjectOutputStream(canoOut);
                serializador.writeObject(faseAtual);
            } else if (e.getKeyCode() == KeyEvent.VK_L) {
                File tanque = new File("POO.dat");
                FileInputStream canoOut = new FileInputStream(tanque);
                ObjectInputStream serializador = new ObjectInputStream(canoOut);
                faseAtual = (ArrayList<Personagem>)serializador.readObject();
            }

            // Se o hero se moveu, processa os turnstiles
            if (moveu) {
                cj.processaTurnstiles(faseAtual, posicaoAnterior, hero.getPosicao());
            }

            this.atualizaCamera();
            int dotsRestantes = cj.contaDotsNaFase(faseAtual);
            this.setTitle("Dots: " + dotsRestantes + " | Cell: " + (hero.getPosicao().getLinha()) + ", " + (hero.getPosicao().getColuna()));

            //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
        } catch (Exception ee) {
            ee.printStackTrace(); 
        }
    }

    public void keyReleased(KeyEvent e) {
        teclasPressionadas.remove(e.getKeyCode());        
    }    

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        repaint();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public static void mostraGameOver() {
        pausado = true;
        javax.swing.JOptionPane.showMessageDialog(null, "GAME OVER!");
        System.exit(0); // Fecha o jogo, ou você pode reiniciar a fase
    }

    public static void mostrarStartGame() {
        pausado = true;
        javax.swing.JOptionPane.showMessageDialog(null, "START GAME!\nClique OK para começar.");
        pausado = false;
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }


}
