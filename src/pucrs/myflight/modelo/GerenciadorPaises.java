package pucrs.myflight.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class GerenciadorPaises {
    private ArrayList<Pais> paises;

    public GerenciadorPaises() {
        this.paises = new ArrayList<>();

        Path path2 = Paths.get("countries.dat");
        try (BufferedReader br = Files.newBufferedReader(path2, Charset.defaultCharset()))
        {
            String header = br.readLine();
            String linha = null;
            while((linha = br.readLine()) != null) {
                Scanner sc = new Scanner(linha).useDelimiter(";"); // separador é ;
                String codigo, nome;

                codigo = sc.next();
                nome = sc.next();

                Pais pais = new Pais(codigo, nome);
                this.paises.add(pais);
            }
        }
        catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
        catch (Exception e) {

        }
    }

    public void adicionar(Pais pais) { this.paises.add(pais); }

    public ArrayList<Pais> listarTodas() { return new ArrayList<>(paises); }

    public Pais buscarPorCodigo(String codigo) {
        for (Pais p : paises)
            if (p.getCodigo().equals(codigo))
                return p;
        return null;
    }

    public void ordenaCodigo() { paises.sort(Comparator.comparing(Pais::getCodigo));}

    public void ordenaNome() { paises.sort(Comparator.comparing(Pais::getNome)); }


}
