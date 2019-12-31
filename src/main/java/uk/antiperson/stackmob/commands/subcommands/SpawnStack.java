package uk.antiperson.stackmob.commands.subcommands;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.commands.*;
import uk.antiperson.stackmob.entity.StackEntity;

@CommandMetadata(command = "spawn", playerReq = true, desc = "Spawn a new stack.")
public class SpawnStack extends SubCommand {

    private StackMob sm;
    public SpawnStack(StackMob sm) {
        super(CommandArgument.construct(ArgumentType.ENTITY_TYPE), CommandArgument.construct(ArgumentType.INTEGER));
        this.sm = sm;
    }

    @Override
    public boolean onCommand(User sender, String[] args) {
        Player player = (Player) sender;
        Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(args[0].toUpperCase()));
        StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) entity);
        stackEntity.setSize(Integer.valueOf(args[1]));
        return false;
    }
}
