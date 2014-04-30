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

import java.awt.Image;

import javax.swing.ImageIcon;

public class Inimigo {

	private double x, y;
	private Image imagem;
	private boolean isVisivel;
	private static final int LARGURA = 50, ALTURA = 52;
	private static int contador = 0;
	private ImageIcon referencia;

	// VELOCIDADE INIMIGO
	private static final double VELOCIDADE = 0.5;

	public Inimigo(int x, int y) {
		this.x = x;
		this.y = y;

		if (contador++ % 2 == 0) {
			referencia = new ImageIcon("res/inimigos/asteroid32.png");

		} else if (contador++ % 3 == 0) {
			referencia = new ImageIcon("res/inimigos/DeathFighter1.png");

		} else if (contador++ % 4 == 1) {
			referencia = new ImageIcon("res/inimigos/spikeyenemy.png");

		} else {
			referencia = new ImageIcon("res/inimigos/wings.png");

		}
		imagem = referencia.getImage();

		// DEFINE INIMIGO VISIVEL
		isVisivel = true;
	}

	public void mover() {
		y += VELOCIDADE;
	}

	public int getAlt() {
		return ALTURA;
	}

	public int getLar() {
		return LARGURA;
	}

	public Image getImagem() {
		return imagem;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean isVisivel() {
		return isVisivel;
	}

	public void setVisivel(boolean visivel) {
		isVisivel = visivel;
	}

}
