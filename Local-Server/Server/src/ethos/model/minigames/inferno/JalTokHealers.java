package ethos.model.minigames.inferno;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Range;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.util.Misc;


public class JalTokHealers {
	
	
	
	/**
	 * Checks the healer stage to avoid multiple spawns
	 */
	public static int stage = 0;
	
	/**
	 * Spawns healers when scorpia reaches below a certain health
	 */
	public static void spawnJadHealer() {
		NPC JALTOK_JAD1 = NPCHandler.getNpc(InfernoWave.JALTOK_JAD1);
		if (JALTOK_JAD1 == null) {
			return;
		}

		List<NPC> Jadhealer = Arrays.asList(NPCHandler.npcs);
		if (Jadhealer.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 7705 && !n.isDead && n.getHealth().getAmount() > 0)) {
			return;
		}

		int maximumHealth = JALTOK_JAD1.getHealth().getMaximum();
		int currentHealth = JALTOK_JAD1.getHealth().getAmount();
		int percentRemaining = (int) (((double) currentHealth / (double) maximumHealth) * 100D);

		if (percentRemaining > 50) {
			return;
		}

		if (!Misc.passedProbability(Range.between(0, percentRemaining), 10, true)) {
			return;
		}
		if (stage == 0) {
			NPCHandler.spawnNpc(7705, 2270, 5343, 0, 0, 70, 18, 100, 120);
			NPCHandler.spawnNpc(7705, 2268, 5345, 0, 0, 70, 18, 100, 120);
			NPCHandler.spawnNpc(7705, 2267, 5341, 0, 0, 70, 18, 100, 120);
			stage = 1;
		}
	}

}
