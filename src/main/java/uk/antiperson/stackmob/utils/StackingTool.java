package uk.antiperson.stackmob.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

public class StackingTool {

    private final Player player;
    private final ItemStack itemStack;
    private final StackMob sm;
    public StackingTool(StackMob sm, Player player) {
        this.sm = sm;
        this.player = player;
        this.itemStack = player.getInventory().getItemInMainHand();
    }

    public int getModeId() {
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(sm.getToolKey(), PersistentDataType.INTEGER, 1);
    }

    public ToolMode getMode() {
        for (ToolMode t : ToolMode.values()) {
            if (t.getId() == getModeId()) {
                return t;
            }
        }
        throw new UnsupportedOperationException("No matching tool mode for given id!");
    }

    public void shiftMode() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        int nextMode = (getModeId() + 1) > ToolMode.values().length ? 1 : getModeId() + 1;
        itemMeta.getPersistentDataContainer().set(sm.getToolKey(), PersistentDataType.INTEGER, nextMode);
        itemStack.setItemMeta(itemMeta);
        player.getInventory().setItemInMainHand(itemStack);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Shifted mode to " + getMode()));
    }

    public void performAction(StackEntity clicked) {
        switch (getMode()) {
            case MODIFY:
                ConversationFactory factory = new ConversationFactory(sm)
                        .withTimeout(25)
                        .withFirstPrompt(new ModifyPrompt(clicked))
                        .withLocalEcho(false)
                        .withPrefix(conversationContext -> Utilities.PREFIX)
                        .addConversationAbandonedListener(new ExitPrompt());
                Conversation conversation = factory.buildConversation(player);
                conversation.begin();
                break;
            case SLICE:
                if (!clicked.isSingle()) {
                    clicked.slice();
                    clicked.removeStackData();
                    break;
                }
                player.sendMessage("Entity is single so cannot be sliced!");
                break;
            case REMOVE_CHUNK:
                for (Entity e : clicked.getEntity().getChunk().getEntities()) {
                    if (!(e instanceof Mob)) {
                        continue;
                    }
                    if (!sm.getEntityManager().isStackedEntity((LivingEntity) e)) {
                        continue;
                    }
                    StackEntity stackEntity = sm.getEntityManager().getStackEntity((LivingEntity) e);
                    stackEntity.removeStackData();
                }
                break;
            case REMOVE_SINGLE:
                clicked.removeStackData();
                break;
        }
    }

    enum ToolMode {
        MODIFY(1),
        SLICE(2),
        REMOVE_SINGLE(3),
        REMOVE_CHUNK(4);

        private final int id;
        ToolMode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private class ModifyPrompt extends NumericPrompt {

        private final StackEntity stackEntity;
        public ModifyPrompt(StackEntity stackEntity) {
            this.stackEntity = stackEntity;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            if (stackEntity.getEntity().isDead()) {
                conversationContext.getForWhom().sendRawMessage(Utilities.PREFIX + ChatColor.RED + "Entity is no longer valid. Modification has been cancelled.");
                return Prompt.END_OF_CONVERSATION;
            }
            stackEntity.setSize(number.intValue());
            conversationContext.getForWhom().sendRawMessage(Utilities.PREFIX + ChatColor.GREEN + "Stack value has been updated.");
            return Prompt.END_OF_CONVERSATION;
        }

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext conversationContext) {
            return ChatColor.YELLOW +  "Enter stack size: ";
        }

        @Nullable
        @Override
        protected String getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
            return Utilities.PREFIX + ChatColor.RED + "Invalid input. Accepted sizes: 1-" + stackEntity.getMaxSize();
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            if (input.intValue() > stackEntity.getMaxSize()) {
                return false;
            }
            if (input.intValue() < 1) {
                return false;
            }
            return true;
        }
    }

    private class ExitPrompt implements ConversationAbandonedListener {

        @Override
        public void conversationAbandoned(@NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
            if (conversationAbandonedEvent.gracefulExit()) {
                return;
            }
            conversationAbandonedEvent.getContext().getForWhom().sendRawMessage(Utilities.PREFIX + ChatColor.RED + "Stack modification has timed out.");
        }

    }
}
