package org.excutar.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.excutar.dao.*;
import org.excutar.model.*;

import java.math.BigDecimal;

public class Tela {

    // --- FXML IDs - PRODUTO ---
    @FXML private TextField txtProdNome, txtProdDescricao, txtProdPeso;
    @FXML private ComboBox<Categoria> cbProdCategoria;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, Integer> colProdId;
    @FXML private TableColumn<Produto, String> colProdNome;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, BigDecimal> colProdPeso;
    @FXML private TableColumn<Produto, Integer> colCateg;

    // --- FXML IDs - VENDA ---
    @FXML private ComboBox<Cliente> cbVendaCliente;
    @FXML private ComboBox<Produto> cbVendaProduto; // Agora é ComboBox!
    @FXML private TextField txtVendaQuantidade;
    @FXML private TextField txtVendaPreco;
    @FXML private TableView<Venda> tblVendas;
    @FXML private TableColumn<Venda, Integer> colVendaId;
    @FXML private TableColumn<Venda, Integer> colCliente;
    @FXML private TableColumn<Venda, Integer> colproduto;
    @FXML private TableColumn<Venda, Integer> colquantidade;
    @FXML private TableColumn<Venda, BigDecimal> colVendaPreco;

    // DAOs instanciados
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private VendaDAO vendaDAO = new VendaDAO();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDadosBanco();
    }

    private void configurarColunas() {
        // Vinculando com os nomes exatos dos atributos lá nas suas classes Model
        colProdId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colProdNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colProdPeso.setCellValueFactory(new PropertyValueFactory<>("pesoKg"));
        colCateg.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));

        colVendaId.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colproduto.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colquantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendida"));
        colVendaPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnidade"));
    }

    private void carregarDadosBanco() {
        try {
            cbProdCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.listar()));
            cbVendaCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));
            
            // Carrega os produtos tanto na Tabela quanto no novo ComboBox de vendas!
            var listaProdutos = FXCollections.observableArrayList(produtoDAO.listar());
            cbVendaProduto.setItems(listaProdutos);
            tblProdutos.setItems(listaProdutos);
            
            tblVendas.setItems(FXCollections.observableArrayList(vendaDAO.listar()));
        } catch (Exception e) {
            mostrarErro("Eita, deu bronca ao carregar dados: " + e.getMessage());
        }
    }

    // ================= MÉTODOS DE PRODUTO ================= //
    
    @FXML
    public void salvarProduto() {
        try {
            if (cbProdCategoria.getValue() == null) {
                mostrarErro("Oxe, selecione uma categoria antes de salvar!");
                return;
            }

            Produto p = new Produto();
            p.setNome(txtProdNome.getText());
            p.setDescricao(txtProdDescricao.getText());
            p.setPesoKg(new BigDecimal(txtProdPeso.getText().replace(",", ".")));
            p.setIdCategoria(cbProdCategoria.getValue().getIdCategoria());

            produtoDAO.salvar(p);

            mostrarSucesso("Produto salvo com sucesso no estoque!");
            limparCamposProduto();
            carregarDadosBanco(); // Recarrega tudo pra atualizar tabela e o combobox de venda

        } catch (Exception e) {
            mostrarErro("Erro ao salvar produto: " + e.getMessage());
        }
    }

    @FXML
    public void deletarProduto() {
        Produto produtoSelecionado = tblProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado == null) {
            mostrarErro("Selecione um produto na tabela primeiro pra mandar pro beleléu!");
            return;
        }

        try {
            produtoDAO.excluir(produtoSelecionado.getIdProduto());
            mostrarSucesso("Produto apagado com sucesso!");
            carregarDadosBanco(); // Atualiza tudo
        } catch (Exception e) {
            mostrarErro("Não deu pra apagar o produto. Será que ele tá vinculado a alguma venda? Erro: " + e.getMessage());
        }
    }

    // ================= MÉTODOS DE VENDA ================= //

    @FXML
    public void finalizarVenda() {
        try {
            Cliente cli = cbVendaCliente.getValue();
            Produto prod = cbVendaProduto.getValue();

            if (cli == null || prod == null) {
                mostrarErro("Selecione o cliente e o produto certinho, cabra!");
                return;
            }

            if (txtVendaQuantidade.getText().isEmpty()) {
                mostrarErro("Tu esqueceu de botar a quantidade!");
                return;
            }

            // TRAVA DO PREÇO: Se tiver vazio, não deixa passar de jeito nenhum!
            if (txtVendaPreco.getText().isEmpty()) {
                mostrarErro("Oxe, vai vender de graça? Digita o preço aí, visse!");
                return;
            }

            Venda v = new Venda();
            v.setIdCliente(cli.getIdCliente());
            v.setIdProduto(prod.getIdProduto());
            v.setQuantidadeVendida(Integer.parseInt(txtVendaQuantidade.getText()));
            
            // Pega o preço digitado, troca vírgula por ponto (caso o usuário digite 4,50)
            BigDecimal precoDigitado = new BigDecimal(txtVendaPreco.getText().replace(",", "."));
            v.setPrecoUnidade(precoDigitado);
            
            v.setDataHora(new java.sql.Timestamp(System.currentTimeMillis()));

            vendaDAO.salvar(v);

            mostrarSucesso("Venda registrada que é uma beleza!");
            limparCamposVenda();
            carregarDadosBanco();

        } catch (NumberFormatException e) {
            mostrarErro("Ei, digita só número na quantidade e no preço!");
        } catch (Exception e) {
            mostrarErro("Erro ao realizar venda: " + e.getMessage());
        }
    }

    @FXML
    public void deletarVenda() {
        Venda vendaSelecionada = tblVendas.getSelectionModel().getSelectedItem();

        if (vendaSelecionada == null) {
            mostrarErro("Selecione uma venda na tabela pra poder cancelar ela!");
            return;
        }

        try {
            vendaDAO.excluir(vendaSelecionada.getIdVenda());
            mostrarSucesso("Venda cancelada e apagada do sistema!");
            carregarDadosBanco();
        } catch (Exception e) {
            mostrarErro("Erro ao apagar venda: " + e.getMessage());
        }
    }

    // ================= MÉTODOS AUXILIARES ================= //

    private void limparCamposProduto() {
        txtProdNome.clear();
        txtProdDescricao.clear();
        txtProdPeso.clear();
        cbProdCategoria.getSelectionModel().clearSelection();
    }

    private void limparCamposVenda() {
        cbVendaCliente.getSelectionModel().clearSelection();
        cbVendaProduto.getSelectionModel().clearSelection();
        txtVendaQuantidade.clear();
        txtVendaPreco.clear();
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Eita!");
        alert.setContentText(msg);
        alert.show();
    }

    private void mostrarSucesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Tudo certo!");
        alert.setContentText(msg);
        alert.show();
    }
}