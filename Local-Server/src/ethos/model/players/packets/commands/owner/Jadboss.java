package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Jadboss extends Command {

	@Override
	public void execute(Player c, String input) {
		c.createJad3Instance();
		c.getJad3().initiateJad3();
	}

}

