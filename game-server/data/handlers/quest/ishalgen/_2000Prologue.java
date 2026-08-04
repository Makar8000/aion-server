package quest.ishalgen;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.Rates;
import com.aionemu.gameserver.model.templates.TitleTemplate;
import com.aionemu.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.abyss.GloryPointsService;
import com.aionemu.gameserver.services.toypet.PetAdoptionService;
import com.aionemu.gameserver.services.CommandsAccessService;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _2000Prologue extends AbstractQuestHandler {

	public _2000Prologue() {
		super(2000);
	}

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (player.getRace() == Race.ASMODIANS && !player.getQuestStateList().hasQuest(questId)) {
			env.setQuestId(questId);
			if (QuestService.startQuest(env) || player.getQuestStateList().getQuestState(questId).getStatus() == QuestStatus.START) {
				playQuestMovie(env, 2, true);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 2)
			return;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return;
		qs.setStatus(QuestStatus.REWARD);
		QuestService.finishQuest(env);

		// Add all non-Elyos titles
		for (int i = 1; i <= 300; i++) {
			TitleTemplate titleTemplate = DataManager.TITLE_DATA.getTitleTemplate(i);
			if (titleTemplate.getRace() != Race.ELYOS) {
				player.getTitleList().addTitle(titleTemplate.getTitleId(), false, 0);
			}
		}
		// Set default title
		player.getCommonData().setTitleId(300);

		// Give permission for custom commands
		// admin
		CommandsAccessService.giveAccess(player, player.getObjectId(), "add");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "addset");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "dispel");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "dropinfo");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "dye");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "equip");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "goto");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "heal");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "info");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "invis");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "invul");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "kill");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "moveplayertoplayer");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "moveto");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "movetome");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "movetoobj");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "pet");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "playerinfo");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "quest");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "remove");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "removecd");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "res");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "speed");
		// regular
		CommandsAccessService.giveAccess(player, player.getObjectId(), "itemcooltime");
		CommandsAccessService.giveAccess(player, player.getObjectId(), "teleport");
		
		// Max lvl, ap, gp
		player.getCommonData().addExp(999999999, Rates.XP_QUEST);
		AbyssPointsService.addAp(player, 999999999);
		GloryPointsService.addGp(player.getObjectId(), 999999999);

		// Pets
		// Siberian Wild Tiger (Purebred)
		PetAdoptionService.addPet(player, 900000, "buffs", 0, 0);
		// Ailu Claw
		PetAdoptionService.addPet(player, 900189, "pouch", 0, 0);
	}
}
