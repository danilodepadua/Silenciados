package com.daniel.TerceiraCamada;

import com.daniel.PrimeiraCamada.Entidades.Player;
import com.daniel.PrimeiraCamada.Exceptions.PlayerInexistenteException;
import com.daniel.game.Main;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCassino implements Initializable {
    private String texto = "Cassinão";

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnBlackJack;

    @FXML
    private Button btnVoltar;

    @FXML
    private Text txtCassino;

    @FXML
    private Text txtMoedas;

    @FXML
    private VBox vboxTextos;

    @FXML
    void Jogar(ActionEvent event) {
        Main.ChangeScene(new FXMLLoader(Main.class.getResource("BlackJack.fxml")));
    }

    @FXML
    void Voltar(ActionEvent event) {
        Main.ChangeScene(new FXMLLoader(Main.class.getResource("TelaCidade.fxml")));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            txtMoedas.setText("" + Player.getPlayer().getCoins() + " Moedas");
            Point2D centro = new Point2D(240, 169);
            desenharTexto(texto, 40, centro);
        } catch (PlayerInexistenteException e) {
            throw new RuntimeException(e);
        }

        anchorPane.setBackground(new Background(new BackgroundImage(new Image(Main.class.getResource("/com.daniel.Images/Cartas/Mesa.png").toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true
                ))));
        vboxTextos.setSpacing(15);

    }

    public void desenharTexto(String palavra, double rotacaoInicial, Point2D centro) {
        double anguloCurva = 105; // 60 a 150 parece melhor
        char[] arrayPalavra = palavra.toCharArray();

        double raio = 100;

        boolean acimaDoCentro = rotacaoInicial < 180;

        final ObservableList<Text> partes = FXCollections.observableArrayList();
        final ObservableList<PathTransition> transicoes = FXCollections.observableArrayList();

        for (int i = 0; i < palavra.length(); i++) {
            double anguloFinal = (i + 1) * anguloCurva / palavra.length();

            Shape curva = criarCurva(centro, raio, anguloCurva, anguloFinal, rotacaoInicial, acimaDoCentro);

            Text parte = new Text(String.valueOf(arrayPalavra[i]));

            // Defina a fonte e a cor aqui
            parte.setFont(Font.font("Barlow Condensed SemiBold", FontWeight.SEMI_BOLD, 90));
            parte.setFill(Paint.valueOf("yellow")); // Amarelo

            partes.add(parte);

            transicoes.add(criarTransicaoCaminho(curva, parte));
        }

        for (int i = 0; i < partes.size(); i++) {
            Text parte = partes.get(i);
            parte.setLayoutX(centro.getX() - raio * Math.cos(Math.toRadians(rotacaoInicial + i * anguloCurva / (palavra.length() - 1))));
            parte.setLayoutY(centro.getY() - raio * Math.sin(Math.toRadians(rotacaoInicial + i * anguloCurva / (palavra.length() - 1))));
            parte.setVisible(true);

            final Transition transicao = transicoes.get(i);
            transicao.stop();
            transicao.play();
        }

        anchorPane.getChildren().addAll(partes);
    }

    private PathTransition criarTransicaoCaminho(Shape curva, Text texto) {
        final PathTransition transicao = new PathTransition(Duration.millis(1), curva, texto);

        transicao.setAutoReverse(false);
        transicao.setCycleCount(1);
        transicao.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        transicao.setInterpolator(Interpolator.LINEAR);

        return transicao;
    }

    private Shape criarCurva(Point2D centro, double raio, double anguloTotal, double angulo, double rotacaoInicial, boolean inverter) {
        Arc arco = new Arc();
        arco.setCenterX(centro.getX());
        arco.setCenterY(centro.getY());
        if (inverter) {
            final double anguloFinal = rotacaoInicial + anguloTotal;
            arco.setStartAngle(anguloFinal);
            arco.setLength(-angulo);
        } else {
            arco.setStartAngle(rotacaoInicial);
            arco.setLength(angulo);
        }
        arco.setRadiusX(raio);
        arco.setRadiusY(raio);
        return arco;
    }
}
