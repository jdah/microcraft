package com.jdh.microcraft.item;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.armor.*;
import com.jdh.microcraft.item.consumable.ItemApple;
import com.jdh.microcraft.item.consumable.ItemBread;
import com.jdh.microcraft.item.consumable.ItemCactusGoo;
import com.jdh.microcraft.item.consumable.ItemPie;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.furniture.*;
import com.jdh.microcraft.item.resource.*;
import com.jdh.microcraft.item.tool.*;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Item {
    public static final Item[] ITEMS = new Item[256];

    public static final ItemGlove GLOVE = new ItemGlove(1);
    public static final ItemRock ROCK = new ItemRock(2);
    public static final ItemWood WOOD = new ItemWood(3);
    public static final ItemOre GOLD_ORE = new ItemOre(4, Metal.GOLD);
    public static final ItemIngot GOLD_INGOT = new ItemIngot(5, Metal.GOLD);
    public static final ItemOre IRON_ORE = new ItemOre(6, Metal.IRON);
    public static final ItemIngot IRON_INGOT = new ItemIngot(7, Metal.IRON);

    public static final ItemSword WOOD_SWORD = new ItemSword(8, Recipe.STATION_BENCH, Material.WOOD);
    public static final ItemShovel WOOD_SHOVEL = new ItemShovel(9, Recipe.STATION_BENCH, Material.WOOD);
    public static final ItemPickaxe WOOD_PICKAXE = new ItemPickaxe(10, Recipe.STATION_BENCH, Material.WOOD);
    public static final ItemAxe WOOD_AXE = new ItemAxe(11, Recipe.STATION_BENCH, Material.WOOD);
    public static final ItemHoe WOOD_HOE = new ItemHoe(12, Recipe.STATION_BENCH, Material.WOOD);

    public static final ItemSword GOLD_SWORD = new ItemSword(13, Recipe.STATION_ANVIL, Material.GOLD);
    public static final ItemShovel GOLD_SHOVEL = new ItemShovel(14, Recipe.STATION_ANVIL, Material.GOLD);
    public static final ItemPickaxe GOLD_PICKAXE = new ItemPickaxe(15, Recipe.STATION_ANVIL, Material.GOLD);
    public static final ItemAxe GOLD_AXE = new ItemAxe(16, Recipe.STATION_ANVIL, Material.GOLD);
    public static final ItemHoe GOLD_HOE = new ItemHoe(17, Recipe.STATION_ANVIL, Material.GOLD);

    public static final ItemSword IRON_SWORD = new ItemSword(18, Recipe.STATION_ANVIL, Material.IRON);
    public static final ItemShovel IRON_SHOVEL = new ItemShovel(19, Recipe.STATION_ANVIL, Material.IRON);
    public static final ItemPickaxe IRON_PICKAXE = new ItemPickaxe(20, Recipe.STATION_ANVIL, Material.IRON);
    public static final ItemAxe IRON_AXE = new ItemAxe(21, Recipe.STATION_ANVIL, Material.IRON);
    public static final ItemHoe IRON_HOE = new ItemHoe(22, Recipe.STATION_ANVIL, Material.IRON);

    public static final ItemSword GEM_SWORD = new ItemSword(23, Recipe.STATION_ANVIL, Material.GEM);
    public static final ItemShovel GEM_SHOVEL = new ItemShovel(24, Recipe.STATION_ANVIL, Material.GEM);
    public static final ItemPickaxe GEM_PICKAXE = new ItemPickaxe(25, Recipe.STATION_ANVIL, Material.GEM);
    public static final ItemAxe GEM_AXE = new ItemAxe(26, Recipe.STATION_ANVIL, Material.GEM);
    public static final ItemHoe GEM_HOE = new ItemHoe(27, Recipe.STATION_ANVIL, Material.GEM);

    public static final ItemCactus CACTUS = new ItemCactus(28);
    public static final ItemSeed SEED = new ItemSeed(29);
    public static final ItemSand SAND = new ItemSand(30);
    public static final ItemWheat WHEAT = new ItemWheat(31);
    public static final ItemBread BREAD = new ItemBread(32);
    public static final ItemGem GEM = new ItemGem(33);
    public static final ItemFlower POPPY = new ItemFlower(34, "POPPY", Tile.POPPY);
    public static final ItemFlower DAISY = new ItemFlower(35, "DAISY", Tile.DAISY);

    public static final ItemCraftingBench CRAFTING_BENCH = new ItemCraftingBench(36);
    public static final ItemFurnace FURNACE = new ItemFurnace(37);
    public static final ItemOven OVEN = new ItemOven(38);
    public static final ItemChest CHEST = new ItemChest(39);
    public static final ItemAnvil ANVIL = new ItemAnvil(40);
    public static final ItemApple APPLE = new ItemApple(41);
    public static final ItemSapling SAPLING = new ItemSapling(42);

    public static final ItemSword ROCK_SWORD = new ItemSword(43, Recipe.STATION_BENCH, Material.ROCK);
    public static final ItemShovel ROCK_SHOVEL = new ItemShovel(44, Recipe.STATION_BENCH, Material.ROCK);
    public static final ItemPickaxe ROCK_PICKAXE = new ItemPickaxe(45, Recipe.STATION_BENCH, Material.ROCK);
    public static final ItemAxe ROCK_AXE = new ItemAxe(46, Recipe.STATION_BENCH, Material.ROCK);
    public static final ItemHoe ROCK_HOE = new ItemHoe(47, Recipe.STATION_BENCH, Material.ROCK);

    public static final ItemCoal COAL = new ItemCoal(48);
    public static final ItemGlass GLASS = new ItemGlass(49);
    public static final ItemPie PIE = new ItemPie(50);
    
    public static final ItemOre MITHRIL_ORE = new ItemOre(51, Metal.MITHRIL);
    public static final ItemIngot MITHRIL_INGOT = new ItemIngot(52, Metal.MITHRIL);

    public static final ItemSword MITHRIL_SWORD = new ItemSword(53, Recipe.STATION_ANVIL, Material.MITHRIL);
    public static final ItemShovel MITHRIL_SHOVEL = new ItemShovel(54, Recipe.STATION_ANVIL, Material.MITHRIL);
    public static final ItemPickaxe MITHRIL_PICKAXE = new ItemPickaxe(55, Recipe.STATION_ANVIL, Material.MITHRIL);
    public static final ItemAxe MITHRIL_AXE = new ItemAxe(56, Recipe.STATION_ANVIL, Material.MITHRIL);
    public static final ItemHoe MITHRIL_HOE = new ItemHoe(57, Recipe.STATION_ANVIL, Material.MITHRIL);

    public static final ItemSlime SLIME = new ItemSlime(58);
    public static final ItemLantern LANTERN = new ItemLantern(59);
    
    public static final ItemHelmet IRON_HELMET = new ItemHelmet(60, ItemArmor.TYPE_HELMET, Material.IRON);
    public static final ItemChestplate IRON_CHESTPLATE = new ItemChestplate(61, ItemArmor.TYPE_CHESTPLATE, Material.IRON);
    public static final ItemLeggings IRON_LEGGINGS = new ItemLeggings(62, ItemArmor.TYPE_LEGGINGS, Material.IRON);
    public static final ItemBoots IRON_BOOTS = new ItemBoots(63, ItemArmor.TYPE_BOOTS, Material.IRON);

    public static final ItemHelmet GOLD_HELMET = new ItemHelmet(64, ItemArmor.TYPE_HELMET, Material.GOLD);
    public static final ItemChestplate GOLD_CHESTPLATE = new ItemChestplate(65, ItemArmor.TYPE_CHESTPLATE, Material.GOLD);
    public static final ItemLeggings GOLD_LEGGINGS = new ItemLeggings(66, ItemArmor.TYPE_LEGGINGS, Material.GOLD);
    public static final ItemBoots GOLD_BOOTS = new ItemBoots(67, ItemArmor.TYPE_BOOTS, Material.GOLD);

    public static final ItemHelmet MITHRIL_HELMET = new ItemHelmet(68, ItemArmor.TYPE_HELMET, Material.MITHRIL);
    public static final ItemChestplate MITHRIL_CHESTPLATE = new ItemChestplate(69, ItemArmor.TYPE_CHESTPLATE, Material.MITHRIL);
    public static final ItemLeggings MITHRIL_LEGGINGS = new ItemLeggings(70, ItemArmor.TYPE_LEGGINGS, Material.MITHRIL);
    public static final ItemBoots MITHRIL_BOOTS = new ItemBoots(71, ItemArmor.TYPE_BOOTS, Material.MITHRIL);

    public static final ItemCactusGoo CACTUS_GOO = new ItemCactusGoo(72);
    public static final ItemBone BONE = new ItemBone(73);
    public static final ItemBonemeal BONEMEAL = new ItemBonemeal(74);

    // all available crafting recipes
    public static final List<Recipe> RECIPES = Arrays.stream(ITEMS)
            .filter(Objects::nonNull)
            .flatMap(i -> i.getRecipes().stream())
            .collect(Collectors.toList());

    // recipes for each crafting station
    public static final Map<Integer, List<Recipe>> STATION_RECIPES = Map.of(
        Recipe.STATION_INVENTORY,
        RECIPES.stream().filter(r -> (r.station & Recipe.STATION_INVENTORY) != 0).collect(Collectors.toList()),
        Recipe.STATION_BENCH,
        RECIPES.stream().filter(r -> (r.station & Recipe.STATION_BENCH) != 0).collect(Collectors.toList()),
        Recipe.STATION_ALL_CRAFTING,
        RECIPES.stream().filter(r -> (r.station & Recipe.STATION_ALL_CRAFTING) != 0).collect(Collectors.toList()),
        Recipe.STATION_OVEN,
        RECIPES.stream().filter(r -> (r.station & Recipe.STATION_OVEN) != 0).collect(Collectors.toList()),
        Recipe.STATION_FURNACE,
        RECIPES.stream().filter(r -> (r.station & Recipe.STATION_FURNACE) != 0).collect(Collectors.toList()),
        Recipe.STATION_ANVIL,
        RECIPES.stream().filter(r -> (r.station & Recipe.STATION_ANVIL) != 0).collect(Collectors.toList())
    );

    public final int id;

    public Item(int id) {
        this.id = id;
        assert(Item.ITEMS[id] == null);
        Item.ITEMS[id] = this;
    }

    public boolean use(ItemInstance instance, Level level, int x, int y, Entity e) {
        return false;
    }

    // render this item in the world
    public void render(ItemInstance instance, Level level, int x, int y) {
        Renderer.render(this.getIconX(), this.getIconY(), x, y, this.getColor(), Renderer.FLIP_NONE);
    }

    // render this item in a menu (as an icon)
    public void renderIcon(ItemInstance instance, int x, int y) {
        Renderer.render(this.getIconX(), this.getIconY(), x, y, this.getColor(), Renderer.FLIP_NONE);
    }

    // render this item being carried by an entity
    public void renderCarry(ItemInstance instance, Level Level, Entity e) {

    }

    public Collection<Recipe> getRecipes() {
        return List.of();
    }

    public boolean carry(Entity e) { return false; }

    public int getLightPower() { return 0; }

    public boolean isEquippable() { return false; }

    public boolean isStackable() {
        return true;
    }

    public boolean isDroppable() {
        return true;
    }

    public boolean showInMenu() {
        return true;
    }

    public abstract String getName();

    public abstract int getColor();

    public abstract int getIconX();

    public abstract int getIconY();
}
