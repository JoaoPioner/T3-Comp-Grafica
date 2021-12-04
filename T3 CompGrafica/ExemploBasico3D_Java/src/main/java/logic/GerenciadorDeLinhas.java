package logic;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glLineWidth;

/**
 * Classe Gerenciador de Linhas.
 * 
 * Gerencia a criação e desenho das linhas.
 */
public class GerenciadorDeLinhas{
    /**
     * Contadores
     */
    public int contadorChamada, contadorInt;

    /**
     * Vetor contendo as linhas instanciadas
     */
    private Linha[] linhas;

    /**
     * Construtor
     * 
     * @param numeroDeLinhas Quantidade de linhas a serem instanciadas
     */
    public GerenciadorDeLinhas(int numeroDeLinhas){
        contadorChamada = contadorInt = 0;
        linhas = new Linha[numeroDeLinhas];

        for(int i=0; i<numeroDeLinhas; i++){
            linhas[i] = new Linha();
        }
    }

    /**
     * Gera as linhas e armazenas no vetor linhas.
     * 
     * @param maxX Valor maximo de x
     * @param tamanhoMax Tamanho maximo da linha
     */
    public void geraLinhas(int maxX, int tamanhoMax){
        for(int i=0; i<linhas.length; i++){
            linhas[i].geraLinha(maxX, tamanhoMax);
        }
    }

    /**
     * Desenha as linhas contidas no vetor
     */
    public void desenhaLinhas(){
        glColor3f(0,1,0);

        for(int i=0; i< linhas.length; i++)
            linhas[i].desenhaLinha();
    }

    /**
     * Computa as linhas com interseccao e as desenha em vermelho.
     */
    public void desenhaCenario()
    {
        Ponto pa = new Ponto();
        Ponto pb = new Ponto();
        Ponto pc = new Ponto();
        Ponto pd = new Ponto();

        contadorChamada = 0;
        contadorInt = 0;
        
        // Desenha as linhas do cenário
        glLineWidth(1);
        glColor3f(1,0,0);
        
        for(int i=0; i< linhas.length; i++)
        {
            pa.set(linhas[i].x1, linhas[i].y1);
            pb.set(linhas[i].x2, linhas[i].y2);

            for(int j=0; j< linhas.length; j++)
            {
                pc.set(linhas[j].x1, linhas[j].y1);
                pd.set(linhas[j].x2, linhas[j].y2);

                contadorChamada++;

                if (haInterseccao(pa, pb, pc, pd))
                {
                    contadorInt ++;
                    linhas[i].desenhaLinha();
                    linhas[j].desenhaLinha();
                }
            }
        }
    }

    /**
     * Verifica se a interseccao entre duas linhas (4 pontos)
     * 
     * @param k Ponto A da linha 1
     * @param l Ponto B da linha 1
     * @param m Ponto A da linha 2
     * @param n Ponto B da linha 2
     * @return true se houver interseccao, false caso contrario
     */
    public boolean haInterseccao(Ponto k, Ponto l, Ponto m, Ponto n)
    {
        Interseccao resultInterseccao = intersec2d( k,  l,  m,  n);
        
        if (!resultInterseccao.ocorreu) return false;

        return resultInterseccao.s>=0.0f && resultInterseccao.s <=1.0f && resultInterseccao.t>=0.0f && resultInterseccao.t<=1.0f;
    }

    /**
     * Calcula a interseccao 2d de duas linhas
     * 
     * @param k Ponto A da linha 1
     * @param l Ponto B da linha 1
     * @param m Ponto A da linha 2
     * @param n Ponto B da linha 2
     * @return objeto Interseccao, contendo uma flag sinalizando se houve a interseccao, junto com o local do encontro.
     */
    public Interseccao intersec2d(Ponto k, Ponto l, Ponto m, Ponto n)
    {
        float det, s, t;

        det = (n.x - m.x) * (l.y - k.y)  -  (n.y - m.y) * (l.x - k.x);

        if (det == 0.0f)
            return new Interseccao(false) ; // nao ha interseccao

        s = ((n.x - m.x) * (m.y - k.y) - (n.y - m.y) * (m.x - k.x))/ det ;
        t = ((l.x - k.x) * (m.y - k.y) - (l.y - k.y) * (m.x - k.x))/ det ;

        return new Interseccao(true, s, t); // ha interseccao
    }

    /**
     * Classe representando o resultado de uma interseccao
     */
    public class Interseccao{
        public boolean ocorreu;
        public float s, t;

        public Interseccao(boolean ocorreu){
            this(ocorreu, 0.0f, 0.0f);
        }

        public Interseccao(boolean ocorreu, float s, float t){
            this.ocorreu = ocorreu;
            this.s = s;
            this.t = t;
        }
    }
}