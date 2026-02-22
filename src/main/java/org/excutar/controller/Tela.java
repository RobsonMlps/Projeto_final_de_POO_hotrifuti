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
    // MODIFICAÇÃO: Declarada a coluna que faltava para a Categoria
    @FXML private TableColumn<Produto, Integer> colCateg;

    // --- FXML IDs - VENDA ---
    @FXML private ComboBox<Cliente> cbVendaCliente;
    @FXML private TextField txtproduto;
    @FXML private TextField txtVendaQuantidade;
    @FXML private TableView<Venda> tblVendas;
    @FXML private TableColumn<Venda, Integer> colVendaId;
    // MODIFICAÇÃO: Declaradas as colunas que faltavam para a Tabela de Vendas
    @FXML private TableColumn<Venda, Integer> colCliente;
    @FXML private TableColumn<Venda, Integer> colproduto;
    @FXML private TableColumn<Venda, Integer> colquantidade;

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
        // Configuração Produto
        colProdId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colProdNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colProdPeso.setCellValueFactory(new PropertyValueFactory<>("pesoKg"));
        // MODIFICAÇÃO: Vincula ao idCategoria do seu Model Produto
        colCateg.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));

        // Configuração Venda
        colVendaId.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        // MODIFICAÇÃO: Vincula aos atributos do seu Model Venda
        colCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colproduto.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colquantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendida"));
    }

    private void carregarDadosBanco() {
        try {
            cbProdCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.listar()));
            cbVendaCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));

            // MODIFICAÇÃO: Agora carrega ambas as tabelas ao iniciar
            tblProdutos.setItems(FXCollections.observableArrayList(produtoDAO.listar()));
            tblVendas.setItems(FXCollections.observableArrayList(vendaDAO.listar()));
        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    public void salvarProduto() {
        try {
            // Validação para evitar erro null
            if (cbProdCategoria.getValue() == null) {
                mostrarErro("Selecione uma categoria antes de salvar!");
                return;
            }

            Produto p = new Produto();
            p.setNome(txtProdNome.getText());
            p.setDescricao(txtProdDescricao.getText());
            p.setPesoKg(new BigDecimal(txtProdPeso.getText().replace(",", ".")));
            p.setIdCategoria(cbProdCategoria.getValue().getIdCategoria());

            produtoDAO.salvar(p);

            // Atualiza a tabela na tela
            tblProdutos.setItems(FXCollections.observableArrayList(produtoDAO.listar()));
            limparCampos();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Sucesso!");
            alert.setContentText("Produto salvo com sucesso!");
            alert.show();

        } catch (Exception e) {
            mostrarErro("Erro ao salvar produto: " + (e.getMessage() != null ? e.getMessage() : "Campos inválidos"));
        }
    }

    @FXML
    public void finalizarVenda() {
        try {
            Cliente cli = cbVendaCliente.getValue();
            if (cli == null) {
                mostrarErro("Selecione um cliente!");
                return;
            }

            Venda v = new Venda();
            v.setIdCliente(cli.getIdCliente());
            v.setIdProduto(Integer.parseInt(txtproduto.getText()));
            v.setQuantidadeVendida(Integer.parseInt(txtVendaQuantidade.getText()));

            // Valores padrão para evitar erro no banco (NOT NULL)
            v.setPrecoUnidade(new BigDecimal("0.00"));
            v.setDataHora(new java.sql.Timestamp(System.currentTimeMillis()));

            vendaDAO.salvar(v);

            // Atualiza a tabela de vendas
            tblVendas.setItems(FXCollections.observableArrayList(vendaDAO.listar()));

            txtproduto.clear();
            txtVendaQuantidade.clear();

        } catch (Exception e) {
            mostrarErro("Erro ao realizar venda: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtProdNome.clear();
        txtProdDescricao.clear();
        txtProdPeso.clear();
        cbProdCategoria.getSelectionModel().clearSelection();
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}