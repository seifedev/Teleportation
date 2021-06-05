package tech.seife.teleportation.signs;

import tech.seife.teleportation.warps.WarpManager;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public final class SignManager {

    private final Set<Sign> signSet;
    private final WarpManager warpManager;

    public SignManager(WarpManager warpManager) {
        this.warpManager = warpManager;
        signSet = new HashSet<>();
    }

    public void addSign(int id, Location signLocation, Location teleportLocation) {
        signSet.add(new Sign(id, signLocation, teleportLocation));
    }

    public boolean isValidSign(String[] lines) {
        if (lines[0] != null && lines[0].equalsIgnoreCase("[Teleportation]")) {
            if (lines[1] != null && lines[1].equalsIgnoreCase("warp")) {
                return lines[2] != null && warpManager.getWarp(lines[2]) != null;
            }
        }
        return false;
    }

}
