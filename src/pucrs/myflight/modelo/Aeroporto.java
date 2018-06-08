package pucrs.myflight.modelo;

public class Aeroporto implements Comparable<Aeroporto> {
	private String codigo;
	private String nome;
	private Geo loc;
	private String codPais;

	//Construtor mantido devido à erro na classe JanelaFX
	public Aeroporto(String codigo, String nome, Geo loc) {
		this.codigo = codigo;
		this.nome = nome;
		this.loc = loc;
	}

	//nao verifica se o codPais existe
	public Aeroporto(String codigo, String nome, Geo loc, String codPais) {
		this.codigo = codigo;
		this.nome = nome;
		this.loc = loc;
		this.codPais = codPais;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public Geo getLocal() {
		return loc;
	}

    @Override
    public String toString() {
        return codigo + " - " + nome + " [" + loc + "]";
    }

	@Override
	public int compareTo(Aeroporto outro) {
		return this.nome.compareTo(outro.nome);
	}
}
