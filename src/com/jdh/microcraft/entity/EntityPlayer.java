package com.jdh.microcraft.entity;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntityHumanoid;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Light;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.gui.PlayerInventoryMenu;
import com.jdh.microcraft.gui.TransitionMenu;
import com.jdh.microcraft.gui.crafting.InventoryCraftingMenu;
import com.jdh.microcraft.item.Inventory;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.consumable.ItemConsumable;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.sound.Sound;
import com.jdh.microcraft.util.ControlHandler;
import com.jdh.microcraft.util.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityPlayer extends EntityHumanoid {
    // maintains a list of the previous N equipped items, used so when
    // this.equipped is null we can go back to the last existing item from this
    // list in the player's current inventory. fx.:
    // - player has sword equipped
    // - player picks up lantern, lantern is equipped
    // - player puts down lantern
    // - sword should be re-equipped
    private final List<Integer> lastEquippedItems;

    private final int color;

    public EntityPlayer(Level level, int color) {
        super(level, new Inventory(256));
        this.lastEquippedItems = new ArrayList<>();
        this.color = color;

        ItemStack glove = new ItemStack(new ItemInstance(Item.GLOVE, 0), 1);
        this.inventory.add(glove);
        this.equipped = glove;
    }

    @Override
    public boolean hurt(int amount, boolean ignoreArmor) {
        if (super.hurt(amount, ignoreArmor)) {
            Sound.PLAYER_HURT.play();
            return true;
        }

        return false;
    }

    @Override
    public Light getLight() {
        Light parent = super.getLight();
        return parent != null ? parent : new Light(
            this.x + (this.width / 2),
            this.y + (this.height / 2),
            2
        );
    }

    @Override
    public void die() {
        super.die();
        EntitySmashParticle.spawn(
            this.level,
            this.getCenterX(), this.getCenterY(),
            this.getColor(),
            10, 30
        );
        Global.game.playerDiedTicks = Global.ticks;
    }

    @Override
    public boolean canPickup(EntityItem e) {
        return true;
    }

    @Override
    public void pickup(ItemStack stack) {
        super.pickup(stack);
        Sound.PICKUP.play();
        Global.game.score++;
    }

    @Override
    public void onDepthChange(int prevDepth) {
        super.onDepthChange(prevDepth);
        Global.game.setLevel(this.level.depth);
        Global.game.setMenu(new TransitionMenu());
    }

    @Override
    public void tick() {
        super.tick();

        // track currently equipped item
        if (this.equipped != null &&
            (this.lastEquippedItems.size() == 0 ||
                this.lastEquippedItems.get(0) != this.equipped.instance.id)) {
            this.lastEquippedItems.add(0, this.equipped.instance.id);
        }

        // if nothing is equipped, try to equip previously equipped items
        // if there are no previous items to equip, equip the POW GLOVE
        if (this.equipped == null) {
            while (this.lastEquippedItems.size() != 0 && this.equipped == null) {
                this.equipped = this.inventory.findByInstanceId(
                    this.lastEquippedItems.remove(0));
            }

            if (this.equipped == null) {
                this.equipped = this.inventory.find(Item.GLOVE);
            }
        }

        // crafting menu
        if (ControlHandler.TOGGLE_CRAFTING.pressedTick()) {
            Global.game.setMenu(new InventoryCraftingMenu(this));
            return;
        }

        // item dropping
        if (ControlHandler.DROP.pressedTick() &&
            this.equipped != null &&
            this.equipped.instance.item.isDroppable()) {
            this.drop(this.equipped);
        }

        // get tile player is currently facing
        int fx = this.getFacingTileX(), fy = this.getFacingTileY();
        Tile ft = Tile.TILES[level.getTile(fx, fy)];

        // entities in this tile and the tile being faced, excluding this one
        List<Entity> facingEntities = Stream.concat(
            level.getEntities(this.tileX, this.tileY).stream(),
            level.getEntities(fx, fy).stream()
        ).filter(e -> e != this).collect(Collectors.toList());

        if (ControlHandler.INTERACT.pressedTick()) {
            boolean interactEntity = false, interactTile = false;

            if (!this.swimming) {
                // try to interact with facing entities
                for (Entity e : facingEntities) {
                    interactEntity |= e.interact(this);
                }

                // interact with facing tile if no entities were interacted with
                if (!interactEntity) {
                    interactTile = ft.interact(this.level, fx, fy, this);
                }
            }

            // toggle inventory
            if (!interactEntity && !interactTile) {
                Global.game.setMenu(new PlayerInventoryMenu(this, true));
                return;
            }
        }

        if (!this.swimming && ControlHandler.HIT.pressedTick()) {
            this.updateAnimationFrame(true);

            boolean hitEntity = false;
            for (Entity e : facingEntities) {
                if (this.hit(e)) {
                    e.onHit(this);
                    hitEntity = true;
                }
            }

            if (hitEntity) {
                this.animateAction();
            } else if (this.equipped.instance.item instanceof ItemConsumable) {
                this.equipped.instance.item.use(this.equipped.instance, level, this.tileX, this.tileY, this);
            } else if (this.equipped.instance.item instanceof ItemTool) {
                // try to use the active tool on the facing tile
                ItemTool tool = (ItemTool) this.equipped.instance.item;
                boolean ideal = (ft.getIdealTools() & tool.type) != 0,
                    usable = (ft.getUsableTools() & tool.type) != 0;

                if (ideal || usable) {
                    int staminaCost = (int) (tool.getStaminaCostMultiplier() *
                        (ideal ?
                            (1 + Global.random.nextInt(2)) :
                            (3 + Global.random.nextInt(3))));
                    if (this.takeStamina(staminaCost)) {
                        Item equippedItem = this.equipped.instance.item;
                        if (ft.hit(this.level, fx, fy, this)) {
                            this.animateHit(equippedItem, fx, fy);
                        } else {
                            // give stamina back if hit failed
                            this.stamina += staminaCost;
                        }
                    }
                }
            } else {
                // try to use the active item on the facing tile
                Item equippedItem = this.equipped.instance.item;
                if (equippedItem.use(this.equipped.instance, level, fx, fy, this)) {
                    this.animateHit(equippedItem, fx, fy);
                }
            }

            if (this.hitTicks == 0 && !hitEntity) {
                // no hit? animate an action
                this.animateAction();
                Sound.MISS.play();
            } else {
                Sound.HIT.play();
            }
        }

        int dx = 0, dy = 0;

        if (ControlHandler.UP.down()) {
            dy--;
        }

        if (ControlHandler.DOWN.down()) {
            dy++;
        }

        if (ControlHandler.LEFT.down()) {
            dx--;
        }

        if (ControlHandler.RIGHT.down()) {
            dx++;
        }

        if ((!this.swimming || Global.ticks % 2 == 0) && (dx == 0 || dy == 0)) {             
        	this.move(dx, dy);         
        }

        // override entity direction, the player should be able to change their
        // direction even when stationary
        if (dx != 0 || dy != 0) {
            this.direction = Direction.get(dx, dy);
        }

        // center camera on the player
        this.updateCamera();
    }

    // updates current renderer camera to track the player
    public void updateCamera() {
        Renderer.camera.centerOn(
            this.x + (this.width / 2), this.y + (this.height / 2),
            0, 0,
            Level.toPixel(this.level.width), Level.toPixel(this.level.height)
        );
    }

    @Override
    public double getStaminaRechargeRate() {
        return 0.11;
    }

    @Override
    public int getMaxStamina() {
        return 10;
    }

    @Override
    public int getMaxHealth() {
        return 10;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public int getHurtColor() {
        return Color.addAll(this.color, 222);
    }
}
