package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.CommandMetadata;
import uk.antiperson.stackmob.commands.SubCommand;
import uk.antiperson.stackmob.commands.User;
import uk.antiperson.stackmob.entity.StackEntity;

@CommandMetadata(command = "stats", playerReq = false, desc = "View plugin statistics.")
public class Stats extends SubCommand {

    private final StackMob sm;
    public Stats(StackMob sm) {
        this.sm = sm;
    }

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        sendMobStats(sender);
        return Command.SINGLE_SUCCESS;
    }

    private void sendMobStats(User sender) {
        int total = 0;
        int waiting = 0;
        int full = 0;
        for (StackEntity stackEntity : sm.getEntityManager().getStackEntities()) {
            if (stackEntity.isWaiting()) {
                waiting += 1;
            }
            if (stackEntity.isMaxSize()) {
                full += 1;
            }
            total += stackEntity.getSize();
        }
        sender.sendInfo("Stacking statistics:");
        sender.sendRawMessage("Total stack entities: " + sm.getEntityManager().getStackEntities().size() + " (" + total + " single entities.)");
        sender.sendRawMessage("Full stacks: " + full + " Waiting to stack: " + waiting);
    }
}
