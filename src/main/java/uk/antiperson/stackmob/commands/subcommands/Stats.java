package uk.antiperson.stackmob.commands.subcommands;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;
import uk.antiperson.stackmob.commands.User;

@CommandMetadata(command = "stats", playerReq = false, desc = "View statistics")
public class Stats extends SubCommand {

    private final StackMob sm;
    public Stats(StackMob sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(User sender, String[] args) {
        sender.sendInfo(sm.getEntityManager().getStackEntities().size() + " stacked entities");
        return false;
    }
}
