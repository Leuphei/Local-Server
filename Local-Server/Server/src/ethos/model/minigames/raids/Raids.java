package ethos.model.minigames.raids;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import ethos.Server;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.world.Clan;
import ethos.world.objects.GlobalObject;

public class Raids {
	
	private SingleInstancedArea raidInstance;
	
	public static final int START_X = 3305;
	public static final int START_Y = 5196;
	public int raidHeight;
	
	public int activeBraziers;
	
	private Player player;
	
	public Raids(Player player) {
		this.player = player;
	}
	
	public void initRaids() {
		if (raidInstance != null) 
			InstancedAreaManager.getSingleton().disposeOf(raidInstance);
		
		activeBraziers = 0;
		raidHeight = InstancedAreaManager.getSingleton().getNextOpenHeight(Boundary.RAID_MAIN);
		raidInstance = new RaidInstance(player, Boundary.RAID_MAIN, raidHeight);
		raidInstance.getPlayer();
		
		player.getPA().removeAllWindows();
		
		/*CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (container.getOwner() == null || player == null || player.isDead || player.getRaids() == null) {
					container.stop();
					return;
				}
				
				int cycle = container.getTotalTicks();
				
				if (cycle == 1) {
					player.getPA().sendScreenFade("", 1, 3);
				} else if (cycle == 6) {
					teleportToStart();
					player.getPA().sendScreenFade("", -1, 3);
					container.stop();
				}
			}
		}, 1);*/
		
		teleportToStart();
		InstancedAreaManager.getSingleton().add(raidHeight, raidInstance);
		spawnSkeletals();
		//raidInstance.spawnSkeletals();
	}
	
	public void teleportToStart() {
		player.getPA().movePlayer(START_X, START_Y, raidHeight);
	}
	
	public boolean handleObjects(int objectId, int objX, int objY) {
		Player raidLeader = this.getRaidLeader();
		
		if (raidLeader == null) {
			return false;
		}
		
		RaidInstance instance = (RaidInstance) raidLeader.getRaids().getRaidInstance();
		
		if (instance == null) {
			return false;
		}
		
		if (objectId == 29789) {
			if (objX == 3307 && objY == 5205) { // To lizard shaman
				player.getPA().movePlayer(3307, 5208);
				return true;
			}
				
			if (objX == 3311 && objY == 5276) { // Lizard Shaman Exit to Vasa
				int left = getLizardsRemaining(raidLeader);
				if (left > 0) {
					player.sendMessage("You can not leave until the room has been cleared.");
					player.sendMessage("There "+(left == 1 ? "is" : "are")+" "+left+" Lizard Shaman"+(left == 1 ? "" : "s")+" remaining.");
					return true;
				}
				player.getPA().movePlayer(3311, 5279);
				return true;
			}
				
			if (objX == 3311 && objY == 5308) { // Vasa exit to Vanguards
				if (!raidLeader.getRaids().isVasaDead()) {
					player.sendMessage("You can not leave until Vasa has been defeated.");
					return true;
				}
				player.getPA().movePlayer(3311, 5311);
				return true;
			}
			
			if (objX == 3311 && objY == 5341) { // vanguard exit to ice demon
				int left = getVanguardsRemaining(raidLeader);
				if (left > 0) {
					player.sendMessage("You can not leave until the room has been cleared.");
					player.sendMessage("There "+(left == 1 ? "is" : "are")+" "+left+" Vanguard"+(left == 1 ? "" : "s")+" remaining.");
					return true;
				}
				player.getPA().movePlayer(3312, 5344);
				return true;
			}
			
			if (objX == 3310 && objY == 5370) { // ice demon exit to thieving room
				if (!raidLeader.getRaids().isIceDemonDead()) {
					player.sendMessage("You can not leave until Ice Demon has been defeated.");
					return true;
				}
				player.getPA().movePlayer(3311, 5373);
				return true;
			} 
		
			
			if (objX == 3318 && objY == 5400) { // thieving room exit to Mages
				player.getPA().movePlayer(3312, 5218, raidHeight + 1);
				return true;
			} 
			
			if (objX == 3309 && objY == 5274) { // mage exit to tekton
				int left = getMysticsRemaining(raidLeader);
				if (left > 0) {
					player.sendMessage("You can not leave until the room has been cleared.");
					player.sendMessage("There "+(left == 1 ? "is" : "are")+" "+left+" Skeletal Mystic"+(left == 1 ? "" : "s")+" remaining.");
					return true;
				}
				player.getPA().movePlayer(3310, 5277);
			} 
			
			if (objX == 3310 && objY == 5306) { // tekton exit to muttadiles
				if (!raidLeader.getRaids().isTektonDead()) {
					player.sendMessage("You can not leave until Tekton has been defeated.");
					return true;
				}
				player.getPA().movePlayer(3311, 5309);
			}
			
			if (objX == 3308 && objY == 5337) { // muttadiles exit to magers/rangers
				if (!raidLeader.getRaids().isMuttadileDead()) {
					player.sendMessage("You can not leave until Muttadile has been defeated.");
					return true;
				}
				player.getPA().movePlayer(3309, 5340);
			}
			
			if (objX == 3309 && objY == 5369) { // magers/rangers exit to Great Olm
				int left = getDeathlyRemaining(raidLeader);
				if (left > 0) {
					player.sendMessage("You can not leave until the room has been cleared.");
					player.sendMessage("There "+(left == 1 ? "is" : "are")+" "+left+" enem"+(left == 1 ? "y" : "ies")+" remaining.");
					return true;
				}
				player.getPA().movePlayer(3276, 5159);
			}
			return true;
		}
		
		if (objectId == 29734) { // to great olm
			player.getPA().movePlayer(3233, 5720, raidHeight);
			return true;
		}
		
		if (objectId == 29879) { // olm barrier
			player.getPA().movePlayer(3233, 5730, raidHeight);
			return true;
		}
		
		if (objectId == 29996) { // to great olm
			player.getPA().movePlayer(3233, 5720, raidHeight);
			return true;
		}

		if (objectId == 30066) {
			if (player.specRestore > 0) {
				int seconds = ((int)Math.floor(player.specRestore * 0.6));
				player.sendMessage("You have to wait another " + seconds + " seconds to use the energy well.");
				return true;
			}
			
			player.specRestore = 120;
			player.specAmount = 10.0;
			player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
			player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
			player.getHealth().removeAllStatuses();
			player.getHealth().reset();
			player.getPA().refreshSkill(5);
			player.sendMessage("You touch the energy well");
			return true;
		}
		
		player.sendMessage("Clicked "+objectId+" at "+objX+", "+objY+"");
		return false;
	}
	
	private NPC iceDemon;
	private NPC tekton;
	private NPC muttadile;
	private NPC vasa;
	
	private NPC[] lizardShaman;
	private NPC[] skeletalMages;
	private NPC[] deathly;
	private NPC[] vanguards;
	
	/**
	 * Room 1: Lizard Shaman 7375
	 * Room 2: Vasa Nistirio (7566)
	 * Room 3: Vanguards (Melee E, Mage N, Range SW) 7527 melee, 7528 range, 7529 mage, 
	 * Room 4: Magers on platform (7559, 7560) Ranger top, mager bottom
	 * Room 5: 
	 */ 
	public void spawnSkeletals() {
		muttadile 	= NPCHandler.spawn(7563, 3306, 5330, raidHeight + 1, 1, 650, 25, 700, 300, true);
		vasa 		= NPCHandler.spawn(7566, 3309, 5292, raidHeight, -1, 1050, 25, 700, 300, true);
		iceDemon 	= NPCHandler.spawn(7585, 3310, 5368, raidHeight, -1, 1300, 45, 1000, 2500, true);
		tekton 		= NPCHandler.spawn(7544, 3310, 5293, raidHeight + 1, -1, 1300, 45, 1000, 2500, true);
		
		lizardShaman = new NPC[] {
			NPCHandler.spawn(7573, 3304, 5262, raidHeight, 1, 500, 25, 600, 500, true),
			NPCHandler.spawn(7573, 3312, 5258, raidHeight, 1, 500, 25, 600, 500, true),
			NPCHandler.spawn(7573, 3303, 5262, raidHeight, 1, 500, 30, 700, 500, true)
		};
		
		skeletalMages = new NPC[] {
			NPCHandler.spawn(7604, 3318, 5262, raidHeight + 1, -1, 500, 25, 600, 500, true),
			NPCHandler.spawn(7606, 3312, 5258, raidHeight + 1, -1, 500, 25, 600, 500, true),
			NPCHandler.spawn(7605, 3303, 5262, raidHeight + 1, -1, 500, 30, 700, 500, true),
			NPCHandler.spawn(7605, 3310, 5267, raidHeight + 1, -1, 500, 30, 700, 500, true)
		};
		
		deathly = new NPC[] {
			NPCHandler.spawn(7559, 3316, 5370, raidHeight + 1, -1, 500, 25, 600, 500, true), // deathly ranger
			NPCHandler.spawn(7559, 3317, 5369, raidHeight + 1, -1, 500, 25, 600, 500, true), // deathly ranger
			NPCHandler.spawn(7559, 3318, 5370, raidHeight + 1, -1, 500, 30, 700, 500, true), // deathly ranger
			
			NPCHandler.spawn(7560, 3317, 5362, raidHeight + 1, -1, 500, 25, 600, 500, true), // deathly mager
			NPCHandler.spawn(7560, 3318, 5363, raidHeight + 1, -1, 500, 25, 600, 500, true), // deathly mager
			NPCHandler.spawn(7560, 3317, 5363, raidHeight + 1, -1, 500, 30, 700, 500, true), // deathly mager
		};
		
		vanguards = new NPC[] {
			NPCHandler.spawn(7527, 3317, 5328, raidHeight, -1, 500, 25, 600, 500, true), // melee vanguard
			NPCHandler.spawn(7528, 3308, 5324, raidHeight, -1, 500, 25, 600, 500, true), // range vanguard
			NPCHandler.spawn(7529, 3307, 5334, raidHeight, -1, 500, 30, 700, 500, true), // magic vanguard
		};
	}
	
	public void killAllSpawns() {
		if (iceDemon != null)
			iceDemon.isDead = true;
		if (tekton != null)
			tekton.isDead = true;
		if (muttadile != null)
			muttadile.isDead = true;
		if (vasa != null)
			vasa.isDead = true;
		
		Arrays.stream(lizardShaman).filter(Objects::nonNull).forEach(npc -> npc.isDead = true);
		Arrays.stream(skeletalMages).filter(Objects::nonNull).forEach(npc -> npc.isDead = true);
		Arrays.stream(vanguards).filter(Objects::nonNull).forEach(npc -> npc.isDead = true);
		Arrays.stream(deathly).filter(Objects::nonNull).forEach(npc -> npc.isDead = true);
	}
	
	public boolean isMuttadileDead() {
		return muttadile != null && muttadile.isDead;
	}
	
	public boolean isIceDemonDead() {
		return iceDemon != null && iceDemon.isDead;
	}
	
	public boolean isTektonDead() {
		return tekton != null && tekton.isDead;
	}
	
	public boolean isVasaDead() {
		return vasa != null && vasa.isDead;
	}
	
	public int getMysticsRemaining(Player raidLeader) {
		return (int) Arrays.stream(raidLeader.getRaids().skeletalMages).filter(n -> !n.isDead).count();
	}
	
	public int getVanguardsRemaining(Player raidLeader) {
		return (int) Arrays.stream(raidLeader.getRaids().vanguards).filter(n -> !n.isDead).count();
	}
	
	public int getLizardsRemaining(Player raidLeader) {
		return (int) Arrays.stream(raidLeader.getRaids().lizardShaman).filter(n -> !n.isDead).count();
	}
	
	public int getDeathlyRemaining(Player raidLeader) {
		return (int) Arrays.stream(raidLeader.getRaids().deathly).filter(n -> !n.isDead).count();
	}
	
	public SingleInstancedArea getRaidInstance() {
		return raidInstance;
	}
	
	public Player getRaidLeader() {
		Clan clan = player.clan;
		
		if (clan == null) {
			return null;
		}
		
		Optional<Player> raidLeader =  Arrays.stream(PlayerHandler.players)
				.filter(Objects::nonNull)
				.filter(pl -> pl.getName().equalsIgnoreCase(clan.getFounder()))
				.findFirst();
		
		if (!raidLeader.isPresent() || raidLeader.get().getRaids().getRaidInstance() == null) {
			return null;
		}
		
		return raidLeader.get();
	}
	
	public static void lightBrazer(Player player, int id, int objX, int objY) {
		Player raidLeader = player.getRaids().getRaidLeader();
		
		if (raidLeader == null) {
			return;
		}
		
		if (!player.getItems().playerHasItem(20799, 1)) {
			player.sendMessage("You need some kindling to light this brazier!");
			return;
		}
		
		int height = raidLeader.getRaids().getRaidInstance().getHeight();
		
		Server.getGlobalObjects().replace(
				new GlobalObject(29747, objX, objY, height, 0, 10, 50, -1),
				new GlobalObject(29748, objX, objY, height, 0, 10, 50, 29747));
		
		player.getItems().deleteItem(20799, 1);
		
		raidLeader.sendMessage("Some has lit one of the braziers!");
	}
	
}
