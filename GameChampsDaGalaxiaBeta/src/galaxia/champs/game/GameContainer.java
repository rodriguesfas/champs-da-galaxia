/**
 *  Este programa é software livre; você pode redistribuí-lo e/ou
 *  modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 *  publicada pela Free Software Foundation; tanto a versão 2 da
 *  Licença como (a seu critério) qualquer versão mais nova.
 *
 *  Este programa é distribuído na expectativa de ser útil, mas SEM
 *  QUALQUER GARANTIA; sem mesmo a garantia implícita de
 *  COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
 *  PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
 *  detalhes.
 *
 *  Você deve ter recebido uma cópia da Licença Pública Geral GNU
 *  junto com este programa; se não, escreva para a Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307, USA.
 * 
 *  @author Francisco de Assis de Souza Rodrigues (2013)
 *  
 */

package galaxia.champs.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import galaxia.champs.addItens.AddVida;

import galaxia.champs.efeitos.sonoros.MusicaCenario;
import galaxia.champs.efeitos.sonoros.SomAddVida;
import galaxia.champs.efeitos.sonoros.SomColisao;
import galaxia.champs.efeitos.sonoros.SomDesparo;
import galaxia.champs.efeitos.sonoros.SomExplosao;
import galaxia.champs.efeitos.sonoros.SomFimJogo;
import galaxia.champs.efeitos.sonoros.SomMissaoCompleta;
import galaxia.champs.efeitos.sonoros.SomNaoAbatidos;
import galaxia.champs.efeitos.sonoros.SomZeroVida;

import galaxia.champs.tempoFase.Temporalizador;

public class GameContainer extends JFrame {

	private static final long serialVersionUID = 1L;

	private Timer timer;
	private Timer novosIni;
	private Timer novaVida;
	private Fase fase;
	private Image fundo;
	private Nave nave;

	private List<Tiro1> tiros;
	private List<Inimigo> inimigos;
	private List<AddVida> addVida;
	private List<Timer> tempos;
	private List<Explosao> explosoes;

	private int naoAbatidos = 0;
	protected int contMissao = 1;
	private int contPontos = 0;
	private int contVida = 3;

	private boolean fimDeJogo = false;
	private boolean andamentoJogo;
	private boolean gerouVida = false;
	private boolean pause = false;
	private boolean zero = false;

	// OBJETO CLASS TEMPORALIZADOR
	Temporalizador t;

	// OBJETO CLASS MUSICA CENARIO
	MusicaCenario mc = new MusicaCenario();

	// OBJETO GHAPHICS 2D
	private Graphics2D grafico;

	// MOUSE MIRA
	int xMira;
	int yMira;

	// COMPONENTE MENU INFO
	private JMenuBar menuBar;

	private JMenu mnMissao;
	private JMenu mnVida;
	private JMenu mnPontuacao;
	private JMenu mnNaoAbatido;
	private JMenu mnTempo;

	/**
	 * ===========================================================MAIN
	 */
	public static void main(String args[]) {
		new GameContainer();
	}

	/**
	 * ===============================================================
	 * ============================================CONSTRUTOR DA CLASS
	 * ===============================================================
	 */
	protected GameContainer() {

		// TITULO DO JOGO
		setTitle("Champs da Galáxia");

		// SAIR JOGO
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// DIMENÇÃO DA JANELA
		setSize(600, 600);

		setLocationRelativeTo(null);

		// NÃO MAXIMIZA JANELA
		setResizable(false);

		// EXPANDE A TELA (FULL SCREEN)
		// setExtendedState(JFrame.MAXIMIZED_BOTH);

		// JANELA VISIVEL
		setVisible(true);

		// INICIA TEMPORALIZADOR
		t = new Temporalizador();

		// START MENU
		iniciarMenuInfo();

		// START FASE
		fase = new Fase();
		add(fase);

		// DEFINIR MISSÃO
		definirMissao();

		// LISTA TIRO1
		tiros = new ArrayList<Tiro1>();

		// LISTA INIMIGOS
		inimigos = new ArrayList<Inimigo>();

		// INICIALIZA INIMIGOS
		inicializaInimigos();

		// LISTA TEMPO (EXPLOSÃO)
		tempos = new ArrayList<Timer>();

		// LISTA EXPLOSÕES
		explosoes = new ArrayList<Explosao>();

		// LISTA DE VIDAS
		addVida = new ArrayList<AddVida>();

		// TECLADO
		addKeyListener(new keyAdapter());

		// MOUSE
		addMouseListener(new Mouse());
		addMouseMotionListener(new Mouse());

		// CHAMA CLASS NAVE
		nave = new Nave();

		// CONTROLA O TEMPO PARA CRIAR INIMIGIOS
		novosIni = new Timer(900, new novoInimigo());
		novosIni.start();

		// CONTROLA O TEMPO PARA CRIAR ADD VIDA
		novaVida = new Timer(900, new novaVida());
		novaVida.start();

		// CONTROLA O TEMPO (VELOCIDADE) JOGO
		timer = new Timer(5, new Listener());
		timer.start();

		// STATUS DO JOGO
		andamentoJogo = true;

		// TOCAR MUSICA DO CENARIO
		mc = new MusicaCenario();
		mc.main(null);
		mc.setloop(true);
	}

	/*
	 * ===============================METODO INFORMAÇÃO GAME
	 */
	public void iniciarMenuInfo() {

		// MENU BAR
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// MISSÃO
		mnMissao = new JMenu("Missão: " + contMissao);
		mnMissao.setEnabled(true);
		mnMissao.setHorizontalTextPosition(SwingConstants.CENTER);
		mnMissao.setHorizontalAlignment(SwingConstants.CENTER);
		mnMissao.setVerticalAlignment(SwingConstants.BOTTOM);
		mnMissao.setVerticalTextPosition(SwingConstants.BOTTOM);
		mnMissao.setIcon(new ImageIcon(""));
		mnMissao.setFont(new Font("Century Schoolbook L", Font.PLAIN, 15));
		menuBar.add(mnMissao);

		// VIDA
		mnVida = new JMenu(" x " + contVida);
		mnVida.setEnabled(true);
		// mnVida.setHorizontalTextPosition(SwingConstants.CENTER);
		// mnVida.setHorizontalAlignment(SwingConstants.CENTER);
		// mnVida.setVerticalAlignment(SwingConstants.BOTTOM);
		// mnVida.setVerticalTextPosition(SwingConstants.BOTTOM);
		mnVida.setIcon(new ImageIcon("res/inf/vida.png"));
		mnVida.setFont(new Font("Century Schoolbook L", Font.PLAIN, 15));
		menuBar.add(mnVida);

		// PONTUAÇÃO
		mnPontuacao = new JMenu("x" + contPontos);
		mnPontuacao.setEnabled(true);
		// mnPontuacao.setHorizontalTextPosition(SwingConstants.CENTER);
		// mnPontuacao.setHorizontalAlignment(SwingConstants.CENTER);
		// mnPontuacao.setVerticalAlignment(SwingConstants.BOTTOM);
		// mnPontuacao.setVerticalTextPosition(SwingConstants.BOTTOM);
		mnPontuacao.setIcon(new ImageIcon("res/inf/pontuacao.gif"));
		mnPontuacao.setFont(new Font("Century Schoolbook L", Font.PLAIN, 15));
		menuBar.add(mnPontuacao);

		// NÃO ABATIDOS
		mnNaoAbatido = new JMenu("Não Abatidos: " + naoAbatidos);
		mnNaoAbatido.setEnabled(true);
		mnNaoAbatido.setHorizontalTextPosition(SwingConstants.CENTER);
		mnNaoAbatido.setHorizontalAlignment(SwingConstants.CENTER);
		mnNaoAbatido.setVerticalAlignment(SwingConstants.BOTTOM);
		mnNaoAbatido.setVerticalTextPosition(SwingConstants.BOTTOM);
		mnNaoAbatido.setIcon(new ImageIcon(""));
		mnNaoAbatido.setFont(new Font("Century Schoolbook L", Font.PLAIN, 15));
		menuBar.add(mnNaoAbatido);

		// TEMPORALIZADOR (TEMPO DO JOGO)
		mnTempo = new JMenu("Tempo: " + t.minutos + ":" + t.segundos);
		mnTempo.setEnabled(true);
		mnTempo.setHorizontalTextPosition(SwingConstants.CENTER);
		mnTempo.setHorizontalAlignment(SwingConstants.CENTER);
		mnTempo.setVerticalAlignment(SwingConstants.BOTTOM);
		mnTempo.setVerticalTextPosition(SwingConstants.BOTTOM);
		mnTempo.setIcon(new ImageIcon(""));
		mnTempo.setFont(new Font("Century Schoolbook L", Font.PLAIN, 15));
		menuBar.add(mnTempo);
	}

	/*
	 * =====================================METODO INICIALIZA INIMIGOS
	 */
	private void inicializaInimigos() {
		for (int i = 0; i < inimigos.size(); i++) {
			inimigos.remove(i);
			i--;
		}
	}

	/*
	 * ===================================METODO INIMIGOS NÃO ABATIDOS
	 */
	public void contNaoAbatidos() {
		naoAbatidos++;

		// TOCAR SOM (NÃO ABATIDO)
		SomNaoAbatidos sna = new SomNaoAbatidos();
		sna.main(null);
	}

	/*
	 * =======================================METODO CONTATO (COLISÃO)
	 */
	private void contato() {
		int sx1, sx2, sy1, sy2;
		int dx1, dx2, dy1, dy2;

		sx1 = nave.getX();
		sx2 = nave.getX() + nave.getLar();
		sy1 = nave.getY();
		sy2 = nave.getY() + nave.getAlt();

		Inimigo iniTemp;

		for (int i = 0; i < inimigos.size(); i++) {
			iniTemp = inimigos.get(i);

			dx1 = (int) iniTemp.getX();
			dx2 = (int) iniTemp.getX() + iniTemp.getLar();
			dy1 = (int) iniTemp.getY();
			dy2 = (int) iniTemp.getY() + iniTemp.getAlt();

			if (sx1 < dx2 && sx2 > dx1 && sy1 < dy2 && sy2 > dy1) {

				// VERIFICA QUANT VIDA
				if (contVida <= 0) {
					nave.setVisivel(false);

				} else {

					// COLISÃO NAVE COM INIMIGO
					for (int n = 0; n < inimigos.size(); n++) {
						iniTemp = inimigos.get(n);

						dx1 = (int) iniTemp.getX();
						dx2 = (int) iniTemp.getX() + iniTemp.getLar();
						dy1 = (int) iniTemp.getY();
						dy2 = (int) iniTemp.getY() + iniTemp.getAlt();

						if (sx1 < dx2 && sx2 > dx1 && sy1 < dy2 && sy2 > dy1) {
							inimigos.remove(i);// REMOVE INIMIGO

							// SOM COLISÃO
							SomColisao sc = new SomColisao();
							sc.main(null);

							// EXIBE IMAGEM DA EXPLOSÃO
							explosoes.add(new Explosao(dx1 - 13, dy1 + 10));
							tempos.add(new Timer(50, explosoes.get(explosoes
									.size() - 1)));

							Timer tempoTemp = tempos.get(tempos.size() - 1);
							tempoTemp.start();
							reduzPontos();// REDUZ PONTO
						}
					}
					reduzVidas();// REDUZ VIDA
				}
			}
		}

		// COLISÃO DISPARO COM INIMIGO
		for (int i = 0; i < inimigos.size(); i++) {
			iniTemp = inimigos.get(i);

			dx1 = (int) iniTemp.getX();
			dx2 = (int) iniTemp.getX() + iniTemp.getLar();
			dy1 = (int) iniTemp.getY();
			dy2 = (int) iniTemp.getY() + iniTemp.getAlt();

			Tiro1 tiroTemp;
			for (int j = 0; j < tiros.size(); j++) {
				tiroTemp = tiros.get(j);

				sx1 = tiroTemp.getX();
				sx2 = tiroTemp.getX() + tiroTemp.getLar();
				sy1 = tiroTemp.getY();
				sy2 = tiroTemp.getY() + tiroTemp.getAlt();

				if (sx1 < dx2 && sx2 > dx1 && sy1 < dy2 && sy2 > dy1) {
					tiros.remove(j);// REMOVE TIRO
					inimigos.remove(i);// REMOVE INIMIGO

					// TOCA SOM EXPLOSÃO
					SomExplosao se = new SomExplosao();
					se.main(null);

					// EXIBE IMAGEM DA EXPLOSÃO
					explosoes.add(new Explosao(dx1 - 13, dy1 + 10));
					tempos.add(new Timer(50,
							explosoes.get(explosoes.size() - 1)));

					Timer tempoTemp = tempos.get(tempos.size() - 1);
					tempoTemp.start();

					contarPontos();// INCREMENTA PONTOS
				}
			}
		}
	}

	/*
	 * =======================================METODO CONTATO ADD VIDA
	 */
	public void contatoAddVida() {
		int sx1, sx2, sy1, sy2;
		int dx1, dx2, dy1, dy2;

		sx1 = nave.getX();
		sx2 = nave.getX() + nave.getLar();
		sy1 = nave.getY();
		sy2 = nave.getY() + nave.getAlt();

		AddVida addVidaTemp;

		// COLISÃO NAVE COM ADD VIDA
		for (int i = 0; i < addVida.size(); i++) {
			addVidaTemp = addVida.get(i);

			dx1 = (int) addVidaTemp.getX();
			dx2 = (int) addVidaTemp.getX() + addVidaTemp.getLar();
			dy1 = (int) addVidaTemp.getY();
			dy2 = (int) addVidaTemp.getY() + addVidaTemp.getAlt();

			if (sx1 < dx2 && sx2 > dx1 && sy1 < dy2 && sy2 > dy1) {

				// VERIFICA QUANT VIDA
				if (contVida <= 0) {
					nave.setVisivel(false);
				} else {

					for (int n = 0; n < addVida.size(); n++) {
						addVidaTemp = addVida.get(n);

						dx1 = (int) addVidaTemp.getX();
						dx2 = (int) addVidaTemp.getX() + addVidaTemp.getLar();
						dy1 = (int) addVidaTemp.getY();
						dy2 = (int) addVidaTemp.getY() + addVidaTemp.getAlt();

						if (sx1 < dx2 && sx2 > dx1 && sy1 < dy2 && sy2 > dy1) {
							addVida.remove(i);// REMOVE IMGEM VIDA

							// TOCAR SOM COLISÃO
							SomAddVida sc = new SomAddVida();
							sc.main(null);

							contarVidas();// INCREMENTA VIDA
						}
					}
				}
			}
		}
	}

	/**
	 * ===============================================================
	 * ===================================CLASS INTERNA (NOVO INIMIGO)
	 * ===============================================================
	 */
	private class novoInimigo implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// DEFINE A POSIÇÃO ONDE SERÁ CRIADO OS INIMIGOS
			inimigos.add(new Inimigo(1 + (int) (550 * Math.random()), -80));
		}
	}

	/**
	 * ===============================================================
	 * ==================================CLASS INTERNA (ADD NOVA VIDA)
	 * ===============================================================
	 */
	private class novaVida implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// A CADA 4 MISSÃO, GERA UMA VIDA
			if ((gerouVida == false) && (t.minutos == 0)
					&& ((contMissao % 4) == 0)) {
				// DEFINE A POSIÇÃO ONDE SERÁ CRIADA A VIDA
				addVida.add(new AddVida(1 + (int) (550 * Math.random()), -80));
				gerouVida = true;
			}
		}
	}

	/**
	 * ===============================================================
	 * =========================================CLASS INTERNA LISTENER
	 * ===============================================================
	 */
	private class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// (GANHOU)
			if ((t.minutos == 0) && (t.segundos == 0)) {

				// STATUS DO JOGO
				andamentoJogo = false;

				// SOM INFOMAÇÃO
				SomMissaoCompleta smc = new SomMissaoCompleta();
				smc.main(null);

				// INFORMA USUARIO (IMAGEM MISSÃO COMPLETA)
				JOptionPane.showMessageDialog(null, "MISSÃO COMPLETA!"
						+ "\nAPÓS (OK) ENTER PARA CONTINUAR.");

				timer.stop();// PARA TEMPO
			}

			// MOVER INIMIGOS
			Inimigo iniTemp;
			for (int i = 0; i < inimigos.size(); i++) {
				iniTemp = inimigos.get(i);

				if (iniTemp.getY() < fase.getAlt()) {
					iniTemp.mover();
				} else {
					iniTemp.setY(-iniTemp.getAlt());
					contNaoAbatidos();
				}
			}

			// MOVER DISPARO
			Tiro1 tiroTemp;
			for (int i = 0; i < tiros.size(); i++) {
				tiroTemp = tiros.get(i);

				if (tiroTemp.getY() > -10) {
					tiroTemp.mover();
				} else {
					tiros.remove(i);
				}
			}

			// MOVER ADD VIDA
			AddVida addVidaTemp;
			for (int i = 0; i < addVida.size(); i++) {
				addVidaTemp = addVida.get(i);

				if (addVidaTemp.getY() < fase.getAlt()) {
					addVidaTemp.mover();
				} else {
					addVidaTemp.setY(-addVidaTemp.getAlt());
				}
			}

			// ==========================================================
			// ATUALIZAR MENU INFO
			mnMissao.setText("Missão: " + contMissao);
			mnVida.setText("x" + contVida);
			mnPontuacao.setText("x" + contPontos);
			mnNaoAbatido.setText("Não Abatidos: " + naoAbatidos);
			mnTempo.setText("Tempo: " + t.minutos + ":" + t.segundos);

			// ============================================================

			// METODO MOVER NAVE
			nave.mover();

			// METODO CONTATO (COLISÃO)
			contato();

			// METODO CONTATO (ADD VIDA)
			contatoAddVida();

			// METODO PINTURA
			fase.repaint();
		}
	}

	/*
	 * ==========================================METODO DEFINIR MISSÃO
	 */
	public void definirMissao() {
		switch (contMissao) {
		case 1:
			ImageIcon referencia = new ImageIcon(
					"res/fundoFase/cenario1.jpg");
			fundo = referencia.getImage();
			break;
		case 2:
			ImageIcon referencia2 = new ImageIcon(
					"res/fundoFase/cenario2.jpg");
			fundo = referencia2.getImage();
			break;
		case 3:
			ImageIcon referencia3 = new ImageIcon(
					"res/fundoFase/cenario3.png");
			fundo = referencia3.getImage();
			break;
		case 4:
			ImageIcon referencia4 = new ImageIcon(
					"res/fundoFase/cenario4.jpg");
			fundo = referencia4.getImage();
			break;
		case 5:
			ImageIcon referencia5 = new ImageIcon(
					"res/fundoFase/cenario5.jpg");
			fundo = referencia5.getImage();
			break;
		case 6:
			ImageIcon referencia6 = new ImageIcon(
					"res/fundoFase/cenario6.jpg");
			fundo = referencia6.getImage();
			break;
		case 7:
			ImageIcon referencia7 = new ImageIcon(
					"res/fundoFase/cenario7.jpg");
			fundo = referencia7.getImage();
			break;
		case 8:
			ImageIcon referencia8 = new ImageIcon(
					"res/fundoFase/cenario8.jpg");
			fundo = referencia8.getImage();
			break;
		case 9:
			ImageIcon referencia9 = new ImageIcon(
					"res/fundoFase/cenario9.jpg");
			fundo = referencia9.getImage();
			break;
		case 10:
			ImageIcon referencia10 = new ImageIcon(
					"res/fundoFase/cenario10.jpg");
			fundo = referencia10.getImage();
			break;
		case 11:
			ImageIcon referencia11 = new ImageIcon(
					"res/fundoFase/cenario11.jpg");
			fundo = referencia11.getImage();
			break;
		case 12:
			ImageIcon referencia12 = new ImageIcon(
					"res/fundoFase/cenario12.jpg");
			fundo = referencia12.getImage();
			break;
		case 13:
			ImageIcon referencia13 = new ImageIcon(
					"res/fundoFase/cenario13.jpg");
			fundo = referencia13.getImage();
			break;
		case 14:
			ImageIcon referencia14 = new ImageIcon(
					"res/fundoFase/cenario14.jpg");
			fundo = referencia14.getImage();
			break;
		case 15:
			ImageIcon referencia15 = new ImageIcon(
					"res/fundoFase/cenario15.jpg");
			fundo = referencia15.getImage();
			break;
		case 16:
			ImageIcon referencia16 = new ImageIcon(
					"res/fundoFase/cenario16.png");
			fundo = referencia16.getImage();
			break;
		case 17:
			ImageIcon referencia17 = new ImageIcon(
					"res/fundoFase/cenario17.jpg");
			fundo = referencia17.getImage();
			break;
		case 18:
			ImageIcon referencia18 = new ImageIcon(
					"res/fundoFase/cenario18.jpg");
			fundo = referencia18.getImage();
			break;
		case 19:
			ImageIcon referencia19 = new ImageIcon(
					"res/fundoFase/cenario19.jpg");
			fundo = referencia19.getImage();
			break;
		case 20:
			ImageIcon referencia20 = new ImageIcon(
					"res/fundoFase/cenario20.jpg");
			fundo = referencia20.getImage();
			break;
		case 21:
			ImageIcon referencia21 = new ImageIcon(
					"res/fundoFase/cenario21.jpg");
			fundo = referencia21.getImage();
			break;
		case 22:
			ImageIcon referencia22 = new ImageIcon(
					"res/fundoFase/cenario22.jpg");
			fundo = referencia22.getImage();
			break;
		case 23:
			ImageIcon referencia23 = new ImageIcon(
					"res/fundoFase/cenario23.jpg");
			fundo = referencia23.getImage();
			break;
		case 24:
			ImageIcon referencia24 = new ImageIcon(
					"res/fundoFase/cenario24.jpg");
			fundo = referencia24.getImage();
			break;
		case 25:
			ImageIcon referencia25 = new ImageIcon(
					"res/fundoFase/cenario25.jpg");
			fundo = referencia25.getImage();
			break;
		case 26:
			ImageIcon referencia26 = new ImageIcon(
					"res/fundoFase/cenario26.jpg");
			fundo = referencia26.getImage();
			break;
		case 27:
			ImageIcon referencia27 = new ImageIcon(
					"res/fundoFase/cenario27.jpg");
			fundo = referencia27.getImage();
			break;
		case 28:
			ImageIcon referencia28 = new ImageIcon(
					"res/fundoFase/cenario28.jpg");
			fundo = referencia28.getImage();
			break;
		case 29:
			ImageIcon referencia29 = new ImageIcon(
					"res/fundoFase/cenario29.jpg");
			fundo = referencia29.getImage();
			break;
		case 30:
			ImageIcon referencia30 = new ImageIcon(
					"res/fundoFase/cenario30.jpg");
			fundo = referencia30.getImage();
			break;
		case 31:
			ImageIcon referencia31 = new ImageIcon(
					"res/fundoFase/cenario31.jpeg");
			fundo = referencia31.getImage();
			break;
		case 32:
			ImageIcon referencia32 = new ImageIcon(
					"res/fundoFase/cenario32.jpg");
			fundo = referencia32.getImage();
			break;
		case 33:
			ImageIcon referencia33 = new ImageIcon(
					"res/fundoFase/cenario33.jpg");
			fundo = referencia33.getImage();
			break;
		case 34:
			ImageIcon referencia34 = new ImageIcon(
					"res/fundoFase/cenario34.jpg");
			fundo = referencia34.getImage();
			break;
		case 35:
			ImageIcon referencia35 = new ImageIcon(
					"res/fundoFase/cenario35.jpg");
			fundo = referencia35.getImage();
			break;
		case 36:
			ImageIcon referencia36 = new ImageIcon(
					"res/fundoFase/cenario36.jpg");
			fundo = referencia36.getImage();
			break;
		case 37:
			ImageIcon referencia37 = new ImageIcon(
					"res/fundoFase/cenario37.jpg");
			fundo = referencia37.getImage();
			break;
		case 38:
			ImageIcon referencia38 = new ImageIcon(
					"res/fundoFase/cenario38.jpg");
			fundo = referencia38.getImage();
			break;
		case 39:
			ImageIcon referencia39 = new ImageIcon(
					"res/fundoFase/cenario39.jpg");
			fundo = referencia39.getImage();
			break;
		case 40:
			ImageIcon referencia40 = new ImageIcon(
					"res/fundoFase/cenario40.jpg");
			fundo = referencia40.getImage();
			break;
		case 41:
			ImageIcon referencia41 = new ImageIcon(
					"res/fundoFase/cenario41.jpg");
			fundo = referencia41.getImage();
			break;
		case 42:
			ImageIcon referencia42 = new ImageIcon(
					"res/fundoFase/fim.png");
			fundo = referencia42.getImage();
			break;
		}
	}

	/*
	 * ===========================================METODO CONTAR MISSÃO
	 */
	public void contarMissao() {
		contMissao++;
	}

	/**
	 * ===============================================================
	 * =============================================CLASS INTERNA FASE
	 * ===============================================================
	 */
	public class Fase extends JPanel {

		private static final long serialVersionUID = 1L;
		private int fundoEsp = 0;
		private Image gameover;

		// DEFINE TAMANHO DA FASE (DESENHOS NA FASE)
		protected static final int ALTURA = 600, LARGURA = 600;

		/*
		 * ==================================CONSTRUTO CLASS INTERNA FASE
		 */
		protected Fase() {
			// IMAGEM GAME OVER
			ImageIcon referencia = new ImageIcon(
					"res/gameOver/fimJogo.jpg");
			gameover = referencia.getImage();

			Timer velocidade = new Timer(18, new Velocidade());
			velocidade.start();

			setDoubleBuffered(true);
			// setFocusable(true);
		}

		/**
		 * ===============================================================
		 * =======================================CLASS INTERNA VELOCIDADE
		 * ===============================================================
		 */
		private class Velocidade implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// REPETIR FUNDO
				if (fundoEsp < 600)
					fundoEsp++;
				else
					fundoEsp = 0;
			}
		}

		/*
		 * =====================================METODO PAINT (GRAPHICS 2D)
		 */
		public void paint(Graphics g) {
			grafico = (Graphics2D) g;

			// FIM (ZERO)
			if (contMissao == 42) {
				timer.stop();// PARA O TEMPO
				zero = true;
			}

			// GAME OVER
			if (!nave.isVisivel()) {

				// SOM FIM DE JOGO
				SomFimJogo sfj = new SomFimJogo();
				sfj.main(null);

				// EXIBE IMAGEM (GAME OVER)
				grafico.drawImage(gameover, 150, 150, null);
				grafico.setColor(Color.WHITE);

				// MENSSAGEM
				grafico.drawString("TOTAL DE PONTOS: "
						+ (contPontos - naoAbatidos), 220, 400);
				grafico.drawString("ENTER PARA NOVO JOGO.", 220, 430);

				fimDeJogo = true;// STATUS DO JOGO

				timer.stop();// PARA O TEMPO

			} else if (nave.isVisivel()) {

				// DESENHA FUNDO (// REPETIR FUNDO)
				grafico.drawImage(fundo, 0, fundoEsp, null);
				grafico.drawImage(fundo, 0, fundoEsp - 600, null);

				// DESENHA NAVE
				grafico.drawImage(nave.getImagem(), nave.getX(), nave.getY(),
						nave.getX() + nave.getLar(),
						nave.getY() + nave.getAlt(),
						1 + (nave.getPos() * nave.getLar()), 1,
						1 + (nave.getPos() * nave.getLar()) + nave.getLar(),
						nave.getAlt() + 1, null);

				// DESENHA TIROS
				Tiro1 tiroTemp;
				for (int i = 0; i < tiros.size(); i++) {
					tiroTemp = tiros.get(i);

					grafico.drawImage(tiroTemp.getImagem(), tiroTemp.getX(),
							tiroTemp.getY(),
							tiroTemp.getX() + tiroTemp.getLar(),
							tiroTemp.getY() + tiroTemp.getAlt(), 1, 1,
							tiroTemp.getLar() + 1, tiroTemp.getAlt() + 1, null);
				}

				// DESENHA EXPLOSÕES
				Timer tempoTemp;
				Explosao expTemp;
				for (int i = 0; i < tempos.size(); i++) {
					tempoTemp = tempos.get(i);
					expTemp = explosoes.get(i);

					if (expTemp.getCont() == 3) {
						tempoTemp.stop();
						tempos.remove(i);
						explosoes.remove(i);
					} else {
						grafico.drawImage(expTemp.getImagem(), expTemp.getX(),
								expTemp.getY(),
								expTemp.getX() + expTemp.getLar(),
								expTemp.getY() + expTemp.getAlt(),
								2 + (expTemp.getCont() * expTemp.getLar()), 2,
								(expTemp.getCont() * expTemp.getLar())
										+ expTemp.getLar() + 2,
								expTemp.getAlt() + 2, null);
					}
				}

				// DESENHA INIMIGOS
				Inimigo iniTemp;
				for (int i = 0; i < inimigos.size(); i++) {
					iniTemp = inimigos.get(i);

					grafico.drawImage(iniTemp.getImagem(),
							(int) iniTemp.getX(), (int) iniTemp.getY(),
							(int) iniTemp.getX() + iniTemp.getLar(),
							(int) iniTemp.getY() + iniTemp.getAlt(), 1, 1,
							iniTemp.getLar() + 1, iniTemp.getAlt() + 1, null);
				}

				// DESENHA ADD VIDA
				AddVida addVidaTemp;
				for (int i = 0; i < addVida.size(); i++) {
					addVidaTemp = addVida.get(i);

					grafico.drawImage(addVidaTemp.getImagem(),
							(int) addVidaTemp.getX(), (int) addVidaTemp.getY(),
							(int) addVidaTemp.getX() + addVidaTemp.getLar(),
							(int) addVidaTemp.getY() + addVidaTemp.getAlt(), 1,
							1, addVidaTemp.getLar() + 1,
							addVidaTemp.getAlt() + 1, null);
				}

				/*
				 * ========================================MOUSE MIRA
				 */
				// DESENHA A MIRA, O -25 e -50 CENTRALIZA O OCURSOR
				// grafico.drawImage(mira.getImage(), xMira - 25, yMira - 50,
				// this);

			}
			g.dispose();
		}

		public int getLar() {
			return LARGURA;
		}

		public int getAlt() {
			return ALTURA;
		}
	}

	/*
	 * ============================================METODO CONTA PONTOS
	 */
	public void contarPontos() {
		contPontos++;
	}

	/*
	 * ============================================METODO REDUZ PONTOS
	 */
	public void reduzPontos() {
		contPontos = contPontos - 1;
	}

	/*
	 * =============================================METODO CONTA VIDAS
	 */
	public void contarVidas() {
		contVida++;
	}

	/*
	 * =============================================METODO REDUZ VIDAS
	 */
	public void reduzVidas() {
		contVida = contVida - 1;

		// (SOM) NOTIFICA (0) VIDA
		if (contVida == 0) {
			SomZeroVida szv = new SomZeroVida();
			szv.main(null);
		}
	}

	/**
	 * ===============================================================
	 * =============================CLASS INTERNA KEYADAPTER (TECLADO)
	 * ===============================================================
	 */
	private class keyAdapter extends KeyAdapter {
		/*
		 * ==========================METODO KEY PRESSED (PRECIONA TECLA)
		 */
		public void keyPressed(KeyEvent tecla) {
			int codigo = tecla.getKeyCode();

			// (GANHOU) CONTINUAR O JOGO
			if ((codigo == KeyEvent.VK_ENTER) & andamentoJogo == false) {

				contarMissao();// INCREMENTA MISSÃO

				definirMissao();// DEFINE MISSÃO

				new Nave();// CHAMA NAVE

				inicializaInimigos();// INIICALIZA INIMIGOS

				// INCREMENTA TEMPO DA MISSÃO
				t.minutos = 1;
				t.segundos = 59;

				andamentoJogo = true;// STATUS DO JOGO

				timer.start();// INICIA JOGO
			}

			// REINICIA JOGO (GAME OVER)
			if ((codigo == KeyEvent.VK_ENTER) && fimDeJogo == true) {

				nave.setVisivel(true);// REINICIA STATUS NAVE

				inicializaInimigos();// REINICIA INIMIGOS

				contVida = 3; // REINICIA VIDA

				contPontos = 0; // REINICIA PONTOS

				fimDeJogo = false; // REINICIA JOGO

				contMissao = 1;// REINICA MISSÃO

				definirMissao();// REDEFINE MISSÃO

				naoAbatidos = 0; // REINICIA

				// REINICIA TEMPO DA MISSÃO
				t.minutos = 1;
				t.segundos = 59;

				timer.start();// REINICIA JOGO
			}

			// MOVIMENTA NAVE (OPÇÃO 1)
			if (codigo == KeyEvent.VK_LEFT) {// PARA ESQUERDA

				// POSIÇÃO DA IMAGEM
				nave.setPos(4);
				nave.setPos(5);
				nave.setPos(6);

				nave.setDx(-1);// VELOCIDADE
			}

			if (codigo == KeyEvent.VK_RIGHT) {// PARA DIREITA
				// POSIÇÃO DA IMAGEM
				nave.setPos(1);
				nave.setPos(2);
				nave.setPos(3);

				nave.setDx(1);// VELOCIDADE
			}

			if (codigo == KeyEvent.VK_UP) {// PARA FRENTE
				nave.setDy(-1);// VELOCIDADE
			}

			if (codigo == KeyEvent.VK_DOWN) {// PARA TRAZ
				nave.setDy(1);// VELOCIDADE
			}

			// MOVIMENTA NAVE (OPÇÃO 2)
			if (codigo == KeyEvent.VK_A) {// PARA ESQUERDA

				// POSIÇÃO DA IMAGEM
				nave.setPos(4);
				nave.setPos(5);
				nave.setPos(6);

				nave.setDx(-1);// VELOCIDADE
			}

			if (codigo == KeyEvent.VK_D) {// PARA DIREITA

				// POSIÇÃO DA IMAGEM
				nave.setPos(1);
				nave.setPos(2);
				nave.setPos(3);

				nave.setDx(1);// VELOCIDADE
			}

			if (codigo == KeyEvent.VK_W) {// PARA FRENTE
				nave.setDy(-1);// VELOCIDADE
			}

			if (codigo == KeyEvent.VK_S) {// PARA TRAZ
				nave.setDy(1);// VELOCIDADE
			}

			// PAUSE JOGO
			if ((codigo == KeyEvent.VK_P) && (pause == false)) {
				pause = true;
				timer.stop();
			}

			// START GAME (PAUSE)
			if ((codigo == KeyEvent.VK_ENTER) && (pause == true)) {
				pause = false;
				timer.start();
			}
		}

		/*
		 * ==========================METODO KEY RELEASED (SOLTA TECLA)
		 */
		public void keyReleased(KeyEvent tecla) {
			int codigo = tecla.getKeyCode();

			// MOVIMENTA NAVE (OPÇÃO 1)
			if (codigo == KeyEvent.VK_LEFT) {// PARA ESQUERDA

				// POSIÇÃO DA IMAGEM
				nave.setPos(6);
				nave.setPos(5);
				nave.setPos(4);
				nave.setPos(0);

				nave.setDx(0);// VELOCIDADE
			}
			if (codigo == KeyEvent.VK_RIGHT) {// PARA DIREITA

				// POSIÇÃO DA IMAGEM
				nave.setPos(3);
				nave.setPos(2);
				nave.setPos(1);
				nave.setPos(0);

				nave.setDx(0);// VELOCIDADE
			}
			if (codigo == KeyEvent.VK_UP) {// PARA FRENTE
				nave.setDy(0);// VELOCIDADE
			}
			if (codigo == KeyEvent.VK_DOWN) {// PARA TRAZ
				nave.setDy(0);// VELOCIDADE
			}

			// MOVIMENTA NAVE (OPÇÃO 2)
			if (codigo == KeyEvent.VK_A) {// PARA ESQUERDA

				nave.setPos(6);
				nave.setPos(5);
				nave.setPos(4);
				nave.setPos(0);

				nave.setDx(0);// VELOCIDADE
			}
			if (codigo == KeyEvent.VK_D) {// PARA DIREITA

				// POSIÇÃO DA IMAGEM
				nave.setPos(3);
				nave.setPos(2);
				nave.setPos(1);
				nave.setPos(0);

				nave.setDx(0);// VELOCIDADE
			}
			if (codigo == KeyEvent.VK_W) {// PARA FRENTE
				nave.setDy(0);// VELOCIDADE
			}
			if (codigo == KeyEvent.VK_S) {// PARA TRAZ
				nave.setDy(0);// VELOCIDADE
			}
		}
	}

	/**
	 * ===============================================================
	 * ============================================CLASS INTERNA MOUSE
	 * ===============================================================
	 */
	private class Mouse implements MouseListener, MouseMotionListener {

		public void mouseDragged(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {
			// X e Y DA MIRA RECEBE AS COORDENADAS DO CURSOR
			xMira = e.getX();
			yMira = e.getY();

		}

		public void mouseClicked(MouseEvent arg0) {
			if (zero == false) {
				// DISPARO CENTRAL
				tiros.add(new Tiro1(nave.getX() + 20, nave.getY() + -25));

				// DISPARO DA DIREITA
				// tiros.add(new Tiro1(nave.getX() + 44, nave.getY() + 1));

				// DIPARO DA ESQUERDA
				// tiros.add(new Tiro1(nave.getX() + 0, nave.getY() + 1));

				// SOM DO DESPARO
				SomDesparo sd = new SomDesparo();
				sd.main(null);
			}
		}

		public void mouseEntered(MouseEvent arg0) {

		}

		public void mouseExited(MouseEvent arg0) {

		}

		public void mousePressed(MouseEvent arg0) {

		}

		public void mouseReleased(MouseEvent arg0) {

		}
	}

}
