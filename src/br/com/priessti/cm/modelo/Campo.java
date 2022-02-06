package br.com.priessti.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Campo {

	private final int linha;
	private final int coluna;

	private boolean aberto;
	private boolean minado;
	private boolean marcado;

	// Campos próximos ao campo "clicado"
	private List<Campo> vizinhos = new ArrayList<>();

	// Instancia o observer na classe do campo.
	private List<CampoObservador> observadores = new ArrayList<>(); // Forma antiga
	// private List<BiConsumer<Campo,CampoEvento>> observers2 = new ArrayList<>();
	// // Forma usando lambdas...

	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public void registrarObservers(CampoObservador observador) {
		observadores.add(observador);
	}

	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach(observador -> observador.eventoOcorreu(this, evento));
	}

	boolean adicionarVizinho(Campo vizinho) {

		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;

		// distância entre os campos... sempre considerando o valor absoluto...

		/*
		 * Quando o campo está na CRUZ o valor absoluto será sempre 1 Quando o campo
		 * está na DIAGONAL PROXIMA o valor absoluto será sempre 2
		 */
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;

		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}

	}

	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
		}

		if (marcado)
			notificarObservadores(CampoEvento.MARCAR);
		else
			notificarObservadores(CampoEvento.DESMARCAR);

	}

	public boolean abrir() {

		if (!aberto && !marcado) {
			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}

			setAberto(true);
			
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir() 
				); // Recursividade
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	public boolean isMinado() {
		return minado;
	}

	public boolean isMarcado() {
		return marcado;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;

		if (aberto)
			notificarObservadores(CampoEvento.ABRIR);
	}

	public boolean isAberto() {
		return aberto;
	}

	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	void minar() {
		minado = true;
	}

	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}

	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}

	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;		
		notificarObservadores(CampoEvento.REINICIAR);
	}

}
