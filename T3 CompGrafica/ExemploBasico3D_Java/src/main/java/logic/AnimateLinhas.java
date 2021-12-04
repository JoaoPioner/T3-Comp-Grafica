package logic;

public class AnimateLinhas {
    
    private int nFrames;
    private double tempoTotal;
    private long oldTime;
    private long nowTime;

    private int contadorChamadas;
    private int contadorInt;

    public AnimateLinhas(){
        nFrames = 0;
        tempoTotal = 0.0;
        oldTime = 0;

        contadorChamadas = 0;
        contadorInt = 0;
    }

    public void startCounter(){
        oldTime = System.currentTimeMillis();
    }
    
    public void stopAndShow()
    {
        nowTime = System.currentTimeMillis();
        double dt = (nowTime - oldTime) / 1000.0;

        tempoTotal += dt;
        nFrames++;
        oldTime = nowTime;

        if (tempoTotal > 5.0)
        {
            System.out.println("------------------------------------------");
            System.out.println(String.format("Tempo Acumulado: %.2f  segundos.", tempoTotal));
            System.out.println(String.format("Nros de Frames sem desenho: %d", nFrames));
            System.out.println(String.format("FPS(sem desenho): %d", (int)(nFrames/tempoTotal)));

            tempoTotal = 0;
            nFrames = 0;
            
            System.out.println(String.format("Contador de Intersecoes Existentes: %d", (int)(contadorInt/2.0)));
            System.out.println(String.format("Contador de Chamadas: %d", contadorChamadas));
            System.out.println("------------------------------------------");
        }
    }

    public void SetContadores(int contadorChamadas, int contadorInt){
        this.contadorChamadas = contadorChamadas;
        this.contadorInt = contadorInt;
    }
}
