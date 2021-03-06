import glfw.listener.KeyListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Personagem {
    public Ponto posicao;
    public Ponto direcao;
    public float angulo = 0;
    public float anguloCanhao = 0;
    public float spd = 0.1f;
    public boolean jogador;

    public Personagem(Ponto posicao, boolean jogador) {
        this.posicao = posicao;
        this.jogador = jogador;
        this.direcao = new Ponto(0, 0, 1);
    }

    void keyListenerExample() {
        if (jogador) {
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_A)) {
                if (angulo > 360) {
                    angulo = 0;
                }
                angulo += 1;
                double v = Math.toRadians(angulo);
                direcao.x = (float) (direcao.x * Math.cos(v) - direcao.z * Math.sin(v));
                direcao.z = (float) (direcao.x * Math.sin(v) + direcao.z * Math.cos(v));
            }
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_D)) {
                if (angulo < 0) {
                    angulo = 360;
                }
                angulo -= 1;

                double v = Math.toRadians(angulo);
                direcao.x = (float) (direcao.x * Math.cos(v) - direcao.z * Math.sin(v));
                direcao.z = (float) (direcao.x * Math.sin(v) + direcao.z * Math.cos(v));
            }
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_W)) {
                posicao.z -= spd * Math.sin(angulo * 3.14159 / 180);
                posicao.x += spd * Math.cos(angulo * 3.14159 / 180);
            }
            if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_S)) {
                if (anguloCanhao < 50) {
                    anguloCanhao++;
                } else {
                    anguloCanhao = -50;
                }
            }
        }
    }

    void draw() {
        glPushMatrix();
        glTranslatef(posicao.x, posicao.y, posicao.z);
        glRotatef(angulo, 0, 1, 0);
        glColor3f(1, 0, 0);
        keyListenerExample();
        DesenhaParalelepipedo();
        glPopMatrix();
    }

    void DesenhaParalelepipedo() {
        glPushMatrix();
        glTranslatef(0, 0, 0);
        glScalef(3, 1, 2);
        //glutSolidCube(2);
        DesenhaCubo(1);
        glPopMatrix();
        glPushMatrix();
        glColor3f(1, 1, 0);
        glRotatef(anguloCanhao, 0, 0, 1);
        glTranslatef(-0.8f, 0, 0);
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
