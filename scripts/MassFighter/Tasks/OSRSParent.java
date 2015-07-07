package scripts.MassFighter.Tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.script.framework.task.Task;

/**
 * ozzy.
 */
public class OSRSParent extends Task {
    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        return Environment.isOSRS();
    }
}
