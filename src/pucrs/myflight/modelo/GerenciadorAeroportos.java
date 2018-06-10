package pucrs.myflight.modelo;

import javafx.beans.Observable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GerenciadorAeroportos {

    private ArrayList<Aeroporto> aeroportos;

    public GerenciadorAeroportos() {
        this.aeroportos = new ArrayList<>();
        carregaDados("airports.dat");
    }

    public void ordenarNomes() {
        Collections.sort(aeroportos);
    }

    public void adicionar(Aeroporto aero) {
        aeroportos.add(aero);
    }

    public void carregaDados(String nomeArq){

        Path path2 = Paths.get(nomeArq);
        try (BufferedReader br = Files.newBufferedReader(path2, Charset.defaultCharset()))
        {
            String header = br.readLine();
            String linha = null;
            while((linha = br.readLine()) != null) {
                Scanner sc = new Scanner(linha).useDelimiter(";"); // separador é ;
                String codigo, nome, codPais;
                double latitude, longitude;
                codigo = sc.next();
                latitude = Double.parseDouble(sc.next());
                longitude = Double.parseDouble(sc.next());
                nome = sc.next();
                codPais = sc.next();
                Geo geo = new Geo(latitude, longitude);

                Aeroporto aeroporto = new Aeroporto(codigo, nome, geo, codPais);
                this.aeroportos.add(aeroporto);
            }
        }
        catch (IOException x) {
            System.err.format("Erro na manipulação do arquivo.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Aeroporto> listarTodos() {
        return new ArrayList<>(aeroportos);
    }

    public Aeroporto buscarCodigo(String codigo) {
        for(Aeroporto a: aeroportos)
            if(a.getCodigo().equals(codigo))
                return a;
        return null;
    }

    public ArrayList<Aeroporto> listarAeroportosPorCodCompanhia(String codCompanhia, GerenciadorRotas gr) {
        ArrayList<Rota> rotas = gr.listarRotasPorCodCompanhia(codCompanhia);
        Set<Aeroporto> listaAeroportos = new HashSet<>();
        ArrayList<Aeroporto> origens = rotas.stream().map(x -> x.getOrigem()).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Aeroporto> destinos = rotas.stream().map(x -> x.getDestino()).collect(Collectors.toCollection(ArrayList::new));

        for (Aeroporto aeroporto : origens) {
            listaAeroportos.add(aeroporto);
        }
        for (Aeroporto aeroporto : destinos) {
            listaAeroportos.add(aeroporto);
        }

        return new ArrayList<Aeroporto>(listaAeroportos);
    }
}
