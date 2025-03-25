package net.koteyka32k.burrowesp;

import net.koteyka32k.burrowesp.module.BurrowESP;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class BurrowESPPlugin extends Plugin {
    @Override
    public void onLoad() {
        final BurrowESP burrowESP = new BurrowESP();
        RusherHackAPI.getModuleManager().registerFeature(burrowESP);
    }

    @Override
    public void onUnload() {

    }
}
