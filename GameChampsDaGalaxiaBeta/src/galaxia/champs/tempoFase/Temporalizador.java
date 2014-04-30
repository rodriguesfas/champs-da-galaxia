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
package galaxia.champs.tempoFase;

import java.util.Timer;
import java.util.TimerTask;

public class Temporalizador {
	Timer timer;
	public int minutos = 1;
	public int segundos = 59;

	public Temporalizador() {
		timer = new Timer();
		timer.schedule(new DisplayCountdown(), 0, 1000);
	}

	class DisplayCountdown extends TimerTask {

		public void run() {
			if (segundos >= 0) {
				// System.out.println(minutos + ":" + segundos);
				segundos--;
			} else if (minutos > 0) {
				minutos--;
				segundos = 59;
			} /*
			 * else { System.out.println("Misão Completa!"); }
			 */
		}
	}
}
