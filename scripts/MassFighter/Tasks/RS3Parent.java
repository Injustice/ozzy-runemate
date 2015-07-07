package scripts.MassFighter.Tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.script.framework.task.Task;

/**
 * ozzy.
 */
public class RS3Parent extends Task {

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return Environment.isRS3() && RuneScape.isLoggedIn();
    }
}
