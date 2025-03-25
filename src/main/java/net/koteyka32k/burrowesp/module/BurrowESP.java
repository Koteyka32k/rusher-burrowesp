package net.koteyka32k.burrowesp.module;

import net.minecraft.core.BlockPos;
import org.rusherhack.client.api.events.render.EventRender3D;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.render.IRenderer3D;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.setting.NumberSetting;
import org.rusherhack.core.utils.ColorUtils;

import java.awt.*;
import java.util.ArrayList;


/**
 * The BurrowESP module.
 * <p>
 * <a href="https://github.com/RusherDevelopment/rusherhack-issues/issues/1834">RusherHack Issue</a>
 *
 * @author Koteyka32k
 * @since March 24, 2025
 */
public class BurrowESP extends ToggleableModule {
    /**
     * Settings
     */
    private final BooleanSetting fill = new BooleanSetting("Fill", "Whether to fill the ESP box.", true);
    private final BooleanSetting outline = new BooleanSetting("Outline", "Whether to outline the ESP box.", true);
    private final ColorSetting color = new ColorSetting("Color", "The color of the ESP.", new Color(0xff, 0x00, 0x4f, 0x32));
    private final BooleanSetting depthTest = new BooleanSetting("Depth Test", "Whether to render only on visible sides.", false);
    private final NumberSetting<Float> lineWidth = new NumberSetting<>("Line Width", "Thickness of the outline.", 3f, 1f, 10f).incremental(0.1f);

    /**
     * Constructor
     */
    public BurrowESP() {
        /* I feel that this could be used in combat but its also a render module. */
        super("BurrowESP", "ESP of blocks where a player is burrowed in.", ModuleCategory.COMBAT);

        registerSettings(
                fill,
                outline,
                color,
                depthTest,
                lineWidth
        );
    }

    /**
     * BurrowESP code
     */

    @Subscribe
    private void onRenderWorld(EventRender3D event) {
        IRenderer3D renderer = event.getRenderer();
        renderer.begin(event.getMatrixStack());

        getBurrowedBlocks().forEach(pos -> {
                renderer.setDepthTest(depthTest.getValue());
                renderer.setLineWidth(lineWidth.getValue());
                renderer.drawBox(pos, fill.getValue(), outline.getValue(), ColorUtils.transparency(color.getValueRGB(), color.getAlpha()));
            });

        renderer.end();
    }

    /**
     * Returns an array list of block positions where a player is burrowed into.
     */
    private ArrayList<BlockPos> getBurrowedBlocks() {
        ArrayList<BlockPos> output = new ArrayList<>();
        mc.level.players().forEach(player -> {
            if (isBlockGood(player.blockPosition())) {
                output.add(player.blockPosition());
            }
        });

        return output;
    }

    /**
     * Validates whether a block can be burrowed into.
     */
    private static boolean isBlockGood(BlockPos pos) {
        if (pos == null && mc.level == null) return false;

        boolean check1exists = !mc.level.getBlockState(pos).isAir();
        boolean check2canCollide = !mc.level.getBlockState(pos).getCollisionShape(mc.level, pos).isEmpty();
        boolean check3canOcclude = mc.level.getBlockState(pos).canOcclude();

        return check1exists && check2canCollide && check3canOcclude;
    }

}