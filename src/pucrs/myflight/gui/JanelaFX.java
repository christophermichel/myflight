package pucrs.myflight.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


import javax.swing.SwingUtilities;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pucrs.myflight.modelo.*;
import pucrs.myflight.modelo.models.Conexao;
import pucrs.myflight.modelo.models.TrafegoAeroporto;

public class JanelaFX extends Application {

	final SwingNode mapkit = new SwingNode();

	private GerenciadorCias gerCias;
	private GerenciadorAeroportos gerAero;
	private GerenciadorRotas gerRotas;
	private GerenciadorAeronaves gerAvioes;

	private GerenciadorPaises gerPaises;
	private GerenciadorMapa gerenciador;

	private EventosMouse mouse;

	private ObservableList<CiaAerea> comboCiasData;
	private ComboBox<CiaAerea> comboCia;
	private ComboBox<Pais> comboPais;
	private ComboBox<Aeroporto> comboAero;
	private ComboBox<Aeroporto> comboAero2;

	private  TextField tempo;

	@Override
	public void start(Stage primaryStage) throws Exception {

		setup();

		GeoPosition poa = new GeoPosition(-30.05, -51.18);
		gerenciador = new GerenciadorMapa(poa, GerenciadorMapa.FonteImagens.VirtualEarth);
		mouse = new EventosMouse();
		gerenciador.getMapKit().getMainMap().addMouseListener(mouse);
		gerenciador.getMapKit().getMainMap().addMouseMotionListener(mouse);

		createSwingContent(mapkit);

		BorderPane pane = new BorderPane();
		GridPane leftPane = new GridPane();

		leftPane.setAlignment(Pos.CENTER);
		leftPane.setHgap(10);
		leftPane.setVgap(10);
		leftPane.setPadding(new Insets(10, 10, 10, 10));

		Button btnConsulta1 = new Button("Aeroportos de uma Cia");
		Button btnConsulta2 = new Button("Volume de Trafego");
		Button btnConsulta3 = new Button("Rotas entre Aeroportos");
		Button btnConsulta4 = new Button("Aeroportos Próximos");

		leftPane.add(btnConsulta1, 0, 0);
		leftPane.add(btnConsulta2, 2, 0);
		leftPane.add(btnConsulta3, 3, 0);
		leftPane.add(btnConsulta4, 4, 0);

		btnConsulta1.setOnAction(e -> {
			aeroportosDeUmaCia(leftPane);
		});

		btnConsulta2.setOnAction(e -> {
			volumeDeTrafego(leftPane);
		});

		btnConsulta3.setOnAction(e -> {
			rotasEntreAeroportos(leftPane);
		});

		btnConsulta4.setOnAction(e -> {
			mostrarTodosOsAeroportosAlcançáveisAtéUmTempo(leftPane);
		});
		pane.setCenter(mapkit);
		pane.setTop(leftPane);

		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mapas com JavaFX");
		primaryStage.show();

	}

	// Inicializando os dados aqui...
	private void setup() {

		gerCias = new GerenciadorCias();
		gerAero = new GerenciadorAeroportos();
		gerRotas = new GerenciadorRotas();
		gerAvioes = new GerenciadorAeronaves();
		gerPaises = new GerenciadorPaises();
	}

	private void consulta1() {

		// Lista para armazenar o resultado da consulta
		List<MyWaypoint> lstPoints = new ArrayList<>();

		Aeroporto poa = new Aeroporto("POA", "Salgado Filho", new Geo(-29.9939, -51.1711));
		Aeroporto gru = new Aeroporto("GRU", "Guarulhos", new Geo(-23.4356, -46.4731));
		Aeroporto lis = new Aeroporto("LIS", "Lisbon", new Geo(38.772,-9.1342));
		Aeroporto mia = new Aeroporto("MIA", "Miami International", new Geo(25.7933, -80.2906));

		gerenciador.clear();
		Tracado tr = new Tracado();
		tr.setLabel("Teste");
		tr.setWidth(5);
		tr.setCor(new Color(0,0,0,60));
		tr.addPonto(poa.getLocal());
		tr.addPonto(mia.getLocal());

		gerenciador.addTracado(tr);

		Tracado tr2 = new Tracado();
		tr2.setWidth(5);
		tr2.setCor(Color.BLUE);
		tr2.addPonto(gru.getLocal());
		tr2.addPonto(lis.getLocal());
		gerenciador.addTracado(tr2);

		// Adiciona os locais de cada aeroporto (sem repetir) na lista de
		// waypoints

		lstPoints.add(new MyWaypoint(Color.RED, poa.getCodigo(), poa.getLocal(), 5));
		lstPoints.add(new MyWaypoint(Color.RED, gru.getCodigo(), gru.getLocal(), 5));
		lstPoints.add(new MyWaypoint(Color.RED, lis.getCodigo(), lis.getLocal(), 5));
		lstPoints.add(new MyWaypoint(Color.RED, mia.getCodigo(), mia.getLocal(), 5));

		// Para obter um ponto clicado no mapa, usar como segue:
		// GeoPosition pos = gerenciador.getPosicao();

		// Informa o resultado para o gerenciador
		gerenciador.setPontos(lstPoints);

		// Quando for o caso de limpar os traçados...
		// gerenciador.clear();

		gerenciador.getMapKit().repaint();
	}

	private void  aeroportosDeUmaCia(GridPane leftPane) {
		List<MyWaypoint> lstPoints = new ArrayList<>();
		gerenciador.clear();

		comboCia = new ComboBox(FXCollections.observableList(gerCias.listarTodas()));
		comboCia.setPromptText("Selecione a Cia...");
		leftPane.add(comboCia, 0, 1);
		comboCia.setMaxSize(200,200);
		Button consultar = new Button("Consultar");
		leftPane.add(consultar, 0,2);
		consultar.setOnAction(e -> {
			ArrayList<Aeroporto> aeroportos = gerAero.listarAeroportosPorCodCompanhia(comboCia.getValue().getCodigo(), gerRotas);

			for (Aeroporto a: aeroportos) {
				lstPoints.add(new MyWaypoint(Color.RED, a.getCodigo(), a.getLocal(), 10));
			}

			ArrayList<Rota> rotas = gerRotas.listarRotasPorCodCompanhia(comboCia.getValue().getCodigo());

			for(Rota rota : rotas) {
				Tracado tr = new Tracado();
				tr.setWidth(2);
				tr.setCor(Color.ORANGE);
				tr.addPonto(rota.getOrigem().getLocal());
				tr.addPonto(rota.getDestino().getLocal());
				gerenciador.addTracado(tr);
			}

			gerenciador.alterarCentro(aeroportos.get(0).getLocal());
			gerenciador.setPontos(lstPoints);
			gerenciador.getMapKit().repaint();
		});

	}

	private void volumeDeTrafego(GridPane leftPane) {
		List<MyWaypoint> lstPoints = new ArrayList<>();
		gerenciador.clear();

		Button buscaVolumeNoMundo = new Button("No mundo");
		Button buscaVolumeEmUmPais = new Button("Em um país");
		leftPane.add(buscaVolumeNoMundo, 2, 1);
		leftPane.add(buscaVolumeEmUmPais, 2, 2);

		buscaVolumeNoMundo.setOnAction(e -> {
			ArrayList<TrafegoAeroporto> ta = gerAero.estimativaTrafegoPorAeroporto(gerRotas);
			for (TrafegoAeroporto trafego: ta) {
				lstPoints.add(new MyWaypoint(Color.GREEN, trafego.getAeroporto().getCodigo(), trafego.getAeroporto().getLocal(), trafego.getNumeroDeRotas()/4));
			}
			gerenciador.setPontos(lstPoints);
			gerenciador.getMapKit().repaint();
		});

		buscaVolumeEmUmPais.setOnAction(e -> {
			comboPais = new ComboBox(FXCollections.observableList(gerPaises.listarTodas()));
			comboPais.setPromptText("Selecione o país...");
			comboPais.setMaxSize(200, 200);
			leftPane.add(comboPais, 2,3);
			Button consultar = new Button("Consultar");
			leftPane.add(consultar, 2,4);
			consultar.setOnAction(x -> {
				ArrayList<TrafegoAeroporto> ta = gerAero.estimativaTrafegoPorAeroporto(gerRotas, comboPais.getValue().getCodigo());
				for (TrafegoAeroporto trafego: ta) {
					lstPoints.add(new MyWaypoint(Color.YELLOW, trafego.getAeroporto().getCodigo(), trafego.getAeroporto().getLocal(), trafego.getNumeroDeRotas()));
				}
				gerenciador.setPontos(lstPoints);
				gerenciador.getMapKit().repaint();
			});

		});
	}

	private void rotasEntreAeroportos(GridPane leftPane) {
		List<MyWaypoint> lstPoints = new ArrayList<>();
		gerenciador.clear();

		comboAero = new ComboBox(FXCollections.observableList(gerAero.listarTodos()));
		comboAero.setPromptText("Aeroporto de origem...");
		comboAero.setMaxSize(200, 200);
		comboAero2 = new ComboBox(FXCollections.observableList(gerAero.listarTodos()));
		comboAero2.setPromptText("Aeroporto de destino...");
		comboAero2.setMaxSize(200, 200);
		leftPane.add(comboAero, 3, 1);
		leftPane.add(comboAero2, 3, 2);
		Button consultar = new Button("Consultar");
		leftPane.add(consultar, 3, 3);

		consultar.setOnAction(e -> {

			Aeroporto origem = comboAero.getValue();
			Aeroporto destino = comboAero2.getValue();

			ArrayList<Conexao> conexoes = gerAero.listarTrajetosComUmaConexao(origem, destino, gerRotas);

			for (Conexao conexao: conexoes) {

				MyWaypoint pontoOrigem = new MyWaypoint(Color.ORANGE, conexao.getOrigem().getCodigo(), conexao.getOrigem().getLocal(), 10);
				MyWaypoint pontoConexao = new MyWaypoint(Color.ORANGE, conexao.getConexao().getCodigo(), conexao.getConexao().getLocal(), 10);
				MyWaypoint pontoDestino = new MyWaypoint(Color.ORANGE, conexao.getDestino().getCodigo(), conexao.getDestino().getLocal(), 10);

				Tracado tr = new Tracado();
				tr.setWidth(2);
				tr.setCor(Color.CYAN);
				tr.addPonto(conexao.getOrigem().getLocal());
				tr.addPonto(conexao.getConexao().getLocal());
				tr.addPonto(conexao.getDestino().getLocal());

				lstPoints.add(pontoOrigem);
				lstPoints.add(pontoConexao);
				lstPoints.add(pontoDestino);
				gerenciador.addTracado(tr);
				gerenciador.setPontos(lstPoints);
			}
			gerenciador.alterarCentro(origem.getLocal());
			gerenciador.getMapKit().repaint();
		});

	}

	private void mostrarTodosOsAeroportosAlcançáveisAtéUmTempo(GridPane leftPane) {
		List<MyWaypoint> lstPoints = new ArrayList<>();
		gerenciador.clear();

		comboAero = new ComboBox(FXCollections.observableList(gerAero.listarTodos()));
		comboAero.setPromptText("Selecione o aeroporto...");
		comboAero.setMaxSize(200,200);
		leftPane.add(comboAero, 4, 1);
		leftPane.add(tempo = new TextField(), 4, 2);
		Button consultar = new Button("Consultar");
		leftPane.add(consultar, 4, 3);

		tempo.setPromptText("Numero de horas...");
		tempo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
				if (!newValue.matches("\\d*")) {
					tempo.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		consultar.setOnAction(e -> {

			double numeroHoras = Double.parseDouble(tempo.getText());

			ArrayList<Aeroporto> aeroportosPossiveisComVooDireto = gerAero.listarAeroportosAlcancaveisAteUmTempoVooDireto(comboAero.getValue(), numeroHoras, gerRotas);

			//Fazer traçado com cores distintas para vôos diretos e segundo vôo.
			for(Aeroporto destino: aeroportosPossiveisComVooDireto) {
				Tracado tr = new Tracado();
				tr.setWidth(3);
				tr.setCor(Color.GREEN);
				tr.addPonto(comboAero.getValue().getLocal());
				tr.addPonto(destino.getLocal());
				gerenciador.addTracado(tr);
				lstPoints.add(new MyWaypoint(Color.blue, destino.getCodigo(), destino.getLocal(), 5));
			}

			for(Conexao c: gerAero.listarAeroportosAlcancaveisAteUmTempo(comboAero.getValue(), numeroHoras, gerRotas)) {
				lstPoints.add(new MyWaypoint(Color.blue, c.getDestino().getCodigo(), c.getDestino().getLocal(), 5));
				Tracado tr = new Tracado();
				tr.setWidth(2);
				tr.setCor(Color.ORANGE);
				tr.addPonto(c.getConexao().getLocal());
				tr.addPonto(c.getDestino().getLocal());
				gerenciador.addTracado(tr);
			}

			gerenciador.alterarCentro(comboAero.getValue().getLocal());
			lstPoints.add(new MyWaypoint(Color.RED, comboAero.getValue().getCodigo(), comboAero.getValue().getLocal(), 15));
			gerenciador.setPontos(lstPoints);
			gerenciador.getMapKit().repaint();
		});

	}

	private class EventosMouse extends MouseAdapter {
		private int lastButton = -1;

		@Override
		public void mousePressed(MouseEvent e) {
			JXMapViewer mapa = gerenciador.getMapKit().getMainMap();
			GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
			// System.out.println(loc.getLatitude()+", "+loc.getLongitude());
			lastButton = e.getButton();
			// Botão 3: seleciona localização
			if (lastButton == MouseEvent.BUTTON3) {
				gerenciador.setPosicao(loc);
				gerenciador.getMapKit().repaint();
			}
		}
	}

	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swingNode.setContent(gerenciador.getMapKit());
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
