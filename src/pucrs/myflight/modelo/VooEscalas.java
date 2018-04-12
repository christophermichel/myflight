package pucrs.myflight.modelo;

import java.time.Duration;
import java.time.LocalDateTime;

public class VooEscalas extends Voo {

    private Rota rotaFinal;

    public VooEscalas(Rota rota, Rota rotaFinal,
         LocalDateTime datahora, Duration duracao) {
       super(rota, datahora, duracao);
//       this.rota = rota;
//       this.datahora = datahora;
//       this.duracao = duracao;
       this.rotaFinal = rotaFinal;
    }

    public Rota getRotaFinal() {
        return rotaFinal;
    }

    @Override
    public String toString() {
        return super.toString() + " -> " + rotaFinal;
//        return getRota() + "->" + rotaFinal + " : " +
//                getDatahora() + " [" +  getDuracao() + "] - "
//                + getStatus();
    }
}