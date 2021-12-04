

import glfw.listener.KeyListener;
import logic.AnimateLinhas;
import logic.GerenciadorDeLinhas;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Color;

import java.util.Objects;

import static geometry.configuration.World.setCoordinatePlane;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static util.Color.WHITE;

import static de.damios.guacamole.gdx.StartOnFirstThreadHelper.startNewJvmIfRequired;

public class Window {
    private int width;
    private int height;
    private long glfwWindowAddress;

    private static Window INSTANCE = null;

    private static final int DEFAULT_WIDTH = 650;
    private static final int DEFAULT_HEIGHT = 500;
    private static final int MIN_WIDTH = 650;
    private static final int MIN_HEIGHT = 500;
    private static final int MAX_WIDTH = GL_DONT_CARE;
    private static final int MAX_HEIGHT = GL_DONT_CARE;
    private static final String TITLE = "Algorimos de Calculo de Colisao";
    private static final Color BACKGROUND_COLOR = WHITE;

    private AnimateLinhas animate;
    private GerenciadorDeLinhas gerenciadorDeLinhas;
    private final int MAX_X = 100;  // Coordenada X maxima da janela
    private final int TAMANHO_MAXIMO = 10;  // Tamanho maximo da linha

    private Window() {
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public static Window getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Window();
        }
        return INSTANCE;
    }

    public void run() {
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

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        boolean glfwStarted = glfwInit();

        // Inicia as variaveis de geracao e desenha das linhas
        animate = new AnimateLinhas();
        gerenciadorDeLinhas = new GerenciadorDeLinhas(50);
        gerenciadorDeLinhas.geraLinhas(MAX_X, TAMANHO_MAXIMO);
        animate.SetContadores(gerenciadorDeLinhas.contadorChamada, gerenciadorDeLinhas.contadorInt);

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

        reshape(width, height);
    }

    /**
     * Executa o loop principal.
     */
    private void execution() {
        // This is the main loop
        while (!glfwWindowShouldClose(glfwWindowAddress)) {
            animate.startCounter();

            keyListenerExample();
            display();
            glfwPollEvents();

            animate.stopAndShow();
        }
    }

    /**
    * This is an example on how to use the implemented {@link KeyListener}
    * In this example, color is set from red to blue while the SPACE key is pressed on the keyboard
    * */
    private void keyListenerExample() {
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_SPACE)) {
            gerenciadorDeLinhas.geraLinhas(MAX_X, TAMANHO_MAXIMO);
        }
    }

    /**
     * Desenha o conteudo do frame a cada iteracao do loop principal.
     */
    private void display() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(), BACKGROUND_COLOR.getBlue(), 0.0f);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        gerenciadorDeLinhas.desenhaLinhas();
        gerenciadorDeLinhas.desenhaCenario();
        animate.SetContadores(gerenciadorDeLinhas.contadorChamada, gerenciadorDeLinhas.contadorInt);
        
        glfwSwapBuffers(glfwWindowAddress);
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

    /**
     * Define as callbacks da janela.
     */
    private void setListeners() {
        glfwSetKeyCallback(glfwWindowAddress, KeyListener.getInstance());
        glfwSetWindowSizeCallback(glfwWindowAddress, (long window, int width, int height) -> {
            reshape(width, height);
        });
    }

    private void reshape(int width, int height) {
        this.width = width;
        this.height = height;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        setCoordinatePlane();
        glViewport(0, 0, width, height);
        //glfwSetWindowAspectRatio(window, width, height);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
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

    public static void main(String[] args) {
        // Starts a new JVM if the application was started on macOS without the
        // -XstartOnFirstThread argument.
        if (startNewJvmIfRequired()) {
            System.exit(0);
        }

        Window.getInstance().run();
    }
}
