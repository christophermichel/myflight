package pucrs.myflight.modelo.models;

import pucrs.myflight.modelo.Aeroporto;

public class Conexao {
    private Aeroporto origem;
    private Aeroporto conexao;
    private Aeroporto destino;

    public Conexao(Aeroporto aeroOrigem, Aeroporto aeroConexao, Aeroporto aeroDestino) {
        this.origem = aeroOrigem;
        this.conexao = aeroConexao;
        this.destino = aeroDestino;
    }

    public Aeroporto getOrigem() {
        return origem;
    }

    public Aeroporto getConexao() {
        return conexao;
    }

    public Aeroporto getDestino() {
        return destino;
    }
}

