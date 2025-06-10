package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class SpeedMultiplierScrollValueBehaviour extends ScrollValueBehaviour {


    public SpeedMultiplierScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
        super(label, be, slot);
        withFormatter(v -> String.valueOf(Math.abs(v)));
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        ImmutableList<Component> rows = ImmutableList.of(Component.literal("Speed"));
        ValueSettingsFormatter formatter = new ValueSettingsFormatter(this::formatSettings);
        return new ValueSettingsBoard(label, 3, 4, rows, formatter);
    }

    @Override
    public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings valueSetting, boolean ctrlHeld) {
        int value = Math.max(1, valueSetting.value());
        if (!valueSetting.equals(getValueSettings()))
            playFeedbackSound(this);
        setValue(valueSetting.row() == 0 ? -value : value);
    }

    @Override
    public ValueSettingsBehaviour.ValueSettings getValueSettings() {
        return new ValueSettingsBehaviour.ValueSettings(value < 0 ? 0 : 1, Math.abs(value));
    }

    public MutableComponent formatSettings(ValueSettingsBehaviour.ValueSettings settings) {
        return CreateLang.number(Math.max(1, Math.abs(settings.value())))
                .component();
    }

    @Override
    public String getClipboardKey() {
        return "Speed";
    }
}
