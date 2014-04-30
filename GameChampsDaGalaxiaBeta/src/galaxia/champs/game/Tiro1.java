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

public class Tiro1 {

	private int x, y;
	private Image imagem;
	private boolean isVisivel;
	private static final int VELOCIDADE = 2;
	private static final int ALTURA = 50;
	private static final int LARGURA = 20;
	
	public Tiro1(int x, int y) {
		this.x = x;
		this.y = y;
		ImageIcon referencia = new ImageIcon("res/armas/laiser.png");
		imagem = referencia.getImage();
		isVisivel = true;
	}

	public void mover() {
		y -= VELOCIDADE;
	}

	public Image getImagem() {
		return imagem;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAlt() {
		return ALTURA;
	}

	public int getLar() {
		return LARGURA;
	}

	public boolean isVisivel() {
		return isVisivel;
	}

	public void setVisivel(boolean visivel) {
		isVisivel = visivel;
	}

}
