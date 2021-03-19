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

    public Sign getSign(Location signLocation) {
        return signSet.stream()
                .filter(sign -> sign.getSignLocation().equals(signLocation))
                .findFirst()
                .orElse(null);
    }

    public boolean isValidSign(String[] lines) {
        if (lines[0] != null && lines[0].equalsIgnoreCase("[Teleportation]")) {
            if (lines[1] != null && lines[1].equalsIgnoreCase("warp")) {
                if (lines[3] != null && warpManager.getWarp(lines[3]) != null) {
                    return true;
                }
            }
        }
        return false;
    }

}
