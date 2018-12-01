package pucrs.myflight.modelo.models;

import pucrs.myflight.modelo.Aeroporto;

public class TrafegoAeroporto {
    Aeroporto aeroporto;
    int numeroDeRotas;

    public TrafegoAeroporto(Aeroporto aero, int numRotas) {
        this.aeroporto = aero;
        this.numeroDeRotas = numRotas;
    }

    public Aeroporto getAeroporto() { return this.aeroporto; }

    public int getNumeroDeRotas() { return this.numeroDeRotas; }
}
