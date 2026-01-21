package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Turnstile extends Personagem implements Serializable {
    // 0: Vertical (ocupa 3 células na vertical)
    // 1: Horizontal (ocupa 3 células na horizontal)
    private Posicao posCentral; 
    private Posicao posSecundaria;
    private Posicao posTerciaria;
    private int orientation; // 0 = vertical, 1 = horizontal
    
    private ImageIcon imageHorizontal;
    private ImageIcon imageVertical;
    
    public Turnstile(int linha, int coluna, int orientation) {
        super("tronco.png", linha, coluna);
        this.orientation = orientation;
        this.bTransponivel = false; 
        this.bMortal = false;
        
        loadImages(); 
        updatePositions(); 
        updateImage(); 
    }
    
    private void updatePositions() {
        posCentral = this.pPosicao;
        if (orientation == 0) { // Vertical: uma acima, uma abaixo da central
            posSecundaria = new Posicao(posCentral.getLinha() - 1, posCentral.getColuna());
            posTerciaria = new Posicao(posCentral.getLinha() + 1, posCentral.getColuna());
        } else { // Horizontal: uma à esquerda, uma à direita da central
            posSecundaria = new Posicao(posCentral.getLinha(), posCentral.getColuna() - 1);
            posTerciaria = new Posicao(posCentral.getLinha(), posCentral.getColuna() + 1);
        }   
    }
    
    public void girar() {
        // Inverte a orientação
        if (orientation == 0) orientation = 1;
        else orientation = 0;
        
        updatePositions(); // Recalcula posições
        updateImage(); // Atualiza a imagem
    }   
    
    public boolean ehPosicaoTransponivel(Posicao p) {
        return posSecundaria.igual(p) || posTerciaria.igual(p);
    }

    public boolean ehPosicaoCentral(Posicao p) {
        return pPosicao.igual(p);
    }

    // Verifica se a posição passada (p) é ocupada por qualquer parte do Turnstile
    public boolean ocupaPosicao(Posicao p) {
        return pPosicao.igual(p) || posSecundaria.igual(p) || posTerciaria.igual(p);
    }
    
    private void updateImage() {
        // Certifique-se de que a imagem correta (vertical ou horizontal) é atribuída ao iImage
        if (orientation == 0) {
            this.iImage = imageVertical; // Usar a imagem vertical para orientação 0
        } else {
            this.iImage = imageHorizontal; // Usar a imagem horizontal para orientação 1
        }
    }
    
    private void loadImages() {
        try {
            String caminhoBase = new java.io.File(".").getCanonicalPath() + Consts.PATH;
            
            // Imagem Vertical (Assumindo que "troncoVertical.png" é para a orientação 0 - Vertical)
            ImageIcon imgVert = new ImageIcon(caminhoBase + "troncoVertical.png");
            Image img1 = imgVert.getImage();
            BufferedImage bi1 = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g1 = bi1.createGraphics();
            g1.drawImage(img1, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            imageVertical = new ImageIcon(bi1); // Armazena a imagem para a orientação Vertical
            g1.dispose();
            
            // Imagem Horizontal (Assumindo que "troncoHorizontal.png" é para a orientação 1 - Horizontal)
            ImageIcon imgHoriz = new ImageIcon(caminhoBase + "troncoHorizontal.png");
            Image img2 = imgHoriz.getImage();
            BufferedImage bi2 = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g2 = bi2.createGraphics();
            g2.drawImage(img2, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            imageHorizontal = new ImageIcon(bi2); // Armazena a imagem para a orientação Horizontal
            g2.dispose();
            
        } catch (IOException ex) {
            System.out.println("Erro ao carregar imagens do Turnstile: " + ex.getMessage());
        }
    }
    
    @Override
    public boolean isbTransponivel() {
        return this.bTransponivel;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        
        // Desenha na posição secundária
        if (posSecundaria != null && iImage != null) {
            Desenho.desenhar(iImage, posSecundaria.getColuna(), posSecundaria.getLinha());
        }
        
        // Desenha na posição terciária
        if (posTerciaria != null && iImage != null) {
            Desenho.desenhar(iImage, posTerciaria.getColuna(), posTerciaria.getLinha());
        }
    }
    

}
