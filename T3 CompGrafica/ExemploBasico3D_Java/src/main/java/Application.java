import java.io.IOException;

import static de.damios.guacamole.gdx.StartOnFirstThreadHelper.startNewJvmIfRequired;

public class Application {

    //private static final Window WINDOW = Window.getInstance();
    private static final ExemploBasico3D WINDOW = ExemploBasico3D.getInstance();

    public static void main(String[] args) throws IOException {
        // Starts a new JVM if the application was started on macOS without the
        // -XstartOnFirstThread argument.
        if (startNewJvmIfRequired()) {
            System.exit(0);
        }
        WINDOW.run();
    }
}
