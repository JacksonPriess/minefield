package br.com.priessti.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.priessti.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tabuleiro) {

		// Como organizar o layout da tela
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		tabuleiro.registrarObservador(e -> {
			SwingUtilities.invokeLater(() -> {
				if (e.isGanhou()) {
					JOptionPane.showMessageDialog(null, "Ganhou");					
				} else {
					JOptionPane.showMessageDialog(null, "Perdeu");
				}
				
				tabuleiro.reiniciar();
			});
		});

		/*
		 * 
		 * int total = tabuleiro.getTotal(); for (int i = 0; i < total ; i++ ) {
		 * //Adicionar o campo ao painel add(new BotaoCampo(null)); }
		 */
	}

}
