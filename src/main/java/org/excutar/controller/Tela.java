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
    @FXML private TableColumn<Produto, Integer> colCateg;

    // --- FXML IDs - VENDA ---
    @FXML private ComboBox<Integer> cbEditarVendaId; // NOVO
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
        colVendaPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnidade"));
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
            mostrarErro("Eita, deu bronca ao carregar dados: " + e.getMessage());
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
            mostrarErro("Erro: " + e.getMessage());
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
        cbProdCategoria.setValue(null);
        cbEditarProdutoId.setValue(null);
        txtproduto.clear();
        txtVendaQuantidade.clear();
        cbVendaCliente.setValue(null);
        cbEditarVendaId.setValue(null);
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

    private void mostrarSucesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}