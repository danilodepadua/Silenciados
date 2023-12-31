package com.daniel.TerceiraCamada;

import com.daniel.PrimeiraCamada.Entidades.Player;
import com.daniel.PrimeiraCamada.Exceptions.PlayerInexistenteException;
import com.daniel.PrimeiraCamada.Interfaces.IConsumableOutBattle;
import com.daniel.PrimeiraCamada.Interfaces.IEquipable;
import com.daniel.PrimeiraCamada.Itens.Arma;
import com.daniel.PrimeiraCamada.Itens.Armaduras.Calca;
import com.daniel.PrimeiraCamada.Itens.Armaduras.Capacete;
import com.daniel.PrimeiraCamada.Itens.Armaduras.Peitoral;
import com.daniel.PrimeiraCamada.Itens.Item;
import com.daniel.game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerInventario implements Initializable {
    private Button lastClicked;
    private boolean statusVisivel = false; // Variável para controlar se os dados estão visíveis
    private boolean botaoInicial = false;
    @FXML
    private Text DefesaMagicaPlayer;
    @FXML
    private Text DefesaPlayer;

    @FXML
    private Text ForcaPlayer;
    @FXML
    private Text AtqFPlayer;
    @FXML
    private Text AtqMPlayer;

    @FXML
    private GridPane Grid;

    @FXML
    private Text HpPlayer;

    @FXML
    private ImageView ImagemItem;

    @FXML
    private Text InteligenciaPlayer;

    @FXML
    private Text MpPlayer;

    @FXML
    private Text NomeItem;

    @FXML
    private AnchorPane PainelInfos;

    @FXML
    private Text ResistenciaPlayer;

    @FXML
    private ScrollPane Scroll;

    @FXML
    private Text VelocidadePlayer;


    @FXML
    private Button btnDesequipar;

    @FXML
    private Button btnEquipar;

    @FXML
    private Button btnUsar;

    @FXML
    private Button btnVender;

    @FXML
    private Button btnVoltar;

    @FXML
    private Button btnStatus;
    @FXML
    private Text txtDescricao;
    @FXML
    private Text txtQtdItem;
    @FXML
    private GridPane gridEquipaveis;


    public void ItemSelecionado(Item i) throws PlayerInexistenteException {
        ImagemItem.setImage(i.getImage());
        NomeItem.setText("Nome: " + i.getNome());
        txtDescricao.setText("Descrição: " + i.getDescricao());
        txtQtdItem.setText("Qtd: " + i.getQuant());
        btnUsar.setText("Usar");
        PainelInfos.setDisable(false);
        PainelInfos.setOpacity(1);

        configurarVenda(i);

        if (i instanceof IConsumableOutBattle) {
            btnUsar.setDisable(false);
            configurarAcaoConsumivel((IConsumableOutBattle) i);
        } else {
            btnUsar.setDisable(true);
        }
        if (i instanceof IEquipable) {
            IEquipable equipableItem = (IEquipable) i;

            if (podeEquiparItem(equipableItem)) {
                configurarBtnEquipar(equipableItem);
            } else {
                configurarBtnDesequipar(equipableItem);
            }
        } else {
            btnEquipar.setDisable(true);
            btnDesequipar.setDisable(true);
        }
    }
    private void criaBotaoEquipavel(Item i) throws PlayerInexistenteException {
        if (i.getNome()!= null){
            Button itemButton = new Button();
            ImageView image = new ImageView();
            image.setImage(i.getImage()); // Usar diretamente o Item i

            image.setFitWidth(40);
            image.setPreserveRatio(true);
            itemButton.prefWidthProperty().bind(gridEquipaveis.prefWidthProperty());
            itemButton.prefHeightProperty().bind(gridEquipaveis.prefHeightProperty().divide(4));
            itemButton.setStyle("-fx-background-color: #0a234d; -fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;");

            itemButton.setOnMousePressed(event -> {
                itemButton.setStyle("-fx-background-color: #0a234d; -fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;-fx-border-color: #ADD8E6;");
            });

            itemButton.setOnMouseReleased(event -> {
                itemButton.setStyle("-fx-background-color: #0a234d; -fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;-fx-border-color: transparent;");
            });


            // Configurar ação do botão para exibir detalhes do item ou equipá-lo
            itemButton.setOnAction(event -> {
                try {
                    ItemSelecionado(i); // Passar o Item i
                } catch (PlayerInexistenteException e) {
                    throw new RuntimeException(e);
                }
            });
            // Adicionar imagem ao botão
            itemButton.setGraphic(image);
            // Configurar ação do botão para exibir detalhes do item ou equipá-lo
            if (i instanceof Peitoral) {
                atualizarGridEquipavel(itemButton, 1);
            } else if (i instanceof Capacete) {
                atualizarGridEquipavel(itemButton, 0);
            } else if (i instanceof Calca) {
                atualizarGridEquipavel(itemButton, 2);
            } else if (i instanceof Arma) {
                atualizarGridEquipavel(itemButton, 3);

            }
        }
    }
    private void atualizarGridEquipavel(Button novoBotao, int linha) {
        // Remove os botões existentes na linha
        gridEquipaveis.getChildren().removeIf(node -> GridPane.getRowIndex(node) == linha);

        // Adiciona o novo botão
        gridEquipaveis.add(novoBotao, 0, linha);
    }
    private void configurarAcaoConsumivel(IConsumableOutBattle consumable) {
        btnUsar.setOnAction(event -> {
            try {
                consumable.Consumir();
                limparTela();
                AtualizarDados();
            } catch (PlayerInexistenteException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void configurarBtnEquipar(IEquipable equipableItem) {
        btnEquipar.setDisable(false);
        btnEquipar.setOnAction(event -> {
            try {
                equipableItem.equipar();
                AtualizarDados();
            } catch (PlayerInexistenteException e) {
                throw new RuntimeException(e);
            }
            btnEquipar.setDisable(true);
        });

        btnDesequipar.setDisable(true);
    }

    private void configurarBtnDesequipar(IEquipable equipableItem){
        btnEquipar.setDisable(true);
        btnDesequipar.setDisable(false);
        btnDesequipar.setOnAction(event -> {
            try {
                equipableItem.desequipar(); //Analogo ao equipar, chama o metodo implementando
                AtualizarDados();
                limparTela();

            } catch (PlayerInexistenteException e) {
                throw new RuntimeException(e);
            }
            btnDesequipar.setDisable(true);
        });
    }
    private boolean podeEquiparItem(IEquipable equipableItem) throws PlayerInexistenteException {
        return !Player.getPlayer().getPeitoral().equals(equipableItem) &&
                !Player.getPlayer().getCapacete().equals(equipableItem) &&
                !Player.getPlayer().getCalca().equals(equipableItem) &&
                !Player.getPlayer().getArma().equals(equipableItem);
                //Compara se o item atual é igual ao atual inserido no player
    }
    private void configurarVenda(Item i){
        btnVender.setOnAction(event -> {
            try {
                venderItem(i);
                AtualizarDados();
                limparTela();
            } catch (PlayerInexistenteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void AtualizarDados() throws PlayerInexistenteException {
        VelocidadePlayer.setText("Velocidade: " + Player.getPlayer().getVelocity());
        ForcaPlayer.setText("Força: " + Player.getPlayer().getForce());
        HpPlayer.setText("HP: " + Player.getPlayer().getcHP() + "/" + Player.getPlayer().getHP());
        MpPlayer.setText("MP: " + Player.getPlayer().getcMp() + "/" + Player.getPlayer().getMP());
        InteligenciaPlayer.setText("Inteligência: " + Player.getPlayer().getInteligence());
        ResistenciaPlayer.setText("Resistência: " + Player.getPlayer().getResistencia());
        DefesaPlayer.setText("Defesa Física: " + Player.getPlayer().getDefesaF());
        DefesaMagicaPlayer.setText("Defesa Mágica: " + Player.getPlayer().getDefesaM());
        AtqFPlayer.setText("Ataque Físico: " + Player.getPlayer().getAtaqueF());
        AtqMPlayer.setText("Ataque Mágico: " + Player.getPlayer().getAtaqueM());
        Grid.getChildren().clear();
        int j =0;
        for (int i = 0; i < Player.getPlayer().inventario.getItens().length; i++) {
            if (Player.getPlayer().inventario.getItens()[i] != null) {
                Button item = new Button();
                ImageView image = new ImageView();
                image.setImage(Player.getPlayer().inventario.getItens()[i].getImage());

                image.setFitWidth(40);
                image.setFitHeight(40);
                Grid.add(item, j % 10, j / 10);
                item.prefWidthProperty().bind(Grid.prefWidthProperty().divide(Grid.getColumnCount()));
                item.prefHeightProperty().bind(Grid.prefHeightProperty().divide(Grid.getRowCount()));
                // Defina a cor de fundo do botão, bordas arredondadas e tamanho mínimo do botão
                item.setStyle("-fx-background-color: #0a234d; -fx-min-width: 60; -fx-min-height: 60;-fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;");

                image.setPreserveRatio(true);
                item.setGraphic(image);

                item.setOnMouseClicked(event -> {
                    desmarcarUltimoClicado();
                    destacarBotao(item);
                });

                item.setOnMousePressed(event -> {
                    escurecerCor(item);
                });

                item.setOnMouseReleased(event -> {
                    restaurarCor(item);
                    desmarcarUltimoClicado();
                    destacarBotao(item);
                });

                int finalI = i;
                item.setOnAction(event -> {
                    try {
                        ItemSelecionado(Player.getPlayer().inventario.getItens()[finalI]);
                    } catch (PlayerInexistenteException e) {
                        throw new RuntimeException(e);
                    }
                });
                j++;
            }
        }

        criaBotaoEquipavel(Player.getPlayer().getPeitoral());
        criaBotaoEquipavel(Player.getPlayer().getCapacete());
        criaBotaoEquipavel(Player.getPlayer().getCalca());
        criaBotaoEquipavel(Player.getPlayer().getArma());
    }
    private void escurecerCor(Button botao) {
        botao.setStyle("-fx-background-color: #0a234d; -fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;-fx-border-color: #ADD8E6;-fx-min-width: 60; -fx-min-height: 60; -fx-opacity: 0.8");
    }
    private void restaurarCor(Button botao) {
        botao.setStyle("-fx-background-color: #0a234d; -fx-min-width: 60; -fx-min-height: 60;-fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;");
    }
    private void desmarcarUltimoClicado() {
        if (lastClicked != null) {
            lastClicked.setStyle("-fx-background-color: #0a234d; -fx-min-width: 60; -fx-min-height: 60;-fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;");
        }
    }
    private void destacarBotao(Button button) {
        button.setStyle("-fx-background-color: #0a234d; -fx-background-insets: 0; -fx-background-radius: 0;-fx-border-width: 2; -fx-focus-traversable: false;-fx-border-color: #ADD8E6;-fx-min-width: 60; -fx-min-height: 60");
        lastClicked = button;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUsar.setDisable(true);
        btnStatus.setDisable(false);
        Grid.prefWidthProperty().bind(Scroll.widthProperty().subtract(20));
        Grid.prefHeightProperty().bind(Grid.prefWidthProperty());
        RowConstraints row = new RowConstraints();
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100f/Grid.getColumnCount());
        row.setPrefHeight(Grid.getColumnConstraints().get(0).getPrefWidth());
        for(int i = 0; i< Grid.getColumnCount(); i++){
            Grid.getRowConstraints().set(i,row);
            Grid.getColumnConstraints().set(i,col);
        }
        Grid.setLayoutX(0);
        Grid.setLayoutY(0);
        try {
            AtualizarDados();
        } catch (PlayerInexistenteException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onClickVoltar(ActionEvent event) {
        Main.ChangeScene(new FXMLLoader(Main.class.getResource("TelaCidade.fxml")));
    }
    public void venderItem(Item item) throws PlayerInexistenteException {
        if (item instanceof IEquipable) {
            IEquipable equipableItem = (IEquipable) item;
            desequiparItemSeEquipado(equipableItem);
        }

        int precoItem = item.getPreco();
        Player.getPlayer().ganhaCoins(precoItem * 70 / 100);
        Player.getPlayer().getInventario().RemoverItem(item);
    }

    private void desequiparItemSeEquipado(IEquipable equipableItem) throws PlayerInexistenteException {
        if (Player.getPlayer().getArma().equals(equipableItem)) {
            Player.getPlayer().desequiparArma();
        } else if (Player.getPlayer().getPeitoral().equals(equipableItem)) {
            Player.getPlayer().desequiparPeitoral();
        } else if (Player.getPlayer().getCapacete().equals(equipableItem)) {
            Player.getPlayer().desequiparCapacete();
        } else if (Player.getPlayer().getCalca().equals(equipableItem)) {
            Player.getPlayer().desequiparCalca();
        }
    }
    public void limparTela() {
        NomeItem.setText("");
        txtDescricao.setText("");
        txtQtdItem.setText("");
        ImagemItem.setImage(null);
    }
    @FXML
    void onClickStatus(ActionEvent event) throws PlayerInexistenteException {
        if (statusVisivel){
            limparDadosStatus(); // Chama o método para limpar os dados
            statusVisivel = false;
        }else {
            AtualizarDados();
            statusVisivel = true;
        }
    }
    // Método para limpar os dados
    private void limparDadosStatus() {
        VelocidadePlayer.setText("");
        ForcaPlayer.setText("");
        HpPlayer.setText("");
        MpPlayer.setText("");
        InteligenciaPlayer.setText("");
        ResistenciaPlayer.setText("");
        DefesaPlayer.setText("");
        DefesaMagicaPlayer.setText("");
    }
}