package scripts.MassFighter.Tasks.Shared;

import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import scripts.MassFighter.Framework.Methods;
import scripts.MassFighter.GUI.Settings;
import scripts.MassFighter.MassFighter;

/**
 * ozzy.
 */
public class Safety extends Task {

    @Override
    public boolean validate() {
        return (outOfFood() && (Settings.foodTeleport || Settings.foodLogout))
                ||  (Health.getCurrent() < Settings.criticalHitpoints && (Settings.healthTeleport || Settings.healthLogout));
    }

    @Override
    public void execute() {
        if ((outOfFood() && Settings.foodTeleport) || (Health.getCurrent() < Settings.criticalHitpoints && Settings.healthTeleport)) {
            MassFighter.status = "Teleporting away!";
            SpriteItemQueryResults validTeleportTabs = Inventory.newQuery().filter(item -> item != null
                    && item.getDefinition() != null && item.getDefinition().getName().toLowerCase().contains("teleport")).results();
            if (!validTeleportTabs.isEmpty()) {
                SpriteItem teleportTab = validTeleportTabs.random();
                if (teleportTab != null && teleportTab.click()) {
                    Execution.delayUntil(() -> !teleportTab.isValid(), 2500, 4000);
                }
            }
        }
        Methods.logout();
    }

    private boolean outOfFood() {
        return Methods.arrayIsValid(Settings.foodNames) && Methods.getFood().results().isEmpty();
    }
}
