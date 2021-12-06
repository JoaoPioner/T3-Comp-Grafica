import glfw.listener.KeyListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Personagem {
    //public float cor;

    /*public Ponto v1;
    public Ponto v2;
    public Ponto v3;
    public Ponto v4;
    public Ponto v5;
    public Ponto v6;*/

    public Ponto posicao;
    public float angulo = 0;
    public float spd = 2.5f;
    public boolean jogador;

    //para o paralelepipedo foi necessario soh 6 pts. nao tenho ctz se eu soh preciso de 6 tbm aqui.

    public Personagem(Ponto posicao,boolean jogador) {
        //this.cor = cor;
        /*this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;*/
        this.posicao = posicao;
        this.jogador = jogador;
    }

    void keyListenerExample() {
        if(jogador) {
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_A)) {
                posicao.x += spd;
            }
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_D)) {
                posicao.x -= spd;
            }
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_W)) {
                posicao.z -= spd;
            }
        }
    }

    void draw() {
        glPushMatrix();
        glTranslatef(posicao.x, posicao.y, posicao.z);
        glColor3f (0, 0.2f, 1);
        keyListenerExample();
        DesenhaParalelepipedo();
        glPopMatrix();
    }

    void DesenhaParalelepipedo() {
        glPushMatrix();
        glTranslatef(0, 0, 0);
        glScalef(2, 1, 3);
        //glutSolidCube(2);
        DesenhaCubo(1);
        glPopMatrix();
        glPushMatrix();
        glTranslatef(0, 0, 0.8f);
        glScalef(0.5f, 5, 0.5f);
        //glutSolidCube(2);
        DesenhaCubo(1);
        glPopMatrix();
    }

    void DesenhaCubo(float tamAresta) {
        glBegin(GL_QUADS);
        // Front Face
        glNormal3f(0, 0, 1);
        glVertex3f(-tamAresta / 2, -tamAresta / 2, tamAresta / 2);
        glVertex3f(tamAresta / 2, -tamAresta / 2, tamAresta / 2);
        glVertex3f(tamAresta / 2, tamAresta / 2, tamAresta / 2);
        glVertex3f(-tamAresta / 2, tamAresta / 2, tamAresta / 2);
        // Back Face
        glNormal3f(0, 0, -1);
        glVertex3f(-tamAresta / 2, -tamAresta / 2, -tamAresta / 2);
        glVertex3f(-tamAresta / 2, tamAresta / 2, -tamAresta / 2);
        glVertex3f(tamAresta / 2, tamAresta / 2, -tamAresta / 2);
        glVertex3f(tamAresta / 2, -tamAresta / 2, -tamAresta / 2);
        // Top Face
        glNormal3f(0, 1, 0);
        glVertex3f(-tamAresta / 2, tamAresta / 2, -tamAresta / 2);
        glVertex3f(-tamAresta / 2, tamAresta / 2, tamAresta / 2);
        glVertex3f(tamAresta / 2, tamAresta / 2, tamAresta / 2);
        glVertex3f(tamAresta / 2, tamAresta / 2, -tamAresta / 2);
        // Bottom Face
        glNormal3f(0, -1, 0);
        glVertex3f(-tamAresta / 2, -tamAresta / 2, -tamAresta / 2);
        glVertex3f(tamAresta / 2, -tamAresta / 2, -tamAresta / 2);
        glVertex3f(tamAresta / 2, -tamAresta / 2, tamAresta / 2);
        glVertex3f(-tamAresta / 2, -tamAresta / 2, tamAresta / 2);
        // Right face
        glNormal3f(1, 0, 0);
        glVertex3f(tamAresta / 2, -tamAresta / 2, -tamAresta / 2);
        glVertex3f(tamAresta / 2, tamAresta / 2, -tamAresta / 2);
        glVertex3f(tamAresta / 2, tamAresta / 2, tamAresta / 2);
        glVertex3f(tamAresta / 2, -tamAresta / 2, tamAresta / 2);
        // Left Face
        glNormal3f(-1, 0, 0);
        glVertex3f(-tamAresta / 2, -tamAresta / 2, -tamAresta / 2);
        glVertex3f(-tamAresta / 2, -tamAresta / 2, tamAresta / 2);
        glVertex3f(-tamAresta / 2, tamAresta / 2, tamAresta / 2);
        glVertex3f(-tamAresta / 2, tamAresta / 2, -tamAresta / 2);
        glEnd();

    }

}
