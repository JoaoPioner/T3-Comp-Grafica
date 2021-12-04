package logic;

public class Animate {
    
    private int nFrames;
    private double tempoTotal;
    private long oldTime;
    private long nowTime;
    private int fps;

    public Animate(){
        nFrames = 0;
        tempoTotal = 0.0;
        oldTime = 0;
        fps = 60;
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
            fps = (int) (nFrames / tempoTotal);

            System.out.println("------------------------------------------");
            System.out.println(String.format("Tempo Acumulado: %.2f  segundos.", tempoTotal));
            System.out.println(String.format("Nros de Frames sem desenho: %d", nFrames));
            System.out.println(String.format("FPS(sem desenho): %d", fps));

            tempoTotal = 0;
            nFrames = 0;
        }
    }

    public int getFPS(){ return fps; }
}
