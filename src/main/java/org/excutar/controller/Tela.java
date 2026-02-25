package org.excutar.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.excutar.dao.*;
import org.excutar.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Tela {

    // --- FXML IDs - PRODUTO ---
    @FXML private ComboBox<Integer> cbEditarProdutoId; // NOVO
    @FXML private TextField txtProdNome, txtProdDescricao, txtProdPeso;
    @FXML private ComboBox<Categoria> cbProdCategoria;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, Integer> colProdId;
    @FXML private TableColumn<Produto, String> colProdNome;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, BigDecimal> colProdPeso;
    @FXML private TableColumn<Produto, String> colCateg;

    // --- FXML IDs - VENDA ---
    @FXML private ComboBox<Integer> cbEditarVendaId; // NOVO
    @FXML private ComboBox<Cliente> cbVendaCliente;
    @FXML private TextField txtproduto;
    @FXML private TextField txtVendaQuantidade;
    @FXML private TableView<Venda> tblVendas;
    @FXML private TableColumn<Venda, Integer> colVendaId;
    @FXML private TableColumn<Venda, String> colCliente;
    @FXML private TableColumn<Venda, String> colproduto;
    @FXML private TableColumn<Venda, Integer> colquantidade;

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
        colProdId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colProdNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colProdPeso.setCellValueFactory(new PropertyValueFactory<>("pesoKg"));
        colCateg.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));

        colVendaId.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colproduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colquantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendida"));
    }

    private void carregarDadosBanco() {
        try {
            cbProdCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.listar()));
            cbVendaCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));

            List<Produto> produtos = produtoDAO.listar();
            List<Venda> vendas = vendaDAO.listar();

            tblProdutos.setItems(FXCollections.observableArrayList(produtos));
            tblVendas.setItems(FXCollections.observableArrayList(vendas));

            // NOVO: Atualiza a lista de IDs para os ComboBoxes de edição
            List<Integer> idsProd = new ArrayList<>();
            for(Produto p : produtos) idsProd.add(p.getIdProduto());
            cbEditarProdutoId.setItems(FXCollections.observableArrayList(idsProd));

            List<Integer> idsVenda = new ArrayList<>();
            for(Venda v : vendas) idsVenda.add(v.getIdVenda());
            cbEditarVendaId.setItems(FXCollections.observableArrayList(idsVenda));

        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    // --- FUNÇÕES DE PREENCHIMENTO ---

    @FXML
    public void preencherCamposProduto() {
        Integer idSel = cbEditarProdutoId.getValue();
        if (idSel != null) {
            for (Produto p : tblProdutos.getItems()) {
                if (p.getIdProduto() == idSel) {
                    txtProdNome.setText(p.getNome());
                    txtProdDescricao.setText(p.getDescricao());
                    txtProdPeso.setText(p.getPesoKg().toString());
                    for (Categoria c : cbProdCategoria.getItems()) {
                        if (c.getIdCategoria() == p.getIdCategoria()) {
                            cbProdCategoria.setValue(c);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    @FXML
    public void preencherCamposVenda() {
        Integer idSel = cbEditarVendaId.getValue();
        if (idSel != null) {
            for (Venda v : tblVendas.getItems()) {
                if (v.getIdVenda() == idSel) {
                    txtproduto.setText(String.valueOf(v.getIdProduto()));
                    txtVendaQuantidade.setText(String.valueOf(v.getQuantidadeVendida()));
                    for (Cliente c : cbVendaCliente.getItems()) {
                        if (c.getIdCliente() == v.getIdCliente()) {
                            cbVendaCliente.setValue(c);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    @FXML
    public void salvarProduto() {
        try {
            if (cbProdCategoria.getValue() == null) {
                mostrarErro("Selecione uma categoria!");
                return;
            }

            Produto p = new Produto();
            p.setNome(txtProdNome.getText());
            p.setDescricao(txtProdDescricao.getText());
            p.setPesoKg(new BigDecimal(txtProdPeso.getText().replace(",", ".")));
            p.setIdCategoria(cbProdCategoria.getValue().getIdCategoria());

            Integer idEdit = cbEditarProdutoId.getValue();
            if (idEdit == null) {
                produtoDAO.salvar(p);
                mostrarSucesso("Produto salvo com sucesso!");
            } else {
                p.setIdProduto(idEdit);
                produtoDAO.atualizar(p); // PRECISA TER O MÉTODO ATUALIZAR NO DAO
                mostrarSucesso("Produto ID " + idEdit + " atualizado!");
            }

            carregarDadosBanco();
            limparCampos();
        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
        }
    }

    @FXML
    public void finalizarVenda() {
        try {
            if (cbVendaCliente.getValue() == null) {
                mostrarErro("Selecione um cliente!");
                return;
            }

            Venda v = new Venda();
            v.setIdCliente(cbVendaCliente.getValue().getIdCliente());
            v.setIdProduto(Integer.parseInt(txtproduto.getText()));
            v.setQuantidadeVendida(Integer.parseInt(txtVendaQuantidade.getText()));
            v.setPrecoUnidade(BigDecimal.ZERO);
            v.setDataHora(new java.sql.Timestamp(System.currentTimeMillis()));

            Integer idEdit = cbEditarVendaId.getValue();
            if (idEdit == null) {
                vendaDAO.salvar(v);
                mostrarSucesso("Venda cadastrada!");
            } else {
                v.setIdVenda(idEdit);
                vendaDAO.atualizar(v); // PRECISA TER O MÉTODO ATUALIZAR NO DAO
                mostrarSucesso("Venda ID " + idEdit + " atualizada!");
            }

            carregarDadosBanco();
            limparCampos();
        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtProdNome.clear();
        txtProdDescricao.clear();
        txtProdPeso.clear();
        cbProdCategoria.setValue(null);
        cbEditarProdutoId.setValue(null);
        txtproduto.clear();
        txtVendaQuantidade.clear();
        cbVendaCliente.setValue(null);
        cbEditarVendaId.setValue(null);
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }

    private void mostrarSucesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}