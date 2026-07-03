package uk.antiperson.stackmob.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.*;
import uk.antiperson.stackmob.entity.StackEntity;

import java.util.HashSet;
import java.util.UUID;

@CommandMetadata(command = "spawn", playerReq = true, desc = "Spawn a new stack.")
public class SpawnStack extends SubCommand {

    private final StackMob sm;
    private final HashSet<UUID> confirm;
    public SpawnStack(StackMob sm) {
        super(CommandArgument.construct(StringArgumentType.greedyString(), false),
                CommandArgument.construct(IntegerArgumentType.integer(1, Integer.MAX_VALUE), false, "stack size"),
                CommandArgument.construct(IntegerArgumentType.integer(1, Integer.MAX_VALUE), true, "number of stacks"));
        this.sm = sm;
        this.confirm = new HashSet<>();
    }

    public int onCommand(CommandContext<CommandSourceStack> ctx, User sender) {
        Player player = (Player) sender.getSender();
        EntityType entityType = EntityType.valueOf(ctx.getArgument("type", String.class).toUpperCase());
        int newSize = ctx.getArgument("size", Integer.class);
        if (newSize < 1) {
            sender.sendError("You cannot spawn a stack with size less than one!");
            return Command.SINGLE_SUCCESS;
        }
        int maxSize = sm.getMainConfig().getConfig(entityType).getMaxStack();
        if (newSize > maxSize) {
            sender.sendError("Provided stack value is too large! (the maximum for " + entityType + " is " + maxSize + ")");
            return Command.SINGLE_SUCCESS;
        }
        int amountOfStacks = ctx.getArgument("amount", Integer.class);
        if (amountOfStacks < 1) {
            sender.sendError("You cannot spawn less than one stack!");
            return Command.SINGLE_SUCCESS;
        }
        if (amountOfStacks > 20 && !confirm.contains(((Player) sender.getSender()).getUniqueId())) {
            sender.sendInfo("Are you sure you want to spawn " + amountOfStacks + " stacks?");
            sender.sendInfo("Run the same command again to confirm.");
            confirm.add(((Player) sender.getSender()).getUniqueId());
            return Command.SINGLE_SUCCESS;
        }
        for (int i = 0; i < amountOfStacks; i++) {
            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), entityType);
            StackEntity stackEntity = sm.getEntityManager().registerStackedEntity(entity);
            stackEntity.setSize(newSize);
        }
        String stackString = amountOfStacks == 1 ? "A new stack has" : amountOfStacks + " stacks have";
        sender.sendSuccess(stackString + " been spawned.");
        confirm.remove(((Player) sender.getSender()).getUniqueId());
        return Command.SINGLE_SUCCESS;
    }
}
