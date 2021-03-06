import glfw.listener.KeyListener;
import logic.Animate;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Random;

import static de.damios.guacamole.gdx.StartOnFirstThreadHelper.startNewJvmIfRequired;
import static geometry.configuration.World.setCoordinatePlane;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ExemploBasico3D {
    private int width;
    private int height;
    private long glfwWindowAddress;

    private static ExemploBasico3D INSTANCE = null;

    private static final int DEFAULT_WIDTH = 650;
    private static final int DEFAULT_HEIGHT = 500;
    private static final int MIN_WIDTH = 650;
    private static final int MIN_HEIGHT = 500;
    private static final int MAX_WIDTH = GL_DONT_CARE;
    private static final int MAX_HEIGHT = GL_DONT_CARE;
    private static final String TITLE = "World of Tanks 2 - Eletric Boogaloo";

    private Animate animate;
    private int ModoDeExibicao = 1;
    private int ModoDeProjecao = 1;
    private double AspectRatio;
    private float angulo = 0f;
    //private Ponto CantoEsquerdo = new Ponto(-20,-1,-10);
    private Vector3f rotacaoAmbiente = new Vector3f(20f, 0f, 0f);
    private boolean isRuinning = true;

    Personagem player;
    private static final int NUM_DE_TEXTURAS = 2;
    private static int[] texturas = {1001, 1011};

    private ExemploBasico3D() {
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public static ExemploBasico3D getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExemploBasico3D();
        }
        return INSTANCE;
    }

    public void run() throws IOException {
        init();
        execution();
        terminateGracefully();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private void init() throws IOException {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        boolean glfwStarted = glfwInit();

        // Inicia Animate
        animate = new Animate();

        // Throw error and terminate if GLFW initialization fails
        if (!glfwStarted) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowAddress = createAndConfigureWindow();

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindowAddress);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        setCoordinatePlane();
        setListeners();
        // Make the window visible
        glfwShowWindow(glfwWindowAddress);

        //WindowResizeListener.getInstance().invoke(glfwWindowAddress, width, height);
        reshape(width, height);

        loadTexture();
        initGL();
    }

    public static void loadTexture() throws IOException {
        //A minha versao java do codigo do matheus ficou meio porca,
        //entao acabei achando uma outra maneira q tlvz faca mais sentido para o java: https://stackoverflow.com/questions/41901468/load-a-texture-within-opengl-and-lwjgl3-in-java/41902221
        // obviamente temos q achar um jeito de mascarar ela
        //se conseguir fazer a do matheus funcionar, melhor.
        //detalhe: o PNGDecoder eh de uma biblioteca externa, vale a pena adicionar? link: https://mvnrepository.com/artifact/org.l33tlabs.twl/pngdecoder/1.0
        glEnable(GL_TEXTURE_2D);
        glGenTextures(texturas);
        glBindTexture(GL_TEXTURE_2D, 1001);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);


        BufferedImage read = ImageIO.read(new File("/home/deividsantos/faculdade/T3-Comp-Grafica/T3 CompGrafica/ExemploBasico3D_Java/src/main/resources/grama.jpg"));
        int largura = read.getWidth();
        int altura = read.getHeight();
        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(4 * read.getWidth() * read.getHeight());
        byte[] buffer = (byte[]) read.getRaster().getDataElements(0, 0, read.getWidth(), read.getHeight(), null);
        imageBuffer.clear();
        imageBuffer.put(buffer);
        imageBuffer.rewind();

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, largura, altura, 0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glBindTexture(GL_TEXTURE_2D, 1011);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        BufferedImage read2 = ImageIO.read(new File("/home/deividsantos/faculdade/T3-Comp-Grafica/T3 CompGrafica/ExemploBasico3D_Java/src/main/resources/img.png"));
        int largura2 = read2.getWidth();
        int altura2 = read2.getHeight();
        ByteBuffer imageBuffer2 = ByteBuffer.allocateDirect(4 * read2.getWidth() * read2.getHeight());
        byte[] buffer2 = (byte[]) read2.getRaster().getDataElements(0, 0, read2.getWidth(), read2.getHeight(), null);
        imageBuffer2.clear();
        imageBuffer2.put(buffer2);
        imageBuffer2.rewind();

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, largura2, altura2, 0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer2);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Fundo de tela preto

        glShadeModel(GL_SMOOTH);
        //glShadeModel(GL_FLAT);
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        if (ModoDeExibicao == 1) // Faces Preenchidas??
        {
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        } else {
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
        glEnable(GL_NORMALIZE);
    }

    /**
     * Executa o loop principal.
     */
    private void execution() {
        player = new Personagem(new Ponto(0, 0, 0), true);
        // This is the main loop
        while (!glfwWindowShouldClose(glfwWindowAddress) && isRuinning) {
            animate.startCounter();

            display();
            glfwPollEvents();

            animate.stopAndShow();
            angulo += 0.1;
        }
    }


    /**
     * Desenha o conteudo do frame a cada iteracao do loop principal.
     */
    private void display() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //DefineLuz();

        PosicUser();
        glMatrixMode(GL_MODELVIEW);

        DesenhaPiso();

        desenhaParede();

        player.draw();

        glfwSwapBuffers(glfwWindowAddress);
    }

    private void desenhaParede() {
        float larg = 0.04f;
        float alt = 0.066f;
        int aux = 0;
        glPushMatrix();
        glTranslated(-12.5, 0, -10);
        for (int i = 0; i < 15; i++) {
            glPushMatrix();
            aux = 0;
            for (int j = -12; j < 13; j++) {
                desenhaParedao(larg, alt, i, aux);
                aux = aux + 1;
                glTranslated(1, 0, 0);
            }
            glPopMatrix();
            glTranslated(0, 1, 0);
        }
        glPopMatrix();
    }

    private void desenhaParedao(float larg, float alt, int x, int z) {

        glBindTexture(GL_TEXTURE_2D, 1011);

        glBegin(GL_QUADS);
        glTexCoord2f(larg * z, alt * x);
        glVertex3f(-0.5f, -0.5f, 0.5f);
        glTexCoord2f(larg * z + larg, alt * x);
        glVertex3f(0.5f, -0.5f, 0.5f);
        glTexCoord2f(larg * z + larg, alt * x + alt);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glTexCoord2f(larg * z, alt * x + alt);
        glVertex3f(-0.5f, 0.5f, 0.5f);

        glTexCoord2f(larg * z + larg, alt * x);
        glVertex3f(-0.5f, -0.5f, -0.5f);
        glTexCoord2f(larg * z + larg, alt * x + alt);
        glVertex3f(-0.5f, 0.5f, -0.5f);
        glTexCoord2f(larg * z, alt * x + alt);
        glVertex3f(0.5f, 0.5f, -0.5f);
        glTexCoord2f(larg * z, alt * x);
        glVertex3f(0.5f, -0.5f, -0.5f);

        glTexCoord2f(larg * z, alt * x + alt);
        glVertex3f(-0.5f, 0.5f, -0.5f);
        glTexCoord2f(larg * z, alt * x);
        glVertex3f(-0.5f, 0.5f, 0.5f);
        glTexCoord2f(larg * z + larg, alt * x);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glTexCoord2f(larg * z + larg, alt * x + alt);
        glVertex3f(0.5f, 0.5f, -0.5f);

        glTexCoord2f(larg * z + larg, alt * x + alt);
        glVertex3f(-0.5f, -0.5f, -0.5f);
        glTexCoord2f(larg * z, alt * x + alt);
        glVertex3f(0.5f, -0.5f, -0.5f);
        glTexCoord2f(larg * z, alt * x);
        glVertex3f(0.5f, -0.5f, 0.5f);
        glTexCoord2f(larg * z + larg, alt * x);
        glVertex3f(-0.5f, -0.5f, 0.5f);

        glTexCoord2f(larg * z + larg, alt * x);
        glVertex3f(0.5f, -0.5f, -0.5f);
        glTexCoord2f(larg * z + larg, alt * x + alt);
        glVertex3f(0.5f, 0.5f, -0.5f);
        glTexCoord2f(larg * z, alt * x + alt);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glTexCoord2f(larg * z, alt * x);
        glVertex3f(0.5f, -0.5f, 0.5f);

        glTexCoord2f(larg * z, alt * x);
        glVertex3f(-0.5f, -0.5f, -0.5f);
        glTexCoord2f(larg * z + larg, alt * x);
        glVertex3f(-0.5f, -0.5f, 0.5f);
        glTexCoord2f(larg * z + larg, alt * x + alt);
        glVertex3f(-0.5f, 0.5f, 0.5f);
        glTexCoord2f(larg * z, alt * x + alt);
        glVertex3f(-0.5f, 0.5f, -0.5f);
        glEnd();

    }

    private void rotacionaAmbiente() {
        glRotated(rotacaoAmbiente.x, 1, 0, 0);
        glRotated(rotacaoAmbiente.y, 0, 1, 0);
        glRotated(rotacaoAmbiente.z, 0, 0, 1);
    }

    // **********************************************************************
    //  void DefineLuz(void)
    // **********************************************************************
    void DefineLuz() {


        // Define cores para um objeto dourado
        float LuzAmbiente[] = {0.4f, 0.4f, 0.4f};
        float LuzDifusa[] = {0.7f, 0.7f, 0.7f};
        float LuzEspecular[] = {0.9f, 0.9f, 0.9f};
        float PosicaoLuz0[] = {2.0f, 3.0f, 0.0f};  // Posi??????o da Luz
        float Especularidade[] = {1.0f, 1.0f, 1.0f};

        // ****************  Fonte de Luz 0

        glEnable(GL_COLOR_MATERIAL);

        // Habilita o uso de ilumina??????o
        glEnable(GL_LIGHTING);

        // Ativa o uso da luz ambiente
        glLightModelfv(GL_LIGHT_MODEL_AMBIENT, LuzAmbiente);
        // Define os parametros da luz n???mero Zero
        glLightfv(GL_LIGHT0, GL_AMBIENT, LuzAmbiente);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, LuzDifusa);
        glLightfv(GL_LIGHT0, GL_SPECULAR, LuzEspecular);
        glLightfv(GL_LIGHT0, GL_POSITION, PosicaoLuz0);
        glEnable(GL_LIGHT0);

        // Ativa o "Color Tracking"
        glEnable(GL_COLOR_MATERIAL);

        // Define a reflectancia do material
        glMaterialfv(GL_FRONT, GL_SPECULAR, Especularidade);

        // Define a concentra??????oo do brilho.
        // Quanto maior o valor do Segundo parametro, mais
        // concentrado ser??? o brilho. (Valores v???lidos: de 0 a 128)
        glMateriali(GL_FRONT, GL_SHININESS, 51);
    }

    /**
     * Cria e configura a instancia da janela grafica.
     *
     * @return endereco da janela
     */
    private long createAndConfigureWindow() {
        // Create the window
        long windowAddress = glfwCreateWindow(this.width, this.height, TITLE, NULL, NULL);

        if (windowAddress == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // glfw window Configuration
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwSetWindowSizeLimits(windowAddress, MIN_WIDTH, MIN_HEIGHT, MAX_WIDTH, MAX_HEIGHT);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        return windowAddress;
    }

    // **********************************************************************
    //  void reshape( int w, int h )
    //		trata o redimensionamento da janela OpenGL
    //
    // **********************************************************************
    void reshape(int w, int h) {

        // Evita divis???o por zero, no caso de uam janela com largura 0.
        if (h == 0) h = 1;
        // Ajusta a rela??????o entre largura e altura para evitar distor??????o na imagem.
        // Veja fun??????o "PosicUser".
        AspectRatio = 1.0f * w / h;
        // Reset the coordinate system before modifying
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        // Seta a viewport para ocupar toda a janela
        glViewport(0, 0, w, h);
        //cout << "Largura" << w << endl;

        width = w;
        height = h;
    }

    /**
     * Define as callbacks da janela.
     */
    private void setListeners() {
        glfwSetKeyCallback(glfwWindowAddress, KeyListener.getInstance());
        //glfwSetWindowSizeCallback(glfwWindowAddress, WindowResizeListener.getInstance());
        glfwSetWindowSizeCallback(glfwWindowAddress, (long window, int width, int height) -> {
            reshape(width, height);
        });
    }

    /**
     * Finaliza o programa, liberando as memorias ocupadas.
     */
    private void terminateGracefully() {
        // Free memory upon leaving
        glfwFreeCallbacks(glfwWindowAddress);
        glfwDestroyWindow(glfwWindowAddress);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    // **********************************************************************
    //  void PosicUser()
    // **********************************************************************
    void PosicUser() {

        // Define os par???metros da proje??????o Perspectiva
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        // Define o volume de visualiza??????o sempre a partir da posicao do
        // observador
        if (ModoDeProjecao == 0) {
            glOrtho(-10, 10, -10, 10, 0, 50); // Projecao paralela Orthografica
        } else {
            //gluPerspective(90,AspectRatio,0.01,50); // Projecao perspectiva
            perspective(90, AspectRatio, 0.01, 50);
        }

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        /*
        gluLookAt(0, 4, 10,   // Posi??????o do Observador
                0,0,0,     // Posi??????o do Alvo
                0.0f,1.0f,0.0f);
*/
        glTranslated(0f, -4f, -10f);
        //rotacionaAmbiente();
    }

    void perspective(double fieldOfView, double aspectRatio, double zNear, double zFar) {
        double fH = Math.tan((fieldOfView / 360.0f * 3.14159f)) * zNear;
        double fW = fH * aspectRatio;
        glFrustum(-fW, fW, -fH, fH, zNear, zFar);
    }

    // Desenhos
    // **********************************************************************
    //  void DesenhaCubo()
    // **********************************************************************
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

    // **********************************************************************
    // void DesenhaLadrilho(int corBorda, int corDentro)
    // Desenha uma c???lula do piso.
    // Eh possivel definir a cor da borda e do interior do piso
    // O ladrilho tem largula 1, centro no (0,0,0) e est??? sobre o plano XZ
    // **********************************************************************
    void DesenhaLadrilho(int corBorda, int corDentro) {
        //defineCor(corDentro);// desenha QUAD preenchido
        glBindTexture(GL_TEXTURE_2D, 1001);
        glBegin(GL_QUADS);
        glNormal3f(0, 1, 0);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-0.5f, 0.0f, -0.5f);
        glTexCoord2f(1.0f, 0.0f);

        glVertex3f(-0.5f, 0.0f, 0.5f);
        glTexCoord2f(1.0f, 1.0f);

        glVertex3f(0.5f, 0.0f, 0.5f);
        glTexCoord2f(1.0f, 0.0f);

        glVertex3f(0.5f, 0.0f, -0.5f);
        glEnd();

        glColor3f(1f, 1f, 1f);
        glBegin(GL_LINE_STRIP);
        glNormal3f(0f, 1f, 0f);
        glTexCoord2f(0.0f, 0.0f);

        glVertex3f(-0.5f, 0.0f, -0.5f);
        glTexCoord2f(0.0f, 0.0f);

        glVertex3f(-0.5f, 0.0f, 0.5f);
        glTexCoord2f(0.0f, 0.0f);

        glVertex3f(0.5f, 0.0f, 0.5f);
        glTexCoord2f(1.0f, 0.0f);

        glVertex3f(0.5f, 0.0f, -0.5f);
        glEnd();
    }

    // **********************************************************************
    //
    //
    // **********************************************************************
    void DesenhaPiso() {
        Random rand = new Random(100); // usa uma semente fixa para gerar sempre as mesma cores no piso

        glPushMatrix();
        //glTranslated(CantoEsquerdo.x, CantoEsquerdo.y, CantoEsquerdo.z);
        glTranslated(-12.5, 0, -43.5);
        //glRotated(30, 1, 0, 0);
        for (int x = 0; x < 25; x++) {
            glPushMatrix();
            //rotacionaAmbiente();
            for (int z = 0; z < 50; z++) {
                DesenhaLadrilho(0, rand.nextInt(50));
                glTranslated(0, 0, 1);
            }
            glPopMatrix();
            glTranslated(1, 0, 0);
        }
        glPopMatrix();
    }

    public static void main(String[] args) throws IOException {
        // Starts a new JVM if the application was started on macOS without the
        // -XstartOnFirstThread argument.
        if (startNewJvmIfRequired()) {
            System.exit(0);
        }

        ExemploBasico3D.getInstance().run();
    }
}
