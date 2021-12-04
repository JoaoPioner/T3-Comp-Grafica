package logic;

import java.util.Random;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.GL_LINES;

/**
 * Classe Linha
 */
public class Linha{
    /**
     * Envelope
     */
    private float minx, miny, maxx, maxy;

    /**
     * Valores dos ponto da linha
     */
    public float x1,y1,x2,y2;

    /**
     * Construtor
     */
    public Linha(){
        minx = miny = maxx = maxy = 0.0f;
        x1 = y1 = x2 = y2 = 0f;
    }

    /**
     * Gera uma linha
     * 
     * @param limite Limite do valor de x
     * @param tamMax Tamanho maxim da linha
     */
    public void geraLinha(int limite, int tamMax){
        float deltaX,deltaY;
        Random random = new Random();
        
        x1 = ((random.nextInt(10000000) % limite)*10)/10.0f;
        y1 = ((random.nextInt(10000000) % limite)*10)/10.0f;
        
        deltaX = (random.nextInt(10000000) % limite)/(float)limite;
        deltaY = (random.nextInt(10000000) % limite)/(float)limite;
        
        if(random.nextInt()%2 == 0)
            x2 = x1 + deltaX * tamMax;
        else 
            x2 = x1 - deltaX * tamMax;

        if(random.nextInt()%2 == 0)
            y2 = y1 + deltaY * tamMax;
        else 
            y2 = y1 - deltaY * tamMax;
    }

    /**
     * Desenha a linha
     */
    public void desenhaLinha(){
        glBegin(GL_LINES);
            glVertex2f(x1,y1);
            glVertex2f(x2,y2);
        glEnd();
    }
}