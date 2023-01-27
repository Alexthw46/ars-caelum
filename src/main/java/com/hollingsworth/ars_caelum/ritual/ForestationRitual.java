package com.hollingsworth.ars_caelum.ritual;

import com.hollingsworth.ars_caelum.ArsCaelum;
import com.hollingsworth.ars_caelum.lib.RitualLang;
import com.hollingsworth.ars_caelum.ritual.features.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;

import java.util.List;

public class ForestationRitual extends FeaturePlacementRitual {


    @Override
    void addFeatures(List<IPlaceableFeature> features) {
        boolean isTaiga = getConsumedItems().stream().anyMatch(i -> i.getItem() == Items.BROWN_MUSHROOM);
        boolean isJungle = getConsumedItems().stream().anyMatch(i -> i.getItem() == Items.GLOW_BERRIES);
        if(isTaiga){
            lowerOffset = new BlockPos(0, -1, 0);
            features.add(new RandomTreeFeature(List.of(Blocks.SPRUCE_SAPLING.defaultBlockState()), 8, 0.8));
            features.add(new ConvertBlockFeature(0, 0.8, state -> state.is(BlockTags.DIRT) || state.getBlock() == Blocks.GRASS_BLOCK,state -> Blocks.PODZOL.defaultBlockState()));
            features.add(new PlaceBlockFeature(0, 0.1, () -> getWorld().random.nextFloat() < 0.3 ? Blocks.LARGE_FERN.defaultBlockState() : Blocks.FERN.defaultBlockState()));
            features.add(new PlaceBlockFeature(0, 0.1, Blocks.BROWN_MUSHROOM::defaultBlockState));
            features.add(new PlaceBlockFeature(0, 0.1, Blocks.GRASS::defaultBlockState));
            features.add(new PlaceBlockFeature(0, 0.01, () -> Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, getWorld().random.nextInt(4))));
        }else if(isJungle){
            features.add(new BigTreeFeature(Blocks.JUNGLE_SAPLING.defaultBlockState(), 12, 0.3));
            features.add(new RandomTreeFeature(List.of(Blocks.JUNGLE_SAPLING.defaultBlockState()), 6, 0.95));
            features.add(new PlaceBlockFeature(0, 0.1, Blocks.FERN::defaultBlockState));
            features.add(new PlaceBlockFeature(0, 0.1, Blocks.GRASS::defaultBlockState));
            features.add(new PlaceBlockFeature(0, 0.01, Blocks.MELON::defaultBlockState));
        } else {
            features.add(new RandomTreeFeature(List.of(Blocks.OAK_SAPLING.defaultBlockState(), Blocks.BIRCH_SAPLING.defaultBlockState()), 5, 0.8));
            features.add(new BonemealFeature(6, 0.8));
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(ArsCaelum.MODID, RitualLang.FORESTATION);
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        boolean isTaiga = getConsumedItems().stream().anyMatch(i -> i.getItem() == Items.BROWN_MUSHROOM);
        boolean isJungle = getConsumedItems().stream().anyMatch(i -> i.getItem() == Items.GLOW_BERRIES);
        boolean isVariant = isTaiga || isJungle;
        return super.canConsumeItem(stack) || (stack.getItem() == Items.BROWN_MUSHROOM && !isVariant) || (stack.getItem() == Items.GLOW_BERRIES && !isVariant);
    }

    @Override
    public String getLangName() {
        return "Forestation";
    }

    @Override
    public String getLangDescription() {
        return "Places grown Oak and Birch trees, and applies bonemeal in a 7x7 (circular) area. Augmenting with a source gem will increase the radius by 1 for each gem. Augmenting with a Brown Mushroom will convert the area to Podzol and spawn taiga resources. Augmenting with Glow Berries will spawn jungle resources.";
    }
}
