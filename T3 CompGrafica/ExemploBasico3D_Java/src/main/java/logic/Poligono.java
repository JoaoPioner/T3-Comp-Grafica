package logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_POINTS;

/**
 * Classe Poligono
 */
public class Poligono{
    /**
     * Lista contendo os vertices do poligono
     */
    List<Ponto> Vertices;
    /**
     * Valores minimos e maximos do poligono nos eixos
     */
    Ponto Min, Max;

    /**
     * Construtor
     */
    public Poligono(){
        Vertices = new ArrayList<Ponto>();
    }
    
    /**
     * Retorna o vertice na posicao escolhina
     * 
     * @param index posicao do vertice 
     * @return ponto na posicao informada
     */
    public Ponto getVertice(int index){
        return Vertices.get(index);
    }

    /**
     * Retorna a quantidade de vertices que compoem o poligono
     * 
     * @return quantidade de vertices no poligono
     */
    public long getNVertices(){
        return Vertices.size();
    }

    /**
     * Insere o vertice na ultima posicao da lista do poligono
     * 
     * @param p vertice a ser inserido
     */
    public void insereVertice(Ponto p){
        Vertices.add(p);
    }

    /**
     * Insere um vertice em uma posicao especifica
     * 
     * @param p vertice a ser inserido
     * @param pos posicao do poligono a inserir o vertice
     */
    public void insereVertice(Ponto p, int pos){
        if ((pos < 0) || (pos>Vertices.size()))
        {
            String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            System.out.println("Metodo " + nameofCurrMethod + ". Posicao Invalida. Vertice nao inserido.");
            return;
        }

        Vertices.add(pos, p);
    }

    /**
     * Desenha o poligon utilizando as primitivas do OpenGL
     */
    public void desenhaPoligono(){
        glBegin(GL_LINE_LOOP);
        for (int i=0; i<Vertices.size(); i++)
            glVertex3f(Vertices.get(i).x, Vertices.get(i).y, Vertices.get(i).z);
        glEnd();
    }

    /**
     * Desenha os verticesd do poligono utilizando as primitivas do OpenGL
     */
    public void desenhaVertices(){
        glBegin(GL_POINTS);
        for (int i=0; i<Vertices.size(); i++)
            glVertex3f(Vertices.get(i).x, Vertices.get(i).y, Vertices.get(i).z);
        glEnd();
    }

    /**
     * Desenha o poligon utilizando as primitivas do OpenGL
     */
    public void pintaPoligono(){
        glBegin(GL_POLYGON);
        for (int i=0; i<Vertices.size(); i++)
            glVertex3f(Vertices.get(i).x, Vertices.get(i).y, Vertices.get(i).z);
        glEnd();
    }

    /**
     * Imprime os vertices do poligono
     */
    public void imprime(){
        for (int i=0; i<Vertices.size(); i++)
            Vertices.get(i).imprime();
    }

    /**
     * Ainda nao implementado
     */
    public void atualizaLimites(){
        // Ainda nao implementado!
    }

    /**
     * Calcula os limites do poligono.
     * 
     * @return Uma estrutura contendo o ponto minimo e o ponto maximo do poligono
     */
    public Limite obtemLimites(){
        Ponto Min, Max;
        Max = Min = Vertices.get(0);
    
        for (int i=0; i<Vertices.size(); i++)
        {
            Min = ObtemMinimo (Vertices.get(i), Min);
            Max = ObtemMaximo (Vertices.get(i), Max);
        }

        return new Limite(Min, Max);
    }

    /**
     * Calcula o valor minimo atingido pelo dois pontos informados.
     * 
     * @param p1 ponto 1
     * @param p2 ponto 2
     * @return O valor minimo atingido pelos eixos dos pontos
     */
    private Ponto ObtemMinimo(Ponto p1, Ponto p2){
        // Ainda nao implementado!
        return null;
    }

    /**
     * Calcula o valor maximo atingido pelo dois pontos informados.
     * 
     * @param p1 ponto 1
     * @param p2 ponto 2
     * @return O valor maximo atingido pelos eixos dos pontos
     */
    private Ponto ObtemMaximo(Ponto p1, Ponto p2){
        // Ainda nao implementado!
        return null;
    }

    /**
     * Le as coordenadas de pontos em um arquivo e as define como um poligono na classe.
     * Estrutura do Arquivo:
     *      numero de vertices
     *      coord_x_1, coord_y_1
     *      coord_x_2, coord_y_2
     *      ...
     * 
     * @param nomeArquivo nome do arquivo que sera lido.
     */
    public void LePoligono(String nomeArquivo){
        try (BufferedReader file = new BufferedReader(new FileReader(nomeArquivo))) {
            String line;
            float x, y;
            String[] tokens;
            long qtdVertices;

            qtdVertices = Long.parseLong((file.readLine()));

            for (long i=0; i<qtdVertices; i++){
                line = file.readLine();
                tokens = line.split(" ");

                x = Float.parseFloat(tokens[0]);
                y = Float.parseFloat(tokens[1]);

                insereVertice(new Ponto(x, y));
            }
            
            System.out.println("Poligono lido com sucesso!");

        }catch (FileNotFoundException error){
            System.out.println("Erro ao abrir arquvio " + nomeArquivo + ".");

        }catch (IOException error){
            System.out.println("Erro ao ler dado do arquivo.");

        }
    }
}