package logic;

public class Limite {
    private Ponto Min, Max;

    public Limite(Ponto Min, Ponto Max){
        this.Min = Min;
        this.Max = Max;
    }

    public Ponto GetMin() { return Min; }
    public Ponto GetMax() { return Max; }
}
