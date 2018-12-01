package pucrs.myflight.modelo;

public class Pais implements Imprimivel{
    private String codigo;
    private String nome;

    public Pais(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() { return this.codigo; }

    public String getNome() { return this.nome; }

    @Override
    public String toString() { return this.nome + " / " + this.codigo; }

    @Override
    public void imprimir() {
        System.out.println(toString());
    }

}
