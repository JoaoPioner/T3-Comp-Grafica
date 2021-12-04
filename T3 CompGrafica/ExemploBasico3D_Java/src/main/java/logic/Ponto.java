
package logic;

/**
 * Class Ponto
 */
public class Ponto {
    /**
     * Eixos
     */
    public float x,y,z;
    
    /**
     * Contrutor
     */
    public Ponto (){
        x = y = z = 0.0f;
    }

    /**
     * Contrutor 
     * 
     * @param x valor do ponto no eixo x
     * @param y valor do ponto no eixo y
     */
    public Ponto(float x, float y){
        this(x, y, 0.0f);
    }

    /**
     * Construtor
     * 
     * @param x valor do ponto no eixo x
     * @param y valor do ponto no eixo y
     * @param z valor do ponto no eixo z
     */
    public Ponto(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Define novos valores para x e y
     * 
     * @param x novo valor de x
     * @param y novo valor de y
     */
    public void set(float x, float y){
        set(x, y, 0.0f);
    }

    /**
     * Define novos valores para x, y e z
     * 
     * @param x novo valor de x
     * @param y novo valor de y
     * @param z novo valor de z
     */
    public void set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Imprime os valores x e y do ponto
     */
    public void imprime(){
        System.out.println("("+x+","+y+")");
    }

    /**
     * Multiplica o ponto atual pelos valores informados
     * 
     * @param x valor a multiplicar x
     * @param y valor a multiplicar y
     * @param z valor a multiplicar z
     */
    public void multiplica(float x, float y, float z){
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    /**
     * Soma os valores atuais do ponto com os informados
     * 
     * @param x valor a somar x
     * @param y valor a somar y
     * @param z valor a somar z
     */
    public void soma(float x, float y, float z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Obtem o valor minimo entre dois pontos
     * 
     * @param p1 Ponto A
     * @param p2 Ponto B
     * @return Ponto, representando o menor ponto possivel entros os informados
     */
    public Ponto ObtemMinimo(Ponto p1, Ponto p2){
        Ponto min = new Ponto();
        
        min.x = (p2.x < p1.x) ? p2.x : p1.x;
        min.y = (p2.y < p1.y) ? p2.y : p1.y;
        min.z = (p2.z < p1.x) ? p2.z : p1.z;

        return min;
    }

    /**
     * Obtem o valor maximo entre dois pontos
     * 
     * @param p1 Ponto A
     * @param p2 Ponto B
     * @return Ponto, representando o maior ponto possivel entros os informados
     */
    public Ponto ObtemMaximo(Ponto p1, Ponto p2){
        Ponto max = new Ponto();
    
        max.x = (p2.x > p1.x) ? p2.x : p1.x;
        max.y = (p2.y > p1.y) ? p2.y : p1.y;
        max.z = (p2.z > p1.x) ? p2.z : p1.z;
        return max;
    }
}