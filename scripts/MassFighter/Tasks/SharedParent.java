package scripts.MassFighter.Tasks;

import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.script.framework.task.Task;

/**
 * ozzy.
 */
public class SharedParent extends Task {
    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return RuneScape.isLoggedIn();
    }
}
