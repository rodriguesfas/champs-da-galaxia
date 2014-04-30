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
package galaxia.champs.efeitos.sonoros;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;

import javazoom.jl.player.Player;

public class SomDesparo {

	public void main(String[] args) {

		// STRING COM O CAMINHO DO ARQUIVO A SER TOCADO
		String path = "res/sons/disparo/shoot.mp3";

		// INSTANCIAÇÃO DE UM OBJETO FILE COMO ARQUIVO MP3
		File mp3File = new File(path);

		// INSTANCIAÇÃO DO OBJETO MP3 SOM DESPARO
		MP3SomDesparo som = new MP3SomDesparo();
		som.tocarDesparo(mp3File);

		// CHAM O METODO QUE TOCA O SOM
		som.start();
	}

	/**
	 * ====================================================================
	 * =======================================CLASS INTERNA MP3 SOM DESPARO
	 * ====================================================================
	 */
	private static class MP3SomDesparo extends Thread {

		// Objeto para nosso arquivo MP3 a ser tocado
		private File mp3;

		// Objeto Player da biblioteca jLayer. Ele tocará o arquivo MP3
		private Player player;

		/*
		 * Construtor que recebe o objeto File referenciando o arquivo MP3 a ser
		 * tocado e atribui ao atributo MP3 da classe.
		 * 
		 * @param mp3
		 */
		public void tocarDesparo(File mp3) {
			this.mp3 = mp3;
		}

		/*
		 * ==================================================================
		 * =============================================METODO QUE TOCA O MP3
		 * ==================================================================
		 */
		public void run() {
			try {
				FileInputStream fis = new FileInputStream(mp3);
				BufferedInputStream bis = new BufferedInputStream(fis);
				this.player = new Player(bis);
				// System.out.println("Desparo!");
				this.player.play();
				// System.out.println("Terminado Desparo!");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Problema ao tocar Desparo"
						+ mp3);
				e.printStackTrace();
			}
		}
	}

}
