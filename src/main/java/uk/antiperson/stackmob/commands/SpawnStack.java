package uk.antiperson.stackmob.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

@CommandMetadata(command = "spawn", playerReq = true, desc = "Spawn a new stack.")
public class SpawnStack extends SubCommand {

    private StackMob sm;
    public SpawnStack(StackMob sm) {
        super(ArgumentType.ENTITY_TYPE, ArgumentType.INTEGER);
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(args[1]));
        StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) entity);
        stackEntity.setSize(Integer.valueOf(args[2]));
        return false;
    }
}
