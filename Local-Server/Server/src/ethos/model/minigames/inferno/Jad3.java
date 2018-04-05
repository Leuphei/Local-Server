package ethos.model.minigames.inferno;


import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;


public class Jad3 extends SingleInstancedArea {

	/**
	 * Player variables, start coordinates.
	 */
	private static final int START_X = 2271, START_Y = 5343;
	
	/**
	 * Npc variables, start coordinates.
	 */
	public static final int SPAWN_A = 2268, SPAWN_B = 5335; // SOUTH
	public static final int SPAWN_C = 2265, SPAWN_D = 5347; // NORTH WEST
	public static final int SPAWN_E = 2274, SPAWN_F = 5346; // NORTH EAST
	
	
	
	public Jad3(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	

	
	
	/**
	 * Constructs the content by creating an event
	 */
	public void init() {
		//Start event by starting Inferno here

	}
	public void spawnNpcs() {

	}
	
	
	public void initiateJad3() {
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (container.getOwner() == null || player == null || player.isDead || player.getJad3() == null) {
					player.sendMessage("heck");
					container.stop();
					return;
				}
				int cycle = container.getTotalTicks();
				// player.sendMessage("tick "+cycle);
				if (cycle == 1) {
					player.getPA().sendScreenFade("3 Yad instance loading...", -1, 5);
					player.getPA().movePlayer(START_X, START_Y, height = 0);
				} else if (cycle == 2) {
					player.turnPlayerTo(START_X, START_Y);
				} else if (cycle == 16) {
					Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD1, SPAWN_A, SPAWN_B, height, 0, 350, 113, 1000, 480, false, false);
				} else if (cycle == 19) {
					Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD1, SPAWN_C, SPAWN_D, height, 0, 350, 113, 1000, 480, false, false);
				} else if (cycle == 22) {
					Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD1, SPAWN_E, SPAWN_F, height, 0, 350, 113, 1000, 480, false, false);
				} else if (cycle >= 24) {
					player.getPA().resetCamera();
					// player.sendMessage("Stop");
					container.stop();
				}
			}
		}, 1);
	}

	/**
	 * Disposes of the content by moving the player and finalizing and or removing any left over content.
	 * 
	 * @param dispose the type of dispose
	 */
	public final void end(DisposeTypes dispose) {
		if (player == null) {
			return;
		}
		if (dispose == DisposeTypes.COMPLETE) {
			NPCHandler.kill(InfernoWave.JALTOK_JAD1, height);
			NPCHandler.kill(InfernoWave.JALTOK_JAD1, height);
			NPCHandler.kill(InfernoWave.JALTOK_JAD1, height);
			NPCHandler.kill(InfernoWave.YT_HURKOT, height);
			//Inferno.reward();
			player.getPA().movePlayer(2495, 5110, 0);
			player.zukDead = true;
		} else if (dispose == DisposeTypes.INCOMPLETE) {			
			NPCHandler.kill(InfernoWave.JALTOK_JAD1, height);
			NPCHandler.kill(InfernoWave.JALTOK_JAD1, height);
			NPCHandler.kill(InfernoWave.JALTOK_JAD1, height);
			NPCHandler.kill(InfernoWave.YT_HURKOT, height);
			player.getPA().movePlayer(2495, 5110, 0);
		}
	}

	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}
	
	public int getHeight() {
		return height;
	}
}
